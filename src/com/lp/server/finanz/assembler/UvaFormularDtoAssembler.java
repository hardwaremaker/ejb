package com.lp.server.finanz.assembler;

import java.util.Collection;

import com.lp.server.finanz.ejb.UvaFormular;
import com.lp.server.finanz.service.UvaFormularDto;

public class UvaFormularDtoAssembler {

	public static UvaFormularDto createDto(UvaFormular entity) {
		UvaFormularDto dto = new UvaFormularDto();
		dto.setIId(entity.getIId());
		dto.setFinanzamtId(entity.getFinanzamtIId());
		dto.setMandantCNr(entity.getMandantCNr());
		dto.setCKennzeichen(entity.getCKennzeichen());
		dto.setIGruppe(entity.getIGruppe());
		dto.setISort(entity.getISort());
		dto.setUvaartId(entity.getUvaartId());
		dto.setPersonalIIdAendern(entity.getPersonalIIdAendern());
		dto.setPersonalIIdAnlegen(entity.getPersonalIIdAnlegen());
		dto.setTAendern(entity.getTAendern());
		dto.setTAnlegen(entity.getTAnlegen());
		return dto;
	}

	public static UvaFormularDto[] createDtos(Collection<UvaFormular> entities) {
		if(entities == null) return new UvaFormularDto[0];
		
		UvaFormularDto[] dtos = new UvaFormularDto[entities.size()];
		int i = 0;
		for (UvaFormular entity : entities) {
			dtos[i++] = createDto(entity);
		}
		return dtos;
	}
}
