
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
package com.lp.server.reklamation.ejb;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class SchweresprPK implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1048081028846741608L;

	@Column(name = "LOCALE_C_NR", insertable = false, updatable = false)
	private String localeCNr;

	@Column(name = "SCHWERE_I_ID", insertable = false, updatable = false)
	private Integer schwereIId;


	public SchweresprPK() {
		super();
	}

	public SchweresprPK(String locUiAsString, Integer id) {
		setLocaleCNr(locUiAsString);
		setSchwereIId(id);
	}

	public Integer getSchwereIId() {
		return schwereIId;
	}

	public void setSchwereIId(Integer schwereIId) {
		this.schwereIId = schwereIId;
	}

	public String getLocaleCNr() {
		return this.localeCNr;
	}

	public void setLocaleCNr(String localeCNr) {
		this.localeCNr = localeCNr;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((schwereIId == null) ? 0 : schwereIId.hashCode());
		result = prime * result
				+ ((localeCNr == null) ? 0 : localeCNr.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SchweresprPK other = (SchweresprPK) obj;
		if (schwereIId == null) {
			if (other.schwereIId != null)
				return false;
		} else if (!schwereIId.equals(other.schwereIId))
			return false;
		if (localeCNr == null) {
			if (other.localeCNr != null)
				return false;
		} else if (!localeCNr.equals(other.localeCNr))
			return false;
		return true;
	}

}
