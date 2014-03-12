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
package com.lp.server.system.automatikjob;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

import com.lp.server.system.ejbfac.ServerDruckerFacBean;
import com.lp.server.system.service.ReportJournalKriterienDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.report.JasperPrintLP;

public class AutomatikjobBestellvorschlagdruck extends AutomatikjobBasis {

	private boolean errorInJob;

	private static final String NICHT_DRUCKEN = "Nicht Drucken";

	public AutomatikjobBestellvorschlagdruck() {
		super();
	}

	/**
	 * performJob
	 * 
	 * @todo Diese com.lp.server.system.automatikjob.AutomatikjobBasis-Methode
	 *       implementieren
	 */
	public boolean performJob(TheClientDto theClientDto) {
		try {
			myLogger.info("start Bestellvorschlagdruck");
			errorInJob = false;
			ReportJournalKriterienDto reportJournalKriterienDto = new ReportJournalKriterienDto();
			reportJournalKriterienDto.iSortierung = ReportJournalKriterienDto.KRIT_SORT_NACH_ART;
			boolean bIsLocked = false;
			try {
				getBestellvorschlagFac()
						.pruefeBearbeitenDesBestellvorschlagsErlaubt(theClientDto);
			} catch (Throwable t) {
				myLogger
						.info("Bestellvorschlag ist von einem anderen Benutzer gesperrt. Job wird nicht durchgef\u00FChrt");
				bIsLocked = true;
				errorInJob = true;
			}
			if (!bIsLocked) {
				JasperPrintLP print = getBestellungReportFac()
						.printBestellVorschlag(reportJournalKriterienDto,
								false,false, theClientDto);
				PrintService[] printService = PrintServiceLookup
						.lookupPrintServices(null, null);
				int i = 0;
				String usedPrinter = getAutoBestellvorschlagFac()
						.autoBestellvorschlagFindByMandantCNr(theClientDto.getMandant())
						.getCDrucker();
				if (usedPrinter.equals("Kein Drucker gefunden")) {
					// do nothing there is no Printer
				} else {
					while (i < printService.length) {
						if (!printService[i].getName().equals(usedPrinter)) {
							i++;
						} else {
							if (!usedPrinter.equals(NICHT_DRUCKEN)) {
								ServerDruckerFacBean.print(print, printService[i]);
							}
							try {
								getBestellvorschlagFac()
										.removeLockDesBestellvorschlagesWennIchIhnSperre(
												theClientDto);
							} catch (Throwable t) {
								myLogger
										.error("Fehler beim entfernen der Sperre... Bitte heben Sie die Sperre manuell \u00FCber einen Client auf.");
							}
							break;
						}
					}
				}
				try {
					getBestellvorschlagFac()
							.removeLockDesBestellvorschlagesWennIchIhnSperre(
									theClientDto);
				} catch (Throwable t) {
					myLogger
							.error("Fehler beim entfernen der Sperre... Bitte heben Sie die Sperre manuell \u00FCber einen Client auf.");
				}

			}
		} catch (Throwable ex) {
			myLogger
					.error("Fehler... Der Bestellvorschlagdruck konnte nicht ausgef\u00FChrt werden");
			errorInJob = true;
		}
		myLogger.info("ende Bestellvorschlagdruck");
		return errorInJob;
	}

}
