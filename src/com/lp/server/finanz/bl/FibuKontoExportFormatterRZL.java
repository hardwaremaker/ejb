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
package com.lp.server.finanz.bl;

import java.util.ArrayList;

import com.lp.server.finanz.service.FibuKontoExportDto;
import com.lp.server.finanz.service.FinanzReportFac;
import com.lp.server.finanz.service.FinanzServiceFac;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.report.LpMailText;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

/**
 * <p>
 * Diese Klasse kuemmert sich um den Export von Personenkonten in RZL
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
 * @author $Author: heidi $
 *         </p>
 * 
 * @version not attributable Date $Date: 2009/01/13 12:59:32 $
 */
class FibuKontoExportFormatterRZL extends FibuKontoExportFormatter {
	private final static String P_KONTONUMMER = "Kontonummer";
	private final static String P_KONTENART = "Kontenart";
	private final static String P_FREMDWAEHRUNG = "Fremdwaehrung";
	private final static String P_UID_NUMMER = "UID-Nummer";
	private final static String P_ANREDE = "Anrede";
	private final static String P_NAME = "Name";
	private final static String P_STRASSE = "Strasse";
	private final static String P_ORT = "Ort";
	private final static String P_MAHNSPERRE = "Mahnsperre";
	private final static String P_MAHNFRIST1 = "Mahnfrist1";
	private final static String P_MAHNFRIST2 = "Mahnfrist2";
	private final static String P_MAHNFRIST3 = "Mahnfrist3";
	private final static String P_LAND = "Land";
	private final static String P_PLZ = "PLZ";

	private final static String KONTENART_ANLAGEKONTO = "1";
	private final static String KONTENART_BESTANDSKONTO = "2";
	private final static String KONTENART_AUFWANDSKONTO = "3";
	private final static String KONTENART_ERTRAGSKONTO = "4";
	private final static String KONTENART_DEBITOR_I = "5";
	private final static String KONTENART_DEBITOR_II = "6";
	private final static String KONTENART_KREDITOR_I = "7";
	private final static String KONTENART_KREDITOR_II = "8";
	private final static String KONTENART_DEBITOR_III = "9";
	private final static String KONTENART_DEBITOR_IV = "10";
	private final static String KONTENART_KREDITOR_III = "11";
	private final static String KONTENART_KREDITOR_IV = "12";

