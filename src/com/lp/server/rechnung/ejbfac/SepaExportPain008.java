package com.lp.server.rechnung.ejbfac;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.lp.server.eingangsrechnung.service.SepaExportTransformerFac;
import com.lp.server.finanz.service.BankverbindungDto;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.partner.service.BankDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerbankDto;
import com.lp.server.rechnung.service.LastschriftvorschlagDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.sepa.errors.KeinLastschriftZahlungsziel;
import com.lp.server.rechnung.service.sepa.errors.KeinSepaverzeichnisException;
import com.lp.server.rechnung.service.sepa.errors.KeineBankverbindungException;
import com.lp.server.rechnung.service.sepa.errors.KeineBicException;
import com.lp.server.rechnung.service.sepa.errors.KeineGlaeubigerIdException;
import com.lp.server.rechnung.service.sepa.errors.KeineIbanException;
import com.lp.server.rechnung.service.sepa.errors.KeineIbanMandantBankverbindungException;
import com.lp.server.rechnung.service.sepa.errors.KeineLastschriftBankverbindung;
import com.lp.server.rechnung.service.sepa.errors.KeineMandatsnummerException;
import com.lp.server.rechnung.service.sepa.errors.MandatsnummerAbgelaufenException;
import com.lp.server.rechnung.service.sepa.errors.MandatsnummerKeinDatumException;
import com.lp.server.rechnung.service.sepa.errors.SepaException;
import com.lp.server.rechnung.service.sepa.errors.SepaReException;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.MandantFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.ZahlungszielDto;
import com.lp.server.util.Facade;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

public abstract class SepaExportPain008 extends Facade  {

	private TheClientDto theClientDto;
	private Long creationTimeMillis;
	private List<LastschriftvorschlagDto> lastschriftvorschlagDtos;

	public SepaExportPain008() {
	}

	public Object transform(List<LastschriftvorschlagDto> dtos, 
			TheClientDto theClientDto) throws SepaException {
		myLogger.info("Start der SepaXML-Transformation der Lastschriftvorschl\u00E4ge");
		setTheClientDto(theClientDto);
		setLastschriftvorschlagDtos(dtos);
		setCreationTimeMillis(getTimestamp().getTime());
		
		if (getLastschriftvorschlagDtos().isEmpty()) {
			throw new SepaException("Keine Lastschriftvorschl\u00E4ge vorhanden");
		}
		
		Object transformed = transformImpl();
		myLogger.info("Ende der SepaXML-Transformation der Lastschriftvorschl\u00E4ge");
		return transformed;
	}
	
	public List<SepaException> checkData(List<LastschriftvorschlagDto> dtos,
			TheClientDto theClientDto) {
		setTheClientDto(theClientDto);
		setLastschriftvorschlagDtos(dtos);
		
		SepaDatenValidator validator = new SepaDatenValidator();
		
		return validator.validate();
	}
	
	protected abstract Object transformImpl() throws SepaException;

	public void setTheClientDto(TheClientDto theClientDto) {
		this.theClientDto = theClientDto;
	}
	
	public TheClientDto getTheClientDto() {
		return theClientDto;
	}
	
	public void setCreationTimeMillis(Long creationTimeMillis) {
		this.creationTimeMillis = creationTimeMillis;
	}
	
	public Long getCreationTimeMillis() {
		return creationTimeMillis;
	}
	
	public void setLastschriftvorschlagDtos(
			List<LastschriftvorschlagDto> lastschriftvorschlagDtos) {
		this.lastschriftvorschlagDtos = lastschriftvorschlagDtos;
	}
	
	public List<LastschriftvorschlagDto> getLastschriftvorschlagDtos() {
		return lastschriftvorschlagDtos;
	}
	
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

	public class PartnerBankDaten {
		private PartnerDto partnerDto;
		private PartnerbankDto bankverbindungDto;
		private BankDto bankDto;
		
