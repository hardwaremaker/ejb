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
package com.lp.server.anfrage.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.anfrage.ejb.Anfrageposition;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

public class AnfragepositionDtoAssembler {
	public static AnfragepositionDto createDto(Anfrageposition anfrageposition) {
		AnfragepositionDto anfragepositionDto = new AnfragepositionDto();
		if (anfrageposition != null) {
			anfragepositionDto.setUsecaseIIdQuelle(QueryParameters.UC_ID_ANFRAGEPOSITION);
			anfragepositionDto.setIId(anfrageposition.getIId());
			anfragepositionDto.setBelegIId(anfrageposition.getAnfrageIId());
			anfragepositionDto.setISort(anfrageposition.getISort());
			anfragepositionDto.setPositionsartCNr(anfrageposition
					.getAnfragepositionartCNr());
			anfragepositionDto.setArtikelIId(anfrageposition.getArtikelIId());
			anfragepositionDto.setCBez(anfrageposition.getCBez());
			anfragepositionDto
					.setBArtikelbezeichnunguebersteuert(anfrageposition
							.getBArtikelbezeichnunguebersteuert());
			anfragepositionDto.setXTextinhalt(anfrageposition.getXTextinhalt());
			anfragepositionDto.setMediastandardIId(anfrageposition
					.getMediastandardIId());
			anfragepositionDto.setNMenge(anfrageposition.getNMenge());
			anfragepositionDto.setEinheitCNr(anfrageposition.getEinheitCNr());
			anfragepositionDto.setNRichtpreis(anfrageposition.getNRichtpreis());
			anfragepositionDto.setCZusatzbez(anfrageposition.getCZbez());
			anfragepositionDto.setAnfragepositionIdZugehoerig(anfrageposition
					.getAnfragepositionIdZugehoerig());
			anfragepositionDto.setLossollmaterialIId(anfrageposition.getLossollmaterialIId());

		}
		return anfragepositionDto;
	}

	public static AnfragepositionDto[] createDtos(Collection<?> anfragepositions) {
		List<AnfragepositionDto> list = new ArrayList<AnfragepositionDto>();
		if (anfragepositions != null) {
			Iterator<?> iterator = anfragepositions.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Anfrageposition) iterator.next()));
			}
		}
		AnfragepositionDto[] returnArray = new AnfragepositionDto[list.size()];
		return (AnfragepositionDto[]) list.toArray(returnArray);
	}
}
