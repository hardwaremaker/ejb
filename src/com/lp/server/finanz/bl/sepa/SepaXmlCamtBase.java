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
package com.lp.server.finanz.bl.sepa;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.lp.server.finanz.service.ISepaCamtFormat;
import com.lp.server.finanz.service.SepaCamtFormat052;
import com.lp.server.finanz.service.SepaCamtFormat053;
import com.lp.server.finanz.service.SepaCamtVersionEnum;
import com.lp.server.finanz.service.SepaImportProperties;
import com.lp.server.finanz.service.SepaImportSourceData;
import com.lp.server.finanz.service.SepaImportTransformResult;
import com.lp.server.finanz.service.SepaKontoauszug;
import com.lp.server.util.Validator;
import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;
import com.lp.util.EJBExceptionLP;
import com.lp.util.EJBSepaImportExceptionLP;

/**
 * Klasse zur Extrahierung eines Sepa-Kontoauszugs
 * 
 * @author andi
 *
 */
public class SepaXmlCamtBase {

	public static String SEPAXML_NAMESPACE_PREFIX = "urn:iso:std:iso:20022:tech:xsd:";
	public static String SEPAXML_CAMT_053_001 = "camt.053.001.";
	public static String SEPAXML_CAMT_052_001 = "camt.052.001.";
	
	public static String SEPAXML_CAMT_VERSION_02 = "02";
	public static String SEPAXML_CAMT_VERSION_04 = "04";
	public static String SEPAXML_CAMT_VERSION_05 = "05";
	
	public static String SEPAXML_NAMESPACE_CAMT053_VERSION_02 = 
			SEPAXML_NAMESPACE_PREFIX + SEPAXML_CAMT_053_001 + SEPAXML_CAMT_VERSION_02;
	public static String SEPAXML_NAMESPACE_CAMT053_VERSION_05 = 
			SEPAXML_NAMESPACE_PREFIX + SEPAXML_CAMT_053_001 + SEPAXML_CAMT_VERSION_05;
	public static String SEPAXML_NAMESPACE_CAMT052_VERSION_02 = 
			SEPAXML_NAMESPACE_PREFIX + SEPAXML_CAMT_052_001 + SEPAXML_CAMT_VERSION_02;
	
	public static String SEPAXML_CAMT_LKZ_AT = "AT";
	public static String SEPAXML_CAMT_LKZ_DE = "DE";
	public static String SEPAXML_CAMT_LKZ_CH = "CH";
	public static String SEPAXML_CAMT_LKZ_LI = "LI";
	
	private List<EJBSepaImportExceptionLP> importWarnings;
	private ILPLogger logger;
	
	public SepaXmlCamtBase() {
		logger = LPLogService.getInstance().getLogger(this.getClass());
	}
	