		public PartnerBankDaten(PartnerDto partnerDto,
				PartnerbankDto bankverbindungDto, BankDto bankDto) {
			setPartnerDto(partnerDto);
			setBankverbindungDto(bankverbindungDto);
			setBankDto(bankDto);
		}
		public PartnerDto getPartnerDto() {
			return partnerDto;
		}
		public void setPartnerDto(PartnerDto partnerDto) {
			this.partnerDto = partnerDto;
		}
		public PartnerbankDto getBankverbindungDto() {
			return bankverbindungDto;
		}
		public void setBankverbindungDto(PartnerbankDto bankverbindungDto) {
			this.bankverbindungDto = bankverbindungDto;
		}
		public BankDto getBankDto() {
			return bankDto;
		}
		public void setBankDto(BankDto bankDto) {
			this.bankDto = bankDto;
		}
		
		public String getIban() {
			return bankverbindungDto.getCIban();
		}
	}
	
	protected class SepaDatenFinder {
		private MandantDto mandantDto;
		
		public MandantDto getMandantDto() throws SepaException {
			if (mandantDto == null) {
				try {
					mandantDto = getMandantFac().mandantFindByPrimaryKeyOhneExc(getTheClientDto().getMandant(), getTheClientDto());
				} catch (RemoteException e) {
					throw new SepaException("Mandant " + getTheClientDto().getMandant() + " konnte nicht gefunden werden.", e);
				}
			}
			return mandantDto;
		}
		
		public RechnungDto getRechnung(Integer rechnungIId) throws SepaException {
			RechnungDto rechnungDto = getRechnungFac().rechnungFindByPrimaryKeyOhneExc(rechnungIId);
			if (rechnungDto == null) {
				throw new SepaException("Rechnung mit iId=" + rechnungIId + " konnte nicht gefunden werden.");
			}
			return rechnungDto;
		}
		
		public PartnerBankDaten getPartnerBankDaten(RechnungDto rechnungDto) throws SepaException {
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(rechnungDto.getKundeIId(), getTheClientDto());
			try {
				return getPartnerBankDatenImpl(kundeDto.getPartnerDto());
			} catch (KeineBankverbindungException e) {
				e.setKundeDto(kundeDto);
				throw e;
			}
		}
		
		private PartnerBankDaten getPartnerBankDatenImpl(PartnerDto partnerDto) throws SepaException {
			PartnerbankDto bvKundeDto = getBankverbindungPartner(partnerDto);
			BankDto bankDto = getBankByPrimaryKey(bvKundeDto.getBankPartnerIId());
			return new PartnerBankDaten(partnerDto, bvKundeDto, bankDto);
		}

		private PartnerbankDto getBankverbindungPartner(PartnerDto partnerDto) throws SepaException {
			try {
				PartnerbankDto[] partnerbank = getBankFac().partnerbankFindByPartnerIIdOhneExc(
						partnerDto.getIId(), getTheClientDto());
				if (partnerbank == null || partnerbank.length == 0) {
					throw new KeineBankverbindungException(null);
				}
				return partnerbank[0];
			} catch (RemoteException e) {
				throw new KeineBankverbindungException(null, e);
			}
		}
		
		public void checkSepaMandat(RechnungDto rechnungDto) throws SepaException {
			int validationCode = getMandantFac().istMandatsreferenzAbgelaufen(rechnungDto.getZahlungszielIId(), 
					rechnungDto.getKundeIId(), rechnungDto.getTBelegdatum(), getTheClientDto());
			if (MandantFac.MANDATSREFERENZ_GUELTIG == validationCode) return;
			
			KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKeyOhneExc(
					rechnungDto.getKundeIId(), getTheClientDto());
			if (MandantFac.MANDATSREFERENZ_KEIN_GUELTIGKEITSDATUM == validationCode) {
				throw new MandatsnummerKeinDatumException(rechnungDto, kundeDto);
			}
			if (MandantFac.MANDATSREFERENZ_KEINE_MANDATSREFERENZNUMMER == validationCode) {
				throw new KeineMandatsnummerException(rechnungDto, kundeDto);
			}
			if (MandantFac.MANDATSREFERENZ_ABGELAUFEN == validationCode) {
				throw new MandatsnummerAbgelaufenException(rechnungDto, kundeDto);
			}

			if (MandantFac.MANDATSREFERENZ_KEINE_GLAEUBIGERNUMMER == validationCode) {
				return;
				// wird an anderer Stelle geprueft
//				throw new KeineGlaeubigerIdException(getMandantDto());
			}

			if (MandantFac.MANDATSREFERENZ_KEIN_BANKVERBINDUNG == validationCode) {
				return;
				// wird an anderer Stelle geprueft
//				throw new KeineBankverbindungException(rechnungDto, kundeDto);
			}
			
			throw new SepaException("Unbekannter Fehlerwert '" + validationCode + "' bei Mandatsnummerpr\u00FCfung");
		}
		
