package com.lp.server.rechnung.service;

import java.util.ArrayList;
import java.util.List;

import com.lp.server.rechnung.ejb.Lastschriftvorschlag;

public class LastschriftvorschlagDtoAssembler {

	public static LastschriftvorschlagDto createDto(Lastschriftvorschlag entity) {
		if (entity == null) return new LastschriftvorschlagDto();
		
		LastschriftvorschlagDto dto = new LastschriftvorschlagDto();
		dto.setCAuftraggeberreferenz(entity.getcAuftraggeberreferenz());
		dto.setIId(entity.getiId());
		dto.setMahnlaufIId(entity.getMahnlaufIId());
		dto.setNBereitsBezahlt(entity.getnBereitsBezahlt());
		dto.setNRechnungsbetrag(entity.getnRechnungsbetrag());
		dto.setNZahlbetrag(entity.getnZahlbetrag());
		dto.setRechnungIId(entity.getRechnungIId());
		dto.setTFaellig(entity.gettFaellig());
		dto.setPersonalIIdAendern(entity.getPersonalIIdAendern());
		dto.setTAendern(entity.gettAendern());
		dto.setPersonalIIdGespeichert(entity.getPersonalIIdGespeichert());
		dto.setTGespeichert(entity.gettGespeichert());
		dto.setCVerwendungszweck(entity.getCVerwendungszweck());
		
		return dto;
	}

	public static List<LastschriftvorschlagDto> createDtos(List<Lastschriftvorschlag> entities) {
		if (entities == null) return new ArrayList<LastschriftvorschlagDto>();
		
		List<LastschriftvorschlagDto> dtos = new ArrayList<LastschriftvorschlagDto>();
		for (Lastschriftvorschlag entity : entities) {
			dtos.add(createDto(entity));
		}
		return dtos;
	}
}