	protected String exportiereDaten(FibuKontoExportDto[] fibuExportDtos,
			TheClientDto theClientDto) throws EJBExceptionLP {
		// try {
		StringBuffer sb = new StringBuffer();

		// MB 15.02.07: lt WH exportieren wir die mahnfristen nicht, damit das
		// von RZL aus gesteuert werden kann
		// // Mahnstufen holen
		// MahnstufeDto mahnstufe1 =
		// getMahnwesenFac().mahnstufeFindByPrimaryKey(new
		// MahnstufePK(new Integer(FinanzServiceFac.MAHNSTUFE_1),
		// theClientDto.getMandant()));
		// MahnstufeDto mahnstufe2 =
		// getMahnwesenFac().mahnstufeFindByPrimaryKey(new
		// MahnstufePK(new Integer(FinanzServiceFac.MAHNSTUFE_2),
		// theClientDto.getMandant()));
		// MahnstufeDto mahnstufe3 =
		// getMahnwesenFac().mahnstufeFindByPrimaryKey(new
		// MahnstufePK(new Integer(FinanzServiceFac.MAHNSTUFE_3),
		// theClientDto.getMandant()));
		// nun die Daten zusammenbauen
		for (int i = 0; i < fibuExportDtos.length; i++) {
			KontoDto kontoDto = fibuExportDtos[i].getKontoDto();
			// // fuer Debitorenkonten werden die Mahndaten benoetigt
			// // Wenn die Mahnstufen nicht eingetragen sind -> Exception
			// if
			//(kontoDto.getKontotypCNr().equals(FinanzServiceFac.KONTOTYP_DEBITOR
			// ) &&
			// (mahnstufe1 == null || mahnstufe2 == null || mahnstufe3 == null))
			// {
			// throw new EJBExceptionLP(EJBExceptionLP.
			// FEHLER_FINANZ_KEINE_MAHNSTUFEN_EINGETRAGEN,
			// new Exception("FEHLER_FINANZ_KEINE_MAHNSTUFEN_EINGETRAGEN"));
			// }
			PartnerDto partnerDto = fibuExportDtos[i].getPartnerDto();
			LpMailText mt = new LpMailText();
			// Die Kontonummer darf laut Exportbeschreibung max. 6 Stellen haben
			if (kontoDto.getCNr().length() > 6) {
				EJBExceptionLP e = new EJBExceptionLP(
						EJBExceptionLP.FEHLER_FINANZ_EXPORT_RZL_KONTONUMMER_MAXIMAL_6_STELLEN,
						new Exception("Konto " + kontoDto.getCNr()
								+ " hat mehr als 6 Stellen"));
				ArrayList<Object> a = new ArrayList<Object>();
				a.add(kontoDto.getCNr() + " " + kontoDto.getCBez());
				e.setAlInfoForTheClient(a);
				throw e;
			}
			mt.addParameter(P_KONTONUMMER, kontoDto.getCNr());
			// Kontenart
			String sKontenart = null;
			if (kontoDto.getKontotypCNr().equals(
					FinanzServiceFac.KONTOTYP_DEBITOR)) {
				sKontenart = KONTENART_DEBITOR_I;
			} else if (kontoDto.getKontotypCNr().equals(
					FinanzServiceFac.KONTOTYP_KREDITOR)) {
				sKontenart = KONTENART_KREDITOR_I;
			} else if (kontoDto.getKontotypCNr().equals(
					FinanzServiceFac.KONTOTYP_SACHKONTO)) {
				sKontenart = KONTENART_ANLAGEKONTO;
			}
			mt.addParameter(P_KONTENART, sKontenart);
			// keine Fremdwaehrungskonten
			mt.addParameter(P_FREMDWAEHRUNG, "");
			// UID - Nummer wenn vorhanden
			String sUID;
			if (partnerDto != null && partnerDto.getCUid() != null) {
				sUID = partnerDto.getCUid().toUpperCase().replaceAll(" ", "");
			} else {
				sUID = "";
			}
			mt.addParameter(P_UID_NUMMER, sUID);
			/**
			 * @todo Anredenummer aus RZL PJ 4280
			 */
			mt.addParameter(P_ANREDE, "");
			// Name wenn vorhanden
			// wird von RZL auf maximal 40 zeichen abgeschnitten
			String sName;
			if (partnerDto != null
					&& partnerDto.getCName1nachnamefirmazeile1() != null) {
				sName = partnerDto.getCName1nachnamefirmazeile1().replace(';',
						' ');
			} else {
				sName = "";
			}
			mt.addParameter(P_NAME, sName);
			// Strasse wenn vorhanden
			// wird von RZL auf maximal 25 zeichen abgeschnitten
			String sStrasse;
			if (partnerDto != null && partnerDto.getCStrasse() != null) {
				sStrasse = partnerDto.getCStrasse().replace(';', ' ');
			} else {
				sStrasse = "";
			}
			mt.addParameter(P_STRASSE, sStrasse);
			// Ort wenn vorhanden
			// wird von RZL auf maximal 25 zeichen abgeschnitten
			String sOrt;
			if (partnerDto != null
					&& partnerDto.getLandplzortDto() != null
					&& partnerDto.getLandplzortDto().getOrtDto() != null
					&& partnerDto.getLandplzortDto().getOrtDto().getCName() != null) {
				sOrt = partnerDto.getLandplzortDto().getOrtDto().getCName()
						.replace(';', ' ');
			} else {
				sOrt = "";
			}
			mt.addParameter(P_ORT, sOrt);

			mt.addParameter(P_MAHNSPERRE, "");
			mt.addParameter(P_MAHNFRIST1, "");
			mt.addParameter(P_MAHNFRIST2, "");
			mt.addParameter(P_MAHNFRIST3, "");

			// // Mahnfristen/Mahnsperren nur bei Debitoren eingeben
			// if
			//(kontoDto.getKontotypCNr().equals(FinanzServiceFac.KONTOTYP_DEBITOR
			// )) {
			// /**
			// * @todo Mahnsperre PJ 4288
			// */
			// // vorlaeufig keine Mahnsperre
			// mt.addParameter(P_MAHNSPERRE, "");
			// // Mahntage der Mahnstufen (nur wenn es einen Kunden/Lieferanten
			// gibt)
			// if (partnerDto != null) {
			// mt.addParameter(P_MAHNFRIST1, mahnstufe1.getITage().toString());
			// mt.addParameter(P_MAHNFRIST2, mahnstufe2.getITage().toString());
			// mt.addParameter(P_MAHNFRIST3, mahnstufe3.getITage().toString());
			// }
			// else {
			// mt.addParameter(P_MAHNFRIST1, "");
			// mt.addParameter(P_MAHNFRIST2, "");
			// mt.addParameter(P_MAHNFRIST3, "");
			// }
			// }
			// else {
			// mt.addParameter(P_MAHNSPERRE, "");
			// mt.addParameter(P_MAHNFRIST1, "");
			// mt.addParameter(P_MAHNFRIST2, "");
			// mt.addParameter(P_MAHNFRIST3, "");
			// }
			// LKZ und PLZ wenn vorhanden
			String sLKZ;
			String sPLZ;
			if (partnerDto != null && partnerDto.getLandplzortDto() != null) {
				sLKZ = partnerDto.getLandplzortDto().getLandDto().getCLkz();
				sPLZ = partnerDto.getLandplzortDto().getCPlz();
			} else {
				sLKZ = "";
				sPLZ = "";
			}
			mt.addParameter(P_LAND, sLKZ);
			mt.addParameter(P_PLZ, sPLZ);
			String sZeile = mt.transformText(FinanzReportFac.REPORT_MODUL,
					theClientDto.getMandant(), getXSLFile(), theClientDto
							.getLocMandant(), theClientDto);
			sb.append(sZeile + Helper.CR_LF);
		}
		return sb.toString();
		// }
		// catch (RemoteException ex) {
		// throwEJBExceptionLPRespectOld(ex);
		// return null;
		// }
	}

