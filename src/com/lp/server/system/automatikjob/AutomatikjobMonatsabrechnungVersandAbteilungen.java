
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
package com.lp.server.system.automatikjob;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedHashMap;

import com.lp.server.personal.service.MonatsabrechnungEmailVersand;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.personal.service.ZeiterfassungFac;
import com.lp.server.personal.service.ZeiterfassungFacAll;
import com.lp.server.system.service.AutoMonatsabrechnungversandDto;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.VersandauftragDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.Helper;

import net.sf.jasperreports.engine.JasperExportManager;

public class AutomatikjobMonatsabrechnungVersandAbteilungen extends AutomatikjobBasis {

	private AutoMonatsabrechnungversandDto autoMonatsabrechnungversandDto;
	private boolean errorInJob;

	Integer iWochentag = null;
	Integer bMonatlich = null;

	public AutomatikjobMonatsabrechnungVersandAbteilungen() {
		super();
	}

	/**
	 * performJob
	 * 
	 * @todo Diese com.lp.server.system.automatikjob.AutomatikjobBasis-Methode
	 *       implementieren
	 */
	public boolean performJob(TheClientDto theClientDto) {
		myLogger.info("start MonatsabrechnungVersandAbteilungen");
		errorInJob = false;

		try {
			autoMonatsabrechnungversandDto = getAutoMonatsabrechnungversandFac()
					.autoMonatsabrechnungversandAbteilungenfindByMandantCNr(theClientDto.getMandant());
		} catch (Throwable t) {
			myLogger.error("fehler beim holen der Parameter");
			errorInJob = true;
		}
		iWochentag = autoMonatsabrechnungversandDto.getIWochentag();
		bMonatlich = autoMonatsabrechnungversandDto.getBMonatlich();

		Calendar cHeute = Calendar.getInstance();

		boolean bVersenden = false;

		Integer personalIIdVersender = theClientDto.getIDPersonal();

		PersonalDto personalDtoVersender = getPersonalFac().personalFindByPrimaryKey(personalIIdVersender,
				theClientDto);
		String absender = personalDtoVersender.getCEmail();

		Calendar cGestern = Calendar.getInstance();
		cGestern.add(Calendar.DAY_OF_MONTH, -1);

		if (absender != null) {

			ArrayList<MonatsabrechnungEmailVersand> infoAlle = null;

			if (bMonatlich != null && bMonatlich == 1) {

				// Jeweils am ersten des Monats versenden
				if (cHeute.get(Calendar.DAY_OF_MONTH) == 1) {
					bVersenden = true;

					JasperPrintLP jasperPrint = getZeiterfassungFac().printMonatsAbrechnung(null,
							cGestern.get(Calendar.YEAR), cGestern.get(Calendar.MONTH), true, null, theClientDto,
							ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_ALLE_PERSONEN, null,
							ZeiterfassungFacAll.REPORT_MONATSABRECHNUNG_OPTION_SORTIERUNG_PERSONALNUMMER, null, true);

					infoAlle = (ArrayList<MonatsabrechnungEmailVersand>) jasperPrint
							.getAdditionalInformation(ZeiterfassungFac.MONATSABRECHNUNG_INFO_EMAIL_VERSAND);

				}

			} else if (bMonatlich != null && bMonatlich == 0 && iWochentag != null) {

				if (cHeute.get(Calendar.DAY_OF_WEEK) == iWochentag) {

					bVersenden = true;

					JasperPrintLP jasperPrint = getZeiterfassungFac().printMonatsAbrechnung(null,
							cGestern.get(Calendar.YEAR), cGestern.get(Calendar.MONTH), false,
							new java.sql.Date(cGestern.getTimeInMillis()), theClientDto,
							ZeiterfassungFac.REPORT_SONDERZEITENLISTE_OPTION_ALLE_PERSONEN, null,
							ZeiterfassungFacAll.REPORT_MONATSABRECHNUNG_OPTION_SORTIERUNG_PERSONALNUMMER, null, true);

					infoAlle = (ArrayList<MonatsabrechnungEmailVersand>) jasperPrint
							.getAdditionalInformation(ZeiterfassungFac.MONATSABRECHNUNG_INFO_EMAIL_VERSAND);
				}

			}

			if (bVersenden && infoAlle != null) {
				if (infoAlle != null) {

					String meldung = "";

					LinkedHashMap<Integer, JasperPrintLP> lhmGruppiertNachNabteilungen = new LinkedHashMap<Integer, JasperPrintLP>();

					for (int i = 0; i < infoAlle.size(); i++) {

						// Nach Kostenstellen/Abteilungen Gruppieren

						MonatsabrechnungEmailVersand infoZeile = infoAlle.get(i);

						if (infoZeile.getKostenstelleIIdAbteilung() != null) {

							JasperPrintLP druck = null;
							if (lhmGruppiertNachNabteilungen.containsKey(infoZeile.getKostenstelleIIdAbteilung())) {
								druck = lhmGruppiertNachNabteilungen.get(infoZeile.getKostenstelleIIdAbteilung());
							}

							if (druck != null) {

								druck = Helper.addReport2Report(druck, infoZeile.getPrint());
							} else {

								druck = infoZeile.getPrint();
							}

							lhmGruppiertNachNabteilungen.put(infoZeile.getKostenstelleIIdAbteilung(), druck);

						}

					}

					Iterator<Integer> itAbteilungen = lhmGruppiertNachNabteilungen.keySet().iterator();

					while (itAbteilungen.hasNext()) {
						Integer kostenstelleIIdAbteilung = itAbteilungen.next();

						KostenstelleDto kstDto = getSystemFac().kostenstelleFindByPrimaryKey(kostenstelleIIdAbteilung);

						// Abteilungsleiter suchen

						PersonalDto personalDtoAbteilungsleiter = getPersonalFac()
								.getAbteilungsleiter(kostenstelleIIdAbteilung, theClientDto);

						if (personalDtoAbteilungsleiter != null) {

							if (personalDtoAbteilungsleiter.getCEmail() != null) {

								JasperPrintLP druckGesammelt = lhmGruppiertNachNabteilungen
										.get(kostenstelleIIdAbteilung);

								VersandauftragDto versandauftragDto = new VersandauftragDto();
								try {
									versandauftragDto.setOInhalt(
											JasperExportManager.exportReportToPdf(druckGesammelt.getPrint()));

									versandauftragDto.setBEmpfangsbestaetigung(new Short("0"));

									versandauftragDto
											.setCText("Im Anhang finden Sie die Monatsabrechnungen Ihrer Abteilung");

									versandauftragDto.setCAbsenderadresse(absender);

									versandauftragDto.setCEmpfaenger(personalDtoAbteilungsleiter.getCEmail());

									versandauftragDto.setCBetreff("Monatsabrechnungen Ihrer Abteilung "
											+ kstDto.formatKostenstellenbezeichnung());

									getVersandFac().createVersandauftrag(versandauftragDto, false, theClientDto);

								} catch (Throwable t) {
									myLogger.error(
											"Fehler beim Erstellen des Versandauftrags f\u00FCr den Abteilungsleiter: "
													+ personalDtoAbteilungsleiter.formatFixName1Name2());
									errorInJob = true;

								}

							} else {
								meldung += "Abteilungsleiter " + personalDtoAbteilungsleiter.formatFixName1Name2()
										+ " hat keine E-Mail Adresse hinterlegt\n";
							}

						} else {
							meldung += kstDto.formatKostenstellenbezeichnung() + " hat keinen Abteilungsleiter\n";
						}

					}
					if (meldung.length() > 0) {
						myLogger.info(meldung);
					}
				}

			}
		} else {
			myLogger.info("Die Person " + personalDtoVersender.formatFixName1Name2()
					+ " hat keine E-Mail Absende-Adresse definiert");
			errorInJob = true;
		}
		myLogger.info("ende MonatsabrechnungVersandAbteilungen");
		return errorInJob;
	}
}