	/**
	 * Wandelt das als String uebergebenen XML-Files in XML-Klassen um. Dabei werden die XML-Elemente
	 * ueber das, ueber die Version ermittelte Schema, nach ihrer Gueltigkeit geprueft.
	 * Das Laenderkennzeichen triggert auch noch die Validierung der laenderspezifischen
	 * Richtlinien. 
	 * 
	 * @param importProperties
	 * @return die transformierten Sepa-Kontoauszuege
	 * @throws EJBExceptionLP
	 */
	public SepaImportTransformResult createSepaKontoauszug(SepaImportProperties importProperties) 
			throws EJBExceptionLP {
		
		importWarnings = new ArrayList<EJBSepaImportExceptionLP>();
		List<SepaKontoauszug> sepaKontoauszuege = new ArrayList<SepaKontoauszug>();
		String lkzIban = importProperties.getBankverbindungDto().getCIban().trim().substring(0, 2);
		ISepaCamtFormat camtFormat = null;
		List<SepaImportSourceData> sources = importProperties.getSources();

		for (SepaImportSourceData source : sources) {
			String xmlContent = source.getXml();
			validateInput(xmlContent, lkzIban);
			try {
				Document docXml = DocumentBuilderFactory.newInstance().newDocumentBuilder()
						.parse(new InputSource(new StringReader(xmlContent)));
				ISepaCamtFormat currentCamtFormat = getCamtVersion(docXml);
				checkCamtFormat(camtFormat, currentCamtFormat);
				camtFormat = currentCamtFormat;
				
				logger.info("Gefundenes und gueltiges camt-Format: " + camtFormat.getCamtFormatEnum().getValue() 
						+ "." + camtFormat.getCamtVersionEnum().getValue());
				if (replaceNamespaceIfNeeded(docXml, camtFormat)) {
					xmlContent = docToString(docXml);
				}
				
				SepaXmlUnmarshallerFactory unmarshallerFactory = new SepaXmlUnmarshallerFactory();
				SepaXmlUnmarshaller unmarshaller = unmarshallerFactory.getUnmarshaller(camtFormat);
				Object sepaDoc = unmarshaller.unmarshal(xmlContent);
				
				SepaTransformerFactory transformerFactory = new SepaTransformerFactory();
				SepaImportTransformer transformer = transformerFactory.getImportTransformer(camtFormat, lkzIban);
				
				List<SepaKontoauszug> transformed = transformer.transform(sepaDoc, camtFormat, importProperties, importWarnings);
				sepaKontoauszuege.addAll(transformed);
				addStmtNumbers(source, transformed);
			} catch (EJBSepaImportExceptionLP e) {
				logger.error("EJBSepaImportExceptionLP:", e);
				importWarnings.add(e);
				return new SepaImportTransformResult(new ArrayList<SepaKontoauszug>(), importWarnings);
			} catch (UnmarshalException e) {
				logger.error("UnmarshalException:", e);
				throw new EJBSepaImportExceptionLP(camtFormat.getCamtFormatEnum().getValue(), lkzIban,
						EJBSepaImportExceptionLP.SEVERITY_ERROR, new EJBExceptionLP(e));
			} catch (JAXBException e) {
				logger.error("JAXBException:", e);
				throw new EJBSepaImportExceptionLP(camtFormat.getCamtFormatEnum().getValue(), lkzIban,
						EJBSepaImportExceptionLP.SEVERITY_ERROR, new EJBExceptionLP(e));
			} catch (SAXException e) {
				logger.error("SAXException:", e);
				throw new EJBSepaImportExceptionLP(camtFormat.getCamtFormatEnum().getValue(), lkzIban,
						EJBSepaImportExceptionLP.SEVERITY_ERROR, new EJBExceptionLP(e));
			} catch (Exception ex) {
				throw new EJBSepaImportExceptionLP(EJBSepaImportExceptionLP.SEVERITY_ERROR, new EJBExceptionLP(ex));
			}
		}
		
		SepaKontoauszugValidatorFactory validatorFactory = new SepaKontoauszugValidatorFactory();
		SepaKontoauszugValidator validator = validatorFactory.getValidator(camtFormat);
		importWarnings.addAll(validator.validate(sepaKontoauszuege));
		
		SepaImportTransformResult result = new SepaImportTransformResult(sepaKontoauszuege, importWarnings);
		result.getSources().addAll(sources);
		return result;
	}

	private void addStmtNumbers(SepaImportSourceData source, List<SepaKontoauszug> transformed) {
		for (SepaKontoauszug ktoauszug : transformed) {
			source.getStmtNumbers().add(ktoauszug.getAuszugsnr() != null 
					? ktoauszug.getAuszugsnr().intValue()
					: ktoauszug.getElektronischeAuszugsnr().intValue());
		}
	}

	private void checkCamtFormat(ISepaCamtFormat camtFormat, ISepaCamtFormat currentCamtFormat) {
		if (camtFormat == null) return;
		
		if (camtFormat.getCamtFormatEnum().equals(currentCamtFormat.getCamtFormatEnum()) 
				&& camtFormat.getCamtVersionEnum().equals(currentCamtFormat.getCamtVersionEnum())) return;

		throw new EJBSepaImportExceptionLP(EJBExceptionLP.FEHLER_SEPAIMPORT_UNTERSCHIEDLICHE_CAMT_FORMATE, 
					EJBSepaImportExceptionLP.SEVERITY_ERROR, "");
	}

