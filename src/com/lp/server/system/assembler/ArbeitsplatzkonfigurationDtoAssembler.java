package com.lp.server.system.assembler;

import java.util.Collection;

import com.lp.server.system.ejb.Arbeitsplatzkonfiguration;
import com.lp.server.system.service.ArbeitsplatzkonfigurationDto;
import com.lp.server.util.DtoAssemblerFactory;

public class ArbeitsplatzkonfigurationDtoAssembler extends DtoAssemblerFactory<Arbeitsplatzkonfiguration, ArbeitsplatzkonfigurationDto>{
	@Override
	public ArbeitsplatzkonfigurationDto setDto(
			Arbeitsplatzkonfiguration entity, ArbeitsplatzkonfigurationDto dto) {
		dto.setArbeitsplatzIId(entity.getArbeitsplatzIId());
		dto.setCSystem(entity.getCSystem());
		dto.setCUser(entity.getCUser());			
		return dto ;
	}
	
	@Override
	public Arbeitsplatzkonfiguration setEntity(
			Arbeitsplatzkonfiguration entity, ArbeitsplatzkonfigurationDto dto) {
		entity.setCSystem(dto.getCSystem());
		entity.setCUser(dto.getCUser());
		return entity ;
	}
	
	public static ArbeitsplatzkonfigurationDto createDto(Arbeitsplatzkonfiguration konfiguration) {
		return new ArbeitsplatzkonfigurationDtoAssembler().internalCreateDto(konfiguration, ArbeitsplatzkonfigurationDto.class) ;
	}
	
	public static ArbeitsplatzkonfigurationDto[] createDtos(Collection<Arbeitsplatzkonfiguration> entities) {
		return new ArbeitsplatzkonfigurationDtoAssembler().internalCreateDtos(entities, ArbeitsplatzkonfigurationDto.class) ;
	}
	
	public static void setEntityFromDto(Arbeitsplatzkonfiguration entity, ArbeitsplatzkonfigurationDto dto) {
		new ArbeitsplatzkonfigurationDtoAssembler().setEntity(entity, dto) ;
	}
}
