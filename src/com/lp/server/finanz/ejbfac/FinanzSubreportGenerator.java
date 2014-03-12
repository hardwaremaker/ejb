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

import java.util.List;

import com.lp.server.finanz.fastlanereader.generated.FLRFinanzBuchungDetail;
import com.lp.server.finanz.service.BuchenFac;
import com.lp.util.LPDatenSubreport;

public class FinanzSubreportGenerator {

	private final static int BELEGNUMMER = 0;
	private final static int BUCHUNGSTEXT = 1;
	private final static int BELEGDATUM = 2;
	private final static int BETRAG = 3;
	private final static int GEGENKONTO_NR = 4;
	private final static int GEGENKONTO_BEZ = 5;
	private final static int KOSTENSTELLE = 6;
	private final static int ANZAHL_SPALTEN = 7;

	private final static String F_BELEGNUMMER = "BELEGNUMMER";
	private final static String F_BUCHUNGSTEXT = "BUCHUNGSTEXT";
	private final static String F_BELEGDATUM = "BELEGDATUM";
	private final static String F_BETRAG = "BETRAG";
	private final static String F_GEGENKONTO_NR = "GEGENKONTO_NR";
	private final static String F_GEGENKONTO_BEZ = "GEGENKONTO_BEZ";
	private final static String F_KOSTENSTELLE = "KOSTENSTELLE";

	public static LPDatenSubreport createBuchungsdetailSubreport(
			List<FLRFinanzBuchungDetail> list, boolean habenIstNegativ) {
		Object[][] data = new Object[list.size()][ANZAHL_SPALTEN];

		int i = 0;
		for (FLRFinanzBuchungDetail detail : list) {
			data[i][BUCHUNGSTEXT] = detail.getFlrbuchung().getC_text();
			data[i][BELEGNUMMER] = detail.getFlrbuchung().getC_belegnummer();
			data[i][BELEGDATUM] = detail.getFlrbuchung().getD_buchungsdatum();
			data[i][BETRAG] = (habenIstNegativ ^ detail.getBuchungdetailart_c_nr().equals(BuchenFac.HabenBuchung)) ?
					detail.getN_betrag() : detail.getN_betrag().negate();
			if(detail.getFlrgegenkonto() != null) {
				data[i][GEGENKONTO_NR] = detail.getFlrgegenkonto().getC_nr();
				data[i][GEGENKONTO_BEZ] = detail.getFlrgegenkonto().getC_bez();
			} else {
				data[i][GEGENKONTO_NR] = null ;
				data[i][GEGENKONTO_BEZ] = null ;				
			}
			data[i][KOSTENSTELLE] = detail.getFlrbuchung().getFlrkostenstelle().getC_nr();
			i++;
		}
		return new LPDatenSubreport(data, new String[] { F_BELEGNUMMER,
				F_BUCHUNGSTEXT, F_BELEGDATUM, F_BETRAG, F_GEGENKONTO_NR, F_GEGENKONTO_BEZ, F_KOSTENSTELLE});

	}
}
