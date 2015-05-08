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
package com.lp.server.util.fastlanereader.service.query;

import java.io.Serializable;

/**
 * This class holds result information of an executed query.
 * 
 * @author werner
 */
public class QueryResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * the actual row number of the first row contained in rowData.
	 */
	private long indexOfFirstRow = -1;

	/**
	 * the actual row number of the last row contained in rowData.
	 */
	private long indexOfLastRow = -1;

	/**
	 * the actual row number of the row that was specified by
	 * {QueryParameters#keyOfSelectedRow : Object}after the query has been
	 * executed (a sort may have changed the index of that row).
	 */
	private long indexOfSelectedRow = -1;

	/**
	 * the total number of rows available for the executed query.
	 */
	private long rowCount = 0;

	/**
	 * a page of row data. The size of the page is handled in the use case
	 * correspondent instance of
	 * {com.lp.server.util.fastlanereader.UseCaseHandler UseCaseHandler}.
	 */
	private Object[][] rowData = null;

	/**
	 * tooltip information for row or cells
	 */
	private String[] tooltipData = null;

	/**
	 * Creates a new QueryResult instance with all attributes set.
	 * 
	 * @param rowData
	 *            the data page's rows.
	 * @param rowCount
	 *            the total number of rows involved with the query (different
	 *            from the page's size).
	 * @param indexOfFirstRow
	 *            the actual row number of the first row contained in rowData
	 * @param indexOfLastRow
	 *            the actual row number of the last row contained in rowData
	 * @param indexOfSelectedRow
	 *            the index of the row that should be selected and made visible
	 *            on the client.
	 */
	public QueryResult(Object[][] rowData, long rowCount, long indexOfFirstRow,
			long indexOfLastRow, long indexOfSelectedRow) {
		this.rowData = rowData;
		this.rowCount = rowCount;
		this.indexOfFirstRow = indexOfFirstRow;
		this.indexOfLastRow = indexOfLastRow;
		this.indexOfSelectedRow = indexOfSelectedRow;
	}

	/**
	 * Creates a new QueryResult instance with all attributes set.
	 * 
	 * @param rowData
	 *            the data page's rows.
	 * @param tooltipData
	 *            String[]
	 * @param rowCount
	 *            the total number of rows involved with the query (different
	 *            from the page's size).
	 * @param indexOfFirstRow
	 *            the actual row number of the first row contained in rowData
	 * @param indexOfLastRow
	 *            the actual row number of the last row contained in rowData
	 * @param indexOfSelectedRow
	 *            the index of the row that should be selected and made visible
	 *            on the client.
	 */
	public QueryResult(Object[][] rowData, long rowCount, long indexOfFirstRow,
			long indexOfLastRow, long indexOfSelectedRow, String[] tooltipData) {
		this(rowData, rowCount, indexOfFirstRow, indexOfLastRow,
				indexOfSelectedRow);
		this.tooltipData = tooltipData;
	}

	/**
	 * @return Returns the indexOfFirstRow.
	 */
	public long getIndexOfFirstRow() {
		return indexOfFirstRow;
	}

	/**
	 * @param indexOfFirstRow
	 *            The indexOfFirstRow to set.
	 */
	public void setIndexOfFirstRow(long indexOfFirstRow) {
		this.indexOfFirstRow = indexOfFirstRow;
	}

	/**
	 * @return Returns the indexOfLastRow.
	 */
	public long getIndexOfLastRow() {
		return indexOfLastRow;
	}

	/**
	 * @param indexOfLastRow
	 *            The indexOfLastRow to set.
	 */
	public void setIndexOfLastRow(long indexOfLastRow) {
		this.indexOfLastRow = indexOfLastRow;
	}

	/**
	 * @return Returns the indexOfSelectedRow.
	 */
	public long getIndexOfSelectedRow() {
		return indexOfSelectedRow;
	}

	/**
	 * @param indexOfSelectedRow
	 *            The indexOfSelectedRow to set.
	 */
	public void setIndexOfSelectedRow(long indexOfSelectedRow) {
		this.indexOfSelectedRow = indexOfSelectedRow;
	}

	/**
	 * @return Returns the rowData.
	 */
	public Object[][] getRowData() {
		return rowData;
	}

	/**
	 * @return true if no data available, false otherwise
	 */
	public boolean isEmpty() {
		return rowData.length == 0;
	}

	/**
	 * @param rowData
	 *            The rowData to set.
	 */
	public void setRowData(Object[][] rowData) {
		this.rowData = rowData;
	}

	/**
	 * @return Returns the rowCount.
	 */
	public long getRowCount() {
		return rowCount;
	}

	public String[] getTooltipData() {
		return tooltipData;
	}

	/**
	 * @param rowCount
	 *            The rowCount to set.
	 */
	public void setRowCount(long rowCount) {
		this.rowCount = rowCount;
	}

	public void setTooltipData(String[] tooltipData) {
		this.tooltipData = tooltipData;
	}
}
