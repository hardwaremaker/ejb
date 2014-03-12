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
package com.lp.server.rechnung.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.rechnung.ejb.Rechnung;

public class RechnungDtoAssembler {

	public static RechnungDto createDto(Rechnung rechnung) {
		RechnungDto rechnungDto = new RechnungDto();
		if (rechnung != null) {
			rechnungDto.setIId(rechnung.getIId());
			rechnungDto.setMandantCNr(rechnung.getMandantCNr());
			rechnungDto.setIGeschaeftsjahr(rechnung.getIGeschaeftsjahr());
			rechnungDto.setCNr(rechnung.getCNr());
			rechnungDto.setRechnungIIdZurechnung(rechnung
					.getRechnungIIdZurechnung());
			rechnungDto.setKundeIId(rechnung.getKundeIId());
			rechnungDto.setAnsprechpartnerIId(rechnung.getAnsprechpartnerIId());
			rechnungDto.setAuftragIId(rechnung.getAuftragIId());
			rechnungDto.setLieferscheinIId(rechnung.getLieferscheinIId());
			rechnungDto.setLagerIId(rechnung.getLagerIId());
			rechnungDto.setTBelegdatum(rechnung.getTBelegdatum());
			rechnungDto.setStatusCNr(rechnung.getStatusCNr());
			rechnungDto.setRechnungartCNr(rechnung.getRechnungartCNr());
			rechnungDto.setKostenstelleIId(rechnung.getKostenstelleIId());
			rechnungDto.setWaehrungCNr(rechnung.getWaehrungCNr());
			rechnungDto.setNKurs(rechnung.getNKurs());
			rechnungDto.setMwstsatzIId(rechnung.getMwstsatzIId());
			rechnungDto.setBMwstallepositionen(rechnung
					.getBMwstallepositionen());
			rechnungDto.setNWert(rechnung.getNWert());
			rechnungDto.setNWertfw(rechnung.getNWertfw());
			rechnungDto.setNWertust(rechnung.getNWertust());
			rechnungDto.setNWertustfw(rechnung.getNWertustfw());
			rechnungDto.setFVersteckterAufschlag(rechnung
					.getFVersteckteraufschlag());
			rechnungDto.setFAllgemeinerRabattsatz(rechnung
					.getFAllgemeinerrabattsatz());
			rechnungDto.setBMindermengenzuschlag(rechnung
					.getBMindermengenzuschlag());
			rechnungDto.setNProvision(rechnung.getNProvision());
			rechnungDto.setCProvisiontext(rechnung.getCProvisiontext());
			rechnungDto.setZahlungszielIId(rechnung.getZahlungszielIId());
			rechnungDto.setLieferartIId(rechnung.getLieferartIId());
			rechnungDto.setSpediteurIId(rechnung.getSpediteurIId());
			rechnungDto.setTGedruckt(rechnung.getTGedruckt());
			rechnungDto.setTFibuuebernahme(rechnung.getTFibuuebernahme());
			rechnungDto.setCKopftextuebersteuert(rechnung
					.getCKopftextuebersteuert());
			rechnungDto.setCFusstextuebersteuert(rechnung
					.getCFusstextuebersteuert());
			rechnungDto.setTStorniert(rechnung.getTStorniert());
			rechnungDto.setPersonalIIdStorniert(rechnung
					.getPersonalIIdStorniert());
			rechnungDto.setTBezahltdatum(rechnung.getTBezahltdatum());
			rechnungDto.setTMahnsperrebis(rechnung.getTMahnsperrebis());
			rechnungDto.setTAnlegen(rechnung.getTAnlegen());
			rechnungDto.setPersonalIIdAnlegen(rechnung.getPersonalIIdAnlegen());
			rechnungDto.setTAendern(rechnung.getTAendern());
			rechnungDto.setPersonalIIdAendern(rechnung.getPersonalIIdAendern());
			rechnungDto.setTManuellerledigt(rechnung.getTManuellerledigt());
			rechnungDto.setPersonalIIdManuellerledigt(rechnung
					.getPersonalIIdManuellerledigt());
			rechnungDto.setCBestellnummer(rechnung.getCBestellnummer());
			rechnungDto.setBReversecharge(rechnung.getBReversecharge());
			rechnungDto.setKundeIIdStatistikadresse(rechnung
					.getKundeIIdStatistikadresse());
			rechnungDto.setPersonalIIdVertreter(rechnung
					.getPersonalIIdVertreter());
			rechnungDto.setIZahltagMtlZahlbetrag(rechnung
					.getIZahltagMtlZahlbetrag());
			rechnungDto.setNMtlZahlbetrag(rechnung.getNMtlZahlbetrag());
			rechnungDto.setCLieferartort(rechnung.getCLieferartort());
			rechnungDto.setCBez(rechnung.getCBez());
			rechnungDto.setPersonalIIdZollpapier(rechnung
					.getPersonalIIdZollpapier());
			rechnungDto.setTZollpapier(rechnung.getTZollpapier());
			rechnungDto.setCZollpapier(rechnung.getCZollpapier());
			rechnungDto.setProjektIId(rechnung.getProjektIId());
		}
		return rechnungDto;
	}

	public static RechnungDto[] createDtos(Collection<?> rechnungs) {
		List<RechnungDto> list = new ArrayList<RechnungDto>();
		if (rechnungs != null) {
			Iterator<?> iterator = rechnungs.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Rechnung) iterator.next()));
			}
		}
		RechnungDto[] returnArray = new RechnungDto[list.size()];
		return (RechnungDto[]) list.toArray(returnArray);
	}
}
