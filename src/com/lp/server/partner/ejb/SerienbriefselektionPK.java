/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2014 HELIUM V IT-Solutions GmbH
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
public class SerienbriefselektionPK implements Serializable {
	@Column(name = "SERIENBRIEF_I_ID", insertable = false, updatable = false)
	private Integer serienbriefIId;

	@Column(name = "SELEKTION_I_ID", insertable = false, updatable = false)
	private Integer selektionIId;

	private static final long serialVersionUID = 1L;

	public SerienbriefselektionPK(Integer serienbriefIId, Integer selektionIId) {
		setSerienbriefIId(serienbriefIId);
		setSelektionIId(selektionIId);
	}

	public SerienbriefselektionPK() {
		// TODO Auto-generated constructor stub
	}

	public Integer getSerienbriefIId() {
		return this.serienbriefIId;
	}

	public void setSerienbriefIId(Integer serienbriefIId) {
		this.serienbriefIId = serienbriefIId;
	}

	public Integer getSelektionIId() {
		return this.selektionIId;
	}

	public void setSelektionIId(Integer selektionIId) {
		this.selektionIId = selektionIId;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof SerienbriefselektionPK)) {
			return false;
		}
		SerienbriefselektionPK other = (SerienbriefselektionPK) o;
		return this.serienbriefIId.equals(other.serienbriefIId)
				&& this.selektionIId.equals(other.selektionIId);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.serienbriefIId.hashCode();
		hash = hash * prime + this.selektionIId.hashCode();
		return hash;
	}

}
