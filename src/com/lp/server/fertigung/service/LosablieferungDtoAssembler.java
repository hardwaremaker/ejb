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
package com.lp.server.fertigung.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.fertigung.ejb.Losablieferung;

public class LosablieferungDtoAssembler {
	public static LosablieferungDto createDto(Losablieferung losablieferung) {
		LosablieferungDto losablieferungDto = new LosablieferungDto();
		if (losablieferung != null) {
			losablieferungDto.setIId(losablieferung.getIId());
			losablieferungDto.setLosIId(losablieferung.getLosIId());
			losablieferungDto.setNMenge(losablieferung.getNMenge());
			losablieferungDto.setNGestehungspreis(losablieferung
					.getNGestehungspreis());
			losablieferungDto.setNMaterialwert(losablieferung
					.getNMaterialwert());
			losablieferungDto.setNArbeitszeitwert(losablieferung
					.getNArbeitszeitwert());
			losablieferungDto.setPersonalIIdAendern(losablieferung
					.getPersonalIIdAendern());
			losablieferungDto.setTAendern(losablieferung.getTAendern());
			losablieferungDto.setNMaterialwertdetailliert(losablieferung
					.getNMaterialwertdetailliert());
			losablieferungDto.setNArbeitszeitwertdetailliert(losablieferung
					.getNArbeitszeitwertdetailliert());
			losablieferungDto.setBGestehungspreisNeuBerechnen(losablieferung
					.getBGestehungspreisneuberechnen());
			losablieferungDto.setLagerIId(losablieferung.getLagerIId());
		}
		return losablieferungDto;
	}

	public static LosablieferungDto[] createDtos(Collection<?> losablieferungs) {
		List<LosablieferungDto> list = new ArrayList<LosablieferungDto>();
		if (losablieferungs != null) {
			Iterator<?> iterator = losablieferungs.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Losablieferung) iterator.next()));
			}
		}
		LosablieferungDto[] returnArray = new LosablieferungDto[list.size()];
		return (LosablieferungDto[]) list.toArray(returnArray);
	}
}
