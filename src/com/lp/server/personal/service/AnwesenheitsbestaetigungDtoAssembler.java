package com.lp.server.personal.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.personal.ejb.Anwesenheitsbestaetigung;

public class AnwesenheitsbestaetigungDtoAssembler {
	public static AnwesenheitsbestaetigungDto createDto(Anwesenheitsbestaetigung bean) {
		AnwesenheitsbestaetigungDto dto = new AnwesenheitsbestaetigungDto();
		if (bean != null) {
			dto.setIId(bean.getIId());
			dto.setAuftragIId(bean.getAuftragIId());
			dto.setCBemerkung(bean.getCBemerkung());
			dto.setDatenformatCNr(bean.getDatenformatCNr());
			dto.setOUnterschrift(bean.getOUnterschrift());
			dto.setPersonalIId(bean.getPersonalIId());
			dto.setProjektIId(bean.getProjektIId());
			dto.setTUnterschrift(bean.getTUnterschrift());
			dto.setTVersandt(bean.getTVersandt());
			dto.setOPdf(bean.getOPdf());
			dto.setCName(bean.getCName());
			dto.setILfdnr(bean.getILfdnr());
		}
		return dto;
	}

	public static AnwesenheitsbestaetigungDto[] createDtos(Collection<?> schweres) {
		List<AnwesenheitsbestaetigungDto> list = new ArrayList<AnwesenheitsbestaetigungDto>();
		if (schweres != null) {
			Iterator<?> iterator = schweres.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Anwesenheitsbestaetigung) iterator.next()));
			}
		}
		AnwesenheitsbestaetigungDto[] returnArray = new AnwesenheitsbestaetigungDto[list.size()];
		return (AnwesenheitsbestaetigungDto[]) list.toArray(returnArray);
	}
}
