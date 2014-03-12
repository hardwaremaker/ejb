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

import com.lp.server.system.ejb.Anwender;
import com.lp.util.EJBExceptionLP;

public class AnwenderDtoAssembler {
	public static AnwenderDto createDto(Anwender anwender) {
		AnwenderDto anwenderDto = new AnwenderDto();
		if (anwender != null) {
			anwenderDto.setIId(anwender.getIId());
			anwenderDto.setIBuildnummerClientBis(anwender
					.getIBuildnummerclientbis());
			anwenderDto.setIBuildnummerClienVon(anwender
					.getIBuildnummerclientvon());
			anwenderDto.setIBuildnummerServerBis(anwender
					.getIBuildnummerserverbis());
			anwenderDto.setIBuildnummerServerVon(anwender
					.getIBuildnummerservervon());
			anwenderDto.setMandantCNrHauptmandant(anwender
					.getMandantCNrHauptmandant());
			anwenderDto.setPersonalIIdAendern(anwender.getPersonalIIdAendern());
			anwenderDto.setIBuildnummerDB(anwender.getIBuildnummerdb());
			anwenderDto.setCVersionDB(anwender.getCVersiondb());
			anwenderDto.setTAendern(anwender.getTAendern());
			anwenderDto.setTAblauf(anwender.getTAblauf());
			anwenderDto.setOCode(anwender.getOCode());
			anwenderDto.setOHash(anwender.getOHash());
		}
		return anwenderDto;
/* AD
		if (anwenderDto.validate()) {
			return anwenderDto;
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_UNGUELTIGE_INSTALLATION, null);
		}
*/
	}
/*
	public static AnwenderDto[] createDtos(Collection<?> anwenders) {
		List<AnwenderDto> list = new ArrayList<AnwenderDto>();
		if (anwenders != null) {
			Iterator<?> iterator = anwenders.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Anwender) iterator.next()));
			}
		}
		AnwenderDto[] returnArray = new AnwenderDto[list.size()];
		return (AnwenderDto[]) list.toArray(returnArray);
	}
*/
}
