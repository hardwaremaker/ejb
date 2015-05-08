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
package com.lp.server.auftrag.bl;

import com.lp.server.util.fastlanereader.UseCaseHandler;
import com.lp.server.util.fastlanereader.service.query.QueryResult;
import com.lp.server.util.fastlanereader.service.query.SortierKriterium;
import com.lp.server.util.fastlanereader.service.query.TableInfo;
import com.lp.util.EJBExceptionLP;

/**
 * Basisklasse fuer eine Tabellenansicht, die ueber Beans gefuettert wird. <br>
 * Diese Handlerklasse geht nicht ueber Hibernate.
 * <p>
 * Copright Logistik Pur GmbH (c) 2004
 * </p>
 * <p>
 * Erstellungsdatum 2005-01-20
 * </p>
 * <p>
 * </p>
 * 
 * @author Uli Walch
 * @version 1.0
 */

public abstract class UseCaseHandlerTabelle extends UseCaseHandler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Die maximale Anzahl der Zeilen in der Tabelle. */
	private final static int ANZAHL_ZEILEN_MAX = 24;

	/** Die maximale Anzahl der Spalten in der Tabelle. */
	private final static int ANZAHL_SPALTEN_MAX = 24;

	/** Die Konfiguration der Spaltenueberschriften. */
	protected TableInfo tableInfo = null;

	/** Die tatsaechliche Anzahl der Zeilen in der Tabelle. */
	private long iAnzahlZeilen = 0;

	/** Die tatsaechliche Anzahl der Spalten in der Tabelle. */
	private int iAnzahlSpalten = 0;

	/** Hier werden die aktuellen Kriterien hinterlegt. */
	// protected FilterKriterium[] aFilterKriterium = null;
	/**
	 * Konstruktor.
	 */
	public UseCaseHandlerTabelle() {
		super();
	}

	public TableInfo getTableInfo() {
		return tableInfo;
	}

	/**
	 * Jede ableitende Klasse muss in dieser Methode die Anzahl der Zeilen
	 * festlegen.
	 * 
	 * @param iAnzahlZeilenI
	 *            die gewuenschte Anzahl der Zeilen
	 * @throws EJBExceptionLP
	 */
	public void setAnzahlZeilen(int iAnzahlZeilenI) throws EJBExceptionLP {
		/*
		 * if (iAnzahlZeilenI < 0 || iAnzahlZeilenI > this.ANZAHL_ZEILEN_MAX) {
		 * throw new EJBExceptionLP(1, new Exception()); }
		 */

		iAnzahlZeilen = iAnzahlZeilenI;
	}

	public long getAnzahlZeilen() {
		return iAnzahlZeilen;
	}

	/**
	 * Jede ableitende Klasse muss in dieser Methode die Anzahl der Spalten
	 * festlegen.
	 * 
	 * @param iAnzahlSpaltenI
	 *            die gewuenschte Anzahl der Zeilen
	 * @throws EJBExceptionLP
	 */
	public void setAnzahlSpalten(int iAnzahlSpaltenI) throws EJBExceptionLP {
		/*
		 * if (iAnzahlSpaltenI < 0 || iAnzahlSpaltenI > this.ANZAHL_SPALTEN_MAX)
		 * { throw new EJBExceptionLP(1, new Exception()); }
		 */

		iAnzahlSpalten = iAnzahlSpaltenI;
	}

	public int getAnzahlSpalten() {
		return iAnzahlSpalten;
	}

	/**
	 * gets the total number of rows represented by the current query. <br>
	 * Das ist der erste Teil des Aufbaus der Tabelle, er bestimmt ueber die
	 * korrekte Anzeige der Scrollbar.
	 * 
	 * @see UseCaseHandler#getRowCountFromDataBase()
	 * @return int
	 */
	protected long getRowCountFromDataBase() {
		// myLogger.info("rowCount: " + getAnzahlZeilen());

		return getAnzahlZeilen();
	}

	/**
	 * sorts the data described by the current query using the specified sort
	 * criterias. The current query is also updated with the new sort criterias.
	 * 
	 * @see UseCaseHandler#sort(SortierKriterium[], Object)
	 * @throws EJBExceptionLP
	 * @param sortierKriterien
	 *            SortierKriterium[]
	 * @param selectedId
	 *            Object
	 * @return QueryResult
	 */
	public QueryResult sort(SortierKriterium[] sortierKriterien,
			Object selectedId) throws EJBExceptionLP {
		int rowNumber = 0; // selektiert ist immer die erste zeile
		QueryResult result = getPageAt(new Integer(rowNumber));
		result.setIndexOfSelectedRow(rowNumber);

		return result;
	}

	/**
	 * Alle FilterKriterien, die gesetzt sind holen.
	 * 
	 * @throws NumberFormatException
	 */
	/*
	 * protected void getFilterKriterien() throws NumberFormatException { if
	 * (getQuery() != null && getQuery().getFilterBlock() != null &&
	 * getQuery().getFilterBlock().filterKrit != null) { aFilterKriterium =
	 * getQuery().getFilterBlock(). filterKrit; } }
	 */
}
