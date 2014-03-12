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
package com.lp.server.finanz.bl.datevexport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class DatevExportZeileFormatter {

	protected Map<String, ExportDatevField> fields = new HashMap<String, ExportDatevField>();
	protected int fieldCount = 1;
	protected int anzahlFelder = 26;
	
	public DatevExportZeileFormatter(int anzahlFelder) {
		this.anzahlFelder = anzahlFelder;
	}
	
	public String formatFieldNames(boolean useApostroph) {
		List<Map.Entry<String, ExportDatevField>> entries = new ArrayList<Map.Entry<String,ExportDatevField>>(fields.entrySet());
		Collections.sort(entries, new Comparator<Map.Entry<String, ExportDatevField>>() {
			FieldComparator c = new FieldComparator();
			@Override
			public int compare(Entry<String, ExportDatevField> e1,
					Entry<String, ExportDatevField> e2) {
				return c.compare(e1.getValue(), e2.getValue());
			}
			
		});
		StringBuffer sb = new StringBuffer();
		int i = 1;
		for (Map.Entry<String, ExportDatevField> entry : entries) {
			while(entry.getValue().getIndex() > i) {
				sb.append(";");
				i++;
			}
			if(useApostroph) sb.append("\"");
			sb.append(entry.getKey());
			if(useApostroph) sb.append("\"");
		}
		return sb.toString();
	}
	
	public String format() {
		List<ExportDatevField> list = new ArrayList<ExportDatevField>(fields.values());
		Collections.sort(list, new FieldComparator());
		
		StringBuffer bf = new StringBuffer();
		int i = 1;
		for (ExportDatevField field : list) {
			while(field.getIndex() > i) {
				bf.append(";");
				i++;
			}
			bf.append(field.formatValue());
		}
		
		//die anzahl der felder muss immer gleich sein
		for(; i < anzahlFelder; i++) {
			bf.append(";");
		}
		return bf.toString();
	}
	
	private class FieldComparator implements Comparator<ExportDatevField> {
		@Override
		public int compare(ExportDatevField o1,
				ExportDatevField o2) {
			return new Integer(o1.getIndex()).compareTo(new Integer(o2.getIndex()));
		}
	}
}
