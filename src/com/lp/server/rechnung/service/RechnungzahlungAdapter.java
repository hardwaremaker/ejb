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
package com.lp.server.rechnung.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

import com.lp.server.util.BelegZahlungAdapter;

public class RechnungzahlungAdapter extends BelegZahlungAdapter implements
		Serializable {

	private static final long serialVersionUID = 1L;
	
	private RechnungzahlungDto reZahlungDto;
	private Boolean bErledigt;
	
	public RechnungzahlungAdapter() {
		reZahlungDto = new RechnungzahlungDto();
		bErledigt = false;
	}

	@Override
	public Integer getRechnungIId() {
		return reZahlungDto.getRechnungIId();
	}

	@Override
	public void setRechnungIId(Integer rechnungIId) {
		reZahlungDto.setRechnungIId(rechnungIId);
	}

	@Override
	public Date getDZahldatum() {
		return reZahlungDto.getDZahldatum();
	}

	@Override
	public void setDZahldatum(Date dZahldatum) {
		reZahlungDto.setDZahldatum(dZahldatum);
	}

	@Override
	public String getZahlungsartCNr() {
		return reZahlungDto.getZahlungsartCNr();
	}

	@Override
	public void setZahlungsartCNr(String zahlungsartCNr) {
		reZahlungDto.setZahlungsartCNr(zahlungsartCNr);
	}

	@Override
	public Integer getBankkontoIId() {
		return reZahlungDto.getBankkontoIId();
	}

	@Override
	public void setBankkontoIId(Integer bankkontoIId) {
		reZahlungDto.setBankkontoIId(bankkontoIId);
	}

	@Override
	public Integer getIAuszug() {
		return reZahlungDto.getIAuszug();
	}

	@Override
	public void setIAuszug(Integer iAuszug) {
		reZahlungDto.setIAuszug(iAuszug);
	}

	@Override
	public BigDecimal getNKurs() {
		return reZahlungDto.getNKurs();
	}

	@Override
	public void setNKurs(BigDecimal nKurs) {
		reZahlungDto.setNKurs(nKurs);
	}

	@Override
	public BigDecimal getNBetrag() {
		return reZahlungDto.getNBetrag();
	}

	@Override
	public void setNBetrag(BigDecimal nBetrag) {
		reZahlungDto.setNBetrag(nBetrag);
	}

	@Override
	public BigDecimal getNBetragfw() {
		return reZahlungDto.getNBetragfw();
	}

	@Override
	public void setNBetragfw(BigDecimal nBetragfw) {
		reZahlungDto.setNBetragfw(nBetragfw);
	}

	@Override
	public BigDecimal getNBetragUst() {
		return reZahlungDto.getNBetragUst();
	}

	@Override
	public void setNBetragUst(BigDecimal nBetragUst) {
		reZahlungDto.setNBetragUst(nBetragUst);
	}

	@Override
	public BigDecimal getNBetragUstfw() {
		return reZahlungDto.getNBetragUstfw();
	}

	@Override
	public void setNBetragUstfw(BigDecimal nBetragUstfw) {
		reZahlungDto.setNBetragUstfw(nBetragUstfw);
	}

	@Override
	public BigDecimal getBruttoBetrag() {
		return getNBetrag().add(getNBetragUst());
	}

	@Override
	public String getKommentar() {
		return reZahlungDto.getCKommentar();
	}

	@Override
	public void setKommentar(String cKommentar) {
		reZahlungDto.setCKommentar(cKommentar);
	}

	@Override
	public Boolean isBErledigt() {
		return bErledigt;
	}

	@Override
	public void setBErledigt(Boolean bErledigt) {
		this.bErledigt = bErledigt;
	}

	@Override
	public Object getRawBelegZahlungDto() {
		return reZahlungDto;
	}

	@Override
	public Boolean getBKursuebersteuert() {
		return null;
	}

	@Override
	public void setBKursuebersteuert(Boolean bKursuebersteuert) {
		
	}

	@Override
	public boolean isEingangsrechnungzahlungAdapter() {
		return false;
	}

	@Override
	public boolean isRechnungzahlungAdapter() {
		return true;
	}

}
