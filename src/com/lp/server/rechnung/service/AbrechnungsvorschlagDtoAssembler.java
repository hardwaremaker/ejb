package com.lp.server.rechnung.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.rechnung.ejb.Abrechnungsvorschlag;


public class AbrechnungsvorschlagDtoAssembler {
	public static AbrechnungsvorschlagDto createDto(Abrechnungsvorschlag bean) {
		AbrechnungsvorschlagDto dto = new AbrechnungsvorschlagDto();
		if (bean != null) {
			dto.setIId(bean.getIId());
			dto.setMandantCNr(bean.getMandantCNr());
			dto.setAuftragIId(bean.getAuftragIId());
			dto.setAuftragszuordnungIId(bean.getAuftragszuordnungIId());
			dto.setLosIId(bean.getLosIId());
			dto.setProjektIId(bean.getProjektIId());
			dto.setNBetragOffen(bean
					.getNBetragOffen());
			dto.setNStundenOffen(bean
					.getNStundenOffen());
			dto.setReiseIId(bean.getReiseIId());
			dto.setTAnlegen(bean.getTAnlegen());
			dto.setZeitdatenIId(bean
					.getZeitdatenIId());
			dto.setKundeIId(bean.getKundeIId());
			dto.setTelefonzeitenIId(bean.getTelefonzeitenIId());
			dto.setTVon(bean.getTVon());
			dto.setTBis(bean.getTBis());
			dto.setPersonalIId(bean.getPersonalIId());
			dto.setBVerrechnet(bean.getBVerrechnet());
			dto.setMaschinenzeitdatenIId(bean.getMaschinenzeitdatenIId());
			dto.setNBetragGesamt(bean
					.getNBetragGesamt());
			dto.setNStundenGesamt(bean
					.getNStundenGesamt());
			dto.setFVerrechenbar(bean.getFVerrechenbar());
			dto.setNBetragVerrechenbar(bean.getNBetragVerrechenbar());
			dto.setNStundenVerrechenbar(bean.getNStundenVerrechenbar());
			dto.setNKilometerGesamt(bean.getNKilometerGesamt());
			dto.setNKilometerOffen(bean.getNKilometerOffen());
			dto.setNKilometerVerrechenbar(bean.getNKilometerVerrechenbar());
			dto.setAuftragpositionIId(bean.getAuftragpositionIId());
			dto.setNSpesenGesamt(bean.getNSpesenGesamt());
			dto.setNSpesenOffen(bean.getNSpesenOffen());
			dto.setNSpesenVerrechenbar(bean.getNSpesenVerrechenbar());
			dto.setWaehrungCNrRechnung(bean.getWaehrungCNrRechnung());
		}
		return dto;
	}

	public static AbrechnungsvorschlagDto[] createDtos(Collection<?> zeitdatens) {
		List<AbrechnungsvorschlagDto> list = new ArrayList<AbrechnungsvorschlagDto>();
		if (zeitdatens != null) {
			Iterator<?> iterator = zeitdatens.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Abrechnungsvorschlag) iterator.next()));
			}
		}
		AbrechnungsvorschlagDto[] returnArray = new AbrechnungsvorschlagDto[list.size()];
		return (AbrechnungsvorschlagDto[]) list.toArray(returnArray);
	}
}
