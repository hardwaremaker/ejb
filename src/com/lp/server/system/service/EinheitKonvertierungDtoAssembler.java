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

import com.lp.server.system.ejb.Einheitkonvertierung;

public class EinheitKonvertierungDtoAssembler {
	public static EinheitKonvertierungDto createDto(
			Einheitkonvertierung einheitKonvertierung) {
		EinheitKonvertierungDto einheitKonvertierungDto = new EinheitKonvertierungDto();
		if (einheitKonvertierung != null) {
			einheitKonvertierungDto.setIId(einheitKonvertierung.getIId());
			einheitKonvertierungDto.setEinheitCNrVon(einheitKonvertierung
					.getEinheitCNrVon());
			einheitKonvertierungDto.setEinheitCNrZu(einheitKonvertierung
					.getEinheitCNrZu());
			einheitKonvertierungDto.setNFaktor(einheitKonvertierung
					.getNFaktor());
			einheitKonvertierungDto.setTAnlegen(einheitKonvertierung
					.getTAnlegen());
			einheitKonvertierungDto.setPersonalIIdAnlegen(einheitKonvertierung
					.getPersonalIIdAnlegen());
			einheitKonvertierungDto.setTAendern(einheitKonvertierung
					.getTAendern());
			einheitKonvertierungDto.setPersonalIIdAendern(einheitKonvertierung
					.getPersonalIIdAendern());

		}
		return einheitKonvertierungDto;
	}

	public static EinheitKonvertierungDto[] createDtos(
			Collection<?> einheitKonvertierungs) {
		List<EinheitKonvertierungDto> list = new ArrayList<EinheitKonvertierungDto>();
		if (einheitKonvertierungs != null) {
			Iterator<?> iterator = einheitKonvertierungs.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Einheitkonvertierung) iterator.next()));
			}
		}
		EinheitKonvertierungDto[] returnArray = new EinheitKonvertierungDto[list
				.size()];
		return (EinheitKonvertierungDto[]) list.toArray(returnArray);
	}
}
