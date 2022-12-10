
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
package com.lp.server.rechnung.fastlanereader;

import java.awt.Color;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.swing.Icon;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.auftrag.service.AuftragzeitenDto;
import com.lp.server.fertigung.service.LosgutschlechtDto;
import com.lp.server.fertigung.service.LossollarbeitsplanDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.personal.fastlanereader.generated.FLRProjektzeiten;
import com.lp.server.personal.fastlanereader.generated.FLRZeitdaten;
import com.lp.server.personal.fastlanereader.generated.FLRZeitdatenProjekt;
import com.lp.server.personal.service.PersonalFac;
import com.lp.server.personal.service.TelefonzeitenDto;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.projekt.service.BereichDto;
import com.lp.server.projekt.service.ProjektDto;
import com.lp.server.projekt.service.ProjektFac;
import com.lp.server.stueckliste.service.StuecklisteDto;
import com.lp.server.system.fastlanereader.service.TableColumnInformation;
import com.lp.server.system.service.LocaleFac;
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
 * Hier wird die FLR Funktionalit&auml;t f&uuml;r die Zeitdaten implementiert.
 * Pro UseCase gibt es einen Handler.
 * </p>
 * <p>
 * Copright Logistik Pur GmbH (c) 2004
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
public class ZeitinfoHandler extends UseCaseHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
			String queryString = "SELECT zeitdaten from FLRProjektzeiten zeitdaten  " + this.buildWhereClause()
					+ this.buildOrderByClause();
			Query query = session.createQuery(queryString);
			query.setFirstResult(startIndex);
			query.setMaxResults(pageSize);
			List<?> resultList = query.list();
			Iterator<?> resultListIterator = resultList.iterator();
			Object[][] rows = new Object[resultList.size()][colCount];
			int row = 0;
			int col = 0;
			while (resultListIterator.hasNext()) {

				// Object[] o = (Object[]) resultListIterator.next();
				// FLRZeitdatenProjekt zeitdaten = (FLRZeitdatenProjekt) o[0];

				FLRProjektzeiten zeitdaten = (FLRProjektzeiten) resultListIterator.next();

				Object[] rowToAddCandidate = new Object[colCount];

				String bereich_c_bez = zeitdaten.getFlrprojekt().getFlrbereich().getC_bez();

				if (bereich_c_bez != null && bereich_c_bez.length() > 2) {
					bereich_c_bez = bereich_c_bez.substring(0, 2);
				}

				rowToAddCandidate[getTableColumnInformation().getViewIndex("i_id")] = zeitdaten.getI_id();

				if (zeitdaten.getZeitdaten_i_id() != null) {
					rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.art")] = "Z";
				} else {
					rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.art")] = "T";
				}
				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.person")] = zeitdaten.getFlrpersonal()
						.getC_kurzzeichen();

				if (zeitdaten.getFlrprojekt().getFlransprechpartner() != null) {
					rowToAddCandidate[getTableColumnInformation().getViewIndex("part.ansprechpartner")] = HelperServer
							.formatAdresseEinesFLRPartner(
									zeitdaten.getFlrprojekt().getFlransprechpartner().getFlrpartneransprechpartner());

				}

				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.firma_nachname")] = HelperServer
						.formatAdresseEinesFLRPartner(zeitdaten.getFlrprojekt().getFlrpartner());

				String email = zeitdaten.getFlrprojekt().getFlrpartner().getC_email();

				KundeDto kdDto;
				try {
					kdDto = getKundeFac().kundeFindByiIdPartnercNrMandantOhneExc(
							zeitdaten.getFlrprojekt().getFlrpartner().getI_id(),
							zeitdaten.getFlrprojekt().getMandant_c_nr(), theClientDto);
					if (kdDto != null && kdDto.getCEmailRechnungsempfang() != null) {
						email = kdDto.getCEmailRechnungsempfang();
					}

				} catch (RemoteException e1) {
					throwEJBExceptionLPRespectOld(e1);
				}

				rowToAddCandidate[getTableColumnInformation().getViewIndex("proj.bereich")] = bereich_c_bez;
				rowToAddCandidate[getTableColumnInformation().getViewIndex("proj.nr")] = zeitdaten.getFlrprojekt()
						.getC_nr();
				rowToAddCandidate[getTableColumnInformation().getViewIndex("proj.titel")] = zeitdaten.getFlrprojekt()
						.getC_titel();

				if (email == null) {
					rowToAddCandidate[getTableColumnInformation()
							.getViewIndex("lp.email")] = LocaleFac.STATUS_DATEN_UNGUELTIG;
				} else {
					rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.email")] = LocaleFac.STATUS_EMAIL;
				}

				try {
					if (zeitdaten.getZeitdaten_i_id() != null) {

						AuftragzeitenDto[] dtos = getZeiterfassungFac().getAllZeitenEinesBeleges(
								LocaleFac.BELEGART_PROJEKT, zeitdaten.getProjekt_i_id(), null,
								zeitdaten.getFlrpersonal().getI_id(), null, null,
								ZeiterfassungFac.SORTIERUNG_ZEITDATEN_PERSONAL, false, zeitdaten.getZeitdaten_i_id(),
								null, ZeiterfassungFac.BELEGZEITEN_NUR_PERSONALZEITEN, theClientDto);

						double zeiten = 0;
						for (int i = 0; i < dtos.length; i++) {
							if (dtos[i] != null && dtos[i].getDdDauer() != null) {
								zeiten = zeiten + dtos[i].getDdDauer().doubleValue();
							}
						}
						rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.dauer")] = new Double(zeiten);
					} else if (zeitdaten.getTelefonzeiten_i_id() != null) {

						double lDauer = zeitdaten.getFlrtelefonzeit().getT_bis().getTime()
								- zeitdaten.getFlrtelefonzeit().getT_von().getTime();

						Double dDauer = lDauer/3600000;

						rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.dauer")] = dDauer;

					}

				} catch (EJBExceptionLP e) {

					if (e.getCode() == EJBExceptionLP.FEHLER_IN_ZEITDATEN) {
						rowToAddCandidate[getTableColumnInformation().getViewIndex("Color")] = Color.RED;
					} else {
						throw e;
					}
				}

				rowToAddCandidate[getTableColumnInformation().getViewIndex("rech.zeitinfo.verr")] = zeitdaten
						.getF_verrechenbar();

				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.beginn")] = zeitdaten.getT_zeit();

				rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.bemerkung")] = zeitdaten
						.getC_bemerkung();

				if (zeitdaten.getFlrzeitdaten() != null) {
					rowToAddCandidate[getTableColumnInformation()
							.getViewIndex("rech.zeitnachweis.dauer.uebersteuert")] = zeitdaten.getFlrzeitdaten()
									.getF_dauer_uebersteuert();
					rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.kommentar")] = zeitdaten
							.getFlrzeitdaten().getX_kommentar();
				} else if (zeitdaten.getFlrtelefonzeit() != null) {
					rowToAddCandidate[getTableColumnInformation()
							.getViewIndex("rech.zeitnachweis.dauer.uebersteuert")] = zeitdaten.getFlrtelefonzeit()
									.getF_dauer_uebersteuert();
					rowToAddCandidate[getTableColumnInformation().getViewIndex("lp.kommentar")] = zeitdaten
							.getFlrtelefonzeit().getX_kommentarext();
				}

				rows[row] = rowToAddCandidate;
				row++;

				col = 0;
			}
			result = new QueryResult(rows, this.getRowCount(), startIndex, endIndex, 0);
		} catch (HibernateException e) {
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

	public static String istBuchungManipuliert(Date t1, Date t2) throws EJBExceptionLP {
		String sManipuliert = "";

		if ((t1.getTime() - t2.getTime()) > 180000 || (t1.getTime() - t2.getTime()) < -180000) {
			sManipuliert = "H";
		}

		return sManipuliert;
	}

	protected long getRowCountFromDataBase() {
		long rowCount = 0;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			session = factory.openSession();
			session = setFilter(session);
			String queryString = "select count(*) from FLRProjektzeiten zeitdaten "
					+ this.buildWhereClause();
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
					if (filterKriterien[i].isBIgnoreCase()) {
						where.append(" upper(" + filterKriterien[i].kritName + ")");
					} else {
						where.append(" " + filterKriterien[i].kritName);
					}
					where.append(" " + filterKriterien[i].operator);

					if (filterKriterien[i].isBIgnoreCase()) {
						where.append(" " + filterKriterien[i].value.toUpperCase());
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
					// nodbfeld: 2: Hier alle Spaltennamen, die mit X enden beim
					// sortieren ignorieren.
					if (!kriterien[i].kritName.endsWith(Facade.NICHT_SORTIERBAR)) {
						if (kriterien[i].isKrit) {
							if (sortAdded) {
								orderBy.append(", ");
							}
							sortAdded = true;
							orderBy.append("zeitdaten." + kriterien[i].kritName);
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
				orderBy.append("zeitdaten." + ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT + " ASC ");
				sortAdded = true;
			}
			if (orderBy.indexOf("zeitdaten." + ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT) < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" zeitdaten." + ZeiterfassungFac.FLR_ZEITDATEN_T_ZEIT + " ");
				sortAdded = true;
			}
			if (sortAdded) {
				orderBy.insert(0, " ORDER BY ");
			}
		}
		return orderBy.toString();
	}

	public QueryResult sort(SortierKriterium[] sortierKriterien, Object selectedId) throws EJBExceptionLP {
		this.getQuery().setSortKrit(sortierKriterien);

		QueryResult result = null;
		int rowNumber = 0;

		if (selectedId != null && selectedId instanceof Integer && ((Integer) selectedId).intValue() >= 0) {
			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = null;

			try {
				session = factory.openSession();
				session = setFilter(session);
				String queryString = "select zeitdaten.i_id from FLRProjektzeiten zeitdaten "
						+ this.buildWhereClause() + this.buildOrderByClause();
				Query query = session.createQuery(queryString);
				ScrollableResults scrollableResult = query.scroll();
				if (scrollableResult != null) {
					scrollableResult.beforeFirst();
					while (scrollableResult.next()) {
						Integer id = scrollableResult.getInteger(0);
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

		columns.add("i_id", Integer.class, "i_id", -1, "i_id");

		columns.add("lp.art", String.class, getTextRespectUISpr("lp.art", mandant, locUi), 2, "zeitdaten_i_id");

		columns.add("lp.person", String.class, getTextRespectUISpr("lp.person", mandant, locUi), 7,
				"flrpersonal.c_kurzzeichen");

		columns.add("lp.firma_nachname", String.class, getTextRespectUISpr("lp.firma_nachname", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST,
				"flrprojekt.flrpartner." + PartnerFac.FLR_PARTNER_C_NAME1NACHNAMEFIRMAZEILE1);

		columns.add("part.ansprechpartner", String.class, getTextRespectUISpr("part.ansprechpartner", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST,
				"flrprojekt.flrpartner." + PartnerFac.FLR_PARTNER_C_NAME1NACHNAMEFIRMAZEILE1);

		columns.add("proj.bereich", String.class, getTextRespectUISpr("proj.bereich", mandant, locUi), 3,
				"flrprojekt.flrbereich.c_bez");

		columns.add("proj.nr", String.class, getTextRespectUISpr("proj.nr", mandant, locUi), 10, "flrprojekt.c_nr");

		columns.add("proj.titel", String.class, getTextRespectUISpr("proj.titel", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST, "flrprojekt.c_titel");

		columns.add("lp.email", Icon.class, getTextRespectUISpr("lp.email", mandant, locUi),
				QueryParameters.FLR_BREITE_S, Facade.NICHT_SORTIERBAR);

		columns.add("lp.beginn", Timestamp.class, getTextRespectUISpr("lp.beginn", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST, "t_zeit");

		columns.add("lp.dauer", Double.class, getTextRespectUISpr("lp.dauer", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST, Facade.NICHT_SORTIERBAR);
		columns.add("rech.zeitnachweis.dauer.uebersteuert", Double.class,
				getTextRespectUISpr("rech.zeitnachweis.dauer.uebersteuert", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST, Facade.NICHT_SORTIERBAR);
		columns.add("rech.zeitinfo.verr", Double.class, getTextRespectUISpr("rech.zeitinfo.verr", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST, "f_verrechenbar");

		columns.add("lp.bemerkung", String.class, getTextRespectUISpr("lp.bemerkung", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST, "c_bemerkungzubelegart");

		columns.add("lp.kommentar", String.class, getTextRespectUISpr("lp.kommentar", mandant, locUi),
				QueryParameters.FLR_BREITE_SHARE_WITH_REST, "x_kommentar");

		columns.add("Color", Color.class, "", 1, "");

		return columns;
	}

}
