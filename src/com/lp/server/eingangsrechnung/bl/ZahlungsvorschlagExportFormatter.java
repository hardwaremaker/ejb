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
package com.lp.server.eingangsrechnung.bl;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.eingangsrechnung.service.ZahlungsvorschlagDto;
import com.lp.server.eingangsrechnung.service.ZahlungsvorschlaglaufDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/**
 * <p>
 * Diese Klasse kuemmert sich um den ZV Export
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur GmbH (c) 2004-2008
 * </p>
 * 
 * <p>
 * Erstellung: Martin Bluehweis; 07.02.07
 * </p>
 * 
 * <p>
 * 
 * @author $Author: sebastian $
 *         </p>
 * 
 * @version not attributable Date $Date: 2009/01/12 17:38:32 $
 */
public abstract class ZahlungsvorschlagExportFormatter extends Facade {
	protected final static String XSL_FILE_CSV = "zv_export_csv";

	protected final TheClientDto theClientDto;

	protected ZahlungsvorschlagExportFormatter(TheClientDto theClientDto)
			throws EJBExceptionLP {
		this.theClientDto = theClientDto;
	}

	public abstract String exportiereDaten(ZahlungsvorschlaglaufDto laufDto,
			ZahlungsvorschlagDto[] zahlungsvorschlagDtos, TheClientDto theClientDto)
			throws EJBExceptionLP;

	protected abstract String getXSLFile();

	protected abstract String exportiereUeberschrift() throws EJBExceptionLP;

	protected ArrayList<Object> getAllInfoForTheClient(PartnerDto partnerDto) {
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(partnerDto.formatFixTitelName1Name2());
		list.add(partnerDto.formatAdresse());
		return list;
	}

	protected ArrayList<Object> getAllInfoForTheClient(EingangsrechnungDto erDto) {
		ArrayList<Object> list = new ArrayList<Object>();
		String sBelegartBelegnummer = getTextRespectUISpr(
				"er.eingangsrechnungsnummer", theClientDto.getMandant(),
				theClientDto.getLocUi())
				+ " " + erDto.getCNr();
		list.add(sBelegartBelegnummer);
		return list;
	}

	protected String formatNumber(Number n) {
		if (n != null) {
			NumberFormat nf = new DecimalFormat("#########0.00");
			return nf.format(n.doubleValue());
		} else {
			return "";
		}
	}

	/**
	 * Einen String von Leerzeichen befreien und auf eine Maximallaenge
	 * abschneiden.
	 * 
	 * @param s
	 *            String
	 * @param length
	 *            int
	 * @return String
	 * 
	 * @todo Methode in den Helper verschieben.
	 */
	protected String cutStringRemoveLeerzeichen(String s, int length) {
		String sOhneLeerzeichen = null;
		if (s != null) {
			sOhneLeerzeichen = s.replace(' ', '_');
		}
		return Helper.cutString(sOhneLeerzeichen, length);
	}
}
