package com.lp.server.rechnung.service.sepa.errors;

import com.lp.server.system.service.MandantDto;

public class KeineLastschriftBankverbindung extends SepaException implements ISepaMandantException {
	private static final long serialVersionUID = -9199107801438473208L;

	private MandantDto mandantDto;
	
	public KeineLastschriftBankverbindung(MandantDto mandantDto) {
		super("Keine Bankverbindung in Mandant " + mandantDto.getCNr() + " mit gesetztem Lastschrift-Flag gefunden");
		setMandantDto(mandantDto);
		setCode(SepaExceptionCode.KEINE_LASTSCHRIFT_BANKVERBINDUNG);
	}
	
	public void setMandantDto(MandantDto mandantDto) {
		this.mandantDto = mandantDto;
	}
	
	public MandantDto getMandantDto() {
		return mandantDto;
	}
}
