package com.lp.server.partner.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.partner.ejb.Identifikationspr;


public class IdentifikationsprDtoAssembler {
	public static IdentifikationsprDto createDto(Identifikationspr bean) {
		IdentifikationsprDto dto = new IdentifikationsprDto();
		if (bean != null) {
			dto.setLocaleCNr(bean.getPk().getLocaleCNr());
			dto.setIdentifikationIId(bean.getPk().getIdentifikationIId());
			dto.setCBez(bean.getCBez());
		}
		return dto;
	}

	public static IdentifikationsprDto[] createDtos(Collection<?> materialsprs) {
		List<IdentifikationsprDto> list = new ArrayList<IdentifikationsprDto>();
		if (materialsprs != null) {
			Iterator<?> iterator = materialsprs.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Identifikationspr) iterator.next()));
			}
		}
		IdentifikationsprDto[] returnArray = new IdentifikationsprDto[list.size()];
		return (IdentifikationsprDto[]) list.toArray(returnArray);
	}
}