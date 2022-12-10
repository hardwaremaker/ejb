package com.lp.server.personal.ejbfac;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.lp.server.personal.ejb.HvmabenutzerParameter;
import com.lp.server.personal.service.HvmabenutzerParameterDto;

public class HvmabenutzerParameterDtoAssembler {
	public static HvmabenutzerParameterDto createDto(HvmabenutzerParameter bean) {
		HvmabenutzerParameterDto dto = new HvmabenutzerParameterDto();
		if(bean != null) {
			dto.setIId(bean.getIId());
			dto.setCNr(bean.getCNr());
			dto.setKategorie(bean.getKategorie());
			dto.setWert(bean.getWert());
			dto.setPersonalIIdAendern(bean.getPersonalIIdAendern());
			dto.setTAendern(bean.getTAendern());
			dto.setHvmabenutzerId(bean.getHvmabenutzerId());
		}
		return dto;
	}
	
	public static HvmabenutzerParameterDto[] createDtos(
			Collection<HvmabenutzerParameter> beans) {
		return createListDtos(beans).toArray(new HvmabenutzerParameterDto[0]);
	}
	
	public static List<HvmabenutzerParameterDto> createListDtos(
			Collection<HvmabenutzerParameter> beans) {
		List<HvmabenutzerParameterDto> dtos = new ArrayList<HvmabenutzerParameterDto>();
		if(beans != null) {
			for (HvmabenutzerParameter bean : beans) {
				dtos.add(createDto(bean));
			}
		}
		
		return dtos;
	}
}
