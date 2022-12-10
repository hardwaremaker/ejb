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

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.XMLGregorianCalendar;

import com.lp.server.finanz.service.ISepaCamtFormat;
import com.lp.server.finanz.service.SepaBetrag;
import com.lp.server.finanz.service.SepaBuchung;
import com.lp.server.finanz.service.SepaHabenBetrag;
import com.lp.server.finanz.service.SepaImportProperties;
import com.lp.server.finanz.service.SepaKontoauszug;
import com.lp.server.finanz.service.SepaSollBetrag;
import com.lp.server.finanz.service.SepaSpesen;
import com.lp.server.finanz.service.SepaZahlung;
import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;
import com.lp.util.EJBExceptionLP;
import com.lp.util.EJBSepaImportExceptionLP;

/**
 * Diese Klasse kuemmert sich um die Umwandlung der Daten aus den 
 * Sepa-XML-Klassen in weiterverarbeitbare Klassen fuer die Benutzung
 * im Helium.
 * 
 * @author andi
 *
 */
public abstract class SepaImportTransformer implements ISepaXMLFieldValidatorCamt053 {

	protected String lkz;
	protected ISepaCamtFormat camtFormat;
	protected ICountrySpecificSchemeValidator countryValidator;
	protected Map<String,ICountrySpecificSchemeValidator> countryValidatorMap;
	private List<EJBSepaImportExceptionLP> transformWarnings;
	private ILPLogger logger;
	private SepaImportProperties importProperties;
	
	public SepaImportTransformer(String lkz) {
		transformWarnings = new ArrayList<EJBSepaImportExceptionLP>();
		logger = LPLogService.getInstance().getLogger(this.getClass());

		initCountryValidatorMap();
	}

	/**
	 * Initialisiert die Map der laenderspezifischen Validatoren
	 */
	private void initCountryValidatorMap() {
		countryValidatorMap = new HashMap<String, SepaImportTransformer.ICountrySpecificSchemeValidator>();
		countryValidatorMap.put(SepaXmlCamtBase.SEPAXML_CAMT_LKZ_AT, new ATSchemeValidator(this));
		countryValidatorMap.put(SepaXmlCamtBase.SEPAXML_CAMT_LKZ_DE, new DESchemeValidator(this));
		countryValidatorMap.put(SepaXmlCamtBase.SEPAXML_CAMT_LKZ_CH, new CHSchemeValidator(this));
		countryValidatorMap.put(SepaXmlCamtBase.SEPAXML_CAMT_LKZ_LI, new CHSchemeValidator(this));
	}

	public SepaImportProperties getImportProperties() {
		return importProperties;
	}

	/**
	 * Initiert die Transformation der Sepa XML-Klassen in eine lesbare Struktur
	 * Dabei werden die laenderspezifischen Richtlinien validiert.
	 * 
	 * @param sepaDoc Wurzelobjekt des zu transformierenden Sepa Kontoauszugs
	 * @param sepaCamtFormat 
	 * @param importProperties 
	 * @param importWarnings Liste fuer auftretende Warnings
	 * @return die transformierten Sepa Kontoauszuege
	 * @throws EJBSepaImportExceptionLP falls Fehler waehrende der Transformation auftreten
	 */
	public List<SepaKontoauszug> transform(Object sepaDoc, ISepaCamtFormat sepaCamtFormat, 
			SepaImportProperties importProperties, List<EJBSepaImportExceptionLP> importWarnings) 
			throws EJBSepaImportExceptionLP {
		this.camtFormat = sepaCamtFormat;
		this.importProperties = importProperties;
		setSepaDocument(sepaDoc);
		logger.info("Null-Felder Instanzierung abgeschlossen");

		List<SepaKontoauszug> kontoauszuege = new ArrayList<SepaKontoauszug>();
		try {
			kontoauszuege = transformImpl();
			logger.info("Sepa XML-Transformation abgeschlossen");
		} catch (Exception e) {
			logger.error("Transformation der XML-Klassen in Sepa-Kontoauszuege fehlgeschlagen", e);
			throw new EJBSepaImportExceptionLP(EJBSepaImportExceptionLP.SEVERITY_ERROR, 
					new EJBExceptionLP(EJBExceptionLP.FEHLER_SEPAIMPORT_TRANSFORMATION_FEHLGESCHLAGEN, e));
		}
		
		setLkz(kontoauszuege);
		initCountryValidator();
		countryValidator.validate();
		normalizeBetraege(kontoauszuege);
		
		importWarnings.addAll(transformWarnings);
		return kontoauszuege;
	}
	
