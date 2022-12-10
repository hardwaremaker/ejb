/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2016 HELIUM V IT-Solutions GmbH
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

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.lp.server.util.IIId;
import com.lp.server.util.IModificationData;

public class SepakontoauszugDto implements Serializable, IModificationData, IIId {

	private static final long serialVersionUID = 2536450806181676920L;

	private Integer iId;
	private Integer bankverbindungIId;
	private Integer iAuszug;
	private Timestamp tAuszug;
	private BigDecimal nAnfangssaldo;
	private BigDecimal nEndsaldo;
	private SepaKontoauszugVersionEnum enumVersion;
	private String cCamtFormat;
	private SepaKontoauszug kontoauszug;
	private Integer personalIIdAendern;
	private Integer personalIIdAnlegen;
	private Timestamp tAnlegen;
	private Timestamp tAendern;
	private Integer personalIIdVerbuchen;
	private Timestamp tVerbuchen;
	private String statusCNr;

	public SepakontoauszugDto() {
		super();
	}

	public Integer getIAuszug() {
		return iAuszug;
	}

	public void setIAuszug(Integer iAuszug) {
		this.iAuszug = iAuszug;
	}

	public BigDecimal getNAnfangssaldo() {
		return nAnfangssaldo;
	}

	public void setNAnfangssaldo(BigDecimal nAnfangssaldo) {
		this.nAnfangssaldo = nAnfangssaldo;
	}

	public BigDecimal getNEndsaldo() {
		return nEndsaldo;
	}

	public void setNEndsaldo(BigDecimal nEndsaldo) {
		this.nEndsaldo = nEndsaldo;
	}

	public SepaKontoauszugVersionEnum getVersion() {
		return enumVersion;
	}

	public void setVersion(SepaKontoauszugVersionEnum enumVersion) {
		this.enumVersion = enumVersion;
	}

	public String getCCamtFormat() {
		return cCamtFormat;
	}

	public void setCCamtFormat(String cCamtFormat) {
		this.cCamtFormat = cCamtFormat;
	}

	public SepaKontoauszug getKontoauszug() {
		return kontoauszug;
	}

	public void setOKontoauszug(SepaKontoauszug kontoauszug) {
		this.kontoauszug = kontoauszug;
	}

	@Override
	public Timestamp getTAnlegen() {
		return tAnlegen;
	}

	@Override
	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	@Override
	public Integer getPersonalIIdAnlegen() {
		return personalIIdAnlegen;
	}

	@Override
	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}

	@Override
	public Timestamp getTAendern() {
		return tAendern;
	}

	@Override
	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	@Override
	public Integer getPersonalIIdAendern() {
		return personalIIdAendern;
	}

	@Override
	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	@Override
	public Integer getIId() {
		return iId;
	}

	@Override
	public void setIId(Integer newIId) {
		this.iId = newIId;
	}

	public Integer getPersonalIIdVerbuchen() {
		return personalIIdVerbuchen;
	}

	public void setPersonalIIdVerbuchen(Integer personalIIdVerbuchen) {
		this.personalIIdVerbuchen = personalIIdVerbuchen;
	}

	public Timestamp getTVerbuchen() {
		return tVerbuchen;
	}

	public void setTVerbuchen(Timestamp tVerbuchen) {
		this.tVerbuchen = tVerbuchen;
	}

	public Integer getBankverbindungIId() {
		return bankverbindungIId;
	}

	public void setBankverbindungIId(Integer bankverbindungIId) {
		this.bankverbindungIId = bankverbindungIId;
	}

	public Timestamp getTAuszug() {
		return tAuszug;
	}

	public void setTAuszug(Timestamp tAuszug) {
		this.tAuszug = tAuszug;
	}

	public String getStatusCNr() {
		return statusCNr;
	}

	public void setStatusCNr(String statusCNr) {
		this.statusCNr = statusCNr;
	}

}
