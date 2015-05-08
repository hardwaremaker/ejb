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
package com.lp.server.auftrag.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.auftrag.ejb.Auftrag;

public class AuftragDtoAssembler {
	public static AuftragDto createDto(Auftrag auftrag) {
		AuftragDto auftragDto = new AuftragDto();
		if (auftrag != null) {
			auftragDto.setIId(auftrag.getIId());
			auftragDto.setAuftragIIdRahmenauftrag(auftrag
					.getAuftragIIdRahmenauftrag());
			auftragDto.setAngebotIId(auftrag.getAngebotIId());
			auftragDto.setCNr(auftrag.getCNr());
			auftragDto.setMandantCNr(auftrag.getMandantCNr());
			auftragDto.setAuftragartCNr(auftrag.getAuftragartCNr());
			auftragDto.setBelegartCNr(auftrag.getBelegartCNr());
			auftragDto.setKundeIIdAuftragsadresse(auftrag
					.getKundeIIdAuftragsadresse());
			auftragDto.setAnsprechpartnerIId(auftrag
					.getAnsprechpartnerIIdKunde());
			auftragDto.setPersonalIIdVertreter(auftrag
					.getPersonalIIdVertreter());
			auftragDto.setKundeIIdLieferadresse(auftrag
					.getKundeIIdLieferadresse());
			auftragDto.setKundeIIdRechnungsadresse(auftrag
					.getKundeIIdRechnungsadresse());
			auftragDto.setCBezProjektbezeichnung(auftrag.getCBez());
			auftragDto.setCBestellnummer(auftrag.getCBestellnummer());
			auftragDto.setDBestelldatum(auftrag.getTBestelldatum());
			auftragDto.setCAuftragswaehrung(auftrag
					.getWaehrungCNrAuftragswaehrung());
			auftragDto.setFWechselkursmandantwaehrungzubelegwaehrung(auftrag
					.getFWechselkursmandantwaehrungzuauftragswaehrung());
			// auftragDto.setFSonderrabattsatz(auftrag.getFSonderrabattsatz());
			auftragDto.setDLiefertermin(auftrag.getTLiefertermin());
			auftragDto.setBLieferterminUnverbindlich(auftrag
					.getBLieferterminunverbindlich());
			auftragDto.setDFinaltermin(auftrag.getTFinaltermin());
			auftragDto.setKostIId(auftrag.getKostenstelleIId());
			auftragDto.setBTeillieferungMoeglich(auftrag
					.getBTeillieferungmoeglich());
			auftragDto.setBPoenale(auftrag.getBPoenale());
			auftragDto.setBRoHs(auftrag.getBRoHs());
			auftragDto.setILeihtage(auftrag.getILeihtage());
			auftragDto.setFVersteckterAufschlag(auftrag
					.getFVersteckteraufschlag());
			auftragDto.setFAllgemeinerRabattsatz(auftrag
					.getFAllgemeinerrabattsatz());
			auftragDto.setFProjektierungsrabattsatz(auftrag
					.getFProjektierungsrabattsatz());
			auftragDto.setLieferartIId(auftrag.getLieferartIId());
			auftragDto.setZahlungszielIId(auftrag.getZahlungszielIId());
			auftragDto.setSpediteurIId(auftrag.getSpediteurIId());
			auftragDto.setIGarantie(auftrag.getIGarantie());
			auftragDto.setNGesamtauftragswertInAuftragswaehrung(auftrag
					.getNGesamtauftragswertinauftragswaehrung());
			auftragDto.setNRohdeckungInMandantenwaehrung(auftrag
					.getNRohdeckunginmandantenwaehrung());
			auftragDto.setNRohdeckungaltInMandantenwaehrung(auftrag
					.getNRohdeckungaltinmandantenwaehrung());
			auftragDto.setNMaterialwertInMandantenwaehrung(auftrag
					.getNMaterialwertinmandantenwaehrung());
			auftragDto.setStatusCNr(auftrag.getAuftragstatusCNr());
			auftragDto.setTBelegdatum(auftrag.getTBelegdatum());
			auftragDto.setTGedruckt(auftrag.getTGedruckt());
			auftragDto.setPersonalIIdStorniert(auftrag
					.getPersonalIIdStorniert());
			auftragDto.setTStorniert(auftrag.getTStorniert());
			auftragDto.setPersonalIIdAnlegen(auftrag.getPersonalIIdAnlegen());
			auftragDto.setTAnlegen(auftrag.getTAnlegen());
			auftragDto.setPersonalIIdAendern(auftrag.getPersonalIIdAendern());
			auftragDto.setTAendern(auftrag.getTAendern());
			auftragDto.setPersonalIIdManuellerledigt(auftrag
					.getPersonalIIdManuellerledigt());
			auftragDto.setTManuellerledigt(auftrag.getTManuellerledigt());
			auftragDto.setAuftragtextIIdKopftext(auftrag
					.getAuftragtextIIdKopftext());
			auftragDto.setCKopftextUebersteuert(auftrag
					.getXKopftextuebersteuert());
			auftragDto.setAuftragtextIIdFusstext(auftrag
					.getAuftragtextIIdFusstext());
			auftragDto.setCFusstextUebersteuert(auftrag
					.getXFusstextuebersteuert());
			auftragDto.setXExternerkommentar(auftrag.getXExternerkommentar());
			auftragDto.setXInternerkommentar(auftrag.getXInternerkommentar());
			auftragDto.setPersonalIIdErledigt(auftrag.getPersonalIIdErledigt());
			auftragDto.setTErledigt(auftrag.getTErledigt());
			auftragDto.setTLauftermin(auftrag.getTLauftermin());
			auftragDto.setWiederholungsintervallCNr(auftrag
					.getAuftragwiederholungsintervallCNr());
			auftragDto.setFErfuellungsgrad(auftrag.getFErfuellungsgrad());
			auftragDto.setTBegruendung(auftrag.getTBegruendung());
			auftragDto.setPersonalIIdBegruendung(auftrag
					.getPersonalIIdBegruendung());
			auftragDto.setAuftragbegruendungIId(auftrag
					.getAuftragbegruendungIId());
			auftragDto.setBVersteckt(auftrag.getBVersteckt());
			auftragDto.setAnsprechpartnerIIdLieferadresse(auftrag
					.getAnsprechpartnerIIdLieferadresse());
			auftragDto.setBMitzusammenfassung(auftrag.getBMitzusammenfassung());
			auftragDto.setLagerIIdAbbuchungslager(auftrag
					.getLagerIIdAbbuchungslager());
			auftragDto.setAnsprechpartnerIIdRechnungsadresse(auftrag
					.getAnsprechpartnerIIdRechnungsadresse());
			auftragDto.setCLieferartort(auftrag.getCLieferartort());
			auftragDto.setTVerrechenbar(auftrag.getTVerrechenbar());
			auftragDto.setPersonalIIdVerrechenbar(auftrag.getPersonalIIdVerrechenbar());
			auftragDto.setProjektIId(auftrag.getProjektIId());
			auftragDto.setNKorrekturbetrag(auftrag.getNKorrekturbetrag());
			auftragDto.setTResponse(auftrag.getTResponse());
			auftragDto.setPersonalIIdResponse(auftrag.getPersonalIIdResponse());
		}
		return auftragDto;
	}

	public static AuftragDto[] createDtos(Collection<?> auftrags) {
		List<AuftragDto> list = new ArrayList<AuftragDto>();
		if (auftrags != null) {
			Iterator<?> iterator = auftrags.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Auftrag) iterator.next()));
			}
		}
		AuftragDto[] returnArray = new AuftragDto[list.size()];
		return (AuftragDto[]) list.toArray(returnArray);
	}
}
