package com.lp.server.artikel.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.artikel.ejb.Verpackungsmittel;

public class VerpackungsmittelDtoAssembler {
	public static VerpackungsmittelDto createDto(Verpackungsmittel bean) {
		VerpackungsmittelDto dto = new VerpackungsmittelDto();
		if (bean != null) {
			dto.setIId(bean.getIId());
			dto.setCNr(bean.getCNr());
			dto.setMandantCNr(bean.getMandantCNr());
			dto.setNGewichtInKG(bean.getNGewichtInKG());
		}
		return dto;
	}

	public static VerpackungsmittelDto[] createDtos(Collection<?> c) {
		List<VerpackungsmittelDto> list = new ArrayList<VerpackungsmittelDto>();
		if (c != null) {
			Iterator<?> iterator = c.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Verpackungsmittel) iterator.next()));
			}
		}
		VerpackungsmittelDto[] returnArray = new VerpackungsmittelDto[list.size()];
		return (VerpackungsmittelDto[]) list.toArray(returnArray);
	}
}
