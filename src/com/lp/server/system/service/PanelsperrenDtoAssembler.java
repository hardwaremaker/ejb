package com.lp.server.system.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.system.ejb.Panelsperren;

public class PanelsperrenDtoAssembler {
	public static PanelsperrenDto createDto(
			Panelsperren bean) {
		PanelsperrenDto dto = new PanelsperrenDto();
		if (bean != null) {
			dto.setIId(bean.getIId());
			dto.setBelegartCNr(bean.getBelegartCNr());
			dto.setMandantCNr(bean.getMandantCNr());
			dto.setCRessourceUnten(bean.getCRessourceUnten());
			dto.setCRessourceOben(bean
					.getCRessourceOben());
		}
		return dto;
	}

	public static PanelsperrenDto[] createDtos(
			Collection<?> panelbeschreibungs) {
		List<PanelsperrenDto> list = new ArrayList<PanelsperrenDto>();
		if (panelbeschreibungs != null) {
			Iterator<?> iterator = panelbeschreibungs.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Panelsperren) iterator.next()));
			}
		}
		PanelsperrenDto[] returnArray = new PanelsperrenDto[list
				.size()];
		return (PanelsperrenDto[]) list.toArray(returnArray);
	}
}
