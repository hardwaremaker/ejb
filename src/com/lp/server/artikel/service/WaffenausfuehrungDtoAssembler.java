package com.lp.server.artikel.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.artikel.ejb.Waffenausfuehrung;

public class WaffenausfuehrungDtoAssembler {
	public static WaffenausfuehrungDto createDto(Waffenausfuehrung bean) {
		WaffenausfuehrungDto dto = new WaffenausfuehrungDto();
		if (bean != null) {
			dto.setIId(bean.getIId());
			dto.setCNr(bean.getCNr());
			dto.setCBez(bean.getCBez());
		}
		return dto;
	}

	public static WaffenausfuehrungDto[] createDtos(Collection<?> dtos) {
		List<WaffenausfuehrungDto> list = new ArrayList<WaffenausfuehrungDto>();
		if (dtos != null) {
			Iterator<?> iterator = dtos.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Waffenausfuehrung) iterator.next()));
			}
		}
		WaffenausfuehrungDto[] returnArray = new WaffenausfuehrungDto[list.size()];
		return (WaffenausfuehrungDto[]) list.toArray(returnArray);
	}
}
