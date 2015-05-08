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
package com.lp.server.inserat.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.inserat.ejb.Inserat;

public class InseratDtoAssembler {
	public static InseratDto createDto(Inserat inserat) {
		InseratDto inseratDto = new InseratDto();
		if (inserat != null) {
			inseratDto.setIId(inserat.getIId());
			inseratDto.setCNr(inserat.getCNr());
			inseratDto.setMandantCNr(inserat.getMandantCNr());
			inseratDto.setTBelegdatum(inserat.getTBelegdatum());
			inseratDto.setFKdRabatt(inserat.getFKdRabatt());
			inseratDto.setFKdZusatzrabatt(inserat.getFKdZusatzrabatt());
			inseratDto.setFKdNachlass(inserat.getFKdNachlass());
			inseratDto.setCBez(inserat.getCBez());
			inseratDto.setCRubrik(inserat.getCRubrik());
			inseratDto.setCRubrik2(inserat.getCRubrik2());
			inseratDto.setCStichwort(inserat.getCStichwort());
			inseratDto.setCStichwort2(inserat.getCStichwort2());
			inseratDto.setCMedium(inserat.getCMedium());
			inseratDto.setXAnhang(inserat.getXAnhang());
			inseratDto.setLieferantIId(inserat.getLieferantIId());
			inseratDto.setAnsprechpartnerIIdLieferant(inserat
					.getAnsprechpartnerIIdLieferant());
			inseratDto.setFLFRabatt(inserat.getFLFRabatt());
			inseratDto.setFLfZusatzrabatt(inserat.getFLfZusatzrabatt());
			inseratDto.setFLfNachlass(inserat.getFLfNachlass());
			inseratDto.setXAnhangLf(inserat.getXAnhangLf());
			inseratDto.setTTermin(inserat.getTTermin());
			inseratDto.setTTerminBis(inserat.getTTerminBis());
			inseratDto.setArtikelIIdInseratart(inserat
					.getArtikelIIdInseratart());
			inseratDto.setStatusCNr(inserat.getStatusCNr());
			inseratDto.setPersonalIIdVertreter(inserat
					.getPersonalIIdVertreter());

			inseratDto.setNMenge(inserat.getNMenge());
			inseratDto.setNNettoeinzelpreisEk(inserat.getNNettoeinzelpreisEk());
			inseratDto.setNNettoeinzelpreisVk(inserat.getNNettoeinzelpreisVk());

			inseratDto.setTErschienen(inserat.getTErschienen());
			inseratDto.setPersonalIIdErschienen(inserat
					.getPersonalIIdErschienen());

			inseratDto.setTVerrechnen(inserat.getTVerrechnen());
			inseratDto.setPersonalIIdVerrechnen(inserat
					.getPersonalIIdVerrechnen());
			inseratDto.setCGestoppt(inserat.getCGestoppt());

			inseratDto.setPersonalIIdAnlegen(inserat.getPersonalIIdAnlegen());
			inseratDto.setTAnlegen(inserat.getTAnlegen());
			inseratDto.setPersonalIIdAendern(inserat.getPersonalIIdAendern());
			inseratDto.setTAendern(inserat.getTAendern());
			inseratDto.setBestellpositionIId(inserat.getBestellpositionIId());

			inseratDto.setBDruckBestellungKd(inserat.getBDruckBestellungKd());
			inseratDto.setBDruckBestellungLf(inserat.getBDruckBestellungLf());
			inseratDto.setBDruckRechnungKd(inserat.getBDruckRechnungKd());
			inseratDto.setTManuellerledigt(inserat.getTManuellerledigt());
			inseratDto.setPersonalIIdManuellerledigt(inserat
					.getPersonalIIdManuellerledigt());
			inseratDto.setTGestoppt(inserat.getTGestoppt());
			inseratDto.setPersonalIIdGestoppt(inserat.getPersonalIIdGestoppt());
			inseratDto.setBWertaufteilen(inserat.getBWertaufteilen());
			inseratDto.setPersonalIIdManuellverrechnen(inserat
					.getPersonalIIdManuellverrechnen());
			inseratDto.setTManuellverrechnen(inserat.getTManuellverrechnen());
		}
		return inseratDto;
	}

	public static InseratDto[] createDtos(Collection<?> inserats) {
		List<InseratDto> list = new ArrayList<InseratDto>();
		if (inserats != null) {
			Iterator<?> iterator = inserats.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Inserat) iterator.next()));
			}
		}
		InseratDto[] returnArray = new InseratDto[list.size()];
		return (InseratDto[]) list.toArray(returnArray);
	}
}
