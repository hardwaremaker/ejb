package com.lp.server.personal.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.personal.ejb.Abwesenheitsartspr;

public class AbwesenheitsartsprDtoAssembler {
	public static AbwesenheitsartsprDto createDto(Abwesenheitsartspr bean) {
		AbwesenheitsartsprDto dto = new AbwesenheitsartsprDto();
		if (bean != null) {
			dto.setLocaleCNr(bean.getPk().getLocaleCNr());
			dto.setAbwesenheitsartIId(bean.getPk().getAbwesenheitsartIId());
			dto.setCBez(bean.getCBez());
		}
		return dto;
	}

	public static AbwesenheitsartsprDto[] createDtos(Collection<?> materialsprs) {
		List<AbwesenheitsartsprDto> list = new ArrayList<AbwesenheitsartsprDto>();
		if (materialsprs != null) {
			Iterator<?> iterator = materialsprs.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Abwesenheitsartspr) iterator.next()));
			}
		}
		AbwesenheitsartsprDto[] returnArray = new AbwesenheitsartsprDto[list
				.size()];
		return (AbwesenheitsartsprDto[]) list.toArray(returnArray);
	}
}
