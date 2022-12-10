package com.lp.server.personal.ejbfac;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.personal.ejb.Hvmarolle;
import com.lp.server.personal.service.HvmarolleDto;

public class HvmarolleDtoAssembler {
	public static HvmarolleDto createDto(Hvmarolle bean) {
		HvmarolleDto dto = new HvmarolleDto();
		if (bean != null) {
			dto.setIId(bean.getIId());
			dto.setSystemrolleIId(bean.getSystemrolleIId());
			dto.setHvmarechtIId(bean.getHvmarechtIId());

		}
		return dto;
	}

	public static HvmarolleDto[] createDtos(Collection<?> schweres) {
		List<HvmarolleDto> list = new ArrayList<HvmarolleDto>();
		if (schweres != null) {
			Iterator<?> iterator = schweres.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Hvmarolle) iterator.next()));
			}
		}
		HvmarolleDto[] returnArray = new HvmarolleDto[list.size()];
		return (HvmarolleDto[]) list.toArray(returnArray);
	}
}
