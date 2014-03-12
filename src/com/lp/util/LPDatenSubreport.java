/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2014 HELIUM V IT-Solutions GmbH
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
package com.lp.util;

import java.io.Serializable;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 * <p>
 * <b>frame</b><br/> HelperReport Funktionen und Konstanten, die client- und
 * serverseitig verwendbar sind. <br/> Wenn hier etwas ge&auml;ndert wird, hat das
 * Auswirkungen auf bestimmte Reports.
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004
 * </p>
 * <p>
 * Erstellungsdatum 2007-08-21
 * </p>
 * <br/>
 * 
 * @author ck
 * @version 1.0
 */

public class LPDatenSubreport implements
		net.sf.jasperreports.engine.JRDataSource, Cloneable, Serializable {

	private Object[][] data = null;
	private int index = -1;
	private String[] fieldnames = null;

	public Object getFieldValue(JRField field) throws JRException {
		String fieldName = field.getName();
		Object value = null;
		for (int i = 0; i < fieldnames.length; i++) {
			if (fieldName.endsWith(fieldnames[i])) {
				value = data[index][i];
			}
		}
		return value;
	}

	public boolean next() throws JRException {
		index++;
		
		if(index >= data.length){
			index=-1;
			return false;
		} else {
			return true;
		}
		
		

	}

	public LPDatenSubreport(Object[][] data, String[] fieldnames) {
		this.data = data;
		this.fieldnames = fieldnames;
	}

	public Object clone() {
		LPDatenSubreport o = null;
		try {
			o = (LPDatenSubreport) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		// Referenzen clonen
		o.data = (Object[][]) this.data.clone();
		o.fieldnames = (String[]) this.fieldnames.clone();
		return o;
	}

}
