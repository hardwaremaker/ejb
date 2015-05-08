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
package com.lp.server.instandhaltung.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.service.BelegpositionDto;

public class WartungslisteDto extends BelegpositionDto implements 
		Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArtikelDto artikelDto;
	public ArtikelDto getArtikelDto() {
		return artikelDto;
	}

	public void setArtikelDto(ArtikelDto artikelDto) {
		this.artikelDto = artikelDto;
	}

	private Integer iId;

	private BigDecimal nMenge;
	private Short bWartungsmaterial;
	private Short bVerrechenbar;
	private String cVeraltet;
	private String xBemerkung;
	private Integer iSort;
	private Timestamp tPersonalVeraltet;
	private Timestamp tVeraltet;
	private String cBez;
	private Integer personalIIdVeraltet;
	private Integer geraetIId;
	private Integer artikelIId;



	public Short getBWartungsmaterial() {
		return bWartungsmaterial;
	}

	public void setBWartungsmaterial(Short bWartungsmaterial) {
		this.bWartungsmaterial = bWartungsmaterial;
	}

	public Short getBVerrechenbar() {
		return bVerrechenbar;
	}

	public void setBVerrechenbar(Short bVerrechenbar) {
		this.bVerrechenbar = bVerrechenbar;
	}

	public String getCVeraltet() {
		return cVeraltet;
	}

	public void setCVeraltet(String cVeraltet) {
		this.cVeraltet = cVeraltet;
	}

	public String getXBemerkung() {
		return xBemerkung;
	}

	public void setXBemerkung(String xBemerkung) {
		this.xBemerkung = xBemerkung;
	}

	public Timestamp getTPersonalVeraltet() {
		return tPersonalVeraltet;
	}

	public void setTPersonalVeraltet(Timestamp tPersonalVeraltet) {
		this.tPersonalVeraltet = tPersonalVeraltet;
	}

	public Timestamp getTVeraltet() {
		return tVeraltet;
	}

	public void settVeraltet(Timestamp tVeraltet) {
		this.tVeraltet = tVeraltet;
	}


	public Integer getPersonalIIdVeraltet() {
		return personalIIdVeraltet;
	}

	public void setPersonalIIdVeraltet(Integer personalIIdVeraltet) {
		this.personalIIdVeraltet = personalIIdVeraltet;
	}

	public Integer getGeraetIId() {
		return this.geraetIId;
	}

	public void setGeraetIId(Integer geraetIId) {
		this.geraetIId = geraetIId;
	}

}
