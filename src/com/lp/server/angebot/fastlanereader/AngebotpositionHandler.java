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
package com.lp.server.angebot.fastlanereader;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.anfrage.service.AnfragepositionFac;
import com.lp.server.angebot.fastlanereader.generated.FLRAngebotposition;
import com.lp.server.angebot.service.AngebotDto;
import com.lp.server.angebot.service.AngebotpositionDto;
import com.lp.server.angebot.service.AngebotpositionFac;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.system.fastlanereader.service.TableColumnInformation;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocNodeAgPosition;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.util.Facade;
import com.lp.server.util.PositionNumberAdapter;
import com.lp.server.util.PositionNumberHandlerPaged;
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
 * FLR fuer ANGB_ANGEBOTPOSITION.
 * </p>
 * <p>
 * Copright Logistik Pur GmbH (c) 2005
 * </p>
 * <p>
 * Erstellungsdatum 14.07.05
 * </p>
 * <p>
 * </p>
 * 
 * @author Uli Walch
 * @version 1.0
 */

public class AngebotpositionHandler extends UseCaseHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String FLR_ANGEBOTPOSITION = "flrangebotposition.";
	public static final String FLR_ANGEBOTPOSITION_FROM_CLAUSE = " from FLRAngebotposition flrangebotposition LEFT OUTER JOIN flrangebotposition.flrartikel flrartikel";

	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {
		QueryResult result = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		String mandantCNr = theClientDto.getMandant();
		Locale locUI = theClientDto.getLocUi();
		try {
			int colCount = getTableInfo().getColumnClasses().length;
			int pageSize = AngebotHandler.PAGE_SIZE;
			int startIndex = Math.max(rowIndex.intValue() - (pageSize / 2), 0);
			int endIndex = startIndex + pageSize - 1;

			session = factory.openSession();
			String queryString = this.getFromClause() + this.buildWhereClause() + this.buildOrderByClause();
			// myLogger.info("HQL: " + queryString);
			Query query = session.createQuery(queryString);

			List<Object[]> resultFLrs = query.list(); // ja, ich will
																// alle(!)
																// Datensaetze
																// haben

			// SP4186 ohne Kalkulatorische Artikel
			List<FLRAngebotposition> resultFLrsPosnr = new ArrayList<FLRAngebotposition>();
			Iterator it = resultFLrs.iterator();
			while (it.hasNext()) {
				Object[] o = (Object[])it.next();
				FLRAngebotposition flr = (FLRAngebotposition) o[0];

				// SP4186
				if (flr.getFlrartikel() != null && Helper.short2boolean(flr.getFlrartikel().getB_kalkulatorisch())) {
					continue;
				}

				resultFLrsPosnr.add(flr);

			}

			PositionNumberAdapter adapter = new FLRAngebotpositionNumberAdapter(
					(List<FLRAngebotposition>) resultFLrsPosnr);
			PositionNumberHandlerPaged numberHandler = new PositionNumberHandlerPaged(adapter);

			query.setFirstResult(startIndex);
			query.setMaxResults(pageSize);
			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();
			Object[][] rows = new Object[resultList.size()][colCount];
			String[] tooltipData = new String[resultList.size()];
			int row = 0;
			int col = 0;

			int iNachkommastellenMenge = getMandantFac().getNachkommastellenMenge(mandantCNr);
			int iNachkommastellenPreis = getMandantFac().getNachkommastellenPreisVK(mandantCNr);

			// darf Preise sehen.
			final boolean bDarfPreiseSehen = getTheJudgeFac().hatRecht(RechteFac.RECHT_LP_DARF_PREISE_SEHEN_VERKAUF,
					theClientDto);

			while (resultListIterator.hasNext()) {
				Object[] o = (Object[]) resultListIterator.next();
				FLRAngebotposition position = (FLRAngebotposition) o[0];

				
				
				Object[] rowToAddCandidate = new Object[colCount];

				rowToAddCandidate[getTableColumnInformation().getViewIndex("i_id")] = position.getI_id();
				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.nr")] = numberHandler.getPositionNummer(position.getI_id(), adapter);
				rowToAddCandidate[getTableColumnInformation()
						.getViewIndex("lp.menge")] = getUIObjectBigDecimalNachkommastellen(position.getN_menge(), iNachkommastellenMenge);
				rowToAddCandidate[getTableColumnInformation().getViewIndex(
						"lp.einheit")] =  position.getEinheit_c_nr() == null ? "" : position.getEinheit_c_nr().trim();
				if (Helper.short2boolean(position.getB_alternative())) {
					rowToAddCandidate[getTableColumnInformation().getViewIndex("alternative")] = AngebotpositionFac.ANGEBOTPOSITION_ALTERNATIVE_SHORT;
				}
				if (position.getFlrartikel() != null) {
					rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.ident")] = position.getFlrartikel()
							.getC_nr();
					if (bReferenznummerInPositionen) {
						rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.referenznummer")] = position.getFlrartikel()
								.getC_referenznr();

					}

				}

				
				
				
				
				
				
				
			
				// in der Spalte Bezeichnung koennen verschiedene Dinge stehen
				String sBezeichnung = null;

				if (position.getTyp_c_nr() == null) {
					if (position.getPositionart_c_nr().equals(LocaleFac.POSITIONSART_TEXTEINGABE)) {
						if (position.getX_textinhalt() != null && position.getX_textinhalt().length() > 0) {
							sBezeichnung = Helper.strippHTML(position.getX_textinhalt());
						}
					} else if (position.getPositionart_c_nr().equals(LocaleFac.POSITIONSART_SEITENUMBRUCH)) {
						sBezeichnung = "[" + getTextRespectUISpr("lp.seitenumbruch", mandantCNr, locUI) + "]";
					} else if (position.getPositionart_c_nr().equals(LocaleFac.POSITIONSART_ENDSUMME)) {
						sBezeichnung = "[" + getTextRespectUISpr("lp.endsumme", mandantCNr, locUI) + "]";
					} else if (position.getPositionart_c_nr().equals(LocaleFac.POSITIONSART_POSITION)) {
						sBezeichnung = getTextRespectUISpr("lp.position", mandantCNr, locUI) + " "
								+ position.getC_bez();
					} else if (position.getPositionart_c_nr().equals(LocaleFac.POSITIONSART_TEXTBAUSTEIN)) {
						sBezeichnung = position.getFlrmediastandard().getC_nr();
					} else if (position.getPositionart_c_nr().equals(LocaleFac.POSITIONSART_AGSTUECKLISTE)) {
						sBezeichnung = position.getFlragstkl().getC_bez();
						if (bReferenznummerInPositionen) {
							rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.referenznummer")] = position.getFlragstkl()
									.getC_zeichnungsnummer();

						}
					} else if (position.getPositionart_c_nr().equals(LocaleFac.POSITIONSART_IDENT)) {
						// die sprachabhaengig Artikelbezeichnung anzeigen
						sBezeichnung = getArtikelFac().formatArtikelbezeichnungEinzeiligOhneExcUebersteuert(
								position.getFlrartikel().getI_id(), theClientDto.getLocUi(),position.getC_bez(),position.getC_zbez());
					} else if (position.getPositionart_c_nr()
							.equals(LocaleFac.POSITIONSART_INTELLIGENTE_ZWISCHENSUMME)) {
						sBezeichnung = "[" + getTextRespectUISpr("angebot.intelligentezwischensumme", mandantCNr, locUI)
								+ "] " + Helper.emptyString(position.getC_bez());
					} else {
						// die restlichen Positionsarten
						if (position.getC_bez() != null) {
							sBezeichnung = position.getC_bez();
						}
					}
				} else {
					if (position.getPosition_i_id() == null) {
						if (position.getPositionart_c_nr().equals(LocaleFac.POSITIONSART_POSITION)) {
							sBezeichnung = getTextRespectUISpr("lp.position", mandantCNr, locUI);
							if (position.getC_bez() != null) {
								sBezeichnung = sBezeichnung + " " + position.getC_bez();
							}
						}
					} else {
						if (position.getPositionart_c_nr().equals(LocaleFac.POSITIONSART_POSITION)) {
							if (position.getTyp_c_nr().equals(LocaleFac.POSITIONTYP_EBENE1)) {
								sBezeichnung = getTextRespectUISpr("lp.position", mandantCNr, locUI);
							} else {
								sBezeichnung = "  " + getTextRespectUISpr("lp.position", mandantCNr, locUI);
							}
							if (position.getC_bez() != null) {
								sBezeichnung = sBezeichnung + " " + position.getC_bez();
							}
						} else {
							if (position.getPositionart_c_nr().equals(LocaleFac.POSITIONSART_TEXTEINGABE)) {
								if (position.getTyp_c_nr().equals(LocaleFac.POSITIONTYP_EBENE1)) {
									if (position.getX_textinhalt() != null && position.getX_textinhalt().length() > 0) {
										sBezeichnung = " " + Helper.strippHTML(position.getX_textinhalt());
									}
								}
							} else if (position.getPositionart_c_nr().equals(LocaleFac.POSITIONSART_SEITENUMBRUCH)) {
								sBezeichnung = "[" + getTextRespectUISpr("lp.seitenumbruch", mandantCNr, locUI) + "]";
							} else if (position.getPositionart_c_nr().equals(LocaleFac.POSITIONSART_ENDSUMME)) {
								sBezeichnung = "[" + getTextRespectUISpr("lp.endsumme", mandantCNr, locUI) + "]";
							} else if (position.getPositionart_c_nr().equals(LocaleFac.POSITIONSART_POSITION)) {
								sBezeichnung = getTextRespectUISpr("lp.position", mandantCNr, locUI) + " "
										+ position.getC_bez();
							} else if (position.getPositionart_c_nr().equals(LocaleFac.POSITIONSART_TEXTBAUSTEIN)) {
								sBezeichnung = position.getFlrmediastandard().getC_nr();
							} else if (position.getPositionart_c_nr().equals(LocaleFac.POSITIONSART_IDENT)) {
								if (position.getTyp_c_nr().equals(LocaleFac.POSITIONTYP_EBENE1)) {
									// die sprachabhaengig Artikelbezeichnung
									// anzeigen
									sBezeichnung = "  " + getArtikelFac().formatArtikelbezeichnungEinzeiligOhneExc(
											position.getFlrartikel().getI_id(), theClientDto.getLocUi());
								} else if (position.getTyp_c_nr().equals(LocaleFac.POSITIONTYP_EBENE2)) {
									// die sprachabhaengig Artikelbezeichnung
									// anzeigen
									sBezeichnung = "    " + getArtikelFac().formatArtikelbezeichnungEinzeiligOhneExc(
											position.getFlrartikel().getI_id(), theClientDto.getLocUi());
								}
							} else {
								if (position.getPositionart_c_nr().equals(LocaleFac.POSITIONSART_HANDEINGABE)) {
									if (position.getTyp_c_nr().equals(LocaleFac.POSITIONTYP_EBENE1)) {
										if (position.getC_bez() != null) {
											sBezeichnung = " " + position.getC_bez();
										}
									} else if (position.getTyp_c_nr().equals(LocaleFac.POSITIONTYP_EBENE2)) {
										if (position.getC_bez() != null) {
											sBezeichnung = "  " + position.getC_bez();
										}
									}
								}
							}
						}
					}
				}

				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.bezeichnung")] = sBezeichnung;
				
				if (bDarfPreiseSehen) {
					BigDecimal bdNettogesamtpreis = null;
					BigDecimal bdZeilensumme = null;
					if (position.getPositionart_c_nr().equals(LocaleFac.POSITIONSART_POSITION)) {

						bdNettogesamtpreis = getUIObjectBigDecimalNachkommastellen(
								getAngebotpositionFac().getGesamtpreisPosition(position.getI_id(), theClientDto),
								iNachkommastellenPreis);
						if (!(bdNettogesamtpreis == null || position.getN_menge() == null)) {
							bdZeilensumme = bdNettogesamtpreis.multiply(position.getN_menge());
						}
					} else {
						bdNettogesamtpreis = getUIObjectBigDecimalNachkommastellen(position.getN_nettogesamtpreis(),
								iNachkommastellenPreis);
						if (!(position.getN_nettogesamtpreis() == null || position.getN_menge() == null)) {
							bdZeilensumme = position.getN_nettogesamtpreis().multiply(position.getN_menge());
							if (position.getFlrverleih() != null) {
								bdZeilensumme = bdZeilensumme
										.multiply(new BigDecimal(position.getFlrverleih().getF_faktor()));
							}
						}
					}
					rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.preis")] = bdNettogesamtpreis;
					rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.zeilensumme")] = bdZeilensumme;
				}
				// Text
				if (position.getX_textinhalt() != null && !position.getX_textinhalt().equals("")) {
					rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.text")] = new Boolean(true);
					String text = position.getX_textinhalt();
					text = text.replaceAll("\n", "<br>");
					text = "<html>" + text + "</html>";
					tooltipData[row] = text;
				} else {
					rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.text")] = new Boolean(false);
				}
				
				
				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.medien.anzahl")] = getBelegartmediaFac()
						.anzahlDerVorhandenenMedien(getUsecaseId(), position.getI_id(), theClientDto);
				
				rows[row] = rowToAddCandidate;
				
				row++;
				col = 0;
			}
			result = new QueryResult(rows, this.getRowCount(), startIndex, endIndex, 0, tooltipData);
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

	protected long getRowCountFromDataBase() {
		long rowCount = 0;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			session = factory.openSession();
			String queryString = "select count(*) " + this.getFromClause() + this.buildWhereClause();
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
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_HIBERNATE, he);
				}
			}
		}
		return rowCount;
	}

	/**
	 * builds the where clause of the HQL (Hibernate Query Language) statement using
	 * the current query.
	 * 
	 * @return the HQL where clause.
	 */
	private String buildWhereClause() {
		StringBuffer where = new StringBuffer("");

		if (this.getQuery() != null && this.getQuery().getFilterBlock() != null
				&& this.getQuery().getFilterBlock().filterKrit != null) {

			FilterBlock filterBlock = this.getQuery().getFilterBlock();
			FilterKriterium[] filterKriterien = this.getQuery().getFilterBlock().filterKrit;
			String booleanOperator = filterBlock.boolOperator;
			boolean filterAdded = false;

			for (int i = 0; i < filterKriterien.length; i++) {
				if (filterKriterien[i].isKrit) {
					if (filterAdded) {
						where.append(" " + booleanOperator);
					}
					filterAdded = true;
					where.append(" " + FLR_ANGEBOTPOSITION + filterKriterien[i].kritName);
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
					if (!kriterien[i].kritName.endsWith(Facade.NICHT_SORTIERBAR)) {
						if (kriterien[i].isKrit) {
							if (sortAdded) {
								orderBy.append(", ");
							}
							sortAdded = true;
							orderBy.append(FLR_ANGEBOTPOSITION + kriterien[i].kritName);
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
				orderBy.append(FLR_ANGEBOTPOSITION)
						// poseinfuegen: 0 default Sortierung nach iSort
						.append(AngebotpositionFac.FLR_ANGEBOTPOSITION_I_SORT).append(" ASC ");
				sortAdded = true;
			}
			if (orderBy.indexOf(FLR_ANGEBOTPOSITION + "i_id") < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" ").append(FLR_ANGEBOTPOSITION).append("i_id").append(" ");
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
		return FLR_ANGEBOTPOSITION_FROM_CLAUSE;
	}

	public QueryResult sort(SortierKriterium[] sortierKriterien, Object selectedId) throws EJBExceptionLP {
		this.getQuery().setSortKrit(sortierKriterien);

		QueryResult result = null;
		int rowNumber = 0;

		if (selectedId != null && ((Integer) selectedId).intValue() >= 0) {
			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = null;

			try {
				session = factory.openSession();
				String queryString = "select " + FLR_ANGEBOTPOSITION + "i_id" + FLR_ANGEBOTPOSITION_FROM_CLAUSE
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
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
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
		TableInfo info = super.getTableInfo();
		if (info != null)
			return info;

		setTableColumnInformation(createColumnInformation(theClientDto.getMandant(), theClientDto.getLocUi()));

		TableColumnInformation c = getTableColumnInformation();
		info = new TableInfo(c.getClasses(), c.getHeaderNames(), c.getWidths(), c.getDbColumNames(),
				c.getHeaderToolTips());
		setTableInfo(info);
		return info;
	}

	private TableColumnInformation createColumnInformation(String mandant, Locale locUi) {
		TableColumnInformation columns = new TableColumnInformation();
		try {

			String mandantCNr = theClientDto.getMandant();

			int iNachkommastellenMenge = getMandantFac().getNachkommastellenMenge(mandantCNr);
			int iNachkommastellenPreis = getMandantFac().getNachkommastellenPreisVK(mandantCNr);

			columns.add("i_id", Integer.class,"i_id",
					QueryParameters.FLR_BREITE_SHARE_WITH_REST, "i_id");

			columns.add("lp.nr", Integer.class, getTextRespectUISpr("lp.nr", mandant, locUi),
					QueryParameters.FLR_BREITE_S, Facade.NICHT_SORTIERBAR);
			columns.add("alternative", String.class, "", QueryParameters.FLR_BREITE_XXS, Facade.NICHT_SORTIERBAR);
			columns.add("lp.menge", super.getUIClassBigDecimalNachkommastellen(iNachkommastellenMenge),
					getTextRespectUISpr("lp.menge", mandant, locUi),
					getUIBreiteAbhaengigvonNachkommastellen(QueryParameters.MENGE, iNachkommastellenMenge),
					AngebotpositionFac.FLR_ANGEBOTPOSITION_N_MENGE);
			columns.add("lp.einheit", String.class, getTextRespectUISpr("lp.einheit", mandant, locUi),
					QueryParameters.FLR_BREITE_XS, AngebotpositionFac.FLR_ANGEBOTPOSITION_EINHEIT_C_NR);
			columns.add("lp.ident", String.class, getTextRespectUISpr("lp.ident", mandant, locUi), getUIBreiteIdent(),
					AngebotpositionFac.FLR_ANGEBOTPOSITION_FLRARTIKEL + ".c_nr");

			if (bReferenznummerInPositionen) {
				columns.add("lp.referenznummer", String.class, getTextRespectUISpr("lp.referenznummer", mandant, locUi),
						QueryParameters.FLR_BREITE_XM,
						AnfragepositionFac.FLR_ANFRAGEPOSITION_FLRARTIKEL + ".c_referenznr");
			}

			columns.add("lp.bezeichnung", String.class, getTextRespectUISpr("lp.bezeichnung", mandant, locUi),
					QueryParameters.FLR_BREITE_SHARE_WITH_REST, AngebotpositionFac.FLR_ANGEBOTPOSITION_C_BEZ);
			columns.add("lp.preis", super.getUIClassBigDecimalNachkommastellen(iNachkommastellenPreis),
					getTextRespectUISpr("lp.preis", mandant, locUi),
					getUIBreiteAbhaengigvonNachkommastellen(QueryParameters.PREIS, iNachkommastellenPreis),
					AngebotpositionFac.FLR_ANGEBOTPOSITION_N_NETTOGESAMTPREIS);
			columns.add("lp.zeilensumme", super.getUIClassBigDecimalNachkommastellen(iNachkommastellenPreis),
					getTextRespectUISpr("lp.zeilensumme", mandant, locUi),
					getUIBreiteAbhaengigvonNachkommastellen(QueryParameters.PREIS, iNachkommastellenPreis),
					Facade.NICHT_SORTIERBAR);
			columns.add("lp.text", Boolean.class, getTextRespectUISpr("lp.text", mandant, locUi), 3,
					Facade.NICHT_SORTIERBAR);
			columns.add("lp.medien.anzahl", Integer.class, getTextRespectUISpr("lp.medien.anzahl", mandant, locUi), 3,
					Facade.NICHT_SORTIERBAR);
		} catch (RemoteException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}

		return columns;

	}

	public PrintInfoDto getSDocPathAndPartner(Object key) {
		AngebotDto angebotDto = null;
		AngebotpositionDto angebotpositionDto = null;
		KundeDto kundeDto = null;
		PartnerDto partnerDto = null;
		try {
			angebotpositionDto = getAngebotpositionFac().angebotpositionFindByPrimaryKey((Integer) key, theClientDto);
			angebotDto = getAngebotFac().angebotFindByPrimaryKey(angebotpositionDto.getBelegIId(), theClientDto);
			kundeDto = getKundeFac().kundeFindByPrimaryKey(angebotDto.getKundeIIdAngebotsadresse(), theClientDto);
			partnerDto = getPartnerFac().partnerFindByPrimaryKey(kundeDto.getPartnerIId(), theClientDto);
		} catch (Exception e) {
			// Nicht gefunden
		}
		if (angebotDto != null && angebotpositionDto != null) {
			// String sPath= JCRDocFac.HELIUMV_NODE + "/" +
			// LocaleFac.BELEGART_ANGEBOT.trim() +
			// "/" +LocaleFac.BELEGART_ANGEBOT.trim()+"/" +
			// angebotDto.getCNr().replace("/", ".") + "/" +
			// "Angebotpositionen/" +
			// "Position " + angebotpositionDto.getIId();
			DocPath docPath = new DocPath(new DocNodeAgPosition(angebotpositionDto, angebotDto));
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
		return "ANGEBOTSPOSITION";
	}
}
