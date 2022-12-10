package com.lp.server.artikel.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.artikel.ejb.Dateiverweis;

public class DateiverweisDtoAssembler {
	public static DateiverweisDto createDto(Dateiverweis bean) {
		DateiverweisDto dto = new DateiverweisDto();
		if (bean != null) {
			dto.setIId(bean.getIId());

			dto.setMandantCNr(bean.getMandantCNr());
			dto.setCLaufwerk(bean.getCLaufwerk());
			dto.setCUnc(bean.getCUnc());
		}
		return dto;
	}

	public static DateiverweisDto[] createDtos(Collection<?> panelbeschreibungs) {
		List<DateiverweisDto> list = new ArrayList<DateiverweisDto>();
		if (panelbeschreibungs != null) {
			Iterator<?> iterator = panelbeschreibungs.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Dateiverweis) iterator.next()));
			}
		}
		DateiverweisDto[] returnArray = new DateiverweisDto[list.size()];
		return (DateiverweisDto[]) list.toArray(returnArray);
	}
}
