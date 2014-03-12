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
package com.lp.server.fertigung.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.fertigung.ejb.Los;

public class LosDtoAssembler {
	public static LosDto createDto(Los los) {
		LosDto losDto = new LosDto();
		if (los != null) {
			losDto.setIId(los.getIId());
			losDto.setMandantCNr(los.getMandantCNr());
			losDto.setCNr(los.getCNr());
			losDto.setLosIIdElternlos(los.getLosIIdElternlos());
			losDto.setKostenstelleIId(los.getKostenstelleIId());
			losDto.setAuftragpositionIId(los.getAuftragpositionIId());
			losDto.setCKommentar(los.getCKommentar());
			losDto.setCProjekt(los.getCProjekt());
			losDto.setStuecklisteIId(los.getStuecklisteIId());
			losDto.setNLosgroesse(los.getNLosgroesse());
			losDto.setPartnerIIdFertigungsort(los.getPartnerIIdFertigungsort());
			losDto.setPersonalIIdTechniker(los.getPersonalIIdTechniker());
			losDto.setTProduktionsende(new Date(los.getTProduktionsende()
					.getTime()));
			losDto.setTProduktionsbeginn(new Date(los.getTProduktionsbeginn()
					.getTime()));
			losDto.setTAusgabe(los.getTAusgabe());
			losDto.setPersonalIIdAusgabe(los.getPersonalIIdAusgabe());
			losDto.setTErledigt(los.getTErledigt());
			losDto.setPersonalIIdErledigt(los.getPersonalIIdErledigt());
			losDto.setTProduktionsstop(los.getTProduktionsstop());
			losDto.setPersonalIIdProduktionsstop(los
					.getPersonalIIdProduktionsstop());
			losDto.setTLeitstandstop(los.getTLeitstandstop());
			losDto.setPersonalIIdLeitstandstop(los
					.getPersonalIIdLeitstandstop());
			losDto.setLagerIIdZiel(los.getLagerIIdZiel());
			losDto.setStatusCNr(los.getStatusCNr());
			losDto.setTAktualisierungstueckliste(los
					.getTAktualisierungstueckliste());
			losDto.setTAktualisierungarbeitszeit(los
					.getTAktualisierungarbeitszeit());
			losDto.setPersonalIIdAnlegen(los.getPersonalIIdAnlegen());
			losDto.setTAnlegen(los.getTAnlegen());
			losDto.setPersonalIIdAendern(los.getPersonalIIdAendern());
			losDto.setTAendern(los.getTAendern());
			losDto.setPersonalIIdManuellerledigt(los
					.getPersonalIIdManuellerledigt());
			losDto.setTManuellerledigt(los.getTManuellerledigt());
			losDto.setXText(los.getXText());
			losDto.setCZusatznummer(los.getCZusatznummer());
			losDto.setFBewertung(los.getFBewertung());
			losDto.setAuftragIId(los.getAuftragIId());
			losDto.setFertigungsgruppeIId(los.getFertigungsgruppeIId());
			losDto.setWiederholendeloseIId(los.getWiederholendeloseIId());
			losDto.setXProduktionsinformation(los.getXProduktionsinformation());
			losDto.setKundeIId(los.getKundeIId());
			losDto.setNSollmaterial(los.getNSollmaterial());
			losDto.setLosbereichIId(los.getLosbereichIId());
		}
		return losDto;
	}

	public static LosDto[] createDtos(Collection<?> loss) {
		List<LosDto> list = new ArrayList<LosDto>();
		if (loss != null) {
			Iterator<?> iterator = loss.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Los) iterator.next()));
			}
		}
		LosDto[] returnArray = new LosDto[list.size()];
		return (LosDto[]) list.toArray(returnArray);
	}
}
