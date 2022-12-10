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
package com.lp.server.eingangsrechnung.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

import com.lp.server.util.BelegZahlungAdapter;
import com.lp.util.Helper;

public class EingangsrechnungzahlungAdapter extends BelegZahlungAdapter implements
		Serializable {

	private static final long serialVersionUID = 1L;
	
	private EingangsrechnungzahlungDto erZahlungDto;
	private Boolean bErledigt;
	
	public EingangsrechnungzahlungAdapter() {
		erZahlungDto = new EingangsrechnungzahlungDto();
		bErledigt = false;
	}

	@Override
	public Integer getRechnungIId() {
		return erZahlungDto.getEingangsrechnungIId();
	}

	@Override
	public void setRechnungIId(Integer rechnungIId) {
		erZahlungDto.setEingangsrechnungIId(rechnungIId);
	}

	@Override
	public Date getDZahldatum() {
		return erZahlungDto.getTZahldatum();
	}

	@Override
	public void setDZahldatum(Date dZahldatum) {
		erZahlungDto.setTZahldatum(dZahldatum);
	}

	@Override
	public String getZahlungsartCNr() {
		return erZahlungDto.getZahlungsartCNr();
	}

	@Override
	public void setZahlungsartCNr(String zahlungsartCNr) {
		erZahlungDto.setZahlungsartCNr(zahlungsartCNr);
	}

	@Override
	public Integer getBankkontoIId() {
		return erZahlungDto.getBankverbindungIId();
	}

	@Override
	public void setBankkontoIId(Integer bankkontoIId) {
		erZahlungDto.setBankverbindungIId(bankkontoIId);
	}

	@Override
	public Integer getIAuszug() {
		return erZahlungDto.getIAuszug();
	}

	@Override
	public void setIAuszug(Integer iAuszug) {
		erZahlungDto.setIAuszug(iAuszug);
	}

	@Override
	public BigDecimal getNKurs() {
		return erZahlungDto.getNKurs();
	}

	@Override
	public void setNKurs(BigDecimal nKurs) {
		erZahlungDto.setNKurs(nKurs);
	}

	@Override
	public BigDecimal getNBetrag() {
		return erZahlungDto.getNBetrag();
	}

	@Override
	public void setNBetrag(BigDecimal nBetrag) {
		erZahlungDto.setNBetrag(nBetrag);
	}

	@Override
	public BigDecimal getNBetragfw() {
		return erZahlungDto.getNBetragfw();
	}

	@Override
	public void setNBetragfw(BigDecimal nBetragfw) {
		erZahlungDto.setNBetragfw(nBetragfw);
	}

	@Override
	public BigDecimal getNBetragUst() {
		return erZahlungDto.getNBetragust();
	}

	@Override
	public void setNBetragUst(BigDecimal nBetragUst) {
		erZahlungDto.setNBetragust(nBetragUst);
	}

	@Override
	public BigDecimal getNBetragUstfw() {
		return erZahlungDto.getNBetragustfw();
	}

	@Override
	public void setNBetragUstfw(BigDecimal nBetragUstfw) {
		erZahlungDto.setNBetragustfw(nBetragUstfw);
	}

	@Override
	public BigDecimal getBruttoBetrag() {
		return getNBetrag();
	}

	@Override
	public String getKommentar() {
		return erZahlungDto.getCKommentar();
	}

	@Override
	public void setKommentar(String cKommentar) {
		erZahlungDto.setCKommentar(cKommentar);
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
		return erZahlungDto;
	}

	@Override
	public Boolean getBKursuebersteuert() {
		return Helper.short2Boolean(erZahlungDto.getBKursuebersteuert());
	}

	@Override
	public void setBKursuebersteuert(Boolean bKursuebersteuert) {
		erZahlungDto.setBKursuebersteuert(Helper.boolean2Short(bKursuebersteuert));
	}

	@Override
	public boolean isEingangsrechnungzahlungAdapter() {
		return true;
	}

	@Override
	public boolean isRechnungzahlungAdapter() {
		return false;
	}

}
