package com.lp.server.angebotstkl.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.lp.server.angebotstkl.ejb.WebFindChips;
import com.lp.server.partner.ejb.WeblieferantFarnell;
import com.lp.server.partner.service.WeblieferantFarnellDto;

public class WebPartnerDtoAssembler {

	public static <T extends WebpartnerDto> T createDto(IWebpartner webpartner) {
		WebpartnerDto dto = new WebpartnerDto();
		if (webpartner == null) return (T) dto;
		
		if (webpartner instanceof WebFindChips) {
			WebFindChipsDto webfindchipsDto = new WebFindChipsDto();
			webfindchipsDto.setcDistributor(((WebFindChips)webpartner).getcDistributor());
			webfindchipsDto.setcName(((WebFindChips)webpartner).getcName());
			dto = webfindchipsDto;
		}
		
		if (webpartner instanceof WeblieferantFarnell) {
			WeblieferantFarnellDto farnellDto = new WeblieferantFarnellDto();
			farnellDto.setCApiKey(((WeblieferantFarnell)webpartner).getCApiKey());
			farnellDto.setCUrl(((WeblieferantFarnell)webpartner).getCUrl());
			farnellDto.setCCustomerId(((WeblieferantFarnell)webpartner).getCCustomerId());
			farnellDto.setCCustomerKey(((WeblieferantFarnell)webpartner).getCCustomerKey());
			farnellDto.setCStore(((WeblieferantFarnell)webpartner).getCStore());
			dto = farnellDto;
		}
		
		dto.setIId(webpartner.getIId());
		dto.setLieferantIId(webpartner.getLieferantIId());
		dto.setWebabfrageIId(webpartner.getWebabfrageIId());
		
		return (T) dto;
	}
		
	public static <T extends IWebpartner, E extends WebpartnerDto> List<E> createDtos(Collection<T> collection) {
		List<E> list = new ArrayList<E>();
		
		if (collection != null) {
			for (T webpartner : collection) {
				E webpartnerDto = createDto(webpartner);
				list.add(webpartnerDto);
			}
		}
		return list;
	}
}
