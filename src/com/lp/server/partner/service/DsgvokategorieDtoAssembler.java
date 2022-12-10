package com.lp.server.partner.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.partner.ejb.Dsgvokategorie;

public class DsgvokategorieDtoAssembler {
	public static DsgvokategorieDto createDto(Dsgvokategorie bean) {
		DsgvokategorieDto dto = new DsgvokategorieDto();
		if (bean != null) {
			dto.setIId(bean.getIId());
			dto.setCNr(bean.getCNr());
			
		}
		return dto;
	}

	public static DsgvokategorieDto[] createDtos(Collection<?> schweres) {
		List<DsgvokategorieDto> list = new ArrayList<DsgvokategorieDto>();
		if (schweres != null) {
			Iterator<?> iterator = schweres.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Dsgvokategorie) iterator.next()));
			}
		}
		DsgvokategorieDto[] returnArray = new DsgvokategorieDto[list.size()];
		return (DsgvokategorieDto[]) list.toArray(returnArray);
	}
}
