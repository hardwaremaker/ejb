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
package com.lp.server.finanz.bl;

import com.lp.server.finanz.service.FibuExportFac;
import com.lp.server.finanz.service.FibuExportKriterienDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

/**
 * <p>
 * Diese Klasse kuemmert sich um die Generierung eines ExportFormatters
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * 
 * <p>
 * Erstellung: Martin Bluehweis; 09.02.06
 * </p>
 * 
 * <p>
 * 
 * @author $Author: adi $
 *         </p>
 * 
 * @version not attributable Date $Date: 2009/11/12 16:58:33 $
 */
abstract class FibuExportFormatterFactory {
	public static FibuExportFormatter getFibuExportFormatter(String sFormat,
			FibuExportKriterienDto exportKriterienDto, TheClientDto theClientDto)
			throws EJBExceptionLP {
		FibuExportFormatter exportFormatter;
		if (sFormat.equalsIgnoreCase(FibuExportFac.ZIEL_RZL)) {
			exportFormatter = new FibuExportFormatterRZL(exportKriterienDto,
					theClientDto);
		} else if (sFormat.equalsIgnoreCase(FibuExportFac.ZIEL_BMD)) {
			exportFormatter = new FibuExportFormatterBMD(exportKriterienDto,
					theClientDto);
		} else if (sFormat.equalsIgnoreCase(FibuExportFac.ZIEL_ABACUS)) {
			exportFormatter = new FibuExportFormatterAbacus(exportKriterienDto,
					theClientDto);
		} else if(sFormat.equalsIgnoreCase(FibuExportFac.ZIEL_SCHLEUPEN)){
			exportFormatter = new FibuExportFormatterSchleupen(exportKriterienDto,theClientDto);
		} else if(sFormat.equalsIgnoreCase(FibuExportFac.ZIEL_DATEV)){
			exportFormatter = new FibuExportFormatterDatev(exportKriterienDto,theClientDto);
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET,
					new Exception(
							"Format: "
									+ sFormat));
		}
		return exportFormatter;
	}
}
