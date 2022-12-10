package com.lp.server.partner.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.partner.ejb.Kundespediteur;

public class KundespediteurDtoAssembler {
	public static KundespediteurDto createDto(Kundespediteur bean) {
		KundespediteurDto dto = new KundespediteurDto();
		if (bean != null) {
			dto.setIId(bean.getIId());
			dto.setKundeIId(bean.getKundeIId());
			dto.setSpediteurIId(bean.getSpediteurIId());
			dto.setNGewichtinkg(bean.getNGewichtinkg());
			dto.setCAccounting(bean.getCAccounting());

		}
		return dto;
	}

	public static KundespediteurDto[] createDtos(Collection<?> kundematerials) {
		List<KundespediteurDto> list = new ArrayList<KundespediteurDto>();
		if (kundematerials != null) {
			Iterator<?> iterator = kundematerials.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Kundespediteur) iterator.next()));
			}
		}
		KundespediteurDto[] returnArray = new KundespediteurDto[list.size()];
		return (KundespediteurDto[]) list.toArray(returnArray);
	}
}
