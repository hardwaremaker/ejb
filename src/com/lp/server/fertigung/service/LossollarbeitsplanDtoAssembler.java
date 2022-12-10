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
package com.lp.server.fertigung.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.fertigung.ejb.Lossollarbeitsplan;

public class LossollarbeitsplanDtoAssembler {
	public static LossollarbeitsplanDto createDto(
			Lossollarbeitsplan lossollarbeitsplan) {
		LossollarbeitsplanDto lossollarbeitsplanDto = new LossollarbeitsplanDto();
		if (lossollarbeitsplan != null) {
			lossollarbeitsplanDto.setIId(lossollarbeitsplan.getIId());
			lossollarbeitsplanDto.setLosIId(lossollarbeitsplan.getLosIId());
			lossollarbeitsplanDto.setArtikelIIdTaetigkeit(lossollarbeitsplan
					.getArtikelIIdTaetigkeit());
			lossollarbeitsplanDto.setLRuestzeit(lossollarbeitsplan
					.getLRuestzeit());
			lossollarbeitsplanDto.setLStueckzeit(lossollarbeitsplan
					.getLStueckzeit());
			lossollarbeitsplanDto.setNGesamtzeit(lossollarbeitsplan
					.getNGesamtzeit());
			lossollarbeitsplanDto.setIArbeitsgangnummer(lossollarbeitsplan
					.getIArbeitsgangnummer());
			lossollarbeitsplanDto.setPersonalIIdAendern(lossollarbeitsplan
					.getPersonalIIdAendern());
			lossollarbeitsplanDto.setTAendern(lossollarbeitsplan.getTAendern());
			lossollarbeitsplanDto.setXText(lossollarbeitsplan.getXText());
			lossollarbeitsplanDto.setCKomentar(lossollarbeitsplan
					.getCKomentar());
			lossollarbeitsplanDto.setBNachtraeglich(lossollarbeitsplan
					.getBNachtraeglich());
			lossollarbeitsplanDto.setMaschineIId(lossollarbeitsplan
					.getMaschineIId());

			lossollarbeitsplanDto.setIMaschinenversatztage(lossollarbeitsplan
					.getIMaschinenversatztage());

			lossollarbeitsplanDto.setBFertig(lossollarbeitsplan.getBFertig());
			lossollarbeitsplanDto.setAgartCNr(lossollarbeitsplan.getAgartCNr());
			lossollarbeitsplanDto.setIAufspannung(lossollarbeitsplan
					.getIAufspannung());
			lossollarbeitsplanDto.setIUnterarbeitsgang(lossollarbeitsplan
					.getIUnterarbeitsgang());
			lossollarbeitsplanDto.setBAutoendebeigeht(lossollarbeitsplan
					.getBAutoendebeigeht());
			lossollarbeitsplanDto.setBNurmaschinenzeit(lossollarbeitsplan
					.getBNurmaschinenzeit());
			lossollarbeitsplanDto.setPersonalIIdZugeordneter(lossollarbeitsplan
					.getPersonalIIdZugeordneter());

			lossollarbeitsplanDto.setLossollmaterialIId(lossollarbeitsplan
					.getLossollmaterialIId());
			lossollarbeitsplanDto.setIMaschinenversatzMs(lossollarbeitsplan
					.getIMaschinenversatzMs());
			lossollarbeitsplanDto.setFFortschritt(lossollarbeitsplan
					.getFFortschritt());
			lossollarbeitsplanDto.setApkommentarIId(lossollarbeitsplan
					.getApkommentarIId());

			lossollarbeitsplanDto.setNPpm(lossollarbeitsplan.getNPpm());
			lossollarbeitsplanDto.setPersonalIIdFertig(lossollarbeitsplan.getPersonalIIdFertig());
			lossollarbeitsplanDto.setTFertig(lossollarbeitsplan.getTFertig());
			
			lossollarbeitsplanDto.setIMaschinenversatztageAusStueckliste(lossollarbeitsplan
					.getIMaschinenversatztageAusStueckliste());
			lossollarbeitsplanDto.setIReihung(lossollarbeitsplan.getIReihung());
			lossollarbeitsplanDto.setTAgbeginnBerechnet(lossollarbeitsplan.getTAgbeginnBerechnet());
			

		}
		return lossollarbeitsplanDto;
	}

	public static LossollarbeitsplanDto[] createDtos(
			Collection<?> lossollarbeitsplans) {
		List<LossollarbeitsplanDto> list = new ArrayList<LossollarbeitsplanDto>();
		if (lossollarbeitsplans != null) {
			Iterator<?> iterator = lossollarbeitsplans.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Lossollarbeitsplan) iterator.next()));
			}
		}
		LossollarbeitsplanDto[] returnArray = new LossollarbeitsplanDto[list
				.size()];
		return (LossollarbeitsplanDto[]) list.toArray(returnArray);
	}
}
