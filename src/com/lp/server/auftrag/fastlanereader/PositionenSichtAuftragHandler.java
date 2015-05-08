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

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikel;
import com.lp.server.artikel.service.ArtikelFac;
import com.lp.server.auftrag.fastlanereader.generated.FLRPositionenSichtAuftrag;
import com.lp.server.auftrag.service.AuftragpositionFac;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.util.Facade;
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
import com.lp.server.util.PositionNumberAdapter ;

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

public class PositionenSichtAuftragHandler extends UseCaseHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String FLR_POSITIONENSICHTAUFTRAG = "flrpositionensichtauftrag.";
	public static final String FLR_POSITIONENSICHTAUFTRAG_FROM_CLAUSE = " from FLRPositionenSichtAuftrag flrpositionensichtauftrag ";

	/**
	 * gets the data page for the specified row using the current query. The row
	 * at rowIndex will be located in the middle of the page.
	 * 
	 * @param rowIndex
	 *            diese Zeile soll selektiert sein
	 * @return QueryResult das Ergebnis der Abfrage
	 * @throws EJBExceptionLP
	 *             Ausnahme
	 * @see UseCaseHandler#getPageAt(java.lang.Integer)
	 */
	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {
		long startTime = System.currentTimeMillis() ;
		
		QueryResult result = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		String mandantCNr = theClientDto.getMandant();
		Locale locUI = theClientDto.getLocUi();

		try {
			int colCount = getTableInfo().getColumnClasses().length;
			// int pageSize = 100;
			// int startIndex = Math.max(rowIndex.intValue() - (pageSize / 2),
			// 0);
			// int endIndex = startIndex + pageSize - 1;

			session = factory.openSession();
			String queryString = this.getFromClause() + this.buildWhereClause()
					+ this.buildOrderByClause();
			// myLogger.info("HQL: " + queryString);
			Query query = session.createQuery(queryString);
			// query.setFirstResult(startIndex);
			// query.setMaxResults(pageSize);
			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();

			// in diesem Fall liegt ein logischer Implementierungsfehler vor,
			// fuehrt
			// entweder zu leerer Anzeige oder zu ENDLOSSCHLEIFE!
			if (getRowCount() != resultList.size()) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR,
						new Exception("rowCount != resultList.size()"));
			}

			Object[][] rows = new Object[resultList.size()][colCount];
			int row = 0;
			int col = 0;

			
			PositionNumberAdapter adapter = new FLRAuftragPositionNumberAdapter(
					(List<FLRPositionenSichtAuftrag>) resultList) ;
			PositionNumberHandlerPaged numberHandler = new PositionNumberHandlerPaged(adapter) ;
			
			while (resultListIterator.hasNext()) {
				FLRPositionenSichtAuftrag position = (FLRPositionenSichtAuftrag) resultListIterator
						.next();
				rows[row][col++] = position.getI_id();

// 				rows[row][col++] = getAuftragpositionFac().getPositionNummer(
//					position.getI_id());

//				Integer originalPosNumber = getAuftragpositionFac().getPositionNummer(
//					position.getI_id());
				Integer newPosNumber = numberHandler.getPositionNummer(position.getI_id(), adapter) ;

//				if(originalPosNumber != null && newPosNumber != null) {
//					if(!originalPosNumber.equals(newPosNumber)) {
//						System.out.println("Positionsnummern stimmen nicht ueberein! PosIId="
//								+ position.getI_id() + " orig " + originalPosNumber + " new " + newPosNumber) ;
//					}
//				}
				
				rows[row][col++] = newPosNumber ;

				// die sprachabhaengig Artikelbezeichnung anzeigen
				FLRArtikel flrartikel = position.getFlrartikel();
				String einheitCNr = "";
				String ident = "";
				if (flrartikel != null) {
					einheitCNr = flrartikel.getEinheit_c_nr() == null ? ""
							: flrartikel.getEinheit_c_nr().trim();
					ident = flrartikel.getC_nr();
				}

				rows[row][col++] = einheitCNr;
				rows[row][col++] = ident;
				String sBezeichnung = null;
				if (position.getTyp_c_nr() == null) {
					if (position.getPositionart_c_nr().equals(
							LocaleFac.POSITIONSART_TEXTEINGABE)) {
						if (position.getX_textinhalt() != null
								&& position.getX_textinhalt().length() > 0) {
							sBezeichnung = Helper.strippHTML(position
									.getX_textinhalt());
						}
					} else if (position.getPositionart_c_nr().equals(
							LocaleFac.POSITIONSART_SEITENUMBRUCH)) {
						sBezeichnung = "["
								+ getTextRespectUISpr("lp.seitenumbruch",
										mandantCNr, locUI) + "]";
					} else if (position.getPositionart_c_nr().equals(
							LocaleFac.POSITIONSART_ENDSUMME)) {
						sBezeichnung = "["
								+ getTextRespectUISpr("lp.endsumme",
										mandantCNr, locUI) + "]";
					} else if (position.getPositionart_c_nr().equals(
							LocaleFac.POSITIONSART_POSITION)) {
						sBezeichnung = getTextRespectUISpr("lp.position",
								mandantCNr, locUI) + " " + position.getC_bez();
					} else if (position.getPositionart_c_nr().equals(
							LocaleFac.POSITIONSART_TEXTBAUSTEIN)) {
						sBezeichnung = position.getFlrmediastandard().getC_nr();
					} else if (position.getPositionart_c_nr().equals(
							LocaleFac.POSITIONSART_IDENT)) {
						// die sprachabhaengig Artikelbezeichnung anzeigen
						sBezeichnung = getArtikelFac()
								.formatArtikelbezeichnungEinzeiligOhneExc(
										position.getFlrartikel().getI_id(),
										theClientDto.getLocUi());
					} else if (position.getPositionart_c_nr().equals(
							LocaleFac.POSITIONSART_INTELLIGENTE_ZWISCHENSUMME)) {
						sBezeichnung = "["
								+ getTextRespectUISpr(
										"lieferscheinsichtauftrag.intelligentezwischensumme",
										mandantCNr, locUI) + "] "
								+ position.getC_bez();
					} else {
						// die restlichen Positionsarten
						if (position.getC_bez() != null) {
							sBezeichnung = position.getC_bez();
						}
					}
				} else {
					if (position.getPosition_i_id() == null) {
						if (position.getPositionart_c_nr().equals(
								LocaleFac.POSITIONSART_POSITION)) {
							sBezeichnung = getTextRespectUISpr("lp.position",
									mandantCNr, locUI);
							if (position.getC_bez() != null) {
								sBezeichnung = sBezeichnung + " "
										+ position.getC_bez();
							}
						}
					} else {
						if (position.getPositionart_c_nr().equals(
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
							if (position.getPositionart_c_nr().equals(
									LocaleFac.POSITIONSART_TEXTEINGABE)) {
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
							} else if (position.getPositionart_c_nr().equals(
									LocaleFac.POSITIONSART_SEITENUMBRUCH)) {
								sBezeichnung = "["
										+ getTextRespectUISpr(
												"lp.seitenumbruch", mandantCNr,
												locUI) + "]";
							} else if (position.getPositionart_c_nr().equals(
									LocaleFac.POSITIONSART_ENDSUMME)) {
								sBezeichnung = "["
										+ getTextRespectUISpr("lp.endsumme",
												mandantCNr, locUI) + "]";
							} else if (position.getPositionart_c_nr().equals(
									LocaleFac.POSITIONSART_POSITION)) {
								sBezeichnung = getTextRespectUISpr(
										"lp.position", mandantCNr, locUI)
										+ " "
										+ position.getC_bez();
							} else if (position.getPositionart_c_nr().equals(
									LocaleFac.POSITIONSART_TEXTBAUSTEIN)) {
								sBezeichnung = position.getFlrmediastandard()
										.getC_nr();
							} else if (position.getPositionart_c_nr().equals(
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
							}
						}
					}
				}

				rows[row][col++] = sBezeichnung;
				/*
				 * rows[row][col++] =
				 * getArtikelFac().formatArtikelbezeichnungEinzeiligOhneExc(
				 * auftragposition.getFlrartikel().getI_id(),
				 * getTheClient(sCurrentUser).getLocUi());
				 */
				rows[row][col++] = position.getN_offene_menge();
				rows[row][col++] = position.getAuftragpositionstatus_c_nr();
				rows[row++][col++] = position.getT_uebersteuerterliefertermin();

				col = 0;
			}
			result = new QueryResult(rows, this.getRowCount(), 0,
					resultList.size(), 0);

			long stopTime = System.currentTimeMillis() ;
			System.out.println("Duration pageAt PositionSichtAuftragHandler : " + (stopTime - startTime) + " for " + rows.length + " rows") ;
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

					if (filterKriterien[i].isBIgnoreCase()) {
						where.append(" LOWER(" + filterKriterien[i].kritName
								+ ")");
					} else {
						where.append(" " + filterKriterien[i].kritName);
					}

					where.append(" " + filterKriterien[i].operator);
					if (filterKriterien[i].isBIgnoreCase()) {
						where.append(" "
								+ filterKriterien[i].value.toLowerCase());
					} else {
						where.append(" " + filterKriterien[i].value);
					}
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
							orderBy.append(FLR_POSITIONENSICHTAUFTRAG
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
				orderBy.append(FLR_POSITIONENSICHTAUFTRAG)
						.append(AuftragpositionFac.FLR_AUFTRAGPOSITION_I_SORT)
						.append(" ASC "); // Sortierung wie bei den
											// Auftragpositionen
				sortAdded = true;
			}
			if (orderBy.indexOf(FLR_POSITIONENSICHTAUFTRAG
					+ AuftragpositionFac.FLR_AUFTRAGPOSITIONSICHTAUFTRAG_I_ID) < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" ")
						.append(FLR_POSITIONENSICHTAUFTRAG)
						.append(AuftragpositionFac.FLR_AUFTRAGPOSITIONSICHTAUFTRAG_I_ID)
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
		return FLR_POSITIONENSICHTAUFTRAG_FROM_CLAUSE;
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
				String queryString = "select "
						+ FLR_POSITIONENSICHTAUFTRAG
						+ AuftragpositionFac.FLR_AUFTRAGPOSITIONSICHTAUFTRAG_I_ID
						+ FLR_POSITIONENSICHTAUFTRAG_FROM_CLAUSE
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
		if (super.getTableInfo() == null) {
			String mandantCNr = theClientDto.getMandant();
			Locale locUI = theClientDto.getLocUi();
			setTableInfo(new TableInfo(
					new Class[] { Integer.class, Integer.class, String.class,
							String.class, String.class, BigDecimal.class,
							String.class, Date.class },
					new String[] {
							"i_id",
							getTextRespectUISpr("lp.nr", mandantCNr, locUI),
							getTextRespectUISpr("lp.einheit", mandantCNr, locUI),
							getTextRespectUISpr("lp.ident", mandantCNr, locUI),
							getTextRespectUISpr("lp.bezeichnung", mandantCNr,
									locUI),
							getTextRespectUISpr("auft.offen", mandantCNr, locUI),
							getTextRespectUISpr("lp.status", mandantCNr, locUI),
							getTextRespectUISpr("lp.termin", mandantCNr, locUI), },
					new int[] {
							QueryParameters.FLR_BREITE_SHARE_WITH_REST, // diese
							// Spalte
							// wird
							// ausgeblendet
							QueryParameters.FLR_BREITE_S,
							QueryParameters.FLR_BREITE_XS,
							getUIBreiteIdent(), //ident
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_M },
					new String[] {
							Facade.NICHT_SORTIERBAR,
							Facade.NICHT_SORTIERBAR,
							AuftragpositionFac.FLR_AUFTRAGPOSITIONSICHTAUFTRAG_FLRARTIKEL
									+ "." + ArtikelFac.FLR_ARTIKEL_EINHEIT_C_NR,
							AuftragpositionFac.FLR_AUFTRAGPOSITIONSICHTAUFTRAG_FLRARTIKEL
									+ "." + ArtikelFac.FLR_ARTIKEL_C_NR,
							Facade.NICHT_SORTIERBAR,
							AuftragpositionFac.FLR_AUFTRAGPOSITIONSICHTAUFTRAG_N_OFFENE_MENGE,
							AuftragpositionFac.FLR_AUFTRAGPOSITIONSICHTAUFTRAG_AUFTRAGPOSITIONSTATUS_C_NR,
							AuftragpositionFac.FLR_AUFTRAGPOSITIONSICHTAUFTRAG_T_UEBERSTEUERTERLIEFERTERMIN }));
		}

		return super.getTableInfo();
	}
}
