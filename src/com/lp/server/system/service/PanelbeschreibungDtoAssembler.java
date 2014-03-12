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

import com.lp.server.system.ejb.Panelbeschreibung;

public class PanelbeschreibungDtoAssembler {
	public static PanelbeschreibungDto createDto(
			Panelbeschreibung panelbeschreibung) {
		PanelbeschreibungDto panelbeschreibungDto = new PanelbeschreibungDto();
		if (panelbeschreibung != null) {
			panelbeschreibungDto.setIId(panelbeschreibung.getIId());
			panelbeschreibungDto.setPanelCNr(panelbeschreibung.getPanelCNr());
			panelbeschreibungDto.setCName(panelbeschreibung.getCName());
			panelbeschreibungDto.setCTyp(panelbeschreibung.getCTyp());
			panelbeschreibungDto.setCTokeninresourcebundle(panelbeschreibung
					.getCTokeninresourcebundle());
			panelbeschreibungDto.setIGridx(panelbeschreibung.getIGridx());
			panelbeschreibungDto.setIGridy(panelbeschreibung.getIGridy());
			panelbeschreibungDto.setIGridwidth(panelbeschreibung
					.getIGridwidth());
			panelbeschreibungDto.setIGridheigth(panelbeschreibung
					.getIGridheigth());
			panelbeschreibungDto.setCFill(panelbeschreibung.getCFill());
			panelbeschreibungDto.setCAnchor(panelbeschreibung.getCAnchor());
			panelbeschreibungDto.setIInsetsleft(panelbeschreibung
					.getIInsetsleft());
			panelbeschreibungDto.setIInsetsright(panelbeschreibung
					.getIInsetsright());
			panelbeschreibungDto.setIInsetstop(panelbeschreibung
					.getIInsetstop());
			panelbeschreibungDto.setIInsetsbottom(panelbeschreibung
					.getIInsetsbottom());
			panelbeschreibungDto.setIIpadx(panelbeschreibung.getIIpadx());
			panelbeschreibungDto.setIIpady(panelbeschreibung.getIIpady());
			panelbeschreibungDto.setBMandatory(panelbeschreibung
					.getBMandatory());
			panelbeschreibungDto.setFWeightx(panelbeschreibung.getFWeightx());
			panelbeschreibungDto.setFWeighty(panelbeschreibung.getFWeighty());
			panelbeschreibungDto.setMandantCNr(panelbeschreibung
					.getMandantCNr());
			panelbeschreibungDto.setArtgruIId(panelbeschreibung.getArtgruIId());
			panelbeschreibungDto.setCDruckname(panelbeschreibung
					.getCDruckname());
			panelbeschreibungDto.setPartnerklasseIId(panelbeschreibung
					.getPartnerklasseIId());
		}
		return panelbeschreibungDto;
	}

	public static PanelbeschreibungDto[] createDtos(
			Collection<?> panelbeschreibungs) {
		List<PanelbeschreibungDto> list = new ArrayList<PanelbeschreibungDto>();
		if (panelbeschreibungs != null) {
			Iterator<?> iterator = panelbeschreibungs.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Panelbeschreibung) iterator.next()));
			}
		}
		PanelbeschreibungDto[] returnArray = new PanelbeschreibungDto[list
				.size()];
		return (PanelbeschreibungDto[]) list.toArray(returnArray);
	}
}
