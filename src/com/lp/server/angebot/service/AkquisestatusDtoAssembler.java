package com.lp.server.angebot.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.angebot.ejb.Akquisestatus;


public class AkquisestatusDtoAssembler {
	public static AkquisestatusDto createDto(Akquisestatus zusatzstatus) {
		AkquisestatusDto zusatzstatusDto = new AkquisestatusDto();
		if (zusatzstatus != null) {
			zusatzstatusDto.setIId(zusatzstatus.getIId());
			zusatzstatusDto.setMandantCNr(zusatzstatus.getMandantCNr());
			zusatzstatusDto.setCBez(zusatzstatus.getCBez());
			zusatzstatusDto.setISort(zusatzstatus.getISort());
		}
		return zusatzstatusDto;
	}

	public static AkquisestatusDto[] createDtos(Collection<?> zusatzstatuss) {
		List<AkquisestatusDto> list = new ArrayList<AkquisestatusDto>();
		if (zusatzstatuss != null) {
			Iterator<?> iterator = zusatzstatuss.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Akquisestatus) iterator.next()));
			}
		}
		AkquisestatusDto[] returnArray = new AkquisestatusDto[list.size()];
		return (AkquisestatusDto[]) list.toArray(returnArray);
	}
}
