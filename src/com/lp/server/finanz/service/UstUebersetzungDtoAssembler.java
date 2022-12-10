package com.lp.server.finanz.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.lp.server.finanz.ejb.UstUebersetzung;

public class UstUebersetzungDtoAssembler {

	public static UstUebersetzungDto createDto(UstUebersetzung entity) {
		UstUebersetzungDto dto = new UstUebersetzungDto();
		if (entity != null) {
			dto.setCNr(entity.getPk().getCNr());
			dto.setMandantCNr(entity.getPk().getMandantCNr());
			dto.setBIgErwerb(entity.getBIgErwerb() != null && entity.getBIgErwerb() == 1 ? Boolean.TRUE : Boolean.FALSE);
			dto.setMwstSatzBezIId(entity.getMwstSatzBezIId());
			dto.setReversechargeartIId(entity.getReversechargeartId());
		}
		return dto;
	}

	public static List<UstUebersetzungDto> createDtos(Collection<UstUebersetzung> entities) {
		List<UstUebersetzungDto> dtos = new ArrayList<UstUebersetzungDto>();
		if (entities == null) return dtos;
		
		for (UstUebersetzung entity : entities) {
			dtos.add(createDto(entity));
		}
		return dtos;
	}
}
