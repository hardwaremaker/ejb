package com.lp.server.artikel.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.artikel.ejb.WaffentypFein;

public class WaffentypFeinDtoAssembler {
	public static WaffentypFeinDto createDto(WaffentypFein bean) {
		WaffentypFeinDto dto = new WaffentypFeinDto();
		if (bean != null) {
			dto.setIId(bean.getIId());
			dto.setCNr(bean.getCNr());
			dto.setCBez(bean.getCBez());
		}
		return dto;
	}

	public static WaffentypFeinDto[] createDtos(Collection<?> dtos) {
		List<WaffentypFeinDto> list = new ArrayList<WaffentypFeinDto>();
		if (dtos != null) {
			Iterator<?> iterator = dtos.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((WaffentypFein) iterator.next()));
			}
		}
		WaffentypFeinDto[] returnArray = new WaffentypFeinDto[list.size()];
		return (WaffentypFeinDto[]) list.toArray(returnArray);
	}
}
