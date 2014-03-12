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
package com.lp.server.finanz.bl.hvraw;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lp.server.finanz.fastlanereader.generated.FLRFinanzBuchungDetail;
import com.lp.server.finanz.service.BuchenFac;
import com.lp.server.finanz.service.FinanzFac;
import com.lp.server.finanz.service.IBuchungsjournalExportFormatter;
import com.lp.util.EJBExceptionLP;

public class BuchungsjournalExportHVRawFormatter implements
		IBuchungsjournalExportFormatter {
	
	private List<FLRFinanzBuchungDetail> buchungen;
	private Map<String,String> buchungsartKz = new HashMap<String, String>() {
		private static final long serialVersionUID = 7371113587086555801L;

		{
			put(FinanzFac.BUCHUNGSART_BANKBUCHUNG, "BK");
			put(FinanzFac.BUCHUNGSART_KASSENBUCHUNG, "KA");
			put(FinanzFac.BUCHUNGSART_EROEFFNUNG, "EB");
			put(FinanzFac.BUCHUNGSART_BUCHUNG, "BU");
			put(FinanzFac.BUCHUNGSART_UMBUCHUNG, "UB");
//			put(FinanzFac.BUCHUNGSART_SALDOVORTRAG, "SV");
		}
	};
	
	public BuchungsjournalExportHVRawFormatter(List<FLRFinanzBuchungDetail> buchungen) {
		this.buchungen = buchungen;
	}

	@Override
	public List<String> getExportLines() {
		List<String> output = new ArrayList<String>();
		output.add("Datum\tBuchung\tBelegnummer\tBuchungsart\tKonto\tKontonamen\tKontotyp\tSoll\tHaben\tStorniert");
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		DecimalFormat decimalFormat = new DecimalFormat();
		decimalFormat.setGroupingUsed(false);
		decimalFormat.setMaximumFractionDigits(2);
		decimalFormat.setMinimumIntegerDigits(1);
		for (FLRFinanzBuchungDetail detail : buchungen) {
			boolean haben = detail.getBuchungdetailart_c_nr().equals(BuchenFac.HabenBuchung);
			String buchungsart = buchungsartKz.get(detail.getFlrbuchung().getBuchungsart_c_nr());
			if(buchungsart == null) throw new EJBExceptionLP(new Exception("kein Kurzzeichen fuer Buchungsart "+ detail.getFlrbuchung().getBuchungsart_c_nr()));
			output.add(String.format("%s\t%d\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s",
					dateFormat.format(detail.getFlrbuchung().getD_buchungsdatum()),
					detail.getBuchung_i_id(),
					detail.getFlrbuchung().getC_belegnummer(),
					buchungsart,
					detail.getFlrkonto().getC_nr(),
					detail.getFlrkonto().getC_bez(),
					detail.getFlrkonto().getKontotyp_c_nr(),
					haben?"":decimalFormat.format(detail.getN_betrag()),
					haben?decimalFormat.format(detail.getN_betrag()):"",
					detail.getFlrbuchung().getT_storniert() == null ? "0":"1"
					));
		}
		return output;
	}

}
