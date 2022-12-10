package com.lp.server.system.automatikjob;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import com.lp.server.system.service.VersandanhangDto;
import com.lp.server.system.service.VersandauftragDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

import net.sf.jasperreports.engine.JRException;

public abstract class AutomatikjobPDFProvider extends AutomatikjobPDFDruck {

	public AutomatikjobPDFProvider() {
		super();
	}

	protected class PrintData {
		private JasperPrintLP print;
		private String reportvariante;
		private String reportname;

		public PrintData(JasperPrintLP print, String reportvariante, String reportname) {
			this.print = print;
			this.reportvariante = reportvariante;
			this.reportname = reportname;
		}
		
		public JasperPrintLP getPrint() {
			return print;
		}
		public String getReportvariante() {
			return reportvariante;
		}
		public String getReportname() {
			return reportname;
		}
	}
	
	protected void providePrint(PrintData printData) throws Throwable {
		if (hasPathPattern()) {
			savePDF(printData);
		}
		
		if (hasValidMailRecipient()) {
			sendPDF(printData);
		}
	}
		
	private void sendPDF(PrintData printData) throws EJBExceptionLP, RemoteException {
		VersandauftragDto versandauftragDto = setupVersandauftragDto(
				getParameterFac().getMailadresseAdmin(getTheClientDto().getMandant()), 
				getMailRecipient(), 
				getMailSubject(printData), 
				getMailMessage(printData));
		VersandanhangDto versandanhangDto = new VersandanhangDto();
		versandanhangDto.setCDateiname(printData.getReportname() + ".pdf");
		versandanhangDto.setOInhalt(exportToPDF(printData.getPrint().getPrint()));
		
		List<VersandanhangDto> anhaenge = new ArrayList<VersandanhangDto>();
		anhaenge.add(versandanhangDto);
		getVersandFac().createVersandauftrag(
				versandauftragDto, anhaenge, false, getTheClientDto());
	}

	private void savePDF(PrintData printData) throws JRException {
		setFilepath(getPathPattern(), getPatternValues(printData));
		if (hasAccessToFilepath()) {
			exportPrint(printData.getPrint());
		}
	}

	protected Object[] getPatternValues(PrintData printData) {
		return new Object[] {getDate(), 
				printData.getReportvariante(),
				printData.getReportname()};
	}
	
	private boolean hasPathPattern() {
		return !Helper.isStringEmpty(getPathPattern());
	}

	private boolean hasValidMailRecipient() {
		return Helper.validateEmailadresse(getMailRecipient());
	}
	
	protected abstract String getPathPattern();
	protected abstract String getMailRecipient();
	protected abstract String getMailSubject(PrintData printData);
	protected abstract String getMailMessage(PrintData printData);
}