	/**
	 * Ueberprueft den XML-String und das IBAN-Laenderkennzeichen, ob sie mit
	 * Inhalt gefuellt sind. Das LKZ wird auch auf Gueltigkeit geprueft,
	 * zwei Zeichen [A-Z]
	 * 
	 * @param xmlContent XML-String
	 * @param lkzIban Laenderkennzeichen aus der IBAN
	 */
	private void validateInput(String xmlContent, String lkzIban) {
		try {
			Validator.notEmpty(xmlContent, "xmlcontent, Inhalt der Sepa-XML-Datei");
			Validator.notEmpty(lkzIban, "lkzIban, Laenderkennzeichen der IBAN");
		} catch (EJBExceptionLP e) {
			logger.error("EJBException:", e);
			throw new EJBSepaImportExceptionLP(EJBSepaImportExceptionLP.SEVERITY_ERROR, e);
		}
		
		if(!lkzIban.matches("^[A-Z]{2}")) {
			logger.error("Laenderkennzahl matcht Pattern ^[A-Z]{2} nicht, lkzIban = " + lkzIban);
			importWarnings.add(new EJBSepaImportExceptionLP(EJBExceptionLP.FEHLER_SEPAIMPORT_LAENDERKENNZAHL_PATTERN_FEHLERHAFT, 
					EJBSepaImportExceptionLP.SEVERITY_WARNING, "Laenderkennzahl match Pattern ^[A-Z]{2} nicht, lkzIban = " + lkzIban));
		}
		
		logger.info("Laenderkennzahl erkannt: " + lkzIban);
	}
	
	private ISepaCamtFormat getCamtVersion(Document docXml) {
		Element root = docXml.getDocumentElement();
		String uri = root.getAttribute("xmlns");
		String version = null;
		
		if (uri != null) {
			int index = uri.indexOf(SEPAXML_CAMT_053_001);
			if (index > 0) {
				int begin = index + SEPAXML_CAMT_053_001.length();
				version = uri.substring(begin, begin + 2);
				if (SEPAXML_CAMT_VERSION_04.equals(version)) {
					version = SEPAXML_CAMT_VERSION_05;
				} else if (!SEPAXML_CAMT_VERSION_02.equals(version) && !SEPAXML_CAMT_VERSION_05.equals(version)) {
					throw new EJBSepaImportExceptionLP(version, "", 
							EJBExceptionLP.FEHLER_SEPAIMPORT_CAMT053_VERSION_NICHT_UNTERSTUETZT, 
							EJBSepaImportExceptionLP.SEVERITY_ERROR, "");
				}
				
				ISepaCamtFormat camtFormat = new SepaCamtFormat053();
				camtFormat.setCamtVersionEnum(SepaCamtVersionEnum.fromString(version));
				return camtFormat;
			}
			
			index = uri.indexOf(SEPAXML_CAMT_052_001);
			if (index > 0) {
				int begin = index + SEPAXML_CAMT_052_001.length();
				version = uri.substring(begin, begin + 2);

				if (!SEPAXML_CAMT_VERSION_02.equals(version)) {
					throw new EJBSepaImportExceptionLP(version, "", 
							EJBExceptionLP.FEHLER_SEPAIMPORT_CAMT052_VERSION_NICHT_UNTERSTUETZT, 
							EJBSepaImportExceptionLP.SEVERITY_ERROR, "");
				}
				
				ISepaCamtFormat camtFormat = new SepaCamtFormat052();
				camtFormat.setCamtVersionEnum(SepaCamtVersionEnum.fromString(version));
				return camtFormat;
			}
		}
		
		logger.error("Unbekannte XML-Datei");
		throw new EJBSepaImportExceptionLP(EJBExceptionLP.FEHLER_SEPAIMPORT_UNBEKANNTE_XML_DATEI, 
				EJBSepaImportExceptionLP.SEVERITY_ERROR, "");
	}

	private boolean replaceNamespaceIfNeeded(Document docXml, ISepaCamtFormat camtFormat) {
		Element root = docXml.getDocumentElement();
		String uri = root.getAttribute("xmlns");
		if (SEPAXML_NAMESPACE_CAMT053_VERSION_02.equals(uri) 
				|| SEPAXML_NAMESPACE_CAMT053_VERSION_05.equals(uri)
				|| SEPAXML_NAMESPACE_CAMT052_VERSION_02.equals(uri)) {
			return false;
		}
		
		root.setAttribute("xmlns", SEPAXML_NAMESPACE_PREFIX + camtFormat.toString());
		logger.info("Namespace \"" + uri + "\" wurde durch \"" 
				+ SEPAXML_NAMESPACE_PREFIX + camtFormat.toString() + "\" ersetzt.");
		
		return true;
	}
	
	private String docToString(Document docXml) throws Exception {
		DOMSource domSource = new DOMSource(docXml);
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.transform(domSource, result);
		
		return writer.toString();

	}
	
}
