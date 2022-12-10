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
package com.lp.server.reklamation.fastlanereader;

import java.awt.Color;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.swing.Icon;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.auftrag.fastlanereader.AuftragHandler;
import com.lp.server.auftrag.service.AuftragFac;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.partner.service.LieferantFac;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.reklamation.fastlanereader.generated.FLRReklamation;
import com.lp.server.reklamation.service.ReklamationDto;
import com.lp.server.reklamation.service.ReklamationFac;
import com.lp.server.system.fastlanereader.service.TableColumnInformation;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.jcr.service.docnode.DocNodeReklamation;
import com.lp.server.system.jcr.service.docnode.DocPath;
import com.lp.server.system.service.MandantFac;
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

/**
 * <p>
 * Hier wird die FLR Funktionalit&auml;t f&uuml;r die Montageart implementiert.
 * Pro UseCase gibt es einen Handler.
 * </p>
 * <p>
 * Copright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * <p>
 * Erstellungsdatum 2005-02-14
 * </p>
 * <p>
 * </p>
 * 
 * @author ck
 * @version 1.0
 */
public class ReklamationHandler extends UseCaseHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	boolean bProjekttitelInAG_AB = false;

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
			session = setFilter(session);
			String queryString = this.getFromClause() + this.buildWhereClause() + this.buildOrderByClause();
			Query query = session.createQuery(queryString);
			query.setFirstResult(startIndex);
			query.setMaxResults(pageSize);
			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();
			Object[][] rows = new Object[resultList.size()][colCount];
			int row = 0;

			while (resultListIterator.hasNext()) {

				Object[] o = (Object[]) resultListIterator.next();

				FLRReklamation reklamation = (FLRReklamation) o[0];

				Object[] rowToAddCandidate = new Object[colCount];

				rowToAddCandidate[getTableColumnInformation().getViewIndex("i_id")] = reklamation.getI_id();
				rowToAddCandidate[getTableColumnInformation().getViewIndex("bes.belegartnummer")] = reklamation
						.getC_nr();

				if (reklamation.getFlrlos() != null) {
					if (reklamation.getFlrlos().getFlrauftrag() != null) {
						rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.kunde")] = reklamation
								.getFlrlos().getFlrauftrag().getFlrkunde().getFlrpartner()
								.getC_name1nachnamefirmazeile1();

					} else {
						if (reklamation.getFlrkunde() != null) {
							rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.kunde")] = reklamation
									.getFlrkunde().getFlrpartner().getC_name1nachnamefirmazeile1();

						}

					}
				} else {
					if (reklamation.getFlrkunde() != null) {
						rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.kunde")] = reklamation
								.getFlrkunde().getFlrpartner().getC_name1nachnamefirmazeile1();

					}
				}

				if (reklamation.getFlrlieferant() != null) {
					rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.lieferant")] = reklamation
							.getFlrlieferant().getFlrpartner().getC_name1nachnamefirmazeile1();

				}

				if (bMaschinenzeiterfassung) {
					if (reklamation.getFlrmaschine() != null) {
						rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.maschinengruppe")] = reklamation
								.getFlrmaschine().getFlrmaschinengruppe().getC_bez();
					}
				}

				if (reklamation.getFlrartikel() != null) {
					rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.artikel")] = reklamation
							.getFlrartikel().getC_nr();
					rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.bezeichnung")] = o[1];

				} else {
					rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.bezeichnung")] = reklamation
							.getC_handartikel();
				}

				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.grund")] = reklamation.getC_grund();

				rowToAddCandidate[getTableColumnInformation().getViewIndex("rekla.kdreklanr")] = reklamation
						.getC_kdreklanr();
				rowToAddCandidate[getTableColumnInformation().getViewIndex("rekla.kndlsnr")] = reklamation
						.getC_kdlsnr();

				rowToAddCandidate[getTableColumnInformation().getViewIndex("bes.belegdatum")] = reklamation
						.getT_belegdatum();
				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.status")] = getStatusMitUebersetzung(
						reklamation.getStatus_c_nr());
				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.kommentar")] = getKommentarart(
						reklamation.getX_kommentar());

				rows[row] = rowToAddCandidate;

				row++;
			}
			result = new QueryResult(rows, this.getRowCount(), startIndex, endIndex, 0);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
		} finally {
			try {
				session.close();
			} catch (HibernateException he) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, he);
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
			session = setFilter(session);
			String queryString = "select count(*) from FLRReklamation reklamation left join reklamation.flrkunde.flrpartner as flrkd "
					+ " left join reklamation.flrlieferant.flrpartner as flrlf "
					+ " left join reklamation.flrprojekt as flrprojekt "
					+ "  LEFT OUTER JOIN reklamation.flrartikel.artikelsprset AS aspr " + this.buildWhereClause();
			Query query = session.createQuery(queryString);
			List<?> rowCountResult = query.list();
			if (rowCountResult != null && rowCountResult.size() > 0) {
				rowCount = ((Long) rowCountResult.get(0)).longValue();
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
		return rowCount;
	}

	private void buildKundeLieferantFilter(FilterKriterium filterKriterium, StringBuffer where) {
		ParametermandantDto parameter = null;
		try {
			parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_SUCHEN_INKLUSIVE_KBEZ);
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		Boolean bSuchenInklusiveKbez = (java.lang.Boolean) parameter.getCWertAsObject();
		
		if (bSuchenInklusiveKbez) {

			where.append(" ((lower(flrkd.c_name1nachnamefirmazeile1)");
			where.append(" " + filterKriterium.operator);
			where.append(" " + filterKriterium.value.toLowerCase());
			where.append(" OR lower(flrkd.c_kbez)");
			where.append(" " + filterKriterium.operator);
			where.append(" " + filterKriterium.value.toLowerCase() + ")");

			where.append(" OR (lower(flrlf.c_name1nachnamefirmazeile1)");
			where.append(" " + filterKriterium.operator);
			where.append(" " + filterKriterium.value.toLowerCase());
			where.append(" OR lower(flrlf.c_kbez)");
			where.append(" " + filterKriterium.operator);
			where.append(" " + filterKriterium.value.toLowerCase() + "))");

		} else {
			
			
			where.append(" (lower(flrkd.c_name1nachnamefirmazeile1)");
			where.append(" " + filterKriterium.operator);
			where.append(" " + filterKriterium.value.toLowerCase());
			where.append(" OR lower(flrlf.c_name1nachnamefirmazeile1)");
			where.append(" " + filterKriterium.operator);
			where.append(" " + filterKriterium.value.toLowerCase() + ")");
			
	

		}
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

					if (filterKriterien[i].kritName.equals("c_nr")) {
						try {
							String sValue = super.buildWhereBelegnummer(filterKriterien[i], false);
							// Belegnummernsuche auch in "altem" Jahr, wenn im
							// neuen noch keines vorhanden ist
							if (!istBelegnummernInJahr("FLRReklamation", sValue)) {
								sValue = super.buildWhereBelegnummer(filterKriterien[i], true);
							}
							where.append(" " + "reklamation." + filterKriterien[i].kritName);
							where.append(" " + filterKriterien[i].operator);
							where.append(" " + sValue);
						} catch (Throwable ex) {
							throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, new Exception(ex));
						}
					} else if (filterKriterien[i].kritName.equals("c_projekt")) {

						where.append("");
						where.append(buildWhereClauseExtendedSearch(Arrays.asList(filterKriterien[i].value.split(" ")),
								"reklamation." + filterKriterien[i].kritName, filterKriterien[i].isBIgnoreCase()));
					} else if (filterKriterien[i].kritName.equals("c_grund")) {

						where.append("");
						where.append(buildWhereClauseExtendedSearch(Arrays.asList(filterKriterien[i].value.split(" ")),
								"reklamation." + filterKriterien[i].kritName, filterKriterien[i].isBIgnoreCase()));
					} else if (filterKriterien[i].kritName.equals("x_grund_lang")) {

						where.append(" (");
						where.append(buildWhereClauseExtendedSearch(Arrays.asList(filterKriterien[i].value.split(" ")),
								"reklamation." + filterKriterien[i].kritName, filterKriterien[i].isBIgnoreCase()));

						// 19915
						if (bProjekttitelInAG_AB) {
							where.append(" OR ");
							where.append(
									buildWhereClauseExtendedSearch(Arrays.asList(filterKriterien[i].value.split(" ")),
											"flrprojekt.c_titel", filterKriterien[i].isBIgnoreCase()));
						}

						where.append(") ");

					} else if (filterKriterien[i].kritName.equals("c_kdreklanr")) {

						where.append(" ( reklamation.c_kdreklanr LIKE " + filterKriterien[i].value
								+ " OR reklamation.c_kdlsnr LIKE " + filterKriterien[i].value + ")");

					} else if (filterKriterien[i].kritName.equals("KUNDE_LIEFERANT")) {

						buildKundeLieferantFilter(filterKriterien[i], where);

					} else {

						if (filterKriterien[i].isBIgnoreCase()) {
							where.append(" upper(reklamation." + filterKriterien[i].kritName + ")");
						} else {
							where.append(" reklamation." + filterKriterien[i].kritName);
						}
						where.append(" " + filterKriterien[i].operator);
						if (filterKriterien[i].isBIgnoreCase()) {
							where.append(" " + filterKriterien[i].value.toUpperCase());
						} else {
							where.append(" " + filterKriterien[i].value);
						}
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
					if (!kriterien[i].kritName.endsWith(Facade.NICHT_SORTIERBAR)) {
						if (kriterien[i].isKrit) {
							if (sortAdded) {
								orderBy.append(", ");
							}
							sortAdded = true;
							orderBy.append("reklamation." + kriterien[i].kritName);
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
				orderBy.append("reklamation.c_nr DESC ");
				sortAdded = true;
			}
			if (orderBy.indexOf("reklamation.c_nr ") < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" reklamation.c_nr ");
				sortAdded = true;
			}
			if (sortAdded) {
				orderBy.insert(0, " ORDER BY ");
			}
		}
		return orderBy.toString();
	}

	private boolean bMaschinenzeiterfassung = false;

	/**
	 * get the basic from clause for the HQL statement.
	 * 
	 * @return the from clause.
	 */
	private String getFromClause() {
		return "SELECT reklamation, aspr.c_bez from FLRReklamation reklamation "

				+ " left join reklamation.flrkunde.flrpartner as flrkd "
				+ " left join reklamation.flrlieferant.flrpartner as flrlf "
				+ " left join reklamation.flrmaschine as flrmaschine "
				+ " left join reklamation.flrprojekt as flrprojekt "
				+ "  LEFT OUTER JOIN reklamation.flrartikel.artikelsprset AS aspr ";

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
				session = setFilter(session);
				String queryString = "select reklamation.i_id from FLRReklamation reklamation left join reklamation.flrkunde.flrpartner as flrkd "
						+ " left join reklamation.flrlieferant.flrpartner as flrlf "
						+ " left join reklamation.flrmaschine as flrmaschine "
						+ " left join reklamation.flrprojekt as flrprojekt "
						+ "  LEFT OUTER JOIN reklamation.flrartikel.artikelsprset AS aspr " + this.buildWhereClause()
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
				try {
					session.close();
				} catch (HibernateException he) {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, he);
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

	private TableColumnInformation createColumnInformation(String mandant, Locale locUi) {

		TableColumnInformation columns = new TableColumnInformation();

		columns.add("i_id", Integer.class, "i_id", QueryParameters.FLR_BREITE_SHARE_WITH_REST, "i_id");
		columns.add("bes.belegartnummer", String.class, getTextRespectUISpr("bes.belegartnummer", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST, "c_nr");
		columns.add("lp.kunde", String.class, getTextRespectUISpr("lp.kunde", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST, ReklamationFac.FLR_REKLAMATION_FRLKUNDE + "."
						+ KundeFac.FLR_PARTNER + "." + PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1);
		columns.add("lp.lieferant", String.class, getTextRespectUISpr("lp.lieferant", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST, ReklamationFac.FLR_REKLAMATION_FLRLIEFERANT + "."
						+ LieferantFac.FLR_PARTNER + "." + PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1);

		if (bMaschinenzeiterfassung) {
			columns.add("lp.maschinengruppe", String.class, getTextRespectUISpr("lp.maschinengruppe", mandant, locUi),
					QueryParameters.FLR_BREITE_SHARE_WITH_REST, "flrmaschine.flrmaschinengruppe.c_bez");
		}

		columns.add("lp.artikel", String.class, getTextRespectUISpr("lp.artikel", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST, ReklamationFac.FLR_REKLAMATION_FLRARTIKEL + ".c_nr");
		columns.add("lp.bezeichnung", String.class, getTextRespectUISpr("lp.bezeichnung", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST, Facade.NICHT_SORTIERBAR);
		columns.add("lp.grund", String.class, getTextRespectUISpr("lp.grund", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST, ReklamationFac.FLR_REKLAMATION_C_GRUND);

		columns.add("rekla.kdreklanr", String.class, getTextRespectUISpr("rekla.kdreklanr", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST, "c_kdreklanr");
		columns.add("rekla.kndlsnr", String.class, getTextRespectUISpr("rekla.kndlsnr", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST, "c_kdlsnr");

		columns.add("bes.belegdatum", java.util.Date.class, getTextRespectUISpr("bes.belegdatum", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST, ReklamationFac.FLR_REKLAMATION_T_BELEGDATUM);
		columns.add("lp.status", Icon.class, getTextRespectUISpr("lp.status", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST, ReklamationFac.FLR_REKLAMATION_STATUS_C_NR);
		columns.add("lp.kommentar", String.class, getTextRespectUISpr("lp.kommentar", mandant, locUi), 1,
				Facade.NICHT_SORTIERBAR);

		return columns;
	}

	public TableInfo getTableInfo() {

		TableInfo info = super.getTableInfo();
		if (info != null)
			return info;

		bMaschinenzeiterfassung = getMandantFac()
				.hatZusatzfunktionberechtigung(MandantFac.ZUSATZFUNKTION_MASCHINENZEITERFASSUNG, theClientDto);

		try {
			ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_PROJEKT_TITEL_IN_AG_AB_PROJEKT);
			bProjekttitelInAG_AB = (Boolean) parameter.getCWertAsObject();

		} catch (RemoteException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}

		setTableColumnInformation(createColumnInformation(theClientDto.getMandant(), theClientDto.getLocUi()));

		TableColumnInformation c = getTableColumnInformation();
		info = new TableInfo(c.getClasses(), c.getHeaderNames(), c.getWidths(), c.getDbColumNames(),
				c.getHeaderToolTips());
		setTableInfo(info);
		return info;

	}

	public PrintInfoDto getSDocPathAndPartner(Object key) {
		ReklamationDto reklamationDto = null;
		KundeDto kundeDto = null;
		PartnerDto partnerDto = null;
		try {
			reklamationDto = getReklamationFac().reklamationFindByPrimaryKey((Integer) key);
			kundeDto = getKundeFac().kundeFindByPrimaryKey(reklamationDto.getKundeIId(), theClientDto);
			partnerDto = getPartnerFac().partnerFindByPrimaryKey(kundeDto.getPartnerIId(), theClientDto);
		} catch (Exception e) {
			// Nicht gefunden
		}
		if (reklamationDto != null) {
			// String sPath = JCRDocFac.HELIUMV_NODE + "/"
			// + theClientDto.getMandant() + "/"
			// + LocaleFac.BELEGART_REKLAMATION.trim() + "/"
			// + LocaleFac.BELEGART_REKLAMATION.trim() + "/"
			// + reklamationDto.getCNr().replace("/", ".");
			DocPath docPath = new DocPath(new DocNodeReklamation(reklamationDto));
			Integer iPartnerIId = null;
			if (partnerDto != null) {
				iPartnerIId = partnerDto.getIId();
			}
			return new PrintInfoDto(docPath, iPartnerIId, null);
		} else {
			return null;
		}
	}

	public String getSTable() {
		return "REKLAMATION";
	}
}
