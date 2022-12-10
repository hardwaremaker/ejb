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
package com.lp.server.system.ejb;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name="LP_VERSANDWEGPARTNERLINIENABRUF")
@PrimaryKeyJoinColumn(name="I_ID")
public class VersandwegPartnerLinienabruf extends VersandwegPartner {
	private static final long serialVersionUID = -6658648217476789169L;

	@Column(name="C_EXPORTPFAD")
	private String cExportPfad;

	@Column(name="C_KOPFZEILE")
	private String cKopfzeile;

	@Column(name="C_DATENZEILE")
	private String cDatenzeile;

	@Column(name="C_BESTELLNUMMER")
	private String cBestellnummer;

	public String getCExportPfad() {
		return cExportPfad;
	}

	public void setCExportPfad(String cExportPfad) {
		this.cExportPfad = cExportPfad;
	}

	public String getCKopfzeile() {
		return cKopfzeile;
	}

	public void setCKopfzeile(String cKopfzeile) {
		this.cKopfzeile = cKopfzeile;
	}

	public String getCDatenzeile() {
		return cDatenzeile;
	}

	public void setCDatenzeile(String cDatenzeile) {
		this.cDatenzeile = cDatenzeile;
	}

	public String getCBestellnummer() {
		return cBestellnummer;
	}

	public void setCBestellnummer(String cBestellnummer) {
		this.cBestellnummer = cBestellnummer;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((cDatenzeile == null) ? 0 : cDatenzeile.hashCode());
		result = prime * result
				+ ((cExportPfad == null) ? 0 : cExportPfad.hashCode());
		result = prime * result
				+ ((cKopfzeile == null) ? 0 : cKopfzeile.hashCode());
		result = prime * result
				+ ((cBestellnummer == null) ? 0 : cBestellnummer.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		VersandwegPartnerLinienabruf other = (VersandwegPartnerLinienabruf) obj;
		if (cDatenzeile == null) {
			if (other.cDatenzeile != null)
				return false;
		} else if (!cDatenzeile.equals(other.cDatenzeile))
			return false;
		if (cExportPfad == null) {
			if (other.cExportPfad != null)
				return false;
		} else if (!cExportPfad.equals(other.cExportPfad))
			return false;
		if (cKopfzeile == null) {
			if (other.cKopfzeile != null)
				return false;
		} else if (!cKopfzeile.equals(other.cKopfzeile))
			return false;
		if (cBestellnummer == null) {
			if (other.cBestellnummer != null)
				return false;
		} else if (!cBestellnummer.equals(other.cBestellnummer))
			return false;
		return true;
	}
}
