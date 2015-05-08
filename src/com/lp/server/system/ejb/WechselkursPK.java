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
package com.lp.server.system.ejb;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class WechselkursPK implements Serializable {
	@Column(name = "WAEHRUNG_C_NR_VON", insertable = false, updatable = false)
	private String waehrungCNrVon;

	@Column(name = "WAEHRUNG_C_NR_ZU", insertable = false, updatable = false)
	private String waehrungCNrZu;

	@Column(name = "T_DATUM")
	private Date tDatum;

	private static final long serialVersionUID = 1L;

	public WechselkursPK() {
		super();
	}

	public WechselkursPK(String waehrungLocaleCNrVon,
			String waehrungLocaleCNrZu, java.sql.Date datum) {
		setWaehrungCNrVon(waehrungLocaleCNrVon);
		setWaehrungCNrZu(waehrungLocaleCNrZu);
		setTDatum(datum);
	}

	public String getWaehrungCNrVon() {
		return this.waehrungCNrVon;
	}

	public void setWaehrungCNrVon(String waehrungCNrVon2) {
		this.waehrungCNrVon = waehrungCNrVon2;
	}

	public String getWaehrungCNrZu() {
		return this.waehrungCNrZu;
	}

	public void setWaehrungCNrZu(String waehrungCNrZu2) {
		this.waehrungCNrZu = waehrungCNrZu2;
	}

	public java.sql.Date getTDatum() {
		return this.tDatum;
	}

	public void setTDatum(Date tDatum) {
		this.tDatum = tDatum;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof WechselkursPK)) {
			return false;
		}
		WechselkursPK other = (WechselkursPK) o;
		return this.waehrungCNrVon.equals(other.waehrungCNrVon)
				&& this.waehrungCNrZu.equals(other.waehrungCNrZu)
				&& this.tDatum.equals(other.tDatum);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.waehrungCNrVon.hashCode();
		hash = hash * prime + this.waehrungCNrZu.hashCode();
		hash = hash * prime + this.tDatum.hashCode();
		return hash;
	}

}
