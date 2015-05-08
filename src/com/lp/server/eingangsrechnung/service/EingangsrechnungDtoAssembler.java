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
package com.lp.server.eingangsrechnung.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.eingangsrechnung.ejb.Eingangsrechnung;

public class EingangsrechnungDtoAssembler {
	public static EingangsrechnungDto createDto(
			Eingangsrechnung eingangsrechnung) {
		EingangsrechnungDto eingangsrechnungDto = new EingangsrechnungDto();
		if (eingangsrechnung != null) {
			eingangsrechnungDto.setIId(eingangsrechnung.getIId());
			eingangsrechnungDto.setCNr(eingangsrechnung.getCNr());
			eingangsrechnungDto.setIGeschaeftsjahr(eingangsrechnung
					.getIGeschaeftsjahr());
			eingangsrechnungDto.setMandantCNr(eingangsrechnung.getMandantCNr());
			eingangsrechnungDto.setEingangsrechnungartCNr(eingangsrechnung
					.getEingangsrechnungartCNr());
			eingangsrechnungDto.setDBelegdatum(eingangsrechnung
					.getTBelegdatum());
			eingangsrechnungDto.setDFreigabedatum(eingangsrechnung
					.getTFreigabedatum());
			eingangsrechnungDto.setLieferantIId(eingangsrechnung
					.getLieferantIId());
			eingangsrechnungDto.setCText(eingangsrechnung.getCText());
			eingangsrechnungDto.setKostenstelleIId(eingangsrechnung
					.getKostenstelleIId());
			eingangsrechnungDto.setZahlungszielIId(eingangsrechnung
					.getZahlungszielIId());
			eingangsrechnungDto.setBestellungIId(eingangsrechnung
					.getBestellungIId());
			eingangsrechnungDto.setKontoIId(eingangsrechnung.getKontoIId());
			eingangsrechnungDto.setNBetrag(eingangsrechnung.getNBetrag());
			eingangsrechnungDto.setNBetragfw(eingangsrechnung.getNBetragfw());
			eingangsrechnungDto.setNUstBetrag(eingangsrechnung.getNUstbetrag());
			eingangsrechnungDto.setNUstBetragfw(eingangsrechnung
					.getNUstbetragfw());
			eingangsrechnungDto.setMwstsatzIId(eingangsrechnung
					.getMwstsatzIId());
			eingangsrechnungDto.setNKurs(eingangsrechnung.getNKurs());
			eingangsrechnungDto.setWaehrungCNr(eingangsrechnung
					.getWaehrungCNr());
			eingangsrechnungDto.setStatusCNr(eingangsrechnung.getStatusCNr());
			eingangsrechnungDto.setDBezahltdatum(eingangsrechnung
					.getTBezahltdatum());
			eingangsrechnungDto.setPersonalIIdAnlegen(eingangsrechnung
					.getPersonalIIdAnlegen());
			eingangsrechnungDto.setTAnlegen(eingangsrechnung.getTAnlegen());
			eingangsrechnungDto.setPersonalIIdAendern(eingangsrechnung
					.getPersonalIIdAendern());
			eingangsrechnungDto.setTAendern(eingangsrechnung.getTAendern());
			eingangsrechnungDto.setPersonalIIdManuellerledigt(eingangsrechnung
					.getPersonalIIdManuellerledigt());
			eingangsrechnungDto.setTManuellerledigt(eingangsrechnung
					.getTManuellerledigt());
			eingangsrechnungDto.setCLieferantenrechnungsnummer(eingangsrechnung
					.getCLieferantenrechnungsnummer());
			eingangsrechnungDto.setTFibuuebernahme(eingangsrechnung
					.getTFibuuebernahme());
			eingangsrechnungDto.setCKundendaten(eingangsrechnung
					.getCKundendaten());
			eingangsrechnungDto.setTGedruckt(eingangsrechnung.getTGedruckt());
			eingangsrechnungDto.setWiederholungsintervallCNr(eingangsrechnung
					.getAuftragwiederholungsintervallCNr());
			eingangsrechnungDto
					.setEingangsrechnungIIdNachfolger(eingangsrechnung
							.getEingangsrechnungIIdNachfolger());
			eingangsrechnungDto.setBReversecharge(eingangsrechnung
					.getBReversecharge());
			eingangsrechnungDto.setBIgErwerb(eingangsrechnung.getBIgErwerb());
			eingangsrechnungDto.setMahnstufeIId(eingangsrechnung
					.getMahnstufeIId());
			eingangsrechnungDto.setTMahndatum(eingangsrechnung.getTMahndatum());
			eingangsrechnungDto
					.setPersonalIIdWiederholenderledigt(eingangsrechnung
							.getPersonalIIdWiederholenderledigt());
			eingangsrechnungDto.setTWiederholenderledigt(eingangsrechnung
					.getTWiederholenderledigt());
			eingangsrechnungDto.setTZollimportpapier(eingangsrechnung
					.getTZollimportpapier());
			eingangsrechnungDto.setPersonalIIdZollimportpapier(eingangsrechnung
					.getPersonalIIdZollimportpapier());
			eingangsrechnungDto.setCZollimportpapier(eingangsrechnung
					.getCZollimportpapier());
			eingangsrechnungDto.setCWeartikel(eingangsrechnung.getCWeartikel());
			eingangsrechnungDto.setEingangsrechnungIdZollimport(eingangsrechnung
					.getEingangsrechnungIdZollimport());
		}
		return eingangsrechnungDto;
	}

	public static EingangsrechnungDto[] createDtos(
			Collection<?> eingangsrechnungs) {
		List<EingangsrechnungDto> list = new ArrayList<EingangsrechnungDto>();
		if (eingangsrechnungs != null) {
			Iterator<?> iterator = eingangsrechnungs.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Eingangsrechnung) iterator.next()));
			}
		}
		EingangsrechnungDto[] returnArray = new EingangsrechnungDto[list.size()];
		return (EingangsrechnungDto[]) list.toArray(returnArray);
	}
}
