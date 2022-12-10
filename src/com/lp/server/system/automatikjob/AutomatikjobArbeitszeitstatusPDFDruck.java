package com.lp.server.system.automatikjob;

import java.sql.Timestamp;
import java.util.Date;

import com.lp.server.fertigung.service.FertigungReportFac;
import com.lp.server.fertigung.service.JobDetailsArbeitszeitstatusDto;
import com.lp.server.util.DatumsfilterVonBis;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.Helper;

public class AutomatikjobArbeitszeitstatusPDFDruck extends AutomatikjobPDFProvider {

	private static final Integer DELETION_PERIOD_IN_DAYS = 14;
	private JobDetailsArbeitszeitstatusDto detailsDto;
	
	public AutomatikjobArbeitszeitstatusPDFDruck() {
		super();
	}

	@Override
	protected void performJobImpl() throws Throwable {
		if (!initJobDetailsDto()) {
			setErrorInJob();
			return;
		}
		
		validateProperties();
		if (isErrorInJob()) {
			return;
		}
		
		JasperPrintLP print = createJournalArbeitszeitstatus();
		if (print != null) {
			providePrint(print);
		}
		
		if (!isErrorInJob()) {
			deleteOldVersionsOfReport();
		}
	}
	
	protected void deleteOldVersionsOfReport() {
		Object[] values = getPatternValues(createPrintData(null));
		Date baseDate = (Date) values[0];
		Integer archivierungstage = detailsDto.getIArchivierungstage() != null ?
				detailsDto.getIArchivierungstage() : 1;
		for (int day = 0; day < DELETION_PERIOD_IN_DAYS; day++) {
			Date date = Helper.addiereTageZuDatum(baseDate, (archivierungstage*-1) - day);
			values[0] = date;
			deleteFile(detailsDto.getCPfadPattern(), values);
		}
	}

	private void providePrint(JasperPrintLP print) {
		try {
			providePrint(createPrintData(print));
		} catch (Throwable t) {
			myLogger.error("Fehler waehrend des Speicherns oder Senden des PDFs", t);
			setErrorInJob();
		}
	}

	private PrintData createPrintData(JasperPrintLP print) {
		return new PrintData(print, 
				getTextRespectUISpr("lp.standard", getTheClientDto().getMandant(), 
						getTheClientDto().getLocUi()), 
				getReportname());
	}

	private JasperPrintLP createJournalArbeitszeitstatus() {
		try {
			Timestamp tBis = Helper.cutTimestamp(getTimestamp());
			Timestamp tVon = Helper.addiereTageZuTimestamp(tBis, -detailsDto.getITageBisStichtag());
			DatumsfilterVonBis vonBis = new DatumsfilterVonBis(tVon, tBis);
			JasperPrintLP print = getFertigungReportFac().printArbeitszeitstatus(vonBis, getTheClientDto());
			return print;
		} catch (Throwable t) {
			myLogger.error("Fehler bei Erstellung des Journals Arbeitszeitstatus", t);
			setErrorInJob();
			return null;
		}
	}

	private boolean initJobDetailsDto() {
		detailsDto = getJobArbeitszeitstatusFac().findByMandantCNr(getTheClientDto().getMandant());
		if (detailsDto == null) {
			myLogger.error("Details Automatikjob Arbeitszeitstatus not found in DB");
			return false;
		}
		return true;
	}

	private void validateProperties() {
		if (Helper.isStringEmpty(detailsDto.getCEmailEmpfaenger()) 
				&& Helper.isStringEmpty(detailsDto.getCPfadPattern())) {
			myLogger.error("Pfad-Pattern und Email-Empf\u00e4nger sind leer.");
			setErrorInJob();
		}
	}
	
	@Override
	protected String getPathPattern() {
		return detailsDto.getCPfadPattern();
	}
	
	@Override
	protected String getMailRecipient() {
		return detailsDto.getCEmailEmpfaenger();
	}
	
	@Override
	protected String getMailSubject(PrintData printData) {
		return "Automatikjob Arbeitszeitstatus PDF";
	}
	
	@Override
	protected String getMailMessage(PrintData printData) {
		return "Report Arbeitszeitstatus wurde erfolgreich erzeugt und ist als PDF angeh\u00e4ngt.";
	}
		
	private String getReportname() {
		String reportname = FertigungReportFac.REPORT_ARBEITSZEITSTATUS;
		reportname = reportname.substring(0, reportname.length() - JASPER_EXTENSION.length());
		return reportname;
	}
}
