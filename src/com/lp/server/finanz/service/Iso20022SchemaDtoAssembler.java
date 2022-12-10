package com.lp.server.finanz.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.lp.server.finanz.ejb.Iso20022Schema;

public class Iso20022SchemaDtoAssembler {

	public static Iso20022SchemaDto createDto(Iso20022Schema entity) {
		Iso20022SchemaDto dto = new Iso20022SchemaDto();
		dto.setIId(entity.getIId());
		dto.setSchema(Iso20022SchemaEnum.fromValue(entity.getCNr()));
		dto.setCBez(entity.getCBez());
		return dto;
	}
	
	public List<Iso20022SchemaDto> createDtos(Collection<Iso20022Schema> entities) {
		List<Iso20022SchemaDto> dtos = new ArrayList<Iso20022SchemaDto>();
		if (entities == null)
			return dtos;
		
		for (Iso20022Schema entity : entities) {
			dtos.add(createDto(entity));
		}
		return dtos;
	}
}
