package com.lp.server.personal.ejbfac;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.lp.server.personal.ejb.Hvmaparameter;
import com.lp.server.personal.service.HvmaparameterDto;

public class HvmaparameterDtoAssembler {
	public static HvmaparameterDto createDto(Hvmaparameter bean) {
		HvmaparameterDto dto = new HvmaparameterDto();
		if(bean != null) {
			dto.setCNr(bean.getCNr());
			dto.setKategorie(bean.getKategorie());
			dto.setDefaultWert(bean.getDefaultWert());
			dto.setDatentyp(bean.getDatentyp());
			dto.setBemerkung(bean.getBemerkung());
			dto.setUebertragen(bean.getUebertragen());
		}
		return dto;
	}
	
	public static HvmaparameterDto[] createDtos(Collection<Hvmaparameter> beans) {
		return createListDtos(beans).toArray(new HvmaparameterDto[0]);
	}
	
	public static List<HvmaparameterDto> createListDtos(Collection<Hvmaparameter> beans) {
		List<HvmaparameterDto> dtos = new ArrayList<HvmaparameterDto>();
		if(beans != null) {
			for (Hvmaparameter bean : beans) {
				dtos.add(createDto(bean));				
			}
		}
		return dtos;
	}
}
