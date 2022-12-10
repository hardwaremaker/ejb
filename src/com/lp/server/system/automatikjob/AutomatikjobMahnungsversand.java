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

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import net.sf.jasperreports.engine.JasperExportManager;

import com.lp.server.bestellung.service.BSMahnungDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.personal.service.PersonalDto;
import com.lp.server.system.ejbfac.HvPrinter;
import com.lp.server.system.service.AutoMahnungsversandDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ParametermandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.VersandauftragDto;
import com.lp.server.util.report.JasperPrintLP;

public class AutomatikjobMahnungsversand extends AutomatikjobBasis {

	private AutoMahnungsversandDto automahnungsversandDto;
	private BSMahnungDto[] mahnungDto;
	private boolean errorInJob;

	private Vector<Integer> vLieferantenIId;
	Integer iMahnlaufiId;

	private static final String VERSANDART_E_MAIL = "EMAIL";
	private static final String VERSANDART_FAX = "FAX";
	private static final String VERSANDART_KEIN_VERSAND = "KEIN";

	public AutomatikjobMahnungsversand() {
		super();
	}

	/**
	 * performJob
	 * 
	 * @todo Diese com.lp.server.system.automatikjob.AutomatikjobBasis-Methode
	 *       implementieren
	 */
	public boolean performJob(TheClientDto theClientDto) {
		myLogger.info("start Mahnungsversand");
		errorInJob = false;

		try {
			automahnungsversandDto = getAutoMahnungsversandFac()
					.autoMahnungsversandFindByMandantCNr(theClientDto.getMandant());
		} catch (Throwable t) {
			myLogger.error("fehler beim holen der Parameter");
			errorInJob = true;
		}
		iMahnlaufiId = automahnungsversandDto.getMahnlaufIId();
		automahnungsversandDto.setMahnlaufIId(null);
		try {
			getAutoMahnungsversandFac().updateAutoMahnungsversand(automahnungsversandDto);
		} catch (Throwable t) {
			myLogger.error("Fehler beim zur\u00FCckschreiben der Parameter");
			errorInJob = true;
		}
		if (iMahnlaufiId != null) {
			try {
				getBSMahnwesenFac().mahneBSMahnlauf(iMahnlaufiId, theClientDto);
			} catch (Throwable t) {
				myLogger.error("fehler beim erstellen des Mahnlaufs");
				errorInJob = true;
			}
			try {
				mahnungDto = getBSMahnwesenFac().bsmahnungFindByBSMahnlaufIId(iMahnlaufiId, theClientDto);
				for (int z = 0; z < mahnungDto.length; z++) {
					Integer iId = getBestellungFac().bestellungFindByPrimaryKey(mahnungDto[z].getBestellungIId())
							.getLieferantIIdBestelladresse();
					if (vLieferantenIId == null) {
						vLieferantenIId = new Vector<Integer>();
					}
					if (!vLieferantenIId.contains(iId)) {
						vLieferantenIId.add(iId);
					}
				}
				// This function doesn't work in our case because we need the
				// LieferantenIId for
				// every Mahnung
				/*
				 * jpPrint = bestellungreportFac.printBSSammelMahnung(iMahnlaufiId, usedUser);
				 */
			} catch (Throwable t) {
				myLogger.error("Fehler beim suchen, der zu Mahnenden Lieferanten");
				errorInJob = true;
				return errorInJob;
			}

			String usedPrinter = automahnungsversandDto.getCDrucker();
			String versandart = automahnungsversandDto.getCVersandart();
			try {
				if (usedPrinter == null)
					usedPrinter = getServerDruckerFac().getServerStandardPrinter();
			} catch (RemoteException e) {
				myLogger.error("Fehler beim Holen des Standarddruckers", e);
			}

			HvPrinter hvPrinter = getServerDruckerFacLocal().createHvPrinter(usedPrinter);
			boolean existsHvPrinter = getServerDruckerFacLocal().exists(hvPrinter);
			ArrayList<JasperPrintLP> jpPrint = null;
			if (!vLieferantenIId.isEmpty()) {
				for (int y = 0; y < vLieferantenIId.size(); y++) {
					// print erstellen

					try {
						jpPrint = getBestellungReportFac().printBSSammelMahnung(iMahnlaufiId,
								(Integer) vLieferantenIId.get(y), theClientDto, false, true);
					} catch (Throwable t) {
						myLogger.error(
								"Fehler beim erstellen des Drucks f\u00FCr den Lieferanten: " + vLieferantenIId.get(y));
						errorInJob = true;
					}

					for (int o = 0; o < jpPrint.size(); o++) {
						JasperPrintLP einzeldruck = jpPrint.get(o);

						try {
							if (existsHvPrinter)
								getServerDruckerFacLocal().print(einzeldruck, hvPrinter);
						} catch (Throwable t) {
							myLogger.error("Fehler beim Drucken", t);
							errorInJob = true;
						}
						// und per mail oder fax versenden
						if (versandart.equals(VERSANDART_KEIN_VERSAND)
								&& automahnungsversandDto.getCEmailZusaetzlich() == null) {
							// Nothing to do we just print
						} else {
							boolean bSend = true;
							VersandauftragDto versandauftragDto = new VersandauftragDto();
							try {
								versandauftragDto
										.setOInhalt(JasperExportManager.exportReportToPdf(einzeldruck.getPrint()));
							} catch (Throwable t) {
								myLogger.error("Fehler beim erstellen des Versandauftrags f\u00FCr den Lieferant: "
										+ vLieferantenIId.get(y));
								errorInJob = true;
								bSend = false;
							}
							versandauftragDto.setBEmpfangsbestaetigung(new Short("0"));

							if (versandart.equals(VERSANDART_E_MAIL) || (versandart.equals(VERSANDART_KEIN_VERSAND)
									&& automahnungsversandDto.getCEmailZusaetzlich() != null)) {
								// E-Mail Versand
								try {
									// TODO: add correct Mailtext
									if (bSend) {
										String mailtext = "";

										versandauftragDto.setCText(mailtext);
									}
								} catch (Throwable t) {
									myLogger.error("Fehler beim finden des Standardmailtextes");
									bSend = false;
									errorInJob = true;
								}
								PersonalDto persDto = null;

								if (bSend) {

									Integer personalIIdAnforderer = (Integer) einzeldruck
											.getAdditionalInformation("personalIIdAnforderer");

									String sAbsender = "";
									if (personalIIdAnforderer == null) {
										persDto = getPersonalFac()
												.personalFindByPrimaryKey(theClientDto.getIDPersonal(), theClientDto);
										sAbsender = persDto.getPartnerDto().getCEmail();
										versandauftragDto.setCAbsenderadresse(sAbsender);
									} else {
										persDto = getPersonalFac().personalFindByPrimaryKey(personalIIdAnforderer,
												theClientDto);
										sAbsender = persDto.getCEmail();
										versandauftragDto.setCAbsenderadresse(sAbsender);
									}

									if (sAbsender == null) {

										myLogger.error(
												"Fehler... Es ist kein Absender f\u00FCr die E-Mails deklariert (PersonalIId="
														+ persDto.getIId() + ")");
										bSend = false;
										errorInJob = true;
									}

									try {
										ParametermandantDto parameter = (ParametermandantDto) getParameterFac()
												.getMandantparameter(theClientDto.getMandant(),
														ParameterFac.KATEGORIE_BESTELLUNG,
														ParameterFac.PARAMETER_MAHNUNG_AUTO_CC);

										boolean bAbsenderBekommtCC = ((Boolean) parameter.getCWertAsObject());
										if (bAbsenderBekommtCC == true) {
											versandauftragDto.setCCcempfaenger(sAbsender);
										}
									} catch (RemoteException e) {
										throwEJBExceptionLPRespectOld(e);
									}
								}

								if (bSend) {
									LieferantDto lieferantDto = getLieferantFac()
											.lieferantFindByPrimaryKey((Integer) vLieferantenIId.get(y), theClientDto);
									String sEmpfaenger = null;

									sEmpfaenger = lieferantDto.getPartnerDto().getCEmail();

									if (sEmpfaenger == null) {
										myLogger.info(
												"Keine Kontaktadresse f\u00FCr Lieferant " + vLieferantenIId.get(y)
														+ lieferantDto.getPartnerDto().formatFixTitelName1Name2()
														+ " gefunden. Mail wird an zugewiesenes Personal gesendet");
									}

									if (sEmpfaenger == null) {
										PersonalDto persDtoEmpfaenger = getPersonalFac()
												.personalFindByPrimaryKey(theClientDto.getIDPersonal(), theClientDto);
										sEmpfaenger = persDtoEmpfaenger.getPartnerDto().getCEmail();
										if (sEmpfaenger == null) {
											myLogger.error("Fehler beim finden der Kontaktadressen");
											bSend = false;
											errorInJob = true;
										}

									}
									versandauftragDto.setCEmpfaenger(sEmpfaenger);
								}

								try {
									if (bSend) {

										HashMap bestellnummern = (HashMap) einzeldruck
												.getAdditionalInformation("bestellnummern");

										String sLocMahnung = getTextRespectUISpr("lp.mahnung",
												theClientDto.getMandant(), theClientDto.getLocMandant());

										String betreff = sLocMahnung;

										if (bestellnummern.size() > 0) {
											Iterator it = bestellnummern.keySet().iterator();
											while (it.hasNext()) {
												betreff += " " + it.next();
												if (it.hasNext()) {
													betreff += ",";
												}
											}
										}

										if (betreff.length() > 95) {
											betreff = betreff.substring(0, 94) + "...";
										}
										versandauftragDto.setCBetreff(betreff);

									}
								} catch (Throwable t) {
									myLogger.error("Fehler beim erstellen des Betreffs");
									errorInJob = true;
									bSend = false;
								}

							}
							if (versandart.equals(VERSANDART_FAX)) {
								// Fax-Versand
								try {
									if (bSend) {
										String sBetreff = "Automatischer Mahnungsversand " + theClientDto.getMandant();
										versandauftragDto.setCBetreff(sBetreff);
									}
								} catch (Throwable t) {
									myLogger.error("Fehler beim erstellen des Betreffs");
									errorInJob = true;
									bSend = false;
								}
								try {
									if (bSend) {
										LieferantDto lieferantDto = getLieferantFac().lieferantFindByPrimaryKey(
												(Integer) vLieferantenIId.get(y), theClientDto);
										String sEmpfaenger = null;

										sEmpfaenger = lieferantDto.getPartnerDto().getCFax();
										if (sEmpfaenger == null) {
											myLogger.info("Keine Faxnummer f\u00FCr den Lieferanten "
													+ vLieferantenIId.get(y)
													+ lieferantDto.getPartnerDto().formatFixTitelName1Name2()
													+ " deklariert... Mahnung wird als Mail an zust\u00E4ndigen Benutzer gesendet");
										}

										if (sEmpfaenger == null) {
											PersonalDto persDto = getPersonalFac().personalFindByPrimaryKey(
													theClientDto.getIDPersonal(), theClientDto);
											sEmpfaenger = persDto.getPartnerDto().getCFax();
										}
										versandauftragDto.setCEmpfaenger(sEmpfaenger);
									}
								} catch (Throwable t) {

								}
							}
							try {
								if (bSend) {

									if (!versandart.equals(VERSANDART_KEIN_VERSAND)) {
										getVersandFac().createVersandauftrag(versandauftragDto, false, theClientDto);
									}

									// PJ21156
									if (automahnungsversandDto.getCEmailZusaetzlich() != null) {
										versandauftragDto.setCEmpfaenger(automahnungsversandDto.getCEmailZusaetzlich());
										versandauftragDto.setCBccempfaenger(null);
										versandauftragDto.setCCcempfaenger(null);
										getVersandFac().createVersandauftrag(versandauftragDto, false, theClientDto);
									}

								} else {
									myLogger.info("Fehler beim erstellen des Versandauftrags");
								}
							} catch (Throwable t) {
								myLogger.error("Fehler beim senden des Versandauftrags");
							}
						}
					}
				}
			} else {
				myLogger.info("Keine Daten zu drucken vorhanden");
			}
		} else {
			myLogger.info("Es wurde kein Mahnlauf erstellt... Keine Daten zu drucken/versenden");
		}
		myLogger.info("ende Mahnungsversand");
		return errorInJob;
	}
}
