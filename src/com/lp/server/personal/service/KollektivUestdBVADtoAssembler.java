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
package com.lp.server.personal.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.personal.ejb.KollektivUestdBVA;

public class KollektivUestdBVADtoAssembler {
	public static KollektivUestdBVADto createDto(
			KollektivUestdBVA kollektivuestd50) {
		KollektivUestdBVADto kollektivuestd50Dto = new KollektivUestdBVADto();
		if (kollektivuestd50 != null) {
			kollektivuestd50Dto.setIId(kollektivuestd50.getIId());
			kollektivuestd50Dto.setKollektivIId(kollektivuestd50
					.getKollektivIId());
			kollektivuestd50Dto.setTagesartIId(kollektivuestd50
					.getTagesartIId());
			kollektivuestd50Dto.setU100Beginn(kollektivuestd50.getU100Beginn());
			kollektivuestd50Dto.setU100Ende(kollektivuestd50.getU100Ende());
			kollektivuestd50Dto.setU50Beginn(kollektivuestd50.getU50Beginn());
			kollektivuestd50Dto.setU50Ende(kollektivuestd50.getU50Ende());
			kollektivuestd50Dto.setUGleitzeitBis(kollektivuestd50
					.getUGleitzeitBis());

		}
		return kollektivuestd50Dto;
	}

	public static KollektivUestdBVADto[] createDtos(
			Collection<?> kollektivuestd50s) {
		List<KollektivUestdBVADto> list = new ArrayList<KollektivUestdBVADto>();
		if (kollektivuestd50s != null) {
			Iterator<?> iterator = kollektivuestd50s.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((KollektivUestdBVA) iterator.next()));
			}
		}
		KollektivUestdBVADto[] returnArray = new KollektivUestdBVADto[list
				.size()];
		return (KollektivUestdBVADto[]) list.toArray(returnArray);
	}
}
