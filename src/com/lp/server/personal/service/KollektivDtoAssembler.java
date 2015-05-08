/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2015 HELIUM V IT-Solutions GmbH
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published 
 * by the Free Software Foundation, either version 3 of theLicense, or 
 * (at your option) any later version.
 * 
 * According to sec. 7 of the GNU Affero General Public License, version 3, 
 * the terms of the AGPL are supplemented with the following terms:
 * 
 * "HELIUM V" and "HELIUM 5" are registered trademarks of 
 * HELIUM V IT-Solutions GmbH. The licensing of the program under the 
 * AGPL does not imply a trademark license. Therefore any rights, title and
 * interest in our trademarks remain entirely with us. If you want to propagate
 * modified versions of the Program under the name "HELIUM V" or "HELIUM 5",
 * you may only do so if you have a written permission by HELIUM V IT-Solutions 
 * GmbH (to acquire a permission please contact HELIUM V IT-Solutions
 * at trademark@heliumv.com).
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact: developers@heliumv.com
 ******************************************************************************/
package com.lp.server.personal.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.personal.ejb.Kollektiv;

public class KollektivDtoAssembler {
	public static KollektivDto createDto(Kollektiv kollektiv) {
		KollektivDto kollektivDto = new KollektivDto();
		if (kollektiv != null) {
			kollektivDto.setIId(kollektiv.getIId());
			kollektivDto.setCBez(kollektiv.getCBez());
			kollektivDto.setBVerbraucheuestd(kollektiv.getBVerbraucheuestd());
			kollektivDto.setBUestdabsollstderbracht(kollektiv
					.getBUestdabsollstderbracht());
			kollektivDto.setBUestdverteilen(kollektiv.getBUestdverteilen());
			kollektivDto.setNNormalstunden(kollektiv.getNNormalstunden());
			kollektivDto.setNFaktoruestd100(kollektiv.getNFaktoruestd100());
			kollektivDto.setNFaktoruestd50(kollektiv.getNFaktoruestd50());
			kollektivDto.setNFaktormehrstd(kollektiv.getNFaktormehrstd());
			kollektivDto.setUBlockzeitab(kollektiv.getUBlockzeitab());
			kollektivDto.setUBlockzeitbis(kollektiv.getUBlockzeitbis());
			kollektivDto.setNFaktoruestd200(kollektiv.getNFaktoruestd200());
			kollektivDto.setN200prozentigeab(kollektiv.getN200prozentigeab());
		}
		return kollektivDto;
	}

	public static KollektivDto[] createDtos(Collection<?> kollektivs) {
		List<KollektivDto> list = new ArrayList<KollektivDto>();
		if (kollektivs != null) {
			Iterator<?> iterator = kollektivs.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Kollektiv) iterator.next()));
			}
		}
		KollektivDto[] returnArray = new KollektivDto[list.size()];
		return (KollektivDto[]) list.toArray(returnArray);
	}
}
