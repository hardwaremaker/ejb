package com.lp.server.fertigung.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.fertigung.ejb.Bedarfsuebernahme;

public class BedarfsuebernahmeDtoAssembler {
	public static BedarfsuebernahmeDto createDto(Bedarfsuebernahme bean) {
		BedarfsuebernahmeDto dto = new BedarfsuebernahmeDto();
		if (bean != null) {
			dto.setIId(bean.getIId());
			dto.setArtikelIId(bean.getArtikelIId());
			dto.setBAbgang(bean.getBAbgang());
			dto.setBzusaetzlich(bean.getBZusaetzlich());
			dto.setCArtikelbezeichnung(bean.getCArtikelbezeichnung());
			dto.setCLosnummer(bean.getCLosnummer());
			dto.setCArtikelnummer(bean.getCArtikelnummer());
			dto.setCKommentar(bean.getCKommentar());
			dto.setLosIId(bean.getLosIId());
			dto.setLossollmaterialIId(bean.getLossollmaterialIId());
			
			dto.setNWunschmenge(bean.getNWunschmenge());
			dto.setOMedia(bean.getOMedia());
			dto.setPersonalIIdAendern(bean.getPersonalIIdAendern());
			dto.setPersonalIIdAnlegen(bean.getPersonalIIdAnlegen());
			dto.setPersonalIIdVerbuchtGedruckt(bean
					.getPersonalIIdVerbuchtGedruckt());
			dto.setTAendern(bean.getTAendern());
			dto.setTAnlegen(bean.getTAnlegen());
			dto.setTVerbuchtGedruckt(bean.getTVerbuchtGedruckt());
			dto.setTWunschtermin(bean.getTWunschtermin());
			dto.setStatusCNr(bean.getStatusCNr());

		}
		return dto;
	}

	public static BedarfsuebernahmeDto[] createDtos(Collection<?> beans) {
		List<BedarfsuebernahmeDto> list = new ArrayList<BedarfsuebernahmeDto>();
		if (beans != null) {
			Iterator<?> iterator = beans.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Bedarfsuebernahme) iterator.next()));
			}
		}
		BedarfsuebernahmeDto[] returnArray = new BedarfsuebernahmeDto[list
				.size()];
		return (BedarfsuebernahmeDto[]) list.toArray(returnArray);
	}
}
