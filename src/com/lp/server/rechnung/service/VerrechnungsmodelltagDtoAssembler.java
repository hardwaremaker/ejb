package com.lp.server.rechnung.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.rechnung.ejb.Verrechnungsmodelltag;

public class VerrechnungsmodelltagDtoAssembler {
	public static VerrechnungsmodelltagDto createDto(Verrechnungsmodelltag bean) {
		VerrechnungsmodelltagDto dto = new VerrechnungsmodelltagDto();
		if (bean != null) {
			dto.setIId(bean.getIId());
			dto.setVerrechnungsmodellIId(bean.getVerrechnungsmodellIId());
			dto.setTagesartIId(bean.getTagesartIId());
			dto.setArtikelIIdGebucht(bean.getArtikelIIdGebucht());
			dto.setArtikelIIdZuverrechnen(bean.getArtikelIIdZuverrechnen());
			dto.setUDauerAb(bean.getUDauerAb());
			dto.setUZeitraumBis(bean.getUZeitraumBis());
			dto.setUZeitraumVon(bean.getUZeitraumVon());
			dto.setBEndedestages(bean.getBEndedestages());

		}
		return dto;
	}

	public static VerrechnungsmodelltagDto[] createDtos(Collection<?> zeitmodells) {
		List<VerrechnungsmodelltagDto> list = new ArrayList<VerrechnungsmodelltagDto>();
		if (zeitmodells != null) {
			Iterator<?> iterator = zeitmodells.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Verrechnungsmodelltag) iterator.next()));
			}
		}
		VerrechnungsmodelltagDto[] returnArray = new VerrechnungsmodelltagDto[list.size()];
		return (VerrechnungsmodelltagDto[]) list.toArray(returnArray);
	}
}
