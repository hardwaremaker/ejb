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

import java.io.Serializable;
import java.math.BigDecimal;

public class ReportLosnachkalkulationDto implements Serializable {
	private String sArtikelnummer = null;
	private String sBezeichnung = null;
	private BigDecimal bdSollmenge = null;
	private BigDecimal bdIstmenge = null;
	private BigDecimal bdIstmengeMaschine = null;
	private BigDecimal bdSollpreis = null;
	private BigDecimal bdIstpreis = null;
	private BigDecimal bdSollmengeMaschine;
	private Boolean bFertig=null;
	private Integer iHoechsterArbeitsgang=null;

	public Integer getIHoechsterArbeitsgang() {
		return iHoechsterArbeitsgang;
	}

	public void setIHoechsterArbeitsgang(Integer iHoechsterArbeitsgang) {
		this.iHoechsterArbeitsgang = iHoechsterArbeitsgang;
	}

	public Boolean getBFertig() {
		return bFertig;
	}

	public void setBFertig(Boolean bFertig) {
		this.bFertig = bFertig;
	}

	public ReportLosnachkalkulationDto() {
		bdSollmenge = new BigDecimal(0);
		bdSollmengeMaschine = new BigDecimal(0);
		bdIstmenge = new BigDecimal(0);
		bdIstmengeMaschine = new BigDecimal(0);
		bdSollpreis = new BigDecimal(0);
		bdIstpreis = new BigDecimal(0);
	}

	public String getSArtikelnummer() {
		return sArtikelnummer;
	}

	public void setSArtikelnummer(String artikelnummer) {
		sArtikelnummer = artikelnummer;
	}

	public BigDecimal getBdIstmenge() {
		return bdIstmenge;
	}

	public BigDecimal getBdIstmengeMaschine() {
		return bdIstmengeMaschine;
	}

	public BigDecimal getBdIstpreis() {
		return bdIstpreis;
	}

	public BigDecimal getBdSollmenge() {
		return bdSollmenge;
	}

	public BigDecimal getBdSollpreis() {
		return bdSollpreis;
	}

	public String getSBezeichnung() {
		return sBezeichnung;
	}

	public BigDecimal getBdSollmengeMaschine() {
		return bdSollmengeMaschine;
	}

	public void setSBezeichnung(String sBezeichnung) {
		this.sBezeichnung = sBezeichnung;
	}

	public void setBdSollmengeMaschine(BigDecimal bdSollmengeMaschine) {
		this.bdSollmengeMaschine = bdSollmengeMaschine;
	}

	public void addiereZuIstmenge(BigDecimal bd2Add) {
		bdIstmenge = bdIstmenge.add(bd2Add);
	}

	public void addiereZuIstmengeMaschine(BigDecimal bd2Add) {
		bdIstmengeMaschine = bdIstmengeMaschine.add(bd2Add);
	}

	public void addiereZuSollmenge(BigDecimal bd2Add) {
		bdSollmenge = bdSollmenge.add(bd2Add);
	}

	public void addiereZuSollmengeMaschine(BigDecimal bd2Add) {
		bdSollmengeMaschine = bdSollmengeMaschine.add(bd2Add);
	}

	public void addiereZuIstpreis(BigDecimal bd2Add) {
		bdIstpreis = bdIstpreis.add(bd2Add);
	}

	public void addiereZuSollpreis(BigDecimal bd2Add) {
		bdSollpreis = bdSollpreis.add(bd2Add);
	}
}
