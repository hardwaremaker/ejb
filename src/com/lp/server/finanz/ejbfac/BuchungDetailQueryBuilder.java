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
package com.lp.server.finanz.ejbfac;


public class BuchungDetailQueryBuilder {
	
	private static final String NUR_OFFENE_BUCHUNGEN = " (SELECT SUM(CASE WHEN bd.buchungdetailart_c_nr LIKE 'HABEN%' THEN -bd.n_betrag ELSE bd.n_betrag END) " +
			"FROM FLRFinanzBuchungDetail bd " +
			"WHERE bd.konto_i_id = {ALIAS}.konto_i_id AND bd.flrbuchung.t_storniert IS NULL " +
			"AND bd.flrbuchung.geschaeftsjahr_i_geschaeftsjahr = buchungdetail.flrbuchung.geschaeftsjahr_i_geschaeftsjahr " +
			"AND ( {ALIAS}.flrbuchung.c_belegnummer = bd.flrbuchung.c_belegnummer " +
			"OR (bd.i_ausziffern IS NOT NULL AND bd.i_ausziffern = {ALIAS}.i_ausziffern))) != 0 ";
	
	
	private static final String NICHT_ZUORDENBARE_BUCHUNGEN = 
			" ({ALIASBUCHUNGDETAIL}.konto_i_id={ALIASKONTO}" +
			" AND {ALIASBUCHUNG}.t_storniert IS NULL" +
			" AND ({ALIASBUCHUNG}.b_autombuchungeb IS NULL" +
				" OR {ALIASBUCHUNG}.b_autombuchungeb = 0)" +
			" AND (SELECT COUNT(*)" +
							" FROM FLRFinanzBelegbuchung beleg LEFT OUTER JOIN beleg.flrbuchung AS flrbuchung" +
							" WHERE flrbuchung.i_id={ALIASBUCHUNG}.i_id) = 0) ";
			
	public static String buildNurOffeneBuchungDetails(String flrBuchungdetailName) {
		return NUR_OFFENE_BUCHUNGEN.replaceAll("\\{ALIAS\\}", flrBuchungdetailName);
	}
	
	public static String buildNichtZuordenbareVonKonto(String flrBuchungdetailName, String flrBuchungName, Integer kontoIId) {
		return NICHT_ZUORDENBARE_BUCHUNGEN.replaceAll("\\{ALIASBUCHUNGDETAIL\\}", flrBuchungdetailName)
				.replaceAll("\\{ALIASBUCHUNG\\}", flrBuchungName)
				.replaceAll("\\{ALIASKONTO\\}", kontoIId.toString());
	}
}
