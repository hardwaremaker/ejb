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
package com.lp.server.finanz.bl.datevexport;

public class ExportDatevField {

	private String fieldname;
	private boolean notNull;
	private boolean isText;
	// private boolean immuteable;
	private int length;
	private String value;
	private int index;

	public ExportDatevField(int index, boolean notNull, boolean isText,
			int length) {
		this(index, notNull, isText, length, null);
	}

	public ExportDatevField(int index, boolean notNull,
			int length, Integer value) {
		this(index, notNull, false, length, ""+value);
	}
	
	public ExportDatevField(int index, boolean notNull,
			int length, String value) {
		this(index, notNull, true, length, value);
	}
	
	public ExportDatevField(int index, boolean notNull, boolean isText,
			int length, String value) {
		this.notNull = notNull;
		this.isText = isText;
		this.length = length;
		this.value = value;
		// this.immuteable = immutable;
		this.index = index;
	}

	public void setValue(String value) {
		if(notNull && value == null)
			throw new IllegalArgumentException("Feld Nr. " + index + " darf nicht null sein");
		if(value != null && value.length() > length)
			throw new IllegalArgumentException("Wert '" + value + "' darf nur " + length + " Zeichen lang sein");
		// if(immuteable)
		// throw new IllegalAccessError("Feld mit Nr. " + index +
		// " darf nicht veraendert werden!");
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public String getFieldname() {
		return fieldname;
	}

	public boolean isNotNull() {
		return notNull;
	}

	public boolean isText() {
		return isText;
	}

	public int getLength() {
		return length;
	}

	public int getIndex() {
		return index;
	}
	
	public String formatValue() {
		if(value == null) return isText ? "\"\"" : "";
		return isText ? "\"" + value + "\"" : value;
	}
}
