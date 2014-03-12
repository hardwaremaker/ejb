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
package com.lp.server.instandhaltung.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.instandhaltung.ejb.Geraet;

public class GeraetDtoAssembler {
	public static GeraetDto createDto(Geraet bean) {
		GeraetDto dto = new GeraetDto();
		if (bean != null) {
			dto.setIId(bean.getIId());
			dto.setCBez(bean.getCBez());
			dto.setMandantCNr(bean.getMandantCNr());
			dto.setAnlageIId(bean.getAnlageIId());
			dto.setBAufwand(bean.getBAufwand());
			dto.setBVersteckt(bean.getBVersteckt());
			dto.setBMesswertabsolut(bean.getBMesswertabsolut());
			dto.setCGeraetesnr(bean.getCGeraetesnr());
			dto.setCStandort(bean.getCStandort());
			dto.setCVersorgungskreis(bean.getCVersorgungskreis());
			dto.setGeraetetypIId(bean.getGeraetetypIId());
			dto.setHalleIId(bean.getHalleIId());
			dto.setIsmaschineIId(bean.getIsmaschineIId());
			dto.setNGrenzwert(bean.getNGrenzwert());
			dto.setNGrenzwertmax(bean.getNGrenzwertmax());
			dto.setNGrenzwertmin(bean.getNGrenzwertmin());
			dto.setStandortIId(bean.getStandortIId());
			dto.setXBemerkung(bean.getXBemerkung());
			dto.setCFabrikat(bean.getCFabrikat());
			dto.setCLeistung(bean.getCLeistung());
			dto.setIAnzahl(bean.getIAnzahl());
			dto.setGewerkIId(bean.getGewerkIId());
			dto.setHerstellerIId(bean.getHerstellerIId());
		}
		return dto;
	}

	public static GeraetDto[] createDtos(Collection<?> beans) {
		List<GeraetDto> list = new ArrayList<GeraetDto>();
		if (beans != null) {
			Iterator<?> iterator = beans.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Geraet) iterator.next()));
			}
		}
		GeraetDto[] returnArray = new GeraetDto[list.size()];
		return (GeraetDto[]) list.toArray(returnArray);
	}
}
