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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name="LP_VERSANDWEGPARTNEREDI4ALL")
@PrimaryKeyJoinColumn(name="I_ID")
public class VersandwegPartnerEdi4All extends VersandwegPartner {
	private static final long serialVersionUID = -6658648217476789169L;

	@Column(name="C_EXPORTPFAD")
	private String cExportPfad;

	@Column(name="C_00E")
	private String c00E;

	@Column(name="C_094")
	private String c094;

	public String getCExportPfad() {
		return cExportPfad;
	}

	public void setCExportPfad(String cExportPfad) {
		this.cExportPfad = cExportPfad;
	}

	public String getC00E() {
		return c00E;
	}
	
	public void setC00E(String c00E) {
		this.c00E = c00E;
	}
	
	public String getC094() {
		return c094;
	}
	public void setC094(String c094) {
		this.c094 = c094;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((c00E == null) ? 0 : c00E.hashCode());
		result = prime * result + ((c094 == null) ? 0 : c094.hashCode());
		result = prime * result + ((cExportPfad == null) ? 0 : cExportPfad.hashCode());
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
		VersandwegPartnerEdi4All other = (VersandwegPartnerEdi4All) obj;
		if (c00E == null) {
			if (other.c00E != null)
				return false;
		} else if (!c00E.equals(other.c00E))
			return false;
		if (c094 == null) {
			if (other.c094 != null)
				return false;
		} else if (!c094.equals(other.c094))
			return false;
		
		if (cExportPfad == null) {
			if (other.cExportPfad != null)
				return false;
		} else if (!cExportPfad.equals(other.cExportPfad))
			return false;
		return true;
	}
}
