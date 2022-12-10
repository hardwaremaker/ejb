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
package com.lp.server.util.fastlanereader;

import java.io.Serializable;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.ejb.Remove;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.lp.server.partner.fastlanereader.generated.FLRPartner;
import com.lp.server.partner.service.PartnerFac;
import com.lp.server.system.fastlanereader.service.TableColumnInformation;
import com.lp.server.system.jcr.service.PrintInfoDto;
import com.lp.server.system.pkgenerator.format.LpBelegnummerFormat;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.HelperServer;
import com.lp.server.util.fastlanereader.service.query.FilterBlock;
import com.lp.server.util.fastlanereader.service.query.FilterKriterium;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.server.util.fastlanereader.service.query.SortierKriterium;
import com.lp.server.util.fastlanereader.service.query.TableInfo;
import com.lp.util.BigDecimal0;
import com.lp.util.BigDecimal1;
import com.lp.util.BigDecimal13;
import com.lp.util.BigDecimal3;
import com.lp.util.BigDecimal4;
import com.lp.util.BigDecimal5;
import com.lp.util.BigDecimal6;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;
import com.lp.util.Pair;

/**
 * A UseCaseHandler is responsible to handle all use case specific tasks for the
 * FastLaneReaderBean. One responsibility is to generate HQL
 * (HibernatQueryLanguage) statement out of the QueryParameters in order to
 * return the correct data from the database.
 * <P>
 * 
 * A UseCaseHandler must be implemented for each use case and then registered in
 * {com.lp.server.util.fastlanereader.ejb.FastLaneReaderBean#ejbCreate() }. Also
 * a constant identifying the use case has to be added to
 * {com.lp.util.fastlanereader.query.QueryParameters QueryParameters}, which has
 * to be passed to the FastLaneReader on every method access.
 * <P>
 * 
 * The UseCaseHandler is also responsible for the size of the returned data page
 * in order to be able to optimize performance individually for each usecase.
 * 
 * @author werner
 */

