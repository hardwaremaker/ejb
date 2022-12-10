package com.lp.server.personal.ejbfac;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.personal.ejb.Hvmalizenz;
import com.lp.server.personal.service.HvmalizenzDto;


public class HvmalizenzDtoAssembler {
	public static HvmalizenzDto createDto(Hvmalizenz bean) {
		HvmalizenzDto dto = new HvmalizenzDto();
		if (bean != null) {
			dto.setIId(bean.getIId());
			dto.setCNr(bean.getCNr());
			dto.setIMaxUser(bean.getIMaxUser());
			
		}
		return dto;
	}

	public static HvmalizenzDto[] createDtos(Collection<?> schweres) {
		List<HvmalizenzDto> list = new ArrayList<HvmalizenzDto>();
		if (schweres != null) {
			Iterator<?> iterator = schweres.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Hvmalizenz) iterator.next()));
			}
		}
		HvmalizenzDto[] returnArray = new HvmalizenzDto[list.size()];
		return (HvmalizenzDto[]) list.toArray(returnArray);
	}
}