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
package com.lp.server.auftrag.fastlanereader;

import java.awt.Color;
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

import com.lp.server.anfrage.service.AnfragepositionFac;
import com.lp.server.auftrag.fastlanereader.generated.FLRAuftragposition;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.auftrag.service.AuftragpositionDto;
import com.lp.server.auftrag.service.AuftragpositionFac;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.system.fastlanereader.service.TableColumnInformation;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocNodeAufPosition;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.util.Facade;
import com.lp.server.util.PositionNumberAdapter;
import com.lp.server.util.PositionNumberHandler;
import com.lp.server.util.PositionNumberHandlerFullPaged;
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
 * Hier wird die FLR Funktionalitaet fuer die Auftragsposition implementiert.
 * Pro UseCase gibt es einen Handler.
 * </p>
 * <p>
 * Copright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * <p>
 * Erstellungsdatum 2004-08-27
 * </p>
 * <p>
 * </p>
 * 
 * @author martin werner, uli walch
 * @version 1.0
 */

public class AuftragpositionHandler extends UseCaseHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String FLR_AUFTRAGPOSITION = "flrauftragposition.";
	public static final String FLR_AUFTRAGPOSITION_FROM_CLAUSE = " from FLRAuftragposition flrauftragposition  LEFT OUTER JOIN flrauftragposition.flrartikel flrartikel";

	boolean bDarfPreiseSehen;

	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {
		QueryResult result = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		String mandantCNr = theClientDto.getMandant();
		Locale locUI = theClientDto.getLocUi();

		try {
			int colCount = getTableInfo().getColumnClasses().length;
			int pageSize = AuftragHandler.PAGE_SIZE;
			int startIndex = Math.max(rowIndex.intValue() - (pageSize / 2), 0);
			int endIndex = startIndex + pageSize - 1;

			session = factory.openSession();
			String queryString = this.getFromClause() + this.buildWhereClause() + this.buildOrderByClause();
			// myLogger.info("HQL: " + queryString);
			Query query = session.createQuery(queryString);
			query.setFirstResult(startIndex);
			query.setMaxResults(pageSize);
			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();
			Object[][] rows = new Object[resultList.size()][colCount];
			String[] tooltipData = new String[resultList.size()];

			boolean bZusatzfunktionHvma2 = getMandantFac()
					.darfAnwenderAufZusatzfunktionZugreifen(MandantFac.ZUSATZFUNKTION_HVMA2, theClientDto);

			int iNachkommastellenMenge = getMandantFac().getNachkommastellenMenge(mandantCNr);
			int iNachkommastellenPreis = getMandantFac().getNachkommastellenPreisVK(mandantCNr);

			int row = 0;
			int col = 0;

			long startPageTime = System.currentTimeMillis();

			PositionNumberAdapter adapter = new FLRAuftragPositionNumberAdapter((List<FLRAuftragposition>) resultList);
			// PositionNumberHandlerPaged numberHandler = new
			// PositionNumberHandlerPaged(adapter) ;
			// PositionNumberHandler numberHandler = new
			// PositionNumberHandler();
			PositionNumberHandler numberHandler = new PositionNumberHandlerFullPaged(adapter, getAuftragpositionFac());

			while (resultListIterator.hasNext()) {

				Object[] o = (Object[]) resultListIterator.next();
				FLRAuftragposition position = (FLRAuftragposition) o[0];

				Object[] rowToAddCandidate = new Object[colCount];

				rowToAddCandidate[getTableColumnInformation().getViewIndex("i_id")] = position.getI_id();
				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.nr")] = numberHandler
						.getPositionNummer(position.getI_id(), adapter);
				rowToAddCandidate[getTableColumnInformation()
						.getViewIndex("lp.menge")] = getUIObjectBigDecimalNachkommastellen(position.getN_menge(),
								iNachkommastellenMenge);
				rowToAddCandidate[getTableColumnInformation().getViewIndex(
						"lp.einheit")] = position.getEinheit_c_nr() == null ? "" : position.getEinheit_c_nr().trim();

				if (position.getFlrartikel() != null) {
					rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.ident")] = position.getFlrartikel()
							.getC_nr();
					if (bReferenznummerInPositionen) {
						rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.referenznummer")] = position
								.getFlrartikel().getC_referenznr();

					}

				}

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
					} else if (position.getPositionart_c_nr().equals(LocaleFac.POSITIONSART_IDENT)) {
						// die sprachabhaengig Artikelbezeichnung anzeigen
						sBezeichnung = getArtikelFac().formatArtikelbezeichnungEinzeiligOhneExcUebersteuert(
								position.getFlrartikel().getI_id(), theClientDto.getLocUi(), position.getC_bez(),
								position.getC_zbez());
					} else if (position.getPositionart_c_nr()
							.equals(LocaleFac.POSITIONSART_INTELLIGENTE_ZWISCHENSUMME)) {
						sBezeichnung = "[" + getTextRespectUISpr("auftrag.intelligentezwischensumme", mandantCNr, locUI)
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

				// flrpreis: 1 In der FLR Liste soll die Anzahl der
				// Nachkommstellen dem
				// Parameter fuer die Preise entsprechen; ausserdem wird der
				// Wert in der Anzeige
				// kaufmaennisch gerundet

				if (bDarfPreiseSehen) {
					if (position.getPositionart_c_nr().equals(LocaleFac.POSITIONSART_POSITION)) {
						BigDecimal bdGesamtpreis = getAuftragpositionFac().getGesamtpreisPosition(position.getI_id(),
								theClientDto);
						rowToAddCandidate[getTableColumnInformation()
								.getViewIndex("lp.preis")] = getUIObjectBigDecimalNachkommastellen(bdGesamtpreis,
										iNachkommastellenPreis);
						if (bdGesamtpreis == null || position.getN_menge() == null) {

						} else {
							rowToAddCandidate[getTableColumnInformation()
									.getViewIndex("lp.zeilensumme")] = getUIObjectBigDecimalNachkommastellen(
											bdGesamtpreis.multiply(position.getN_menge()), iNachkommastellenPreis);
						}
					} else {
						rowToAddCandidate[getTableColumnInformation()
								.getViewIndex("lp.preis")] = getUIObjectBigDecimalNachkommastellen(
										position.getN_nettogesamtpreis(), iNachkommastellenPreis);
						if (position.getN_nettogesamtpreis() == null || position.getN_menge() == null) {

						} else {
							BigDecimal verleih = new BigDecimal(1);
							if (position.getFlrverleih() != null) {
								verleih = new BigDecimal(position.getFlrverleih().getF_faktor());
							}

							rowToAddCandidate[getTableColumnInformation()
									.getViewIndex("lp.zeilensumme")] = getUIObjectBigDecimalNachkommastellen(
											position.getN_nettogesamtpreis().multiply(
													position.getN_menge().multiply(verleih)),
											iNachkommastellenPreis);
						}
					}

				}

				rowToAddCandidate[getTableColumnInformation().getViewIndex("auft.pos.liefertermin")] = position
						.getT_uebersteuerterliefertermin();

				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.status")] = position
						.getAuftragpositionstatus_c_nr();

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

				// PJ17194
				if (position.getFlrauftrag().getAuftragart_c_nr().equals(AuftragServiceFac.AUFTRAGART_ABRUF)
						&& position.getAuftragposition_i_id_rahmenposition() == null && position.getN_menge() != null) {
					rowToAddCandidate[getTableColumnInformation().getViewIndex("Color")] = Color.RED;
				} else {
					if (Helper.short2boolean(position.getB_gesehen())) {
						rowToAddCandidate[getTableColumnInformation().getViewIndex("Color")] = new Color(0, 153, 51);// GRUEN
					} else if (bZusatzfunktionHvma2 && Helper.short2boolean(position.getB_hvmauebertragen())) {
						rowToAddCandidate[getTableColumnInformation().getViewIndex("Color")] = new Color(0, 153, 51);// GRUEN
					}

				}

				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.medien.anzahl")] = getBelegartmediaFac()
						.anzahlDerVorhandenenMedien(getUsecaseId(), position.getI_id(), theClientDto);

				rows[row] = rowToAddCandidate;
				row++;
				col = 0;
			}

			myLogger.info("Dauer PageTime: " + (System.currentTimeMillis() - startPageTime));

			result = new QueryResult(rows, this.getRowCount(), startIndex, endIndex, 0, tooltipData);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
		} finally {
			closeSession(session);
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
					where.append(" " + FLR_AUFTRAGPOSITION + filterKriterien[i].kritName);
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
							orderBy.append(FLR_AUFTRAGPOSITION + kriterien[i].kritName);
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
				orderBy.append(FLR_AUFTRAGPOSITION)
						// poseinfuegen: 0 default Sortierung nach iSort
						.append(AuftragpositionFac.FLR_AUFTRAGPOSITION_I_SORT).append(" ASC ");
				sortAdded = true;
			}
			if (orderBy.indexOf(FLR_AUFTRAGPOSITION + "i_id") < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" ").append(FLR_AUFTRAGPOSITION).append("i_id").append(" ");
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
		return FLR_AUFTRAGPOSITION_FROM_CLAUSE;
	}

	public QueryResult sort(SortierKriterium[] sortierKriterien, Object selectedId) throws EJBExceptionLP {
		getQuery().setSortKrit(sortierKriterien);

		QueryResult result = null;
		int rowNumber = 0;

		if (selectedId != null && ((Integer) selectedId).intValue() >= 0) {
			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = null;

			try {
				session = factory.openSession();
				String queryString = "select " + FLR_AUFTRAGPOSITION + "i_id" + FLR_AUFTRAGPOSITION_FROM_CLAUSE
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

	private TableColumnInformation createColumnInformation(String mandant, Locale locUi) {
		TableColumnInformation columns = new TableColumnInformation();
		try {

			String mandantCNr = theClientDto.getMandant();

			int iNachkommastellenMenge = getMandantFac().getNachkommastellenMenge(mandantCNr);
			int iNachkommastellenPreis = getMandantFac().getNachkommastellenPreisVK(mandantCNr);

			bDarfPreiseSehen = getTheJudgeFac().hatRecht(RechteFac.RECHT_LP_DARF_PREISE_SEHEN_VERKAUF, theClientDto);

			columns.add("i_id", Integer.class, "i_id", QueryParameters.FLR_BREITE_SHARE_WITH_REST, "i_id");

			columns.add("lp.nr", Integer.class, getTextRespectUISpr("lp.nr", mandant, locUi),
					QueryParameters.FLR_BREITE_S, Facade.NICHT_SORTIERBAR);
			columns.add("lp.menge", super.getUIClassBigDecimalNachkommastellen(iNachkommastellenMenge),
					getTextRespectUISpr("lp.menge", mandant, locUi),
					getUIBreiteAbhaengigvonNachkommastellen(QueryParameters.MENGE, iNachkommastellenMenge),
					AuftragpositionFac.FLR_AUFTRAGPOSITION_N_MENGE);
			columns.add("lp.einheit", String.class, getTextRespectUISpr("lp.einheit", mandant, locUi),
					QueryParameters.FLR_BREITE_XS, AuftragpositionFac.FLR_AUFTRAGPOSITION_EINHEIT_C_NR);
			columns.add("lp.ident", String.class, getTextRespectUISpr("lp.ident", mandant, locUi), getUIBreiteIdent(),
					AuftragpositionFac.FLR_AUFTRAGPOSITION_FLRARTIKEL + ".c_nr");

			if (bReferenznummerInPositionen) {
				columns.add("lp.referenznummer", String.class, getTextRespectUISpr("lp.referenznummer", mandant, locUi),
						QueryParameters.FLR_BREITE_XM,
						AnfragepositionFac.FLR_ANFRAGEPOSITION_FLRARTIKEL + ".c_referenznr");
			}

			columns.add("lp.bezeichnung", String.class, getTextRespectUISpr("lp.bezeichnung", mandant, locUi),
					QueryParameters.FLR_BREITE_SHARE_WITH_REST, "c_bez");

			if (bDarfPreiseSehen) {

				columns.add("lp.preis", super.getUIClassBigDecimalNachkommastellen(iNachkommastellenPreis),
						getTextRespectUISpr("lp.preis", mandant, locUi),
						getUIBreiteAbhaengigvonNachkommastellen(QueryParameters.PREIS, iNachkommastellenPreis),
						AuftragpositionFac.FLR_AUFTRAGPOSITION_N_NETTOGESAMTPREIS);
				columns.add("lp.zeilensumme", super.getUIClassBigDecimalNachkommastellen(iNachkommastellenPreis),
						getTextRespectUISpr("lp.zeilensumme", mandant, locUi),
						getUIBreiteAbhaengigvonNachkommastellen(QueryParameters.PREIS, iNachkommastellenPreis),
						Facade.NICHT_SORTIERBAR);
			}

			columns.add("auft.pos.liefertermin", java.util.Date.class,
					getTextRespectUISpr("auft.pos.liefertermin", mandant, locUi), QueryParameters.FLR_BREITE_M,
					"t_uebersteuerterliefertermin");
			columns.add("lp.status", String.class, getTextRespectUISpr("lp.status", mandant, locUi),
					QueryParameters.FLR_BREITE_M, "auftragpositionstatus_c_nr");
			columns.add("lp.text", Boolean.class, getTextRespectUISpr("lp.text", mandant, locUi), 3,
					Facade.NICHT_SORTIERBAR);
			columns.add("lp.medien.anzahl", Integer.class, getTextRespectUISpr("lp.medien.anzahl", mandant, locUi), 3,
					Facade.NICHT_SORTIERBAR);

			columns.add("Color", Color.class, "", 1, Facade.NICHT_SORTIERBAR);

		} catch (RemoteException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}

		return columns;

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

	public PrintInfoDto getSDocPathAndPartner(Object key) {
		AuftragDto auftragDto = null;
		AuftragpositionDto auftragpositionDto = null;
		KundeDto kundeDto = null;
		PartnerDto partnerDto = null;
		try {
			auftragpositionDto = getAuftragpositionFac().auftragpositionFindByPrimaryKey((Integer) key);
			auftragDto = getAuftragFac().auftragFindByPrimaryKey(auftragpositionDto.getBelegIId());
			kundeDto = getKundeFac().kundeFindByPrimaryKey(auftragDto.getKundeIIdAuftragsadresse(), theClientDto);
			partnerDto = getPartnerFac().partnerFindByPrimaryKey(kundeDto.getPartnerIId(), theClientDto);
		} catch (Exception e) {
			// Nicht gefunden
		}
		if (auftragDto != null && auftragpositionDto != null) {
			// String sPath = JCRDocFac.HELIUMV_NODE + "/"
			// + theClientDto.getMandant() + "/"
			// + LocaleFac.BELEGART_AUFTRAG.trim() + "/"
			// + LocaleFac.BELEGART_AUFTRAG.trim() + "/"
			// + auftragDto.getCNr().replace("/", ".") + "/"
			// + "Auftragpositionen/" + "Position "
			// + auftragpositionDto.getIId();
			DocPath docPath = new DocPath(new DocNodeAufPosition(auftragpositionDto, auftragDto));
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
		return "AUFTRAGPOSITION";
	}
}
