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
package com.lp.server.system.ejbfac;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.system.ejb.Lieferart;
import com.lp.server.system.service.LieferartDto;

public class LieferartDtoAssembler {
	public static Lieferart set(Lieferart lieferart, LieferartDto dto) {
		lieferart.setCNr(dto.getCNr());
		lieferart.setBFrachtkostenalserledigtverbuchen(dto
				.getBFrachtkostenalserledigtverbuchen());
		lieferart.setBVersteckt(dto.getBVersteckt());
		lieferart.setCVersandort(dto.getCVersandort());
		lieferart.setILieferort(dto.getILieferort());
		lieferart.setCExtern(dto.getCExtern());
		lieferart.setArtikelIIdVersand(dto.getArtikelIIdVersand());
		return lieferart;
	}
	
	public static Lieferart flush(EntityManager em, Lieferart lieferart) {
		em.merge(lieferart);
		em.flush();
		
		return lieferart;		
	}
	
	public static Lieferart setFlush(EntityManager em,
			Lieferart lieferart, LieferartDto lieferartDto) {
		set(lieferart, lieferartDto);
		return flush(em, lieferart);
	}
	
	public static LieferartDto createDto(Lieferart lieferart) {
		LieferartDto lieferartDto = new LieferartDto();
		if (lieferart != null) {
			lieferartDto.setIId(lieferart.getIId());
			lieferartDto.setCNr(lieferart.getCNr());
			lieferartDto.setBFrachtkostenalserledigtverbuchen(lieferart
					.getBFrachtkostenalserledigtverbuchen());
			lieferartDto.setCVersandort(lieferart.getCVersandort());
			lieferartDto.setMandantCNr(lieferart.getMandantCNr());
			lieferartDto.setBVersteckt(lieferart.getBVersteckt());
			lieferartDto.setILieferort(lieferart.getILieferort());
			lieferartDto.setCExtern(lieferart.getCExtern());
			lieferartDto.setArtikelIIdVersand(lieferart.getArtikelIIdVersand());
		}
		return lieferartDto;
	}

	public static LieferartDto[] createDtos(Collection<?> lieferarts) {
		List<LieferartDto> list = new ArrayList<LieferartDto>();
		if (lieferarts != null) {
			Iterator<?> iterator = lieferarts.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Lieferart) iterator.next()));
			}
		}
		LieferartDto[] returnArray = new LieferartDto[list.size()];
		return (LieferartDto[]) list.toArray(returnArray);
	}
}
