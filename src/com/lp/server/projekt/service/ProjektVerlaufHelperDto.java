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
package com.lp.server.projekt.service;

import java.io.Serializable;
import java.util.ArrayList;

import com.lp.server.personal.service.TelefonzeitenDto;
import com.lp.service.BelegpositionDto;

public class ProjektVerlaufHelperDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer iEbene;
	
	private Integer partnerIId;

	public Integer getPartnerIId() {
		return partnerIId;
	}

	public void setPartnerIId(Integer partnerIId) {
		this.partnerIId = partnerIId;
	}

	private ArrayList<BelegpositionDto> belegpositionenDtos;

	public ArrayList<BelegpositionDto> getBelegpositionenDtos() {
		return belegpositionenDtos;
	}

	public void setBelegpositionenDtos(
			ArrayList<BelegpositionDto> belegpositionenDtos) {
		this.belegpositionenDtos = belegpositionenDtos;
	}

	public void setBelegpositionenDtos(BelegpositionDto[] belegpositionenDtos) {
		this.belegpositionenDtos = new ArrayList<BelegpositionDto>();

		if (belegpositionenDtos != null) {
			for (int i = 0; i < belegpositionenDtos.length; i++) {
				this.belegpositionenDtos.add(belegpositionenDtos[i]);
			}
		}

	}

	public ProjektVerlaufHelperDto(Integer iEbene, Object oBelegDto) {
		this.iEbene = iEbene;
		this.oBelegDto = oBelegDto;
	}

	public Integer getiEbene() {
		return iEbene;
	}

	public void setiEbene(Integer iEbene) {
		this.iEbene = iEbene;
	}

	public ArrayList<TelefonzeitenDto> telefonzeitenZuBeleg;
	
	
	public ArrayList<TelefonzeitenDto> getTelefonzeitenZuBeleg() {
		return telefonzeitenZuBeleg;
	}

	public void setTelefonzeitenZuBeleg(ArrayList<TelefonzeitenDto> telefonzeitenZuBeleg) {
		this.telefonzeitenZuBeleg = telefonzeitenZuBeleg;
	}

	public Object getBelegDto() {
		return oBelegDto;
	}

	public void setBelegDto(Object oBelegDto) {
		this.oBelegDto = oBelegDto;
	}

	private Object oBelegDto = null;

}
