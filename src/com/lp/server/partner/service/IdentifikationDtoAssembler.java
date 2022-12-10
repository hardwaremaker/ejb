package com.lp.server.partner.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.partner.ejb.Identifikation;

public class IdentifikationDtoAssembler {
	public static IdentifikationDto createDto(Identifikation bean) {
		IdentifikationDto dto = new IdentifikationDto();
		if (bean != null) {
			dto.setIId(bean.getIId());
			dto.setCNr(bean.getCNr());
			
		}
		return dto;
	}

	public static IdentifikationDto[] createDtos(Collection<?> schweres) {
		List<IdentifikationDto> list = new ArrayList<IdentifikationDto>();
		if (schweres != null) {
			Iterator<?> iterator = schweres.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Identifikation) iterator.next()));
			}
		}
		IdentifikationDto[] returnArray = new IdentifikationDto[list.size()];
		return (IdentifikationDto[]) list.toArray(returnArray);
	}
}

