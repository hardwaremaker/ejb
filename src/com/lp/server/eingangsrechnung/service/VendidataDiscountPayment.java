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
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.lp.server.schema.vendidata.discounts.XMLPeriodicType;

public class VendidataDiscountPayment implements Serializable {
	private static final long serialVersionUID = -5818530188595403712L;

	public enum Type {
		AUSGANGSGUTSCHRIFT,
		RECHNUNG;
	}
	private String kundennummer;
	private String sDebitorennummer;
	private String sBezeichnungKunde;
	//periodeninfo??
	private Date belegdatum;
	private List<VendidataPaymentInfo> positionen;
	private Type type;

	public VendidataDiscountPayment() {
	}

	public String getDebitorennummer() {
		return sDebitorennummer;
	}

	public void setDebitorennummer(String debitorennummer) {
		this.sDebitorennummer = debitorennummer;
	}

	public String getBezeichnungKunde() {
		return sBezeichnungKunde;
	}

	public void setBezeichnungKunde(String sBezeichnungKunde) {
		this.sBezeichnungKunde = sBezeichnungKunde;
	}

	public Date getBelegdatum() {
		return belegdatum;
	}

	public void setBelegdatum(Date belegdatum) {
		this.belegdatum = belegdatum;
	}

	public List<VendidataPaymentInfo> getPositionen() {
		if (positionen == null) {
			positionen = new ArrayList<VendidataPaymentInfo>();
		}
		return positionen;
	}

	public void setPositionen(List<VendidataPaymentInfo> positionen) {
		this.positionen = positionen;
	}

	public void addPosition(VendidataPaymentInfo pos) {
		getPositionen().add(pos);
	}

	public String getTextPeriodenInfo() {
		if (!getPositionen().isEmpty()) {
			VendidataPaymentInfo pos = getPositionen().get(0);
			if (pos.getPeriodenTyp() == null) return null;
			
			StringBuilder builder = new StringBuilder();
			builder.append(pos.getPeriodenJahr() + " ");
			if (pos.getPeriodenTyp().equals(XMLPeriodicType.MONTHLY.name())) {
				builder.append("Monat ");
				builder.append(pos.getPeriodenNummer() + " ");
			} else if (pos.getPeriodenTyp().equals(XMLPeriodicType.QUARTERLY.name())) {
				builder.append("Quartal ");
				builder.append(pos.getPeriodenNummer() + " ");
			} else if (pos.getPeriodenTyp().equals(XMLPeriodicType.BIANNUAL.name())) {
				builder.append("Halbjahr ");
				builder.append(pos.getPeriodenNummer() + " ");
			} 
			
			return builder.toString();
		}
		return null;
	}
	
	public Boolean isEmpty() {
		return getPositionen().isEmpty();
	}

	public void setKundennummer(String kundennummer) {
		this.kundennummer = kundennummer;
	}
	
	public String getKundennummer() {
		return kundennummer;
	}

	public void setType(Type type) {
		this.type = type;
	}
	
	public Type getType() {
		return type;
	}
	
	public boolean isAusgangsgutschrift() {
		return Type.AUSGANGSGUTSCHRIFT.equals(getType());
	}
	
	public boolean isRechnung() {
		return Type.RECHNUNG.equals(getType());
	}
	
	@Override
	public String toString() {
		return "DiscountPayment [" + getType() 
			+ ", kundennummer=" + getKundennummer() 
			+ ", belegdatum=" + getBelegdatum() 
			+ ", #positionen=" + getPositionen().size() 
			+ "]";
	}
	
}
