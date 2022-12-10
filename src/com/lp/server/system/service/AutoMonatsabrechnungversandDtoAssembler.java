package com.lp.server.system.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.system.ejb.AutoMonatsabrechnungversand;
import com.lp.server.system.ejb.AutoMonatsabrechnungversandAbteilungen;

public class AutoMonatsabrechnungversandDtoAssembler {
	public static AutoMonatsabrechnungversandDto createDto(AutoMonatsabrechnungversand bean) {
		AutoMonatsabrechnungversandDto dto = new AutoMonatsabrechnungversandDto();
		if (bean != null) {
			dto.setIId(bean.getIId());
			dto.setMandantCNr(bean.getMandantCNr());
			dto.setIWochentag(bean.getIWochentag());
			dto.setBMonatlich(bean.getBMonatlich());

		}
		return dto;
	}
	public static AutoMonatsabrechnungversandDto createDto(AutoMonatsabrechnungversandAbteilungen bean) {
		AutoMonatsabrechnungversandDto dto = new AutoMonatsabrechnungversandDto();
		if (bean != null) {
			dto.setIId(bean.getIId());
			dto.setMandantCNr(bean.getMandantCNr());
			dto.setIWochentag(bean.getIWochentag());
			dto.setBMonatlich(bean.getBMonatlich());

		}
		return dto;
	}
	public static AutoMonatsabrechnungversandDto[] createDtos(Collection<?> beans) {
		List<AutoMonatsabrechnungversandDto> list = new ArrayList<AutoMonatsabrechnungversandDto>();
		if (beans != null) {
			Iterator<?> iterator = beans.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((AutoMonatsabrechnungversand) iterator.next()));
			}
		}
		AutoMonatsabrechnungversandDto[] returnArray = new AutoMonatsabrechnungversandDto[list.size()];
		return (AutoMonatsabrechnungversandDto[]) list.toArray(returnArray);
	}
}
