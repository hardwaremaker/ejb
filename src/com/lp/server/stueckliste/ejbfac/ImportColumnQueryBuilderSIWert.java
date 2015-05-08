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

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import com.lp.server.stueckliste.service.FertigungsStklImportSpezifikation;
import com.lp.server.util.HelperServer;
import com.lp.service.StklImportSpezifikation;
import com.lp.util.siprefixparser.DefaultSiPrefixParser;
import com.lp.util.siprefixparser.ISiPrefixParser;

public class ImportColumnQueryBuilderSIWert implements IImportColumnQueryBuilder {
	
	private static final String QUERY = "replace(" + ArtikelSpr + ".c_siwert, 'A','0')='{VALUE}'";
	
	private static final List<String> DEEPER_COLS =
			Arrays.asList(FertigungsStklImportSpezifikation.BEZEICHNUNG,
					FertigungsStklImportSpezifikation.BEZEICHNUNG1,
					FertigungsStklImportSpezifikation.BEZEICHNUNG2,
					FertigungsStklImportSpezifikation.BEZEICHNUNG3,
					FertigungsStklImportSpezifikation.BAUFORM);
	
	private ISiPrefixParser parser = new DefaultSiPrefixParser();

	@Override
	public boolean isTotalMatch() {
		return false;
	}
	
	protected void setParser(ISiPrefixParser parser) {
		this.parser = parser;
	}
	
	protected ISiPrefixParser getParser() {
		return parser;
	}

	@Override
	public List<String> getDeeperColumnQueryBuilders() {
		return DEEPER_COLS;
	}

	@Override
	public String buildJoinQuery() {
		return JOIN_ARTIKELSPR;
	}

	@Override
	public String buildWhereQuery(String columnValue,
			StklImportSpezifikation spez) {
		BigDecimal siWert1 = getParser().parseFirst(columnValue);
		BigDecimal siWert2 = getParser().parseFirst(columnValue.toLowerCase());

		String value1 = HelperServer.getDBValueFromBigDecimal(siWert1, 60);
		String value2 = HelperServer.getDBValueFromBigDecimal(siWert2, 60);
		
		if(value1 == null && value2 == null) return null;
		if(value1 != null) {
			value1 = QUERY.replaceAll("\\{VALUE\\}", value1.replace('A', '0'));
			if(value2 != null) value1 += " OR ";
		} else value1 = "";
		if(value2 != null)
			value2 = QUERY.replaceAll("\\{VALUE\\}", value2.replace('A', '0'));
		
		else value2 = "";
		
		return "(" + value1 + value2 + ")";
	}

}
