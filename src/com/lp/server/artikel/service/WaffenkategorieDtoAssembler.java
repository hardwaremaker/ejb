package com.lp.server.artikel.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.artikel.ejb.Waffenkategorie;

public class WaffenkategorieDtoAssembler {
	public static WaffenkategorieDto createDto(Waffenkategorie bean) {
		WaffenkategorieDto dto = new WaffenkategorieDto();
		if (bean != null) {
			dto.setIId(bean.getIId());
			dto.setCNr(bean.getCNr());
			dto.setCBez(bean.getCBez());
		}
		return dto;
	}

	public static WaffenkategorieDto[] createDtos(Collection<?> dtos) {
		List<WaffenkategorieDto> list = new ArrayList<WaffenkategorieDto>();
		if (dtos != null) {
			Iterator<?> iterator = dtos.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Waffenkategorie) iterator.next()));
			}
		}
		WaffenkategorieDto[] returnArray = new WaffenkategorieDto[list.size()];
		return (WaffenkategorieDto[]) list.toArray(returnArray);
	}
}
