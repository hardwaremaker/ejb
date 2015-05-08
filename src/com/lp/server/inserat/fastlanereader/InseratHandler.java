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
package com.lp.server.inserat.fastlanereader;

import java.awt.Color;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.Icon;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.auftrag.fastlanereader.generated.FLRAuftrag;
import com.lp.server.auftrag.service.AuftragDto;
import com.lp.server.auftrag.service.AuftragFac;
import com.lp.server.auftrag.service.AuftragServiceFac;
import com.lp.server.benutzer.service.RechteFac;
import com.lp.server.bestellung.service.BestellpositionFac;
import com.lp.server.inserat.fastlanereader.generated.FLRInserat;
import com.lp.server.inserat.fastlanereader.generated.FLRInserater;
import com.lp.server.inserat.fastlanereader.generated.FLRInseratrechnung;
import com.lp.server.inserat.service.InseratDto;
import com.lp.server.inserat.service.InseratFac;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.partner.service.LieferantFac;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.system.fastlanereader.generated.FLRLandplzort;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocNodeAuftrag;
import com.lp.server.system.jcr.service.docnode.DocNodeInserat;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.service.PanelFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemFac;
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
 * Hier wird die FLR Funktionalitaet fuer den Auftrag implementiert. Pro UseCase
 * gibt es einen Handler.
 * </p>
 * <p>
 * Copright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * <p>
 * Erstellungsdatum 2004-08-14
 * </p>
 * <p>
 * </p>
 * 
 * @author martin werner, uli walch
 * @version 1.0
 */

