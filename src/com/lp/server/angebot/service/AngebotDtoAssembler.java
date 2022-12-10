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
package com.lp.server.angebot.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.angebot.ejb.Angebot;

public class AngebotDtoAssembler {
	public static AngebotDto createDto(Angebot angebot) {
		AngebotDto angebotDto = new AngebotDto();
		if (angebot != null) {
			angebotDto.setIId(angebot.getIId());
			angebotDto.setCNr(angebot.getCNr());
			angebotDto.setMandantCNr(angebot.getMandantCNr());
			angebotDto.setArtCNr(angebot.getAngebotartCNr());
			angebotDto.setStatusCNr(angebot.getAngebotstatusCNr());
			angebotDto.setBelegartCNr(angebot.getBelegartCNr());
			angebotDto.setTBelegdatum(angebot.getTBelegdatum());
			angebotDto.setTAnfragedatum(angebot.getTAnfragedatum());
			angebotDto.setTAngebotsgueltigkeitbis(angebot
					.getTAngebotsgueltigkeitbis());
			angebotDto.setKundeIIdAngebotsadresse(angebot
					.getKundeIIdAngebotsadresse());
			angebotDto.setAnsprechpartnerIIdKunde(angebot
					.getAnsprechpartnerIIdKunde());
			angebotDto.setPersonalIIdVertreter(angebot
					.getPersonalIIdVertreter());
			angebotDto.setCBez(angebot.getCBez());
			angebotDto.setWaehrungCNr(angebot.getWaehrungCNrAngebotswaehrung());
			angebotDto.setFWechselkursmandantwaehrungzubelegwaehrung(angebot
					.getFWechselkursmandantwaehrungzuangebotswaehrung());
			angebotDto.setILieferzeitinstunden(angebot
					.getILieferzeitinstunden());
			angebotDto.setAngeboteinheitCNr(angebot.getAngeboteinheitCNr());
			angebotDto.setKostenstelleIId(angebot.getKostenstelleIId());
			angebotDto.setAngeboterledigungsgrundCNr(angebot
					.getAngeboterledigungsgrundCNr());
			angebotDto.setTNachfasstermin(angebot.getTNachfasstermin());
			angebotDto.setTRealisierungstermin(angebot
					.getTRealisierungstermin());
			angebotDto.setFAuftragswahrscheinlichkeit(angebot
					.getFAuftragswahrscheinlichkeit());
			angebotDto.setXAblageort(angebot.getXAblageort());
			angebotDto.setFVersteckterAufschlag(angebot
					.getFVersteckteraufschlag());
			angebotDto.setFAllgemeinerRabattsatz(angebot
					.getFAllgemeinerrabattsatz());
			angebotDto.setFProjektierungsrabattsatz(angebot
					.getFProjektierungsrabattsatz());
			angebotDto.setLieferartIId(angebot.getLieferartIId());
			angebotDto.setZahlungszielIId(angebot.getZahlungszielIId());
			angebotDto.setSpediteurIId(angebot.getSpediteurIId());
			angebotDto.setIGarantie(angebot.getIGarantie());
			angebotDto.setNGesamtwertinbelegwaehrung(angebot
					.getNGesamtangebotswertinangebotswaehrung());
			angebotDto.setBelegtextIIdKopftext(angebot
					.getAngebottextIIdKopftext());
			angebotDto.setXKopftextuebersteuert(angebot
					.getXKopftextuebersteuert());
			angebotDto.setBelegtextIIdFusstext(angebot
					.getAngebottextIIdFusstext());
			angebotDto.setXFusstextuebersteuert(angebot
					.getXFusstextuebersteuert());
			angebotDto.setXExternerkommentar(angebot.getXExternerkommentar());
			angebotDto.setXInternerkommentar(angebot.getXInternerkommentar());
			angebotDto.setTGedruckt(angebot.getTGedruckt());
			angebotDto.setPersonalIIdStorniert(angebot
					.getPersonalIIdStorniert());
			angebotDto.setTStorniert(angebot.getTStorniert());
			angebotDto.setPersonalIIdManuellerledigt(angebot
					.getPersonalIIdManuellerledigt());
			angebotDto.setTManuellerledigt(angebot.getTManuellerledigt());
			angebotDto.setPersonalIIdAnlegen(angebot.getPersonalIIdAnlegen());
			angebotDto.setTAnlegen(angebot.getTAnlegen());
			angebotDto.setPersonalIIdAendern(angebot.getPersonalIIdAendern());
			angebotDto.setTAendern(angebot.getTAendern());
			angebotDto.setCKundenanfrage(angebot.getCKundenanfrage());
			angebotDto.setBMitzusammenfassung(angebot.getBMitzusammenfassung());
			angebotDto.setCLieferartort(angebot.getCLieferartort());
			angebotDto.setProjektIId(angebot.getProjektIId());
			angebotDto.setNKorrekturbetrag(angebot.getNKorrekturbetrag());
			angebotDto.setTAenderungsangebot(angebot.getTAenderungsangebot());
			angebotDto.setIVersion(angebot.getIVersion());
			angebotDto.setKundeIIdLieferadresse(angebot.getKundeIIdLieferadresse());
			angebotDto.setKundeIIdRechnungsadresse(angebot.getKundeIIdRechnungsadresse());
			angebotDto.setAnsprechpartnerIIdLieferadresse(angebot.getAnsprechpartnerIIdLieferadresse());
			angebotDto.setAnsprechpartnerIIdRechnungsadresse(angebot.getAnsprechpartnerIIdRechnungsadresse());
			angebotDto.setPersonalIIdVertreter2(angebot.getPersonalIIdVertreter2());
			angebotDto.setAkquisestatusIId(angebot.getAkquisestatusIId());
			angebotDto.setBMindermengenzuschlag(angebot.getBMindermengenzuschlag());
			angebotDto.setCKommission(angebot.getCKommission());
		}
		return angebotDto;
	}

	public static AngebotDto[] createDtos(Collection<?> angebots) {
		List<AngebotDto> list = new ArrayList<AngebotDto>();
		if (angebots != null) {
			Iterator<?> iterator = angebots.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Angebot) iterator.next()));
			}
		}
		AngebotDto[] returnArray = new AngebotDto[list.size()];
		return (AngebotDto[]) list.toArray(returnArray);
	}
}
