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
package com.lp.server.angebotstkl.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.angebotstkl.ejb.Agstklarbeitsplan;

public class AgstklarbeitsplanDtoAssembler {
	public static AgstklarbeitsplanDto createDto(Agstklarbeitsplan bean) {
		AgstklarbeitsplanDto dto = new AgstklarbeitsplanDto();
		if (bean != null) {
			dto.setIId(bean.getIId());
			dto.setAgstklIId(bean.getAgstklIId());
			dto.setIArbeitsgang(bean.getIArbeitsgang());
			dto.setArtikelIId(bean.getArtikelIId());
			dto.setLStueckzeit(bean.getLStueckzeit());
			dto.setLRuestzeit(bean.getLRuestzeit());
			dto.setCKommentar(bean.getCKommentar());
			dto.setXLangtext(bean.getXLangtext());
			dto.setMaschineIId(bean.getMaschineIId());
			dto.setBNurmaschinenzeit(bean.getBNurmaschinenzeit());
			dto.setAgartCNr(bean.getAgartCNr());
			dto.setIAufspannung(bean.getIAufspannung());
			dto.setIUnterarbeitsgang(bean.getIUnterarbeitsgang());
			dto.setNStundensatzMann(bean.getNStundensatzMann());
			dto.setNStundensatzMaschine(bean.getNStundensatzMaschine());
			dto.setBInitial(bean.getBInitial());

		}
		return dto;
	}

	public static AgstklarbeitsplanDto[] createDtos(
			Collection<?> stuecklistearbeitsplans) {
		List<AgstklarbeitsplanDto> list = new ArrayList<AgstklarbeitsplanDto>();
		if (stuecklistearbeitsplans != null) {
			Iterator<?> iterator = stuecklistearbeitsplans.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Agstklarbeitsplan) iterator.next()));
			}
		}
		AgstklarbeitsplanDto[] returnArray = new AgstklarbeitsplanDto[list
				.size()];
		return (AgstklarbeitsplanDto[]) list.toArray(returnArray);
	}
}
