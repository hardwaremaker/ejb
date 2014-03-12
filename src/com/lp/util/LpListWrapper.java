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

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Wrapper f&uuml;r java.util.list zum csv-export
 * 
 * @author Adi
 *
 */
public class LpListWrapper {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7953012767255073213L;
	private List<?> list;
	
	public LpListWrapper() {
		super();
	}

	public LpListWrapper(List<?> list) {
		this.list = list;
	}

	public LpListWrapper(String csvPos, String fieldSep, String rowSep) {
		String[] s = csvPos.split(rowSep);
		if (s.length > 0) {
			String[] ss = s[0].split(fieldSep, -1);
			String[][] sss = new String[s.length][ss.length];
			for (int i=0; i < s.length; i++) {
				ss = s[i].split(";", -1);
				for (int j=0; j < ss.length; j++) {
					sss[i][j] = ss[j];
				}
			}
			this.list = Arrays.asList(sss);
		}
	}

	public LpListWrapper(String csvPos) {
		// Webservice schluckt die \r bei \r\n !!
		// daher entfernen, damit sonst auch kompatibel
		this(csvPos.replace("\r\n", "\n"), ";", "\n");
	}

	public String toCSV() {
		return toCSV(";", "\r\n");
	}
	
	public List<?> getList() {
		return this.list;
	}
	
	public String toCSV(String fieldSep, String rowSep) {
		StringBuffer s = new StringBuffer();
		Iterator<?> it = list.iterator();
		while (it.hasNext()) {
			Object[] o = (Object[]) it.next();
			for (int i=0; i < o.length; i++) {
				s.append(o[i]);
				if (i < o.length-1) s.append(fieldSep);
			}
			s.append(rowSep);
		}
		return new String(s);
	}
	
}