	protected String getXSLFile() {
		return XSL_FILE_RZL;
	}

	protected String exportiereUebberschrift(TheClientDto theClientDto)
			throws EJBExceptionLP {
		// wegen dem xsl-file muss ich das zeilenweise machen - als fuer jeden
		// datensatz
		LpMailText mt = new LpMailText();
		mt.addParameter(P_KONTONUMMER, P_KONTONUMMER);
		mt.addParameter(P_KONTENART, P_KONTENART);
		mt.addParameter(P_FREMDWAEHRUNG, P_FREMDWAEHRUNG);
		mt.addParameter(P_UID_NUMMER, P_UID_NUMMER);
		mt.addParameter(P_ANREDE, P_ANREDE);
		mt.addParameter(P_NAME, P_NAME);
		mt.addParameter(P_STRASSE, P_STRASSE);
		mt.addParameter(P_ORT, P_ORT);
		mt.addParameter(P_MAHNSPERRE, P_MAHNSPERRE);
		mt.addParameter(P_MAHNFRIST1, P_MAHNFRIST1);
		mt.addParameter(P_MAHNFRIST2, P_MAHNFRIST2);
		mt.addParameter(P_MAHNFRIST3, P_MAHNFRIST3);
		mt.addParameter(P_LAND, P_LAND);
		mt.addParameter(P_PLZ, P_PLZ);
		String sZeile = mt.transformText(FinanzReportFac.REPORT_MODUL,
				theClientDto.getMandant(), getXSLFile(), theClientDto
						.getLocMandant(), theClientDto);
		return sZeile;
	}
}
