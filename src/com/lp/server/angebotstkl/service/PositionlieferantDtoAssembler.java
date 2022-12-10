package com.lp.server.angebotstkl.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.angebotstkl.ejb.Positionlieferant;
import com.lp.util.Helper;

public class PositionlieferantDtoAssembler {
	public static PositionlieferantDto createDto(Positionlieferant bean) {
		PositionlieferantDto dto = new PositionlieferantDto();
		if (bean != null) {
			dto.setIId(bean.getIId());
			dto.setEinkaufsangebotpositionIId(bean.getEinkaufsangebotpositionIId());
			dto.setEgaklieferantIId(bean.getEgaklieferantIId());
			dto.setCArtikelnrlieferant(bean.getCArtikelnrlieferant());
			
			dto.setCBemerkung(bean.getCBemerkung());
			dto.setCBemerkungIntern(bean.getCBemerkungIntern());
			dto.setCBemerkungVerkauf(bean.getCBemerkungVerkauf());
			dto.setILieferzeitinkw(bean.getILieferzeitinkw());
			dto.setNLagerstand(bean.getNLagerstand());
			dto.setNMindestbestellmenge(bean.getNMindestbestellmenge());
			dto.setNPreisMenge1(bean.getNPreisMenge1());
			dto.setNPreisMenge2(bean.getNPreisMenge2());
			dto.setNPreisMenge3(bean.getNPreisMenge3());
			dto.setNPreisMenge4(bean.getNPreisMenge4());
			dto.setNPreisMenge5(bean.getNPreisMenge5());
			
			dto.setNTransportkosten(bean.getNTransportkosten());
			dto.setNVerpackungseinheit(bean.getNVerpackungseinheit());
			dto.setTAendern(bean.getTAendern());
			
			dto.setBMenge1Bestellen(bean.getBMenge1Bestellen());
			dto.setBMenge2Bestellen(bean.getBMenge2Bestellen());
			dto.setBMenge3Bestellen(bean.getBMenge3Bestellen());
			dto.setBMenge4Bestellen(bean.getBMenge4Bestellen());
			dto.setBMenge5Bestellen(bean.getBMenge5Bestellen());
			
			

		}
		return dto;
	}

	public static PositionlieferantDto[] createDtos(Collection<?> beans) {
		List<PositionlieferantDto> list = new ArrayList<PositionlieferantDto>();
		if (beans != null) {
			Iterator<?> iterator = beans.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Positionlieferant) iterator.next()));
			}
		}
		PositionlieferantDto[] returnArray = new PositionlieferantDto[list.size()];
		return (PositionlieferantDto[]) list.toArray(returnArray);
	}
}
