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

import com.lp.server.angebotstkl.ejb.Agstkl;

public class AgstklDtoAssembler {
	public static AgstklDto createDto(Agstkl agstkl) {
		AgstklDto agstklDto = new AgstklDto();
		if (agstkl != null) {
			agstklDto.setIId(agstkl.getIId());
			agstklDto.setMandantCNr(agstkl.getMandantCNr());
			agstklDto.setCNr(agstkl.getCNr());
			agstklDto.setBelegartCNr(agstkl.getBelegartCNr());
			agstklDto.setKundeIId(agstkl.getKundeIId());
			agstklDto.setAnsprechpartnerIIdKunde(agstkl.getAnsprechpartnerIIdKunde());
			agstklDto.setCBez(agstkl.getCBez());
			agstklDto.setWaehrungCNr(agstkl.getWaehrungCNr());
			agstklDto.setFWechselkursmandantwaehrungzuagstklwaehrung(
					agstkl.getFWechselkursmandantwaehrungzuagstklwaehrung());
			agstklDto.setPersonalIIdAnlegen(agstkl.getPersonalIIdAnlegen());
			agstklDto.setTAnlegen(agstkl.getTAnlegen());
			agstklDto.setPersonalIIdAendern(agstkl.getPersonalIIdAendern());
			agstklDto.setTAendern(agstkl.getTAendern());
			agstklDto.setTBelegdatum(agstkl.getTBelegdatum());
			agstklDto.setProjektIId(agstkl.getProjektIId());
			agstklDto.setIEkpreisbasis(agstkl.getIEkpreisbasis());
			agstklDto.setBDatengeaendert(agstkl.getBDatengeaendert());
			agstklDto.setStuecklisteIId(agstkl.getStuecklisteIId());
			agstklDto.setBVorlage(agstkl.getBVorlage());
			agstklDto.setOMedia(agstkl.getOMedia());
			agstklDto.setDatenformatCNr(agstkl.getDatenformatCNr());
			agstklDto.setIHoeheDialog(agstkl.getIHoeheDialog());
			agstklDto.setCDateiname(agstkl.getCDateiname());
			agstklDto.setCZeichnungsnummer(agstkl.getCZeichnungsnummer());
			agstklDto.setNInitialkosten(agstkl.getNInitialkosten());
			
		}
		return agstklDto;
	}

	public static AgstklDto[] createDtos(Collection<?> agstkls) {
		List<AgstklDto> list = new ArrayList<AgstklDto>();
		if (agstkls != null) {
			Iterator<?> iterator = agstkls.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Agstkl) iterator.next()));
			}
		}
		AgstklDto[] returnArray = new AgstklDto[list.size()];
		return (AgstklDto[]) list.toArray(returnArray);
	}
}
