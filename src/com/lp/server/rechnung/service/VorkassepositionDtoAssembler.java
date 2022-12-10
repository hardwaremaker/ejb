package com.lp.server.rechnung.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.rechnung.ejb.Vorkasseposition;

public class VorkassepositionDtoAssembler {
	public static VorkassepositionDto createDto(Vorkasseposition bean) {
		VorkassepositionDto dto = new VorkassepositionDto();
		if (bean != null) {
			dto.setIId(bean.getIId());
			dto.setRechnungIId(bean.getRechnungIId());
			dto.setAuftragspositionIId(bean.getAuftragspositionIId());
			dto.setNBetrag(bean.getNBetrag());
			
		}
		return dto;
	}

	public static VorkassepositionDto[] createDtos(Collection<?> mahnstufes) {
		List<VorkassepositionDto> list = new ArrayList<VorkassepositionDto>();
		if (mahnstufes != null) {
			Iterator<?> iterator = mahnstufes.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Vorkasseposition) iterator.next()));
			}
		}
		VorkassepositionDto[] returnArray = new VorkassepositionDto[list.size()];
		return (VorkassepositionDto[]) list.toArray(returnArray);
	}
}
