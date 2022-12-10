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

import com.lp.server.personal.ejb.Lohnart;

public class LohnartDtoAssembler {
	public static LohnartDto createDto(Lohnart lohnart) {
		LohnartDto lohnartDto = new LohnartDto();
		if (lohnart != null) {
			lohnartDto.setIId(lohnart.getIId());
			lohnartDto.setCBez(lohnart.getCBez());
			lohnartDto.setCFormel(lohnart.getCFormel());
			lohnartDto.setCKommentar(lohnart.getcKommentar());
			lohnartDto.setCTyp(lohnart.getCTyp());
			lohnartDto.setILohnart(lohnart.getILohnart());
			lohnartDto.setPersonalartCNr(lohnart.getPersonalartCNr());
			lohnartDto.setTaetigkeitIIdNl(lohnart.getTaetigkeitIIdNl());
			lohnartDto.setIAusfallWochen(lohnart.getIAusfallWochen());
			lohnartDto.setFMindestuestd(lohnart.getFMindestuestd());
		}
		return lohnartDto;
	}

	public static LohnartDto[] createDtos(Collection<?> berufs) {
		List<LohnartDto> list = new ArrayList<LohnartDto>();
		if (berufs != null) {
			Iterator<?> iterator = berufs.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Lohnart) iterator.next()));
			}
		}
		LohnartDto[] returnArray = new LohnartDto[list.size()];
		return (LohnartDto[]) list.toArray(returnArray);
	}
}
