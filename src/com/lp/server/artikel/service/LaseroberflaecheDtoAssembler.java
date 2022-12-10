package com.lp.server.artikel.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.artikel.ejb.Laseroberflaeche;

public class LaseroberflaecheDtoAssembler {
	public static LaseroberflaecheDto createDto(Laseroberflaeche bean) {
		LaseroberflaecheDto dto = new LaseroberflaecheDto();
		if (bean != null) {
			dto.setIId(bean.getIId());
			dto.setMandantCNr(bean.getMandantCNr());
			dto.setCNr(bean.getCNr());
			dto.setCBez(bean.getCBez());
		}
		return dto;
	}

	public static LaseroberflaecheDto[] createDtos(Collection<?> dtos) {
		List<LaseroberflaecheDto> list = new ArrayList<LaseroberflaecheDto>();
		if (dtos != null) {
			Iterator<?> iterator = dtos.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Laseroberflaeche) iterator.next()));
			}
		}
		LaseroberflaecheDto[] returnArray = new LaseroberflaecheDto[list.size()];
		return (LaseroberflaecheDto[]) list.toArray(returnArray);
	}
}
