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

import com.lp.server.fertigung.ejb.Lossollmaterial;

public class LossollmaterialDtoAssembler {
	public static LossollmaterialDto createDto(Lossollmaterial lossollmaterial) {
		LossollmaterialDto lossollmaterialDto = new LossollmaterialDto();
		if (lossollmaterial != null) {
			lossollmaterialDto.setIId(lossollmaterial.getIId());
			lossollmaterialDto.setLosIId(lossollmaterial.getLosIId());
			lossollmaterialDto.setArtikelIId(lossollmaterial.getArtikelIId());
			lossollmaterialDto.setNMenge(lossollmaterial.getNMenge());
			lossollmaterialDto.setEinheitCNr(lossollmaterial.getEinheitCNr());
			lossollmaterialDto.setFDimension1(lossollmaterial.getFDimension1());
			lossollmaterialDto.setFDimension2(lossollmaterial.getFDimension2());
			lossollmaterialDto.setFDimension3(lossollmaterial.getFDimension3());
			lossollmaterialDto.setCPosition(lossollmaterial.getCPosition());
			lossollmaterialDto.setCKommentar(lossollmaterial.getCKommentar());
			lossollmaterialDto.setMontageartIId(lossollmaterial
					.getMontageartIId());
			lossollmaterialDto.setILfdnummer(lossollmaterial.getILfdnummer());
			lossollmaterialDto.setISort(lossollmaterial.getISort());
			lossollmaterialDto.setBNachtraeglich(lossollmaterial
					.getBNachtraeglich());
			lossollmaterialDto.setTAendern(lossollmaterial.getTAendern());
			lossollmaterialDto.setPersonalIIdAendern(lossollmaterial
					.getPersonalIIdAendern());
			lossollmaterialDto.setNSollpreis(lossollmaterial.getNSollpreis());
			lossollmaterialDto.setLossollmaterialIIdOriginal(lossollmaterial
					.getLossollmaterialIIdOriginal());
			lossollmaterialDto.setIBeginnterminoffset(lossollmaterial
					.getIBeginnterminoffset());
		}
		return lossollmaterialDto;
	}

	public static LossollmaterialDto[] createDtos(Collection<?> lossollmaterials) {
		List<LossollmaterialDto> list = new ArrayList<LossollmaterialDto>();
		if (lossollmaterials != null) {
			Iterator<?> iterator = lossollmaterials.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Lossollmaterial) iterator.next()));
			}
		}
		LossollmaterialDto[] returnArray = new LossollmaterialDto[list.size()];
		return (LossollmaterialDto[]) list.toArray(returnArray);
	}
}
