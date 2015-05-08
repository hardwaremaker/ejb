/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2015 HELIUM V IT-Solutions GmbH
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
package com.lp.server.lieferschein.fastlanereader;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.lieferschein.fastlanereader.generated.FLRLieferscheinposition;
import com.lp.server.lieferschein.service.LieferscheinDto;
import com.lp.server.lieferschein.service.LieferscheinpositionDto;
import com.lp.server.lieferschein.service.LieferscheinpositionFac;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocNodeLieferscheinPosition;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.service.LocaleFac;
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
 * Hier wird die FLR Funktionalitaet fuer die Lieferscheinpositionen
 * implementiert. Pro UseCase gibt es einen Handler.
 * </p>
 * <p>
 * Copright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * <p>
 * Erstellungsdatum 2004-10-21
 * </p>
 * <p>
 * </p>
 * 
 * @author Uli Walch
 * @version 1.0
 */

public class LieferscheinpositionHandler extends UseCaseHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String FLR_LIEFERSCHEINPOSITION = "flrlieferscheinposition.";
	public static final String FLR_LIEFERSCHEINPOSITION_FROM_CLAUSE = " from FLRLieferscheinposition flrlieferscheinposition ";

	/**
	 * gets the data page for the specified row using the current query. The row
	 * at rowIndex will be located in the middle of the page.
	 * 
	 * @param rowIndex
	 *            diese Zeile soll selektiert sein
	 * @return QueryResult das Ergebnis der Abfrage
	 * @throws EJBExceptionLP
	 * @see UseCaseHandler#getPageAt(java.lang.Integer)
	 */
	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {
		QueryResult result = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		String mandantCNr = theClientDto.getMandant();
		Locale locUI = theClientDto.getLocUi();
		try {
			int colCount = getTableInfo().getColumnClasses().length;
			int pageSize = LieferscheinpositionHandler.PAGE_SIZE;
			int startIndex = Math.max(rowIndex.intValue() - (pageSize / 2), 0);
			int endIndex = startIndex + pageSize - 1;

			session = factory.openSession();
			String queryString = this.getFromClause() + this.buildWhereClause()
					+ this.buildOrderByClause();
			// myLogger.info("HQL: " + queryString);
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
			// darf Preise sehen.
			final boolean bDarfPreiseSehen = getTheJudgeFac().hatRecht(
					RechteFac.RECHT_LP_DARF_PREISE_SEHEN_VERKAUF, theClientDto);

			while (resultListIterator.hasNext()) {
				FLRLieferscheinposition position = (FLRLieferscheinposition) resultListIterator
						.next();
				int col = 0;
				Integer pNummer = null;
				rows[row][col++] = position.getI_id();
				rows[row][col++] = getLieferscheinpositionFac().getLSPositionNummer(position.getI_id()) ;
				
				if (position.getAuftragposition_i_id() != null)
					pNummer = getAuftragpositionFac().getPositionNummer(
							position.getAuftragposition_i_id());
				else {
					if (position.getFlrlieferschein().getFlrauftrag() == null)
						pNummer = getLieferscheinpositionFac()
								.getPositionNummer(position.getI_id());
				}
				rows[row][col++] = pNummer;
				rows[row][col++] = getUIObjectBigDecimalNachkommastellen(
						position.getN_menge(), iNachkommastellenMenge);
				rows[row][col++] = position.getEinheit_c_nr() == null ? ""
						: position.getEinheit_c_nr().trim();

				String sIdentnummer = "";
				if (position.getFlrartikel() != null) {
					sIdentnummer = position.getFlrartikel().getC_nr();
				}
				rows[row][col++] = sIdentnummer;

				// in der Spalte Bezeichnung koennen verschiedene Dinge stehen
				String sBezeichnung = null;
				if (position.getTyp_c_nr() == null) {
					if (position.getPositionsart_c_nr().equals(
							LocaleFac.POSITIONSART_TEXTEINGABE)) {
						if (position.getC_textinhalt() != null
								&& position.getC_textinhalt().length() > 0) {
							sBezeichnung = Helper.strippHTML(position
									.getC_textinhalt());
						}
					} else if (position.getPositionsart_c_nr().equals(
							LocaleFac.POSITIONSART_SEITENUMBRUCH)) {
						sBezeichnung = "["
								+ getTextRespectUISpr("lp.seitenumbruch",
										mandantCNr, locUI) + "]";
					} else if (position.getPositionsart_c_nr().equals(
							LocaleFac.POSITIONSART_ENDSUMME)) {
						sBezeichnung = "["
								+ getTextRespectUISpr("lp.endsumme",
										mandantCNr, locUI) + "]";
					} else if (position.getPositionsart_c_nr().equals(
							LocaleFac.POSITIONSART_POSITION)) {
						sBezeichnung = getTextRespectUISpr("lp.position",
								mandantCNr, locUI) + " " + position.getC_bez();
					} else if (position.getPositionsart_c_nr().equals(
							LocaleFac.POSITIONSART_TEXTBAUSTEIN)) {
						sBezeichnung = position.getFlrmediastandard().getC_nr();
					} else if (position.getPositionsart_c_nr().equals(
							LocaleFac.POSITIONSART_IDENT)) {
						// die sprachabhaengig Artikelbezeichnung anzeigen
						sBezeichnung = getArtikelFac()
								.formatArtikelbezeichnungEinzeiligOhneExc(
										position.getFlrartikel().getI_id(),
										theClientDto.getLocUi());
					} else if (position.getPositionsart_c_nr()
							.equals(LocaleFac.POSITIONSART_INTELLIGENTE_ZWISCHENSUMME)) {
						sBezeichnung = "["
								+ getTextRespectUISpr("lieferschein.intelligentezwischensumme",
										mandantCNr, locUI) + "] " 
								+ position.getC_bez() ;
					} else {
						// die restlichen Positionsarten
						if (position.getC_bez() != null) {
							sBezeichnung = position.getC_bez();
						}
					}
				} else {
					if (position.getPosition_i_id() == null) {
						if (position.getPositionsart_c_nr().equals(
								LocaleFac.POSITIONSART_POSITION)) {
							sBezeichnung = getTextRespectUISpr("lp.position",
									mandantCNr, locUI);
							if (position.getC_bez() != null) {
								sBezeichnung = sBezeichnung + " "
										+ position.getC_bez();
							}
						}
					} else {
						if (position.getPositionsart_c_nr().equals(
								LocaleFac.POSITIONSART_POSITION)) {
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
							if (position.getPositionsart_c_nr().equals(
									LocaleFac.POSITIONSART_TEXTEINGABE)) {
								if (position.getTyp_c_nr().equals(
										LocaleFac.POSITIONTYP_EBENE1)) {
									if (position.getC_textinhalt() != null
											&& position.getC_textinhalt()
													.length() > 0) {
										sBezeichnung = " "
												+ Helper.strippHTML(position
														.getC_textinhalt());
									}
								}
							} else if (position.getPositionsart_c_nr().equals(
									LocaleFac.POSITIONSART_SEITENUMBRUCH)) {
								sBezeichnung = "["
										+ getTextRespectUISpr(
												"lp.seitenumbruch", mandantCNr,
												locUI) + "]";
							} else if (position.getPositionsart_c_nr().equals(
									LocaleFac.POSITIONSART_ENDSUMME)) {
								sBezeichnung = "["
										+ getTextRespectUISpr("lp.endsumme",
												mandantCNr, locUI) + "]";
							} else if (position.getPositionsart_c_nr().equals(
									LocaleFac.POSITIONSART_POSITION)) {
								sBezeichnung = getTextRespectUISpr(
										"lp.position", mandantCNr, locUI)
										+ " "
										+ position.getC_bez();
							} else if (position.getPositionsart_c_nr().equals(
									LocaleFac.POSITIONSART_TEXTBAUSTEIN)) {
								sBezeichnung = position.getFlrmediastandard()
										.getC_nr();
							} else if (position.getPositionsart_c_nr().equals(
									LocaleFac.POSITIONSART_IDENT)) {
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
								if (position.getPositionsart_c_nr().equals(
										LocaleFac.POSITIONSART_HANDEINGABE)) {
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
				rows[row][col++] = sBezeichnung;

				if (bDarfPreiseSehen) {
					if (position.getPositionsart_c_nr().equals(
							LocaleFac.POSITIONSART_POSITION)) {
						BigDecimal bdGesamtpreis = getLieferscheinpositionFac()
								.getGesamtpreisPosition(position.getI_id(),
										theClientDto);
						rows[row][col++] = getUIObjectBigDecimalNachkommastellen(
								bdGesamtpreis, iNachkommastellenPreis);
						if (bdGesamtpreis == null
								|| position.getN_menge() == null) {
							rows[row][col++] = null;
						} else {
							rows[row][col++] = getUIObjectBigDecimalNachkommastellen(
									bdGesamtpreis.multiply(position
											.getN_menge()),
									iNachkommastellenPreis);
						}
					} else {

						rows[row][col++] = getUIObjectBigDecimalNachkommastellen(
								position.getN_nettogesamtpreis(),
								iNachkommastellenPreis);
						if (position.getN_nettogesamtpreis() == null
								|| position.getN_menge() == null) {
							rows[row][col++] = null;
						} else {
							
							BigDecimal verleih=new BigDecimal(1);
							if(position.getFlrverleih()!=null){
								verleih=new BigDecimal(position.getFlrverleih().getF_faktor());
							}
							
							rows[row][col++] = getUIObjectBigDecimalNachkommastellen(
									position.getN_nettogesamtpreis().multiply(
											position.getN_menge().multiply(verleih)),
									iNachkommastellenPreis);
						}
					}
				} else {
					rows[row][col++] = null;
					rows[row][col++] = null;
				}
				// Text
				if (position.getC_textinhalt() != null
						&& !position.getC_textinhalt().equals("")) {
					rows[row][col++] = new Boolean(true);
					String text = position.getC_textinhalt();
					text = text.replaceAll("\n", "<br>");
					text = "<html>" + text + "</html>";
					tooltipData[row] = text;
				} else {
					rows[row][col++] = new Boolean(false);
				}

				row++;
			}
			result = new QueryResult(rows, this.getRowCount(), startIndex,
					endIndex, 0, tooltipData);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
		} finally {
			try {
				session.close();
			} catch (HibernateException he) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, he);
			}
		}
		return result;
	}

	/**
	 * gets the total number of rows represented by the current query.
	 * 
	 * @return int die Anzehl der Zeilen im Ergebnis
	 * @see UseCaseHandler#getRowCountFromDataBase()
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
			if (session != null) {
				try {
					session.close();
				} catch (HibernateException he) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_HIBERNATE,
							he);
				}
			}
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
					where.append(" " + FLR_LIEFERSCHEINPOSITION
							+ filterKriterien[i].kritName);
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
							orderBy.append(FLR_LIEFERSCHEINPOSITION
									+ kriterien[i].kritName);
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
				orderBy.append(FLR_LIEFERSCHEINPOSITION)
						.append(LieferscheinpositionFac.FLR_LIEFERSCHEINPOSITION_I_SORT)
						.append(" ASC ");
				sortAdded = true;
			}
			if (orderBy.indexOf(FLR_LIEFERSCHEINPOSITION
					+ LieferscheinpositionFac.FLR_LIEFERSCHEINPOSITION_I_ID) < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" ")
						.append(FLR_LIEFERSCHEINPOSITION)
						.append(LieferscheinpositionFac.FLR_LIEFERSCHEINPOSITION_I_ID)
						.append(" ");
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
		return FLR_LIEFERSCHEINPOSITION_FROM_CLAUSE;
	}

	/**
	 * sorts the data described by the current query using the specified sort
	 * criterias. The current query is also updated with the new sort criterias.
	 * 
	 * @param sortierKriterien
	 *            nach diesen Kriterien wird das Ergebnis sortiert
	 * @param selectedId
	 *            auf diesem Datensatz soll der Cursor stehen
	 * @return QueryResult das Ergebnis der Abfrage
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 * @see UseCaseHandler#sort(SortierKriterium[], Object)
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
				String queryString = "select " + FLR_LIEFERSCHEINPOSITION
						+ LieferscheinpositionFac.FLR_LIEFERSCHEINPOSITION_I_ID
						+ FLR_LIEFERSCHEINPOSITION_FROM_CLAUSE
						+ this.buildWhereClause() + this.buildOrderByClause();
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
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
			} finally {
				try {
					session.close();
				} catch (HibernateException he) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER, he);
				}
			}
		}

		if (rowNumber < 0 || rowNumber >= this.getRowCount()) {
			rowNumber = 0;
		}

		result = this.getPageAt(new Integer(rowNumber));
		result.setIndexOfSelectedRow(rowNumber);

		return result;
	}

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
								Integer.class,
								super.getUIClassBigDecimalNachkommastellen(iNachkommastellenMenge),
								String.class,
								String.class,
								String.class,
								super.getUIClassBigDecimalNachkommastellen(iNachkommastellenPreis),
								super.getUIClassBigDecimalNachkommastellen(iNachkommastellenPreis),
								Boolean.class },
						new String[] {
								"i_id", // hier steht immer i_id oder c_nr
								getTextRespectUISpr("lp.lieferschein.nr", mandantCNr, locUI),
								getTextRespectUISpr("lp.nr", mandantCNr, locUI),								
								getTextRespectUISpr("lp.menge", mandantCNr,
										locUI),
								getTextRespectUISpr("lp.einheit", mandantCNr,
										locUI),
								getTextRespectUISpr("lp.ident", mandantCNr,
										locUI),
								getTextRespectUISpr("lp.bezeichnung",
										mandantCNr, locUI),
								getTextRespectUISpr("lp.preis", mandantCNr,
										locUI),
								getTextRespectUISpr("lp.zeilensumme",
										mandantCNr, locUI),
								getTextRespectUISpr("lp.text",
										theClientDto.getMandant(),
										theClientDto.getLocUi()) },
						new int[] {
								QueryParameters.FLR_BREITE_SHARE_WITH_REST, // diese
								QueryParameters.FLR_BREITE_S,
								QueryParameters.FLR_BREITE_S,
								// Spalte
								// wird
								// ausgeblendet
								getUIBreiteAbhaengigvonNachkommastellen(
										QueryParameters.MENGE,
										iNachkommastellenMenge), // Menge
								QueryParameters.FLR_BREITE_XS,
								getUIBreiteIdent(), //ident
								QueryParameters.FLR_BREITE_SHARE_WITH_REST, // Breite
								// variabel
								getUIBreiteAbhaengigvonNachkommastellen(
										QueryParameters.PREIS,
										iNachkommastellenPreis), // Preis
								getUIBreiteAbhaengigvonNachkommastellen(
										QueryParameters.PREIS,
										iNachkommastellenPreis), 4 },
						new String[] {
								LieferscheinpositionFac.FLR_LIEFERSCHEINPOSITION_I_ID,
								Facade.NICHT_SORTIERBAR,
								Facade.NICHT_SORTIERBAR,								
								LieferscheinpositionFac.FLR_LIEFERSCHEINPOSITION_N_MENGE,
								LieferscheinpositionFac.FLR_LIEFERSCHEINPOSITION_EINHEIT_C_NR,
								Facade.NICHT_SORTIERBAR,
								LieferscheinpositionFac.FLR_LIEFERSCHEINPOSITION_C_BEZEICHNUNG,
								LieferscheinpositionFac.FLR_LIEFERSCHEINPOSITION_N_NETTOGESAMTPREIS,
								Facade.NICHT_SORTIERBAR,
								Facade.NICHT_SORTIERBAR }));

			}

			return super.getTableInfo();
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
			return null;
		}

	}

	public PrintInfoDto getSDocPathAndPartner(Object key) {
		LieferscheinDto lieferscheinDto = null;
		LieferscheinpositionDto lieferscheinpositionDto = null;
		KundeDto kundeDto = null;
		PartnerDto partnerDto = null;
		try {
			lieferscheinpositionDto = getLieferscheinpositionFac()
					.lieferscheinpositionFindByPrimaryKey((Integer) key,
							theClientDto);
			lieferscheinDto = getLieferscheinFac()
					.lieferscheinFindByPrimaryKey(
							lieferscheinpositionDto.getLieferscheinIId(),
							theClientDto);
			kundeDto = getKundeFac().kundeFindByPrimaryKey(
					lieferscheinDto.getKundeIIdLieferadresse(), theClientDto);
			partnerDto = getPartnerFac().partnerFindByPrimaryKey(
					kundeDto.getPartnerIId(), theClientDto);
		} catch (Exception e) {
			// Nicht gefunden
		}
		if (lieferscheinDto != null && lieferscheinpositionDto != null) {
//			String sPath = JCRDocFac.HELIUMV_NODE + "/"
//					+ theClientDto.getMandant() + "/"
//					+ LocaleFac.BELEGART_LIEFERSCHEIN.trim() + "/"
//					+ LocaleFac.BELEGART_LIEFERSCHEIN.trim() + "/"
//					+ lieferscheinDto.getCNr().replace("/", ".") + "/"
//					+ "Lieferscheinpositionen/" + "Position "
//					+ lieferscheinpositionDto.getIId();
			DocPath docPath = new DocPath(new DocNodeLieferscheinPosition(lieferscheinpositionDto, lieferscheinDto));
			Integer iPartnerIId = null;
			if (partnerDto != null) {
				iPartnerIId = partnerDto.getIId();
			}
			return new PrintInfoDto(docPath, iPartnerIId, getSTable());
		} else {
			return null;
		}
	}

	public String getSTable() {
		return "LIEFERSCHEINPOSITION";
	}
}
