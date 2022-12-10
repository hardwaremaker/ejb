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
package com.lp.server.partner.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.partner.ejb.Partnerbank;

public class PartnerbankDtoAssembler {
	public static PartnerbankDto createDto(Partnerbank partnerbank) {
		PartnerbankDto partnerbankDto = new PartnerbankDto();
		if (partnerbank != null) {
			partnerbankDto.setIId(partnerbank.getIId());
			partnerbankDto.setPartnerIId(partnerbank.getPartnerIId());
			partnerbankDto.setBankPartnerIId(partnerbank.getPartnerbankIId());
			partnerbankDto.setCKtonr(partnerbank.getCKtonr());
			partnerbankDto.setCIban(partnerbank.getCIban());
			partnerbankDto.setISort(partnerbank.getISort());
			partnerbankDto.setTSepaerteilt(partnerbank.getTSepaerteilt());
			partnerbankDto.setCSepamandatsnummer(partnerbank
					.getCSepamandatsnummer());
			partnerbankDto.setCEsr(partnerbank.getCEsr());
			partnerbankDto.setWaehrungCNr(partnerbank.getWaehrungCNr());
		}
		return partnerbankDto;
	}

	public static PartnerbankDto[] createDtos(Collection<?> partnerbanks) {
		List<PartnerbankDto> list = new ArrayList<PartnerbankDto>();
		if (partnerbanks != null) {
			Iterator<?> iterator = partnerbanks.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Partnerbank) iterator.next()));
			}
		}
		PartnerbankDto[] returnArray = new PartnerbankDto[list.size()];
		return (PartnerbankDto[]) list.toArray(returnArray);
	}
}
