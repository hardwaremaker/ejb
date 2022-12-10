package com.lp.server.angebotstkl.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.angebotstkl.ejb.Ekgruppe;

public class EkgruppeDtoAssembler {
	public static EkgruppeDto createDto(Ekgruppe bean) {
		EkgruppeDto dto = new EkgruppeDto();
		if (bean != null) {
			dto.setIId(bean.getIId());
			dto.setCBez(bean.getCBez());
			dto.setMandantCNr(bean.getMandantCNr());
			
		}
		return dto;
	}

	public static EkgruppeDto[] createDtos(Collection<?> beans) {
		List<EkgruppeDto> list = new ArrayList<EkgruppeDto>();
		if (beans != null) {
			Iterator<?> iterator = beans.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Ekgruppe) iterator.next()));
			}
		}
		EkgruppeDto[] returnArray = new EkgruppeDto[list.size()];
		return (EkgruppeDto[]) list.toArray(returnArray);
	}
}
