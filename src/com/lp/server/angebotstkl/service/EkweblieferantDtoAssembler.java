package com.lp.server.angebotstkl.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.lp.server.angebotstkl.ejb.Ekweblieferant;

public class EkweblieferantDtoAssembler {

	public static EkweblieferantDto createDto(Ekweblieferant weblieferant) {
		EkweblieferantDto dto = new EkweblieferantDto();
		
		if (weblieferant != null) {
			dto.setIId(weblieferant.getIId());
			dto.setISort(weblieferant.getISort());
			dto.setWebpartnerIId(weblieferant.getWebpartnerIId());
			dto.setEinkaufsangebotIId(weblieferant.getEinkaufsangebotIId());
		}
		
		return dto;
	}
	
	public static List<EkweblieferantDto> createDtos(Collection<Ekweblieferant> collection) {
		List<EkweblieferantDto> list = new ArrayList<EkweblieferantDto>();
		
		if (collection != null) {
			for (Ekweblieferant weblieferant : collection) {
				list.add(createDto(weblieferant));
			}
		}
		return list;
	}
}
