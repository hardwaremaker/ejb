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
package com.lp.server.angebotstkl.service;

import java.io.Serializable;

import com.lp.server.partner.service.LieferantDto;

public class WebpartnerDto extends WebBaseDto implements IWebpartnerDto, Serializable {

	private static final long serialVersionUID = -2281268180380822944L;

	private Integer iId;
	private Integer webabfrageIId;
	private Integer lieferantIId;
	private LieferantDto lieferantDto;

	public WebpartnerDto() {
		super();
		lieferantDto = new LieferantDto();
	}

	@Override
	public Integer getIId() {
		return iId;
	}

	@Override
	public void setIId(Integer iId) {
		this.iId = iId;
	}

	@Override
	public Integer getWebabfrageIId() {
		return webabfrageIId;
	}

	@Override
	public void setWebabfrageIId(Integer webabfrageIId) {
		this.webabfrageIId = webabfrageIId;
	}

	@Override
	public Integer getLieferantIId() {
		return lieferantIId;
	}

	@Override
	public void setLieferantIId(Integer lieferantIId) {
		this.lieferantIId = lieferantIId;
	}

	@Override
	public LieferantDto getLieferantDto() {
		return lieferantDto;
	}

	@Override
	public void setLieferantDto(LieferantDto lieferantDto) {
		this.lieferantDto = lieferantDto;
	}

	@Override
	public String getId() {
		return getIId() != null ? getIId().toString() : "";
	}

	@Override
	public String getName() {
		return getIId() != null ? getIId().toString() : "";
	}

}
