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
package com.lp.server.system.ejb;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries( { @NamedQuery(name = "TheJudgefindByWerWas", query = "SELECT OBJECT (o) FROM Thejudge o WHERE o.pk.cWer=?1 AND o.pk.cWas=?2") })
@Entity
@Table(name = "LP_THEJUDGE")
public class Thejudge implements Serializable {
	@EmbeddedId
	private TheJudgePK pk;

	@Column(name = "T_WANN")
	private Timestamp tWann;

	@Column(name = "PERSONAL_I_ID_LOCKER")
	private Integer personalIIdLocker;

	private static final long serialVersionUID = 1L;

	public Thejudge() {
		super();
	}

	public Thejudge(String wer, String was, String usernr,
			Integer personalIIdLocker
			) {
		TheJudgePK pk = new TheJudgePK(wer, was, usernr);
		setTWann(new Timestamp(System.currentTimeMillis()));
		setPersonalIIdLocker(personalIIdLocker);
		setPk(pk);
	}

	public TheJudgePK getPk() {
		return this.pk;
	}

	public void setPk(TheJudgePK pk) {
		this.pk = pk;
	}

	public Timestamp getTWann() {
		return this.tWann;
	}

	public void setTWann(Timestamp tWann) {
		this.tWann = tWann;
	}

	public Integer getPersonalIIdLocker() {
		return this.personalIIdLocker;
	}

	public void setPersonalIIdLocker(Integer personalIIdLocker) {
		this.personalIIdLocker = personalIIdLocker;
	}

}
