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
package com.lp.server.bestellung.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.bestellung.ejb.Wareneingang;

public class WareneingangDtoAssembler {
	public static WareneingangDto createDto(Wareneingang wareneingang) {
		WareneingangDto wareneingangDto = new WareneingangDto();
		if (wareneingang != null) {
			wareneingangDto.setIId(wareneingang.getIId());
			wareneingangDto.setISort(wareneingang.getISort());
			wareneingangDto.setCLieferscheinnr(wareneingang
					.getCLieferscheinnr());
			wareneingangDto.setTLieferscheindatum(wareneingang
					.getTLieferscheindatum());
			wareneingangDto.setNTransportkosten(wareneingang
					.getNTransportkosten());
			wareneingangDto.setNBankspesen(wareneingang
					.getNBankspesen());
			wareneingangDto.setNSonstigespesen(wareneingang
					.getNSonstigespesen());
			wareneingangDto.setNZollkosten(wareneingang
					.getNZollkosten());
			wareneingangDto.setDGemeinkostenfaktor(wareneingang
					.getFGemeinkostenfaktor());
			wareneingangDto.setFRabattsatz(wareneingang
					.getFRabattsatz());
			wareneingangDto.setTWareneingangsdatum(wareneingang
					.getTWareneingangsdatum());
			wareneingangDto.setBestellungIId(wareneingang.getBestellungIId());
			wareneingangDto.setLagerIId(wareneingang.getLagerIId());
			wareneingangDto.setNWechselkurs(wareneingang.getNWechselkurs());
			wareneingangDto.setEingangsrechnungIId(wareneingang
					.getEingangsrechnungIId());
		}
		return wareneingangDto;
	}

	public static WareneingangDto[] createDtos(Collection<?> wareneingangs) {
		List<WareneingangDto> list = new ArrayList<WareneingangDto>();
		if (wareneingangs != null) {
			Iterator<?> iterator = wareneingangs.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Wareneingang) iterator.next()));
			}
		}
		WareneingangDto[] returnArray = new WareneingangDto[list.size()];
		return (WareneingangDto[]) list.toArray(returnArray);
	}
}
