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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import com.lp.server.system.service.TheClientDto;
import com.lp.util.Helper;

public abstract class DatevExportHeaderFormatter extends DatevExportZeileFormatter {
	
	public static final String FORMAT_KZ = "FORMAT_KZ";
	public static final String VERSIONSNR = "VERSIONSNR";
	public static final String DATENKATEGORIE = "DATENKATEGORIE";
	public static final String FORMATNAME = "FORMATNAME";
	public static final String FORMATVERSION = "FORMATVERSION";
	public static final String ERZEUGTAM = "ERZEUGTAM";
	public static final String IMPORTIERT = "IMPORTIERT";
	public static final String HERKUNFT = "HERKUNFT";
	public static final String EXPORTIERTVON = "EXPORTIERTVON";
	public static final String IMPORTIERTVON = "IMPORTIERTVON";
	public static final String BERATER = "BERATER";
	public static final String MANDANT = "MANDANT";
	public static final String GJBEGINN = "GFJAHRBEGINN";
	public static final String SACHKONTOLAENGE = "SACHKONTOLAENGE";
	public static final String DATUMVON = "DATUMVON";
	public static final String DATUMBIS = "DATUMBIS";
	public static final String BEZEICHNUNG = "BEZEICHNUNG";
	public static final String DIKTATKUERZEL = "DIKTATKUERZEL";
	public static final String BUCHUNGSTYP = "BUCHUNGSTYP";
	public static final String RECHNUNGSLEGUNGSZWECK = "RECHNUNGSLEGUNGSZWECK";
	public static final String RESERVIERT21 = "RESERVIERT21";
	public static final String WKZ = "WKZ";
	public static final String RESERVIERT23 = "RESERVIERT23";
	public static final String RESERVIERT24 = "RESERVIERT24";
	public static final String RESERVIERT25 = "RESERVIERT25";
	public static final String RESERVIERT26 = "RESERVIERT26";

	public static final int DATENKATEGORIE_BUCHUNGSSTAPEL = 21;
	public static final String FORMATNAME_BUCHUNGSSTAPEL = "Buchungsstapel";
	
	
	public DatevExportHeaderFormatter(int datenkategorie, String formatname, int formatversion,
			TheClientDto theClientDto, String berater, String mandant, long gfjBeginn, int sachkontolaenge) {
		super(26);
		fields = new HashMap<String, ExportDatevField>();
		fields.put(FORMAT_KZ, new ExportDatevField(fieldCount++, true, 4, "EXTF"));
		fields.put(VERSIONSNR, new ExportDatevField(fieldCount++, true, 3, 300));
		fields.put(DATENKATEGORIE, new ExportDatevField(fieldCount++, true, 2, datenkategorie));
		fields.put(FORMATNAME, new ExportDatevField(fieldCount++, true, Integer.MAX_VALUE, formatname));
		fields.put(FORMATVERSION, new ExportDatevField(fieldCount++, true, Integer.MAX_VALUE, formatversion));
		
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		fields.put(ERZEUGTAM, new ExportDatevField(fieldCount++, false, false, 17,
				dateFormat.format(new Date(System.currentTimeMillis()))));
		fields.put(IMPORTIERT, new ExportDatevField(fieldCount++, false, false, 17));
		fields.put(HERKUNFT, new ExportDatevField(fieldCount++, false, 2, "HV"));
		String benutzername = theClientDto.getBenutzername();
		if (benutzername != null)
			benutzername = benutzername.trim().substring(0, benutzername.indexOf("|"));
		fields.put(EXPORTIERTVON, new ExportDatevField(fieldCount++, false, 25, benutzername));
		fields.put(IMPORTIERTVON, new ExportDatevField(fieldCount++, false, true, 10));
		fields.put(BERATER, new ExportDatevField(fieldCount++, true, 7, berater));
		fields.put(MANDANT, new ExportDatevField(fieldCount++, true, 5, mandant));
		fields.put(GJBEGINN, new ExportDatevField(fieldCount++, true, 8, Helper.formatJJJJMMTT(gfjBeginn)));
		fields.put(SACHKONTOLAENGE, new ExportDatevField(fieldCount++, true, 1, sachkontolaenge));
	}
	

}
