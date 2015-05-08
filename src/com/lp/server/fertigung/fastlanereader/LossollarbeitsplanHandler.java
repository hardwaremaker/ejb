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

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.auftrag.service.AuftragzeitenDto;
import com.lp.server.fertigung.fastlanereader.generated.FLRLossollarbeitsplan;
import com.lp.server.fertigung.service.FertigungFac;
import com.lp.server.fertigung.service.LosgutschlechtDto;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.system.service.LocaleFac;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
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
						false, false, theClientDto);
			}

			AuftragzeitenDto[] maschinenZeiten = getZeiterfassungFac()
					.getAllMaschinenzeitenEinesBeleges(losIId, null, null,
							null, theClientDto);

			while (resultListIterator.hasNext()) {
				FLRLossollarbeitsplan loszeit = (FLRLossollarbeitsplan) resultListIterator
						.next();
				rows[row][col++] = loszeit.getI_id();
				if (Helper.short2boolean(loszeit.getB_nachtraeglich()) == true) {
					// Nachtraeglich hinzugefuegte Arbeitsgaenge
					rows[row][col++] = "N";
				} else {
					// Arbeitsgaenge, die aus der Stueckliste uebernommen wurden
					rows[row][col++] = "S";
				}
				rows[row][col++] = loszeit.getI_arbeitsgangsnummer();
				rows[row][col++] = loszeit.getI_unterarbeitsgang();
				rows[row][col++] = loszeit.getI_maschinenversatztage();
				// Maschinen-identifikationsnummer
				if (loszeit.getFlrmaschine() != null) {
					rows[row][col++] = loszeit.getFlrmaschine()
							.getC_identifikationsnr();
				} else {
					rows[row][col++] = "";
				}

				if (bMaschinenbezeichnungStattArtikelbezeichnung) {
					if (loszeit.getFlrmaschine() != null) {
						rows[row][col++] = loszeit.getFlrmaschine().getC_bez();
					} else {
						rows[row][col++] = "";
					}

					if (bStueckrueckmeldung) {

						rows[row][col++] = loszeit.getC_kommentar();
					}
					rows[row][col++] = loszeit.getFlrartikel().getC_nr();
				} else {
					rows[row][col++] = loszeit.getFlrartikel().getC_nr();
					rows[row][col++] = getArtikelFac()
							.artikelFindByPrimaryKeySmall(
									loszeit.getFlrartikel().getI_id(),
									theClientDto).formatBezeichnung();
				}

				rows[row][col++] = loszeit.getN_gesamtzeit();
				BigDecimal bdZeit = new BigDecimal(0);

				if (bZuvieleZeitbuchungen == false) {

					for (int i = 0; i < mannZeiten.length; i++) {
						if (loszeit.getI_id().equals(
								mannZeiten[i].getBelegpositionIId())) {
							bdZeit = bdZeit.add(new BigDecimal(mannZeiten[i]
									.getDdDauer().doubleValue()));
						}
					}

					for (int i = 0; i < maschinenZeiten.length; i++) {
						if (loszeit.getI_id().equals(
								maschinenZeiten[i].getBelegpositionIId())) {
							bdZeit = bdZeit.add(new BigDecimal(
									maschinenZeiten[i].getDdDauer()
											.doubleValue()));
						}
					}

				} else {
					bdZeit = new BigDecimal(-1);
				}

				rows[row][col++] = bdZeit;
				// Optional: Stueckrueckmeldung
				if (bStueckrueckmeldung) {
					BigDecimal bdLosgroesse = loszeit.getFlrlos()
							.getN_losgroesse();

					BigDecimal[] bdGutSchlechtInarbeit = getFertigungFac()
							.getGutSchlechtInarbeit(loszeit.getI_id(),
									theClientDto);

					rows[row][col++] = bdGutSchlechtInarbeit[0];
					rows[row][col++] = bdGutSchlechtInarbeit[1];
					rows[row][col++] = bdGutSchlechtInarbeit[2];
					rows[row][col++] = bdGutSchlechtInarbeit[3];

				}
				rows[row][col++] = Helper.short2Boolean(loszeit.getB_fertig());

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

	public TableInfo getTableInfo() {
		if (super.getTableInfo() == null) {
			String mandantCNr = theClientDto.getMandant();
			Locale locUI = theClientDto.getLocUi();
			// Wenn die Stueckrueckmeldung aktiviert ist, gibts zusaetzliche
			// Spalten
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
				throwEJBExceptionLPRespectOld(ex);
			}
			if (bStueckrueckmeldung) {

				if (bMaschinenbezeichnungStattArtikelbezeichnung) {

					setTableInfo(new TableInfo(
							new Class[] { Integer.class, String.class,
									Integer.class, Integer.class,
									Integer.class, String.class, String.class,
									String.class, String.class,
									BigDecimal.class, BigDecimal.class,
									BigDecimal.class, BigDecimal.class,
									BigDecimal.class, BigDecimal.class,
									Boolean.class },
							new String[] {
									"i_id",
									getTextRespectUISpr("lp.art", mandantCNr,
											locUI),
									getTextRespectUISpr("fert.ag", mandantCNr,
											locUI),
									getTextRespectUISpr("fert.uag", mandantCNr,
											locUI),
									getTextRespectUISpr("fert.agbeginn",
											mandantCNr, locUI),
									getTextRespectUISpr("lp.maschine",
											mandantCNr, locUI),
									getTextRespectUISpr("lp.bezeichnung",
											mandantCNr, locUI),
									getTextRespectUISpr("lp.kommentar",
											mandantCNr, locUI),
									getTextRespectUISpr("lp.ident", mandantCNr,
											locUI),

									getTextRespectUISpr("lp.dauer", mandantCNr,
											locUI),
									getTextRespectUISpr("lp.ist", mandantCNr,
											locUI),
									getTextRespectUISpr("lp.gut", mandantCNr,
											locUI),
									getTextRespectUISpr("lp.schlecht",
											mandantCNr, locUI),
									getTextRespectUISpr("lp.inarbeit",
											mandantCNr, locUI),
									getTextRespectUISpr("lp.offen", mandantCNr,
											locUI),
									getTextRespectUISpr("lp.fertig",
											mandantCNr, locUI) },
							new int[] {
									QueryParameters.FLR_BREITE_SHARE_WITH_REST,
									3, 3, 3, 3, 3,
									QueryParameters.FLR_BREITE_XM,
									QueryParameters.FLR_BREITE_SHARE_WITH_REST,
									QueryParameters.FLR_BREITE_SHARE_WITH_REST,
									QueryParameters.FLR_BREITE_PREIS,
									QueryParameters.FLR_BREITE_PREIS, 10, 10,
									10, 10, QueryParameters.FLR_BREITE_S },
							new String[] {
									FertigungFac.FLR_LOSSOLLARBEITSPLAN_I_ID,
									Facade.NICHT_SORTIERBAR,
									FertigungFac.FLR_LOSSOLLARBEITSPLAN_I_ARBEITSGANGNUMMER,
									FertigungFac.FLR_LOSSOLLARBEITSPLAN_I_UNTERARBEITSGANG,
									FertigungFac.FLR_LOSSOLLARBEITSPLAN_I_MASCHINENVERSATZTAGE,
									FertigungFac.FLR_LOSSOLLARBEITSPLAN_FLRMASCHINE
											+ "."
											+ ZeiterfassungFac.FLR_MASCHINE_C_IDENTIFIKATIONSNR,
									FertigungFac.FLR_LOSSOLLARBEITSPLAN_FLRMASCHINE
											+ ".c_bez",
									"c_kommentar",
									FertigungFac.FLR_LOSSOLLARBEITSPLAN_FLRARTIKEL
											+ ".c_nr",
									FertigungFac.FLR_LOSSOLLARBEITSPLAN_N_GESAMTZEIT,
									Facade.NICHT_SORTIERBAR,
									Facade.NICHT_SORTIERBAR,
									Facade.NICHT_SORTIERBAR,
									Facade.NICHT_SORTIERBAR,
									Facade.NICHT_SORTIERBAR,
									FertigungFac.FLR_LOSSOLLARBEITSPLAN_B_FERTIG }));
				} else {

					setTableInfo(new TableInfo(
							new Class[] { Integer.class, String.class,
									Integer.class, Integer.class,
									Integer.class, String.class, String.class,
									String.class, BigDecimal.class,
									BigDecimal.class, BigDecimal.class,
									BigDecimal.class, BigDecimal.class,
									BigDecimal.class, Boolean.class },
							new String[] {
									"i_id",
									getTextRespectUISpr("lp.art", mandantCNr,
											locUI),
									getTextRespectUISpr("fert.ag", mandantCNr,
											locUI),
									getTextRespectUISpr("fert.uag", mandantCNr,
											locUI),
									getTextRespectUISpr("fert.agbeginn",
											mandantCNr, locUI),
									getTextRespectUISpr("lp.maschine",
											mandantCNr, locUI),
									getTextRespectUISpr("lp.ident", mandantCNr,
											locUI),
									getTextRespectUISpr("lp.bezeichnung",
											mandantCNr, locUI),
									getTextRespectUISpr("lp.dauer", mandantCNr,
											locUI),
									getTextRespectUISpr("lp.ist", mandantCNr,
											locUI),
									getTextRespectUISpr("lp.gut", mandantCNr,
											locUI),
									getTextRespectUISpr("lp.schlecht",
											mandantCNr, locUI),
									getTextRespectUISpr("lp.inarbeit",
											mandantCNr, locUI),
									getTextRespectUISpr("lp.offen", mandantCNr,
											locUI),
									getTextRespectUISpr("lp.fertig",
											mandantCNr, locUI) },
							new int[] {
									QueryParameters.FLR_BREITE_SHARE_WITH_REST,
									3, 3, 3, 3, 3,
									QueryParameters.FLR_BREITE_M,
									QueryParameters.FLR_BREITE_SHARE_WITH_REST,
									QueryParameters.FLR_BREITE_PREIS,
									QueryParameters.FLR_BREITE_PREIS, 10, 10,
									10, 10, QueryParameters.FLR_BREITE_S },
							new String[] {
									FertigungFac.FLR_LOSSOLLARBEITSPLAN_I_ID,
									Facade.NICHT_SORTIERBAR,
									FertigungFac.FLR_LOSSOLLARBEITSPLAN_I_ARBEITSGANGNUMMER,
									FertigungFac.FLR_LOSSOLLARBEITSPLAN_I_UNTERARBEITSGANG,
									FertigungFac.FLR_LOSSOLLARBEITSPLAN_I_MASCHINENVERSATZTAGE,
									FertigungFac.FLR_LOSSOLLARBEITSPLAN_FLRMASCHINE
											+ "."
											+ ZeiterfassungFac.FLR_MASCHINE_C_IDENTIFIKATIONSNR,
									FertigungFac.FLR_LOSSOLLARBEITSPLAN_FLRARTIKEL
											+ ".c_nr",
									Facade.NICHT_SORTIERBAR,
									FertigungFac.FLR_LOSSOLLARBEITSPLAN_N_GESAMTZEIT,
									Facade.NICHT_SORTIERBAR,
									Facade.NICHT_SORTIERBAR,
									Facade.NICHT_SORTIERBAR,
									Facade.NICHT_SORTIERBAR,
									Facade.NICHT_SORTIERBAR,
									FertigungFac.FLR_LOSSOLLARBEITSPLAN_B_FERTIG }));
				}
			} else {

				if (bMaschinenbezeichnungStattArtikelbezeichnung) {
					setTableInfo(new TableInfo(
							new Class[] { Integer.class, String.class,
									Integer.class, Integer.class,
									Integer.class, String.class, String.class,
									String.class, BigDecimal.class,
									BigDecimal.class, Boolean.class },
							new String[] {
									"i_id",
									getTextRespectUISpr("lp.art", mandantCNr,
											locUI),
									getTextRespectUISpr("fert.ag", mandantCNr,
											locUI),
									getTextRespectUISpr("fert.uag", mandantCNr,
											locUI),
									getTextRespectUISpr("fert.agbeginn",
											mandantCNr, locUI),
									getTextRespectUISpr("lp.maschine",
											mandantCNr, locUI),
									getTextRespectUISpr("lp.bezeichnung",
											mandantCNr, locUI),
									getTextRespectUISpr("lp.ident", mandantCNr,
											locUI),

									getTextRespectUISpr("lp.dauer", mandantCNr,
											locUI),
									getTextRespectUISpr("lp.ist", mandantCNr,
											locUI),
									getTextRespectUISpr("lp.fertig",
											mandantCNr, locUI) },
							new int[] {
									QueryParameters.FLR_BREITE_SHARE_WITH_REST,
									3, 3, 3, 3, 3,
									QueryParameters.FLR_BREITE_XM,
									QueryParameters.FLR_BREITE_SHARE_WITH_REST,
									QueryParameters.FLR_BREITE_PREIS,
									QueryParameters.FLR_BREITE_PREIS,
									QueryParameters.FLR_BREITE_S },
							new String[] {
									FertigungFac.FLR_LOSSOLLARBEITSPLAN_I_ID,
									Facade.NICHT_SORTIERBAR,
									FertigungFac.FLR_LOSSOLLARBEITSPLAN_I_ARBEITSGANGNUMMER,
									FertigungFac.FLR_LOSSOLLARBEITSPLAN_I_UNTERARBEITSGANG,
									FertigungFac.FLR_LOSSOLLARBEITSPLAN_I_MASCHINENVERSATZTAGE,
									FertigungFac.FLR_LOSSOLLARBEITSPLAN_FLRMASCHINE
											+ "."
											+ ZeiterfassungFac.FLR_MASCHINE_C_IDENTIFIKATIONSNR,
									FertigungFac.FLR_LOSSOLLARBEITSPLAN_FLRMASCHINE
											+ ".c_bez",
									FertigungFac.FLR_LOSSOLLARBEITSPLAN_FLRARTIKEL
											+ ".c_nr",

									FertigungFac.FLR_LOSSOLLARBEITSPLAN_N_GESAMTZEIT,
									Facade.NICHT_SORTIERBAR,
									FertigungFac.FLR_LOSSOLLARBEITSPLAN_B_FERTIG }));
				} else {

					setTableInfo(new TableInfo(
							new Class[] { Integer.class, String.class,
									Integer.class, Integer.class,
									Integer.class, String.class, String.class,
									String.class, BigDecimal.class,
									BigDecimal.class, Boolean.class },
							new String[] {
									"i_id",
									getTextRespectUISpr("lp.art", mandantCNr,
											locUI),
									getTextRespectUISpr("fert.ag", mandantCNr,
											locUI),
									getTextRespectUISpr("fert.uag", mandantCNr,
											locUI),
									getTextRespectUISpr("fert.agbeginn",
											mandantCNr, locUI),
									getTextRespectUISpr("lp.maschine",
											mandantCNr, locUI),
									getTextRespectUISpr("lp.ident", mandantCNr,
											locUI),
									getTextRespectUISpr("lp.bezeichnung",
											mandantCNr, locUI),
									getTextRespectUISpr("lp.dauer", mandantCNr,
											locUI),
									getTextRespectUISpr("lp.ist", mandantCNr,
											locUI),
									getTextRespectUISpr("lp.fertig",
											mandantCNr, locUI) },
							new int[] {
									QueryParameters.FLR_BREITE_SHARE_WITH_REST,
									3, 3, 3, 3, 3,
									QueryParameters.FLR_BREITE_M,
									QueryParameters.FLR_BREITE_SHARE_WITH_REST,
									QueryParameters.FLR_BREITE_PREIS,
									QueryParameters.FLR_BREITE_PREIS,
									QueryParameters.FLR_BREITE_S },
							new String[] {
									FertigungFac.FLR_LOSSOLLARBEITSPLAN_I_ID,
									Facade.NICHT_SORTIERBAR,
									FertigungFac.FLR_LOSSOLLARBEITSPLAN_I_ARBEITSGANGNUMMER,
									FertigungFac.FLR_LOSSOLLARBEITSPLAN_I_UNTERARBEITSGANG,
									FertigungFac.FLR_LOSSOLLARBEITSPLAN_I_MASCHINENVERSATZTAGE,
									FertigungFac.FLR_LOSSOLLARBEITSPLAN_FLRMASCHINE
											+ "."
											+ ZeiterfassungFac.FLR_MASCHINE_C_IDENTIFIKATIONSNR,
									FertigungFac.FLR_LOSSOLLARBEITSPLAN_FLRARTIKEL
											+ ".c_nr",
									Facade.NICHT_SORTIERBAR,
									FertigungFac.FLR_LOSSOLLARBEITSPLAN_N_GESAMTZEIT,
									Facade.NICHT_SORTIERBAR,
									FertigungFac.FLR_LOSSOLLARBEITSPLAN_B_FERTIG }));
				}
			}
		}
		return super.getTableInfo();
	}
}
