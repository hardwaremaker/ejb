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
package com.lp.server.eingangsrechnung.bl;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungReportFac;
import com.lp.server.eingangsrechnung.service.ErZahlungsempfaenger;
import com.lp.server.eingangsrechnung.service.ZahlungsvorschlagDto;
import com.lp.server.eingangsrechnung.service.ZahlungsvorschlagExportException;
import com.lp.server.eingangsrechnung.service.ZahlungsvorschlagExportResult;
import com.lp.server.eingangsrechnung.service.ZahlungsvorschlaglaufDto;
import com.lp.server.finanz.service.BankverbindungDto;
import com.lp.server.partner.service.BankDto;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.EingangsrechnungId;
import com.lp.server.util.report.LpMailText;
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
 * @author $Author: christian $
 *         </p>
 * 
 * @version not attributable Date $Date: 2012/02/10 10:14:49 $
 */
public class ZahlungsvorschlagExportFormatterCSV extends
		ZahlungsvorschlagExportFormatter {

	private final static String BLZ_EMPFAENGER = "blzempfaenger";
	private final static String IBAN_EMPFAENGER = "ibanempfaenger";
	private final static String BIC_EMPFAENGER = "bicempfaenger";
	private final static String KONTO_EMPFAENGER = "kontoempfaenger";
	private final static String EMPFAENGERNAME1 = "empfaengername1";
	private final static String EMPFAENGERNAME2 = "empfaengername2";
	private final static String BETRAG = "betrag";
	private final static String WAEHRUNG = "waehrung";
	private final static String BLZ_AUFTRAGGEBER = "blzauftraggeber";
	private final static String IBAN_AUFTRAGGEBER = "ibanauftraggeber";
	private final static String BIC_AUFTRAGGEBER = "bicauftraggeber";
	private final static String KONTO_AUFTRAGGEBER = "kontoauftraggeber";
	private final static String WAEHRUNG_KONTO_AUFTRAGGEBER = "waehrungkontoauftraggeber";
	private final static String AUFTRAGGEBERNAME1 = "auftraggebername1";
	private final static String AUFTRAGGEBERNAME2 = "auftraggebername2";
	private final static String KURZVERWENDUNG = "kurzverwendung";
	private final static String KUNDENDATEN = "kundendaten";
	
	protected ZahlungsvorschlagExportFormatterCSV(TheClientDto theClientDto)
			throws EJBExceptionLP {
		super(theClientDto);
	}

	public ZahlungsvorschlagExportResult exportiereDaten(ZahlungsvorschlaglaufDto laufDto,
			ZahlungsvorschlagDto[] zahlungsvorschlagDtos,
			TheClientDto theClientDto) throws EJBExceptionLP {
		List<ZahlungsvorschlagExportException> exportErrors = 
				new ArrayList<ZahlungsvorschlagExportException>();

		// Ueberschrift mit exportieren
		String header = exportiereUeberschrift() + Helper.CR_LF;
		StringBuilder data = new StringBuilder();
		try {

			BankverbindungDto bvDto = getFinanzFac()
					.bankverbindungFindByPrimaryKey(
							laufDto.getBankverbindungIId());
			BankDto bankDto = getBankFac().bankFindByPrimaryKey(
					bvDto.getBankIId(), theClientDto);
			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(
					theClientDto.getMandant(), theClientDto);

			LpMailText mt = new LpMailText();
			for (int i = 0; i < zahlungsvorschlagDtos.length; i++) {
				// nur wenn bezahlen = true
				if (!Helper.short2Boolean(zahlungsvorschlagDtos[i].getBBezahlen())) {
					continue;
				}
				
				EingangsrechnungDto erDto = getEingangsrechnungFac()
						.eingangsrechnungFindByPrimaryKey(
								zahlungsvorschlagDtos[i]
										.getEingangsrechnungIId());

				ErZahlungsempfaenger erBv = getErBankverbindung(erDto, theClientDto, exportErrors);
				if (erBv == null) {
					continue;
				}
				// ich nehm die erste
				BankDto bankDtoLieferant = getBankFac()
						.bankFindByPrimaryKey(erBv.getPartnerbankDto().getBankPartnerIId(),
								theClientDto);
				// nur wann die Bank des LF in Oesterreich ist
				if (bankDtoLieferant.getPartnerDto().getLandplzortDto() == null) {
					exportErrors.add(new ZahlungsvorschlagExportException(erDto, erBv.getPartnerDto(), 
							EJBExceptionLP.FEHLER_FINANZ_ZVEXPORT_BANK_HAT_KEINEN_ORT,
							ZahlungsvorschlagExportException.SEVERITY_ERROR));
					continue;
				}
				// PJ 17200 alle SEPA-Teilnehmer
				if (!Helper.short2boolean(bankDtoLieferant.getPartnerDto()
						.getLandplzortDto().getLandDto().getBSepa())) {
					exportErrors.add(new ZahlungsvorschlagExportException(erDto, erBv.getPartnerDto(), 
							EJBExceptionLP.FEHLER_FINANZ_ZVEXPORT_BANK_AUS_NICHT_SEPA_LAND,
							ZahlungsvorschlagExportException.SEVERITY_WARNING));
					continue;
				}
				//------------------------------------------------------
				// ----------
				// Empfaenger BLZ
				//------------------------------------------------------
				// ----------

				boolean bIbanUndBicVorhanden = false;

				if (bankDtoLieferant.getCBic() != null
						&& erBv.getPartnerbankDto().getCIban() != null) {
					bIbanUndBicVorhanden = true;
				}

				if (bIbanUndBicVorhanden == true) {
					mt.addParameter(BLZ_EMPFAENGER, Helper.cutString(
							"", 7));
				} else {
					mt.addParameter(BLZ_EMPFAENGER, Helper.cutString(
							bankDtoLieferant.getCBlz(), 7));
					
				}
				mt.addParameter(BIC_EMPFAENGER, Helper.cutString(
						bankDtoLieferant.getCBic(), 11));

				//------------------------------------------------------
				// ----------
				// Empfaenger Konto
				//------------------------------------------------------
				// ----------
				mt.addParameter(IBAN_EMPFAENGER,
						cutStringRemoveLeerzeichen(erBv.getPartnerbankDto().getCIban(),
								40));

				if (bIbanUndBicVorhanden == true) {
					mt.addParameter(KONTO_EMPFAENGER, Helper.cutString(
							"", 11));
				} else {
					String sKontoEmpf = Helper
							.befreieNummerVonSonderzeichen(erBv.getPartnerbankDto().getCKtonr());
					mt.addParameter(KONTO_EMPFAENGER, Helper.cutString(
							sKontoEmpf, 11));
				}

				//------------------------------------------------------
				// ----------
				// Empfaenger name 1
				//------------------------------------------------------
				// ----------
				mt.addParameter(EMPFAENGERNAME1, Helper.cutString(
						erBv.getPartnerDto()
								.getCName1nachnamefirmazeile1(), 35));
				//------------------------------------------------------
				// ----------
				// Empfaenger name 2
				//------------------------------------------------------
				// ----------
				mt.addParameter(EMPFAENGERNAME2, Helper.cutString(erBv
						.getPartnerDto().getCName2vornamefirmazeile2(),
						35));
				//------------------------------------------------------
				// ----------
				// offener Betrag
				//------------------------------------------------------
				// ----------
				mt.addParameter(BETRAG, super
						.formatNumber(zahlungsvorschlagDtos[i]
								.getNZahlbetrag()));
				//------------------------------------------------------
				// ----------
				// Waehrung der ER
				//------------------------------------------------------
				// ----------
				mt.addParameter(WAEHRUNG, Helper.cutString(erDto
						.getWaehrungCNr(), 3));
				//------------------------------------------------------
				// ----------
				// eigene BLZ
				//------------------------------------------------------
				// ----------
				mt.addParameter(BLZ_AUFTRAGGEBER, Helper.cutString(
						bankDto.getCBlz(), 5));
				mt.addParameter(BIC_AUFTRAGGEBER, Helper.cutString(
						bankDto.getCBic(), 11));
				//------------------------------------------------------
				// ----------
				// Auftraggeber Konto
				//------------------------------------------------------
				// ----------
				mt.addParameter(IBAN_AUFTRAGGEBER,
								cutStringRemoveLeerzeichen(bvDto
										.getCIban(), 40));
				String sKontoAuft = Helper
						.befreieNummerVonSonderzeichen(bvDto
								.getCKontonummer());
				mt.addParameter(KONTO_AUFTRAGGEBER, Helper.cutString(
						sKontoAuft, 11));

				//------------------------------------------------------
				// ----------
				// Auftraggeber Waehrung ist die Mandantenwaehrung
				//------------------------------------------------------
				// ----------
				mt.addParameter(WAEHRUNG_KONTO_AUFTRAGGEBER,
						Helper.cutString(theClientDto
								.getSMandantenwaehrung(), 3));
				//------------------------------------------------------
				// ----------
				// Auftraggeber name 1
				//------------------------------------------------------
				// ----------
				mt.addParameter(AUFTRAGGEBERNAME1, Helper.cutString(
						mandantDto.getPartnerDto()
								.getCName1nachnamefirmazeile1(), 35));
				//------------------------------------------------------
				// ----------
				// Auftraggeber name 2
				//------------------------------------------------------
				// ----------
				mt.addParameter(AUFTRAGGEBERNAME2, Helper.cutString(
						mandantDto.getPartnerDto()
								.getCName2vornamefirmazeile2(), 35));
				//------------------------------------------------------
				// ----------
				// Kurzverwendung
				//------------------------------------------------------
				// ----------
				String sKurzverwendung;
				if (erDto.getCLieferantenrechnungsnummer() != null) {
					sKurzverwendung = erDto
							.getCLieferantenrechnungsnummer()
							+ "|";
				} else {
					sKurzverwendung = "";
				}
				sKurzverwendung = sKurzverwendung + erDto.getCNr();
				mt.addParameter(KURZVERWENDUNG, Helper.cutString(
						sKurzverwendung, 28));
				//------------------------------------------------------
				// ----------
				// Kundendaten
				//------------------------------------------------------
				// ----------
				String sKundendaten;
				if (erDto.getCKundendaten() != null) {
					sKundendaten = Helper
							.befreieNummerVonSonderzeichen(erDto
									.getCKundendaten());
				} else {
					sKundendaten = "";
				}
				mt.addParameter(KUNDENDATEN, Helper.cutString(
						sKundendaten, 12));
				//------------------------------------------------------
				// ----------
				String sZeile = mt.transformText(
						EingangsrechnungReportFac.REPORT_MODUL,
						theClientDto.getMandant(), getXSLFile(),
						theClientDto.getLocMandant(), theClientDto);
				data.append(sZeile + Helper.CR_LF);
				
			}
		} catch (RemoteException ex) {
			throwEJBExceptionLPRespectOld(ex);
		}
		
		if (data.toString().isEmpty()) {
			exportErrors.add(new ZahlungsvorschlagExportException(
					EJBExceptionLP.FEHLER_FINANZ_ZVEXPORT_KEINE_BELEGE_ZU_EXPORTIEREN, 
					ZahlungsvorschlagExportException.SEVERITY_ERROR));
			return new ZahlungsvorschlagExportResult(null, exportErrors);
		}
		
		StringBuilder allData = new StringBuilder();
		allData.append(header).append(data.toString());
		
		return new ZahlungsvorschlagExportResult(allData.toString(), exportErrors);
	}

	protected String exportiereUeberschrift() throws EJBExceptionLP {
		// wegen dem xsl-file muss ich das zeilenweise machen - als fuer jeden
		// datensatz
		LpMailText mt = new LpMailText();
		mt.addParameter(BLZ_EMPFAENGER, "BLZ_EMPFAENGER");
		mt.addParameter(BIC_EMPFAENGER, "BIC_EMPFAENGER");
		mt.addParameter(IBAN_EMPFAENGER, "IBAN_EMPFAENGER");
		mt.addParameter(KONTO_EMPFAENGER, "KONTO_EMPFAENGER");
		mt.addParameter(EMPFAENGERNAME1, "EMPFAENGERNAME1");
		mt.addParameter(EMPFAENGERNAME2, "EMPFAENGERNAME2");
		mt.addParameter(BETRAG, "BETRAG");
		mt.addParameter(WAEHRUNG, "WAEHRUNG");
		mt.addParameter(BLZ_AUFTRAGGEBER, "BLZ_AUFTRAGGEBER");
		mt.addParameter(BIC_AUFTRAGGEBER, "BIC_AUFTRAGGEBER");
		mt.addParameter(IBAN_AUFTRAGGEBER, "IBAN_AUFTRAGGEBER");
		mt.addParameter(KONTO_AUFTRAGGEBER, "KONTO_AUFTRAGGEBER");
		mt.addParameter(WAEHRUNG_KONTO_AUFTRAGGEBER,
				"WAEHRUNG_KONTO_AUFTRAGGEBER");
		mt.addParameter(AUFTRAGGEBERNAME1, "AUFTRAGGEBERNAME1");
		mt.addParameter(AUFTRAGGEBERNAME2, "AUFTRAGGEBERNAME2");
		mt.addParameter(KURZVERWENDUNG, "KURZVERWENDUNG");
		mt.addParameter(KUNDENDATEN, "KUNDENDATEN");
		String sZeile = mt.transformText(
				EingangsrechnungReportFac.REPORT_MODUL, theClientDto
						.getMandant(), getXSLFile(), theClientDto
						.getLocMandant(), theClientDto);
		return sZeile;
	}

	protected String getXSLFile() {
		return XSL_FILE_CSV;
	}

	protected ErZahlungsempfaenger getErBankverbindung(EingangsrechnungDto erDto, TheClientDto theClientDto, List<ZahlungsvorschlagExportException> exportErrors) {
		try {
			ErZahlungsempfaenger erBv = getEingangsrechnungFac().getErZahlungsempfaenger(
					new EingangsrechnungId(erDto.getIId()), theClientDto);
			if (erBv.isAbweichend() && !erBv.exists()) {
				exportErrors.add(new ZahlungsvorschlagExportException(erDto, erBv.getPartnerDto(), 
						EJBExceptionLP.FEHLER_FINANZ_ZVEXPORT_ER_MIT_ABWEICHENDER_BANKVERBINDUNG_HAT_KEINE,
						ZahlungsvorschlagExportException.SEVERITY_ERROR));
				return null;
			} else if (!erBv.exists()) {
				exportErrors.add(new ZahlungsvorschlagExportException(erDto, erBv.getPartnerDto(), 
						EJBExceptionLP.FEHLER_FINANZ_ZVEXPORT_LF_HAT_KEINE_BANKVERBINDUNG,
						ZahlungsvorschlagExportException.SEVERITY_ERROR));
				return null;
			}
			return erBv;
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
		
		return null;
	}
}
