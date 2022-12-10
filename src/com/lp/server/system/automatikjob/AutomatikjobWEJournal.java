package com.lp.server.system.automatikjob;

import java.util.Date;

import com.lp.server.bestellung.service.BestellungReportFac;
import com.lp.server.bestellung.service.JobDetailsWEJournalDto;
import com.lp.server.system.service.ReportJournalKriterienDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.Helper;

public class AutomatikjobWEJournal extends AutomatikjobPDFProvider {
	
	private static final Integer DELETION_PERIOD_IN_DAYS = 14;
	private JobDetailsWEJournalDto detailsDto;

	public AutomatikjobWEJournal() {
		super();
	}

	@Override
	protected void performJobImpl() throws Throwable {
		if (!initJobDetailsDto()) {
			return;
		}	
		
		validateProperties();
		if (isErrorInJob()) {
			return;
		}
		
		JasperPrintLP print = createWareneingangsjournal();
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
			providePrint(new PrintData(print, 
					getTextRespectUISpr("lp.standard", getTheClientDto().getMandant(), 
							getTheClientDto().getLocUi()), 
					getReportname()));
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
	
	private JasperPrintLP createWareneingangsjournal() {
		ReportJournalKriterienDto krit = new ReportJournalKriterienDto();
		krit.dBis = Helper.cutDate(getDate());
		krit.dVon = Helper.addiereTageZuDatum(krit.dBis, -detailsDto.getITageBisStichtag());
		krit.iSortierung = ReportJournalKriterienDto.KRIT_SORT_NACH_PARTNER;
		try {
			JasperPrintLP print = getBestellungReportFac().printBestellungWareneingangsJournal(
					krit, null, null, null, null, null, null, false, getTheClientDto());
			return print;
		} catch (Throwable t) {
			myLogger.error("Fehler bei Erstellung des Wareneingangsjournals", t);
			setErrorInJob();
		}
		return null;
	}

	private boolean initJobDetailsDto() {
		detailsDto = getJobWEJournalFac().findByMandantCNr(getTheClientDto().getMandant());
		if (detailsDto == null) {
			myLogger.error("Details Automatikjob WE-Journal not found in DB");
			setErrorInJob();
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
		return "Automatikjob Wareneingangsjournal PDF";
	}

	@Override
	protected String getMailMessage(PrintData printData) {
		return "Report Wareneingangsjournal wurde erfolgreich erzeugt und ist als PDF angeh\u00e4ngt.";
	}
	
	private String getReportname() {
		String reportname = BestellungReportFac.REPORT_BESTELLUNG_JOURNAL_WARENEINGANG;
		reportname = reportname.substring(0, reportname.length() - JASPER_EXTENSION.length());
		return reportname;
	}

}
