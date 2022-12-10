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

import com.lp.server.partner.ejb.Partner;

public class PartnerDtoAssembler {
	public static PartnerDto createDto(Partner partner) {
		PartnerDto partnerDto = new PartnerDto();
		if (partner != null) {
			partnerDto.setIId(partner.getIId());
			partnerDto.setLocaleCNrKommunikation(partner
					.getLocaleCNrKommunikation());
			partnerDto.setPartnerartCNr(partner.getPartnerartCNr());
			partnerDto.setCKbez(partner.getCKbez());
			partnerDto.setCIln(partner.getCIln());
			partnerDto.setCFilialnummer(partner.getCFilialnummer());
			partnerDto.setAnredeCNr(partner.getAnredeCNr());
			partnerDto.setCName1nachnamefirmazeile1(partner
					.getCName1nachnamefirmazeile1());
			partnerDto.setCName2vornamefirmazeile2(partner
					.getCName2vornamefirmazeile2());
			partnerDto.setCName3vorname2abteilung(partner
					.getCName3vorname2abteilung());
			partnerDto.setCStrasse(partner.getCStrasse());
			partnerDto.setLandplzortIId(partner.getLandplzortIId());
			partnerDto.setLandplzortIIdPostfach(partner
					.getLandplzortIIdPostfach());
			partnerDto.setCPostfach(partner.getCPostfach());
			partnerDto.setCAdressart(partner.getCAdressart());
			partnerDto.setBrancheIId(partner.getBrancheIId());
			partnerDto.setPartnerklasseIId(partner.getPartnerklasseIId());
			partnerDto.setPartnerIIdVater(partner.getPartnerIIdVater());
			partnerDto.setCUid(partner.getCUid());
			partnerDto.setXBemerkung(partner.getXBemerkung());
			if (partner.getTGeburtsdatumansprechpartner() != null) {
				partnerDto.setDGeburtsdatumansprechpartner(partner
						.getTGeburtsdatumansprechpartner());
			} else {
				partnerDto.setDGeburtsdatumansprechpartner(null);
			}
			partnerDto.setRechtsformIId(partner.getRechtsformIId());
			partnerDto.setPartnerIIdEigentuemer(partner
					.getPartnerIIdEigentuemer());
			partnerDto.setCFirmenbuchnr(partner.getCFirmenbuchnr());
			partnerDto.setCTitel(partner.getCTitel());
			partnerDto.setCNtitel(partner.getCNtitel());
			partnerDto.setCGerichtsstand(partner.getCGerichtsstand());
			partnerDto.setBVersteckt(partner.getBVersteckt());
			partnerDto.setLagerIIdZiellager(partner.getLagerIIdZiellager());
			partnerDto.setTAnlegen(partner.getTAnlegen());
			partnerDto.setPersonalIIdAnlegen(partner.getPersonalIIdAnlegen());
			partnerDto.setTAendern(partner.getTAendern());
			partnerDto.setPersonalIIdAendern(partner.getPersonalIIdAendern());
			partnerDto.setOBild(partner.getOBild());
			partnerDto.setCDirektfax(partner.getCDirektfax());
			partnerDto.setCEmail(partner.getCEmail());
			partnerDto.setCFax(partner.getCFax());
			partnerDto.setCHandy(partner.getCHandy());
			partnerDto.setCTelefon(partner.getCTelefon());
			partnerDto.setCHomepage(partner.getCHomepage());
			partnerDto.setLandIIdAbweichendesustland(partner
					.getLandIIdAbweichendesustland());

			partnerDto.setFGmtversatz(partner.getFGmtversatz());
			partnerDto.setCEori(partner.getCEori());
			partnerDto.setVersandwegIId(partner.getVersandwegIId());
			partnerDto.setNewslettergrundIId(partner.getNewslettergrundIId());

		}
		return partnerDto;
	}

	public static PartnerDtoSmall createDtoSmall(Partner partner) {
		PartnerDtoSmall partnerDtoSmall = new PartnerDtoSmall();
		if (partner != null) {
			partnerDtoSmall.setiId(partner.getIId());
			partnerDtoSmall.setAnredeCNr(partner.getAnredeCNr());
			partnerDtoSmall.setcName1nachnamefirmazeile1(partner
					.getCName1nachnamefirmazeile1());
			partnerDtoSmall.setcName2vornamefirmazeile2(partner
					.getCName2vornamefirmazeile2());
			partnerDtoSmall.setcTitel(partner.getCTitel());
		}
		return partnerDtoSmall;
	}

	public static PartnerDto[] createDtos(Collection<?> partners) {
		List<PartnerDto> list = new ArrayList<PartnerDto>();
		if (partners != null) {
			Iterator<?> iterator = partners.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Partner) iterator.next()));
			}
		}
		PartnerDto[] returnArray = new PartnerDto[list.size()];
		return (PartnerDto[]) list.toArray(returnArray);
	}
}
