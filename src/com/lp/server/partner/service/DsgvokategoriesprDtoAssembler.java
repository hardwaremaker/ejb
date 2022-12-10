package com.lp.server.partner.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.partner.ejb.Dsgvokategoriespr;

public class DsgvokategoriesprDtoAssembler {
	public static DsgvokategoriesprDto createDto(Dsgvokategoriespr bean) {
		DsgvokategoriesprDto dto = new DsgvokategoriesprDto();
		if (bean != null) {
			dto.setLocaleCNr(bean.getPk().getLocaleCNr());
			dto.setDsgvokategorieIId(bean.getPk().getDsgvokategorieIId());
			dto.setCBez(bean.getCBez());
		}
		return dto;
	}

	public static DsgvokategoriesprDto[] createDtos(Collection<?> materialsprs) {
		List<DsgvokategoriesprDto> list = new ArrayList<DsgvokategoriesprDto>();
		if (materialsprs != null) {
			Iterator<?> iterator = materialsprs.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Dsgvokategoriespr) iterator.next()));
			}
		}
		DsgvokategoriesprDto[] returnArray = new DsgvokategoriesprDto[list.size()];
		return (DsgvokategoriesprDto[]) list.toArray(returnArray);
	}
}