public class InseratHandler extends UseCaseHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String FLR_INSERAT = "flrinserat.";
	public static final String FLR_INSERAT_FROM_CLAUSE = " from FLRInserat flrinserat  left outer join flrinserat.rechnungset re WHERE re.i_sort=1 ";

	public final String ORDER_KUNDE = "re.flrkunde.flrpartner.c_kbez";
	public final String ORDER_RECHNUNG = "re.flrrechnungposition.flrrechnung.c_nr";

	boolean bSuchenInklusiveKbez = true;

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
		QueryResult result = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			int colCount = getTableInfo().getColumnClasses().length;
			int pageSize = PAGE_SIZE;
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
			int row = 0;
			int col = 0;

			String[] tooltipData = new String[resultList.size()];
			
			// darf Preise sehen.
			final boolean bDarfPreiseSehen = getTheJudgeFac().hatRecht(
					RechteFac.RECHT_LP_DARF_PREISE_SEHEN_VERKAUF, theClientDto);

			while (resultListIterator.hasNext()) {
				FLRInserat inserat = (FLRInserat) resultListIterator.next();

				FLRInseratrechnung inseratrechnung = null;
				Set s = inserat.getRechnungset();

				String subStatusBezahlt = null;

				if (inserat.getC_nr().equals("13/M000738")) {
					int u = 0;
				}

				if (s.size() > 0) {
					Iterator it = s.iterator();

					boolean bAlleBezahlt = true;
					while (it.hasNext()) {
						FLRInseratrechnung ir = (FLRInseratrechnung) it.next();
						if (inseratrechnung == null) {
							inseratrechnung = ir;
						}

						if (ir.getFlrrechnungposition() == null
								|| !ir.getFlrrechnungposition()
										.getFlrrechnung().getStatus_c_nr()
										.equals(RechnungFac.STATUS_BEZAHLT)) {
							bAlleBezahlt = false;
						}

						if (ir.getFlrrechnungposition() != null
								&& ir.getFlrrechnungposition().getFlrrechnung()
										.getStatus_c_nr()
										.equals(RechnungFac.STATUS_TEILBEZAHLT)) {
							subStatusBezahlt = RechnungFac.STATUS_TEILBEZAHLT;
							break;
						}

					}

					if (bAlleBezahlt == true) {
						subStatusBezahlt = RechnungFac.STATUS_BEZAHLT;
					}

				}

				rows[row][col++] = inserat.getI_id();
				rows[row][col++] = inserat.getC_nr();

				if (inseratrechnung != null) {
					rows[row][col++] = inseratrechnung.getFlrkunde()
							.getFlrpartner().getC_kbez();
				} else {
					rows[row][col++] = null;
				}

				rows[row][col++] = inserat.getT_belegdatum();

				rows[row][col++] = inserat.getC_stichwort();
				rows[row][col++] = inserat.getFlrlieferant().getFlrpartner()
						.getC_kbez();
				rows[row][col++] = inserat.getT_termin();
				rows[row][col++] = inserat.getFlrartikel_inseratart().getC_nr();

				if (inserat.getFlrbestellposition() != null) {
					rows[row][col++] = inserat.getFlrbestellposition()
							.getFlrbestellung().getC_nr();
				} else {
					rows[row][col++] = null;
				}

				if (inserat.getT_erschienen() != null) {
					rows[row][col++] = Boolean.TRUE;
				} else {
					rows[row][col++] = Boolean.FALSE;
				}
				rows[row][col++] = inserat.getN_menge();

				if (bDarfPreiseSehen) {
					rows[row][col++] = inserat.getN_nettoeinzelpreis_vk();
				} else {
					rows[row][col++] = null;
				}

				rows[row][col++] = inserat.getStatus_c_nr();
				rows[row][col++] = subStatusBezahlt;

				if (inseratrechnung != null
						&& inseratrechnung.getFlrrechnungposition() != null) {
					rows[row][col++] = inseratrechnung.getFlrrechnungposition()
							.getFlrrechnung().getC_nr();
				} else {
					rows[row][col++] = null;
				}

				Set sEr = inserat.getErset();
				if (sEr.size() > 0) {
					FLRInserater inserater = (FLRInserater) sEr.iterator()
							.next();
					rows[row][col++] = inserater.getFlreingangsrechnung()
							.getC_nr();
				} else {
					rows[row][col++] = null;
				}

			String tooltip="";
			
			if (inserat.getC_rubrik() != null) {
				tooltip+=inserat.getC_rubrik()+" ";
			}
			
			if (inserat.getC_bez() != null) {
				if (inserat.getC_rubrik() != null) {
					tooltip+=" | ";
				}
				tooltip+=inserat.getC_bez()+" ";
				
				
				
			}
		
			
				if (inserat.getX_anhang() != null) {
					String text = "<b>" + tooltip
							+ ":</b>\n" + inserat.getX_anhang();
					text = text.replaceAll("\n", "<br>");
					text = "<html>" + text + "</html>";
					tooltip=text;
				} else {
					String text = "<b>" + tooltip
							+ "</b>\n" ;
					text = "<html>" + text + "</html>";
					tooltip=text;
				}
				tooltipData[row] = tooltip;
				row++;

				col = 0;
			}
			result = new QueryResult(rows, this.getRowCount(), startIndex,
					endIndex, 0,tooltipData);
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
			String queryString = "select count(*) from FLRInserat  as flrinserat  left outer join flrinserat.rechnungset re WHERE re.i_sort=1 "
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
					// wenn nach der c_nr gefilter wird, wird das kriterium
					// veraendert
					if (filterKriterien[i].kritName.equals("c_nr")) {
						try {
							String sValue = super.buildWhereBelegnummer(
									filterKriterien[i], false);
							// Belegnummernsuche auch in "altem" Jahr, wenn im
							// neuen noch keines vorhanden ist
							if (!istBelegnummernInJahr("FLRInserat", sValue)) {
								sValue = super.buildWhereBelegnummer(
										filterKriterien[i], true);
							}
							where.append(" " + FLR_INSERAT
									+ filterKriterien[i].kritName);
							where.append(" " + filterKriterien[i].operator);
							where.append(" " + sValue);
						} catch (Exception ex) {
							throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR,
									ex);
						}
					} else if (filterKriterien[i].kritName
							.equals("filter_kunde")) {

						if (bSuchenInklusiveKbez) {
							where.append(" (lower(re.flrkunde.flrpartner.c_name1nachnamefirmazeile1)");
							where.append(" " + filterKriterien[i].operator);
							where.append(" "
									+ filterKriterien[i].value.toLowerCase());
							where.append(" OR lower(re.flrkunde.flrpartner.c_name2vornamefirmazeile2)");
							where.append(" " + filterKriterien[i].operator);
							where.append(" "
									+ filterKriterien[i].value.toLowerCase());
							where.append(" OR lower(re.flrkunde.flrpartner.c_kbez)");
							where.append(" " + filterKriterien[i].operator);
							where.append(" "
									+ filterKriterien[i].value.toLowerCase()
									+ ")");
						} else if (bSuchenInklusiveKbez == false) {
							where.append(" (lower(re.flrkunde.flrpartner.c_name1nachnamefirmazeile1)");
							where.append(" " + filterKriterien[i].operator);
							where.append(" "
									+ filterKriterien[i].value.toLowerCase());
							where.append(" OR lower(re.flrkunde.flrpartner.c_name2vornamefirmazeile2)");
							where.append(" " + filterKriterien[i].operator);
							where.append(" "
									+ filterKriterien[i].value.toLowerCase()
									+ ")");
						}

					} else if (filterKriterien[i].kritName
							.equals("flrlieferant.flrpartner.c_name1nachnamefirmazeile1")) {
						
						if (bSuchenInklusiveKbez) {
							where.append(" (lower(flrinserat.flrlieferant.flrpartner.c_name1nachnamefirmazeile1)");
							where.append(" " + filterKriterien[i].operator);
							where.append(" "
									+ filterKriterien[i].value.toLowerCase());
							where.append(" OR lower(flrinserat.flrlieferant.flrpartner.c_name2vornamefirmazeile2)");
							where.append(" " + filterKriterien[i].operator);
							where.append(" "
									+ filterKriterien[i].value.toLowerCase());
							where.append(" OR lower(flrinserat.flrlieferant.flrpartner.c_kbez)");
							where.append(" " + filterKriterien[i].operator);
							where.append(" "
									+ filterKriterien[i].value.toLowerCase()
									+ ")");
						} else if (bSuchenInklusiveKbez == false) {
							where.append(" (lower(flrinserat.flrlieferant.flrpartner.c_name1nachnamefirmazeile1)");
							where.append(" " + filterKriterien[i].operator);
							where.append(" "
									+ filterKriterien[i].value.toLowerCase());
							where.append(" OR lower(flrinserat.flrlieferant.flrpartner.c_name2vornamefirmazeile2)");
							where.append(" " + filterKriterien[i].operator);
							where.append(" "
									+ filterKriterien[i].value.toLowerCase()
									+ ")");
						}

					} else {
						if (filterKriterien[i].isBIgnoreCase()) {
							where.append(" lower(" + FLR_INSERAT
									+ filterKriterien[i].kritName + ")");
						} else {
							where.append(" " + FLR_INSERAT
									+ filterKriterien[i].kritName);
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
			}
			if (filterAdded) {
				where.insert(0, " AND");
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

							if (kriterien[i].kritName.equals(ORDER_KUNDE)
									|| kriterien[i].kritName
											.equals(ORDER_RECHNUNG)) {
								orderBy.append(kriterien[i].kritName);
							} else {
								orderBy.append(FLR_INSERAT
										+ kriterien[i].kritName);
							}

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
				orderBy.append(FLR_INSERAT).append("c_nr").append(" DESC ");
				sortAdded = true;
			}
			if (orderBy.indexOf(FLR_INSERAT + "i_id") < 0) {
				// Martin Werner original: "unique sort required because
				// otherwise rowNumber of selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt()."
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" ").append(FLR_INSERAT).append("i_id")
						.append(" DESC ");
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
		return "SELECT flrinserat from FLRInserat  as flrinserat left outer join flrinserat.rechnungset re WHERE re.i_sort=1 ";

	}

	public QueryResult sort(SortierKriterium[] sortierKriterien,
			Object selectedId) throws EJBExceptionLP {
		QueryResult result = null;

		try {
			int rowNumber = 0;

			getQuery().setSortKrit(sortierKriterien);

			if (selectedId != null && ((Integer) selectedId).intValue() >= 0) {
				SessionFactory factory = FLRSessionFactory.getFactory();
				Session session = null;

				try {
					session = factory.openSession();
					String queryString = "select " + FLR_INSERAT + "i_id"
							+ FLR_INSERAT_FROM_CLAUSE + this.buildWhereClause()
							+ this.buildOrderByClause();
					Query query = session.createQuery(queryString);
					ScrollableResults scrollableResult = query.scroll();
					if (scrollableResult != null) {
						scrollableResult.beforeFirst();
						while (scrollableResult.next()) {
							Integer id = (Integer) scrollableResult
									.getInteger(0);
							if (selectedId.equals(id)) {
								rowNumber = scrollableResult.getRowNumber();
								break;
							}
						}
					}
				} finally {
					try {
						session.close();
					} catch (HibernateException he) {
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_HIBERNATE, he);
					}
				}
			}

			if (rowNumber < 0 || rowNumber >= this.getRowCount()) {
				rowNumber = 0;
			}

			result = this.getPageAt(new Integer(rowNumber));
			result.setIndexOfSelectedRow(rowNumber);
		} catch (Exception ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}

		return result;
	}

	public TableInfo getTableInfo() {

		if (super.getTableInfo() == null) {
			int iNachkommastellenPreisVK = 2;
			try {
				ParametermandantDto parameter = getParameterFac()
						.getMandantparameter(theClientDto.getMandant(),
								ParameterFac.KATEGORIE_ALLGEMEIN,
								ParameterFac.PARAMETER_SUCHEN_INKLUSIVE_KBEZ);
				bSuchenInklusiveKbez = (java.lang.Boolean) parameter
						.getCWertAsObject();
				iNachkommastellenPreisVK = getMandantFac()
						.getNachkommastellenPreisVK(theClientDto.getMandant());
			} catch (RemoteException ex) {
				throwEJBExceptionLPRespectOld(ex);
			}
			setTableInfo(new TableInfo(
					new Class[] { Integer.class, String.class, String.class,
							java.util.Date.class, String.class, String.class,
							java.util.Date.class, String.class, String.class,
							Boolean.class, BigDecimal.class, super.getUIClassBigDecimalNachkommastellen(iNachkommastellenPreisVK),
							Icon.class, Icon.class, String.class, String.class },
					new String[] {
							"i_id",
							getTextRespectUISpr("iv.inserat",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("lp.kunde",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("lp.datum",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("iv.stichwort",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("lp.lieferant",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("lp.termin",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("lp.art",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("lp.bestellnummer",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("iv.erschienen",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("lp.menge",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("lp.preis",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("lp.status",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("iv.substatusbezahlt",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),
							getTextRespectUISpr("rechnung.rechnungsnummer",
									theClientDto.getMandant(),
									theClientDto.getLocUi()),

							getTextRespectUISpr("er.eingangsrechnungsnummer",
									theClientDto.getMandant(),
									theClientDto.getLocUi()), },
					new int[] {
							QueryParameters.FLR_BREITE_SHARE_WITH_REST, // diese
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_M,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_XXS,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_XXS,
							QueryParameters.FLR_BREITE_XXS,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST,
							QueryParameters.FLR_BREITE_SHARE_WITH_REST },
					new String[] {
							"i_id",
							"c_nr",
							ORDER_KUNDE,
							InseratFac.FLR_INSERAT_T_BELEGDATUM,
							InseratFac.FLR_INSERAT_C_STICHWORT,
							InseratFac.FLR_INSERAT_FLRLIEFERANT
									+ "."
									+ KundeFac.FLR_PARTNER
									+ "."
									+ PartnerFac.FLR_PARTNER_C_KBEZ,
							InseratFac.FLR_INSERAT_T_TERMIN,
							InseratFac.FLR_INSERAT_FLRARTIKEL_INSERATART
									+ ".c_nr",
							InseratFac.FLR_INSERAT_FLRBESTELLPOSITION
									+ "."
									+ BestellpositionFac.FLR_BESTELLPOSITION_FLRBESTELLUNG
									+ ".c_nr",
							InseratFac.FLR_INSERAT_T_ERSCHIENEN,
							InseratFac.FLR_INSERAT_N_MENGE,
							InseratFac.FLR_INSERAT_N_NETTOEINZELPRESI_VK,
							InseratFac.FLR_INSERAT_STATUS_C_NR,
							Facade.NICHT_SORTIERBAR, Facade.NICHT_SORTIERBAR,
							Facade.NICHT_SORTIERBAR }));
		}

		return super.getTableInfo();
	}

	public PrintInfoDto getSDocPathAndPartner(Object key) {
		InseratDto inseratDto = null;
		KundeDto kundeDto = null;
		Integer partnerIId = null;
		try {
			inseratDto = getInseratFac().inseratFindByPrimaryKey((Integer) key);
			/*
			 * partnerIId = getKundeFac().kundeFindByPrimaryKey(
			 * inseratDto.getKundeIId(), theClientDto).getPartnerIId();
			 */
		} catch (EJBExceptionLP e) {

		}

		if (inseratDto != null) {

			DocPath docPath = new DocPath(new DocNodeInserat(inseratDto));

			return new PrintInfoDto(docPath, partnerIId, getSTable());
		} else {
			return null;
		}
	}

	public String getSTable() {
		return "AUFTRAG";
	}
}
