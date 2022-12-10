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
package com.lp.server.eingangsrechnung.bl;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.eingangsrechnung.service.ErZahlungsempfaenger;
import com.lp.server.eingangsrechnung.service.SepaExportTransformerFac;
import com.lp.server.eingangsrechnung.service.ZahlungsvorschlagDto;
import com.lp.server.eingangsrechnung.service.ZahlungsvorschlaglaufDto;
import com.lp.server.partner.service.BankDto;
import com.lp.server.partner.service.LieferantDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.system.ejbfac.HvCreatingCachingProvider;
import com.lp.server.system.service.LandDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.EingangsrechnungId;
import com.lp.server.util.Facade;
import com.lp.server.util.Validator;
import com.lp.util.BICValidator;
import com.lp.util.EJBExceptionLP;
import com.lp.util.EJBSepaExportExceptionLP;
import com.lp.util.Helper;

/**
 * Klasse zur Transformierung von Elementen eines Zahlungsvorschlags in
 * eine SEPA-XML-Klassenstruktur, fuer das anschliessende Marshalling
 * der Objekte in ein XML-File
 * 
 * @author andi
 *
 */
public abstract class SepaExportTransformer extends Facade  {
	
	private TheClientDto theClientDto;
	private Long creationTimeMillis;
	private ZahlungsvorschlaglaufDto laufDto;
	private List<ZahlungsvorschlagDto> zahlungsvorschlagDtos;
	private SepaExportDataValidator dataValidator;
	private List<EJBSepaExportExceptionLP> exportErrors;
	private EingangsrechnungCache eingangsrechnungCache;
	private LieferantCache lieferantCache;
	private LandCache landCache;

	protected SepaExportTransformer() {
	}

