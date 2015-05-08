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
package com.lp.server.util;

import java.util.Iterator;

import org.apache.commons.collections.iterators.ArrayIterator;

import com.lp.server.rechnung.service.RechnungPositionDto;
import com.lp.service.BelegpositionVerkaufDto;

public class BelegPositionNumberDtoAdapter extends
		RechnungPositionNumberAdapter {
	/**
	 * 
	 */
	private static final long serialVersionUID = -422266210761012753L;

	private BelegpositionVerkaufDto[] belegPositionDtos ;
	private BelegpositionVerkaufDto belegPositionPos ;
	
	public BelegPositionNumberDtoAdapter(BelegpositionVerkaufDto[] dtos) {
		if(null == dtos) throw new IllegalArgumentException("dtos == null") ;
		belegPositionDtos = dtos ;
	}
	
	public BelegPositionNumberDtoAdapter(BelegpositionVerkaufDto dto) {
		this.belegPositionPos = dto ;
	}
	
	public void setAdaptee(Object adaptee) {
		this.belegPositionPos = (BelegpositionVerkaufDto) adaptee ;
	}
	
	@Override
	public Iterator<?> getPositionsIteratorForHeadIId(Integer rechnungIId) {
		return new ArrayIterator(belegPositionDtos) ;
	}
	
	@Override
	public Integer getPositionIId() {
		return belegPositionPos.getPositioniId() ;
	}

	@Override
	public String getPositionartCNr() {
		return belegPositionPos.getPositionsartCNr() ;
	}

	@Override
	public String getCZbez() {
		return belegPositionPos.getCZusatzbez() ;
	}

	@Override
	public String getTypCNr() {
		return belegPositionPos.getTypCNr() ;
	}

	@Override
	public Integer getHeadIIdFromPosition(Integer posIId) {
		for (BelegpositionVerkaufDto position : belegPositionDtos) {
			if(position.getIId().equals(posIId)) return position.getBelegIId() ;
		}
		
		return null ;
	}

	@Override
	public Integer getPositionIIdArtikelset() {
		return belegPositionPos.getPositioniIdArtikelset() ;
	}

	@Override
	public Integer getIId() {
		return belegPositionPos.getIId() ;
	}	

}
