package com.lp.server.personal.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.personal.ejb.Reisespesen;

public class ReisespesenDtoAssembler {
	public static ReisespesenDto createDto(Reisespesen bean) {
		ReisespesenDto dto = new ReisespesenDto();
		if (bean != null) {
			dto.setIId(bean.getIId());
			dto.setReiseIId(bean.getReiseIId());
			dto.setEingangsrechnungIId(bean.getEingangsrechnungIId());
			
			
		}
		return dto;
	}

	public static ReisespesenDto[] createDtos(Collection<?> reises) {
		List<ReisespesenDto> list = new ArrayList<ReisespesenDto>();
		if (reises != null) {
			Iterator<?> iterator = reises.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Reisespesen) iterator.next()));
			}
		}
		ReisespesenDto[] returnArray = new ReisespesenDto[list.size()];
		return (ReisespesenDto[]) list.toArray(returnArray);
	}
}
