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
package com.lp.server.bestellung.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.lp.server.bestellung.ejb.Bestellung;

public class BestellungDtoAssembler {
	public static BestellungDto createDto(Bestellung bestellung) {
		BestellungDto bestellungDto = new BestellungDto();
		if (bestellung != null) {
			bestellungDto.setIId(bestellung.getIId());
			bestellungDto.setCNr(bestellung.getCNr());
			bestellungDto.setMandantCNr(bestellung.getMandantCNr());
			bestellungDto.setBestellungartCNr(bestellung.getBestellungartCNr());
			bestellungDto.setStatusCNr(bestellung.getBestellungstatusCNr());
			bestellungDto.setBelegartCNr(bestellung.getBelegartCNr());
			bestellungDto.setDBelegdatum(bestellung.getTBelegdatum());
			bestellungDto.setLieferantIIdBestelladresse(bestellung
					.getLieferantIIdBestelladresse());
			bestellungDto.setAnsprechpartnerIId(bestellung
					.getAnsprechpartnerIId());
			bestellungDto.setPersonalIIdAnforderer(bestellung
					.getPersonalIIdAnforderer());
			bestellungDto.setLieferantIIdRechnungsadresse(bestellung
					.getLieferantIIdRechnungsadresse());
			bestellungDto.setCBez(bestellung.getCBezprojektbezeichnung());
			bestellungDto.setWaehrungCNr(bestellung
					.getWaehrungCNrBestellungswaehrung());
			bestellungDto
					.setFWechselkursmandantwaehrungzubelegwaehrung(bestellung
							.getFWechselkursmandantwaehrungbestellungswaehrung());
			bestellungDto.setDLiefertermin(bestellung.getTLiefertermin());
			bestellungDto.setKostenstelleIId(bestellung.getKostenstelleIId());
			bestellungDto.setBTeillieferungMoeglich(bestellung
					.getBTeillieferungmoeglich());
			bestellungDto.setILeihtage(bestellung.getILeihtage());
			bestellungDto.setFAllgemeinerRabattsatz(bestellung
					.getFAllgemeinerrabattsatz());
			bestellungDto.setLieferartIId(bestellung.getLieferartIId());
			bestellungDto.setZahlungszielIId(bestellung.getZahlungszielIId());
			bestellungDto.setSpediteurIId(bestellung.getSpediteurIId());
			bestellungDto.setNBestellwert(bestellung.getNBestellwert());
			bestellungDto.setAnfrageIId(bestellung.getAnfrageIId());
			bestellungDto.setBestelltextIIdKopftext(bestellung
					.getBestellungtextIIdKopftext());
			bestellungDto.setCKopftextUebersteuert(bestellung
					.getCKopftextuebersteuert());
			bestellungDto.setBestelltextIIdFusstext(bestellung
					.getBestellungtextIIdFusstext());
			bestellungDto.setCFusstextUebersteuert(bestellung
					.getCFusstextuebersteuert());
			bestellungDto.setTGedruckt(bestellung.getTGedruckt());
			bestellungDto.setPersonalIIdStorniert(bestellung
					.getPersonalIIdStorniert());
			bestellungDto.setTStorniert(bestellung.getTStorniert());
			bestellungDto.setPersonalIIdAnlegen(bestellung
					.getPersonalIIdAnlegen());
			bestellungDto.setTAnlegen(bestellung.getTAnlegen());
			bestellungDto.setPersonalIIdAendern(bestellung
					.getPersonalIIdAendern());
			bestellungDto.setTAendern(bestellung.getTAendern());
			bestellungDto.setPartnerIIdLieferadresse(bestellung
					.getPartnerIIdLieferadresse());
			bestellungDto.setIBestellungIIdRahmenbestellung(bestellung
					.getBestellungIIdRahmenbestellung());
			bestellungDto.setXExternerKommentar(bestellung
					.getXExternerkommentar());
			bestellungDto.setXInternerKommentar(bestellung
					.getXInternerkommentar());
			bestellungDto.setTMahnsperreBis(bestellung.getTMahnsperrebis());
			bestellungDto.setTManuellGeliefert(bestellung
					.getTManuellgeliefert());
			bestellungDto.setAuftragIId(bestellung.getAuftragIId());
			bestellungDto.setIMahnstufeIId(bestellung.getMahnstufeIId());
			bestellungDto.setTAenderungsbestellung(bestellung
					.getTAenderungsbestellung());
			bestellungDto.setTVersandzeitpunkt(bestellung
					.getTVersandzeitpunkt());
			bestellungDto.setAnsprechpartnerIIdLieferadresse(bestellung
					.getAnsprechpartnerIIdLieferadresse());

			bestellungDto.setPartnerIIdAbholadresse(bestellung
					.getPartnerIIdAbholadresse());
			bestellungDto.setAnsprechpartnerIIdAbholadresse(bestellung
					.getAnsprechpartnerIIdAbholadresse());
			bestellungDto.setBPoenale(bestellung.getBPoenale());
			bestellungDto.setCLieferantenangebot(bestellung
					.getCLieferantenangebot());
			bestellungDto.setCLieferartort(bestellung.getCLieferartort());
			bestellungDto.setProjektIId(bestellung.getProjektIId());
			bestellungDto.setNKorrekturbetrag(bestellung.getNKorrekturbetrag());
			bestellungDto.setTKommissionierungDurchgefuehrt(bestellung
					.getTKommissionierungDurchgefuehrt());
			bestellungDto.setTKommissionierungGeplant(bestellung
					.getTKommissionierungGeplant());
			bestellungDto.setTUebergabeTechnik(bestellung
					.getTUebergabeTechnik());
			bestellungDto.setTTVollstaendigGeliefert(bestellung.getTVollstaendigGeliefert());
			bestellungDto.setIVersion(bestellung.getIVersion());
			bestellungDto.setNTransportkosten(bestellung.getNTransportkosten());
			bestellungDto.setPersonalIIdInterneranforderer(bestellung.getPersonalIIdInterneranforderer());
		}
		return bestellungDto;
	}

	public static BestellungDto[] createDtos(Collection<?> bestellungs) {
		List<BestellungDto> list = createList((Collection<Bestellung>)bestellungs);
		BestellungDto[] returnArray = new BestellungDto[list.size()];
		return (BestellungDto[]) list.toArray(returnArray);
	}
	
	public static List<BestellungDto> createList(Collection<Bestellung> bestellungs) {
		List<BestellungDto> list = new ArrayList<BestellungDto>();
		if (bestellungs != null) {
			for (Bestellung bestellung : bestellungs) {
				list.add(createDto(bestellung));				
			}
		}
		
		return list;
	}
}
