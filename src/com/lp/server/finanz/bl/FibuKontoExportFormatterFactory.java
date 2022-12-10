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
import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.util.EJBExceptionLP;

/**
 * <p>
 * Diese Klasse kuemmert sich um die Generierung eines KontoExportFormatters
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * 
 * <p>
 * Erstellung: Martin Bluehweis; 16.03.06
 * </p>
 * 
 * <p>
 * 
 * @author $Author: adi $
 *         </p>
 * 
 * @version not attributable Date $Date: 2009/11/12 16:58:33 $
 */
abstract class FibuKontoExportFormatterFactory {
	public static FibuKontoExportFormatter getFibuKontoExportFormatter(
			String sFormat) throws EJBExceptionLP {
		if (sFormat.equalsIgnoreCase(FibuExportFac.ZIEL_RZL)) {
			return new FibuKontoExportFormatterRZL();
		} else if (sFormat.equalsIgnoreCase(FibuExportFac.ZIEL_BMD)) {
			return new FibuKontoExportFormatterBMD();
		} else if (sFormat.equalsIgnoreCase(FibuExportFac.ZIEL_BMD_NTCS)) {
			return new FibuKontoExportFormatterBMD();
		} else if (sFormat.equalsIgnoreCase(FibuExportFac.ZIEL_ABACUS)) {
			return new FibuKontoExportFormatterAbacus();
		} else if (sFormat.equalsIgnoreCase(FibuExportFac.ZIEL_SCHLEUPEN)) {
			return new FibuKontoExportFormatterSchleupen();
		} else if (sFormat.equalsIgnoreCase(FibuExportFac.ZIEL_DATEV)) {
			return new FibuKontoExportFormatterDatev();
		} else if (sFormat.equalsIgnoreCase(FibuExportFac.ZIEL_DATEV_EXTF)) {
			return new FibuKontoExportFormatterDatev();
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET,
					new Exception("Format: " + sFormat));
		}
	}
	
	public static FibuKontoExportFormatter getFibuKontoExportFormatter(String sFormat, String kontotypCnr) {
		if (FibuExportFac.ZIEL_RZL.equals(sFormat)
				&& FinanzServiceFac.KONTOTYP_SACHKONTO.equals(kontotypCnr)) {
			return new FibuSachkontoExportFormatterRZL();
		}
		
		if ((FibuExportFac.ZIEL_DATEV.equalsIgnoreCase(sFormat)
					|| FibuExportFac.ZIEL_DATEV_EXTF.equalsIgnoreCase(sFormat))
				&& FinanzServiceFac.KONTOTYP_SACHKONTO.equals(kontotypCnr)) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET,
					new Exception("Format: " + sFormat + ", Kontotyp: " + kontotypCnr));
		}
		
		return getFibuKontoExportFormatter(sFormat);
	}
}
