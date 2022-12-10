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

import com.lp.server.personal.ejb.Personal;

public class PersonalDtoAssembler {
	public static PersonalDto createDto(Personal personal) {
		PersonalDto personalDto = new PersonalDto();
		if (personal != null) {
			personalDto.setIId(personal.getIId());
			personalDto.setPartnerIId(personal.getPartnerIId());
			personalDto.setMandantCNr(personal.getMandantCNr());
			personalDto.setCPersonalnr(personal.getCPersonalnr());
			personalDto.setPersonalartCNr(personal.getPersonalartCNr());
			personalDto.setPersonalfunktionCNr(personal
					.getPersonalfunktionCNr());
			personalDto.setCAusweis(personal.getCAusweis());
			personalDto.setBMaennlich(personal.getBMaennlich());
			personalDto.setFamilienstandCNr(personal.getFamilienstandCNr());
			personalDto.setKollektivIId(personal.getKollektivIId());
			personalDto.setBerufIId(personal.getBerufIId());
			personalDto.setLohngruppeIId(personal.getLohngruppeIId());
			personalDto.setLandIIdStaatsangehoerigkeit(personal
					.getLandIIdStaatsangehoerigkeit());
			personalDto.setBUeberstundenausbezahlt(personal
					.getBUeberstundenausbezahlt());
			personalDto.setReligionIId(personal.getReligionIId());
			personalDto.setLandplzortIIdGeburt(personal
					.getLandplzortIIdGeburt());
			personalDto.setTGeburtsdatum(personal.getTGeburtsdatum());
			personalDto.setCSozialversnr(personal.getCSozialversnr());
			personalDto.setPartnerIIdSozialversicherer(personal
					.getPartnerIIdSozialversicherer());
			personalDto.setPartnerIIdFirma(personal.getPartnerIIdFirma());
			personalDto.setKostenstelleIIdAbteilung(personal
					.getKostenstelleIIdAbteilung());
			personalDto.setKostenstelleIIdStamm(personal
					.getKostenstelleIIdStamm());
			personalDto.setBAnwesenheitsliste(personal.getBAnwesenheitsliste());
			personalDto.setCKurzzeichen(personal.getCKurzzeichen());
			personalDto.setPendlerpauschaleIId(personal
					.getPendlerpauschaleIId());
			personalDto.setCUnterschriftsfunktion(personal
					.getCUnterschriftsfunktion());
			personalDto.setCUnterschriftstext(personal.getCUnterschriftstext());

			personalDto.setCImapbenutzer(personal.getCImapbenutzer());
			personalDto.setCImapkennwort(personal.getCImapkennwort());

			personalDto.setXKommentar(personal.getXKommentar());
			personalDto.setBVersteckt(personal.getBVersteckt());
			personalDto.setBAnwesenheitTerminal(personal
					.getBAnwesenheitTerminal());
			personalDto.setTAnlegen(personal.getTAnlegen());
			personalDto.setPersonalIIdAnlegen(personal.getPersonalIIdAnlegen());
			personalDto.setTAendern(personal.getTAendern());
			personalDto.setPersonalIIdAendern(personal.getPersonalIIdAendern());
			personalDto.setPersonalgruppeIId(personal.getPersonalgruppeIId());

			personalDto.setCFax(personal.getCFax());
			personalDto.setCEmail(personal.getCEmail());
			personalDto.setCDirektfax(personal.getCDirektfax());
			personalDto.setCHandy(personal.getCHandy());
			personalDto.setCTelefon(personal.getCTelefon());
			personalDto.setBAnwesenheitTerminal(personal
					.getBAnwesenheitTerminal());
			personalDto.setBAnwesenheitalleterminal(personal
					.getBAnwesenheitalleterminal());
			personalDto.setBTelefonzeitstarten(personal
					.getBTelefonzeitstarten());
			personalDto.setCImapInboxFolder(personal.getCImapInboxFolder());
			personalDto.setBStartMitMeinenOffenenProjekten(personal
					.getBStartMitMeinenOffenenProjekten());
			personalDto.setBKommtAmTerminal(personal.getBKommtAmTerminal());
			personalDto.setCBccempfaenger(personal.getCBccempfaenger());
			personalDto.setBKeineAnzeigeAmTerminal(personal.getBKeineAnzeigeAmTerminal());
			personalDto.setBWepInfo(personal.getBWepInfo());
			personalDto.setMaschinengruppeIId(personal.getMaschinengruppeIId());
			personalDto.setbSynchronisiereAlleKontakte(personal.getbSynchAlleKontakte());
			personalDto.setCVersandkennwort(personal.getCVersandkennwort());
		}
		return personalDto;
	}

	public static PersonalDto[] createDtos(Collection<?> personals) {
		List<PersonalDto> list = new ArrayList<PersonalDto>();
		if (personals != null) {
			Iterator<?> iterator = personals.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Personal) iterator.next()));
			}
		}
		PersonalDto[] returnArray = new PersonalDto[list.size()];
		return (PersonalDto[]) list.toArray(returnArray);
	}
}
