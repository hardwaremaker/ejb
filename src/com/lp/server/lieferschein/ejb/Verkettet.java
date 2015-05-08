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
package com.lp.server.lieferschein.ejb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
		@NamedQuery(name = "VerkettetfindByLieferscheinIIdLieferscheinIIdVerkettet", query = "SELECT OBJECT (o) FROM Verkettet o WHERE o.lieferscheinIId=?1 AND o.lieferscheinIIdVerkettet=?2"),
		@NamedQuery(name = "VerkettetfindByLieferscheinIIdVerkettet", query = "SELECT OBJECT (o) FROM Verkettet o WHERE o.lieferscheinIIdVerkettet=?1"),
		@NamedQuery(name = "VerkettetfindByLieferscheinIId", query = "SELECT OBJECT (o) FROM Verkettet o WHERE o.lieferscheinIId=?1 ORDER BY o.lieferscheinIId ") })
@Entity
@Table(name = "LS_VERKETTET")
public class Verkettet implements Serializable {

	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "LIEFERSCHEIN_I_ID")
	private Integer lieferscheinIId;

	@Column(name = "LIEFERSCHEIN_I_ID_VERKETTET")
	private Integer lieferscheinIIdVerkettet;

	private static final long serialVersionUID = 1L;

	public Verkettet() {
		super();
	}

	public Verkettet(Integer id, Integer lieferscheinIId,
			Integer lieferscheinIIdVerkettet) {
		setIId(id);
		setLieferscheinIId(lieferscheinIId);
		setLieferscheinIIdVerkettet(lieferscheinIIdVerkettet);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getLieferscheinIId() {
		return this.lieferscheinIId;
	}

	public void setLieferscheinIId(Integer lieferscheinIId) {
		this.lieferscheinIId = lieferscheinIId;
	}

	public Integer getLieferscheinIIdVerkettet() {
		return lieferscheinIIdVerkettet;
	}

	public void setLieferscheinIIdVerkettet(Integer lieferscheinIIdVerkettet) {
		this.lieferscheinIIdVerkettet = lieferscheinIIdVerkettet;
	}

}
