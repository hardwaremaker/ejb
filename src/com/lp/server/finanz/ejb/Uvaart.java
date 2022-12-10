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
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.lp.server.util.ICNr;

@NamedQueries( 
		{ @NamedQuery(name = "UvaartfindByCNrMandant", query = "SELECT OBJECT(o) FROM Uvaart o WHERE o.cNr=?1 AND o.mandantCNr=?2"),
		  @NamedQuery(name = "UvaartfindAll", query = "SELECT OBJECT(o) FROM Uvaart o WHERE o.mandantCNr=?1")})

@Entity
@Table(name = "FB_UVAART")
public class Uvaart implements Serializable, ICNr {
	@Id
	@Column(name = "I_ID")
	private Integer iId;
	
	@Column(name = "C_NR")
	private String cNr;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;
	
	@Column(name = "C_KENNZEICHEN")
	private String cKennzeichen;
	
	@Column(name = "I_SORT")
	private Integer iSort;
	
	@Column(name = "B_INVERTIERT")
	private short bInvertiert;
	
	@Column(name = "B_KEINE_AUSWAHL_BEI_ER")
	private short bKeineAuswahlBeiEr;

	public short getBKeineAuswahlBeiEr() {
		return bKeineAuswahlBeiEr;
	}

	public void setBKeineAuswahlBeiEr(short bKeineAuswahlBeiEr) {
		this.bKeineAuswahlBeiEr = bKeineAuswahlBeiEr;
	}

	private static final long serialVersionUID = 1L;

	public Uvaart() {
		super();
	}

	public Uvaart(String nr) {
		setCNr(nr);
	}

	public String getCNr() {
		return this.cNr;
	}

	public short getBInvertiert() {
		return bInvertiert;
	}

	public void setBInvertiert(short invertiert) {
		bInvertiert = invertiert;
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

	public void setCKennzeichen(String cKennzeichen) {
		this.cKennzeichen = cKennzeichen;
	}

	public String getCKennzeichen() {
		return cKennzeichen;
	}

}
