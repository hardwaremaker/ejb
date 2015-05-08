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
package com.lp.server.anfrage.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.anfrage.ejb.Anfrage;

public class AnfrageDtoAssembler {
	public static AnfrageDto createDto(Anfrage anfrage) {
		AnfrageDto anfrageDto = new AnfrageDto();
		if (anfrage != null) {
			anfrageDto.setIId(anfrage.getIId());
			anfrageDto.setCNr(anfrage.getCNr());
			anfrageDto.setMandantCNr(anfrage.getMandantCNr());
			anfrageDto.setArtCNr(anfrage.getAnfrageartCNr());
			anfrageDto.setStatusCNr(anfrage.getAnfragestatusCNr());
			anfrageDto.setBelegartCNr(anfrage.getBelegartCNr());
			anfrageDto.setTBelegdatum(anfrage.getTBelegdatum());
			anfrageDto.setLieferantIIdAnfrageadresse(anfrage
					.getLieferantIIdAnfrageadresse());
			anfrageDto.setAnsprechpartnerIIdLieferant(anfrage
					.getAnsprechpartnerIIdLieferant());
			anfrageDto.setLiefergruppeIId(anfrage.getLfliefergruppeIId());
			anfrageDto.setCBez(anfrage.getCBez());
			anfrageDto.setCAngebotnummer(anfrage.getCAngebotnummer());
			anfrageDto.setWaehrungCNr(anfrage.getWaehrungCNrAnfragewaehrung());
			anfrageDto.setFWechselkursmandantwaehrungzubelegwaehrung(anfrage
					.getFWechselkursmandantwaehrungzuanfragewaehrung());
			anfrageDto.setTAnliefertermin(anfrage.getTAnliefertermin());
			anfrageDto.setTAngebotdatum(anfrage.getTAngebotdatum());
			anfrageDto.setTAngebotgueltigbis(anfrage.getTAngebotgueltigbis());
			anfrageDto.setKostenstelleIId(anfrage.getKostenstelleIId());
			anfrageDto.setFAllgemeinerRabattsatz(anfrage
					.getFAllgemeinerrabattsatz());
			anfrageDto.setLieferartIId(anfrage.getLieferartIId());
			anfrageDto.setZahlungszielIId(anfrage.getZahlungszielIId());
			anfrageDto.setSpediteurIId(anfrage.getSpediteurIId());
			anfrageDto.setNGesamtwertinbelegwaehrung(anfrage
					.getNGesamtanfragewertinanfragewaehrung());
			anfrageDto.setNTransportkosteninanfragewaehrung(anfrage
					.getNTransportkosteninanfragewaehrung());
			anfrageDto.setBelegtextIIdKopftext(anfrage
					.getAnfragetextIIdKopftext());
			anfrageDto.setXKopftextuebersteuert(anfrage
					.getXKopftextuebersteuert());
			anfrageDto.setBelegtextIIdFusstext(anfrage
					.getAnfragetextIIdFusstext());
			anfrageDto.setXFusstextuebersteuert(anfrage
					.getXFusstextuebersteuert());
			anfrageDto.setTGedruckt(anfrage.getTGedruckt());
			anfrageDto.setPersonalIIdStorniert(anfrage
					.getPersonalIIdStorniert());
			anfrageDto.setTStorniert(anfrage.getTStorniert());
			anfrageDto.setPersonalIIdAnlegen(anfrage.getPersonalIIdAnlegen());
			anfrageDto.setTAnlegen(anfrage.getTAnlegen());
			anfrageDto.setPersonalIIdAendern(anfrage.getPersonalIIdAendern());
			anfrageDto.setTAendern(anfrage.getTAendern());
			anfrageDto.setPersonalIIdManuellerledigt(anfrage
					.getPersonalIIdManuellerledigt());
			anfrageDto.setTManuellerledigt(anfrage.getTManuellerledigt());
			anfrageDto.setAnfrageIIdLiefergruppenanfrage(anfrage
					.getAnfrageIIdLiefergruppenanfrage());
			anfrageDto.setCLieferartort(anfrage.getCLieferartort());
			anfrageDto.setProjektIId(anfrage.getProjektIId());
			anfrageDto.setAnfrageerledigungsgrundIId(anfrage
					.getAnfrageerledigungsgrundIId());
		}
		return anfrageDto;
	}

	public static AnfrageDto[] createDtos(Collection<?> anfrages) {
		List<AnfrageDto> list = new ArrayList<AnfrageDto>();
		if (anfrages != null) {
			Iterator<?> iterator = anfrages.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Anfrage) iterator.next()));
			}
		}
		AnfrageDto[] returnArray = new AnfrageDto[list.size()];
		return (AnfrageDto[]) list.toArray(returnArray);
	}
}
