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
package com.lp.server.angebot.fastlanereader;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import com.lp.server.angebot.fastlanereader.generated.FLRAngebotposition;
import com.lp.server.util.PositionNumberAdapter;

public class FLRAngebotpositionNumberAdapter extends PositionNumberAdapter
		implements Serializable {

	private List<FLRAngebotposition> positions ;
	private FLRAngebotposition flrPosition ;

	public FLRAngebotpositionNumberAdapter(List<FLRAngebotposition> positions) {
		this.positions = positions ;
	}
	
	@Override
	public void setAdaptee(Object adaptee) {
		flrPosition = (FLRAngebotposition) adaptee ;
	}

	@Override
	public Integer getIId() {
		return flrPosition.getI_id() ;
	}

	@Override
	public Integer getPositionIId() {
		return flrPosition.getPosition_i_id() ;
	}

	@Override
	public String getPositionartCNr() {
		return flrPosition.getPositionart_c_nr() ;
	}

	@Override
	public String getCZbez() {
		return flrPosition.getC_zbez() ;
	}

	@Override
	public String getTypCNr() {
		return flrPosition.getTyp_c_nr() ;
	}

	@Override
	public Integer getAuftragPositionIId() {
		return null;
	}

	@Override
	public Integer getHeadIIdFromPosition(Integer posIId) {
		return null;
	}

	@Override
	public Integer getPositionIIdArtikelset() {
		return flrPosition.getPosition_i_id_artikelset() ;
	}

	@Override
	public Iterator<?> getPositionsIteratorForAnyPosition(Integer anyPosIId) {
		return positions.iterator() ;
	}

	@Override
	public Iterator<?> getPositionsIteratorForHeadIId(Integer headIId) {
		return positions.iterator() ;
	}
}
