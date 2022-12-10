package com.lp.server.lieferschein.service.errors;

import com.lp.server.system.service.MandantDto;

public class StmMandantKeinKundeExc extends StmException {
	private static final long serialVersionUID = -1332859815633680315L;

	private MandantDto mandantDto;
	
	public StmMandantKeinKundeExc(MandantDto mandantDto) {
		super("Partner des Mandants (" + mandantDto.getPartnerDto().formatFixName1Name2() + ") ist nicht als Kunde definiert");
		setExcCode(StmExceptionCode.MANDANT_KEIN_KUNDE);
		setMandantDto(mandantDto);
	}
	
	public MandantDto getMandantDto() {
		return mandantDto;
	}
	
	public void setMandantDto(MandantDto mandantDto) {
		this.mandantDto = mandantDto;
	}
}
