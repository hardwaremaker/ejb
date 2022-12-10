package com.lp.server.eingangsrechnung.service;

import java.io.Serializable;

import com.lp.server.partner.service.BankDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerbankDto;
import com.lp.server.util.EingangsrechnungId;
import com.lp.server.util.LieferantId;
import com.lp.util.PersonalId;

public class ErZahlungsempfaenger implements Serializable {
	private static final long serialVersionUID = -6562772069040466100L;

	private EingangsrechnungId eingangsrechnungId;
	private LieferantId lieferantId;
	private PersonalId personalIdAbweichend;
	private PartnerDto partnerDtoEmpfaenger;
	private PartnerbankDto partnerbankDtoEmpfaenger;
	private BankDto bankDtoEmpfaenger;
	
	public ErZahlungsempfaenger(EingangsrechnungId eingangsrechnungId) {
		setEingangsrechnungId(eingangsrechnungId);
	}
	
	public EingangsrechnungId getEingangsrechnungId() {
		return eingangsrechnungId;
	}
	public void setEingangsrechnungId(EingangsrechnungId eingangsrechnungId) {
		this.eingangsrechnungId = eingangsrechnungId;
	}
	
	public LieferantId getLieferantId() {
		return lieferantId;
	}
	public void setLieferantId(LieferantId lieferantId) {
		this.lieferantId = lieferantId;
	}
	
	public PersonalId getPersonalIdAbweichend() {
		return personalIdAbweichend;
	}
	public void setPersonalIdAbweichend(PersonalId personalIdAbweichend) {
		this.personalIdAbweichend = personalIdAbweichend;
	}
	
	public boolean isAbweichend() {
		return getPersonalIdAbweichend() != null;
	}
	
	/**
	 * Liefert die gueltige, zu verwendende Bankverbindung der ER
	 * Ist eine abweichende Bankverbindung definiert, handelt es sich um diese Bankverbindung.
	 * Ansonsten wird die Bankverbindung des Lieferanten geliefert.<br>
	 * Somit kann auch <code>null</code> geliefert werden. Zur Pruefung <code>hasPartnerbankDto</code> verwenden.
	 * 
	 * @return PartnerbankDto der gueltigen Bankverbindung der ER, ansonsten <code>null</code>
	 */
	public PartnerbankDto getPartnerbankDto() {
		return partnerbankDtoEmpfaenger;
	}
	public void setPartnerbankDto(PartnerbankDto partnerbankDtoEmpfaenger) {
		this.partnerbankDtoEmpfaenger = partnerbankDtoEmpfaenger;
	}
	
	/**
	 * Liefert die Partnerdaten der gueltigen, zu verwendenden Bankverbindung der ER
	 * 
	 * Ist eine abweichende Bankverbindung definiert, wird der Partner (=Personal) dieser geliefert.
	 * Ansonsten ist es der Partner des Lieferanten.
	 * 
	 * @return Daten des Partners, dem die zu verwendende Bankverbindung zugehoerig ist
	 */
	public PartnerDto getPartnerDto() {
		return partnerDtoEmpfaenger;
	}
	public void setPartnerDto(PartnerDto partnerDtoEmpfaenger) {
		this.partnerDtoEmpfaenger = partnerDtoEmpfaenger;
	}
	
	/**
	 * Liefert die Bankdaten der gueltigen zu verwendenen Bankverbindung der ER
	 * 
	 * @return BankDto der gueltigen Bankverbindung der ER
	 */
	public BankDto getBankDto() {
		return bankDtoEmpfaenger;
	}
	public void setBankDto(BankDto bankDtoEmpfaenger) {
		this.bankDtoEmpfaenger = bankDtoEmpfaenger;
	}
	
	/**
	 * Ist fuer diese ER eine Bankverbindung bekannt?
	 * 
	 * Ist eine abweichende Bankverbindung definiert, wird geprueft, ob das jeweilige
	 * hinterlegte Personal eine Bankverbindung hat.<br>
	 * Ansonsten wird geprueft, ob der Lieferant eine Bankverbindung hat.
	 * 
	 * @return <code>true</code>, wenn eine Bankverbindung bekannt ist
	 */
	public boolean exists() {
		return getPartnerbankDto() != null;
	}
}
