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
package com.lp.server.bestellung.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.bestellung.ejb.Bestellposition;

public class BestellpositionDtoAssembler {
	public static BestellpositionDto createDto(Bestellposition bestellposition) {
		BestellpositionDto bestellpositionDto = new BestellpositionDto();
		if (bestellposition != null) {
			bestellpositionDto.setIId(bestellposition.getIId());
			bestellpositionDto.setBestellungIId(bestellposition
					.getBestellungIId());
			bestellpositionDto.setISort(bestellposition.getISort());
			bestellpositionDto.setPositionsartCNr(bestellposition
					.getBestellpositionartCNr());
			bestellpositionDto.setBestellpositionstatusCNr(bestellposition
					.getBestellpositionstatusCNr());
			bestellpositionDto.setArtikelIId(bestellposition.getArtikelIId());
			bestellpositionDto.setCBez(bestellposition.getCBez());
			bestellpositionDto.setKundeIId(bestellposition.getKundeIId());
			bestellpositionDto
					.setBArtikelbezeichnunguebersteuert(bestellposition
							.getBArtikelbezuebersteuert());
			bestellpositionDto.setCZusatzbez(bestellposition.getCZbez());
			bestellpositionDto.setXTextinhalt(bestellposition.getCTextinhalt());
			bestellpositionDto.setMediastandardIId(bestellposition
					.getMediastandardIId());
			bestellpositionDto.setNMenge(bestellposition.getNMenge());
			bestellpositionDto.setEinheitCNr(bestellposition.getEinheitCNr());
			bestellpositionDto.setDRabattsatz(bestellposition.getFRabattsatz());
			bestellpositionDto.setMwstsatzIId(bestellposition.getMwstsatzIId());
			bestellpositionDto.setBMwstsatzUebersteuert(bestellposition
					.getBMwstsatzuebersteuert());
			bestellpositionDto.setBNettopreisuebersteuert(bestellposition.getBNettopreisuebersteuert());
			bestellpositionDto.setNNettoeinzelpreis(bestellposition
					.getNNettoeinzelpreis());
			bestellpositionDto.setNRabattbetrag(bestellposition
					.getNRabattbetrag());
			bestellpositionDto.setNNettogesamtpreis(bestellposition
					.getNNettogesamtpreis());
			bestellpositionDto.setNNettogesamtPreisminusRabatte(bestellposition
					.getNNettogesamtpreisminusrabatte());
			bestellpositionDto
					.setIBestellpositionIIdRahmenposition(bestellposition
							.getBestellpositionIIdRahmenposition());
			bestellpositionDto.setNOffeneMenge(bestellposition
					.getNOffenemenge());
			if (bestellposition.getTUebersteuerterliefertermin() != null) {
				bestellpositionDto
						.setTUebersteuerterLiefertermin(bestellposition
								.getTUebersteuerterliefertermin());
			}
			bestellpositionDto.setBDrucken(bestellposition.getBDrucken());
			bestellpositionDto.setTAuftragsbestaetigungstermin(bestellposition
					.getTAuftragsbestaetigungstermin());
			bestellpositionDto.setCABKommentar(bestellposition
					.getCAbkommentar());
			bestellpositionDto.setCABNummer(bestellposition.getCAbnummer());
			bestellpositionDto.setNFixkosten(bestellposition.getNFixkosten());
			bestellpositionDto.setNFixkostengeliefert(bestellposition
					.getNFixkostengeliefert());
			bestellpositionDto.setTManuellvollstaendiggeliefert(bestellposition
					.getTManuellvollstaendiggeliefert());
			bestellpositionDto.setPersonalIIdAbterminAendern(bestellposition.getPersonalIIdAbterminAendern());
			bestellpositionDto.setTAbterminAendern(bestellposition.getTAbterminAendern());
			bestellpositionDto.setTAbursprungstermin(bestellposition.getTAbursprungstermin());
			bestellpositionDto.setCAngebotnummer(bestellposition.getCAngebotnummer());
			bestellpositionDto.setNMaterialzuschlag(bestellposition.getNMaterialzuschlag());
			bestellpositionDto.setTLieferterminbestaetigt(bestellposition.getTLieferterminbestaetigt());
			bestellpositionDto.setPersonalIIdLieferterminbestaetigt(bestellposition.getPersonalIIdLieferterminbestaetigt());
			bestellpositionDto.setLossollmaterialIId(bestellposition.getLossollmaterialIId());
			bestellpositionDto.setPositioniIdArtikelset(bestellposition.getPositionIIdArtikelset());
		}
		return bestellpositionDto;
	}

	public static BestellpositionDto[] createDtos(Collection<?> bestellpositions) {
		List<BestellpositionDto> list = new ArrayList<BestellpositionDto>();
		if (bestellpositions != null) {
			Iterator<?> iterator = bestellpositions.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Bestellposition) iterator.next()));
			}
		}
		BestellpositionDto[] returnArray = new BestellpositionDto[list.size()];
		return (BestellpositionDto[]) list.toArray(returnArray);
	}
}
