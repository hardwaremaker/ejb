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
package com.lp.server.system.fastlanereader.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TableColumnInformation implements Serializable {
	private static final long serialVersionUID = -8381238204835159949L;
	
	public TableColumnInformation() {
		columnClasses = new ArrayList<Class>();
		headerNames = new ArrayList<Object>();
		widths = new ArrayList<Integer>();
		dbColumnNames = new ArrayList<String>();
		columnViewNamesMap = new HashMap<String, Integer>();
	}

	public void add(String columnViewName, Class columnClass,
			Object headerName, int width, String dbColumnName) {
		columnClasses.add(columnClass);
		headerNames.add(headerName);
		widths.add(width);
		dbColumnNames.add(dbColumnName);
		columnViewNamesMap.put(columnViewName, columnViewNamesMap.size());
	}

	public Class[] getClasses() {
		return columnClasses.toArray(new Class[0]);
	}

	public Object[] getHeaderNames() {
		return headerNames.toArray(new Object[0]);
	}

	public int[] getWidths() {
		int[] values = new int[widths.size()];
		for (int i = 0; i < values.length; i++) {
			values[i] = widths.get(i);
		}
		return values;
	}

	public String[] getDbColumNames() {
		return dbColumnNames.toArray(new String[0]);
	}

	public int getViewIndex(String columnViewName) {
		Integer index = columnViewNamesMap.get(columnViewName);
		if (null == index)
			throw new IllegalArgumentException(columnViewName
					+ " is not known");
		return index.intValue();
	}

	public boolean existsColumnName(String columnViewName) {
		return null != columnViewNamesMap.get(columnViewName) ;
	}
	
	public Integer getViewIndexObject(String columnViewName) {
		return columnViewNamesMap.get(columnViewName) ;
 	}
	
	private List<Class> columnClasses;
	private List<Object> headerNames;
	private List<Integer> widths;
	private List<String> dbColumnNames;
	private HashMap<String, Integer> columnViewNamesMap;
}
