package com.lp.server.system.ejbfac;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.lp.server.system.ejb.MailProperty;
import com.lp.server.system.mail.service.MailPropertyDto;

public class MailPropertyDtoAssembler {

	public static MailPropertyDto createDto(MailProperty entity) {
		MailPropertyDto dto = new MailPropertyDto();
		if (entity != null && entity.getPk() != null) {
			dto.setCNr(entity.getPk().getCNr());
			dto.setMandantCNr(entity.getPk().getMandantCNr());
			dto.setCWert(entity.getCWert());
			dto.setCDefaultWert(entity.getCDefaultWert());
			dto.setPersonalIIdAendern(entity.getPersonalIIdAendern());
			dto.setTAendern(entity.getTAendern());
		}
		return dto;
	}
	
	public static Collection<MailPropertyDto> createDtos(Collection<MailProperty> entities) {
		List<MailPropertyDto> dtos = new ArrayList<MailPropertyDto>();
		for (MailProperty entity : entities) {
			dtos.add(createDto(entity));
		}
		return dtos;
	}

}
