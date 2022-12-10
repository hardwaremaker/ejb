
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
package com.lp.server.personal.fastlanereader;

import java.awt.Color;
import java.math.BigDecimal;
import java.rmi.RemoteException;
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
import com.lp.server.lieferschein.service.LieferscheinFac;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.partner.service.LieferantFac;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.personal.fastlanereader.generated.FLRTelefontodo;
import com.lp.server.personal.fastlanereader.generated.FLRTelefonzeiten;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.personal.service.ZeiterfassungFacAll;
import com.lp.server.system.fastlanereader.service.TableColumnInformation;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.SystemFac;
import com.lp.server.util.Facade;
import com.lp.server.util.HelperServer;
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
 * Hier wird die FLR Funktionalit&auml;t f&uuml;r den Personal implementiert.
 * Pro UseCase gibt es einen Handler.
 * </p>
 * <p>
 * Copright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * <p>
 * Erstellungsdatum 2004-11-02
 * </p>
 * <p>
 * </p>
 * 
 * @author ck
 * @version 1.0
 */

public class TelefonzeitenTodoHandler extends UseCaseHandler {

	boolean bKurzzeichenStattName = false;

	private static final long serialVersionUID = 1L;

	public QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP {
		QueryResult result = null;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			int colCount = getTableInfo().getColumnClasses().length;
			int pageSize = TelefonzeitenHandler.PAGE_SIZE;
			int startIndex = Math.max(rowIndex.intValue() - (pageSize / 2), 0);
			int endIndex = startIndex + pageSize - 1;

			session = factory.openSession();
			String queryString = this.getFromClause() + this.buildWhereClause() + this.buildOrderByClause();
			Query query = session.createQuery(queryString);
			query.setFirstResult(startIndex);
			query.setMaxResults(pageSize);
			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();
			Object[][] rows = new Object[resultList.size()][colCount];
			int row = 0;
			int col = 0;

			while (resultListIterator.hasNext()) {

				Object[] o = (Object[]) resultListIterator.next();

				FLRTelefontodo reise = (FLRTelefontodo) o[0];

				Object[] rowToAddCandidate = new Object[colCount];
				rowToAddCandidate[getTableColumnInformation().getViewIndex("c_nr")] = reise.getC_nr();
				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.von")] = reise.getT_von();
				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.bis")] = reise.getT_bis();

				if (reise.getFlrpartner() != null) {
					rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.partner")] = HelperServer
							.formatAdresseEinesFLRPartner(reise.getFlrpartner());
				}

				if (reise.getFlransprechpartner() != null) {
					rowToAddCandidate[getTableColumnInformation().getViewIndex("part.ansprechpartner")] = HelperServer
							.formatPersonAusFLRPartner(reise.getFlransprechpartner().getFlrpartneransprechpartner());
				}

				String projektbez = "";

				if (reise.getProjekt_i_id() != null) {
					rowToAddCandidate[getTableColumnInformation().getViewIndex("proj.projekt")] = reise.getFlrprojekt()
							.getC_nr();
					projektbez = reise.getFlrprojekt().getC_titel();

				}

				if (reise.getAngebot_i_id() != null) {
					rowToAddCandidate[getTableColumnInformation().getViewIndex("angb.angebot")] = reise.getFlrangebot()
							.getC_nr();
					projektbez = reise.getFlrangebot().getC_bez();
				}

				if (reise.getAuftrag_i_id() != null) {
					rowToAddCandidate[getTableColumnInformation().getViewIndex("auft.auftrag")] = reise.getFlrauftrag()
							.getC_nr();
					projektbez = reise.getFlrauftrag().getC_bez();
				}

				rowToAddCandidate[getTableColumnInformation().getViewIndex("proj.projektbez")] = projektbez;

				if (bKurzzeichenStattName) {
					rowToAddCandidate[getTableColumnInformation().getViewIndex("proj.personal.zugewiesener")] = reise
							.getFlrpersonal_zugewiesener().getC_kurzzeichen();
				} else {
					rowToAddCandidate[getTableColumnInformation()
							.getViewIndex("proj.personal.zugewiesener")] = HelperServer
									.formatPersonAusFLRPErsonal(reise.getFlrpersonal_zugewiesener());

				}

				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.wiedervorlage")] = reise
						.getT_wiedervorlage();
				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.erledigt")] = reise
						.getT_wiedervorlage_erledigt();

				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.titel")] = reise.getC_titel();

				rows[row] = rowToAddCandidate;

				row++;
				col = 0;
			}
			result = new QueryResult(rows, this.getRowCount(), startIndex, endIndex, 0);
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
			try {
				session.close();
			} catch (HibernateException he) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, he);
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

