package com.lp.server.personal.ejbfac;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.personal.ejb.Hvmarecht;
import com.lp.server.personal.service.HvmarechtDto;

public class HvmarechtDtoAssembler {
	public static HvmarechtDto createDto(Hvmarecht bean) {
		HvmarechtDto dto = new HvmarechtDto();
		if (bean != null) {
			dto.setIId(bean.getIId());
			dto.setCNr(bean.getCNr());
			dto.setHvmalizenzIId(bean.getHvmalizenzIId());
			dto.setbAktiv(bean.getBAktiv());
		}
		return dto;
	}

	public static HvmarechtDto[] createDtos(Collection<?> schweres) {
		List<HvmarechtDto> list = new ArrayList<HvmarechtDto>();
		if (schweres != null) {
			Iterator<?> iterator = schweres.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Hvmarecht) iterator.next()));
			}
		}
		HvmarechtDto[] returnArray = new HvmarechtDto[list.size()];
		return (HvmarechtDto[]) list.toArray(returnArray);
	}
}