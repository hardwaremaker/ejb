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
package com.lp.server.angebotstkl.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.angebotstkl.ejb.Einkaufsangebotposition;

public class EinkaufsangebotpositionDtoAssembler {
	public static EinkaufsangebotpositionDto createDto(
			Einkaufsangebotposition einkaufsangebotposition) {
		EinkaufsangebotpositionDto einkaufsangebotpositionDto = new EinkaufsangebotpositionDto();
		if (einkaufsangebotposition != null) {
			einkaufsangebotpositionDto.setIId(einkaufsangebotposition.getIId());
			einkaufsangebotpositionDto.setBelegIId(einkaufsangebotposition
					.getEinkaufsangebotIId());
			einkaufsangebotpositionDto.setISort(einkaufsangebotposition
					.getISort());
			einkaufsangebotpositionDto
					.setPositionsartCNr(einkaufsangebotposition
							.getAgstklpositionsartCNr());
			einkaufsangebotpositionDto.setArtikelIId(einkaufsangebotposition
					.getArtikelIId());
			einkaufsangebotpositionDto.setCBez(einkaufsangebotposition
					.getCBez());
			einkaufsangebotpositionDto.setCBemerkung(einkaufsangebotposition
					.getCBemerkung());
			einkaufsangebotpositionDto.setCZusatzbez(einkaufsangebotposition
					.getCZbez());
			einkaufsangebotpositionDto
					.setBArtikelbezeichnunguebersteuert(einkaufsangebotposition
							.getBArtikelbezeichnunguebersteuert());
			einkaufsangebotpositionDto.setNMenge(einkaufsangebotposition
					.getNMenge());
			einkaufsangebotpositionDto.setNPreis1(einkaufsangebotposition
					.getNPreis1());
			einkaufsangebotpositionDto.setNPreis2(einkaufsangebotposition
					.getNPreis2());
			einkaufsangebotpositionDto.setNPreis3(einkaufsangebotposition
					.getNPreis3());
			einkaufsangebotpositionDto.setNPreis4(einkaufsangebotposition
					.getNPreis4());
			einkaufsangebotpositionDto.setNPreis5(einkaufsangebotposition
					.getNPreis5());
			einkaufsangebotpositionDto.setEinheitCNr(einkaufsangebotposition
					.getEinheitCNr());
			einkaufsangebotpositionDto
					.setIVerpackungseinheit(einkaufsangebotposition
							.getIVerpackungseinheit());
			einkaufsangebotpositionDto
					.setIWiederbeschaffungszeit(einkaufsangebotposition
							.getIWiederbeschaffungszeit());
			einkaufsangebotpositionDto
					.setFMindestbestellmenge(einkaufsangebotposition
							.getFMindestbestellmenge());
			einkaufsangebotpositionDto
			.setCPosition(einkaufsangebotposition
					.getCPosition());
			einkaufsangebotpositionDto
			.setCInternebemerkung(einkaufsangebotposition
					.getCInternebemerkung());
			einkaufsangebotpositionDto
			.setBMitdrucken(einkaufsangebotposition
					.getBMitdrucken());
			einkaufsangebotpositionDto.setCKommentar1(einkaufsangebotposition.getCKommentar1());
			einkaufsangebotpositionDto.setCKommentar2(einkaufsangebotposition.getCKommentar2());
		}
		return einkaufsangebotpositionDto;
	}

	public static EinkaufsangebotpositionDto[] createDtos(
			Collection<?> einkaufsangebotpositions) {
		List<EinkaufsangebotpositionDto> list = new ArrayList<EinkaufsangebotpositionDto>();
		if (einkaufsangebotpositions != null) {
			Iterator<?> iterator = einkaufsangebotpositions.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Einkaufsangebotposition) iterator.next()));
			}
		}
		EinkaufsangebotpositionDto[] returnArray = new EinkaufsangebotpositionDto[list
				.size()];
		return (EinkaufsangebotpositionDto[]) list.toArray(returnArray);
	}
}
