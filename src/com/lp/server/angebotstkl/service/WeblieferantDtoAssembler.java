package com.lp.server.angebotstkl.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.lp.server.angebotstkl.ejb.Weblieferant;

public class WeblieferantDtoAssembler {

	public static WeblieferantDto createDto(Weblieferant weblieferant) {
		WeblieferantDto dto = new WeblieferantDto();
		
		if (weblieferant != null) {
			dto.setIId(weblieferant.getIId());
			dto.setISort(weblieferant.getISort());
			dto.setWebpartnerIId(weblieferant.getWebpartnerIId());
		}
		
		return dto;
	}
	
	public static List<WeblieferantDto> createDtos(Collection<Weblieferant> collection) {
		List<WeblieferantDto> list = new ArrayList<WeblieferantDto>();
		
		if (collection != null) {
			for (Weblieferant weblieferant : collection) {
				list.add(createDto(weblieferant));
			}
		}
		return list;
	}
}
