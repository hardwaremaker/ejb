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
package com.lp.server.bestellung.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.bestellung.ejb.Bestellvorschlag;

public class BestellvorschlagDtoAssembler {
	public static BestellvorschlagDto createDto(
			Bestellvorschlag bestellvorschlag) {
		BestellvorschlagDto bestellvorschlagDto = new BestellvorschlagDto();
		if (bestellvorschlag != null) {
			bestellvorschlagDto.setIId(bestellvorschlag.getIId());
			bestellvorschlagDto
					.setCMandantCNr(bestellvorschlag.getMandantCNr());
			bestellvorschlagDto.setIArtikelId(bestellvorschlag.getArtikelIId());
			bestellvorschlagDto.setNZubestellendeMenge(bestellvorschlag
					.getNZubestellendemenge());
			bestellvorschlagDto.setTLiefertermin(bestellvorschlag
					.getTLiefertermin());
			bestellvorschlagDto.setCBelegartCNr(bestellvorschlag
					.getBelegartCNr());
			bestellvorschlagDto.setIBelegartId(bestellvorschlag
					.getIBelegartid());
			bestellvorschlagDto.setILieferantId(bestellvorschlag
					.getLieferantIId());
			bestellvorschlagDto.setNNettoeinzelpreis(bestellvorschlag
					.getNNettoeinzelpreis());
			bestellvorschlagDto.setNRabattbetrag(bestellvorschlag
					.getNRabattbetrag());
			bestellvorschlagDto.setNNettogesamtpreis(bestellvorschlag
					.getNNettogesamtpreis());
			bestellvorschlagDto
					.setNNettoGesamtPreisMinusRabatte(bestellvorschlag
							.getNNettogesamtpreisminusrabatte());
			bestellvorschlagDto.setDRabattsatz(bestellvorschlag
					.getFRabattsatz());
			bestellvorschlagDto.setIBelegartpositionid(bestellvorschlag
					.getIBelegartpositionid());
			bestellvorschlagDto.setBNettopreisuebersteuert(bestellvorschlag
					.getBNettopreisuebersteuert());
			bestellvorschlagDto.setProjektIId(bestellvorschlag.getProjektIId());
			bestellvorschlagDto.setXTextinhalt(bestellvorschlag
					.getXTextinhalt());
			bestellvorschlagDto.setBVormerkung(bestellvorschlag
					.getBVormerkung());
			bestellvorschlagDto.setTVormerkung(bestellvorschlag
					.getTVormerkung());
			bestellvorschlagDto.setPersonalIIdVormerkung(bestellvorschlag
					.getPersonalIIdVormerkung());
		}
		return bestellvorschlagDto;
	}

	public static BestellvorschlagDto[] createDtos(
			Collection<?> bestellvorschlags) {
		List<BestellvorschlagDto> list = new ArrayList<BestellvorschlagDto>();
		if (bestellvorschlags != null) {
			Iterator<?> iterator = bestellvorschlags.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Bestellvorschlag) iterator.next()));
			}
		}
		BestellvorschlagDto[] returnArray = new BestellvorschlagDto[list.size()];
		return (BestellvorschlagDto[]) list.toArray(returnArray);
	}
}