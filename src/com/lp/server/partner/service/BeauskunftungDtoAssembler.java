package com.lp.server.partner.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.partner.ejb.Beauskunftung;

public class BeauskunftungDtoAssembler {
	public static BeauskunftungDto createDto(Beauskunftung bean) {
		BeauskunftungDto dto = new BeauskunftungDto();
		if (bean != null) {
			dto.setIId(bean.getIId());
			dto.setPartnerIId(bean.getPartnerIId());
			dto.setBKostenpflichtig(bean.getBKostenpflichtig());
			dto.setIdentifikationIId(bean.getIdentifikationIId());
			dto.setPersonalIIdAnlegen(bean.getPersonalIIdAnlegen());
			dto.setTAnlegen(bean.getTAnlegen());

		}
		return dto;
	}

	public static BeauskunftungDto[] createDtos(Collection<?> banks) {
		List<BeauskunftungDto> list = new ArrayList<BeauskunftungDto>();
		if (banks != null) {
			Iterator<?> iterator = banks.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Beauskunftung) iterator.next()));
			}
		}
		BeauskunftungDto[] returnArray = new BeauskunftungDto[list.size()];
		return (BeauskunftungDto[]) list.toArray(returnArray);
	}
}