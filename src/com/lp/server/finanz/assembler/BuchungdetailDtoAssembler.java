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
package com.lp.server.finanz.assembler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.finanz.ejb.Buchungdetail;
import com.lp.server.finanz.service.BuchungdetailDto;

public class BuchungdetailDtoAssembler {
	public static BuchungdetailDto createDto(Buchungdetail buchungdetail) {
		BuchungdetailDto buchungdetailDto = new BuchungdetailDto();
		if (buchungdetail != null) {
			buchungdetailDto.setIId(buchungdetail.getIId());
			buchungdetailDto.setBuchungIId(buchungdetail.getBuchungIId());
			buchungdetailDto.setKontoIId(buchungdetail.getKontoIId());
			buchungdetailDto.setKontoIIdGegenkonto(buchungdetail
					.getKontoIIdGegenkonto());
			buchungdetailDto.setNBetrag(buchungdetail.getNBetrag());
			buchungdetailDto.setBuchungdetailartCNr(buchungdetail
					.getBuchungdetailartCNr());
			buchungdetailDto.setNUst(buchungdetail.getNUst());
			buchungdetailDto.setTAnlegen(buchungdetail.getTAnlegen());
			buchungdetailDto.setPersonalIIdAnlegen(buchungdetail
					.getPersonalIIdAnlegen());
			buchungdetailDto.setTAendern(buchungdetail.getTAendern());
			buchungdetailDto.setPersonalIIdAendern(buchungdetail
					.getPersonalIIdAendern());
			buchungdetailDto.setIAuszug(buchungdetail.getIAuszug());
			buchungdetailDto.setIAusziffern(buchungdetail.getIAusziffern());
			buchungdetailDto.setCKommentar(buchungdetail.getCKommentar());
		}
		return buchungdetailDto;
	}

	public static BuchungdetailDto[] createDtos(Collection<?> buchungdetails) {
		List<BuchungdetailDto> list = new ArrayList<BuchungdetailDto>();
		if (buchungdetails != null) {
			Iterator<?> iterator = buchungdetails.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Buchungdetail) iterator.next()));
			}
		}
		BuchungdetailDto[] returnArray = new BuchungdetailDto[list.size()];
		return (BuchungdetailDto[]) list.toArray(returnArray);
	}
}
