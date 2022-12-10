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
package com.lp.server.stueckliste.ejbfac;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.stueckliste.ejb.Stuecklisteposition;
import com.lp.server.stueckliste.service.StuecklistepositionDto;

public class StuecklistepositionDtoAssembler {
	public static StuecklistepositionDto createDto(
			Stuecklisteposition stuecklisteposition) {
		StuecklistepositionDto stuecklistepositionDto = new StuecklistepositionDto();
		if (stuecklisteposition != null) {
			stuecklistepositionDto.setIId(stuecklisteposition.getIId());
			stuecklistepositionDto.setStuecklisteIId(stuecklisteposition
					.getStuecklisteIId());
			stuecklistepositionDto.setArtikelIId(stuecklisteposition
					.getArtikelIId());
			stuecklistepositionDto.setNMenge(stuecklisteposition.getNMenge());
			stuecklistepositionDto.setNKalkpreis(stuecklisteposition
					.getNKalkpreis());
			stuecklistepositionDto.setEinheitCNr(stuecklisteposition
					.getEinheitCNr());
			stuecklistepositionDto.setFDimension1(stuecklisteposition
					.getFDimension1());
			stuecklistepositionDto.setFDimension2(stuecklisteposition
					.getFDimension2());
			stuecklistepositionDto.setFDimension3(stuecklisteposition
					.getFDimension3());
			stuecklistepositionDto.setCPosition(stuecklisteposition
					.getCPosition());
			stuecklistepositionDto.setCKommentar(stuecklisteposition
					.getCKommentar());
			stuecklistepositionDto.setMontageartIId(stuecklisteposition
					.getMontageartIId());
			stuecklistepositionDto.setILfdnummer(stuecklisteposition
					.getILfdnummer());
			stuecklistepositionDto.setISort(stuecklisteposition.getISort());
			stuecklistepositionDto.setBMitdrucken(stuecklisteposition
					.getBMitdrucken());
			stuecklistepositionDto.setTAendern(stuecklisteposition
					.getTAendern());
			stuecklistepositionDto.setTAnlegen(stuecklisteposition
					.getTAnlegen());
			stuecklistepositionDto.setPersonalIIdAendern(stuecklisteposition
					.getPersonalIIdAendern());
			stuecklistepositionDto.setPersonalIIdAnlegen(stuecklisteposition
					.getPersonalIIdAnlegen());
			stuecklistepositionDto.setIBeginnterminoffset(stuecklisteposition.getIBeginnterminoffset());
			stuecklistepositionDto.setAnsprechpartnerIIdAendern(stuecklisteposition.getAnsprechpartnerIIdAendern());
			stuecklistepositionDto.setAnsprechpartnerIIdAnlegen(stuecklisteposition.getAnsprechpartnerIIdAnlegen());
			stuecklistepositionDto.setTAendernAnsprechpartner(stuecklisteposition.getTAendernAnsprechpartner());
			stuecklistepositionDto.setTAnlegenAnsprechpartner(stuecklisteposition.getTAnlegenAnsprechpartner()) ;
			stuecklistepositionDto.setBRuestmenge(stuecklisteposition.getBRuestmenge());
			stuecklistepositionDto.setXFormel(stuecklisteposition.getXFormel());
			stuecklistepositionDto.setBInitial(stuecklisteposition.getBInitial());
		}
		
		return stuecklistepositionDto;
	}

	public static StuecklistepositionDto[] createDtos(
			Collection<?> stuecklistepositions) {
		List<StuecklistepositionDto> list = new ArrayList<StuecklistepositionDto>();
		if (stuecklistepositions != null) {
			Iterator<?> iterator = stuecklistepositions.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Stuecklisteposition) iterator.next()));
			}
		}
		StuecklistepositionDto[] returnArray = new StuecklistepositionDto[list
				.size()];
		return (StuecklistepositionDto[]) list.toArray(returnArray);
	}
}
