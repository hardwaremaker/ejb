package com.lp.server.personal.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.personal.ejb.Passivereise;


public class PassivereiseDtoAssembler {
	public static PassivereiseDto createDto(Passivereise pr) {
		PassivereiseDto kollektivuestdDto = new PassivereiseDto();
		if (pr != null) {
			kollektivuestdDto.setIId(pr.getIId());
			kollektivuestdDto.setKollektivIId(pr.getKollektivIId());
			kollektivuestdDto.setUBis(pr.getUBis());
			kollektivuestdDto.setBRestdestages(pr
					.getBRestdestages());
			kollektivuestdDto.setUAb(pr.getUAb());
			kollektivuestdDto.setTagesartIId(pr.getTagesartIId());
			
		}
		return kollektivuestdDto;
	}

	public static PassivereiseDto[] createDtos(Collection<?> kollektivuestds) {
		List<PassivereiseDto> list = new ArrayList<PassivereiseDto>();
		if (kollektivuestds != null) {
			Iterator<?> iterator = kollektivuestds.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Passivereise) iterator.next()));
			}
		}
		PassivereiseDto[] returnArray = new PassivereiseDto[list.size()];
		return (PassivereiseDto[]) list.toArray(returnArray);
	}
}
