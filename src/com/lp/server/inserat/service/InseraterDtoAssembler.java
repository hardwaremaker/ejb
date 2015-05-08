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
package com.lp.server.inserat.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.inserat.ejb.Inserater;

public class InseraterDtoAssembler {
	public static InseraterDto createDto(Inserater inserater) {
		InseraterDto inseraterDto = new InseraterDto();
		if (inserater != null) {
			inseraterDto.setIId(inserater.getIId());
			inseraterDto.setEingangsrechnungIId(inserater
					.getEingangsrechnungIId());
			inseraterDto.setInseratIId(inserater.getInseratIId());
			inseraterDto.setNBetrag(inserater.getNBetrag());
			inseraterDto.setCText(inserater.getCText());
			inseraterDto.setPersonalIIdAnlegen(inserater
					.getPersonalIIdAnlegen());
			inseraterDto.setTAnlegen(inserater.getTAnlegen());
			inseraterDto.setPersonalIIdAendern(inserater
					.getPersonalIIdAendern());
			inseraterDto.setTAendern(inserater.getTAendern());

		}
		return inseraterDto;
	}

	public static InseraterDto[] createDtos(Collection<?> inseraters) {
		List<InseraterDto> list = new ArrayList<InseraterDto>();
		if (inseraters != null) {
			Iterator<?> iterator = inseraters.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Inserater) iterator.next()));
			}
		}
		InseraterDto[] returnArray = new InseraterDto[list.size()];
		return (InseraterDto[]) list.toArray(returnArray);
	}
}
