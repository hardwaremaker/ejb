package com.lp.server.partner.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.partner.ejb.Dsgvotext;

public class DsgvotextDtoAssembler {
	public static DsgvotextDto createDto(Dsgvotext bean) {
		DsgvotextDto dto = new DsgvotextDto();
		if (bean != null) {
			dto.setIId(bean.getIId());
			dto.setBKopftext(bean.getBKopftext());
			dto.setDsgvokategorieIId(bean.getDsgvokategorieIId());
			dto.setISort(bean.getISort());
			dto.setMandantCNr(bean.getMandantCNr());
			dto.setXInhalt(bean.getXInhalt());
		}
		return dto;
	}

	public static DsgvotextDto[] createDtos(Collection<?> banks) {
		List<DsgvotextDto> list = new ArrayList<DsgvotextDto>();
		if (banks != null) {
			Iterator<?> iterator = banks.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Dsgvotext) iterator.next()));
			}
		}
		DsgvotextDto[] returnArray = new DsgvotextDto[list.size()];
		return (DsgvotextDto[]) list.toArray(returnArray);
	}
}
