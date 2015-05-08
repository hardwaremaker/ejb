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
 package com.lp.server.stueckliste.ejbfac;

import java.util.List;

public class ImportQueryBuilder {

	/**
	 * Setzt die FROM und WHERE Teile zu einem Query zusammen.
	 * Die Reihenfolge der Strings ist die Reihenfolge im
	 * zur&uuml;ckgegebenen String. Das bedeutet, dass das SELECT .. FROM im
	 * ersten String in <code>froms</code> enthalten sein muss.
	 * @param froms die Liste der Strings vor dem WHERE, inkl. SELECT und FROM
	 * @param wheres die Liste der Strings nach dem WHERE
	 * @return die Query
	 */
	public static String buildQuery(List<String> froms, List<String> wheres) {
		StringBuffer query = new StringBuffer();
		for(String from : froms) {
			if(from != null)
				query.append(from.trim() + " ");
		}
		query.append("WHERE ");
		boolean first = true;
		for(String where : wheres) {
			if(where == null) continue;
			if(!first)
				query.append(" AND ");
			else 
				first = false;
			query.append(where.trim());
		}
		return query.toString();
	}
}
