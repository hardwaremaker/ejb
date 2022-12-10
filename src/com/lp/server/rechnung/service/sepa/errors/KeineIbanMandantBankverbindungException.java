package com.lp.server.rechnung.service.sepa.errors;

import com.lp.server.finanz.service.BankverbindungDto;
import com.lp.server.finanz.service.KontoDto;

public class KeineIbanMandantBankverbindungException extends SepaException implements ISepaBankverbindungException {
	private static final long serialVersionUID = 8254455733010202043L;

	private BankverbindungDto bankverbindungDto;
	private KontoDto kontoDto;
	
	public KeineIbanMandantBankverbindungException(BankverbindungDto bankverbindungDto, KontoDto kontoDto) {
		super("Kein IBAN definiert in Bankverbindung mit Sachkontonummer '" + kontoDto.getCNr());
		setBankverbindungDto(bankverbindungDto);
		setKontoDto(kontoDto);
		setCode(SepaExceptionCode.KEINE_IBAN_MANDANT_BANKVERBINDUNG);
	}

	@Override
	public BankverbindungDto getBankverbindungDto() {
		return bankverbindungDto;
	}
	
	public void setBankverbindungDto(BankverbindungDto bankverbindungDto) {
		this.bankverbindungDto = bankverbindungDto;
	}
	
	@Override
	public KontoDto getKontoDto() {
		return kontoDto;
	}
	
	public void setKontoDto(KontoDto kontoDto) {
		this.kontoDto = kontoDto;
	}
}
