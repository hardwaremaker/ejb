package com.lp.server.artikel.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.artikel.ejb.Waffentyp;

public class WaffentypDtoAssembler {
	public static WaffentypDto createDto(Waffentyp bean) {
		WaffentypDto dto = new WaffentypDto();
		if (bean != null) {
			dto.setIId(bean.getIId());
			dto.setCNr(bean.getCNr());
			dto.setCBez(bean.getCBez());
		}
		return dto;
	}

	public static WaffentypDto[] createDtos(Collection<?> dtos) {
		List<WaffentypDto> list = new ArrayList<WaffentypDto>();
		if (dtos != null) {
			Iterator<?> iterator = dtos.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Waffentyp) iterator.next()));
			}
		}
		WaffentypDto[] returnArray = new WaffentypDto[list.size()];
		return (WaffentypDto[]) list.toArray(returnArray);
	}
}
