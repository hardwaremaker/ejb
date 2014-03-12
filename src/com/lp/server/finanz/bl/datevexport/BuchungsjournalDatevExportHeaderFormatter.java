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

import com.lp.server.system.service.TheClientDto;
import com.lp.util.Helper;

public class BuchungsjournalDatevExportHeaderFormatter extends DatevExportHeaderFormatter {

	public BuchungsjournalDatevExportHeaderFormatter(TheClientDto theClientDto, String berater, String mandant,
			long gfjBeginn, int sachkontolaenge, long datumVon, long datumBis, String bezeichnung, String waehrung) {
		super(DATENKATEGORIE_BUCHUNGSSTAPEL, FORMATNAME_BUCHUNGSSTAPEL, 2, theClientDto, berater, mandant, gfjBeginn, sachkontolaenge);
		fields.put(DATUMVON, new ExportDatevField(fieldCount++, true, 8, Helper.formatJJJJMMTT(datumVon)));
		fields.put(DATUMBIS, new ExportDatevField(fieldCount++, true, 8, Helper.formatJJJJMMTT(datumBis)));
		fields.put(BEZEICHNUNG, new ExportDatevField(fieldCount++, false, 30, bezeichnung));
		fields.put(DIKTATKUERZEL, new ExportDatevField(fieldCount++, false, true, 2));
		fields.put(BUCHUNGSTYP, new ExportDatevField(fieldCount++, false, true, 1));
		fields.put(RECHNUNGSLEGUNGSZWECK, new ExportDatevField(fieldCount++, false, true, 2));
		fields.put(RESERVIERT21, new ExportDatevField(fieldCount++, false, false, 0));
		fields.put(WKZ, new ExportDatevField(fieldCount++, false, 3, waehrung));
		fields.put(RESERVIERT23, new ExportDatevField(fieldCount++, false, false, 0));
		fields.put(RESERVIERT24, new ExportDatevField(fieldCount++, false, false, 0));
		fields.put(RESERVIERT25, new ExportDatevField(fieldCount++, false, false, 0));
		fields.put(RESERVIERT26, new ExportDatevField(fieldCount++, false, false, 0));
	}
}
