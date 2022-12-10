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
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.lp.server.angebotstkl.service.IWebpartner;
import com.lp.server.partner.ejb.Lieferant;
import com.lp.server.system.service.ITablenames;

@NamedQueries({
	@NamedQuery(name = WebpartnerQuery.ByWebabfrageTypId, query = "SELECT OBJECT (o) FROM Webpartner o JOIN o.webabfrage w WHERE w.iTyp = :webabfrageTypId"),
	@NamedQuery(name = WebpartnerQuery.ByLieferantIdWebabfrageTypIIds, query = "SELECT OBJECT (o) FROM Webpartner o JOIN o.webabfrage w WHERE o.lieferantIId = :lieferant AND w.iTyp IN (:webabfrageTypIds)"),
	@NamedQuery(name = WebpartnerQuery.ByMandantWebabfrageTypIIds, query = "SELECT OBJECT (o) FROM Webpartner o JOIN o.webabfrage w JOIN o.lieferant l WHERE w.iTyp IN (:webabfrageTypIds) AND l.mandantCNr = :mandant")
})

@Entity
@Table(name = ITablenames.AS_WEBPARTNER)
@Inheritance(strategy = InheritanceType.JOINED)
public class Webpartner implements Serializable, IWebpartner {

	private static final long serialVersionUID = -5081189090314171651L;
	
	@Id
	@Column(name = "I_ID")
	@TableGenerator(name = "webpartner_id", table = "LP_PRIMARYKEY", pkColumnName = "C_NAME",
			pkColumnValue = "webpartner", valueColumnName = "I_INDEX", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "webpartner_id")
	private Integer iId;

	@Column(name = "WEBABFRAGE_I_ID")
	private Integer webabfrageIId;

	@Column(name = "LIEFERANT_I_ID")
	private Integer lieferantIId;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "WEBABFRAGE_I_ID" , referencedColumnName= "I_ID", insertable = false, updatable = false)
	private Webabfrage webabfrage;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LIEFERANT_I_ID", referencedColumnName = "I_ID", insertable = false, updatable = false)
	private Lieferant lieferant;

	public Webpartner() {
	}

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getWebabfrageIId() {
		return webabfrageIId;
	}

	public void setWebabfrageIId(Integer webabfrageIId) {
		this.webabfrageIId = webabfrageIId;
	}

	public Integer getLieferantIId() {
		return lieferantIId;
	}

	public void setLieferantIId(Integer lieferantIId) {
		this.lieferantIId = lieferantIId;
	}

	public Webabfrage getWebabfrageObject() {
		return webabfrage;
	}

	public void setWebabfrageObject(Webabfrage webabfrage) {
		this.webabfrage = webabfrage;
	}

	public Lieferant getLieferantObject() {
		return lieferant;
	}

	public void setLieferantObject(Lieferant lieferant) {
		this.lieferant = lieferant;
	}

}
