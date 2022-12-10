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
package com.lp.server.forecast.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.forecast.ejb.Fclieferadresse;

public class FclieferadresseDtoAssembler {
	public static FclieferadresseDto createDto(Fclieferadresse bean) {
		FclieferadresseDto dto = new FclieferadresseDto();
		if (bean != null) {
			buildDto(bean, dto);
		}
		return dto;
	}

	public static FclieferadresseNokaDto createNokaDto(Fclieferadresse bean) {
		FclieferadresseNokaDto dto = new FclieferadresseNokaDto();
		if (bean != null) {
			buildDto(bean, dto);
		}
		return dto;
	}

	private static FclieferadresseDto buildDto(Fclieferadresse bean,
			FclieferadresseDto dto) {
		dto.setIId(bean.getiId());
		dto.setForecastIId(bean.getForecastIId());
		dto.setKundeIIdLieferadresse(bean.getKundeIIdLieferadresse());
		dto.setCPfadImport(bean.getCPfadImport());
		dto.setImportdefCNrPfad(bean.getImportdefCNrPfad());
		dto.setBZusammenziehen(bean.getBZusammenziehen());
		dto.setBKommissionieren(bean.getBKommissionieren());
		dto.setITypRundungLinienabruf(bean.getITypRundungLinienabruf());
		dto.setITypRundungPosition(bean.getITypRundungPosition());
		dto.setKommdruckerIId(bean.getKommdruckerIId());
		dto.setBLiefermengenberuecksichtigen(bean.getBLiefermengenberuecksichtigen());

		return dto;
	}

	public static FclieferadresseDto[] createDtos(Collection<?> auftragarts) {
		List<FclieferadresseDto> list = new ArrayList<FclieferadresseDto>();
		if (auftragarts != null) {
			Iterator<?> iterator = auftragarts.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Fclieferadresse) iterator.next()));
			}
		}
		FclieferadresseDto[] returnArray = new FclieferadresseDto[list.size()];
		return (FclieferadresseDto[]) list.toArray(returnArray);
	}

}
