package com.lp.server.system.automatikjob;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.List;

import com.lp.server.kpi.service.KpiReportFac;
import com.lp.server.system.service.JobDetailsKpiReportDto;
import com.lp.server.system.service.ReportvarianteDto;
import com.lp.server.util.HvOptional;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.Helper;

public class AutomatikjobKpiPdfDruck extends AutomatikjobPDFProvider {

	private HvOptional<JobDetailsKpiReportDto> jobDetails = HvOptional.empty();
	
	@Override
	protected void performJobImpl() throws Throwable {
		if(!hatFunktion()) {
			return;
		}
		
		JobDetailsKpiReportDto jobDto = getJobKpiReportFac().findByMandantCNr(getTheClientDto().getMandant());
		if(!isJobDetailValid(jobDto)) {
			setErrorInJob();
			return;
		}
		jobDetails = HvOptional.of(jobDto);
		
		Date sqlBis = Helper.addiereTageZuDatum(Helper.cutDate(getDate()), -1);
		Date sqlVon = Helper.addiereTageZuDatum(sqlBis, -jobDto.getiTage());
				
		List<ReportvarianteDto> reportvarianten = 
				getReportvarianten(KpiReportFac.REPORT_KPI);
		for (ReportvarianteDto variante : reportvarianten) {
			try {
				providePrint(createPrint(sqlVon, sqlBis, variante));
			} catch (Throwable t) {
				myLogger.error("Fehler waehrend der Druckverarbeitung", t);
				setErrorInJob();				
			}
			
//			setFilepath(jobDto.getcPfadPattern(), buildPatternValues(now, variante));
//			if (hasAccessToFilepath()) {
//				getTheClientDto().setReportvarianteIId(variante.getIId());
//				JasperPrintLP print = getKpiReportFac().printKpi(sqlVon, sqlBis, getTheClientDto());
//				exportPrint(print);
//			}					
		}
	
		deleteOldPdfs(sqlBis, reportvarianten, 
				jobDto.getcPfadPattern(), jobDto.getiArchivierungstage());
	}

	
	private PrintData createPrint(java.sql.Date sqlVon, java.sql.Date sqlBis, ReportvarianteDto varianteDto) throws RemoteException {
		getTheClientDto().setReportvarianteIId(varianteDto.getIId());
		JasperPrintLP print = getKpiReportFac().printKpi(sqlVon, sqlBis, getTheClientDto());
			
		return new PrintData(print, 
				simpleName(varianteDto.getCReportnamevariante()),
				simpleName(varianteDto.getCReportname()));
	}

	private String simpleName(String reportName) {
		return reportName.replace(JASPER_EXTENSION, "");
	}
	
	private boolean isJobDetailValid(JobDetailsKpiReportDto jobDto) {
		if(jobDto == null) {
			myLogger.error("Autojob KpiReport not found in DB for Mandant " + getTheClientDto().getMandant());
			return false;
		}
	
		if(jobDto.getcPfadPattern() == null ||
			jobDto.getiArchivierungstage() == null || 
			jobDto.getiTage() == null) {
			myLogger.error("Autojob KpiReport Properties not set");
			return false;
		}
		
		return true;
	}
	
	private boolean hatFunktion() {
		return getMandantFac()
				.hatZusatzfunktionLiquiditaetsvorschau(getTheClientDto());
	}
	
	@Override
	protected String getMailMessage(PrintData printData) {
		return getTextRespectUISpr("lp.automatikjob.kpireport.mail.text", 
				getTheClientDto().getMandant(), getTheClientDto().getLocUi(), 
				printData.getReportvariante());
	}
	
	@Override
	protected String getMailSubject(PrintData printData) {
		return getTextRespectUISpr("lp.automatikjob.kpireport.mail.betreff", 
				getTheClientDto().getMandant(), getTheClientDto().getLocUi(), 
				printData.getReportvariante());
	}
	
	@Override
	protected String getMailRecipient() {
		return jobDetails.get().getcEmailEmpfaenger();
	}
	
	@Override
	protected String getPathPattern() {
		return jobDetails.get().getcPfadPattern();
	}
}