					if (filterKriterien[i].kritName.equals(ZeiterfassungFac.TELEFONZEITEN_TODO_ANSPRECHPARTNER)) {
						where.append(
								" (lower(telefonzeiten.flransprechpartner.flrpartneransprechpartner.c_name1nachnamefirmazeile1) ");
						where.append(" " + filterKriterien[i].operator);
						where.append(" " + filterKriterien[i].value.toLowerCase());
						where.append(
								" OR lower(telefonzeiten.flransprechpartner.flrpartneransprechpartner.c_name2vornamefirmazeile2)");
						where.append(" " + filterKriterien[i].operator);
						where.append(" " + filterKriterien[i].value.toLowerCase() + ")");
					} else if (filterKriterien[i].kritName
							.equals("telefonzeiten.flrpartner." + PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1)) {

						where.append(" (lower(telefonzeiten.flrpartner."
								+ PartnerFac.FLR_PARTNER_NAME1NACHNAMEFIRMAZEILE1 + ")");
						where.append(" " + filterKriterien[i].operator);
						where.append(" " + filterKriterien[i].value.toLowerCase());
						where.append(" OR lower(telefonzeiten.flrpartner.c_name2vornamefirmazeile2)");
						where.append(" " + filterKriterien[i].operator);
						where.append(" " + filterKriterien[i].value.toLowerCase() + ")");

					} else if (filterKriterien[i].kritName.equals("COMBO")) {

						if (filterKriterien[i].value
								.equals(ZeiterfassungFacAll.TELEFONZEITEN_TODO_OPTION_OFFENE_WIEDERVORLAGEN)) {
							where.append(
									" telefonzeiten.t_wiedervorlage IS NOT NULL AND telefonzeiten.t_wiedervorlage_erledigt IS NULL ");
						}
						if (filterKriterien[i].value
								.equals(ZeiterfassungFacAll.TELEFONZEITEN_TODO_OPTION_ALLE_WIEDERVORLAGEN)) {
							where.append(" telefonzeiten.t_wiedervorlage IS NOT NULL ");
						}
						if (filterKriterien[i].value
								.equals(ZeiterfassungFacAll.TELEFONZEITEN_TODO_OPTION_ALLE_TELEFONZEITEN)) {
							where.append(" 1=1 ");
						}

					} else if (filterKriterien[i].kritName
							.equals("telefonzeiten.flrpersonal_zugewiesener.flrpartner.c_name1nachnamefirmazeile1")) {

						where.append(
								"( lower(telefonzeiten.flrpersonal_zugewiesener.flrpartner.c_name1nachnamefirmazeile1) LIKE "
										+ filterKriterien[i].value
										+ " OR  lower(telefonzeiten.flrpersonal_zugewiesener.flrpartner.c_name2vornamefirmazeile2) LIKE "+filterKriterien[i].value.toLowerCase()+")");

					} else if (filterKriterien[i].kritName.equals(ZeiterfassungFac.TELEFONZEITEN_TODO_TEXT)) {

						String suchstring = "  coalesce(telefonzeiten.flrtelefonzeiten.c_titel,'')||' '||coalesce(cast(telefonzeiten.flrtelefonzeiten.x_kommentarext as string),'')||' '||coalesce(cast(telefonzeiten.flrtelefonzeiten.x_kommentarint as string),'')";

						String[] teile = filterKriterien[i].value.toLowerCase().split(" ");

						for (int p = 0; p < teile.length; p++) {

							if (teile[p].startsWith("-")) {
								where.append(" NOT ");

								teile[p] = teile[p].substring(1);

							}

							where.append(" lower(" + suchstring + ") like '%" + teile[p].toLowerCase() + "%'");
							if (p < teile.length - 1) {
								where.append(" AND ");
							}
						}

					} else {

						if (filterKriterien[i].isBIgnoreCase()) {
							where.append(" lower(" + filterKriterien[i].kritName + ")");
						} else {
							where.append(" " + filterKriterien[i].kritName);
						}
						where.append(" " + filterKriterien[i].operator);
						if (filterKriterien[i].isBIgnoreCase()) {
							where.append(" " + filterKriterien[i].value.toLowerCase());
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
							orderBy.append("telefonzeiten." + kriterien[i].kritName);
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
				orderBy.append("telefonzeiten." + ZeiterfassungFac.FLR_TELEFONZEITEN_T_VON + " DESC ");
				sortAdded = true;
			}
			if (orderBy.indexOf("telefonzeiten.c_nr") < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" telefonzeiten.c_nr ");
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
		return "from FLRTelefontodo telefonzeiten LEFT OUTER JOIN telefonzeiten.flrpartner flrpartner LEFT OUTER JOIN telefonzeiten.flransprechpartner flransprechpartner LEFT OUTER JOIN telefonzeiten.flransprechpartner.flrpartneransprechpartner flrpartneransprechpartner LEFT OUTER JOIN telefonzeiten.flrauftrag flrauftrag LEFT OUTER JOIN telefonzeiten.flrangebot flrangebot LEFT OUTER JOIN telefonzeiten.flrprojekt flrprojekt";
	}

