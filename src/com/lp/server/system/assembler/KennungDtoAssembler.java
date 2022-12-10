package com.lp.server.system.assembler;

import java.util.Collection;
import java.util.List;

import com.lp.server.system.ejb.Kennung;
import com.lp.server.system.service.KennungDto;
import com.lp.server.util.DtoAssemblerFactory;

public class KennungDtoAssembler extends DtoAssemblerFactory<Kennung, KennungDto> {

	@Override
	public KennungDto setDto(Kennung entity, KennungDto dto) {
		dto.setIId(entity.getIId());
		dto.setCnr(entity.getCNr());
		dto.setBez(entity.getCBez());
		return dto;
	}

	@Override
	public Kennung setEntity(Kennung entity, KennungDto dto) {
		entity.setCNr(dto.getCnr());
		entity.setCBez(dto.getBez());
		entity.setIId(dto.getIId());
		return entity;
	}

	public static KennungDto createDto(Kennung kennung) {
		return new KennungDtoAssembler().internalCreateDto(kennung, KennungDto.class);
	}
	
	public static KennungDto[] createDtos(Collection<Kennung> entities) {
		return new KennungDtoAssembler().internalCreateDtos(entities, KennungDto.class);
	}
	
	public static List<KennungDto> createDtosList(Collection<Kennung> entities) {
		return new KennungDtoAssembler().internalCreateDtosList(entities, KennungDto.class);
	}
}
