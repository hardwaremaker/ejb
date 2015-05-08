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
package com.lp.server.artikel.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.artikel.ejb.Artikellieferant;

public class ArtikellieferantDtoAssembler {
	public static ArtikellieferantDto createDto(
			Artikellieferant artikellieferant) {
		ArtikellieferantDto artikellieferantDto = new ArtikellieferantDto();
		if (artikellieferant != null) {
			artikellieferantDto.setArtikelIId(artikellieferant.getArtikelIId());
			artikellieferantDto.setLieferantIId(artikellieferant
					.getLieferantIId());
			artikellieferantDto.setCBezbeilieferant(artikellieferant
					.getCBezbeilieferant());
			artikellieferantDto.setCArtikelnrlieferant(artikellieferant
					.getCArtikelnrlieferant());
			artikellieferantDto.setBHerstellerbez(artikellieferant
					.getBHerstellerbez());
			artikellieferantDto.setBRabattbehalten(artikellieferant
					.getBRabattbehalten());
			artikellieferantDto.setBWebshop(artikellieferant
					.getBWebshop());
			artikellieferantDto.setNEinzelpreis(artikellieferant
					.getNEinzelpreis());
			artikellieferantDto.setFRabatt(artikellieferant.getFRabatt());
			artikellieferantDto.setFMindestbestelmenge(artikellieferant
					.getFMindestbestellmenge());
			artikellieferantDto.setNNettopreis(artikellieferant
					.getNNettopreis());
			artikellieferantDto.setFStandardmenge(artikellieferant
					.getFStandardmenge());
			artikellieferantDto.setNVerpackungseinheit(artikellieferant
					.getNVerpackungseinheit());
			artikellieferantDto.setNFixkosten(artikellieferant.getNFixkosten());
			artikellieferantDto.setCRabattgruppe(artikellieferant
					.getCRabattgruppe());
			artikellieferantDto.setTPreisgueltigab(artikellieferant
					.getTPreisgueltigab());
			artikellieferantDto.setTPreisgueltigbis(artikellieferant
					.getTPreisgueltigbis());
			artikellieferantDto.setISort(artikellieferant.getISort());
			artikellieferantDto.setIWiederbeschaffungszeit(artikellieferant
					.getIWiederbeschaffungszeit());
			artikellieferantDto.setIId(artikellieferant.getIId());
			artikellieferantDto.setPersonalIIdAendern(artikellieferant.getPersonalIIdAendern());
			artikellieferantDto.setTAendern(artikellieferant.getTAendern());
			artikellieferantDto.setCAngebotnummer(artikellieferant
					.getCAngebotnummer());
			artikellieferantDto.setZertifikatartIId(artikellieferant
					.getZertifikatartIId());
			artikellieferantDto.setCWeblink(artikellieferant.getCWeblink());
			artikellieferantDto.setEinheitCNrVpe(artikellieferant.getEinheitCNrVpe());
		}
		return artikellieferantDto;
	}

	public static ArtikellieferantDto[] createDtos(Collection<?> artikellieferants) {
		List<ArtikellieferantDto> list = new ArrayList<ArtikellieferantDto>();
		if (artikellieferants != null) {
			Iterator<?> iterator = artikellieferants.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Artikellieferant) iterator.next()));
			}
		}
		ArtikellieferantDto[] returnArray = new ArtikellieferantDto[list.size()];
		return (ArtikellieferantDto[]) list.toArray(returnArray);
	}
}
