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
package com.lp.server.artikel.ejb;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
		@NamedQuery(name = "VerschleissteilwerkzeugfindByVerschleissteilIIdWerkzeugIId", query = "SELECT OBJECT(o) FROM Verschleissteilwerkzeug o WHERE o.verschleissteilIId=?1 AND o.werkzeugIId=?2"),
		@NamedQuery(name = "VerschleissteilwerkzeugfindByVerschleissteilIId", query = "SELECT OBJECT(o) FROM Verschleissteilwerkzeug o WHERE o.verschleissteilIId=?1") })
@Entity
@Table(name = "WW_VERSCHLEISSTEILWERKZEUG")
public class Verschleissteilwerkzeug implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "VERSCHLEISSTEIL_I_ID")
	private Integer verschleissteilIId;

	public Integer getVerschleissteilIId() {
		return verschleissteilIId;
	}

	public void setVerschleissteilIId(Integer verschleissteilIId) {
		this.verschleissteilIId = verschleissteilIId;
	}

	public Integer getWerkzeugIId() {
		return werkzeugIId;
	}

	public void setWerkzeugIId(Integer werkzeugIId) {
		this.werkzeugIId = werkzeugIId;
	}

	@Column(name = "WERKZEUG_I_ID")
	private Integer werkzeugIId;

	private static final long serialVersionUID = 1L;

	public Verschleissteilwerkzeug() {
		super();
	}

	public Verschleissteilwerkzeug(Integer id, Integer verschleissteilIId,
			Integer werkzeugIId) {
		setIId(id);
		setWerkzeugIId(werkzeugIId);
		setVerschleissteilIId(verschleissteilIId);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

}
