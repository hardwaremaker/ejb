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
package com.lp.server.lieferschein.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.lieferschein.ejb.Lieferscheinposition;
import com.lp.server.util.fastlanereader.service.query.QueryParameters;

public class LieferscheinpositionDtoAssembler {
	public static LieferscheinpositionDto createDto(
			Lieferscheinposition lieferscheinposition) {
		LieferscheinpositionDto lieferscheinpositionDto = new LieferscheinpositionDto();
		if (lieferscheinposition != null) {
			lieferscheinpositionDto.setUsecaseIIdQuelle(QueryParameters.UC_ID_LIEFERSCHEINPOSITION);
			lieferscheinpositionDto.setIId(lieferscheinposition.getIId());
			lieferscheinpositionDto.setLieferscheinIId(lieferscheinposition
					.getLieferscheinIId());
			lieferscheinpositionDto.setISort(lieferscheinposition.getISort());
			lieferscheinpositionDto
					.setLieferscheinpositionartCNr(lieferscheinposition
							.getLieferscheinpositionartCNr());
			lieferscheinpositionDto.setArtikelIId(lieferscheinposition
					.getArtikelIId());
			lieferscheinpositionDto.setCBez(lieferscheinposition.getCBez());
			lieferscheinpositionDto.setCZusatzbez(lieferscheinposition
					.getCZbez());
			lieferscheinpositionDto
					.setBArtikelbezeichnunguebersteuert(lieferscheinposition
							.getBArtikelbezeichnunguebersteuert());
			lieferscheinpositionDto.setXTextinhalt(lieferscheinposition
					.getXTextinhalt());
			lieferscheinpositionDto.setMediastandardIId(lieferscheinposition
					.getMediastandardIId());
			lieferscheinpositionDto.setAuftragpositionIId(lieferscheinposition
					.getAuftragpositionIId());
			lieferscheinpositionDto.setNMenge(lieferscheinposition.getNMenge());
			lieferscheinpositionDto.setEinheitCNr(lieferscheinposition
					.getEinheitCNr());
			lieferscheinpositionDto.setFRabattsatz(lieferscheinposition
					.getFRabattsatz());
			lieferscheinpositionDto
					.setBRabattsatzuebersteuert(lieferscheinposition
							.getBRabattsatzuebersteuert());
			lieferscheinpositionDto.setFZusatzrabattsatz(lieferscheinposition
					.getFZusatzrabattsatz());
			lieferscheinpositionDto.setFKupferzuschlag(lieferscheinposition
					.getFKupferzuschlag());
			lieferscheinpositionDto.setMwstsatzIId(lieferscheinposition
					.getMwstsatzIId());
			lieferscheinpositionDto
					.setBMwstsatzuebersteuert(lieferscheinposition
							.getBMwstsatzuebersteuert());
			lieferscheinpositionDto
					.setBNettopreisuebersteuert(lieferscheinposition
							.getBNettopreisuebersteuert());
			lieferscheinpositionDto.setNEinzelpreis(lieferscheinposition
					.getNNettoeinzelpreis());
			lieferscheinpositionDto
					.setNEinzelpreisplusversteckteraufschlag(lieferscheinposition
							.getNNettoeinzelpreisplusversteckteraufschlag());
			lieferscheinpositionDto.setNRabattbetrag(lieferscheinposition
					.getNRabattbetrag());
			lieferscheinpositionDto.setNNettoeinzelpreis(lieferscheinposition
					.getNNettogesamtpreis());
			lieferscheinpositionDto
					.setNNettoeinzelpreisplusversteckteraufschlag(lieferscheinposition
							.getNNettogesamtpreisplusversteckteraufschlag());
			lieferscheinpositionDto
					.setNNettoeinzelpreisplusversteckteraufschlagminusrabatte(lieferscheinposition
							.getNNettogesamtpreisplusversteckteraufschlagminusrabatte());
			lieferscheinpositionDto.setNMwstbetrag(lieferscheinposition
					.getNMwstbetrag());
			lieferscheinpositionDto.setNBruttoeinzelpreis(lieferscheinposition
					.getNBruttogesamtpreis());
			lieferscheinpositionDto.setPositioniId(lieferscheinposition
					.getPositionIId());
			lieferscheinpositionDto
					.setPositioniIdArtikelset(lieferscheinposition
							.getPositionIIdArtikelset());
			lieferscheinpositionDto.setTypCNr(lieferscheinposition.getTypCNr());
			lieferscheinpositionDto.setVerleihIId(lieferscheinposition
					.getVerleihIId());
			lieferscheinpositionDto.setKostentraegerIId(lieferscheinposition
					.getKostentraegerIId());
			lieferscheinpositionDto.setBKeinlieferrest(lieferscheinposition
					.getBKeinlieferrest());
			lieferscheinpositionDto.setZwsVonPosition(lieferscheinposition
					.getZwsVonPosition());
			lieferscheinpositionDto.setZwsBisPosition(lieferscheinposition
					.getZwsBisPosition());
			lieferscheinpositionDto.setZwsNettoSumme(lieferscheinposition
					.getZwsNettoSumme());
			lieferscheinpositionDto.setBZwsPositionspreisDrucken(
					lieferscheinposition.getBZwsPositionspreisZeigen());
			lieferscheinpositionDto.setCLvposition(lieferscheinposition
					.getCLvposition());
			lieferscheinpositionDto.setNMaterialzuschlag(lieferscheinposition
					.getNMaterialzuschlag());
			lieferscheinpositionDto.setLagerIId(lieferscheinposition
					.getLagerIId());
			lieferscheinpositionDto.setNMaterialzuschlagKurs(lieferscheinposition.getNMaterialzuschlagKurs());
			lieferscheinpositionDto.setTMaterialzuschlagDatum(lieferscheinposition.getTMaterialzuschlagDatum());
			lieferscheinpositionDto.setForecastpositionIId(lieferscheinposition.getForecastpositionIId());
			lieferscheinpositionDto.setWareneingangspositionIIdAndererMandant(lieferscheinposition.getWareneingangspositionIIdAndererMandant());
			lieferscheinpositionDto.setPositionIIdZugehoerig(lieferscheinposition.getPositionIIdZugehoerig());
			
			lieferscheinpositionDto.setNDimBreite(lieferscheinposition.getNDimBreite());
			lieferscheinpositionDto.setNDimHoehe(lieferscheinposition.getNDimHoehe());
			lieferscheinpositionDto.setNDimTiefe(lieferscheinposition.getNDimTiefe());
			lieferscheinpositionDto.setNDimMenge(lieferscheinposition.getNDimMenge());
			
		}
		return lieferscheinpositionDto;
	}

	public static LieferscheinpositionDto[] createDtos(
			Collection<?> lieferscheinpositions) {
		List<LieferscheinpositionDto> list = new ArrayList<LieferscheinpositionDto>();
		if (lieferscheinpositions != null) {
			Iterator<?> iterator = lieferscheinpositions.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Lieferscheinposition) iterator.next()));
			}
		}
		LieferscheinpositionDto[] returnArray = new LieferscheinpositionDto[list
				.size()];
		return (LieferscheinpositionDto[]) list.toArray(returnArray);
	}
}
