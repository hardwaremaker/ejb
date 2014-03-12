/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2014 HELIUM V IT-Solutions GmbH
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published 
 * by the Free Software Foundation, either version 3 of theLicense, or 
 * (at your option) any later version.
 * 
 * According to sec. 7 of the GNU Affero General Public License, version 3, 
 * the terms of the AGPL are supplemented with the following terms:
 * 
 * "HELIUM V" and "HELIUM 5" are registered trademarks of 
 * HELIUM V IT-Solutions GmbH. The licensing of the program under the 
 * AGPL does not imply a trademark license. Therefore any rights, title and
 * interest in our trademarks remain entirely with us. If you want to propagate
 * modified versions of the Program under the name "HELIUM V" or "HELIUM 5",
 * you may only do so if you have a written permission by HELIUM V IT-Solutions 
 * GmbH (to acquire a permission please contact HELIUM V IT-Solutions
 * at trademark@heliumv.com).
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact: developers@heliumv.com
 ******************************************************************************/
package com.lp.server.rechnung.fastlanereader;

import java.awt.Color;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.swing.JLabel;

import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.lieferschein.service.LieferscheinpositionDto;
import com.lp.server.rechnung.fastlanereader.generated.FLRRechnungPosition;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.rechnung.service.RechnungPositionDto;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MediastandardDto;
import com.lp.server.util.Facade;
import com.lp.server.util.fastlanereader.FLRSessionFactory;
import com.lp.server.util.fastlanereader.UseCaseHandler;
import com.lp.server.util.fastlanereader.service.query.FilterBlock;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.server.util.fastlanereader.service.query.SortierKriterium;
import com.lp.server.util.fastlanereader.service.query.TableInfo;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/**
 * <p>
 * Hier wird die FLR Funktionalitaet fuer die RechnungPosition implementiert.
 * Pro UseCase gibt es einen Handler.
 * </p>
 * <p>
 * Copright Logistik Pur GmbH (c) 2005
 * </p>
 * <p>
 * Erstellungsdatum 26.03.2005
 * </p>
 * <p>
 * </p>
 * 
 * @author Martin Bluehweis
 * @version 1.0
 */
public class PositionHandler extends UseCaseHandler {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Spalten-indizes, koennen in den subklassen auch adaptiert werden.
	protected static int SPALTE_I_ID = 0;
	protected static int SPALTE_NR = 1;
	protected static int SPALTE_MENGE = 2;
	protected static int SPALTE_EINHEIT = 3;
	protected static int SPALTE_IDENT = 4;
	protected static int SPALTE_BEZEICHNUNG = 5;
	protected static int SPALTE_PREIS = 6;
	protected static int SPALTE_ZEILENSUMME = 7;
	protected static int SPALTE_TEXT = 8;

