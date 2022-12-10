package com.lp.server.personal.ejbfac;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.personal.ejb.Hvmabenutzer;
import com.lp.server.personal.service.HvmabenutzerDto;


public class HvmabenutzerDtoAssembler {
	public static HvmabenutzerDto createDto(Hvmabenutzer bean) {
		HvmabenutzerDto dto = new HvmabenutzerDto();
		if (bean != null) {
			dto.setIId(bean.getIId());
			dto.setBenutzerIId(bean.getBenutzerIId());
			dto.setHvmalizenzIId(bean.getHvmalizenzIId());
			dto.setTAnlegen(bean.getTAnlegen());
			dto.setCToken(bean.getcToken());
			
		}
		return dto;
	}

	public static HvmabenutzerDto[] createDtos(Collection<?> schweres) {
		List<HvmabenutzerDto> list = new ArrayList<HvmabenutzerDto>();
		if (schweres != null) {
			Iterator<?> iterator = schweres.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Hvmabenutzer) iterator.next()));
			}
		}
		HvmabenutzerDto[] returnArray = new HvmabenutzerDto[list.size()];
		return (HvmabenutzerDto[]) list.toArray(returnArray);
	}
}