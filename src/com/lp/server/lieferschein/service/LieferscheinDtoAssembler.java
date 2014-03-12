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
package com.lp.server.lieferschein.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.lieferschein.ejb.Lieferschein;

public class LieferscheinDtoAssembler {
	public static LieferscheinDto createDto(Lieferschein lieferschein) {
		LieferscheinDto lieferscheinDto = new LieferscheinDto();
		if (lieferschein != null) {
			lieferscheinDto.setIId(lieferschein.getIId());
			lieferscheinDto.setRechnungIId(lieferschein.getRechnungIId());
			lieferscheinDto.setCNr(lieferschein.getCNr());
			lieferscheinDto.setLieferscheinartCNr(lieferschein
					.getLieferscheinartCNr());
			lieferscheinDto.setStatusCNr(lieferschein
					.getLieferscheinstatusCNr());
			lieferscheinDto.setBelegartCNr(lieferschein.getBelegartCNr());
			lieferscheinDto.setBVerrechenbar(lieferschein.getBVerrechenbar());
			lieferscheinDto.setTBelegdatum(lieferschein.getTBelegdatum());
			lieferscheinDto.setKundeIIdLieferadresse(lieferschein
					.getKundeIIdLieferadresse());
			lieferscheinDto.setAnsprechpartnerIId(lieferschein
					.getAnsprechpartnerIIdKunde());
			lieferscheinDto.setPersonalIIdVertreter(lieferschein
					.getPersonalIIdVertreter());
			lieferscheinDto.setKundeIIdRechnungsadresse(lieferschein
					.getKundeIIdRechnungsadresse());
			lieferscheinDto.setCBezProjektbezeichnung(lieferschein.getCBez());
			lieferscheinDto.setCBestellnummer(lieferschein.getCBestellnummer());
			lieferscheinDto.setLagerIId(lieferschein.getLagerIId());
			lieferscheinDto.setZiellagerIId(lieferschein.getZiellagerIId());
			lieferscheinDto.setKostenstelleIId(lieferschein
					.getKostenstelleIId());
			lieferscheinDto.setTLiefertermin(lieferschein.getTLiefertermin());
			lieferscheinDto.setTRueckgabetermin(lieferschein
					.getTRueckgabetermin());
			lieferscheinDto.setFVersteckterAufschlag(lieferschein
					.getFVersteckteraufschlag());
			lieferscheinDto.setFAllgemeinerRabattsatz(lieferschein
					.getFAllgemeinerrabatt());
			lieferscheinDto.setLieferartIId(lieferschein.getLieferartIId());
			lieferscheinDto.setBMindermengenzuschlag(lieferschein
					.getBMindermengenzuschlag());
			lieferscheinDto.setIAnzahlPakete(lieferschein.getIAnzahlpakete());
			lieferscheinDto.setFGewichtLieferung(lieferschein
					.getFGewichtlieferung());
			lieferscheinDto.setCVersandnummer(lieferschein.getCVersandnummer());
			lieferscheinDto.setTGedruckt(lieferschein.getTGedruckt());
			lieferscheinDto.setPersonalIIdManuellErledigt(lieferschein
					.getPersonalIIdManuellerledigt());
			lieferscheinDto.setTManuellErledigt(lieferschein
					.getTManuellerledigt());
			lieferscheinDto.setPersonalIIdStorniert(lieferschein
					.getPersonalIIdStorniert());
			lieferscheinDto.setTStorniert(lieferschein.getTStorniert());
			lieferscheinDto.setPersonalIIdAnlegen(lieferschein
					.getPersonalIIdAnlegen());
			lieferscheinDto.setTAnlegen(lieferschein.getTAnlegen());
			lieferscheinDto.setPersonalIIdAendern(lieferschein
					.getPersonalIIdAendern());
			lieferscheinDto.setTAendern(lieferschein.getTAendern());
			lieferscheinDto.setMandantCNr(lieferschein.getMandantCNr());
			lieferscheinDto.setWaehrungCNr(lieferschein
					.getWaehrungCNrLieferscheinwaehrung());
			lieferscheinDto
					.setFWechselkursmandantwaehrungzubelegwaehrung(lieferschein
							.getFWechselkursmandantwaehrungzulieferscheinwaehrung());
			lieferscheinDto.setNGesamtwertInLieferscheinwaehrung(lieferschein
					.getNGesamtwertinlieferscheinwaehrung());
			lieferscheinDto.setNGestehungswertInMandantenwaehrung(lieferschein
					.getNGestehungswertinmandantenwaehrung());
			lieferscheinDto.setZahlungszielIId(lieferschein
					.getZahlungszielIId());
			lieferscheinDto.setSpediteurIId(lieferschein.getSpediteurIId());
			lieferscheinDto.setLieferscheintextIIdDefaultKopftext(lieferschein
					.getLieferscheintextIIdKopftext());
			lieferscheinDto.setCLieferscheinKopftextUeberschrieben(lieferschein
					.getXKopftextuebersteuert());
			lieferscheinDto.setLieferscheintextIIdDefaultFusstext(lieferschein
					.getLieferscheintextIIdFusstext());
			lieferscheinDto.setCLieferscheinFusstextUeberschrieben(lieferschein
					.getXFusstextuebersteuert());
			lieferscheinDto.setAuftragIId(lieferschein.getAuftragIId());
			lieferscheinDto.setCKommission(lieferschein.getCKommission());
			lieferscheinDto.setBegruendungIId(lieferschein.getBegruendungIId());
			lieferscheinDto.setAnsprechpartnerIIdRechnungsadresse(lieferschein
					.getAnsprechpartnerIIdRechnungsadresse());
			lieferscheinDto.setCLieferartort(lieferschein.getCLieferartort());
			lieferscheinDto.setProjektIId(lieferschein.getProjektIId());
			lieferscheinDto.setTZollexportpapier(lieferschein.getTZollexportpapier());
			lieferscheinDto.setPersonalIIdZollexportpapier(lieferschein.getPersonalIIdZollexportpapier());
			lieferscheinDto.setEingangsrechnungIdZollexport(lieferschein.getEingangsrechnungIdZollexport());
			lieferscheinDto.setCZollexportpapier(lieferschein.getCZollexportpapier());
			
			lieferscheinDto.setTLieferaviso(lieferschein.getTLieferaviso());
			lieferscheinDto.setPersonalIIdLieferaviso(lieferschein.getPersonalIIdLieferaviso());
		}
		return lieferscheinDto;
	}

	public static LieferscheinDto[] createDtos(Collection<?> lieferscheins) {
		List<LieferscheinDto> list = new ArrayList<LieferscheinDto>();
		if (lieferscheins != null) {
			Iterator<?> iterator = lieferscheins.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Lieferschein) iterator.next()));
			}
		}
		LieferscheinDto[] returnArray = new LieferscheinDto[list.size()];
		return (LieferscheinDto[]) list.toArray(returnArray);
	}
}