	public QueryResult sort(SortierKriterium[] sortierKriterien, Object selectedId) throws EJBExceptionLP {
		this.getQuery().setSortKrit(sortierKriterien);

		QueryResult result = null;
		int rowNumber = 0;

		if (selectedId != null) {
			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = null;

			try {
				session = factory.openSession();
				String queryString = "select telefonzeiten.c_nr from FLRTelefontodo telefonzeiten "
						+ this.buildWhereClause() + this.buildOrderByClause();
				Query query = session.createQuery(queryString);
				ScrollableResults scrollableResult = query.scroll();
				if (scrollableResult != null) {
					scrollableResult.beforeFirst();
					while (scrollableResult.next()) {
						String id = (String) scrollableResult.getString(0);
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

		TableInfo info = super.getTableInfo();
		if (info != null)
			return info;

		setupParameters();

		setTableColumnInformation(createColumnInformation(theClientDto.getMandant(), theClientDto.getLocUi()));

		TableColumnInformation c = getTableColumnInformation();
		info = new TableInfo(c.getClasses(), c.getHeaderNames(), c.getWidths(), c.getDbColumNames(),
				c.getHeaderToolTips());
		setTableInfo(info);
		return info;

	}

	private void setupParameters() {
		try {
			ParametermandantDto parameter = getParameterFac().getMandantparameter(theClientDto.getMandant(),
					ParameterFac.KATEGORIE_PROJEKT, ParameterFac.PARAMETER_KURZZEICHEN_STATT_NAME_IN_AUSWAHLLISTE);
			bKurzzeichenStattName = (java.lang.Boolean) parameter.getCWertAsObject();

		} catch (RemoteException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}
	}

	private TableColumnInformation createColumnInformation(String mandant, Locale locUi) {

		/*
		 * public TableInfo getTableInfo() {
		 * 
		 * if (super.getTableInfo() == null) { setTableInfo(new TableInfo( new Class[] {
		 * Integer.class, java.sql.Timestamp.class, java.sql.Timestamp.class,
		 * String.class, String.class, java.sql.Timestamp.class,
		 * java.sql.Timestamp.class, Color.class }, new String[] { "Id",
		 * getTextRespectUISpr("lp.von", theClientDto.getMandant(),
		 * theClientDto.getLocUi()), getTextRespectUISpr("lp.bis",
		 * theClientDto.getMandant(), theClientDto.getLocUi()),
		 * getTextRespectUISpr("lp.partner", theClientDto.getMandant(),
		 * theClientDto.getLocUi()), getTextRespectUISpr("lp.kommentar",
		 * theClientDto.getMandant(), theClientDto.getLocUi()),
		 * getTextRespectUISpr("lp.wiedervorlage", theClientDto.getMandant(),
		 * theClientDto.getLocUi()), getTextRespectUISpr("lp.erledigt",
		 * theClientDto.getMandant(), theClientDto.getLocUi()) }, new int[] { -1, //
		 * diese Spalte wird ausgeblendet QueryParameters.FLR_BREITE_XM,
		 * QueryParameters.FLR_BREITE_XM, QueryParameters.FLR_BREITE_XL,
		 * QueryParameters.FLR_BREITE_SHARE_WITH_REST, QueryParameters.FLR_BREITE_XM,
		 * QueryParameters.FLR_BREITE_XM, },
		 * 
		 * new String[] { "i_id", ZeiterfassungFac.FLR_TELEFONZEITEN_T_VON,
		 * ZeiterfassungFac.FLR_TELEFONZEITEN_T_BIS,
		 * ZeiterfassungFac.FLR_TELEFONZEITEN_FLRPARTNER + "." +
		 * PartnerFac.FLR_PARTNER_C_NAME1NACHNAMEFIRMAZEILE1,
		 * ZeiterfassungFac.FLR_TELEFONZEITEN_X_KOMMENTAREXT, "t_wiedervorlage",
		 * "t_wiedervorlage_erledigt" }));
		 * 
		 * }
		 * 
		 * return super.getTableInfo(); }
		 */

		TableColumnInformation columns = new TableColumnInformation();

		columns.add("c_nr", String.class, "c_nr", QueryParameters.FLR_BREITE_SHARE_WITH_REST, "c_nr");

		columns.add("lp.von", java.sql.Timestamp.class, getTextRespectUISpr("lp.von", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST, ZeiterfassungFac.FLR_TELEFONZEITEN_T_VON);
		columns.add("lp.bis", java.sql.Timestamp.class, getTextRespectUISpr("lp.bis", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST, ZeiterfassungFac.FLR_TELEFONZEITEN_T_BIS);

		columns.add("lp.partner", String.class, getTextRespectUISpr("lp.partner", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST, ZeiterfassungFac.FLR_TELEFONZEITEN_FLRPARTNER + "."
						+ PartnerFac.FLR_PARTNER_C_NAME1NACHNAMEFIRMAZEILE1);
		columns.add("part.ansprechpartner", String.class, getTextRespectUISpr("part.ansprechpartner", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST,
				"flransprechpartner.flrpartneransprechpartner.c_name1nachnamefirmazeile1");

		columns.add("proj.projekt", String.class, getTextRespectUISpr("proj.projekt", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST, "flrprojekt.c_nr");
		columns.add("angb.angebot", String.class, getTextRespectUISpr("angb.angebot", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST, "flrangebot.c_nr");
		columns.add("auft.auftrag", String.class, getTextRespectUISpr("auft.auftrag", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST, "flrauftrag.c_nr");

		columns.add("proj.projektbez", String.class, getTextRespectUISpr("proj.projekt", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST, Facade.NICHT_SORTIERBAR);

		columns.add("lp.titel", String.class, getTextRespectUISpr("lp.titel", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST, "c_titel");

		String sort = "flrpersonal_zugewiesener.flrpartner.c_name1nachnamefirmazeile1";
		if (bKurzzeichenStattName) {
			sort = "flrpersonal_zugewiesener.c_kurzzeichen";
		}

		columns.add("proj.personal.zugewiesener", String.class,
				getTextRespectUISpr("proj.personal.zugewiesener", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST, sort);

		columns.add("lp.wiedervorlage", java.sql.Timestamp.class,
				getTextRespectUISpr("lp.wiedervorlage", mandant, locUi), QueryParameters.FLR_BREITE_SHARE_WITH_REST,
				"t_wiedervorlage");

		columns.add("lp.erledigt", java.sql.Timestamp.class, getTextRespectUISpr("lp.erledigt", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST, "t_wiedervorlage_erledigt");

		return columns;
	}

}
