/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2014 HELIUM V IT-Solutions GmbH
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
package com.lp.server.rechnung.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.rechnung.ejb.Rechnungart;

/**
 * <p>
 * <I></I>
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * <p>
 * Erstellungsdatum <I>6. 8. 2004</I>
 * </p>
 * <p>
 * </p>
 * 
 * @author Martin Bluehweis
 * @version 1.0
 */
public class RechnungartDtoAssembler {
	public static RechnungartDto createDto(Rechnungart rechnungart) {
		RechnungartDto rechnungartDto = new RechnungartDto();
		if (rechnungart != null) {
			rechnungartDto.setCNr(rechnungart.getCNr());
			rechnungartDto.setRechnungtypCNr(rechnungart.getRechnungtypCNr());
			rechnungartDto.setISort(rechnungart.getISort());
		}
		return rechnungartDto;
	}

	public static RechnungartDto[] createDtos(Collection<?> rechnungarts) {
		List<RechnungartDto> list = new ArrayList<RechnungartDto>();
		if (rechnungarts != null) {
			Iterator<?> iterator = rechnungarts.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Rechnungart) iterator.next()));
			}
		}
		RechnungartDto[] returnArray = new RechnungartDto[list.size()];
		return (RechnungartDto[]) list.toArray(returnArray);
	}
}
