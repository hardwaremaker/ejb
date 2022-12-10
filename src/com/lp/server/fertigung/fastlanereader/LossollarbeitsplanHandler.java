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
package com.lp.server.fertigung.fastlanereader;

import java.awt.Color;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.swing.Icon;

import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.auftrag.service.AuftragzeitenDto;
import com.lp.server.fertigung.fastlanereader.generated.FLRLossollarbeitsplan;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.fertigung.service.LosgutschlechtDto;
import com.lp.server.partner.service.KundeFac;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.personal.service.AbschnittEinerReiseDto;
import com.lp.server.personal.service.PersonalFac;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.system.fastlanereader.service.TableColumnInformation;
import com.lp.server.system.service.LocaleFac;
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
import com.lp.util.Helper;

/**
 * <p>
 * Hier wird die FLR Funktionalitaet fuer den Losarbeitsplan implementiert.
 * </p>
 * <p>
 * Copright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * <p>
 * Erstellungsdatum 21.07.2005
 * </p>
 * <p>
 * </p>
 * 
 * @author Martin Bluehweis
 * @version 1.0
 */
public class LossollarbeitsplanHandler extends UseCaseHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean bStueckrueckmeldung;
	private boolean bMaschinenbezeichnungStattArtikelbezeichnung;

	private static final String FLR_LOSMAT = "flrlossollarbeitsplan.";
	private static final String FLR_LOSMAT_FROM_CLAUSE = " from FLRLossollarbeitsplan flrlossollarbeitsplan ";

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

			FilterKriterium[] filterKriterien = this.getQuery()
					.getFilterBlock().filterKrit;
			Integer losIId = null;
			for (int i = 0; i < filterKriterien.length; i++) {
				if (filterKriterien[i].kritName
						.equals(FertigungFac.FLR_LOSSOLLARBEITSPLAN_LOS_I_ID)) {
					losIId = new Integer(
							Integer.parseInt(filterKriterien[i].value
									.replaceAll("'", "")));
				}
			}

			boolean bZuvieleZeitbuchungen = getZeiterfassungFac()
					.sindZuvieleZeitdatenEinesBelegesVorhanden(
							LocaleFac.BELEGART_LOS, losIId, theClientDto);
			AuftragzeitenDto[] mannZeiten = null;
			if (bZuvieleZeitbuchungen == false) {
				mannZeiten = getZeiterfassungFac().getAllZeitenEinesBeleges(
						LocaleFac.BELEGART_LOS, losIId, null, null, null, null,
						ZeiterfassungFac.SORTIERUNG_ZEITDATEN_ARTIKEL, theClientDto);
			}

			AuftragzeitenDto[] maschinenZeiten = getZeiterfassungFac()
					.getAllMaschinenzeitenEinesBeleges(losIId, null, null,
							null, theClientDto);
			
			String[] kurzeWochentage = new DateFormatSymbols(theClientDto.getLocUi()).getShortWeekdays();
			

			while (resultListIterator.hasNext()) {
				FLRLossollarbeitsplan loszeit = (FLRLossollarbeitsplan) resultListIterator
						.next();

				Object[] rowToAddCandidate = new Object[colCount];

				rowToAddCandidate[getTableColumnInformation().getViewIndex(
						"i_id")] = loszeit.getI_id();

				if (Helper.short2boolean(loszeit.getB_nachtraeglich()) == true) {
					// Nachtraeglich hinzugefuegte Arbeitsgaenge
					rowToAddCandidate[getTableColumnInformation().getViewIndex(
							"lp.art")] = "N";
				} else {
					// Arbeitsgaenge, die aus der Stueckliste uebernommen wurden
					rowToAddCandidate[getTableColumnInformation().getViewIndex(
							"lp.art")] = "S";
				}

				rowToAddCandidate[getTableColumnInformation().getViewIndex(
						"fert.ag")] = loszeit.getI_arbeitsgangsnummer();
				rowToAddCandidate[getTableColumnInformation().getViewIndex(
						"fert.uag")] = loszeit.getI_unterarbeitsgang();
				
				//PJ21850
				java.util.Date tAgBeginn=null;
				
				if (loszeit.getI_maschinenversatztage() == null) {
					tAgBeginn = loszeit.getFlrlos().getT_produktionsbeginn();
				} else {
					tAgBeginn= Helper.addiereTageZuDatum(
									loszeit.getFlrlos().getT_produktionsbeginn(), loszeit.getI_maschinenversatztage());
				}
				
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(tAgBeginn.getTime());
				
				rowToAddCandidate[getTableColumnInformation().getViewIndex(
						"fert.agbeginn")] =Helper.formatDatum(tAgBeginn, theClientDto.getLocUi())+" ("+kurzeWochentage[cal.get(Calendar.DAY_OF_WEEK)]+")" ;
				

				
				
				
				
				
				if (loszeit.getFlrmaschine() != null) {
					rowToAddCandidate[getTableColumnInformation().getViewIndex(
							"lp.maschine")] = loszeit.getFlrmaschine()
							.getC_identifikationsnr();
				}

				if (bMaschinenbezeichnungStattArtikelbezeichnung) {
					if (loszeit.getFlrmaschine() != null) {

						rowToAddCandidate[getTableColumnInformation()
								.getViewIndex("lp.bezeichnung")] = loszeit
								.getFlrmaschine().getC_bez();

					}

					if (bStueckrueckmeldung) {

						rowToAddCandidate[getTableColumnInformation()
								.getViewIndex("lp.kommentar")] = loszeit
								.getC_kommentar();
					}
					rowToAddCandidate[getTableColumnInformation().getViewIndex(
							"lp.ident")] = loszeit.getFlrartikel().getC_nr();
				} else {
					rowToAddCandidate[getTableColumnInformation().getViewIndex(
							"lp.ident")] = loszeit.getFlrartikel().getC_nr();
					rowToAddCandidate[getTableColumnInformation().getViewIndex(
							"lp.bezeichnung")] = getArtikelFac()
							.artikelFindByPrimaryKeySmall(
									loszeit.getFlrartikel().getI_id(),
									theClientDto).formatBezeichnung();
				}

				if (Helper.short2boolean(loszeit.getB_nurmaschinenzeit()) == false) {
					rowToAddCandidate[getTableColumnInformation().getViewIndex(
							"lp.dauer.person")] = loszeit.getN_gesamtzeit();
				}

				if (loszeit.getFlrmaschine() != null) {
					rowToAddCandidate[getTableColumnInformation().getViewIndex(
							"lp.dauer.maschine")] = loszeit.getN_gesamtzeit();
				}

				BigDecimal bdZeitPerson = new BigDecimal(0);

				BigDecimal bdZeitMaschine = new BigDecimal(0);

				if (bZuvieleZeitbuchungen == false) {

					for (int i = 0; i < mannZeiten.length; i++) {
						if (loszeit.getI_id().equals(
								mannZeiten[i].getBelegpositionIId())) {
							bdZeitPerson = bdZeitPerson.add(new BigDecimal(
									mannZeiten[i].getDdDauer().doubleValue()));
						}
					}

					for (int i = 0; i < maschinenZeiten.length; i++) {
						if (loszeit.getI_id().equals(
								maschinenZeiten[i].getBelegpositionIId())) {
							bdZeitMaschine = bdZeitMaschine.add(new BigDecimal(
									maschinenZeiten[i].getDdDauer()
											.doubleValue()));
						}
					}

				} else {
					bdZeitPerson = new BigDecimal(-1);
					bdZeitMaschine = new BigDecimal(-1);
				}

				rowToAddCandidate[getTableColumnInformation().getViewIndex(
						"lp.ist.person")] = bdZeitPerson;

				rowToAddCandidate[getTableColumnInformation().getViewIndex(
						"lp.ist.maschine")] = bdZeitMaschine;
				// Optional: Stueckrueckmeldung
				if (bStueckrueckmeldung) {

					BigDecimal[] bdGutSchlechtInarbeit = getFertigungFac()
							.getGutSchlechtInarbeit(loszeit.getI_id(),
									theClientDto);

					rowToAddCandidate[getTableColumnInformation().getViewIndex(
							"lp.gut")] = bdGutSchlechtInarbeit[0];
					rowToAddCandidate[getTableColumnInformation().getViewIndex(
							"lp.schlecht")] = bdGutSchlechtInarbeit[1];
					rowToAddCandidate[getTableColumnInformation().getViewIndex(
							"lp.inarbeit")] = bdGutSchlechtInarbeit[2];
					rowToAddCandidate[getTableColumnInformation().getViewIndex(
							"lp.offen")] = bdGutSchlechtInarbeit[3];

				}

				rowToAddCandidate[getTableColumnInformation().getViewIndex(
						"fert.fortschritt")] = loszeit.getF_fortschritt();
				rowToAddCandidate[getTableColumnInformation().getViewIndex(
						"lp.fertig")] = Helper.short2Boolean(loszeit
						.getB_fertig());

				rows[row] = rowToAddCandidate;
				row++;
				col = 0;
			}
			result = new QueryResult(rows, this.getRowCount(), startIndex,
					endIndex, 0);
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
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
			String queryString = "select count(*) " + this.getFromClause()
					+ this.buildWhereClause();
			Query query = session.createQuery(queryString);
			List<?> rowCountResult = query.list();
			if (rowCountResult != null && rowCountResult.size() > 0) {
				rowCount = ((Long) rowCountResult.get(0)).longValue();
			}
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, e);
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
					where.append(" " + FLR_LOSMAT + filterKriterien[i].kritName);
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
							orderBy.append(FLR_LOSMAT + kriterien[i].kritName);
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
				orderBy.append(FLR_LOSMAT)
						.append(FertigungFac.FLR_LOSSOLLARBEITSPLAN_I_ARBEITSGANGNUMMER
								+ ", ")
						.append(FLR_LOSMAT)
						.append(FertigungFac.FLR_LOSSOLLARBEITSPLAN_I_UNTERARBEITSGANG)
						.append(" ASC ");
				sortAdded = true;
			}
			if (orderBy.indexOf(FLR_LOSMAT
					+ FertigungFac.FLR_LOSSOLLARBEITSPLAN_I_ARBEITSGANGNUMMER) < 0) {
				// unique sort required because otherwise rowNumber of
				// selectedId
				// within sort() method may be different from the position of
				// selectedId
				// as returned in the page of getPageAt().
				if (sortAdded) {
					orderBy.append(", ");
				}
				orderBy.append(" ")
						.append(FLR_LOSMAT)
						.append(FertigungFac.FLR_LOSSOLLARBEITSPLAN_I_ARBEITSGANGNUMMER)
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
		return FLR_LOSMAT_FROM_CLAUSE;
	}

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
				String queryString = "select " + FLR_LOSMAT
						+ FertigungFac.FLR_LOSSOLLARBEITSPLAN_I_ID
						+ FLR_LOSMAT_FROM_CLAUSE + this.buildWhereClause()
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

	private TableColumnInformation createColumnInformation(String mandant,
			Locale locUi) {
		TableColumnInformation columns = new TableColumnInformation();

		columns.add("i_id", Integer.class, "i_id", -1, "i_id");

		columns.add("lp.art", String.class,
				getTextRespectUISpr("lp.art", mandant, locUi), 3,
				Facade.NICHT_SORTIERBAR);
		columns.add("fert.ag", Integer.class,
				getTextRespectUISpr("fert.ag", mandant, locUi), 3,
				FertigungFac.FLR_LOSSOLLARBEITSPLAN_I_ARBEITSGANGNUMMER);
		columns.add("fert.uag", Integer.class,
				getTextRespectUISpr("fert.uag", mandant, locUi), 3,
				FertigungFac.FLR_LOSSOLLARBEITSPLAN_I_UNTERARBEITSGANG);
		columns.add("fert.agbeginn", String.class,
				getTextRespectUISpr("fert.agbeginn", mandant, locUi), 5,
				FertigungFac.FLR_LOSSOLLARBEITSPLAN_I_MASCHINENVERSATZTAGE);
		columns.add("lp.maschine", String.class,
				getTextRespectUISpr("lp.maschine", mandant, locUi), 3,
				FertigungFac.FLR_LOSSOLLARBEITSPLAN_FLRMASCHINE + "."
						+ ZeiterfassungFac.FLR_MASCHINE_C_IDENTIFIKATIONSNR);

		if (bMaschinenbezeichnungStattArtikelbezeichnung) {

			columns.add("lp.bezeichnung", String.class,
					getTextRespectUISpr("lp.bezeichnung", mandant, locUi),
					QueryParameters.FLR_BREITE_XM,
					FertigungFac.FLR_LOSSOLLARBEITSPLAN_FLRMASCHINE + ".c_bez");
			if (bStueckrueckmeldung) {
				columns.add("lp.kommentar", String.class,
						getTextRespectUISpr("lp.kommentar", mandant, locUi),
						QueryParameters.FLR_BREITE_SHARE_WITH_REST,
						"c_kommentar");
			}

			columns.add("lp.ident", String.class,
					getTextRespectUISpr("lp.ident", mandant, locUi),
					QueryParameters.FLR_BREITE_SHARE_WITH_REST,
					FertigungFac.FLR_LOSSOLLARBEITSPLAN_FLRARTIKEL + ".c_nr");

		} else {
			columns.add("lp.ident", String.class,
					getTextRespectUISpr("lp.ident", mandant, locUi),
					QueryParameters.FLR_BREITE_M,
					FertigungFac.FLR_LOSSOLLARBEITSPLAN_FLRARTIKEL + ".c_nr");
			columns.add("lp.bezeichnung", String.class,
					getTextRespectUISpr("lp.bezeichnung", mandant, locUi),
					QueryParameters.FLR_BREITE_XM, Facade.NICHT_SORTIERBAR);
		}

		columns.add("lp.dauer.person", BigDecimal.class,
				getTextRespectUISpr("lp.dauer.person", mandant, locUi),
				QueryParameters.FLR_BREITE_PREIS,
				FertigungFac.FLR_LOSSOLLARBEITSPLAN_N_GESAMTZEIT);
		columns.add("lp.ist.person", BigDecimal.class,
				getTextRespectUISpr("lp.ist", mandant, locUi),
				QueryParameters.FLR_BREITE_PREIS, Facade.NICHT_SORTIERBAR);

		columns.add("lp.dauer.maschine", BigDecimal.class,
				getTextRespectUISpr("lp.dauer.maschine", mandant, locUi),
				QueryParameters.FLR_BREITE_PREIS,
				FertigungFac.FLR_LOSSOLLARBEITSPLAN_N_GESAMTZEIT);
		columns.add("lp.ist.maschine", BigDecimal.class,
				getTextRespectUISpr("lp.ist", mandant, locUi),
				QueryParameters.FLR_BREITE_PREIS, Facade.NICHT_SORTIERBAR);

		if (bStueckrueckmeldung) {

			columns.add("lp.gut", BigDecimal.class,
					getTextRespectUISpr("lp.gut", mandant, locUi), 10,
					Facade.NICHT_SORTIERBAR);
			columns.add("lp.schlecht", BigDecimal.class,
					getTextRespectUISpr("lp.schlecht", mandant, locUi), 10,
					Facade.NICHT_SORTIERBAR);
			columns.add("lp.inarbeit", BigDecimal.class,
					getTextRespectUISpr("lp.inarbeit", mandant, locUi), 10,
					Facade.NICHT_SORTIERBAR);
			columns.add("lp.offen", BigDecimal.class,
					getTextRespectUISpr("lp.offen", mandant, locUi), 10,
					Facade.NICHT_SORTIERBAR);
		}
		columns.add("fert.fortschritt", Double.class,
				getTextRespectUISpr("fert.fortschritt", mandant, locUi),
				QueryParameters.FLR_BREITE_S,
				FertigungFac.FLR_LOSSOLLARBEITSPLAN_F_FORTSCHRITT);
		columns.add("lp.fertig", Boolean.class,
				getTextRespectUISpr("lp.fertig", mandant, locUi),
				QueryParameters.FLR_BREITE_S,
				FertigungFac.FLR_LOSSOLLARBEITSPLAN_B_FERTIG);

		return columns;
	}

	private void setupParameters() {
		try {
			bStueckrueckmeldung = getMandantFac()
					.hatZusatzfunktionberechtigung(
							MandantFac.ZUSATZFUNKTION_STUECKRUECKMELDUNG,
							theClientDto);

			ParametermandantDto parameter = getParameterFac()
					.getMandantparameter(
							theClientDto.getMandant(),
							ParameterFac.KATEGORIE_FERTIGUNG,
							ParameterFac.PARAMETER_MASCHINENBEZEICHNUNG_ANZEIGEN);
			bMaschinenbezeichnungStattArtikelbezeichnung = (Boolean) parameter
					.getCWertAsObject();

		} catch (RemoteException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER, ex);
		}
	}

	public TableInfo getTableInfo() {
		TableInfo info = super.getTableInfo();
		if (info != null)
			return info;

		setupParameters();
		setTableColumnInformation(createColumnInformation(
				theClientDto.getMandant(), theClientDto.getLocUi()));

		TableColumnInformation c = getTableColumnInformation();
		info = new TableInfo(c.getClasses(), c.getHeaderNames(), c.getWidths(),
				c.getDbColumNames(), c.getHeaderToolTips());
		setTableInfo(info);
		return info;
	}
}
