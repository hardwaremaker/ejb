package com.lp.server.angebotstkl.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.angebotstkl.ejb.AgstklmengenstaffelSchnellerfassung;

public class AgstklmengenstaffelSchnellerfassungDtoAssembler {
	public static AgstklmengenstaffelSchnellerfassungDto createDto(AgstklmengenstaffelSchnellerfassung bean) {
		AgstklmengenstaffelSchnellerfassungDto dto = new AgstklmengenstaffelSchnellerfassungDto();
		if (bean != null) {
			dto.setIId(bean.getIId());
			dto.setAgstklIId(bean.getAgstklIId());
			dto.setNMenge(bean.getNMenge());
			dto.setNAufschlagAz(bean.getNAufschlagAz());
			dto.setNAufschlagMaterial(bean.getNAufschlagMaterial());
			dto.setNWertAz(bean.getNWertAz());
			dto.setNWertMaterial(bean.getNWertMaterial());
			dto.setNPreisEinheit(bean.getNPreisEinheit());

		}
		return dto;
	}

	public static AgstklmengenstaffelSchnellerfassungDto[] createDtos(Collection<?> stuecklistearbeitsplans) {
		List<AgstklmengenstaffelSchnellerfassungDto> list = new ArrayList<AgstklmengenstaffelSchnellerfassungDto>();
		if (stuecklistearbeitsplans != null) {
			Iterator<?> iterator = stuecklistearbeitsplans.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((AgstklmengenstaffelSchnellerfassung) iterator.next()));
			}
		}
		AgstklmengenstaffelSchnellerfassungDto[] returnArray = new AgstklmengenstaffelSchnellerfassungDto[list.size()];
		return (AgstklmengenstaffelSchnellerfassungDto[]) list.toArray(returnArray);
	}
}
