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

import com.lp.server.system.ejb.Dokumentenlink;

public class DokumentenlinkDtoAssembler {
	public static DokumentenlinkDto createDto(Dokumentenlink dokumentenlink) {
		DokumentenlinkDto dokumentenlinkDto = new DokumentenlinkDto();
		if (dokumentenlink != null) {
			dokumentenlinkDto.setIId(dokumentenlink.getIId());
			dokumentenlinkDto.setBelegartCNr(dokumentenlink.getBelegartCNr());
			dokumentenlinkDto.setCBasispfad(dokumentenlink.getCBasispfad());
			dokumentenlinkDto.setCOrdner(dokumentenlink.getCOrdner());
			dokumentenlinkDto.setCMenuetext(dokumentenlink.getCMenuetext());
			dokumentenlinkDto.setMandantCNr(dokumentenlink.getMandantCNr());
			dokumentenlinkDto.setBPfadabsolut(dokumentenlink.getBPfadabsolut());
			dokumentenlinkDto.setBUrl(dokumentenlink.getBUrl());
		}
		return dokumentenlinkDto;
	}

	public static DokumentenlinkDto[] createDtos(Collection<?> dokumentenlinks) {
		List<DokumentenlinkDto> list = new ArrayList<DokumentenlinkDto>();
		if (dokumentenlinks != null) {
			Iterator<?> iterator = dokumentenlinks.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Dokumentenlink) iterator.next()));
			}
		}
		DokumentenlinkDto[] returnArray = new DokumentenlinkDto[list.size()];
		return (DokumentenlinkDto[]) list.toArray(returnArray);
	}
}
