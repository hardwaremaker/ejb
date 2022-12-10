package com.lp.server.lieferschein.service.errors;

import com.lp.server.personal.service.PersonalDto;
import com.lp.server.system.service.MandantDto;

public class StmAnsprechpartnerNichtEingetragenExc extends StmException {
	private static final long serialVersionUID = -1199685438596024829L;

	private MandantDto mandantDto;
	private PersonalDto personalDto;
	
	public StmAnsprechpartnerNichtEingetragenExc(MandantDto mandantDto, PersonalDto personalDto) {
		super("Fuer Mandant-Partner '" + mandantDto.getPartnerDto().formatFixName1Name2() + "' fehlt Ansprechpartner von Personal '" + personalDto.getPartnerDto().formatFixName1Name2() + "' mit Personalnummer '" + personalDto.getCPersonalnr() + "'");
		setPersonalDto(personalDto);
		setMandantDto(mandantDto);
		setExcCode(StmExceptionCode.ANSPRECHPARTNER_FEHLT);
	}
	
	public PersonalDto getPersonalDto() {
		return personalDto;
	}
	
	public void setPersonalDto(PersonalDto personalDto) {
		this.personalDto = personalDto;
	}
	
	public MandantDto getMandantDto() {
		return mandantDto;
	}
	
	public void setMandantDto(MandantDto mandantDto) {
		this.mandantDto = mandantDto;
	}
}
