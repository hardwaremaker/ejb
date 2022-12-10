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
package com.lp.util.report;

import java.math.BigDecimal;

import com.lp.server.system.service.MwstsatzDto;

public class UvaRpt implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private BigDecimal saldo;
	private String kennzeichen;
	private BigDecimal saldogerundet;
	private MwstsatzDto mwstsatzDto;
	private int kontoUvavariante;
	private Integer sort;
	private Integer gruppe;
	private boolean alternativ;
	private String uvaartCnr;
	private BigDecimal anzahlungsSaldo;
	private BigDecimal anzahlungsSaldoGerundet;
	
	public UvaRpt(String kennzeichenI, BigDecimal saldoI, BigDecimal saldogerundetI) {
		kennzeichen = kennzeichenI;
		saldo = saldoI;
		saldogerundet = saldogerundetI;
	}
	
	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}
	public BigDecimal getSaldo() {
		return saldo;
	}
	
	public void setKennzeichen(String kennzeichen) {
		this.kennzeichen = kennzeichen;
	}
	public String getKennzeichen() {
		return kennzeichen;
	}

	public void setSaldogerundet(BigDecimal saldogerundet) {
		this.saldogerundet = saldogerundet;
	}
	public BigDecimal getSaldogerundet() {
		return saldogerundet;
	}

	public MwstsatzDto getMwstsatzDto() {
		return mwstsatzDto;
	}

	public void setMwstsatzDto(MwstsatzDto mwstsatzDto) {
		this.mwstsatzDto = mwstsatzDto;
	}
	
	public Double getSteuersatz() {
		return getMwstsatzDto() == null ? null : getMwstsatzDto().getFMwstsatz();
	}

	public int getKontoUvavariante() {
		return kontoUvavariante;
	}

	public void setKontoUvavariante(int kontoUvavariante) {
		this.kontoUvavariante = kontoUvavariante;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getGruppe() {
		return gruppe;
	}

	public void setGruppe(Integer gruppe) {
		this.gruppe = gruppe;
	}

	public boolean isAlternativ() {
		return alternativ;
	}

	public void setAlternativ(boolean alternativ) {
		this.alternativ = alternativ;
	}

	public String getUvaartCnr() {
		return uvaartCnr;
	}

	public void setUvaartCnr(String uvaartCnr) {
		this.uvaartCnr = uvaartCnr;
	}

	public BigDecimal getAnzahlungsSaldo() {
		return anzahlungsSaldo;
	}

	public void setAnzahlungsSaldo(BigDecimal anzahlungsSaldo) {
		this.anzahlungsSaldo = anzahlungsSaldo;
	}

	public BigDecimal getAnzahlungsSaldoGerundet() {
		return anzahlungsSaldoGerundet;
	}

	public void setAnzahlungsSaldoGerundet(BigDecimal anzahlungsSaldoGerundet) {
		this.anzahlungsSaldoGerundet = anzahlungsSaldoGerundet;
	}
}
