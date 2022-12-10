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
package com.lp.server.stueckliste.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.stueckliste.ejb.Pruefkombination;

public class PruefkombinationDtoAssembler {
	public static PruefkombinationDto createDto(Pruefkombination bean) {
		PruefkombinationDto dto = new PruefkombinationDto();
		if (bean != null) {
			dto.setIId(bean.getIId());
			dto.setPruefartIId(bean.getPruefartIId());
			dto.setArtikelIIdKontakt(bean.getArtikelIIdKontakt());
			dto.setArtikelIIdLitze(bean.getArtikelIIdLitze());
			dto.setBStandard(bean.getBStandard());
			dto.setNCrimpbreitDraht(bean.getNCrimpbreitDraht());
			dto.setNCrimphoeheDraht(bean.getNCrimphoeheDraht());
			dto.setNCrimphoeheIsolation(bean.getNCrimphoeheIsolation());
			dto.setNCrimpbreiteIsolation(bean.getNCrimpbreiteIsolation());
			dto.setPersonalIIdAendern(bean.getPersonalIIdAendern());
			dto.setPersonalIIdAnlegen(bean.getPersonalIIdAnlegen());

			dto.setNToleranzCrimpbreitDraht(bean.getNToleranzCrimpbreitDraht());
			dto.setNToleranzCrimphoeheDraht(bean.getNToleranzCrimphoeheDraht());
			dto.setNToleranzCrimphoeheIsolation(bean
					.getNToleranzCrimphoeheIsolation());
			dto.setNToleranzCrimpbreiteIsolation(bean
					.getNToleranzCrimpbreiteIsolation());

			dto.setNToleranzWert(bean.getNToleranzWert());
			dto.setNWert(bean.getNWert());

			dto.setTAendern(bean.getTAendern());
			dto.setTAnlegen(bean.getTAnlegen());
			dto.setVerschleissteilIId(bean.getVerschleissteilIId());

			dto.setArtikelIIdLitze2(bean.getArtikelIIdLitze2());
			dto.setNAbzugskraftLitze(bean.getNAbzugskraftLitze());
			dto.setNAbzugskraftLitze2(bean.getNAbzugskraftLitze2());

			dto.setBDoppelanschlag(bean.getBDoppelanschlag());

		}
		return dto;
	}

	public static PruefkombinationDto[] createDtos(Collection<?> dtos) {
		List<PruefkombinationDto> list = new ArrayList<PruefkombinationDto>();
		if (dtos != null) {
			Iterator<?> iterator = dtos.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Pruefkombination) iterator.next()));
			}
		}
		PruefkombinationDto[] returnArray = new PruefkombinationDto[list.size()];
		return (PruefkombinationDto[]) list.toArray(returnArray);
	}
}