	public Object transform(ZahlungsvorschlaglaufDto zvlaufDto,
			ZahlungsvorschlagDto[] zvDtos, TheClientDto theClientDto) 
					throws EJBExceptionLP, RemoteException {
		exportErrors = null;
		setLaufDto(zvlaufDto);
		setZahlungsvorschlagDtos(zvDtos);
		
		if (!getZahlungsvorschlagDtos().isEmpty()) {
			return transformImpl(theClientDto);
		}

		if (isSepa()) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_SEPAEXPORT_KEINE_EURO_BELEGE, 
					"Keine Euro-Belege zu exportieren.");
		}
		throw new EJBExceptionLP(EJBExceptionLP.FEHLER_SEPAEXPORT_KEINE_BELEGE, 
				"Keine Belege zu exportieren.");
	}

	/**
	 * @param theClientDto
	 * @return
	 * @throws RemoteException
	 */
	private Object transformImpl(TheClientDto theClientDto)
			throws RemoteException {
		setTheClientDto(theClientDto);
		setCreationTimeMillis(getTimestamp().getTime());

		myLogger.info("Beginn der Sepa-Transformation, Erzeugung der XML-Objekte");
		Object transformedObject = transformImpl();
		
		if (getExportErrors().size() > 0) {
			throw new EJBSepaExportExceptionLP(getExportErrors());
		}
		myLogger.info("Ende der Sepa-Transformation. Anzahl der aufgetretenen Errors: " + getExportErrors().size());
		return transformedObject;
	}
	
	protected abstract Object transformImpl() throws EJBExceptionLP, RemoteException ;
	
	/**
	 * Konvertiert eine Zeit (in Millisekunden) in eine XMLGregorianCalendar-Darstellung
	 * nach uebergebenem Datumsformat
	 * 
	 * @param timeMillis Zeit in Millisekunden
	 * @param dateFormat gewuenschtes Datenformat
	 * @return einen XMLGregorianCalendar
	 */
	protected XMLGregorianCalendar convertToXmlGregorian(long timeMillis, String dateFormat) {
		SimpleDateFormat creDtTmFormat = new SimpleDateFormat(dateFormat);
		Date dt = new Date(timeMillis);
		
		try {
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(dt);
			TimeZone timeZone = TimeZone.getTimeZone(SepaExportTransformerFac.TIMEZONE_SEPA);
			cal.setTimeZone(timeZone);
			return DatatypeFactory.newInstance().newXMLGregorianCalendar(creDtTmFormat.format(cal.getTime()));
		} catch (Exception e) {
			myLogger.error("Error bei der Erstellung eines XMLGregorianCalendars. DateFormat: " + dateFormat
					+ ", Date: " + dt, e);
			throw new EJBExceptionLP(e);
		}

	}
	
	/**
	 * Setzt die Lieferantenrechnungsnummer und die Eingangsrechnungsnummer
	 * zu einem String zusammen. Ist die Lieferantenrechnungsnummer null oder 
	 * leer wird nur die Eingangsrechnungsnummer retourniert.
	 * 
	 * @param liefReCnr Lieferantenrechnungsnummer
	 * @param erCnr Eingangsrechnungsnummer
	 * @return zusammengesetzter String der beiden Nummern mit "|" als
	 * Trennzeichen
	 */
	protected String getPaymentIdentification(
			EingangsrechnungDto erDto, ZahlungsvorschlagDto zvDto) {
		
		StringBuilder pmtId = new StringBuilder();
		
		if (!Helper.isStringEmpty(erDto.getCLieferantenrechnungsnummer())) {
			pmtId.append(erDto.getCLieferantenrechnungsnummer())
				.append(SepaExportTransformerFac.DELIMITER_RMTINF_USTRD);
		}
		pmtId.append(erDto.getCNr());
		
		if (BigDecimal.ZERO.compareTo(zvDto.getNAngewandterskontosatz()) != 0) {
			pmtId.append(SepaExportTransformerFac.DELIMITER_RMTINF_USTRD)
				.append(erDto.getWaehrungCNr()).append(" ")
				.append(zvDto.getNErBruttoBetrag()).append(" ")
				.append("-").append(zvDto.getNAngewandterskontosatz().stripTrailingZeros().toPlainString())
				.append("% ").append(getTextRespectUISpr("lp.skonto", getTheClientDto().getMandant(), 
						getTheClientDto().getLocUi()));
		}
		
		return Helper.cutString(pmtId.toString(), SepaExportTransformerFac.XMLLength.Max140Text);
	}

	protected String getEndToEndId(ZahlungsvorschlagDto zvDto) {
		if (zvDto.getCAuftraggeberreferenz() != null) {
			//ist schon eine vorhanden, bestehende verwenden!
			return zvDto.getCAuftraggeberreferenz();
		}
		
		try {
			return getZahlungsvorschlagFac().generateCAuftraggeberreferenzAndUpdateZV(zvDto, theClientDto);
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
		return null;
	}

	protected void setTheClientDto(TheClientDto theClientDto) {
		this.theClientDto = theClientDto;
	}

	protected void setLaufDto(ZahlungsvorschlaglaufDto laufDto) {
		this.laufDto = laufDto;
	}

	protected void setZahlungsvorschlagDtos(
			ZahlungsvorschlagDto[] zvDtos) throws RemoteException {
		zahlungsvorschlagDtos = new ArrayList<ZahlungsvorschlagDto>();
		List<ZahlungsvorschlagDto> keineEuroZvDtos = new ArrayList<ZahlungsvorschlagDto>();
		
		for (ZahlungsvorschlagDto zvDto : zvDtos) {
			if (!Helper.short2Boolean(zvDto.getBBezahlen())) continue;
			// nur wenn bezahlen = true
			EingangsrechnungDto erDto = findER(zvDto.getEingangsrechnungIId());
			if (acceptEingangsrechnung(erDto)) {
				zahlungsvorschlagDtos.add(zvDto);
			} else {
				keineEuroZvDtos.add(zvDto);
			}
		}
		updateBBezahlenOfZahlungsvorschlaege(keineEuroZvDtos, false);
	}

	private void updateBBezahlenOfZahlungsvorschlaege(
			List<ZahlungsvorschlagDto> zahlungsvorschlaege, boolean bValue) throws RemoteException {
		for (ZahlungsvorschlagDto zvDto : zahlungsvorschlaege) {
			zvDto.setBBezahlen(Helper.boolean2Short(bValue));
			getZahlungsvorschlagFac().updateZahlungsvorschlag(zvDto, getTheClientDto());
		}
	}
	
	protected abstract boolean acceptEingangsrechnung(EingangsrechnungDto erDto);

	protected void setCreationTimeMillis(Long creationTimeMillis) {
		this.creationTimeMillis = creationTimeMillis;
	}
	
	protected SepaExportDataValidator getDataValidator() {
		if(dataValidator == null) {
			dataValidator = new SepaExportDataValidator();
		}
		return dataValidator;
	}

	protected TheClientDto getTheClientDto() {
		return theClientDto;
	}

	protected Long getCreationTimeMillis() {
		return creationTimeMillis;
	}

	protected ZahlungsvorschlaglaufDto getLaufDto() {
		return laufDto;
	}

	protected List<ZahlungsvorschlagDto> getZahlungsvorschlagDtos() {
		return zahlungsvorschlagDtos;
	}
	
	protected void addExportErrors(EJBSepaExportExceptionLP ex) {
		getExportErrors().add(ex);
	}

	protected List<EJBSepaExportExceptionLP> getExportErrors() {
		if(exportErrors == null) {
			exportErrors = new ArrayList<EJBSepaExportExceptionLP>();
		}
		return exportErrors;
	}

	protected EingangsrechnungCache getEingangsrechnungCache() {
		if (eingangsrechnungCache == null) {
			eingangsrechnungCache = new EingangsrechnungCache();
		}
		return eingangsrechnungCache;
	}
	
	protected EingangsrechnungDto findER(Integer eingangsrechnungId) {
		return getEingangsrechnungCache().getValueOfKey(eingangsrechnungId);
	}
	
	protected LieferantCache getLieferantCache() {
		if (lieferantCache == null) {
			lieferantCache = new LieferantCache();
		}
		return lieferantCache;
	}
	
	protected LieferantDto findLieferant(Integer lieferantId) {
		return getLieferantCache().getValueOfKey(lieferantId);
	}
	
	protected LandCache getLandCache() {
		if (landCache == null) {
			landCache = new LandCache();
		}
		return landCache;
	}
	
	protected LandDto findLand(Integer landId) {
		return getLandCache().getValueOfKey(landId);
	}

	class EingangsrechnungCache extends HvCreatingCachingProvider<Integer, EingangsrechnungDto> {
		protected EingangsrechnungDto provideValue(Integer key, Integer transformedKey) {
			try {
				return getEingangsrechnungFac().eingangsrechnungFindByPrimaryKeyOhneExc(key);
			} catch (RemoteException e) {
				throwEJBExceptionLPRespectOld(e);
			}
			return null;
		}
	};
	
	class LieferantCache extends HvCreatingCachingProvider<Integer, LieferantDto> {
		protected LieferantDto provideValue(Integer key, Integer transformedKey) {
			return getLieferantFac().lieferantFindByPrimaryKey(key, getTheClientDto());
		}
	}
	
	class LandCache extends HvCreatingCachingProvider<Integer, LandDto> {
		protected LandDto provideValue(Integer key, Integer transformedKey) {
			return getSystemFac().landFindByPrimaryKey(key);
		}
	}

	protected ErZahlungsempfaenger getErBankverbindung(EingangsrechnungDto erDto) {
		try {
			ErZahlungsempfaenger erBv = getEingangsrechnungFac().getErZahlungsempfaenger(
					new EingangsrechnungId(erDto.getIId()), getTheClientDto());
			if (erBv.isAbweichend() && !erBv.exists()) {
				getExportErrors().add(new EJBSepaExportExceptionLP(erDto, erBv.getPartnerDto(), 
						EJBExceptionLP.FEHLER_SEPAEXPORT_ER_MIT_ABWEICHENDER_BANKVERBINDUNG_HAT_KEINE));
				return null;
			} else if (!erBv.exists()) {
				getExportErrors().add(new EJBSepaExportExceptionLP(erDto, erBv.getPartnerDto(), 
						EJBExceptionLP.FEHLER_SEPAEXPORT_LF_HAT_KEINE_BANKVERBINDUNG));
				return null;
			}
			return erBv;
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
		
		return null;
	}

	protected BankDto findBank(Integer partnerIIdBank) {
		try {
			return getBankFac().bankFindByPrimaryKey(partnerIIdBank, getTheClientDto());
		} catch (RemoteException e) {
			throwEJBExceptionLPRespectOld(e);
		}
		return null;
	}
	
	protected abstract boolean isSepa();
	protected abstract boolean isSwiss();

	/**
	 * Innere Klasse zum Validieren der benoetigten Daten fuer die Pflichtfelder
	 * des SEPA-Zahlungsauftrages. Sind fuer AT, DE, CH gleich.
	 * 
	 * @author andi
	 *
	 */
	class SepaExportDataValidator {

		private BICValidator bicValidator;
		
		public SepaExportDataValidator() {
			bicValidator = new BICValidator();
		}
		
		public BICValidator getBicValidator() {
			return bicValidator;
		}
		
		/**
		 * Validiert ueber die Kopfdaten des Zahlungsauftrags, die fuer den Exports der
		 * Sepa-Datei notwendig sind.
		 * 
		 * @param auftraggeberName Name des Auftraggebers
		 * @param cIban IBAN des Auftraggebers
		 * @param cBic BIC des Auftraggebers
		 */
		public void validateHeaderData(String auftraggeberName, String cIban, String cBic) {
			try {
				Validator.notEmpty(auftraggeberName, "Name des Auftraggebers");
			} catch (EJBExceptionLP ex) {
				EJBSepaExportExceptionLP ejbEx = new EJBSepaExportExceptionLP(ex);
				getExportErrors().add(ejbEx);
				myLogger.error("Name des Aufttraggebers ist null oder leer: " + auftraggeberName, ejbEx);
			}
			try {
				Validator.notEmpty(cIban, "IBAN des Auftraggebers");
			} catch (EJBExceptionLP ex) {
				EJBSepaExportExceptionLP ejbEx = new EJBSepaExportExceptionLP(ex);
				getExportErrors().add(ejbEx);
				myLogger.error("IBAN des Aufttraggebers ist null oder leer: " + cIban, ejbEx);
			}
			try {
				Validator.notEmpty(cBic, "BIC des Auftraggebers");
			} catch (EJBExceptionLP ex) {
				EJBSepaExportExceptionLP ejbEx = new EJBSepaExportExceptionLP(ex);
				getExportErrors().add(ejbEx);
				myLogger.error("BIC des Aufttraggebers ist null oder leer: " + cBic, ejbEx);
			}
		}
		
		/**
		 * Validiert ueber die Daten der Zahlung, die fuer den Export der Sepa-Datei
		 * notwendig sind.
		 * 
		 * @param nZahlbetrag Betrag des Zahlungsauftrags
		 * @param cIban IBAN des Empfaengers
		 * @param bankLiefDto Information der Bank des Lieferanten
		 * @param liefPartnerDto Information des Lieferanten
		 * @param erDto Information der Eingangsrechnung
		 */
		public void validatePaymentData(BigDecimal nZahlbetrag, String cIban, BankDto bankLiefDto,
				PartnerDto partnerDtoEmpfaenger, EingangsrechnungDto erDto) {
			try {
				Validator.notNull(nZahlbetrag, "Zahlbetrag");
			} catch (EJBExceptionLP ex) {
				EJBSepaExportExceptionLP ejbEx = new EJBSepaExportExceptionLP(erDto, partnerDtoEmpfaenger, ex);
				getExportErrors().add(ejbEx);
				myLogger.error("Zahlbetrag ist null oder leer", ejbEx);
			}
			try {
				Validator.notEmpty(partnerDtoEmpfaenger.getCName1nachnamefirmazeile1(), "Name des Empf\u00E4ngers");
			} catch (EJBExceptionLP ex) {
				EJBSepaExportExceptionLP ejbEx = new EJBSepaExportExceptionLP(erDto, partnerDtoEmpfaenger, ex);
				getExportErrors().add(ejbEx);
				myLogger.error("Name des Empf\\u00E4ngers ist null oder leer: " 
						+ partnerDtoEmpfaenger.getCName1nachnamefirmazeile1(), ejbEx);
			}
			try {
				Validator.notEmpty(cIban, "IBAN des Empf\u00E4ngers");
			} catch (EJBExceptionLP ex) {
				EJBSepaExportExceptionLP ejbEx = new EJBSepaExportExceptionLP(erDto, partnerDtoEmpfaenger, 
						EJBExceptionLP.FEHLER_SEPAEXPORT_KEINE_IBAN_VORHANDEN);
				getExportErrors().add(ejbEx);
				myLogger.error("IBAN des Empf\\u00E4ngers ist null oder leer: " + cIban, ejbEx);
			}
			
			if(bankLiefDto.getPartnerDto().getLandplzortDto() == null) {
				EJBSepaExportExceptionLP ejbEx = new EJBSepaExportExceptionLP(erDto, bankLiefDto.getPartnerDto(), 
						EJBExceptionLP.FEHLER_SEPAEXPORT_BANK_HAT_KEINEN_ORT);
				getExportErrors().add(ejbEx);
				myLogger.error("Bank des Lieferanten hat keinen Ort definiert. Bank-IId Lieferant: " 
						+ bankLiefDto.getPartnerIId(), ejbEx);
				return;
			}
			
			if(isSepa()
					&& Helper.short2boolean(bankLiefDto.getPartnerDto().getLandplzortDto().getLandDto().getBSepa()) != true) {
				EJBSepaExportExceptionLP ejbEx = new EJBSepaExportExceptionLP(erDto, bankLiefDto.getPartnerDto(), 
						EJBExceptionLP.FEHLER_SEPAEXPORT_BANK_AUS_NICHT_SEPA_LAND);
				getExportErrors().add(ejbEx);
				myLogger.error("Bank ist aus Nicht-SEPA-Land. LKZ: " 
						+ bankLiefDto.getPartnerDto().getLandplzortDto().getLandDto().getCLkz(), ejbEx);
			}
			
			try {
				Validator.notEmpty(bankLiefDto.getCBic(), "BIC des Empf\u00E4ngers");
			} catch (EJBExceptionLP ex) {
				EJBSepaExportExceptionLP ejbEx = new EJBSepaExportExceptionLP(erDto, partnerDtoEmpfaenger, 
						EJBExceptionLP.FEHLER_SEPAEXPORT_KEINE_BIC_VORHANDEN);
				getExportErrors().add(ejbEx);
				myLogger.error("BIC des Empf\\u00E4ngers ist null oder leer: ", ejbEx);
			}
			
			if (!getBicValidator().isValidBIC(bankLiefDto.getCBic())) {
				EJBSepaExportExceptionLP ejbEx = new EJBSepaExportExceptionLP(erDto, partnerDtoEmpfaenger, 
						EJBExceptionLP.FEHLER_SEPAEXPORT_BIC_UNGUELTIG);
				getExportErrors().add(ejbEx);
				myLogger.error("BIC des Empf\\u00E4ngers ist ung\\u00fcltig: ", ejbEx);
			}
		}
		
	}
	
}