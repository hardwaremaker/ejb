package com.lp.server.rechnung.service.sepa.errors;

import com.lp.server.system.service.MandantDto;

public class KeineGlaeubigerIdException extends SepaException implements ISepaMandantException {
	private static final long serialVersionUID = 2888358645358421523L;

	private MandantDto mandantDto;
	
	public KeineGlaeubigerIdException(MandantDto mandantDto) {
		super("Keine Gl\u00E4ubiger-Id f\u00FCr Mandant " + mandantDto.getCNr() + " eingetragen.");
		setMandantDto(mandantDto);
		setCode(SepaExceptionCode.KEINE_GLAEUBIGERID);
	}
	
	public void setMandantDto(MandantDto mandantDto) {
		this.mandantDto = mandantDto;
	}
	
	public MandantDto getMandantDto() {
		return mandantDto;
	}
}
