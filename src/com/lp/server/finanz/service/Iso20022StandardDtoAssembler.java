package com.lp.server.finanz.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.lp.server.finanz.ejb.Iso20022Standard;

public class Iso20022StandardDtoAssembler {

	public static Iso20022StandardDto createDto(Iso20022Standard entity) {
		Iso20022StandardDto dto = new Iso20022StandardDto();
		dto.setIId(entity.getIId());
		dto.setStandardEnum(Iso20022StandardEnum.lookup(entity.getCNr()));
		dto.setCBez(entity.getCBez());
		return dto;
	}

	public static List<Iso20022StandardDto> createDtos(Collection<Iso20022Standard> entities) {
		List<Iso20022StandardDto> dtos = new ArrayList<Iso20022StandardDto>();
		if (entities == null)
			return dtos;
		
		for (Iso20022Standard entity : entities) {
			dtos.add(createDto(entity));
		}
		return dtos;
	}
}
