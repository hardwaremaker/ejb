package com.lp.server.personal.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.personal.ejb.Personalterminal;

public class PersonalterminalDtoAssembler {
	public static PersonalterminalDto createDto(
			Personalterminal bean) {
		PersonalterminalDto dto = new PersonalterminalDto();
		if (bean != null) {
			dto.setIId(bean.getIId());
			dto.setPersonalIId(bean
					.getPersonalIId());
			dto.setArbeitsplatzIId(bean
					.getArbeitsplatzIId());
			
		}
		return dto;
	}

	public static PersonalterminalDto[] createDtos(
			Collection<?> personalverfuegbarkeits) {
		List<PersonalterminalDto> list = new ArrayList<PersonalterminalDto>();
		if (personalverfuegbarkeits != null) {
			Iterator<?> iterator = personalverfuegbarkeits.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Personalterminal) iterator.next()));
			}
		}
		PersonalterminalDto[] returnArray = new PersonalterminalDto[list
				.size()];
		return (PersonalterminalDto[]) list.toArray(returnArray);
	}
}