	private void normalizeBetraege(List<SepaKontoauszug> kontoauszuege) {
		for (SepaKontoauszug ktoauszug : kontoauszuege) {
			for (SepaBuchung buchung : ktoauszug.getBuchungen()) {
				for (SepaZahlung zahlung : buchung.getZahlungen()) {
					if (zahlung.getBetrag() == null) {
						if (buchung.getZahlungen().size() == 1) {
							zahlung.setBetrag(buchung.getBetrag());
						} else {
							continue;
						}
					}
				}
			}
		}
	}
	
	protected SepaBetrag addSpesen(SepaBetrag sepaBetrag, List<SepaSpesen> spesenList) {
		BigDecimal result = sepaBetrag.getWert();
		for (SepaSpesen spesen : spesenList) {
			result = result.add(spesen.getBetrag().getWert());
		}
		return sepaBetrag.isHaben() ? new SepaHabenBetrag(result) : new SepaSollBetrag(result);
	}

	/**
	 * Setzt das zu transformierende Sepa-Dokument in der verwendeten Version
	 * 
	 * @param sepaDoc
	 */
	protected abstract void setSepaDocument(Object sepaDoc);
	
	/**
	 * Transformationsimplementierung der verschiedenen Versionen
	 * 
	 * @return Liste der transformierten Sepa-Kontoauszuege
	 */
	protected abstract List<SepaKontoauszug> transformImpl();
	
	protected void setLkz(List<SepaKontoauszug> ktoauszuege) {
		lkz = ktoauszuege.get(0).getKontoInfo().getIban().trim().substring(0, 2);
	}

	/**
	 * Initialisiert die laenderspezifische Validierung ueber das
	 * Laenderkurzzeichen
	 */
	protected void initCountryValidator() {		
		countryValidator = countryValidatorMap.get(lkz);
		
		if(countryValidator == null) {
			
			countryValidator = new DefaultSchemeValidator();
			EJBSepaImportExceptionLP ex = new EJBSepaImportExceptionLP(camtFormat.toString(), lkz, 
					EJBExceptionLP.FEHLER_SEPAIMPORT_KEIN_VALIDATOR_FUER_LAND_VORHANDEN,
					EJBSepaImportExceptionLP.SEVERITY_WARNING, 
					"Validator fuer Land \"" + lkz + "\" nicht vorhanden");
			transformWarnings.add(ex);
			logger.warn("Validator fuer Land \"" + lkz + "\" nicht vorhanden", ex);
		}
	}
	
	/**
	 * Extrahiert das Datum aus einem {@link XMLGregorianCalendar} und 
	 * wandelt es in ein {@link Date} um.
	 * 
	 * @param xmlCalendar
	 * @return umgewandeltes Date
	 */
	protected Date extractDate(XMLGregorianCalendar xmlCalendar) {
		if(xmlCalendar == null)
			return null;
		
		return new Date(xmlCalendar.toGregorianCalendar().getTimeInMillis());
	}
	
	protected String concatListOfStrings(List<String> strings) {
		Iterator<String> iterator = strings.iterator();
		StringBuilder result = new StringBuilder();
		
		while(iterator.hasNext()) {
			result.append(iterator.next());
		}
		
		return result.toString();
	}
	
