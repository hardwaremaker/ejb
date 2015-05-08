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
package com.lp.server.partner.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.partner.ejb.Serienbrief;
import com.lp.util.Helper;

public class SerienbriefDtoAssembler {
	public static SerienbriefDto createDto(Serienbrief serienbrief) {
		SerienbriefDto serienbriefDto = new SerienbriefDto();
		if (serienbrief != null) {
			serienbriefDto.setIId(serienbrief.getIId());
			serienbriefDto.setCBez(serienbrief.getCBez());
			serienbriefDto.setCPlz(serienbrief.getCPlz());
			serienbriefDto.setMandantCNr(serienbrief.getMandantCNr());
			serienbriefDto.setTAnlegen(serienbrief.getTAnlegen());
			serienbriefDto.setPersonalIIdAnlegen(serienbrief
					.getPersonalIIdAnlegen());
			serienbriefDto.setTAendern(serienbrief.getTAendern());
			serienbriefDto.setPersonalIIdAendern(serienbrief
					.getPersonalIIdAendern());
			serienbriefDto.setBGehtAnKunden(serienbrief.getBGehtankunden());
			serienbriefDto.setBGehtanlieferanten(serienbrief
					.getBGehtanlieferanten());
			serienbriefDto.setBGehtanmoeglichelieferanten(serienbrief
					.getBGehtanmoeglichelieferanten());
			serienbriefDto.setBGehtAnInteressenten(serienbrief
					.getBGehtaninteressenten());
			serienbriefDto.setBGehtanpartner(serienbrief.getBGehtanpartner());
			serienbriefDto.setBVersteckteDabei(serienbrief
					.getBVerstecktedabei());
			serienbriefDto.setAnsprechpartnerfunktionIId(serienbrief
					.getAnsprechpartnerfunktionIId());
			serienbriefDto.setBAnsprechpartnerfunktionAuchOhne(serienbrief
					.getBAnsprechpartnerfunktionauchohne());
			serienbriefDto.setSBetreff(serienbrief.getCBetreff());
			serienbriefDto.setSXText(serienbrief.getXText());
			serienbriefDto.setLandIId(serienbrief.getLandIId());
			serienbriefDto.setNAbumsatz(serienbrief.getNAbumsatz());
			serienbriefDto.setNBisumsatz(serienbrief.getNBisumsatz());
			serienbriefDto.setTUmsatzab(serienbrief.getTUmsatzab());
			serienbriefDto.setTUmsatzbis(serienbrief.getTUmsatzbis());
			serienbriefDto.setBMitzugeordnetenfirmen(serienbrief
					.getBMitzugeordnetenfirmen());
			serienbriefDto.setPartnerklasseIId(serienbrief
					.getPartnerklasseIId());
			serienbriefDto.setBrancheIId(serienbrief.getBrancheIId());
			serienbriefDto.setXMailtext(serienbrief.getXMailtext());
			serienbriefDto.setNewsletter(Helper.short2boolean(serienbrief.getbNewsletter()));
			serienbriefDto.setBWennkeinanspmitfktDannersteransp(serienbrief.getBWennkeinanspmitfktDannersteransp());
			serienbriefDto.setBSelektionenLogischesOder(serienbrief.getBSelektionenLogischesOder());

		}
		return serienbriefDto;
	}

	public static SerienbriefDto[] createDtos(Collection<?> serienbriefs) {
		List<SerienbriefDto> list = new ArrayList<SerienbriefDto>();
		if (serienbriefs != null) {
			Iterator<?> iterator = serienbriefs.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Serienbrief) iterator.next()));
			}
		}
		SerienbriefDto[] returnArray = new SerienbriefDto[list.size()];
		return (SerienbriefDto[]) list.toArray(returnArray);
	}
}
