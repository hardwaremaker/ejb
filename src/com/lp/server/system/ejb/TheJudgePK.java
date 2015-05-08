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

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class TheJudgePK implements Serializable {
	@Column(name = "C_WER")
	private String cWer;

	@Column(name = "C_WAS")
	private String cWas;

	@Column(name = "C_USERNR")
	private String cUsernr;

	private static final long serialVersionUID = 1L;

	public TheJudgePK() {
		super();
	}

	public TheJudgePK(String wer, String was, String usernr) {
		setCWer(wer);
		setCWas(was);
		setCUsernr(usernr);
	}

	public String getCWer() {
		return this.cWer;
	}

	public void setCWer(String cWer) {
		this.cWer = cWer;
	}

	public String getCWas() {
		return this.cWas;
	}

	public void setCWas(String cWas) {
		this.cWas = cWas;
	}

	public String getCUsernr() {
		return this.cUsernr;
	}

	public void setCUsernr(String cUsernr) {
		this.cUsernr = cUsernr;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof TheJudgePK)) {
			return false;
		}
		TheJudgePK other = (TheJudgePK) o;
		return this.cWer.equals(other.cWer) && this.cWas.equals(other.cWas)
				&& this.cUsernr.equals(other.cUsernr);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.cWer.hashCode();
		hash = hash * prime + this.cWas.hashCode();
		hash = hash * prime + this.cUsernr.hashCode();
		return hash;
	}

}
