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
package com.lp.server.partner.ejb;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class LflfliefergruppePK implements Serializable {
	public LflfliefergruppePK() {
		// TODO Auto-generated constructor stub
	}

	@Column(name = "LFLIEFERGRUPPE_I_ID", insertable = false, updatable = false)
	private Integer lfliefergruppeIId;

	@Column(name = "LIEFERANT_I_ID", insertable = false, updatable = false)
	private Integer lieferantIId;

	private static final long serialVersionUID = 1L;

	public LflfliefergruppePK(Integer lieferantIId, Integer lfliefergruppeIId) {
		setLieferantIId(lieferantIId);
		setLfliefergruppeIId(lfliefergruppeIId);

	}

	public Integer getLfliefergruppeIId() {
		return this.lfliefergruppeIId;
	}

	public void setLfliefergruppeIId(Integer lfliefergruppeIId) {
		this.lfliefergruppeIId = lfliefergruppeIId;
	}

	public Integer getLieferantIId() {
		return this.lieferantIId;
	}

	public void setLieferantIId(Integer lieferantIId) {
		this.lieferantIId = lieferantIId;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof LflfliefergruppePK)) {
			return false;
		}
		LflfliefergruppePK other = (LflfliefergruppePK) o;
		return this.lfliefergruppeIId.equals(other.lfliefergruppeIId)
				&& this.lieferantIId.equals(other.lieferantIId);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.lfliefergruppeIId.hashCode();
		hash = hash * prime + this.lieferantIId.hashCode();
		return hash;
	}

}
