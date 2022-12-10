package com.lp.server.personal.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.personal.ejb.Abwesenheitsart;

public class AbwesenheitsartDtoAssembler {
	public static AbwesenheitsartDto createDto(Abwesenheitsart bean) {
		AbwesenheitsartDto dto = new AbwesenheitsartDto();
		if (bean != null) {
			dto.setIId(bean.getIId());
			dto.setCNr(bean.getCNr());
			dto.setISort(bean.getISort());
			
		}
		return dto;
	}

	public static AbwesenheitsartDto[] createDtos(Collection<?> schweres) {
		List<AbwesenheitsartDto> list = new ArrayList<AbwesenheitsartDto>();
		if (schweres != null) {
			Iterator<?> iterator = schweres.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Abwesenheitsart) iterator.next()));
			}
		}
		AbwesenheitsartDto[] returnArray = new AbwesenheitsartDto[list.size()];
		return (AbwesenheitsartDto[]) list.toArray(returnArray);
	}
}