public abstract class UseCaseHandler extends Facade implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private QueryParameters queryParameters;
	private long rowCount = 0;
	private Timestamp tErstellung = null;
	private TableColumnInformation columns = null;

	public Timestamp getTErstellung() {
		return tErstellung;
	}

	/*
	 * SK: Logger hier auskommentiert da er nicht serialisierbar ist und fuer die
	 * Dokumentenablage ein UseCaseHandler Objekt an den Client geschickt werden
	 * muss.
	 */
	// protected final ILPLogger myLogger;
	protected TheClientDto theClientDto = null;
	protected FilterKriterium[] aFilterKriterium = null;
	private Locale localeOld = null;
	private String mandantOld = null;

	protected FilterKriterium[] defaultFiltersForTableInfo = null;

	public String getMandantOld() {
		return mandantOld;
	}

	public void setMandantOld(String mandantOld) {
		this.mandantOld = mandantOld;
	}

	protected TableInfo tableInfo = null;
	protected HashMap<?, ?> hmStatus = null;
	private String uuid = null;

	private Integer usecaseId = null;

	public Integer getUsecaseId() {
		return usecaseId;
	}

	public void setUsecaseId(Integer usecaseId) {
		this.usecaseId = usecaseId;
	}

	public String getPartnerName(FLRPartner flrpartner) {

		if (flrpartner != null && !PartnerFac.PARTNER_ANREDE_FIRMA.equals(flrpartner.getAnrede_c_nr())) {

			String partner = flrpartner.getC_name1nachnamefirmazeile1();

			if (flrpartner.getC_name2vornamefirmazeile2() != null) {
				partner = flrpartner.getC_name2vornamefirmazeile2() + " " + partner;
			}

			return partner;
		} else {

			return flrpartner.getC_name1nachnamefirmazeile1();
		}

	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	// the default size of the data page returned in QueryResult.
	public static int PAGE_SIZE = 50;

	public static int MENGE_UI_NACHKOMMASTELLEN = -1;
	public static int PREISERABATTE_UI_NACHKOMMASTELLEN_ALLGEMEIN = -1;
	public static int PREISERABATTE_UI_NACHKOMMASTELLEN_EK = -1;
	public static int PREISERABATTE_UI_NACHKOMMASTELLEN_VK = -1;
	public static boolean SORTIERUNG_UI_PARTNER_ORT = false;

	public boolean bReferenznummerInPositionen = false;
	public boolean bTitelInAF_BS_LS_RE_LOS = false;

	public UseCaseHandler() {
		super();
		tErstellung = new Timestamp(System.currentTimeMillis());
	}

	/**
	 * sets and executes the passed queryParameters. The row count accessable
	 * through getRowCount() is initialized by calling getRowCountFromDataBase().
	 * The row count is assumed to stay constant until the next call to setQuery().
	 * The result is retrieved using the sort() method of this class.
	 * 
	 * @param queryParameters the new query to execute.
	 * @return the data containing the page where the row with the id passed in the
	 *         queryParameters is located.
	 * @throws EJBExceptionLP
	 */
	public QueryResult setQuery(QueryParameters queryParameters) throws EJBExceptionLP {
		// myLogger.logData("setQuery(): " + queryParameters);

		this.queryParameters = queryParameters;

		if (queryParameters.iMaxAnzahlZeilen > 0) {
			PAGE_SIZE = queryParameters.iMaxAnzahlZeilen;
			this.rowCount = queryParameters.iMaxAnzahlZeilen;

			QueryResult result = getPageAt(0);

			if (result != null) {
				int iSelectedRow = 0;
				if (this.queryParameters.getKeyOfSelectedRow() != null) {

					for (int i = 0; i < result.getRowData().length; i++) {

						Object key = result.getRowData()[i][0];

						if (key != null && this.queryParameters.getKeyOfSelectedRow() != null) {
							if (key.equals(this.queryParameters.getKeyOfSelectedRow())) {
								iSelectedRow = i;
								break;
							}
						}

					}
				}
				result.setIndexOfSelectedRow(iSelectedRow);
				rowCount = result.getRowData().length;
				result.setRowCount(this.getRowCount());
			}
			return result;
		} else {
			this.queryParameters = queryParameters;
			setRowCount(getRowCountFromDataBase());

			QueryResult result = null;
			if (queryParameters.getIsApi()) {
				if (queryParameters.getSortKrit() != null) {
					result = sort(queryParameters.getSortKrit(), queryParameters.getKeyOfSelectedRow());
				} else {
					Integer rowIndex = (Integer) queryParameters.getKeyOfSelectedRow();
					if (rowIndex == null) {
						rowIndex = 0;
					}
					result = getPageAt(rowIndex);
				}
			} else {
				result = this.sort(queryParameters.getSortKrit(), this.queryParameters.getKeyOfSelectedRow());
			}

			if (result != null) {
				result.setRowCount(this.getRowCount());
			}
			return result;
		}

	}

	/**
	 * gets the number of rows in the data described by the current query.
	 * 
	 * @return the number of rows in the querie's (complete) result.
	 */
	protected long getRowCount() {
		return this.rowCount;
	}

	public Locale getLocaleOld() {
		return localeOld;
	}

	/**
	 * gets the current query.
	 * 
	 * @return the current query.
	 */
	protected QueryParameters getQuery() {
		return this.queryParameters;
	}

	/**
	 * gets the total number of rows available using the current query.
	 * 
	 * @return the number of rows in the query.
	 */
	protected abstract long getRowCountFromDataBase();

	/**
	 * Sets the current User.
	 * 
	 * @param theClientDto the current User
	 */
	public void setCurrentUser(TheClientDto theClientDto) {
		this.theClientDto = theClientDto;
	}

	public void setDefaultFiltersForTableInfo(FilterKriterium[] defaultFilters) {
		defaultFiltersForTableInfo = defaultFilters;
	}

	// SP 2015
	protected void logQuery(String query) {
		StackTraceElement[] stack = Thread.currentThread().getStackTrace();
		if (stack != null && stack.length > 2)
			myLogger.warn(stack[2].getMethodName());
		// myLogger.warn(String.format("uuid = %s; sTable = %s; IDUser = %s; Login Time
		// = %s; Query = %s",
		// getUuid(), getSTable(), theClientDto.getIDUser(),
		// theClientDto.getDLoggedin().toString(), query));
	}

	/**
	 * sorts the data of the current query using the specified criterias and returns
	 * the page of data where the row of selectedId is contained. The sort criterias
	 * are updated in queryParameters in order to make them available throughout the
	 * transaction.
	 * 
	 * @param sortierKriterien the new sort criterias.
	 * @param selectedId       the id of the entity that should be included in the
	 *                         result page.
	 * @return the sorted data containing the page where the entity with the
	 *         specified id is located.
	 * @throws EJBExceptionLP
	 */
	public abstract QueryResult sort(SortierKriterium[] sortierKriterien, Object selectedId) throws EJBExceptionLP;

	/**
	 * gets the page of data for the specified row using the current
	 * queryParameters.
	 * 
	 * @param rowIndex the index of the row that should be contained in the page.
	 * @return the data page for the specified row.
	 * @throws EJBExceptionLP
	 */
	public abstract QueryResult getPageAt(Integer rowIndex) throws EJBExceptionLP;

	public PrintInfoDto getSDocPathAndPartner(Object key) {
		return null;
	}

	public String getSTable() {
		return null;
	}

	public Object[] getStatusMitUebersetzung(String sStatus) throws RemoteException {
		if (hmStatus == null) {
			// das ui-locale wurde gewechselt.
			hmStatus = getSystemMultilanguageFac().getAllStatiMitUebersetzung(theClientDto.getLocUi(), theClientDto);
		}
		return new Object[] { sStatus, hmStatus.get(sStatus) };
	}

	public Object[] getStatusMitUebersetzung(String sStatus, Date versandDatum, String versandType)
			throws RemoteException {
		if (hmStatus == null) {
			// das ui-locale wurde gewechselt.
			hmStatus = getSystemMultilanguageFac().getAllStatiMitUebersetzung(theClientDto.getLocUi(), theClientDto);
		}
		return new Object[] { sStatus, hmStatus.get(sStatus), versandDatum, versandType };
	}

	/**
	 * gets information such as column names an column types used for the table on
	 * the client side.
	 * 
	 * @return the information needed to create the client side table.
	 */
	public TableInfo getTableInfo() {
		if (!theClientDto.getLocUi().equals(getLocaleOld()) || !theClientDto.getMandant().equals(getMandantOld())) {
			// das ui-locale wurde gewechselt.
			tableInfo = null;
			hmStatus = null;
			setLocaleOld(theClientDto.getLocUi());
			setMandantOld(theClientDto.getMandant());
			try {
				// UW 31.03.06 Die Nachkommastellen fuer das FLR UI setzen
				ParametermandantDto parametermandantDto = null;

				if (MENGE_UI_NACHKOMMASTELLEN == -1) {
					parametermandantDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_MENGE_UI_NACHKOMMASTELLEN);

					MENGE_UI_NACHKOMMASTELLEN = ((Integer) parametermandantDto.getCWertAsObject()).intValue();
				}

				if (PREISERABATTE_UI_NACHKOMMASTELLEN_ALLGEMEIN == -1) {
					parametermandantDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_PREISERABATTE_UI_NACHKOMMASTELLEN);

					PREISERABATTE_UI_NACHKOMMASTELLEN_ALLGEMEIN = ((Integer) parametermandantDto.getCWertAsObject())
							.intValue();
				}

				if (PREISERABATTE_UI_NACHKOMMASTELLEN_EK == -1) {
					parametermandantDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ALLGEMEIN,
							ParameterFac.PARAMETER_PREISERABATTE_UI_NACHKOMMASTELLEN_EK);

					PREISERABATTE_UI_NACHKOMMASTELLEN_EK = ((Integer) parametermandantDto.getCWertAsObject())
							.intValue();
				}

				if (PREISERABATTE_UI_NACHKOMMASTELLEN_VK == -1) {
					parametermandantDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
							ParameterFac.KATEGORIE_ALLGEMEIN,
							ParameterFac.PARAMETER_PREISERABATTE_UI_NACHKOMMASTELLEN_VK);

					PREISERABATTE_UI_NACHKOMMASTELLEN_VK = ((Integer) parametermandantDto.getCWertAsObject())
							.intValue();
				}

				parametermandantDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_ALLGEMEIN,
						ParameterFac.PARAMETER_BEWEGUNGSMODULE_SORTIERUNG_PARTNER_ORT);
				SORTIERUNG_UI_PARTNER_ORT = ((Boolean) parametermandantDto.getCWertAsObject()).booleanValue();

				parametermandantDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_REFERENZNUMMER_IN_POSITIONEN);
				bReferenznummerInPositionen = ((Boolean) parametermandantDto.getCWertAsObject()).booleanValue();

				parametermandantDto = getParameterFac().getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_PROJEKT_TITEL_IN_AF_BS_LS_RE_LOS);
				bTitelInAF_BS_LS_RE_LOS = ((Boolean) parametermandantDto.getCWertAsObject()).booleanValue();

			} catch (Throwable t) {
				myLogger.error("getTableInfo()", t);
				try {
					throw new EJBExceptionLP(EJBExceptionLP.FEHLER, new Exception(t));
				} catch (EJBExceptionLP e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return tableInfo;
	}

	/**
	 * Alle FilterKriterien, die gesetzt sind holen.
	 * 
	 * @throws NumberFormatException
	 */
	public void getFilterKriterien() throws NumberFormatException {
		if (getQuery() != null && getQuery().getFilterBlock() != null
				&& getQuery().getFilterBlock().filterKrit != null) {
			aFilterKriterium = getQuery().getFilterBlock().filterKrit;
		}
	}

	public void setLocaleOld(Locale localeOld) {
		this.localeOld = localeOld;
	}

	public void setTableInfo(TableInfo tableInfo) {
		this.tableInfo = tableInfo;
	}

	public void setRowCount(long rowCount) {
		this.rowCount = rowCount;
	}

	/**
	 * Suche in rows bis rowMax nach row.
	 * 
	 * @param rowsI   Object[][]: bis rowMax gefuellt.
	 * @param rowMaxI int
	 * @param keyI    Object[]: sucht um das Einfuegen an.
	 * @param localeI String
	 * @return int: <br/>
	 *         >0: an dieser Stelle in rows einzufuegen;<br/>
	 *         =-1: bereits vorhanden.
	 */
	protected int findRowidx(Object[][] rowsI, int rowMaxI, Object keyI, String localeI) {

		int xRowForInsert = -1;
		int xRows = 0;

		while (xRows < rowMaxI) {
			if (rowsI[xRows][0].equals(keyI)) {
				// id gefunden
				if (localeI != null) {
					// neue Uebersetzung vorhanden -> immer einfuegen
					xRowForInsert = xRows;
					break;
				} else {
					// keine neue Uebersetzung -> lassen
					break;
				}
			}
			xRows++;
		}
		if (xRows == rowMaxI) {
			// keyI not found -> hinten anfuegen.
			xRowForInsert = xRows;
		}
		return xRowForInsert;
	}

	protected Session setFilter(Session session) {
		String sLocUI = Helper.locale2String(theClientDto.getLocUi());
		session.enableFilter("filterLocale").setParameter("paramLocale", sLocUI);
		return session;
	}

	protected final Class<?> getUIClassBigDecimalNachkommastellen(int iNachkommastellen) {
		Class<?> clazz;
		switch (iNachkommastellen) {
		case 0: {
			clazz = BigDecimal0.class;
		}
			break;
		case 1: {
			clazz = BigDecimal1.class;
		}
			break;
		case 3: {
			clazz = BigDecimal3.class;
		}
			break;
		case 4: {
			clazz = BigDecimal4.class;
		}
			break;
		case 5: {
			clazz = BigDecimal5.class;
		}
			break;
		case 6: {
			clazz = BigDecimal6.class;
		}
			break;
		case 13: {
			clazz = BigDecimal13.class;
		}
			break;
		default: {
			clazz = BigDecimal.class;
		}
		}
		return clazz;
	}

	/**
	 * Passe Spaltenbreite von FLRs an Nachkommastellen an. Nachkommastellen fuer
	 * Preise und Mengen werden ueber Mandantparameter gesetzt.
	 * 
	 * @param iBreiteFeldDefault       Die Breite des Feldes mit der
	 *                                 iNachkommastellenDefault Anzahl von
	 *                                 Nachkommastellen
	 * @param iNachkommastellenDefault aktuelle Breite hat Platz fuer soviele
	 *                                 Nachkommastellen
	 * @param iNachkommastellen        int Wieviele Nachkommastellen sind ueber
	 *                                 Mandantparameter eingestellt
	 * @return int Feld um diese Anzahl von Stellen erweitern, damit alle
	 *         Nachkommastellen Platz haben.
	 */
	protected final int getUIBreiteAbhaengigvonNachkommastellen(int iBreiteFeldDefault, int iNachkommastellenDefault,
			int iNachkommastellen) {
		int iBreite = iBreiteFeldDefault;
		int iDiff = 0;
		iDiff = iNachkommastellen - iNachkommastellenDefault;
		if (iDiff > 0) {
			// Feldbreite um iDiff erhoehen
			iBreite += iDiff;
		}
		return iBreite;
	}

	/**
	 * Gib mir die Ident-Spalten Breite
	 * 
	 * @return die Breite der Identspalte
	 */
	protected int getUIBreiteIdent() {
		return QueryParameters.FLR_BREITE_IDENT;
	}

	/**
	 * Gib mir die Breite einer Preis oder Menge Spalte in Abhaengigkeit von Anzahl
	 * der eingestellten Nachkommastellen durch Mandantparameter.
	 * {@link #getUIObjectBigDecimalNachkommastellen}
	 * 
	 * @param sFeldNameI        Entwerder QueryParameters.PREIS oder
	 *                          QueryParameters.MENGE
	 * @param iNachkommastellen int
	 * @return int
	 */
	protected final int getUIBreiteAbhaengigvonNachkommastellen(String sFeldNameI, int iNachkommastellen) {
		int iBreite = 0;

		if (sFeldNameI != null) {
			if (sFeldNameI.equals(QueryParameters.MENGE)) {
				iBreite = getUIBreiteAbhaengigvonNachkommastellen(QueryParameters.FLR_BREITE_MENGE,
						QueryParameters.FLR_BREITE_MENGE_NACHKOMMASTELLEN, iNachkommastellen);
			} else if (sFeldNameI.equals(QueryParameters.PREIS)) {
				iBreite = getUIBreiteAbhaengigvonNachkommastellen(QueryParameters.FLR_BREITE_PREIS,
						QueryParameters.FLR_BREITE_PREIS_NACHKOMMASTELLEN, iNachkommastellen);
			}
		}
		return iBreite;
	}

	protected final BigDecimal getUIObjectBigDecimalNachkommastellen(BigDecimal bdValue, int iNachkommastellen) {
		if (bdValue == null) {
			return null;
		}

		switch (iNachkommastellen) {
		case 0: {
			return new BigDecimal0(Helper.rundeKaufmaennisch(bdValue, 0));
		}
		case 3: {
			return new BigDecimal3(Helper.rundeKaufmaennisch(bdValue, 3));
		}
		case 4: {
			return new BigDecimal4(Helper.rundeKaufmaennisch(bdValue, 4));
		}
		case 5: {
			return new BigDecimal5(Helper.rundeKaufmaennisch(bdValue, 5));
		}
		case 6: {
			return new BigDecimal6(Helper.rundeKaufmaennisch(bdValue, 6));
		}
		default: {
			return Helper.rundeKaufmaennisch(bdValue, 2);
		}
		}
	}

	/**
	 * if isBIgnoreCase is set, then a part of a condition for the where-clause is
	 * build
	 * 
	 * @param object          the object to handle
	 * @param filterKriterium the filterKriterium
	 * @return the part of the where clause
	 */
	protected final String buildWhereClausePart(String object, FilterKriterium filterKriterium) {
		StringBuffer where = new StringBuffer();
		if (filterKriterium.isBIgnoreCase()) {
			where.append(" lower(" + object + filterKriterium.kritName + ")");
		} else {
			where.append(" " + object + filterKriterium.kritName);
		}
		where.append(" " + filterKriterium.operator);
		if (filterKriterium.isBIgnoreCase()) {
			where.append(" " + filterKriterium.value.toLowerCase());
		} else {
			where.append(" " + filterKriterium.value);
		}
		return where.toString();
	}

	protected final String buildWhereClauseRTrim(String flr, FilterKriterium filterKriterium) {
		StringBuffer where = new StringBuffer();
		try {
			String sValue = filterKriterium.value;
			where.append(" rtrim(");
			if (filterKriterium.isBIgnoreCase())
				where.append("lower(");
			where.append(flr).append(filterKriterium.kritName).append(")");
			if (filterKriterium.isBIgnoreCase())
				where.append(")");
			where.append(filterKriterium.operator);
			where.append(sValue);
			return where.toString();
		} catch (Exception ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, ex);
		}
	}

	public String getKommentarart(String intern, String extern) {

		boolean bInternvorhanden = false;
		if (intern != null && intern.length() > 0) {
			bInternvorhanden = true;
		}
		boolean bExternvorhanden = false;
		if (extern != null && extern.length() > 0) {
			bExternvorhanden = true;
		}

		if (bInternvorhanden && bExternvorhanden) {
			return "K";
		} else if (bInternvorhanden == true && bExternvorhanden == false) {
			return "I";
		} else if (bInternvorhanden == false && bExternvorhanden == true) {
			return "E";
		}

		return null;
	}

	public String getKommentarart(String kommentar) {
		if (kommentar != null && kommentar.length() > 0) {
			return "K";
		} else {
			return null;
		}
	}

	public boolean istBelegnummernInJahr(String FLR, String value, String zusaetzlichesWhere) {
		TheClientDto theClient = theClientDto;
		// Belegnummernsuche auch in "altem" Jahr, wenn im neuen noch keines
		// vorhanden ist
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			session = factory.openSession();
			String queryString = "select flr.i_id FROM " + FLR + " as flr where flr.c_nr LIKE " + value
					+ " and flr.mandant_c_nr='" + theClient.getMandant() + "' ";

			if (zusaetzlichesWhere != null) {
				queryString += " AND flr." + zusaetzlichesWhere;
			}

			Query query = session.createQuery(queryString);
			List<?> resultList = query.list();
			if (resultList.size() != 0) {
				return true;
			}
		} finally {
			sessionClose(session);
		}
		return false;
	}

	public boolean istBelegnummernInJahr(String FLR, String value) {
		return istBelegnummernInJahr(FLR, value, null);
	}

	public boolean istBelegnummernInJahrFuerViewLSRE(String FLR, String value) {
		// Belegnummernsuche auch in "altem" Jahr, wenn im neuen noch keines
		// vorhanden ist
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			session = factory.openSession();
			String queryString = "select flr.c_nr FROM " + FLR + " as flr where flr.c_nr LIKE " + value + "";
			Query query = session.createQuery(queryString);
			List<?> resultList = query.list();
			if (resultList.size() != 0) {
				return true;
			}
		} finally {
			sessionClose(session);
		}
		return false;
	}

	@Remove
	public void remove() {
		queryParameters = null;
		theClientDto = null;
		aFilterKriterium = null;
		localeOld = null;
		tableInfo = null;
		hmStatus = null;
	}

	public String buildWhereBelegnummer(FilterKriterium filterKriterien, boolean bVorjahr) throws RemoteException {
		return buildWhereBelegnummer(filterKriterien, bVorjahr, null, null);
	}

	public String buildWhereBelegnummer(FilterKriterium filterKriterien, boolean bVorjahr,
			String kategorieParameterFortlaufend, String parameterBelegnummerFortlaufend) throws RemoteException {
		LpBelegnummerFormat f = getBelegnummerGeneratorObj().getBelegnummernFormat(theClientDto.getMandant());
		String mandantKuerzel = getParameterFac().getMandantparameter(theClientDto.getMandant(),
				ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_BELEGNUMMER_MANDANTKENNUNG).getCWert();
		Integer iGeschaeftsjahr = getParameterFac().getGeschaeftsjahr(theClientDto.getMandant());
		if (bVorjahr)
			iGeschaeftsjahr--;
		String sValue = HelperServer.getBelegnummernFilter(f, iGeschaeftsjahr, mandantKuerzel, filterKriterien.value);

		if (kategorieParameterFortlaufend != null && parameterBelegnummerFortlaufend != null) {
			Integer iWert = (Integer) getParameterFac().getMandantparameter(theClientDto.getMandant(),
					kategorieParameterFortlaufend, parameterBelegnummerFortlaufend).getCWertAsObject();

			if (iWert.intValue() == -1) {

				ParametermandantDto pTrennzeichen = getParameterFac().getMandantparameter(theClientDto.getMandant(),
						ParameterFac.KATEGORIE_ALLGEMEIN, ParameterFac.PARAMETER_BELEGNUMMERNFORMAT_TRENNZEICHEN);

				int i = sValue.indexOf(pTrennzeichen.getCWert());
				if (i > 0) {
					sValue = "'%" + sValue.substring(i);
				}
			}
		}

		return sValue;
	}

	/**
	 * Ermittelt die Anzahl der in der Tabelle befindlichen Datens&auml;tze
	 * basierend auf der angegebenen Query
	 * 
	 * @param queryString enth&auml;lt die SQL Query mit der die Anzahl der
	 *                    Datens&auml;tze ermittelt wird
	 * @return Anzahl der Datens&auml;tze
	 */
	protected long getRowCountFromDataBaseByQuery(String queryString) {
		long rowCount = 0;
		SessionFactory factory = FLRSessionFactory.getFactory();
		Session session = null;
		try {
			session = factory.openSession();
			session = setFilter(session);
			Query query = session.createQuery(queryString);
			List<?> rowCountResult = query.list();
			if (rowCountResult != null && rowCountResult.size() > 0) {
				rowCount = ((Long) rowCountResult.get(0)).longValue();
			}
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
		} finally {
			sessionClose(session);
		}

		return rowCount;
	}

	protected Integer getLimit() {
		Integer queryLimit = queryParameters.getLimit();
		if (queryLimit == null)
			return PAGE_SIZE;

		return queryLimit <= 0 ? Integer.MAX_VALUE : queryLimit;
		// return queryParameters.getLimit() != null ?
		// queryParameters.getLimit() : PAGE_SIZE ;
	}

	protected Integer getStartIndex(Integer rowIndex, Integer pageSize) {
		return queryParameters.getIsApi() ? rowIndex : Math.max(rowIndex - (pageSize / 2), 0);
	}

	public List<Pair<?, ?>> getInfoForSelectedIIds(TheClientDto theClientDto, List<Object> selectedIIds) {
		return null;
	}

	public TableColumnInformation getTableColumnInformation() {
		return columns;
	}

	protected void setTableColumnInformation(TableColumnInformation columnInfo) {
		columns = columnInfo;
	}

	/**
	 * Konstruiert die Where-Klausel fuer die Textsuche, die mit der erweiterten
	 * Suche (Google-Suche) arbeitet. Verhindert die mehrfache Anzeige von gleichen
	 * Treffern.
	 * 
	 * @param sFlrObjektname  FLR-Klasse des aufrufenden Belegs
	 * @param sFlrBeleg       FLR-Name des aufrufenden Belegs
	 * @param filterKriterium Filter des Benutzers
	 * @return Where-Klausel der erweiterten Textsuche
	 */
	protected String buildWhereClauseExtendedSearchWithoutDuplicates(String sFlrObjektname, String sFlrBeleg,
			FilterKriterium filterKriterium) {

		StringBuilder where = new StringBuilder();
		where.append(" exists (SELECT flrtextsuche FROM ").append(sFlrObjektname).append(" as flrtextsuche ");
		where.append("WHERE ").append(sFlrBeleg).append("i_id=flrtextsuche.i_id ");
		where.append("AND ").append(buildWhereClauseExtendedSearch(Arrays.asList(filterKriterium.value.split(" ")),
				"flrtextsuche." + filterKriterium.kritName, filterKriterium.isBIgnoreCase())).append(") ");

		return where.toString();
	}

	/**
	 * Konstruiert die Where-Klausel fuer eine erweiterte Suche aehnlich zur
	 * Google-Suche. Es koennen mehrere Suchbegriffe, durch ein Leerzeichen
	 * getrennt, angegeben werden, die im jeweiligen Suchtext enthalten sein
	 * muessen. Begriffe mit einem fuehrenden Bindestrich duerfen im Suchtext nicht
	 * vorkommen.
	 * 
	 * @param items        Liste der Suchbegriffe
	 * @param searchString Bezeichnung des HQL-Objekts, in dem gesucht wird.
	 * @param bIgnoreCase  true, wenn Gross-/Kleinschreibung ignoriert werden soll.
	 * @return die Where-Klausel
	 */
	protected String buildWhereClauseExtendedSearch(List<String> items, String searchString, boolean bIgnoreCase) {
		Iterator<String> iter = items.iterator();
		StringBuilder where = new StringBuilder(" ");

		while (iter.hasNext()) {
			String item = iter.next();

			if (item.startsWith("-")) {
				where.append(" NOT");
				item = item.substring(1);
			}

			where.append(" ");
			if (bIgnoreCase) {
				where.append("lower(").append(searchString).append(")");
			} else {
				where.append(searchString);
			}

			where.append(" LIKE ");
			if (bIgnoreCase) {
				where.append("lower('%").append(item).append("%') ");
			} else {
				where.append("'%").append(item).append("%' ");
			}

			if (iter.hasNext()) {
				where.append("AND ");
			}
		}

		return where.toString();
	}

	protected String buildDefaultWhereClause(String flrTablePrefix) {
		StringBuffer where = new StringBuffer("");

		if (getQuery() != null && getQuery().getFilterBlock() != null
				&& getQuery().getFilterBlock().filterKrit != null) {

			FilterBlock filterBlock = getQuery().getFilterBlock();
			FilterKriterium[] filterKriterien = getQuery().getFilterBlock().filterKrit;
			String booleanOperator = filterBlock.boolOperator;
			boolean filterAdded = false;

			for (int i = 0; i < filterKriterien.length; i++) {
				filterAdded = buildDefaultFilterKriterium(filterKriterien[i], flrTablePrefix, filterAdded,
						booleanOperator, where);
			}
			if (filterAdded) {
				where.insert(0, " WHERE");
			}
		}

		return where.toString();
	}

	protected boolean buildDefaultFilterKriterium(FilterKriterium filterkriterium, String flrTablePrefix,
			boolean filterAdded, String booleanOperator, StringBuffer where) {
		if (filterkriterium.isKrit) {
			if (filterAdded) {
				where.append(" " + booleanOperator);
			}
			filterAdded = true;
			where.append(" " + flrTablePrefix + filterkriterium.kritName);
			where.append(" " + filterkriterium.operator);
			where.append(" " + filterkriterium.value);
		}
		return filterAdded;
	}

	protected QueryResult defaultSort(SortierKriterium[] sortierKriterien, Object selectedId, String sortQueryString)
			throws EJBExceptionLP {
		getQuery().setSortKrit(sortierKriterien);

		QueryResult result = null;
		int rowNumber = 0;

		if (selectedId != null) {
			SessionFactory factory = FLRSessionFactory.getFactory();
			Session session = null;

			try {
				session = factory.openSession();
				Query query = session.createQuery(sortQueryString);
				ScrollableResults scrollableResult = query.scroll();
				if (scrollableResult != null) {
					scrollableResult.beforeFirst();
					while (scrollableResult.next()) {
						Object id = scrollableResult.get(0);
						if (selectedId.equals(id)) {
							rowNumber = scrollableResult.getRowNumber();
							break;
						}
					}
				}
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_FLR, e);
			} finally {
				sessionClose(session);
			}
		}

		if (rowNumber < 0 || rowNumber >= this.getRowCount()) {
			rowNumber = 0;
		}

		result = getPageAt(new Integer(rowNumber));
		result.setIndexOfSelectedRow(rowNumber);

		return result;
	}

	protected void sessionClose(Session session) {
		if (session != null) {
			try {
				session.close();
			} catch (HibernateException he) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER, he);
			}
		}
	}

	protected String buildWhereClausePart(String flrTablePrefix, String kritName, String operator, String value,
			boolean bIgnoreCase) {
		return buildWhereClausePart(flrTablePrefix, new FilterKriterium(kritName, true, value, operator, bIgnoreCase));
	}
}
