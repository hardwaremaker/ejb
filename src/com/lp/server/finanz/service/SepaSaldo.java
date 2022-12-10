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
package com.lp.server.finanz.service;

import java.io.Serializable;
import java.sql.Date;

public class SepaSaldo implements Serializable {
	
	private static final long serialVersionUID = 8711773154061610967L;

	private String waehrung;
	private SepaBetrag betrag;
	private Date datum;
	private SepaSaldoCodeEnum saldocode;

	public SepaSaldo() {
		super();
	}

	public String getWaehrung() {
		return waehrung;
	}

	public void setWaehrung(String waehrung) {
		this.waehrung = waehrung;
	}

	public SepaBetrag getBetrag() {
		return betrag;
	}

	public void setBetrag(SepaBetrag betrag) {
		this.betrag = betrag;
	}

	public Boolean isSoll() {
		return betrag != null ? betrag.isSoll() : false;
	}

	public Boolean isHaben() {
		return betrag != null ? betrag.isHaben() : false;
	}

	public Date getDatum() {
		return datum;
	}

	public void setDatum(Date datum) {
		this.datum = datum;
	}

	public SepaSaldoCodeEnum getSaldocode() {
		return saldocode;
	}

	public void setSaldocode(SepaSaldoCodeEnum saldocode) {
		this.saldocode = saldocode;
	}

	@Override
	public String toString() {
		return "SepaSaldo [\n\twaehrung=" + waehrung + ", \n\tbetrag=" + betrag
				+ ", \n\tistSoll=" + isSoll() + ", \n\tistHaben=" + isHaben() + ", \n\tdatum=" + datum 
				+ ", \n\tsaldocode=" + saldocode + "]";
	}
	
}
