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
package com.lp.server.personal.ejbfac;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.lp.server.personal.ejb.Zeitdatenpruefen;
import com.lp.server.personal.service.ZeitdatenpruefenDto;

public class ZeitdatenpruefenDtoAssembler {

	public static ZeitdatenpruefenDto createDto(Zeitdatenpruefen zeitdaten) {
		ZeitdatenpruefenDto zeitdatenDto = new ZeitdatenpruefenDto();
		if (zeitdaten != null) {
			zeitdatenDto.setIId(zeitdaten.getIId());
			zeitdatenDto.setPersonalIId(zeitdaten.getPersonalIId());
			zeitdatenDto.setTZeit(zeitdaten.getTZeit());
			zeitdatenDto.setCBelegartnr(zeitdaten.getCBelegartnr());
			zeitdatenDto.setIBelegartid(zeitdaten.getIBelegartid());
			zeitdatenDto.setIBelegartpositionid(zeitdaten
					.getIBelegartpositionid());
			zeitdatenDto.setTaetigkeitIId(zeitdaten.getTaetigkeitIId());
			zeitdatenDto.setArtikelIId(zeitdaten.getArtikelIId());
			zeitdatenDto.setBTaetigkeitgeaendert(zeitdaten
					.getBTaetigkeitgeaendert());
			zeitdatenDto.setBAutomatikbuchung(zeitdaten.getBAutomatikbuchung());
			zeitdatenDto.setPersonalIIdAnlegen(zeitdaten
					.getPersonalIIdAnlegen());
			zeitdatenDto.setTAnlegen(zeitdaten.getTAnlegen());
			zeitdatenDto.setPersonalIIdAendern(zeitdaten
					.getPersonalIIdAendern());
			zeitdatenDto.setTAendern(zeitdaten.getTAendern());
			zeitdatenDto.setXKommentar(zeitdaten.getXKommentar());
			zeitdatenDto.setCWowurdegebucht(zeitdaten.getCWowurdegebucht());
			zeitdatenDto.setFehlerCode(zeitdaten.getFehlerCode());
			zeitdatenDto.setXFehlertext(zeitdaten.getXFehlerText());
		}
		return zeitdatenDto;
	}

	public static ZeitdatenpruefenDto[] createDtos(Collection<Zeitdatenpruefen> zeitdatens) {
		List<ZeitdatenpruefenDto> list = new ArrayList<ZeitdatenpruefenDto>();
		if (zeitdatens != null) {
			for (Zeitdatenpruefen zeitdatenpruefen : zeitdatens) {
				list.add(createDto(zeitdatenpruefen));				
			}
		}
		ZeitdatenpruefenDto[] returnArray = new ZeitdatenpruefenDto[list.size()];
		return (ZeitdatenpruefenDto[]) list.toArray(returnArray);
	}
}
