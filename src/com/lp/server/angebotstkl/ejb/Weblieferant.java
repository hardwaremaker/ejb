
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
package com.lp.server.angebotstkl.ejb;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.lp.server.angebotstkl.service.IWeblieferant;

@NamedQueries({
		@NamedQuery(name = WeblieferantQuery.ByWebpartnerIId, query = "SELECT OBJECT (o) FROM Weblieferant o WHERE o.webpartnerIId = :webpartnerIId"),
		@NamedQuery(name = WeblieferantQuery.SelectMaxISort, query = "SELECT MAX (o.iSort) FROM Weblieferant AS o"),
		@NamedQuery(name = WeblieferantQuery.ByMandantCNr, query = "SELECT OBJECT (wl) FROM Weblieferant wl JOIN wl.webpartner wp JOIN wp.lieferant l WHERE l.mandantCNr = :mandantCNr"),
		@NamedQuery(name = WeblieferantQuery.ByWebabfrageTypMandantCNr, query = "SELECT OBJECT (wl) FROM Weblieferant wl JOIN wl.webpartner wp JOIN wp.lieferant l JOIN wp.webabfrage wa WHERE l.mandantCNr = :mandantCNr AND wa.iTyp = :webabfrageTyp")
})

@Entity
@Table(name = "AS_WEBLIEFERANT")
public class Weblieferant implements Serializable, IWeblieferant {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "I_ID")
	@TableGenerator(name = "weblieferant_id", table = "LP_PRIMARYKEY", pkColumnName = "C_NAME",
			pkColumnValue = "weblieferant", valueColumnName = "I_INDEX", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "weblieferant_id")
	private Integer iId;

	@Column(name = "WEBPARTNER_I_ID")
	private Integer webpartnerIId;
	
	@Column(name = "I_SORT")
	private Integer iSort;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "WEBPARTNER_I_ID", referencedColumnName = "I_ID", insertable = false, updatable = false)
	private Webpartner webpartner;

	public Weblieferant() {

	}

	public Weblieferant(Integer iId, Integer webpartnerIId,
			Integer iSort) {
		setIId(iId);
		setWebpartnerIId(webpartnerIId);
		setISort(iSort);
		
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getISort() {
		return iSort;
	}

	public void setISort(Integer iSort) {
		this.iSort = iSort;
	}

	public Integer getWebpartnerIId() {
		return webpartnerIId;
	}

	public void setWebpartnerIId(Integer webpartnerIId) {
		this.webpartnerIId = webpartnerIId;
	}

	public Webpartner getWebpartnerObject() {
		return webpartner;
	}

	public void setWebpartnerObject(Webpartner webpartner) {
		this.webpartner = webpartner;
	}

}
