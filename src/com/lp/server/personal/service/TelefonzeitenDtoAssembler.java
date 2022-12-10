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
package com.lp.server.personal.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.personal.ejb.Telefonzeiten;

public class TelefonzeitenDtoAssembler {

	public static TelefonzeitenDto createDto(Telefonzeiten telefonzeiten) {
		TelefonzeitenDto telefonzeitenDto = new TelefonzeitenDto();
		if (telefonzeiten != null) {
			telefonzeitenDto.setIId(telefonzeiten.getIId());
			telefonzeitenDto.setPersonalIId(telefonzeiten.getPersonalIId());
			telefonzeitenDto.setPartnerIId(telefonzeiten.getPartnerIId());
			telefonzeitenDto.setTVon(telefonzeiten.getTVon());
			telefonzeitenDto.setTBis(telefonzeiten.getTBis());
			telefonzeitenDto.setXKommentarext(telefonzeiten.getXKommentarext());
			telefonzeitenDto.setXKommentarint(telefonzeiten.getXKommentarint());
			telefonzeitenDto.setAnsprechpartnerIId(telefonzeiten
					.getAnsprechpartnerIId());
			telefonzeitenDto.setProjektIId(telefonzeiten.getProjektIId());
			
			telefonzeitenDto.setFVerrechenbar(telefonzeiten.getFVerrechenbar());
			telefonzeitenDto.setPersonalIIdErledigt(telefonzeiten.getPersonalIIdErledigt());
			telefonzeitenDto.setTErledigt(telefonzeiten.getTErledigt());
			telefonzeitenDto.setCTelefonnrGewaehlt(telefonzeiten.getCTelefonnrGewaehlt());
			
			telefonzeitenDto.setCTitel(telefonzeiten.getCTitel());
			telefonzeitenDto.setTWiedervorlage(telefonzeiten.getTWiedervorlage());
			telefonzeitenDto.setTWiedervorlageErledigt(telefonzeiten.getTWiedervorlageErledigt());
			telefonzeitenDto.setPersonalIIdZugewiesener(telefonzeiten.getPersonalIIdZugewiesener());
			telefonzeitenDto.setKontaktartIId(telefonzeiten.getKontaktartIId());
			
			telefonzeitenDto.setAuftragIId(telefonzeiten.getAuftragIId());
			telefonzeitenDto.setAngebotIId(telefonzeiten.getAngebotIId());
			telefonzeitenDto.setFDauerUebersteuert(telefonzeiten.getFDauerUebersteuert());
			
		}
		return telefonzeitenDto;
	}

	public static TelefonzeitenDto[] createDtos(Collection<?> telefonzeitens) {
		List<TelefonzeitenDto> list = new ArrayList<TelefonzeitenDto>();
		if (telefonzeitens != null) {
			Iterator<?> iterator = telefonzeitens.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Telefonzeiten) iterator.next()));
			}
		}
		TelefonzeitenDto[] returnArray = new TelefonzeitenDto[list.size()];
		return (TelefonzeitenDto[]) list.toArray(returnArray);
	}
}
