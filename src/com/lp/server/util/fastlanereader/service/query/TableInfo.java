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
 * This class holds use case specific information about the table to be
 * displayed, such as column names and column types.
 * 
 * @author werner
 */
public class TableInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * the types of the column's data.
	 */
	@SuppressWarnings("unchecked")
	private Class[] columnClasses = null;

	/**
	 * the column names (normally Strings, otherwise toString() gets called on
	 * the Object).
	 */
	private Object[] columnHeaderValues = null;

	/**
	 * the column names as they exist in the database (expressed in HQL).
	 * Example the Class KCVAuftrag has an Attribute Partner of type AllgPartner
	 * and the Name1 attribute of the partner will be displayed in the first
	 * column then dataBaseColumnNames[0] must be set to "Partner.Name1";
	 */
	private String[] dataBaseColumnNames = null;
	
	
	private boolean isNegativeWerteRoteinfaerben = false;

	public boolean isNegativeWerteRoteinfaerben() {
		return isNegativeWerteRoteinfaerben;
	}

	public void setNegativeWerteRoteinfaerben(boolean isNegativeWerteRoteinfaerben) {
		this.isNegativeWerteRoteinfaerben = isNegativeWerteRoteinfaerben;
	}

	/**
	 * Im Handler wird festgelegt, wie breit die Spalten zur Anzeige sein
	 * sollen. <br>
	 * Der hier uebergebene Wert wird mit der durchschnittlichen Breite eines
	 * Zeichens multipliziert.
	 */
	private int[] columnHeaderWidth = null;

	/**
	 * creates a new instance of TableInfo with all attributes set.
	 * 
	 * @param columnClasses
	 *            the columns' data types.
	 * @param columnHeaderValues
	 *            the columns' header values used to determine the columns'
	 *            names.
	 * @param dataBaseColumnNames
	 *            the columns' names as they exist in the data base (expressed
	 *            in HQL).
	 */
	public TableInfo(Class[] columnClasses, Object[] columnHeaderValues,
			String[] dataBaseColumnNames) {
		this.columnClasses = columnClasses;
		this.columnHeaderValues = columnHeaderValues;
		this.dataBaseColumnNames = dataBaseColumnNames;
	}

	public TableInfo(Class[] columnClasses, Object[] columnHeaderValues,
			int[] columnHeaderWidth, String[] dataBaseColumnNames) {
		this.columnClasses = columnClasses;
		this.columnHeaderValues = columnHeaderValues;
		this.columnHeaderWidth = columnHeaderWidth;
		this.dataBaseColumnNames = dataBaseColumnNames;
	}

	/**
	 * @return Returns the columnClasses.
	 */
	public Class[] getColumnClasses() {
		return columnClasses;
	}

	/**
	 * @return Returns the columnHeaderValues.
	 */
	public Object[] getColumnHeaderValues() {
		return columnHeaderValues;
	}

	/**
	 * Die Spalten&uuml;berschriften k&ouml;nnen durch eine &UUml;bersetzung &uuml;berschrieben
	 * werden.
	 * 
	 * @param pValues
	 *            Object[]
	 */
	public void setColumnHeaderValues(Object[] pValues) {
		this.columnHeaderValues = pValues;
	}

	/**
	 * @return Returns the dataBaseColumnNames.
	 */
	public String[] getDataBaseColumnNames() {
		return dataBaseColumnNames;
	}

	public int[] getColumnHeaderWidths() {
		return columnHeaderWidth;
	}

	public int getColumnHeaderWidth() {
		int iW = 0;
		for (int i = 0; i < columnHeaderWidth.length; i++) {
			// -1 wird zu xxlarge->eher die "breiten" spalten zum schieben
			// nehmen
			iW += (columnHeaderWidth[i] == QueryParameters.FLR_BREITE_SHARE_WITH_REST ? QueryParameters.FLR_BREITE_XXL
					: columnHeaderWidth[i]);
		}
		return iW;
	}
}
