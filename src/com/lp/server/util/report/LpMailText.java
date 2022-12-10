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
package com.lp.server.util.report;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.jdom.Element;

import com.lp.server.system.ejbfac.EJBExcFactory;
import com.lp.server.system.ejbfac.SystemServicesFacBean;
import com.lp.server.system.service.MailtextDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.util.EJBExceptionLP;

/**
 * <p>
 * Util Klasse f&uuml;r Mailversand.
 * </p>
 * <p>
 * Copyright Logistik Pur GmbH (c) 2005
 * </p>
 * <p>
 * Erstellungsdatum 2005-08-11
 * </p>
 * <p>
 * </p>
 * 
 * @author adi daum
 * @version 1.0
 */
public class LpMailText {
	Map<String, String> parameter = new LinkedHashMap<String, String>();

	private static final String OUTPUT_ENCODING = "UTF-8";

	public LpMailText() {
	}

	protected String getReportDirImpl(String modul, String xslPrefix, String mandant, Locale sprache, TheClientDto theClientDto) {
		return SystemServicesFacBean.getPathFromLPDir(
				modul, xslPrefix + ".xsl", mandant, sprache, null, theClientDto);		
	}

	protected String getReportDir(String modul, String mandant, String xslFile, Locale sprache, TheClientDto theClientDto) {
		String reportname = xslFile.replaceAll(".jasper", "");
		String reportdir = getReportDirImpl(modul, reportname, mandant, sprache, theClientDto);

		if (reportdir == null) {
			reportdir = getReportDirImpl(modul, "mail", mandant, sprache, theClientDto);
		}

		if (reportdir == null) {
			// wenn kein xsl fuer modul gefunden, das allgemeine holen
			reportdir = getReportDirImpl("allgemein", "mail", mandant, sprache, theClientDto);
		}

		if (reportdir == null) {
			throw EJBExcFactory.mailtextvorlageNichtGefunden(modul, mandant, reportname, sprache);
//			throw new EJBExceptionLP(
//					EJBExceptionLP.FEHLER_DRUCKEN_REPORT_NICHT_GEFUNDEN,
//					"Es konnte kein Reportdir gefunden werden. mandantcnr: " + mandant + " sprache " + sprache);
		}
		
		return reportdir ;
	}

	public String transformText(String modul, String mandantCNr, String xslFile, Locale sprache, TheClientDto theClientDto) {
//		String reportdir = SystemServicesFacBean.getPathFromLPDir(modul,
//				xslFile.replaceAll(".jasper", "") + ".xsl", mandantCNr,
//				sprache, null, theClientDto);
//
//		if (reportdir == null) {
//			// wenn kein xsl fuer modul gefunden, das allgemeine holen
//			reportdir = SystemServicesFacBean.getPathFromLPDir("allgemein",
//					"mail.xsl", mandantCNr, sprache, null, theClientDto);
//			if (reportdir == null) {
//				throw new EJBExceptionLP(
//						EJBExceptionLP.FEHLER_DRUCKEN_REPORT_NICHT_GEFUNDEN,
//						"Es konnte kein Reportdir gefunden werden. mandantcnr: " + mandantCNr + " sprache " + sprache);
//			}
//		}
		
		String reportdir = getReportDir(modul, mandantCNr, xslFile, sprache, theClientDto) ;
		return transformXML(reportdir);
	}
	
	

	public String transformText(MailtextDto mailtextDto, TheClientDto theClientDto) {
		String modul = mailtextDto.getParamModul();
		String mandantCNr = mailtextDto.getParamMandantCNr();
		String xslFile = mailtextDto.getParamXslFile();
		Locale sprache = mailtextDto.getParamLocale();
		return transformText(modul, mandantCNr, xslFile, sprache, theClientDto);
	}
		
	
//	public String transformText(String modul, String mandantCNr, String xslFile, Locale sprache, TheClientDto theClientDto) {
//
//		String reportdir = SystemServicesFacBean.getPathFromLPDir(modul,
//				xslFile.replaceAll(".jasper", "") + ".xsl", mandantCNr,
//				sprache, null, theClientDto);
//
//		if (reportdir == null) {
//			// wenn kein xsl fuer modul gefunden, das allgemeine holen
//			reportdir = SystemServicesFacBean.getPathFromLPDir("allgemein",
//					"mail.xsl", mandantCNr, sprache, null, theClientDto);
//			if (reportdir == null) {
//				throw new EJBExceptionLP(
//						EJBExceptionLP.FEHLER_DRUCKEN_REPORT_NICHT_GEFUNDEN,
//						"Es konnte kein Reportdir gefunden werden. mandantcnr: " + mandantCNr + " sprache " + sprache);
//			}
//		}
//		
//		return transformXML(reportdir);
//	}
//	

