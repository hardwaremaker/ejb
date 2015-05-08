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
package com.lp.server.system.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.system.ejb.Mandant;

public class MandantDtoAssembler {
	public static MandantDto createDto(Mandant mandant) {
		MandantDto mandantDto = new MandantDto();
		if (mandant != null) {
			mandantDto.setCNr(mandant.getCNr());
			mandantDto.setCKbez(mandant.getCKbez());
			mandantDto.setPartnerIId(mandant.getPartnerIId());
			mandantDto.setBDemo(mandant.getBDemo());
			mandantDto.setWaehrungCNr(mandant.getWaehrungCNr());
			mandantDto.setSpediteurIIdKunde(mandant.getSpediteurIIdKunde());
			mandantDto.setLieferartIIdKunde(mandant.getLieferartIIdKunde());
			mandantDto.setZahlungszielIIdKunde(mandant.getZahlungszielIIdKunde());
			mandantDto.setSpediteurIIdLF(mandant.getSpediteurIIdLieferant());
			mandantDto.setLieferartIIdLF(mandant.getLieferartIIdLieferant());
			mandantDto.setZahlungszielIIdLF(mandant.getZahlungszielIIdLieferant());
			mandantDto.setVkpfArtikelpreislisteIId(mandant.getVkpfartikelpreislisteIId());
			mandantDto.setTAnlegen(mandant.getTAnlegen());
			mandantDto.setIAnlegen(mandant.getPersonalIIdAnlegen());
			mandantDto.setTAendern(mandant.getTAendern());
			mandantDto.setIAendern(mandant.getPersonalIIdAendern());
			mandantDto.setIIdKostenstelle(mandant.getKostenstelleIId());
			mandantDto.setMwstsatzbezIIdStandardinlandmwstsatz(mandant.getMwstsatzIIdStandardinlandmwstsatz());
			mandantDto.setMwstsatzbezIIdStandardauslandmwstsatz(mandant.getMwstsatzIIdStandardauslandmwstsatz());
			mandantDto.setMwstsatzbezIIdStandarddrittlandmwstsatz(mandant.getMwstsatzIIdStandarddrittlandmwstsatz());
			mandantDto.setIBankverbindung(mandant.getBankverbindungIIdMandant());
			mandantDto.setPartnerIIdLieferadresse(mandant.getPartnerIIdLieferadresse());
			mandantDto.setOCode(mandant.getOCode());
			mandantDto.setOHash(mandant.getOHash());
			mandantDto.setIBenutzermax(mandant.getIBenutzerMax());
			mandantDto.setBDemo(mandant.getBDemo());
			mandantDto.setKundeIIdStueckliste(mandant.getKundeIIdStueckliste());
			mandantDto.setPartnerIIdFinanzamt(mandant.getPartnerIIdFinanzamt());
			mandantDto.setJahreRueckdatierbar(mandant.getJahreRueckdatierbar());
			mandantDto.setKostenstelleIIdFibu(mandant.getKostenstelleIIdFibu());
		}
		return mandantDto;
/* AD
		if (mandantDto.validate()) {
			return mandantDto;
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_UNGUELTIGE_INSTALLATION, null);
		}
*/
	}

	public static MandantDto[] createDtos(Collection<?> mandants) {
		List<MandantDto> list = new ArrayList<MandantDto>();
		if (mandants != null) {
			Iterator<?> iterator = mandants.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Mandant) iterator.next()));
			}
		}
		MandantDto[] returnArray = new MandantDto[list.size()];
		return (MandantDto[]) list.toArray(returnArray);
	}
}
