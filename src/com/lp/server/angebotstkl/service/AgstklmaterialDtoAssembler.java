package com.lp.server.angebotstkl.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.angebotstkl.ejb.Agstklmaterial;

public class AgstklmaterialDtoAssembler {
	public static AgstklmaterialDto createDto(Agstklmaterial bean) {
		AgstklmaterialDto dto = new AgstklmaterialDto();
		if (bean != null) {
			dto.setIId(bean.getIId());
			dto.setAgstklIId(bean.getAgstklIId());
			dto.setISort(bean.getISort());
			dto.setMaterialIId(bean.getMaterialIId());
			dto.setCBez(bean.getCBez());
			dto.setCMaterialtyp(bean.getCMaterialtyp());
			dto.setNDimension1(bean.getNDimension1());
			dto.setNDimension2(bean.getNDimension2());
			dto.setNDimension3(bean.getNDimension3());
			dto.setNGewichtpreis(bean.getNGewichtpreis());
			dto.setNGewicht(bean.getNGewicht());
		}
		return dto;
	}

	public static AgstklmaterialDto[] createDtos(Collection<?> agstklpositions) {
		List<AgstklmaterialDto> list = new ArrayList<AgstklmaterialDto>();
		if (agstklpositions != null) {
			Iterator<?> iterator = agstklpositions.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Agstklmaterial) iterator.next()));
			}
		}
		AgstklmaterialDto[] returnArray = new AgstklmaterialDto[list.size()];
		return (AgstklmaterialDto[]) list.toArray(returnArray);
	}
}
