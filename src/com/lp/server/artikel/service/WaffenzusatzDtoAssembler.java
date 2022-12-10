package com.lp.server.artikel.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.artikel.ejb.Waffenzusatz;

public class WaffenzusatzDtoAssembler {
	public static WaffenzusatzDto createDto(Waffenzusatz bean) {
		WaffenzusatzDto dto = new WaffenzusatzDto();
		if (bean != null) {
			dto.setIId(bean.getIId());
			dto.setCNr(bean.getCNr());
			dto.setCBez(bean.getCBez());
		}
		return dto;
	}

	public static WaffenzusatzDto[] createDtos(Collection<?> dtos) {
		List<WaffenzusatzDto> list = new ArrayList<WaffenzusatzDto>();
		if (dtos != null) {
			Iterator<?> iterator = dtos.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Waffenzusatz) iterator.next()));
			}
		}
		WaffenzusatzDto[] returnArray = new WaffenzusatzDto[list.size()];
		return (WaffenzusatzDto[]) list.toArray(returnArray);
	}
}