	public String transformBetreff(MailtextDto mailtextDto, TheClientDto theClientDto) {
		return transformMailPartImpl("betreff", mailtextDto, theClientDto);
	}
	
	public String transformAbsender(MailtextDto mailtextDto, TheClientDto theClientDto) {
		return transformMailPartImpl("absender", mailtextDto, theClientDto);
	}
	
	public String transformAnhang(MailtextDto mailtextDto, TheClientDto theClientDto) {
		return transformMailPartImpl("anhang", mailtextDto, theClientDto);
	}
	
	private String transformMailPartImpl(String xslPrefix, MailtextDto mailtextDto, TheClientDto theClientDto) {
		String modul = mailtextDto.getParamModul();
		String mandantCNr = mailtextDto.getParamMandantCNr();
		String xslFile = mailtextDto.getParamXslFile();
		Locale sprache = mailtextDto.getParamLocale();
		
		String reportdir = getReportDirImpl(modul, xslFile.replaceAll(".jasper", "") + "_" + xslPrefix, mandantCNr, sprache, theClientDto);

		if (reportdir == null) {
			// wenn kein xsl fuer modul gefunden, das allgemeine holen
			reportdir = getReportDirImpl("allgemein", xslPrefix, mandantCNr, sprache, theClientDto);
			if (reportdir == null) {
				return null;
			}
		}
		
		return transformXML(reportdir);
	}
	
	private String transformXML(String dir) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		// xml generieren aus map parameter
		Element root = new org.jdom.Element("mail");
		org.jdom.DocType dt = new org.jdom.DocType("mail");
		org.jdom.Document xmldoc = new org.jdom.Document(root, dt);
		Set<String> s = parameter.keySet();
		Iterator<String> i = s.iterator();
		Element child;
		while (i.hasNext()) {
			String key = (String) i.next();
			child = new org.jdom.Element(key);
			child.setText((String) parameter.get(key));
			root.addContent(child);
		}

		try {
			javax.xml.transform.TransformerFactory tFactory = javax.xml.transform.TransformerFactory
					.newInstance();
			org.jdom.transform.JDOMSource jdSrc = new org.jdom.transform.JDOMSource(
					xmldoc);
			URL url = new URL("file", "", dir);
			InputStream stream = null;
			stream = url.openStream();
			javax.xml.transform.Source xslSource = new javax.xml.transform.stream.StreamSource(
					stream);
			// transformer generieren
			javax.xml.transform.Transformer transformer = tFactory
					.newTransformer(xslSource);
			// transformation ausfuehren,ausgabe in den Outputstream
			transformer.transform(jdSrc,
					new javax.xml.transform.stream.StreamResult(out));
		} catch (Throwable t) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_BEIM_DRUCKEN,
					new Exception(t));
		}

		String sOut = null;
		try {
			sOut = out.toString(OUTPUT_ENCODING);
		} catch (UnsupportedEncodingException ex) {
			sOut = out.toString();
		}

		return sOut;
	}

	/**
	 * addParameter
	 * 
	 * @param parName
	 *            String
	 * @param para
	 *            String
	 */
	public void addParameter(String parName, String para) {
		parameter.put(parName, para);
	}

	/**
	 * getParameter
	 * 
	 * @param parName
	 *            String
	 * @return String
	 */
	public String getParameter(String parName) {
		return (String) parameter.get(parName);
	}

}
