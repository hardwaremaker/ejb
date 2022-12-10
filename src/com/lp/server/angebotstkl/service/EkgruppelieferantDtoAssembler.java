package com.lp.server.angebotstkl.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.angebotstkl.ejb.Ekgruppelieferant;

public class EkgruppelieferantDtoAssembler {
	public static EkgruppelieferantDto createDto(Ekgruppelieferant bean) {
		EkgruppelieferantDto dto = new EkgruppelieferantDto();
		if (bean != null) {
			dto.setIId(bean.getIId());
			dto.setEkgruppeIId(bean.getEkgruppeIId());
			dto.setLieferantIId(bean.getLieferantIId());
			dto.setAnsprechpartnerIId(bean.getAnsprechpartnerIId());

		}
		return dto;
	}

	public static EkgruppelieferantDto[] createDtos(Collection<?> beans) {
		List<EkgruppelieferantDto> list = new ArrayList<EkgruppelieferantDto>();
		if (beans != null) {
			Iterator<?> iterator = beans.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Ekgruppelieferant) iterator.next()));
			}
		}
		EkgruppelieferantDto[] returnArray = new EkgruppelieferantDto[list.size()];
		return (EkgruppelieferantDto[]) list.toArray(returnArray);
	}
}
