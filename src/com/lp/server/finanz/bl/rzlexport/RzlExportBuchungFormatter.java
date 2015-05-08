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
 *******************************************************************************/
package com.lp.server.finanz.bl.rzlexport;

import java.text.DecimalFormat;

import com.lp.server.finanz.bl.datevexport.BuchungsjournalExportDatevBuchung;
import com.lp.server.finanz.bl.datevexport.DatevExportZeileFormatter;
import com.lp.server.finanz.bl.datevexport.ExportDatevField;
import com.lp.util.Helper;

public class RzlExportBuchungFormatter extends DatevExportZeileFormatter {

	public static final String UMSATZ = "Umsatz";
	public static final String SOLLHABEN = "S/H";
	public static final String WKZ_UMSATZ = "WKZ Umsatz";
	public static final String KURS = "Kurs";
	public static final String BASIS_UMSATZ = "Basis Umsatz";
	public static final String WKZ_BASIS_UMSATZ = "WKZ Basis Umsatz";
	public static final String KONTO = "Konto";
	public static final String GEGENKONTO = "Gegenkonto";
	public static final String BU_SCHLUESSEL = "BU";
	public static final String BELEGDATUM = "Belegdatum";
	public static final String BELEGFELD1 = "Belegfeld 1";
	public static final String BUCHUNGSTEXT = "Buchungstext";
	public static final String UID = "UID";
	public static final String EU_STEUERSATZ = "EU-Steuersatz";

	public RzlExportBuchungFormatter(BuchungsjournalExportDatevBuchung buchung) {
		super(41);
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		df.setMinimumFractionDigits(2);
		df.setGroupingUsed(false);
		fields.put(UMSATZ, 			new ExportDatevField(1, true, false, 13, df.format(buchung.getUmsatz())));
		fields.put(SOLLHABEN, 		new ExportDatevField(2, false, 1, buchung.isSoll()?"S":"H"));
		fields.put(WKZ_UMSATZ, 		new ExportDatevField(3, false, true, 3, null));
		fields.put(KURS, 			new ExportDatevField(4, false, false, 11, null));
		fields.put(BASIS_UMSATZ, 	new ExportDatevField(5, false, false, 1, null));
		fields.put(WKZ_BASIS_UMSATZ,new ExportDatevField(6, false, true, 1, null));
		fields.put(KONTO, 			new ExportDatevField(7, true, false, 9, buchung.getKonto()));
		fields.put(GEGENKONTO, 		new ExportDatevField(8, true, false, 9, buchung.getGegenkonto()));
		fields.put(BU_SCHLUESSEL,	new ExportDatevField(9, false, 2, buchung.getBuSchluessel()));
		fields.put(BELEGDATUM, 		new ExportDatevField(10, true, false, 4, Helper.formatTTMM(buchung.getBelegdatum().getTime())));
		fields.put(BELEGFELD1, 		new ExportDatevField(11, false, 12, buchung.getBeleg()));
		fields.put(BUCHUNGSTEXT, 	new ExportDatevField(14, false, 60, buchung.getBuchungstext()));
		fields.put(UID, 			new ExportDatevField(15, false, 15, buchung.getUid() == null ? null : buchung.getUid().replaceAll(" ", "")));
		fields.put(EU_STEUERSATZ, 	new ExportDatevField(40, false, false, 16, buchung.getEuSteuersatz() == null ? null : df.format(buchung.getEuSteuersatz())));
	}

}
