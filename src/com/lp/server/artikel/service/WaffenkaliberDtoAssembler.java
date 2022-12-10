package com.lp.server.artikel.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.artikel.ejb.Waffenkaliber;

public class WaffenkaliberDtoAssembler {
	public static WaffenkaliberDto createDto(Waffenkaliber bean) {
		WaffenkaliberDto dto = new WaffenkaliberDto();
		if (bean != null) {
			dto.setIId(bean.getIId());
			dto.setCNr(bean.getCNr());
			dto.setCBez(bean.getCBez());
		}
		return dto;
	}

	public static WaffenkaliberDto[] createDtos(Collection<?> dtos) {
		List<WaffenkaliberDto> list = new ArrayList<WaffenkaliberDto>();
		if (dtos != null) {
			Iterator<?> iterator = dtos.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Waffenkaliber) iterator.next()));
			}
		}
		WaffenkaliberDto[] returnArray = new WaffenkaliberDto[list.size()];
		return (WaffenkaliberDto[]) list.toArray(returnArray);
	}
}
