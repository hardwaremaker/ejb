package com.lp.server.artikel.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.artikel.ejb.Materialpreis;

public class MaterialpreisDtoAssembler {
	public static MaterialpreisDto createDto(Materialpreis materialpreis) {
		MaterialpreisDto materialpreisDto = new MaterialpreisDto();
		if (materialpreis != null) {
			materialpreisDto.setMaterialIId(materialpreis.getMaterialIId());
			materialpreisDto.setTGueltigab(materialpreis.getTGueltigab());
			materialpreisDto.setNPreisProKG(materialpreis.getNPreisProKG());
			materialpreisDto.setIId(materialpreis.getIId());
		}
		return materialpreisDto;
	}

	public static MaterialpreisDto[] createDtos(Collection<?> materialpreiss) {
		List<MaterialpreisDto> list = new ArrayList<MaterialpreisDto>();
		if (materialpreiss != null) {
			Iterator<?> iterator = materialpreiss.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Materialpreis) iterator.next()));
			}
		}
		MaterialpreisDto[] returnArray = new MaterialpreisDto[list.size()];
		return (MaterialpreisDto[]) list.toArray(returnArray);
	}
}
