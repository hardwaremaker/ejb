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
package com.lp.server.auftrag.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.auftrag.ejb.Auftragposition;

public class AuftragpositionDtoAssembler {
	public static AuftragpositionDto createDto(Auftragposition auftragposition) {
		AuftragpositionDto auftragpositionDto = new AuftragpositionDto();
		if (auftragposition != null) {
			auftragpositionDto.setIId(auftragposition.getIId());
			auftragpositionDto.setBelegIId(auftragposition.getAuftragIId());
			auftragpositionDto.setISort(auftragposition.getISort());
			auftragpositionDto.setPositionsartCNr(auftragposition
					.getAuftragpositionartCNr());
			auftragpositionDto.setAuftragpositionstatusCNr(auftragposition
					.getAuftragpositionstatusCNr());
			auftragpositionDto.setArtikelIId(auftragposition.getArtikelIId());
			auftragpositionDto.setCBez(auftragposition.getCBezeichnung());
			auftragpositionDto.setCZusatzbez(auftragposition
					.getCZusatzbezeichnung());
			auftragpositionDto
					.setBArtikelbezeichnunguebersteuert(auftragposition
							.getBArtikelbezeichnungUebersteuert());
			auftragpositionDto.setXTextinhalt(auftragposition.getXTextinhalt());
			auftragpositionDto.setMediastandardIId(auftragposition
					.getMediastandardIId());
			auftragpositionDto.setNOffeneMenge(auftragposition
					.getNOffeneMenge());
			auftragpositionDto.setNMenge(auftragposition.getNMenge());
			auftragpositionDto.setNOffeneRahmenMenge(auftragposition
					.getNOffenerahmenmenge());
			auftragpositionDto.setEinheitCNr(auftragposition.getEinheitCNr());
			auftragpositionDto.setFRabattsatz(auftragposition.getFRabattsatz());
			auftragpositionDto.setBRabattsatzuebersteuert(auftragposition
					.getBRabattsatzuebersteuert());
			auftragpositionDto.setFZusatzrabattsatz(auftragposition
					.getFZusatzrabattsatz());
			auftragpositionDto.setMwstsatzIId(auftragposition.getMwstsatzIId());
			auftragpositionDto.setBMwstsatzuebersteuert(auftragposition
					.getBMwstsatzuebersteuert());
			auftragpositionDto.setBNettopreisuebersteuert(auftragposition
					.getBNettopreisuebersteuert());
			auftragpositionDto.setNEinzelpreis(auftragposition
					.getNNettoeinzelpreis());
			auftragpositionDto
					.setNEinzelpreisplusversteckteraufschlag(auftragposition
							.getNNettoeinzelpreisplusversteckteraufschlag());
			auftragpositionDto.setNRabattbetrag(auftragposition
					.getNRabattbetrag());
			auftragpositionDto.setNNettoeinzelpreis(auftragposition
					.getNNettogesamtpreis());
			auftragpositionDto
					.setNNettoeinzelpreisplusversteckteraufschlag(auftragposition
							.getNNettogesamtpreisplusversteckteraufschlag());
			auftragpositionDto
					.setNNettoeinzelpreisplusversteckteraufschlagminusrabatte(auftragposition
							.getNNettogesamtpreisplusversteckteraufschlagminusrabatte());
			auftragpositionDto
					.setNMwstbetrag(auftragposition.getNMwstbetrag());
			auftragpositionDto.setNBruttoeinzelpreis(auftragposition
					.getNBruttogesamtpreis());
			auftragpositionDto.setTUebersteuerbarerLiefertermin(auftragposition
					.getTUebersteuerterliefertermin());
			auftragpositionDto.setBDrucken(auftragposition.getBDrucken());
			auftragpositionDto
					.setAuftragpositionIIdRahmenposition(auftragposition
							.getAuftragpositionIIdRahmenposition());
			auftragpositionDto.setCSeriennrchargennr(auftragposition
					.getCSeriennrchargennr());
			auftragpositionDto.setPositioniId(auftragposition.getPositionIId());
			auftragpositionDto.setTypCNr(auftragposition.getTypCNr());
			auftragpositionDto.setBdEinkaufpreis(auftragposition.getNEinkaufpreis());
			auftragpositionDto.setPositioniIdArtikelset(auftragposition.getPositionIIdArtikelset());
			auftragpositionDto.setVerleihIId(auftragposition.getVerleihIId());
			auftragpositionDto.setKostentraegerIId(auftragposition.getKostentraegerIId());
			
			auftragpositionDto.setZwsVonPosition(auftragposition.getZwsVonPosition()) ;
			auftragpositionDto.setZwsBisPosition(auftragposition.getZwsBisPosition()) ;
			auftragpositionDto.setZwsNettoSumme(auftragposition.getZwsNettoSumme()) ;
			auftragpositionDto.setBZwsPositionspreisDrucken(auftragposition.getBZwsPositionspreisZeigen()); 
			
			auftragpositionDto.setCLvposition(auftragposition
					.getCLvposition());
			auftragpositionDto.setNMaterialzuschlag(auftragposition.getNMaterialzuschlag());
			auftragpositionDto.setLieferantIId(auftragposition.getLieferantIId());
			auftragpositionDto.setNMaterialzuschlagKurs(auftragposition.getNMaterialzuschlagKurs());
			auftragpositionDto.setTMaterialzuschlagDatum(auftragposition.getTMaterialzuschlagDatum());
		}
		return auftragpositionDto;
	}

	public static AuftragpositionDto[] createDtos(Collection<?> auftragpositions) {
		Collection<AuftragpositionDto> list = createDtosAsList((Collection<Auftragposition>)auftragpositions);
		AuftragpositionDto[] returnArray = new AuftragpositionDto[list.size()];
		return list.toArray(returnArray);
	}
	
	public static Collection<AuftragpositionDto> createDtosAsList(Collection<Auftragposition> auftragpositions) {
		List<AuftragpositionDto> list = new ArrayList<AuftragpositionDto>();
		if (auftragpositions != null) {
			for (Auftragposition auftragposition : auftragpositions) {
				list.add(createDto(auftragposition));				
			}
		}
		return list ;
	}	
}