	// welche Namen haben die FLR Spalten zum AuftragfunktionsprHandler im
	// Hibernate Mapping
	public static final String FLR_POSITION = " flrposition.";
	public static final String FLR_POSITION_FROM_CLAUSE = " from FLRRechnungPosition flrposition ";

	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {
		String mandantCNr = theClientDto.getMandant();
		Locale locUI = theClientDto.getLocUi();
		QueryResult result = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			int colCount = getTableInfo().getColumnClasses().length;
			int pageSize = PositionHandler.PAGE_SIZE;
			int startIndex = Math.max(rowIndex.intValue() - (pageSize / 2), 0);
			int endIndex = startIndex + pageSize - 1;

			session = factory.openSession();
			String queryString = this.getFromClause() + this.buildWhereClause()
					+ this.buildOrderByClause();
			Query query = session.createQuery(queryString);
			query.setFirstResult(startIndex);
			query.setMaxResults(pageSize);
			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();
			Object[][] rows = new Object[resultList.size()][colCount];
			String[] tooltipData = new String[resultList.size()];
			int iNachkommastellenMenge = getMandantFac()
					.getNachkommastellenMenge(mandantCNr);
			int iNachkommastellenPreis = getMandantFac()
					.getNachkommastellenPreisVK(mandantCNr);

			int row = 0;
			while (resultListIterator.hasNext()) {
				FLRRechnungPosition position = (FLRRechnungPosition) resultListIterator
						.next();
				// Spalte 1: immer die ID
				rows[row][SPALTE_I_ID] = position.getI_id();
				rows[row][SPALTE_NR] = getRechnungFac().getPositionNummer(
						position.getI_id());
				rows[row][SPALTE_MENGE] = getUIObjectBigDecimalNachkommastellen(
						position.getN_menge(), iNachkommastellenMenge);
				if (position.getEinheit_c_nr() != null) {
					rows[row][SPALTE_EINHEIT] = position.getEinheit_c_nr()
							.trim();
				} else {
					rows[row][SPALTE_EINHEIT] = null;
				}

				String sIdentnummer = "";
				if (position.getFlrartikel() != null) {
					sIdentnummer = position.getFlrartikel().getC_nr();
				}
				rows[row][SPALTE_IDENT] = sIdentnummer;

				String positionsart = position.getPositionsart_c_nr();
				if (positionsart.equals(LocaleFac.POSITIONSART_POSITION)) {
					BigDecimal bdGesamtpreis = getRechnungFac()
							.getGesamtpreisPosition(position.getI_id(),
									theClientDto);
					rows[row][SPALTE_PREIS] = getUIObjectBigDecimalNachkommastellen(
							bdGesamtpreis, iNachkommastellenPreis);
					if (position.getN_menge() == null) {
						if (bdGesamtpreis == null) {
							rows[row][SPALTE_ZEILENSUMME] = null;
						} else {
							rows[row][SPALTE_ZEILENSUMME] = bdGesamtpreis;
						}
					} else {
						if (bdGesamtpreis == null) {
							rows[row][SPALTE_ZEILENSUMME] = null;
						} else {
							rows[row][SPALTE_ZEILENSUMME] = getUIObjectBigDecimalNachkommastellen(
									bdGesamtpreis.multiply(position
											.getN_menge()),
									iNachkommastellenPreis);
						}
					}

				} else {
					rows[row][SPALTE_PREIS] = getUIObjectBigDecimalNachkommastellen(
							position.getN_nettoeinzelpreis(),
							iNachkommastellenPreis);
					if (position.getN_menge() == null) {
						if (position.getN_nettoeinzelpreis() == null) {
							rows[row][SPALTE_ZEILENSUMME] = null;
						} else {
							rows[row][SPALTE_ZEILENSUMME] = position
									.getN_nettoeinzelpreis();
						}
					} else {
						if (position.getN_nettoeinzelpreis() == null) {
							rows[row][SPALTE_ZEILENSUMME] = null;
						} else {

							BigDecimal verleih = new BigDecimal(1);
							if (position.getFlrverleih() != null) {
								verleih = new BigDecimal(position
										.getFlrverleih().getF_faktor());
							}

							rows[row][SPALTE_ZEILENSUMME] = getUIObjectBigDecimalNachkommastellen(
									position.getN_nettoeinzelpreis().multiply(
											position.getN_menge().multiply(
													verleih)),
									iNachkommastellenPreis);
						}
					}
				}
				// in der Spalte Bezeichnung koennen verschiedene Dinge stehen
				String sBezeichnung = null;

				if (position.getTyp_c_nr() == null) {
					if (positionsart.equals(LocaleFac.POSITIONSART_BETRIFFT)) {
						sBezeichnung = "["
								+ getTextRespectUISpr("rechnung.betrifft",
										mandantCNr, locUI) + "] "
								+ position.getC_bez();
					} else if (positionsart
							.equals(LocaleFac.POSITIONSART_SEITENUMBRUCH)) {
						sBezeichnung = "["
								+ getTextRespectUISpr("rechnung.seitenumbruch",
										mandantCNr, locUI) + "]";
					} else if (positionsart
							.equals(LocaleFac.POSITIONSART_LEERZEILE)) {
						sBezeichnung = "["
								+ getTextRespectUISpr("rechnung.leerzeile",
										mandantCNr, locUI) + "]";
					} else if (positionsart.equals(
							LocaleFac.POSITIONSART_ENDSUMME)) {
						sBezeichnung = "["
								+ getTextRespectUISpr("lp.endsumme",
										mandantCNr, locUI) + "]";
					} else if (positionsart
							.equals(LocaleFac.POSITIONSART_ZWISCHENSUMME)) {
						sBezeichnung = "["
								+ getTextRespectUISpr("rechnung.zwischensumme",
										mandantCNr, locUI) + "]";
					} else if (positionsart
							.equals(LocaleFac.POSITIONSART_INTELLIGENTE_ZWISCHENSUMME)) {
						sBezeichnung = "["
								+ getTextRespectUISpr("rechnung.intelligentezwischensumme",
										mandantCNr, locUI) + "] " 
								+ position.getC_bez() ;
					} else if (positionsart.equals(LocaleFac.POSITIONSART_TEXTEINGABE)) {
						RechnungPositionDto rePosDto = getRechnungFac()
								.rechnungPositionFindByPrimaryKey(
										position.getI_id());
						sBezeichnung = rePosDto.getXTextinhalt();
					} else if (positionsart
							.equals(LocaleFac.POSITIONSART_URSPRUNGSLAND)) {
						sBezeichnung = "["
								+ getTextRespectUISpr("rechnung.ursprung",
										mandantCNr, locUI) + "] "
								+ position.getC_bez();
					} else if (positionsart
							.equals(LocaleFac.POSITIONSART_TEXTBAUSTEIN)) {
						RechnungPositionDto rePosDto = getRechnungFac()
								.rechnungPositionFindByPrimaryKey(
										position.getI_id());
						MediastandardDto mDto = getMediaFac()
								.mediastandardFindByPrimaryKey(
										rePosDto.getMediastandardIId());
						sBezeichnung = mDto.getCNr();
					} else if (positionsart
							.equals(LocaleFac.POSITIONSART_HANDEINGABE)) {
						sBezeichnung = position.getC_bez();
					} else if (positionsart
							.equals(LocaleFac.POSITIONSART_IDENT)) {
						if (position.getC_bez() != null) {
							sBezeichnung = position.getC_bez();
						} else {
							ArtikelDto a = getArtikelFac()
									.artikelFindByPrimaryKey(
											position.getArtikel_i_id(),
											theClientDto);
							sBezeichnung = a.formatBezeichnung();
						}
					} else if (positionsart
							.equals(LocaleFac.POSITIONSART_LIEFERSCHEIN)) {
						RechnungPositionDto dto = getRechnungFac()
								.rechnungPositionFindByPrimaryKey(
										position.getI_id());
						LieferscheinDto ls = getLieferscheinFac()
								.lieferscheinFindByPrimaryKey(
										dto.getLieferscheinIId(), theClientDto);
						sBezeichnung = ""
								+ getTextRespectUISpr("rechnung.lieferschein",
										mandantCNr, locUI) + " " + ls.getCNr();

						//PJ 17802 -> lt. WH auskommentieren
						// Positionen des Lieferscheins auf Unterpreis pruefen
						/*LieferscheinpositionDto[] lsPos = getLieferscheinpositionFac()
								.lieferscheinpositionFindByLieferscheinIId(
										ls.getIId());
						boolean bUnterdeckung = false;
						for (int i = 0; i < lsPos.length; i++) {
							if (lsPos[i].getArtikelIId() != null) {
								if (!lsPos[i].getPositionsartCNr().equals(
										LocaleFac.POSITIONSART_HANDEINGABE))
									bUnterdeckung = getVkPreisfindungFac()
											.liegtVerkaufspreisUnterMinVerkaufspreis(
													lsPos[i].getArtikelIId(),
													ls.getLagerIId(),
													lsPos[i].getNEinzelpreis(),
													ls.getFWechselkursmandantwaehrungzubelegwaehrung(),
													lsPos[i].getNMenge(),
													theClientDto);
								if (bUnterdeckung) {
									JLabel jlaUnterdeckung = new JLabel("!!!");
									jlaUnterdeckung
											.setToolTipText("Der Lieferschein enth\u00E4lt Positionen, die den Mindestverkaufspreis unterschreiten");
									jlaUnterdeckung.setForeground(Color.RED);
									rows[row][SPALTE_EINHEIT] = jlaUnterdeckung
											.getText();
									sBezeichnung = sBezeichnung
											+ " (mit Unterpreisen)";
									break;
								}
							}
						}*/

						// PJ 16690
						sBezeichnung += "  ("
								+ Helper.formatDatum(ls.getTBelegdatum(),
										theClientDto.getLocUi()) + ")";
						
						
						if(getLieferscheinFac().enthaeltLieferscheinNullPreise(ls.getIId())){
							sBezeichnung += " "+getTextRespectUISpr("rech.lspos.nullpreiseenthalten",
									mandantCNr, locUI);
						}
						

					}
				} else {
					if (position.getPosition_i_id() == null) {
						if (positionsart
								.equals(LocaleFac.POSITIONSART_POSITION)) {
							sBezeichnung = getTextRespectUISpr("lp.position",
									mandantCNr, locUI);
							if (position.getC_bez() != null) {
								sBezeichnung = sBezeichnung + " "
										+ position.getC_bez();
							}
						}
					} else {
						if (positionsart
								.equals(LocaleFac.POSITIONSART_POSITION)) {
							if (position.getTyp_c_nr().equals(
									LocaleFac.POSITIONTYP_EBENE1)) {
								sBezeichnung = getTextRespectUISpr(
										"lp.position", mandantCNr, locUI);
							} else {
								sBezeichnung = "  "
										+ getTextRespectUISpr("lp.position",
												mandantCNr, locUI);
							}
							if (position.getC_bez() != null) {
								sBezeichnung = sBezeichnung + " "
										+ position.getC_bez();
							}
						} else {
							if (positionsart
									.equals(LocaleFac.POSITIONSART_TEXTEINGABE)) {
								if (position.getTyp_c_nr().equals(
										LocaleFac.POSITIONTYP_EBENE1)) {
									if (position.getX_textinhalt() != null
											&& position.getX_textinhalt()
													.length() > 0) {
										sBezeichnung = " "
												+ Helper.strippHTML(position
														.getX_textinhalt());
									}
								}
							} else if (positionsart
									.equals(LocaleFac.POSITIONSART_SEITENUMBRUCH)) {
								sBezeichnung = "["
										+ getTextRespectUISpr(
												"lp.seitenumbruch", mandantCNr,
												locUI) + "]";
							} else if (positionsart
									.equals(LocaleFac.POSITIONSART_ENDSUMME)) {
								sBezeichnung = "["
										+ getTextRespectUISpr("lp.endsumme",
												mandantCNr, locUI) + "]";
							} else if (positionsart
									.equals(LocaleFac.POSITIONSART_POSITION)) {
								sBezeichnung = getTextRespectUISpr(
										"lp.position", mandantCNr, locUI)
										+ " "
										+ position.getC_bez();
							} else if (positionsart
									.equals(LocaleFac.POSITIONSART_TEXTBAUSTEIN)) {
								RechnungPositionDto rePosDto = getRechnungFac()
										.rechnungPositionFindByPrimaryKey(
												position.getI_id());
								MediastandardDto mDto = getMediaFac()
										.mediastandardFindByPrimaryKey(
												rePosDto.getMediastandardIId());
								sBezeichnung = mDto.getCNr();

							} else if (positionsart
									.equals(LocaleFac.POSITIONSART_IDENT)) {
								if (position.getTyp_c_nr().equals(
										LocaleFac.POSITIONTYP_EBENE1)) {
									// die sprachabhaengig Artikelbezeichnung
									// anzeigen
									sBezeichnung = "  "
											+ getArtikelFac()
													.formatArtikelbezeichnungEinzeiligOhneExc(
															position.getFlrartikel()
																	.getI_id(),
															theClientDto
																	.getLocUi());
								} else if (position.getTyp_c_nr().equals(
										LocaleFac.POSITIONTYP_EBENE2)) {
									// die sprachabhaengig Artikelbezeichnung
									// anzeigen
									sBezeichnung = "    "
											+ getArtikelFac()
													.formatArtikelbezeichnungEinzeiligOhneExc(
															position.getFlrartikel()
																	.getI_id(),
															theClientDto
																	.getLocUi());
								}
							} else {
								if (positionsart
										.equals(LocaleFac.POSITIONSART_HANDEINGABE)) {
									if (position.getTyp_c_nr().equals(
											LocaleFac.POSITIONTYP_EBENE1)) {
										if (position.getC_bez() != null) {
											sBezeichnung = " "
													+ position.getC_bez();
										}
									} else if (position.getTyp_c_nr().equals(
											LocaleFac.POSITIONTYP_EBENE2)) {
										if (position.getC_bez() != null) {
											sBezeichnung = "  "
													+ position.getC_bez();
										}
									}
								}
							}
						}
					}
				}
				rows[row][SPALTE_BEZEICHNUNG] = sBezeichnung;

				// Text
				if (position.getX_textinhalt() != null
						&& !position.getX_textinhalt().equals("")) {
					rows[row][SPALTE_TEXT] = new Boolean(true);
					String text = position.getX_textinhalt();
					text = text.replaceAll("\n", "<br>");
					text = "<html>" + text + "</html>";
					tooltipData[row] = text;
				} else {
					rows[row][SPALTE_TEXT] = new Boolean(false);
				}

				row++;
			}
			result = new QueryResult(rows, this.getRowCount(), startIndex,
					endIndex, 0, tooltipData);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
		} finally {
			closeSession(session);
		}
		return result;
	}

	/**
	 * gets the total number of rows represented by the current query.
	 * 
	 * @see UseCaseHandler#getRowCountFromDataBase()
	 * @return int
	 */
	protected long getRowCountFromDataBase() {
		long rowCount = 0;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			session = factory.openSession();
			String queryString = "select count(*) " + this.getFromClause()
					+ this.buildWhereClause();
			Query query = session.createQuery(queryString);
			List<?> rowCountResult = query.list();
			if (rowCountResult != null && rowCountResult.size() > 0) {
				rowCount = ((Long) rowCountResult.get(0)).longValue();
			}
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
		} finally {
			closeSession(session);
		}
		return rowCount;
	}

	/**
	 * builds the where clause of the HQL (Hibernate Query Language) statement
	 * using the current query.
	 * 
	 * @return the HQL where clause.
	 */
	private String buildWhereClause() {
		StringBuffer where = new StringBuffer("");

		if (this.getQuery() != null && this.getQuery().getFilterBlock() != null
				&& this.getQuery().getFilterBlock().filterKrit != null) {

			FilterBlock filterBlock = this.getQuery().getFilterBlock();
			FilterKriterium[] filterKriterien = this.getQuery()
					.getFilterBlock().filterKrit;
			String booleanOperator = filterBlock.boolOperator;

			boolean filterAdded = false;

			for (int i = 0; i < filterKriterien.length; i++) {
				if (filterKriterien[i].isKrit) {
					if (filterAdded) {
						where.append(" " + booleanOperator);
					}
					filterAdded = true;
					where.append(FLR_POSITION + filterKriterien[i].kritName);
					where.append(" " + filterKriterien[i].operator);
					where.append(" " + filterKriterien[i].value);
				}
			}
			if (filterAdded) {
				where.insert(0, " WHERE");
			}
		}

		return where.toString();
	}

	/**
	 * builds the HQL (Hibernate Query Language) order by clause using the sort
	 * criterias contained in the current query.
	 * 
	 * @return the HQL order by clause.
	 */
	private String buildOrderByClause() {
		StringBuffer orderBy = new StringBuffer("");
		if (this.getQuery() != null) {
			SortierKriterium[] kriterien = this.getQuery().getSortKrit();
			boolean sortAdded = false;
			if (kriterien != null && kriterien.length > 0) {
				for (int i = 0; i < kriterien.length; i++) {
					if (!kriterien[i].kritName
							.endsWith(Facade.NICHT_SORTIERBAR)) {
						if (kriterien[i].isKrit) {
							if (sortAdded) {
								orderBy.append(", ");
							}
							sortAdded = true;
							orderBy.append(FLR_POSITION + kriterien[i].kritName);
							orderBy.append(" ");
							orderBy.append(kriterien[i].value);
						}
					}
				}
			} else {
				// no sort criteria found, add default sort
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(FLR_POSITION
						+ RechnungFac.FLR_RECHNUNGPOSITION_I_SORT + " ASC ");
				sortAdded = true;
			}
			if (orderBy.indexOf(FLR_POSITION
					+ RechnungFac.FLR_RECHNUNGPOSITION_I_ID) < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(FLR_POSITION
						+ RechnungFac.FLR_RECHNUNGPOSITION_I_ID + " ");
				sortAdded = true;
			}
			if (sortAdded) {
				orderBy.insert(0, " ORDER BY ");
			}
		}
		return orderBy.toString();
	}

	/**
	 * get the basic from clause for the HQL statement.
	 * 
	 * @return the from clause.
	 */
	private String getFromClause() {
		return FLR_POSITION_FROM_CLAUSE;
	}

	/**
	 * sorts the data described by the current query using the specified sort
	 * criterias. The current query is also updated with the new sort criterias.
	 * 
	 * @see UseCaseHandler#sort(SortierKriterium[], Object)
	 * @throws EJBExceptionLP
	 * @param sortierKriterien
	 *            SortierKriterium[]
	 * @param selectedId
	 *            Object
	 * @return QueryResult
	 */
	public QueryResult sort(SortierKriterium[] sortierKriterien,
			Object selectedId) throws EJBExceptionLP {
		this.getQuery().setSortKrit(sortierKriterien);

		QueryResult result = null;
		int rowNumber = 0;

		if (selectedId != null && ((Integer) selectedId).intValue() >= 0) {
			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = null;

			try {
				session = factory.openSession();
				String queryString = "select" + FLR_POSITION
						+ RechnungFac.FLR_RECHNUNGPOSITION_I_ID
						+ FLR_POSITION_FROM_CLAUSE + this.buildWhereClause()
						+ this.buildOrderByClause();
				Query query = session.createQuery(queryString);
				ScrollableResults scrollableResult = query.scroll();
				if (scrollableResult != null) {
					scrollableResult.beforeFirst();
					while (scrollableResult.next()) {
						Integer id = (Integer) scrollableResult.getInteger(0);
						if (selectedId.equals(id)) {
							rowNumber = scrollableResult.getRowNumber();
							break;
						}
					}
				}
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
			} finally {
				closeSession(session);
			}
		}

		if (rowNumber < 0 || rowNumber >= this.getRowCount()) {
			rowNumber = 0;
		}

		result = this.getPageAt(new Integer(rowNumber));
		result.setIndexOfSelectedRow(rowNumber);

		return result;
	}

	/**
	 * gets information about the Kontentable.
	 * 
	 * @return TableInfo
	 */
	public TableInfo getTableInfo() {
		try {
			if (super.getTableInfo() == null) {
				String mandantCNr = theClientDto.getMandant();
				Locale locUI = theClientDto.getLocUi();

				int iNachkommastellenMenge = getMandantFac()
						.getNachkommastellenMenge(mandantCNr);
				int iNachkommastellenPreis = getMandantFac()
						.getNachkommastellenPreisVK(mandantCNr);

				setTableInfo(new TableInfo(
						new Class[] {
								Integer.class,
								Integer.class,
								super.getUIClassBigDecimalNachkommastellen(iNachkommastellenMenge),
								String.class,
								String.class,
								String.class,
								super.getUIClassBigDecimalNachkommastellen(iNachkommastellenPreis),
								super.getUIClassBigDecimalNachkommastellen(iNachkommastellenPreis),
								Boolean.class },
						new String[] {
								"Id",
								getTextRespectUISpr("lp.nr", mandantCNr, locUI),
								getTextRespectUISpr("lp.menge", mandantCNr,
										locUI),
								getTextRespectUISpr("lp.einh", mandantCNr,
										locUI),
								getTextRespectUISpr("lp.ident",
										theClientDto.getMandant(),
										theClientDto.getLocUi()),
								getTextRespectUISpr("lp.bezeichnung",
										mandantCNr, locUI),
								getTextRespectUISpr("lp.preis", mandantCNr,
										locUI),
								getTextRespectUISpr("lp.zeilensumme",
										mandantCNr, locUI),
								getTextRespectUISpr("lp.text", mandantCNr,
										locUI) },
						new int[] {
								QueryParameters.FLR_BREITE_SHARE_WITH_REST, // diese
								// Spalte
								// wird
								// ausgeblendet
								QueryParameters.FLR_BREITE_S,
								getUIBreiteAbhaengigvonNachkommastellen(
										QueryParameters.MENGE,
										iNachkommastellenMenge), // Menge
								5, // z.B. Woche
								getUIBreiteIdent(), //ident
								QueryParameters.FLR_BREITE_SHARE_WITH_REST, // Breite
								// variabel
								getUIBreiteAbhaengigvonNachkommastellen(
										QueryParameters.PREIS,
										iNachkommastellenPreis), // Preis
								getUIBreiteAbhaengigvonNachkommastellen(
										QueryParameters.PREIS,
										iNachkommastellenPreis), 3 },
						new String[] {
								RechnungFac.FLR_RECHNUNGPOSITION_I_ID,
								Facade.NICHT_SORTIERBAR,
								RechnungFac.FLR_RECHNUNGPOSITION_N_MENGE,
								RechnungFac.FLR_RECHNUNGPOSITION_EINHEIT_C_NR,
								Facade.NICHT_SORTIERBAR,
								RechnungFac.FLR_RECHNUNGPOSITION_C_BEZ,
								RechnungFac.FLR_RECHNUNGPOSITION_N_NETTOEINZELPREIS,
								Facade.NICHT_SORTIERBAR,
								Facade.NICHT_SORTIERBAR }));

			}
			return super.getTableInfo();
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}
	}
}
