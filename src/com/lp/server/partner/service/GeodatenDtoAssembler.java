package com.lp.server.partner.service;

import com.lp.server.partner.ejb.Geodaten;

public class GeodatenDtoAssembler {

	public static GeodatenDto createDto(Geodaten entity) {
		GeodatenDto dto = new GeodatenDto();
		if (entity != null) {
			dto.setIId(entity.getIId());
			dto.setPartnerIId(entity.getPartnerIId());
			dto.setBreitengrad(entity.getBreitengrad());
			dto.setLaengengrad(entity.getLaengengrad());
		}
		return dto;
	}
}
