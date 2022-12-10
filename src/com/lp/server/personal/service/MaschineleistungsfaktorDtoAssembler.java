package com.lp.server.personal.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.personal.ejb.Maschineleistungsfaktor;

public class MaschineleistungsfaktorDtoAssembler {
	public static MaschineleistungsfaktorDto createDto(Maschineleistungsfaktor bean) {
		MaschineleistungsfaktorDto dto = new MaschineleistungsfaktorDto();
		if (bean != null) {
			dto.setIId(bean.getIId());
			dto.setMaschineIId(bean.getMaschineIId());
			dto.setMaterialIId(bean.getMaterialIId());
			dto.setNFaktorInProzent(bean.getNFaktorInProzent());
			dto.setTGueltigab(bean.getTGueltigab());
		}
		return dto;
	}

	public static MaschineleistungsfaktorDto[] createDtos(Collection<?> dtos) {
		List<MaschineleistungsfaktorDto> list = new ArrayList<MaschineleistungsfaktorDto>();
		if (dtos != null) {
			Iterator<?> iterator = dtos.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Maschineleistungsfaktor) iterator.next()));
			}
		}
		MaschineleistungsfaktorDto[] returnArray = new MaschineleistungsfaktorDto[list.size()];
		return (MaschineleistungsfaktorDto[]) list.toArray(returnArray);
	}
}
