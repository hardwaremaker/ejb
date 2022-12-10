package com.lp.server.rechnung.service.sepa.errors;

import com.lp.server.finanz.service.BankverbindungDto;
import com.lp.server.finanz.service.KontoDto;

public class KeinSepaverzeichnisException extends SepaException implements ISepaBankverbindungException {
	private static final long serialVersionUID = 1355105752148871335L;

	private BankverbindungDto bankverbindungDto;
	private KontoDto kontoDto;
	
	public KeinSepaverzeichnisException(BankverbindungDto bankverbindungDto, KontoDto kontoDto) {
		super("Kein Sepaverzeichnis definiert in Bankverbindung mit Sachkontonummer '" + kontoDto.getCNr() + "'");
		setBankverbindungDto(bankverbindungDto);
		setKontoDto(kontoDto);
		setCode(SepaExceptionCode.KEIN_SEPAVERZEICHNIS);
	}

	public void setBankverbindungDto(BankverbindungDto bankverbindungDto) {
		this.bankverbindungDto = bankverbindungDto;
	}
	
	public BankverbindungDto getBankverbindungDto() {
		return bankverbindungDto;
	}
	
	public void setKontoDto(KontoDto kontoDto) {
		this.kontoDto = kontoDto;
	}
	
	@Override
	public KontoDto getKontoDto() {
		return kontoDto;
	}
}
