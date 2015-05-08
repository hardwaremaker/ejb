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
package com.lp.server.kueche.service;



import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.kueche.ejb.Kdc100log;

public class Kdc100logDtoAssembler {
	public static Kdc100logDto createDto(Kdc100log kdc100log) {
		Kdc100logDto kdc100logDto = new Kdc100logDto();
		if (kdc100log != null) {
			kdc100logDto.setIId(kdc100log.getIId());
			kdc100logDto.setCBarcode(kdc100log.getCBarcode());
			kdc100logDto.setCKommentar(kdc100log.getCKommentar());
			kdc100logDto.setCSeriennummer(kdc100log.getCSeriennummer());
			kdc100logDto.setCBarcode(kdc100log.getCBarcode());
			kdc100logDto.setTStiftzeit(kdc100log.getTStiftzeit());
			kdc100logDto.setTBuchungszeit(kdc100log.getTBuchungszeit());
			kdc100logDto.setCArt(kdc100log.getCArt());
		}
		return kdc100logDto;
	}

	public static Kdc100logDto[] createDtos(Collection<?> kdc100logs) {
		List<Kdc100logDto> list = new ArrayList<Kdc100logDto>();
		if (kdc100logs != null) {
			Iterator<?> iterator = kdc100logs.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Kdc100log) iterator.next()));
			}
		}
		Kdc100logDto[] returnArray = new Kdc100logDto[list.size()];
		return (Kdc100logDto[]) list.toArray(returnArray);
	}
}
