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
package com.lp.server.finanz.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.lp.server.system.ejb.Laenderartspr;

public class LaenderartsprDtoAssembler {
	public static LaenderartsprDto createDto(Laenderartspr laenderartspr) {
		LaenderartsprDto laenderartsprDto = new LaenderartsprDto();
		if (laenderartspr != null) {
			laenderartsprDto.setLaenderartCNr(laenderartspr.getPk().getLaenderartCNr());
			laenderartsprDto.setLocaleCNr(laenderartspr.getPk().getLocaleCNr());
			laenderartsprDto.setCBez(laenderartspr.getCBez());
		}
		return laenderartsprDto;
	}

	public static LaenderartsprDto[] createDtos(Collection<?> laenderartsprs) {
		List<LaenderartsprDto> list = new ArrayList<LaenderartsprDto>();
		if (laenderartsprs != null) {
			Iterator<?> iterator = laenderartsprs.iterator();
			while (iterator.hasNext()) {
				list.add(createDto((Laenderartspr) iterator.next()));
			}
		}
		LaenderartsprDto[] returnArray = new LaenderartsprDto[list.size()];
		return (LaenderartsprDto[]) list.toArray(returnArray);
	}
}
