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
package com.lp.server.system.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.system.ejb.Versandauftrag;

public class VersandauftragDtoAssembler {
	public static VersandauftragDto createDto(Versandauftrag versandauftrag) {
		VersandauftragDto versandauftragDto = new VersandauftragDto();
		if (versandauftrag != null) {
			versandauftragDto.setIId(versandauftrag.getIId());
			versandauftragDto.setCEmpfaenger(versandauftrag.getCEmpfaenger());
			versandauftragDto.setCCcempfaenger(versandauftrag
					.getCCcempfaenger());
			versandauftragDto.setCBetreff(versandauftrag.getCBetreff());
			versandauftragDto.setCText(versandauftrag.getCText());
			versandauftragDto.setCAbsenderadresse(versandauftrag
					.getCAbsenderadresse());
			versandauftragDto.setCDateiname(versandauftrag.getCDateiname());
			versandauftragDto.setTSendezeitpunktwunsch(versandauftrag
					.getTSendezeitpunktwunsch());
			versandauftragDto.setTSendezeitpunkt(versandauftrag
					.getTSendezeitpunkt());
			versandauftragDto.setPersonalIId(versandauftrag.getPersonalIId());
			versandauftragDto.setTAnlegen(versandauftrag.getTAnlegen());
			versandauftragDto.setPartnerIIdEmpfaenger(versandauftrag
					.getPartnerIIdEmpfaenger());
			versandauftragDto.setPartnerIIdSender(versandauftrag
					.getPartnerIIdSender());
			versandauftragDto.setBelegartCNr(versandauftrag.getBelegartCNr());
			versandauftragDto.setIIdBeleg(versandauftrag.getIBelegIIdd());
			versandauftragDto.setStatusCNr(versandauftrag.getStatusCNr());
			versandauftragDto.setCStatustext(versandauftrag.getCStatustext());
			versandauftragDto.setOInhalt(versandauftrag.getOInhalt());
			versandauftragDto.setBEmpfangsbestaetigung(versandauftrag
					.getBEmpfangsbestaetigung());
		}
		return versandauftragDto;
	}

	public static VersandauftragDto[] createDtos(Collection<?> versandauftrags) {
		List<VersandauftragDto> list = new ArrayList<VersandauftragDto>();
		if (versandauftrags != null) {
			Iterator<?> iterator = versandauftrags.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Versandauftrag) iterator.next()));
			}
		}
		VersandauftragDto[] returnArray = new VersandauftragDto[list.size()];
		return (VersandauftragDto[]) list.toArray(returnArray);
	}
}
