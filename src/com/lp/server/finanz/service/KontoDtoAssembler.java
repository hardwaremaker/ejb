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
package com.lp.server.finanz.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.finanz.ejb.Konto;

public class KontoDtoAssembler {
	public static KontoDto createDto(Konto konto) {
		KontoDto kontoDto = new KontoDto();
		if (konto != null) {
			kontoDto.setIId(konto.getIId());
			kontoDto.setMandantCNr(konto.getMandantCNr());
			kontoDto.setCNr(konto.getCNr());
			kontoDto.setCBez(konto.getCBez());
			kontoDto.setKontoIIdWeiterfuehrendUst(konto
					.getKontoIIdWeiterfuehrendust());
			kontoDto.setRechenregelCNrWeiterfuehrendUst(konto
					.getRechenregelCNrWeiterfuehrendust());
			kontoDto.setRechenregelCNrWeiterfuehrendBilanz(konto
					.getRechenregelCNrWeiterfuehrendbilanz());
			kontoDto.setKontoIIdWeiterfuehrendSkonto(konto
					.getKontoIIdWeiterfuehrendskonto());
			kontoDto.setRechenregelCNrWeiterfuehrendSkonto(konto
					.getRechenregelCNrWeiterfuehrendskonto());
			kontoDto.setUvaartIId(konto.getUvaartIId());
			kontoDto.setDGueltigvon(konto.getTGueltigvon());
			kontoDto.setDGueltigbis(konto.getTGueltigbis());
			kontoDto.setFinanzamtIId(konto.getFinanzamtIId());
			kontoDto.setKostenstelleIId(konto.getKostenstelleIId());
			kontoDto.setBAutomeroeffnungsbuchung(konto
					.getBAutomeroeffnungsbuchung());
			kontoDto.setBAllgemeinsichtbar(konto.getBAllgemeinsichtbar());
			kontoDto.setBManuellbebuchbar(konto.getBManuellbebuchbar());
			kontoDto.setKontoartCNr(konto.getKontoartCNr());
			kontoDto.setKontotypCNr(konto.getKontotypCNr());
			kontoDto.setErgebnisgruppeIId(konto.getErgebnisgruppeIId());
			kontoDto.setTAnlegen(konto.getTAnlegen());
			kontoDto.setPersonalIIdAnlegen(konto.getPersonalIIdAnlegen());
			kontoDto.setTAendern(konto.getTAendern());
			kontoDto.setPersonalIIdAendern(konto.getPersonalIIdAendern());
			kontoDto.setCLetztesortierung(konto.getCLetztesortierung());
			kontoDto.setILetzteselektiertebuchung(konto
					.getILetzteselektiertebuchung());
			kontoDto.setBVersteckt(konto.getBVersteckt());
			kontoDto.setSteuerkategorieIId(konto.getSteuerkategorieIId());
			kontoDto.setSteuerkategorieIIdReverse(konto
					.getSteuerkategorieIIdReverse());
			kontoDto.setCsortierung(konto.getcSortierung());
			kontoDto.setxBemerkung(konto.getXBemerkung());
			kontoDto.setWaehrungCNrDruck(konto.getWaehrungCNrDruck());
			kontoDto.settEBAnlegen(konto.getTEBAnlegen());
			kontoDto.setiGeschaeftsjahrEB(konto.getiGeschaeftsjahrEB());
			kontoDto.setErgebnisgruppeIId_negativ(konto
					.getErgebnisgruppeIId_negativ());
			kontoDto.setBOhneUst(konto.getBOhneUst());
			kontoDto.setcSteuerart(konto.getcSteuerart());
		}
		return kontoDto;
	}

	public static KontoDto[] createDtos(Collection<?> kontos) {
		List<KontoDto> list = new ArrayList<KontoDto>();
		if (kontos != null) {
			Iterator<?> iterator = kontos.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Konto) iterator.next()));
			}
		}
		KontoDto[] returnArray = new KontoDto[list.size()];
		return (KontoDto[]) list.toArray(returnArray);
	}
}