		public String getAuftraggeberreferenz(LastschriftvorschlagDto lvDto) {
			if (lvDto.getCAuftraggeberreferenz() != null) {
				return lvDto.getCAuftraggeberreferenz();
			}
			return getMahnwesenFac().generateCAuftraggeberreferenzAndUpdateLastschriftvorschlag(lvDto, getTheClientDto());
		}
		
		public String getGlaeubigerId() throws SepaException {
			if (getMandantDto().getCGlauebiger() == null) {
				throw new KeineGlaeubigerIdException(getMandantDto());
			}
			return getMandantDto().getCGlauebiger();
		}
		
		public java.sql.Date getLastschriftFaelligkeit(RechnungDto rechnungDto) throws SepaException {
			try {
				java.sql.Date rechnungZieldatum = getMandantFac().berechneZielDatumFuerBelegdatum(
						rechnungDto.getTBelegdatum(), rechnungDto.getZahlungszielIId(), getTheClientDto());
				if (rechnungZieldatum.before(getDate())) {
					rechnungZieldatum = new java.sql.Date(getCreationTimeMillis());
				}
				return Helper.addiereTageZuDatum(rechnungZieldatum, 
						getParameterFac().getLastschriftFaelligkeitNachZahlungsziel(getTheClientDto().getMandant()));
			} catch (RemoteException e) {
				throw new SepaException("Berechnung des F\u00E4lligkeitsdatums von Rechnung " 
						+ rechnungDto.getCNr() + " schlug fehl", e);
			}
		}
		
		public BankverbindungDto getMandantLastschriftBankverbindung() throws SepaException {
			BankverbindungDto bankverbindungDto = getFinanzFac()
					.getBankverbindungFuerSepaLastschriftByMandantOhneExc(getTheClientDto().getMandant());
			if (bankverbindungDto == null) {
				throw new KeineLastschriftBankverbindung(getMandantDto());
			}
			return bankverbindungDto;
		}
		
		public BankDto getBankByPrimaryKey(Integer bankIId)  throws SepaException {
			BankDto bankDto = getBankFac().bankFindByPrimaryKeyOhneExc(bankIId, getTheClientDto());
			if (bankDto == null) {
				throw new SepaException("Unbekannte Bank mit iId=" + bankIId);
			}
			return bankDto;
		}
		
		public String getLastschriftvorschlagSepaExportFilename() throws SepaException, RemoteException {
			String exportFilename = getMahnwesenFac().getLastschriftvorschlagSepaExportFilename(getTheClientDto());
			if (exportFilename != null) return exportFilename;
			
			BankverbindungDto bankverbindungDto = getMandantLastschriftBankverbindung();
			KontoDto kontoDto = getFinanzFac().kontoFindByPrimaryKey(bankverbindungDto.getKontoIId());
			throw new KeinSepaverzeichnisException(bankverbindungDto, kontoDto);
		}
		

		public String getVerwendungszweck(LastschriftvorschlagDto lvDto, RechnungDto rechnungDto) {
			if (!Helper.isStringEmpty(lvDto.getCVerwendungszweck()))
				return lvDto.getCVerwendungszweck();
			
			return getMahnwesenFac().getDefaultLastschriftVerwendungszweck(rechnungDto, getTheClientDto());
		}
	}
	
