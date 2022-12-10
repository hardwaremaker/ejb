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

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.lp.server.fertigung.service.FertigungReportFac;
import com.lp.server.system.service.JobDetailsAuslieferlisteDto;
import com.lp.server.system.service.ReportvarianteDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.Helper;

public class AutomatikjobAuslieferlistePDFDruck extends AutomatikjobPDFDruck {

	private static final Integer DELETION_PERIOD_IN_DAYS = 14;

	public AutomatikjobAuslieferlistePDFDruck() {
		super();
	}

	@Override
	public void performJobImpl() throws Throwable {

		JobDetailsAuslieferlisteDto jobAuslieferlisteDto = getJobAuslieferlisteFac()
				.findByMandantCNr(getTheClientDto().getMandant());
		if (!hasJobAuslieferlisteValidProperties(jobAuslieferlisteDto)) {
			setErrorInJob();
			return;
		}

		Timestamp tStichtag = new Timestamp(
				Helper.addiereTageZuDatum(getDate(), jobAuslieferlisteDto.getiTageBisStichtag()).getTime());
		List<ReportvarianteDto> reportvarianten = getReportvarianten(FertigungReportFac.REPORT_AUSLIEFERLISTE);
		for (ReportvarianteDto variante : reportvarianten) {
			setFilepath(jobAuslieferlisteDto.getcPfadPattern(), getPatternValues(variante));
			if (hasAccessToFilepath()) {
				boolean bNurNachLosEndeTerminSortiert = false;
				if (jobAuslieferlisteDto.getBNurLoseNachEndetermin() != null) {
					bNurNachLosEndeTerminSortiert = Helper
							.short2boolean(jobAuslieferlisteDto.getBNurLoseNachEndetermin());
				}

				getTheClientDto().setReportvarianteIId(variante.getIId());
				JasperPrintLP print = getFertigungReportFac().printAuslieferliste(tStichtag, null,
						bNurNachLosEndeTerminSortiert, getTheClientDto());
				exportPrint(print);
			}
		}

		if (!isErrorInJob()) {
			// alte Daten loeschen
			for (ReportvarianteDto variante : reportvarianten) {
				deleteOldVersionsOfReport(jobAuslieferlisteDto.getcPfadPattern(), getPatternValues(variante),
						jobAuslieferlisteDto.getiArchivierungstage());
			}
		}
	}

	protected void deleteOldVersionsOfReport(String pfadpattern, Object[] values, Integer archivierungstage) {
		Date baseDate = (Date) values[0];
		for (int day = 0; day < DELETION_PERIOD_IN_DAYS; day++) {
			Date date = Helper.addiereTageZuDatum(baseDate, (archivierungstage * -1) - day);
			values[0] = date;
			deleteFile(pfadpattern, values);
		}
	}

	private Object[] getPatternValues(ReportvarianteDto variante) {
		String reportname = variante.getCReportnamevariante();
		if (reportname != null && reportname.endsWith(JASPER_EXTENSION)) {
			reportname = reportname.substring(0, reportname.length() - JASPER_EXTENSION.length());
		}
		return new Object[] { getDate(), variante.getCRessource(), reportname };
	}

	private boolean hasJobAuslieferlisteValidProperties(JobDetailsAuslieferlisteDto jobAuslieferlisteDto) {
		if (jobAuslieferlisteDto == null) {
			myLogger.error("Autojob Auslieferliste not found in DB for Mandant " + getTheClientDto().getMandant());
			return false;
		}

		if (jobAuslieferlisteDto.getcPfadPattern() == null || jobAuslieferlisteDto.getiArchivierungstage() == null
				|| jobAuslieferlisteDto.getiTageBisStichtag() == null) {

			myLogger.error(
					"Autojob Auslieferliste Property PfadPattern || Archivierungstage || TageBisStichtag not set");
			return false;
		}
		return true;
	}

}