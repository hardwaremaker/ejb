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
package com.lp.server.partner.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.partner.ejb.Lieferant;

public class LieferantDtoAssembler {
	public static LieferantDto createDto(Lieferant lieferant) {
		LieferantDto lieferantDto = new LieferantDto();
		if (lieferant != null) {
			lieferantDto.setIId(lieferant.getIId());
			lieferantDto.setPartnerIId(lieferant.getPartnerIId());
			lieferantDto.setMandantCNr(lieferant.getMandantCNr());
			lieferantDto.setPersonalIIdAnlegen(lieferant
					.getPersonalIIdAnlegen());
			lieferantDto.setPersonalIIdAendern(lieferant
					.getPersonalIIdAendern());
			lieferantDto.setTAendern(lieferant.getTAendern());
			lieferantDto.setTAnlegen(lieferant.getTAnlegen());
			lieferantDto.setMwstsatzbezIId(lieferant.getMwstsatzIId());
			lieferantDto.setWaehrungCNr(lieferant.getWaehrungCNr());
			lieferantDto.setLieferartIId(lieferant.getLieferartIId());
			lieferantDto.setZahlungszielIId(lieferant.getZahlungszielIId());
			lieferantDto.setNMindestbestellwert(lieferant
					.getNMindestbestellwert());
			lieferantDto.setNKredit(lieferant.getNKredit());
			lieferantDto.setNTransportkostenprolieferung(lieferant
					.getNTransportkostenprolieferung());
			lieferantDto.setNJahrbonus(lieferant.getNJahrbonus());
			lieferantDto.setNAbumsatz(lieferant.getNAbumsatz());
			lieferantDto.setNRabatt(lieferant.getNRabatt());
			lieferantDto.setNMindermengenzuschlag(lieferant
					.getNMindermengenzuschlag());
			lieferantDto.setKontoIIdKreditorenkonto(lieferant
					.getKontoIIdKreditorenkonto());
			lieferantDto.setKontoIIdWarenkonto(lieferant
					.getKontoIIdWarenkonto());
			lieferantDto.setCKundennr(lieferant.getCKundennr());
			lieferantDto.setCHinweisintern(lieferant.getCHinweisintern());
			lieferantDto.setCHinweisextern(lieferant.getCHinweisextern());
			lieferantDto.setXKommentar(lieferant.getXKommentar());
			lieferantDto.setBBeurteilen(lieferant.getBBeurteilen());
			lieferantDto.setIBeurteilung(lieferant.getIBeurteilung());
			lieferantDto.setPartnerIIdRechnungsadresse(lieferant
					.getPartnerIIdRechnungsadresse());
			lieferantDto.setBMoeglicherLieferant(lieferant
					.getBMoeglicherlieferant());
			lieferantDto.setIIdKostenstelle(lieferant.getKostenstelleIId());
			lieferantDto.setIdSpediteur(lieferant.getSpediteurIId());
			lieferantDto.setTBestellsperream(lieferant.getTBestellsperream());
			lieferantDto.setNKupferzahl(lieferant.getNKupferzahl());

			lieferantDto.setCFreigabe(lieferant.getCFreigabe());
			lieferantDto.setTFreigabe(lieferant.getTFreigabe());
			lieferantDto.setPersonalIIdFreigabe(lieferant
					.getPersonalIIdFreigabe());
			lieferantDto.setTPersonalFreigabe(lieferant.getTPersonalFreigabe());
			lieferantDto.setBReversecharge(lieferant.getBReversecharge());
			lieferantDto.setBIgErwerb(lieferant.getBIgErwerb());
			lieferantDto.setLagerIIdZubuchungslager(lieferant
					.getLagerIIdZubuchungslager());
			lieferantDto.setBZollimportpapier(lieferant.getBZollimportpapier());
		}
		return lieferantDto;
	}

	public static LieferantDto[] createDtos(Collection<?> lieferants) {
		List<LieferantDto> list = new ArrayList<LieferantDto>();
		if (lieferants != null) {
			Iterator<?> iterator = lieferants.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Lieferant) iterator.next()));
			}
		}
		LieferantDto[] returnArray = new LieferantDto[list.size()];
		return (LieferantDto[]) list.toArray(returnArray);
	}
}
