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
package com.lp.server.finanz.ejb;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.lp.server.util.ICNr;
import com.lp.server.util.IISort;

@NamedQueries( { 
		@NamedQuery(name = ReversechargeartQuery.ByCnrMandant, query = "SELECT OBJECT(o) FROM Reversechargeart o WHERE o.cNr= :cnr AND o.mandantCNr= :mandant ORDER BY o.iSort"),
		@NamedQuery(name = ReversechargeartQuery.ByMandant, query = "SELECT OBJECT(o) FROM Reversechargeart o WHERE o.mandantCNr= :mandant AND o.bVersteckt = 0 ORDER BY o.iSort"),
		@NamedQuery(name = ReversechargeartQuery.NextISortByMandant, query = "SELECT MAX (o.iSort) FROM Reversechargeart o WHERE o.mandantCNr = :mandant"),	
		@NamedQuery(name = ReversechargeartQuery.ByMandantMitVersteckten, query = "SELECT OBJECT(o) FROM Reversechargeart o WHERE o.mandantCNr= :mandant ORDER BY o.iSort"),
		@NamedQuery(name = ReversechargeartQuery.IIdsByMandant, query = "SELECT iId FROM Reversechargeart o WHERE o.mandantCNr= :mandant AND o.bVersteckt = 0 ORDER BY o.iSort"),
		@NamedQuery(name = ReversechargeartQuery.IIdsByMandantMitVersteckten, query = "SELECT iId FROM Reversechargeart o WHERE o.mandantCNr= :mandant ORDER BY o.iSort")
})

@Entity
@Table(name = "FB_REVERSECHARGEART")
public class Reversechargeart implements Serializable, ICNr, IISort {
	private static final long serialVersionUID = -2252081764207424354L;

	@Id
	@Column(name = "I_ID")
	@TableGenerator(name="reversechargeart_id", table="LP_PRIMARYKEY",
		pkColumnName = "C_NAME", pkColumnValue="reversechargeart", valueColumnName="I_INDEX", initialValue = 1, allocationSize = 5)
	@GeneratedValue(strategy = GenerationType.TABLE, generator="reversechargeart_id")
	private Integer iId;
	
	@Column(name = "C_NR")
	private String cNr;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;
		
	@Column(name = "I_SORT")
	private Integer iSort;
	
	@Column(name = "B_VERSTECKT")
	private Short bVersteckt;

	public Reversechargeart() {
	}

	public Reversechargeart(String nr) {
		setCNr(nr);
		setBVersteckt(new Short("0"));
	}

	public String getCNr() {
		return this.cNr;
	}
	
	public void setCNr(String cNr) {
		this.cNr = cNr;
	}

	public Integer getISort() {
		return this.iSort;
	}

	public void setISort(Integer iSort) {
		this.iSort = iSort;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getIId() {
		return iId;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public String getMandantCNr() {
		return mandantCNr;
	}
	
	public void setBVersteckt(Short bVersteckt) {
		this.bVersteckt = bVersteckt;
	}
	
	public Short getBVersteckt() {
		return bVersteckt;
	}
}
