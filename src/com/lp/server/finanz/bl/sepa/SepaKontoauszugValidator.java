package com.lp.server.finanz.bl.sepa;

import java.util.ArrayList;
import java.util.List;

import com.lp.server.finanz.service.SepaBetrag;
import com.lp.server.finanz.service.SepaBuchung;
import com.lp.server.finanz.service.SepaKontoauszug;
import com.lp.server.finanz.service.SepaZahlung;
import com.lp.util.EJBExceptionLP;
import com.lp.util.EJBSepaImportExceptionLP;

public abstract class SepaKontoauszugValidator {

	protected List<EJBSepaImportExceptionLP> errors;
	
	public SepaKontoauszugValidator() {
		errors = new ArrayList<EJBSepaImportExceptionLP>();
	}

	public List<EJBSepaImportExceptionLP> validate(List<SepaKontoauszug> ktoauszuege) {
		validateCommon(ktoauszuege);
		validateSpecific(ktoauszuege);
		
		return errors;
	}
	
	private void validateCommon(List<SepaKontoauszug> ktoauszuege) {
		for (SepaKontoauszug ktoauszug : ktoauszuege) {
			hasIban(ktoauszug);
			
			if (hasBuchungen(ktoauszug)) {
				for (SepaBuchung buchung : ktoauszug.getBuchungen()) {
					hasBuchungsdetails(ktoauszug, buchung);
					if (hasZahlungen(ktoauszug, buchung)) {
						for (SepaZahlung zahlung : buchung.getZahlungen()) {
							hasZahlungsDetails(ktoauszug, zahlung);
						}
					}
				}
			}
		}
	}
	
	protected void addNullOrEmptyError(SepaKontoauszug ktoauszug, String field) {
		errors.add(new EJBSepaImportExceptionLP(
				EJBExceptionLP.FEHLER_SEPAIMPORT_FELD_NULL_ODER_LEER, EJBSepaImportExceptionLP.SEVERITY_ERROR,
				field, ktoauszug.getAuszugsnr(), ktoauszug.getElektronischeAuszugsnr()));
	}

	protected abstract void validateSpecific(List<SepaKontoauszug> ktoauszuege);
	
	protected boolean hasIban(SepaKontoauszug ktoauszug) {
		if (ktoauszug.getKontoInfo() != null && ktoauszug.getKontoInfo().getIban() != null) return true;
		
		addNullOrEmptyError(ktoauszug, "IBAN Bankverbindung");
		return false;
	}
	
	protected boolean hasSalden(SepaKontoauszug ktoauszug) {
		if (ktoauszug.hatSalden()) return true;
		
		addNullOrEmptyError(ktoauszug, "Salden");
		return false;
	}
	
	protected boolean hasBuchungen(SepaKontoauszug ktoauszug) {
		return ktoauszug.getBuchungen() != null && !ktoauszug.getBuchungen().isEmpty();
	}
	
	protected boolean hasBuchungsdetails(SepaKontoauszug ktoauszug, SepaBuchung buchung) {
		boolean hasError = false;
		if (buchung.getValutadatum() == null && buchung.getBuchungsdatum() != null) {
			addNullOrEmptyError(ktoauszug, "Valutadatum || Buchungsdatum");
			hasError = true;
		}
		
		if (buchung.getBetrag() == null) {
			addNullOrEmptyError(ktoauszug, "Buchungsbetrag");
			hasError = true;
		}
		
		return hasError;
	}
	
	protected boolean hasZahlungen(SepaKontoauszug ktoauszug, SepaBuchung buchung) {
		if (buchung.getZahlungen() != null && !buchung.getZahlungen().isEmpty()) return true;
		
		if (buchung.getBetrag().getWert().signum() == 0) return true;
		
		addNullOrEmptyError(ktoauszug, "Zahlungen");
		return false;
	}
	
	protected boolean hasZahlungsDetails(SepaKontoauszug ktoauszug, SepaZahlung zahlung) {
		if (zahlung.getBetrag() != null) return true;
		
		addNullOrEmptyError(ktoauszug, "Zahlungsbetrag");
		return false;
	}
	
	protected boolean validateBetraegeUndSalden(SepaKontoauszug ktoauszug) {
		SepaBetrag betrag = ktoauszug.getStartSaldo().getBetrag();
		for (SepaBuchung buchung : ktoauszug.getBuchungen()) {
			betrag = betrag.add(buchung.getBetrag());
		}
		
		if (betrag.getPlusMinusWert().compareTo(ktoauszug.getEndSaldo().getBetrag().getPlusMinusWert()) == 0) return true;
		
		errors.add(new EJBSepaImportExceptionLP(
				EJBExceptionLP.FEHLER_SEPAIMPORT_SALDENBETRAEGE_FEHLERHAFT, EJBSepaImportExceptionLP.SEVERITY_ERROR,
				betrag.getPlusMinusWert(), ktoauszug.getEndSaldo().getBetrag().getPlusMinusWert()));
		return false;
	}
}
