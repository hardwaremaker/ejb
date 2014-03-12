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
package com.lp.server.rechnung.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.rechnung.ejb.Rechnungposition;

public class RechnungPositionDtoAssembler {
	public static RechnungPositionDto createDto(
			Rechnungposition rechnungPosition) {
		RechnungPositionDto rechnungPositionDto = new RechnungPositionDto();
		if (rechnungPosition != null) {
			rechnungPositionDto.setIId(rechnungPosition.getIId());
			rechnungPositionDto.setRechnungIId(rechnungPosition
					.getRechnungIId());
			rechnungPositionDto.setRechnungpositionartCNr(rechnungPosition
					.getPositionsartCNr());
			rechnungPositionDto.setISort(rechnungPosition.getISort());
			rechnungPositionDto.setRechnungIIdGutschrift(rechnungPosition
					.getRechnungIIdGutschrift());
			rechnungPositionDto.setLieferscheinIId(rechnungPosition
					.getLieferscheinIId());
			rechnungPositionDto.setRechnungpositionIId(rechnungPosition
					.getRechnungpositionIId());
			rechnungPositionDto.setAuftragpositionIId(rechnungPosition
					.getAuftragpositionIId());
			rechnungPositionDto.setCBez(rechnungPosition.getCBez());
			rechnungPositionDto.setCZusatzbez(rechnungPosition.getCZbez());
			rechnungPositionDto.setXTextinhalt(rechnungPosition
					.getXTextinhalt());
			rechnungPositionDto.setMediastandardIId(rechnungPosition
					.getMediastandardIId());
			rechnungPositionDto.setArtikelIId(rechnungPosition.getArtikelIId());
			rechnungPositionDto.setNMenge(rechnungPosition.getNMenge());
			rechnungPositionDto.setEinheitCNr(rechnungPosition.getEinheitCNr());
			rechnungPositionDto.setBDrucken(rechnungPosition.getBDrucken());
			rechnungPositionDto.setFKupferzuschlag(rechnungPosition
					.getFKupferzuschlag());
			rechnungPositionDto.setFRabattsatz(rechnungPosition
					.getFRabattsatz());
			rechnungPositionDto.setBRabattsatzuebersteuert(rechnungPosition
					.getBRabattsatzuebersteuert());
			rechnungPositionDto.setMwstsatzIId(rechnungPosition
					.getMwstsatzIId());
			rechnungPositionDto.setBMwstsatzuebersteuert(rechnungPosition
					.getBMwstsatzuebersteuert());
			rechnungPositionDto.setBNettopreisuebersteuert(rechnungPosition
					.getBNettopreisuebersteuert());
			rechnungPositionDto.setNEinzelpreis(rechnungPosition
					.getNEinzelpreis());
			rechnungPositionDto
					.setNEinzelpreisplusversteckteraufschlag(rechnungPosition
							.getNEinzelpreisplusaufschlag());
			rechnungPositionDto.setNNettoeinzelpreis(rechnungPosition
					.getNNettoeinzelpreis());
			rechnungPositionDto
					.setNNettoeinzelpreisplusversteckteraufschlag(rechnungPosition
							.getNNettoeinzelpreisplusaufschlag());
			rechnungPositionDto
					.setNNettoeinzelpreisplusversteckteraufschlagminusrabatte(rechnungPosition
							.getNNettoeinzelpreisplusaufschlagminusrabatt());
			rechnungPositionDto.setNBruttoeinzelpreis(rechnungPosition
					.getNBruttoeinzelpreis());
			rechnungPositionDto.setFZusatzrabattsatz(rechnungPosition
					.getFZusatzrabattsatz());
			rechnungPositionDto.setPositioniId(rechnungPosition
					.getPositionIId());
			rechnungPositionDto.setPositioniIdArtikelset(rechnungPosition
					.getPositionIIdArtikelset());
			rechnungPositionDto.setTypCNr(rechnungPosition.getTypCNr());
			rechnungPositionDto.setVerleihIId(rechnungPosition.getVerleihIId());
			rechnungPositionDto.setKostentraegerIId(rechnungPosition
					.getKostentraegerIId());

			rechnungPositionDto.setZwsVonPosition(rechnungPosition
					.getZwsVonPosition());
			rechnungPositionDto.setZwsBisPosition(rechnungPosition
					.getZwsBisPosition());
			rechnungPositionDto.setZwsNettoSumme(rechnungPosition
					.getZwsNettoSumme());
			rechnungPositionDto.setCLvposition(rechnungPosition
					.getCLvposition());
			rechnungPositionDto.setNMaterialzuschlag(rechnungPosition
					.getNMaterialzuschlag());
		}
		return rechnungPositionDto;
	}

	public static RechnungPositionDto[] createDtos(
			Collection<?> rechnungPositions) {
		List<RechnungPositionDto> list = new ArrayList<RechnungPositionDto>();
		if (rechnungPositions != null) {
			Iterator<?> iterator = rechnungPositions.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Rechnungposition) iterator.next()));
			}
		}
		RechnungPositionDto[] returnArray = new RechnungPositionDto[list.size()];
		return (RechnungPositionDto[]) list.toArray(returnArray);
	}
}
