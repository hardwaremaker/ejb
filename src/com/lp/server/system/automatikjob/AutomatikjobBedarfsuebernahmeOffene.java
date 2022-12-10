package com.lp.server.system.automatikjob;

import com.lp.server.fertigung.service.FertigungReportFac;
import com.lp.server.fertigung.service.JobDetailsBedarfsuebernahmeOffeneDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.Helper;

public class AutomatikjobBedarfsuebernahmeOffene extends AutomatikjobPDFProvider {
	
	private JobDetailsBedarfsuebernahmeOffeneDto detailsDto;

	public AutomatikjobBedarfsuebernahmeOffene() {
		super();
	}
	
	private boolean initJobDetailsDto() {
		detailsDto = getJobBedarfsuebernahmeOffeneFac().findByMandantCNr(getTheClientDto().getMandant());
		if (detailsDto == null) {
			myLogger.error("Details Automatikjob WE-Journal not found in DB");
			setErrorInJob();
			return false;
		}
		return true;
	}
	
	private boolean validateProperties() {
		if (Helper.isStringEmpty(detailsDto.getCEmailEmpfaenger())) {
			myLogger.error("Email-Empf\u00e4nger ist leer.");
			setErrorInJob();
			return false;
		}
		return true;
	}

	@Override
	protected void performJobImpl() throws Throwable {
		if (!initJobDetailsDto() 
				|| !validateProperties()) {
			return;
		}
		
		JasperPrintLP print = createBedarfsuebernahmeOffen();
		if (print != null) {
			providePrint(print);
		}
	}

	private JasperPrintLP createBedarfsuebernahmeOffen() {
		try {
			JasperPrintLP print = getFertigungReportFac().printBedarfsuebernahmeOffene(null, null, false, getTheClientDto());
			return print;
		} catch (Throwable t) {
			myLogger.error("Fehler bei Erstellung der Bedarfsuebernahme Offen", t);
			setErrorInJob();
		}
		return null;
	}

	private void providePrint(JasperPrintLP print) {
		try {
			String reportname = FertigungReportFac.REPORT_BEDARFSUEBERNAHME_OFFENE;
			reportname = reportname.substring(0, reportname.length() - JASPER_EXTENSION.length());
			providePrint(new PrintData(print, 
					getTextRespectUISpr("lp.standard", getTheClientDto().getMandant(), 
							getTheClientDto().getLocUi()), reportname));
		} catch (Throwable t) {
			myLogger.error("Fehler waehrend des Sendens des PDFs", t);
			setErrorInJob();
		}
	}

	@Override
	protected String getPathPattern() {
		return null;
	}

	@Override
	protected String getMailRecipient() {
		return detailsDto.getCEmailEmpfaenger();
	}

	@Override
	protected String getMailSubject(PrintData printData) {
		return getTextRespectUISpr("lp.automatikjob.bedarfsuebernahmedruck.mail.betreff", 
				getTheClientDto().getMandant(), getTheClientDto().getLocUi());
	}

	@Override
	protected String getMailMessage(PrintData printData) {
		return getTextRespectUISpr("lp.automatikjob.bedarfsuebernahmedruck.mail.text", 
				getTheClientDto().getMandant(), getTheClientDto().getLocUi());
	}

}
