package com.lp.server.util.report;

import java.util.Locale;

import com.lp.server.system.ejbfac.EJBExcFactory;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.TheClientDto;

public class LpMailTextMitHtmlSignatur extends LpMailText {
	private static final String xslSuffix = "_signatur";	
	private ParameterFac parameterFac;
	private boolean hasHtmlSignature;
	 
	public LpMailTextMitHtmlSignatur(ParameterFac parameterFac) {
		this.parameterFac = parameterFac;
	}
	
	protected String getReportDir(String modul, String mandant, String xslFile, Locale sprache, TheClientDto theClientDto) {
		if(!hasHtmlSignature) {
			return super.getReportDir(modul, mandant, xslFile, sprache, theClientDto);
		}
		
		String reportname = xslFile.replaceAll(".jasper", "");
		String reportdir = getReportDirImpl(modul, reportname + xslSuffix, mandant, sprache, theClientDto);

		if (reportdir == null) {
			reportdir = getReportDirImpl(modul, "mail" + xslSuffix, mandant, sprache, theClientDto);
		}

		if (reportdir == null) {
			reportdir = getReportDirImpl("allgemein", "mail" + xslSuffix, mandant, sprache, theClientDto);
		}

		if (reportdir == null) {
			throw EJBExcFactory.mailtextvorlageMitSignaturNichtGefunden(modul, mandant, reportname, sprache);
//			throw new EJBExceptionLP(
//					EJBExceptionLP.FEHLER_DRUCKEN_REPORT_NICHT_GEFUNDEN,
//					"Es konnte kein Reportdir gefunden werden. mandantcnr: " + mandant + " sprache " + sprache);
		}
		
		return reportdir ;
	}

	public String transformText(MailtextDto mailtextDto, TheClientDto theClientDto) {
		hasHtmlSignature = prepareHtmlSignature(mailtextDto, theClientDto);
		if(!hasHtmlSignature) {
			return super.transformText(mailtextDto, theClientDto);
		}

		String modul = mailtextDto.getParamModul();
		String mandantCNr = mailtextDto.getParamMandantCNr();
		String xslFile = mailtextDto.getParamXslFile();
		Locale sprache = mailtextDto.getParamLocale();
		String content = transformText(modul, mandantCNr, xslFile, sprache, theClientDto); 
		return createHtmlMailBody(content);
	}
	
	private boolean prepareHtmlSignature(MailtextDto mailtextDto, TheClientDto theClientDto) {
		String value = parameterFac
				.getHtmlSignaturMailversand(mailtextDto.getParamMandantCNr());
		if(value.isEmpty()) {
			return false;
		}
		addParameter("html_signatur", value);
		return true;
	}
	
	private String createHtmlMailBody(String plaintext) {
		String content = "<!DOCTYPE html><html><body>" +
				"<head><meta charset=\"utf-8\"></head>" +
				plaintext.replaceAll("\r\n\r\n", "<br>") +
				"</body></html>";
		return content;	
	}
}
