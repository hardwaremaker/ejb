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

import com.lp.server.personal.ejb.Maschine;

public class MaschineDtoAssembler {
	public static MaschineDto createDto(Maschine maschine) {
		MaschineDto maschineDto = new MaschineDto();
		if (maschine != null) {
			maschineDto.setIId(maschine.getIId());
			maschineDto.setMandantCNr(maschine.getMandantCNr());
			maschineDto.setCInventarnummer(maschine.getCInventarnummer());
			maschineDto.setCIdentifikationsnr(maschine.getCIdentifikationsnr());
			maschineDto.setCBez(maschine.getCBez());
			maschineDto.setBAutoendebeigeht(maschine.getBAutoendebeigeht());
			maschineDto.setBVersteckt(maschine.getBVersteckt());
			maschineDto.setTKaufdatum(maschine.getTKaufdatum());
			maschineDto.setMaschinengruppeIId(maschine.getMaschinengruppeIId());

			maschineDto.setIAbschreibungInMonaten(maschine
					.getIAbschreibungInMonaten());
			maschineDto.setIPlanstunden(maschine.getIPlanstunden());
			maschineDto.setNAnschaffungskosten(maschine
					.getNAnschaffungskosten());
			maschineDto.setNEnergiekosten(maschine.getNEnergiekosten());
			maschineDto.setNRaumkosten(maschine.getNRaumkosten());
			maschineDto.setNVerzinsung(maschine.getNVerzinsung());
			maschineDto.setNSonstigekosten(maschine.getNSonstigekosten());
			maschineDto.setArtikelIIdVerrechnen(maschine.getArtikelIIdVerrechnen());
			maschineDto.setBManuelleBedienung(maschine.getBManuelleBedienung());
			maschineDto.setCSeriennummer(maschine.getCSeriennummer());

		}
		return maschineDto;
	}

	public static MaschineDto[] createDtos(Collection<?> maschines) {
		List<MaschineDto> list = new ArrayList<MaschineDto>();
		if (maschines != null) {
			Iterator<?> iterator = maschines.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Maschine) iterator.next()));
			}
		}
		MaschineDto[] returnArray = new MaschineDto[list.size()];
		return (MaschineDto[]) list.toArray(returnArray);
	}
}
