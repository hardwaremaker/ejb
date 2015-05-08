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

import com.lp.server.personal.ejb.Zeitmodell;

public class ZeitmodellDtoAssembler {
	public static ZeitmodellDto createDto(Zeitmodell zeitmodell) {
		ZeitmodellDto zeitmodellDto = new ZeitmodellDto();
		if (zeitmodell != null) {
			zeitmodellDto.setIId(zeitmodell.getIId());
			zeitmodellDto.setMandantCNr(zeitmodell.getMandantCNr());
			zeitmodellDto.setCNr(zeitmodell.getCNr());
			zeitmodellDto.setBTeilzeit(zeitmodell.getBTeilzeit());
			zeitmodellDto.setPersonalIIdAendern(zeitmodell
					.getPersonalIIdAendern());
			zeitmodellDto.setTAendern(zeitmodell.getTAendern());
			zeitmodellDto.setBVersteckt(zeitmodell.getBVersteckt());
			zeitmodellDto.setFUrlaubstageprowoche(zeitmodell.getFUrlaubstageprowoche());
			zeitmodellDto.setNSollstundenfix(zeitmodell.getNSollstundenfix());
			zeitmodellDto.setBDynamisch(zeitmodell.getBDynamisch());
			zeitmodellDto.setIMinutenabzug(zeitmodell.getIMinutenabzug());
			zeitmodellDto.setBFeiertagssollAddieren(zeitmodell.getBFeiertagssollAddieren());
			zeitmodellDto.setNMaximalesWochenist(zeitmodell.getNMaximalesWochenist());
			zeitmodellDto.setBFixepauseTrotzkommtgeht(zeitmodell.getBFixepauseTrotzkommtgeht());
		}
		return zeitmodellDto;
	}

	public static ZeitmodellDto[] createDtos(Collection<?> zeitmodells) {
		List<ZeitmodellDto> list = new ArrayList<ZeitmodellDto>();
		if (zeitmodells != null) {
			Iterator<?> iterator = zeitmodells.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Zeitmodell) iterator.next()));
			}
		}
		ZeitmodellDto[] returnArray = new ZeitmodellDto[list.size()];
		return (ZeitmodellDto[]) list.toArray(returnArray);
	}
}