	/**
	 * Erstellt einen Haben- oder Sollbetrag
	 * 
	 * @param amt
	 * @param cdtDbtInd
	 * @return Haben- oder Sollbetrag
	 */
	protected SepaBetrag createSepaBetrag(BigDecimal amt, String cdtDbtInd) {
		if(SepaKontoauszug.CREDITOR.equals(cdtDbtInd) && amt != null) {
			return new SepaSollBetrag(amt);
		} else if(SepaKontoauszug.DEBITOR.equals(cdtDbtInd) && amt != null) {
			return new SepaHabenBetrag(amt);
		}
		
		return null;
	}
	
	/**
	 * Interface der laenderspezifischen XML-Schema Validierung
	 * 
	 * @author andi
	 *
	 */
	public interface ICountrySpecificSchemeValidator {
		
		public void validate();
	}
	
	/**
	 * Zusaetzliche XML-Schema Validierung nach den oesterreichischen
	 * Richtlinien
	 * 
	 * @author andi
	 *
	 */
	public class ATSchemeValidator implements ICountrySpecificSchemeValidator {

		private ISepaXMLFieldValidatorCamt053 validator;
		
		public ATSchemeValidator(ISepaXMLFieldValidatorCamt053 v) {
			validator = v;
		}
		
		@Override
		public void validate() {
			try {
				validator.validateMsgRecipient();
			} catch (EJBSepaImportExceptionLP ex) {
				transformWarnings.add(ex);
			}
			try {
				validator.validateLegalSeqNumber();
			} catch (EJBSepaImportExceptionLP ex) {
				transformWarnings.add(ex);
			}
			try {
				validator.validateEntryBankTransactionCode();
			} catch (EJBSepaImportExceptionLP ex) {
				transformWarnings.add(ex);
			}
		}
		
	}
	
	/**
	 * Zusaetzliche XML-Schema Validierung nach den deutschen
	 * Richtlinien
	 * 
	 * @author andi
	 *
	 */
	public class DESchemeValidator implements ICountrySpecificSchemeValidator {

		private ISepaXMLFieldValidatorCamt053 validator;
		
		public DESchemeValidator(ISepaXMLFieldValidatorCamt053 v) {
			validator = v;
		}
		
		@Override
		public void validate() {
			try {
				validator.validateElectronicSeqNumber();
			} catch (EJBSepaImportExceptionLP ex) {
				transformWarnings.add(ex);
			}
			try {
				validator.validateAccountServicer();
			} catch (EJBSepaImportExceptionLP ex) {
				transformWarnings.add(ex);
			}
			try {
				validator.validateTransactionDtlsBankTransactionCode();
			} catch (EJBSepaImportExceptionLP ex) {
				transformWarnings.add(ex);
			}
		}
		
	}
	
	/**
	 * Zusaetzliche XML-Schema Validierung nach den Schweizer
	 * Richtlinien
	 * 
	 * @author andi
	 *
	 */
	public class CHSchemeValidator implements ICountrySpecificSchemeValidator {

		private ISepaXMLFieldValidatorCamt053 validator;
		
		public CHSchemeValidator(ISepaXMLFieldValidatorCamt053 v) {
			validator = v;
		}
		
		@Override
		public void validate() {
			try {
				validator.validateElectronicSeqNumber();
			} catch (EJBSepaImportExceptionLP ex) {
				transformWarnings.add(ex);
			}
			try {
				validator.validateEntryBankTransactionCode();
			} catch (EJBSepaImportExceptionLP ex) {
				transformWarnings.add(ex);
			}
		}
		
	}
	
	/**
	 * Default-Validator, wenn Land noch nicht unterstuetzt wird
	 * 
	 * @author andi
	 *
	 */
	public class DefaultSchemeValidator implements ICountrySpecificSchemeValidator {

		@Override
		public void validate() throws EJBSepaImportExceptionLP {
			// do nothing
		}
		
	}

}
