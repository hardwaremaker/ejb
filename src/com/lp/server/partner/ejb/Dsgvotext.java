


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
package com.lp.server.partner.ejb;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity

@NamedQueries( {
	@NamedQuery(name = "DsgvotextejbSelectNextReihung", query = "SELECT MAX (o.iSort) FROM Dsgvotext o WHERE o.mandantCNr = ?1") })


@Table(name = "PART_DSGVOTEXT")
public class Dsgvotext implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;
	
	@Column(name = "I_SORT")
	private Integer iSort;
	
	@Column(name = "DSGVOKATEGORIE_I_ID")
	private Integer dsgvokategorieIId;
	
	@Column(name = "B_KOPFTEXT")
	private Short bKopftext;
	
	@Column(name = "X_INHALT")
	private String xInhalt;

	
	public String getXInhalt() {
		return xInhalt;
	}

	public void setXInhalt(String xInhalt) {
		this.xInhalt = xInhalt;
	}

	private static final long serialVersionUID = 1L;

	public Dsgvotext() {
		super();
	}

	public Dsgvotext(Integer id, String mandant_c_nr,Integer dsgvokategorieIId,Short bKopftext,Integer iSort, String xInhalt) {
		setIId(id);
		setMandantCNr(mandant_c_nr);
		setDsgvokategorieIId(dsgvokategorieIId);
		setBKopftext(bKopftext);
		setISort(iSort);
		setXInhalt(xInhalt);

	}

	public Integer getISort() {
		return iSort;
	}

	public void setISort(Integer iSort) {
		this.iSort = iSort;
	}

	public Integer getDsgvokategorieIId() {
		return dsgvokategorieIId;
	}

	public void setDsgvokategorieIId(Integer dsgvokategorieIId) {
		this.dsgvokategorieIId = dsgvokategorieIId;
	}

	public Short getBKopftext() {
		return bKopftext;
	}

	public void setBKopftext(Short bKopftext) {
		this.bKopftext = bKopftext;
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getMandantCNr() {
		return this.mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

}
