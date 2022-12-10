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
package com.lp.server.stueckliste.ejb;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class PruefkombinationsprPK implements Serializable {
	@Column(name = "LOCALE_C_NR", insertable = false, updatable = false)
	private String localeCNr;

	@Column(name = "PRUEFKOMBINATION_I_ID", insertable = false, updatable = false)
	private Integer pruefkombinationIId;

	public Integer getPruefkombinationIId() {
		return pruefkombinationIId;
	}

	public void setPruefkombinationIId(Integer pruefkombinationIId) {
		this.pruefkombinationIId = pruefkombinationIId;
	}

	private static final long serialVersionUID = 1L;

	public String getLocaleCNr() {
		return this.localeCNr;
	}

	public void setLocaleCNr(String localeCNr) {
		this.localeCNr = localeCNr;
	}

	public PruefkombinationsprPK() {

	}

	public PruefkombinationsprPK(String locUiAsString,
			Integer pruefkombinationIId) {
		setLocaleCNr(locUiAsString);
		setPruefkombinationIId(pruefkombinationIId);
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof PruefkombinationsprPK)) {
			return false;
		}
		PruefkombinationsprPK other = (PruefkombinationsprPK) o;
		return this.localeCNr.equals(other.localeCNr)
				&& this.pruefkombinationIId.equals(other.pruefkombinationIId);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.localeCNr.hashCode();
		hash = hash * prime + this.pruefkombinationIId.hashCode();
		return hash;
	}

}
