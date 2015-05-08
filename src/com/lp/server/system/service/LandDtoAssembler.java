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
package com.lp.server.system.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.system.ejb.Land;

public class LandDtoAssembler {
	public static LandDto createDto(Land land) {
		LandDto landDto = new LandDto();
		if (land != null) {
			landDto.setIID(land.getIId());
			landDto.setCLkz(land.getCLkz());
			landDto.setCName(land.getCName());
			landDto.setCTelvorwahl(land.getCTelvorwahl());
			landDto.setWaehrungCNr(land.getWaehrungCNr());
			landDto.setTEUMitglied(land.getTEumitglied());
			landDto.setNUidnummerpruefenabbetrag(land
					.getNUidnummerpruefenabbetrag());
			landDto.setILaengeuidnummer(land.getILaengeuidnummer());
			landDto.setCUstcode(land.getCUstcode());
			landDto.setBSepa(land.getBSepa());
			landDto.setFGmtversatz(land.getFGmtversatz());
			landDto.setNMuenzRundung(land.getNMuenzRundung()) ;
			landDto.setLandIIdGemeinsamespostland(land.getLandIIdGemeinsamespostland());
			landDto.setBPlznachort(land.getBPlznachort());
		}
		return landDto;
	}

	public static LandDto[] createDtos(Collection<?> lands) {
		List<LandDto> list = new ArrayList<LandDto>();
		if (lands != null) {
			Iterator<?> iterator = lands.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Land) iterator.next()));
			}
		}
		LandDto[] returnArray = new LandDto[list.size()];
		return (LandDto[]) list.toArray(returnArray);
	}
}
