package com.lp.server.rechnung.service.sepa.errors;

import java.rmi.RemoteException;

import com.lp.server.partner.service.KundeDto;
import com.lp.server.rechnung.service.RechnungDto;

public class KeineBankverbindungException extends SepaReException implements ISepaReKundeException {
	private static final long serialVersionUID = 3323896570534247438L;

	private KundeDto kundeDto;
	
	public KeineBankverbindungException(RechnungDto rechnungDto, KundeDto kundeDto) {
		this (rechnungDto, kundeDto, null);
	}
	
	public KeineBankverbindungException(RechnungDto rechnungDto, KundeDto kundeDto, Throwable throwable) {
		super(rechnungDto, "Keine Bankverbindung von '" + (kundeDto != null ? 
				kundeDto.getPartnerDto().getCName1nachnamefirmazeile1() : "null") + "' gefunden.", throwable);
		setKundeDto(kundeDto);
		setCode(SepaExceptionCode.KEINE_BANKVERBINDUNG);
	}

	public KeineBankverbindungException(KundeDto kundeDto) {
		this(null, kundeDto, null);
	}

	public KeineBankverbindungException(KundeDto kundeDto, RemoteException e) {
		this(null, kundeDto, e);
	}
	
	public void setKundeDto(KundeDto kundeDto) {
		this.kundeDto = kundeDto;
	}
	
	public KundeDto getKundeDto() {
		return kundeDto;
	}
}
