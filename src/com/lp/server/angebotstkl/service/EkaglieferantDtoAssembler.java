package com.lp.server.angebotstkl.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.angebotstkl.ejb.Ekaglieferant;

public class EkaglieferantDtoAssembler {
	public static EkaglieferantDto createDto(Ekaglieferant bean) {
		EkaglieferantDto dto = new EkaglieferantDto();
		if (bean != null) {
			dto.setIId(bean.getIId());
			dto.setEinkaufsangebotIId(bean.getEinkaufsangebotIId());
			dto.setLieferantIId(bean.getLieferantIId());
			dto.setCAngebotsnummer(bean.getCAngebotsnummer());
			dto.setTImport(bean.getTImport());
			dto.setWaehrungCNr(bean.getWaehrungCNr());
			dto.setLieferantIId(bean.getLieferantIId());
			dto.setAnsprechpartnerIId(bean.getAnsprechpartnerIId());
			dto.setTVersand(bean.getTVersand());
			dto.setNAufschlag(bean.getNAufschlag());

		}
		return dto;
	}

	public static EkaglieferantDto[] createDtos(Collection<?> beans) {
		List<EkaglieferantDto> list = new ArrayList<EkaglieferantDto>();
		if (beans != null) {
			Iterator<?> iterator = beans.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Ekaglieferant) iterator.next()));
			}
		}
		EkaglieferantDto[] returnArray = new EkaglieferantDto[list.size()];
		return (EkaglieferantDto[]) list.toArray(returnArray);
	}
}
