package com.lp.server.rechnung.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.rechnung.ejb.Mmz;

public class MmzDtoAssembler {
	public static MmzDto createDto(Mmz mmz) {
		MmzDto mahnstufeDto = new MmzDto();
		if (mmz != null) {
			mahnstufeDto.setIId(mmz.getIId());
			mahnstufeDto.setMandantCNr(mmz.getMandantCNr());
			mahnstufeDto.setArtikelIId(mmz.getArtikelIId());
			mahnstufeDto.setNBisWert(mmz.getNBisWert());
			mahnstufeDto.setNZuschlag(mmz.getNZuschlag());
			mahnstufeDto.setLandIId(mmz.getLandIId());
		}
		return mahnstufeDto;
	}

	public static MmzDto[] createDtos(Collection<?> mahnstufes) {
		List<MmzDto> list = new ArrayList<MmzDto>();
		if (mahnstufes != null) {
			Iterator<?> iterator = mahnstufes.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Mmz) iterator.next()));
			}
		}
		MmzDto[] returnArray = new MmzDto[list.size()];
		return (MmzDto[]) list.toArray(returnArray);
	}
}
