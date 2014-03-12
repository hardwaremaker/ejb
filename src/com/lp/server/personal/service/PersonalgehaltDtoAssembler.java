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
package com.lp.server.personal.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.personal.ejb.Personalgehalt;

public class PersonalgehaltDtoAssembler {
	public static PersonalgehaltDto createDto(Personalgehalt personalgehalt) {
		PersonalgehaltDto personalgehaltDto = new PersonalgehaltDto();
		if (personalgehalt != null) {
			personalgehaltDto.setIId(personalgehalt.getIId());
			personalgehaltDto.setPersonalIId(personalgehalt.getPersonalIId());
			personalgehaltDto.setIJahr(personalgehalt.getIJahr());
			personalgehaltDto.setIMonat(personalgehalt.getIMonat());
			personalgehaltDto.setNGehalt(personalgehalt.getNGehalt());
			personalgehaltDto.setFUestpauschale(personalgehalt
					.getFUestpauschale());
			personalgehaltDto.setNStundensatz(personalgehalt.getNStundensatz());
			personalgehaltDto.setFVerfuegbarkeit(personalgehalt
					.getFVerfuegbarkeit());
			personalgehaltDto.setFLeistungswert(personalgehalt.getFLeistungswert());
			
			personalgehaltDto.setNKmgeld1(personalgehalt.getNKmgeld1());
			personalgehaltDto.setNUestdpuffer(personalgehalt.getNUestdpuffer());
			personalgehaltDto.setFBiskilometer(personalgehalt
					.getFBiskilometer());
			personalgehaltDto.setNKmgeld2(personalgehalt.getNKmgeld2());
			personalgehaltDto.setBKksgebbefreit(personalgehalt
					.getBKksgebbefreit());
			personalgehaltDto.setBUestdauszahlen(personalgehalt
					.getBUestdauszahlen());
			personalgehaltDto.setCGrundkksgebbefreit(personalgehalt
					.getCGrundkksgebbefreit());
			personalgehaltDto.setBAlleinverdiener(personalgehalt
					.getBAlleinverdiener());
			personalgehaltDto.setBAlleinerzieher(personalgehalt
					.getBAlleinerzieher());
			personalgehaltDto.setPersonalIIdAendern(personalgehalt
					.getPersonalIIdAendern());
			personalgehaltDto.setTAendern(personalgehalt.getTAendern());
			
			
			personalgehaltDto.setBStundensatzFixiert(personalgehalt.getBStundensatzFixiert());
			personalgehaltDto.setNGehaltBruttobrutto(personalgehalt.getNGehaltBruttobrutto());
			personalgehaltDto.setNGehaltNetto(personalgehalt.getNGehaltNetto());
			personalgehaltDto.setNPraemieBruttobrutto(personalgehalt.getNPraemieBruttobrutto());
			personalgehaltDto.setNLohnmittelstundensatz(personalgehalt.getNLohnmittelstundensatz());
			personalgehaltDto.setFFaktorLohnmittelstundensatz(personalgehalt.getFFaktorLohnmittelstundensatz());
			personalgehaltDto.setFKalkIstJahresstunden(personalgehalt.getFKalkIstJahresstunden());
			personalgehaltDto.setNAufschlagLohnmittelstundensatz(personalgehalt.getNAufschlagLohnmittelstundensatz());
		}
		return personalgehaltDto;
	}

	public static PersonalgehaltDto[] createDtos(Collection<?> personalgehalts) {
		List<PersonalgehaltDto> list = new ArrayList<PersonalgehaltDto>();
		if (personalgehalts != null) {
			Iterator<?> iterator = personalgehalts.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Personalgehalt) iterator.next()));
			}
		}
		PersonalgehaltDto[] returnArray = new PersonalgehaltDto[list.size()];
		return (PersonalgehaltDto[]) list.toArray(returnArray);
	}
}
