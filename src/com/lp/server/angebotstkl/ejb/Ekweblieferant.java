
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
	@NamedQuery(name = EkWeblieferantQuery.ByEinkaufsangebotIIdWebabfrageTyp, query = "SELECT OBJECT (wl) FROM Ekweblieferant wl JOIN wl.webpartner wp JOIN wp.webabfrage wa WHERE wl.einkaufsangebotIId = :einkaufsangebotIId AND wa.iTyp = :webabfrageTyp"),
	@NamedQuery(name = EkWeblieferantQuery.ByEinkaufsangebotIIdWebpartnerIId, query = "SELECT OBJECT (o) FROM Ekweblieferant o WHERE o.einkaufsangebotIId = :einkaufsangebotIId AND o.webpartnerIId = :webpartnerIId"),
	@NamedQuery(name = EkWeblieferantQuery.SelectMaxISortEinkaufsangebotIId, query = "SELECT MAX (o.iSort) FROM Ekweblieferant AS o WHERE o.einkaufsangebotIId = :einkaufsangebotIId") 
})

@Entity
@Table(name = "AS_EKWEBLIEFERANT")
public class Ekweblieferant implements Serializable, IWeblieferant {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "I_ID")
	@TableGenerator(name = "ekweblieferant_id", table = "LP_PRIMARYKEY", pkColumnName = "C_NAME",
		pkColumnValue = "ekweblieferant", valueColumnName = "I_INDEX", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "ekweblieferant_id")
	private Integer iId;

	@Column(name = "EINKAUFSANGEBOT_I_ID")
	private Integer einkaufsangebotIId;

	@Column(name = "WEBPARTNER_I_ID")
	private Integer webpartnerIId;
	
	@Column(name = "I_SORT")
	private Integer iSort;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "WEBPARTNER_I_ID", referencedColumnName = "I_ID", insertable = false, updatable = false)
	private Webpartner webpartner;

	public Ekweblieferant() {
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getWebpartnerIId() {
		return webpartnerIId;
	}

	public void setWebpartnerIId(Integer webpartnerIId) {
		this.webpartnerIId = webpartnerIId;
	}

	public Integer getISort() {
		return iSort;
	}

	public void setISort(Integer iSort) {
		this.iSort = iSort;
	}

	public Integer getEinkaufsangebotIId() {
		return this.einkaufsangebotIId;
	}

	public void setEinkaufsangebotIId(Integer einkaufsangebot) {
		this.einkaufsangebotIId = einkaufsangebot;
	}

	public Webpartner getWebpartnerObject() {
		return webpartner;
	}

	public void setWebpartnerObject(Webpartner webpartner) {
		this.webpartner = webpartner;
	}

}
