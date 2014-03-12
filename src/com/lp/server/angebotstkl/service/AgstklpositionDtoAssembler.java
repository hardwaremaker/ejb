/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2014 HELIUM V IT-Solutions GmbH
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

import com.lp.server.angebotstkl.ejb.Agstklposition;

public class AgstklpositionDtoAssembler {
	public static AgstklpositionDto createDto(Agstklposition agstklposition) {
		AgstklpositionDto agstklpositionDto = new AgstklpositionDto();
		if (agstklposition != null) {
			agstklpositionDto.setIId(agstklposition.getIId());
			agstklpositionDto.setAgstklIId(agstklposition.getAgstklIId());
			agstklpositionDto.setISort(agstklposition.getISort());
			agstklpositionDto.setAgstklpositionsartCNr(agstklposition
					.getAgstklpositionsartCNr());
			agstklpositionDto.setArtikelIId(agstklposition.getArtikelIId());
			agstklpositionDto.setCBez(agstklposition.getCBez());
			agstklpositionDto.setCZbez(agstklposition.getCZbez());
			agstklpositionDto.setBArtikelbezeichnunguebersteuert(agstklposition
					.getBArtikelbezeichnunguebersteuert());
			agstklpositionDto.setNMenge(agstklposition.getNMenge());
			agstklpositionDto.setEinheitCNr(agstklposition.getEinheitCNr());
			agstklpositionDto.setFRabattsatz(agstklposition.getFRabattsatz());
			agstklpositionDto.setBRabattsatzuebersteuert(agstklposition
					.getBRabattsatzuebersteuert());
			agstklpositionDto.setFZusatzrabattsatz(agstklposition
					.getFZusatzrabattsatz());
			agstklpositionDto.setNNettoeinzelpreis(agstklposition
					.getNNettoeinzelpreis());
			agstklpositionDto.setNNettogesamtpreis(agstklposition
					.getNNettogesamtpreis());
			agstklpositionDto.setBDrucken(agstklposition.getBDrucken());
			agstklpositionDto.setNGestehungspreis(agstklposition
					.getNGestehungspreis());
			agstklpositionDto.setNMaterialzuschlag(agstklposition
					.getNMaterialzuschlag());
			agstklpositionDto.setBAufschlaggesamtFixiert(agstklposition
					.getBAufschlaggesamtFixiert());
			agstklpositionDto.setFAufschlag(agstklposition.getFAufschlag());
			agstklpositionDto.setNAufschlag(agstklposition.getNAufschlag());
			agstklpositionDto.setNNettogesamtmitaufschlag(agstklposition
					.getNNettogesamtmitaufschlag());
		}
		return agstklpositionDto;
	}

	public static AgstklpositionDto[] createDtos(Collection<?> agstklpositions) {
		List<AgstklpositionDto> list = new ArrayList<AgstklpositionDto>();
		if (agstklpositions != null) {
			Iterator<?> iterator = agstklpositions.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Agstklposition) iterator.next()));
			}
		}
		AgstklpositionDto[] returnArray = new AgstklpositionDto[list.size()];
		return (AgstklpositionDto[]) list.toArray(returnArray);
	}
}