	protected class SepaDatenValidator {
		private SepaDatenFinder sepaDatenFinder;
		
		public void setSepaDatenFinder(SepaDatenFinder sepaDatenFinder) {
			this.sepaDatenFinder = sepaDatenFinder;
		}
		
		private SepaDatenFinder getSepaDatenFinder() {
			if (sepaDatenFinder == null) {
				sepaDatenFinder = new SepaDatenFinder();
			}
			return sepaDatenFinder;
		}
		
		public List<SepaException> validate() {
			List<SepaException> sepaErrors = new ArrayList<SepaException>();
			validateGlaubigerDaten(sepaErrors);
			
			for (LastschriftvorschlagDto dto : getLastschriftvorschlagDtos()) {
				RechnungDto rechnungDto = validateRechnung(dto, sepaErrors);
				if (rechnungDto == null) continue;
				
				validateZahlungsziel(rechnungDto, sepaErrors);
				validateSepaMandat(rechnungDto, sepaErrors);
				validateKundeBankverbindung(rechnungDto, sepaErrors);
			}
			return sepaErrors;
		}
		
		private void validateKundeBankverbindung(RechnungDto rechnungDto,
				List<SepaException> sepaErrors) {
			try {
				PartnerBankDaten bvDaten = getSepaDatenFinder().getPartnerBankDaten(rechnungDto);
				if (bvDaten.getBankverbindungDto().getCIban() == null) {
					KundeDto kundeDto = getKundeFac().kundeFindByPrimaryKey(
							rechnungDto.getKundeIId(), getTheClientDto());
					sepaErrors.add(new KeineIbanException(rechnungDto, kundeDto));
				}
				if (bvDaten.getBankDto().getCBic() == null) {
					sepaErrors.add(new KeineBicException(rechnungDto, bvDaten.getBankDto()));
				}
			} catch (SepaReException e) {
				e.setRechnungDto(rechnungDto);
				sepaErrors.add(e);
			} catch (SepaException e) {
				sepaErrors.add(e);
			}
		}

		private void validateSepaMandat(RechnungDto rechnungDto,
				List<SepaException> sepaErrors) {
			try {
				getSepaDatenFinder().checkSepaMandat(rechnungDto);
			} catch (SepaException e) {
				sepaErrors.add(e);
			}
		}

		private void validateZahlungsziel(RechnungDto rechnungDto, List<SepaException> sepaErrors) {
			try {
				ZahlungszielDto zielDto = getMandantFac().zahlungszielFindByPrimaryKey(
						rechnungDto.getZahlungszielIId(), getTheClientDto());
				if (!Helper.short2boolean(zielDto.getBLastschrift())) {
					sepaErrors.add(new KeinLastschriftZahlungsziel(rechnungDto));
				}
			} catch (RemoteException e) {
				sepaErrors.add(new SepaException(e));
			}
		}
		
		private RechnungDto validateRechnung(LastschriftvorschlagDto dto, List<SepaException> sepaErrors) {
			try {
				return getSepaDatenFinder().getRechnung(dto.getRechnungIId());
			} catch (SepaException e) {
				sepaErrors.add(e);
				return null;
			}
		}

		private void validateGlaubigerDaten(List<SepaException> sepaErrors) {
			try {
				getSepaDatenFinder().getGlaeubigerId();
			} catch (SepaException e) {
				sepaErrors.add(e);
			}
			
			try {
				BankverbindungDto bvDto = getSepaDatenFinder().getMandantLastschriftBankverbindung();
				if (bvDto.getCIban() == null) {
					KontoDto kontoDto = getFinanzFac().kontoFindByPrimaryKey(bvDto.getKontoIId());
					sepaErrors.add(new KeineIbanMandantBankverbindungException(bvDto, kontoDto));
				}
				getSepaDatenFinder().getLastschriftvorschlagSepaExportFilename();
			} catch (SepaException e) {
				sepaErrors.add(e);
			} catch (RemoteException e) {
				sepaErrors.add(new SepaException(e));
			} 
		}
	}
}
