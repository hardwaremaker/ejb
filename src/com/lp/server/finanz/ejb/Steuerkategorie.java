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

import com.lp.util.Helper;

@NamedQueries({
	@NamedQuery(name = "SteuerkategorieByCNrMandantCNr", query = "SELECT OBJECT(o) FROM Steuerkategorie o WHERE o.cNr = ?1 AND o.mandantCNr = ?2"),
	@NamedQuery(name = "SteuerkategorieByCNrFinanzamtIIDMandant", query = "SELECT OBJECT(o) FROM Steuerkategorie o WHERE o.cNr = ?1 AND o.finanzamtIId = ?2 AND o.mandantCNr = ?3"),
	@NamedQuery(name = "SteuerkategorieByMandantCNr", query = "SELECT OBJECT(o) FROM Steuerkategorie o WHERE o.mandantCNr = ?1"),
	@NamedQuery(name = "SteuerkategorieejbSelectNextReihung", query = "SELECT MAX (o.iSort) FROM Steuerkategorie o WHERE o.mandantCNr = ?1"),
	@NamedQuery(name = "SteuerkategorieByFinanzamtIIDMandant", query = "SELECT OBJECT(o) FROM Steuerkategorie o WHERE o.finanzamtIId = ?1 AND o.mandantCNr = ?2"),
	@NamedQuery(name = "SteuerkategorieByCNrReversechargeartIdFinanzamtIIDMandant", query = "SELECT OBJECT(o) FROM Steuerkategorie o WHERE o.cNr = ?1 AND o.reversechargeartId = ?2 AND o.finanzamtIId = ?3 AND o.mandantCNr = ?4"),
	@NamedQuery(name = "SteuerkategorieByFinanzamtIIDReversechargeartMandant", query = "SELECT OBJECT(o) FROM Steuerkategorie o WHERE o.finanzamtIId = ?1 And o.reversechargeartId = ?2 AND o.mandantCNr = ?3")
	
})
@Entity
@Table(name = "FB_STEUERKATEGORIE")
public class Steuerkategorie implements Serializable {
	
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_NR")
	private String cNr;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;

	@Column(name = "B_REVERSECHARGE")
	private Short bReversecharge;

	@Column(name = "C_BEZ")
	private String cBez;

	@Column(name = "I_SORT")
	private Integer iSort;

	@Column(name = "KONTO_I_ID_FORDERUNGEN")
	private Integer kontoIIdForderungen;
	
	@Column(name = "KONTO_I_ID_VERBINDLICHKEITEN")
	private Integer kontoIIdVerbindlichkeiten;
	
	@Column(name = "FINANZAMT_I_ID")
	private Integer finanzamtIId;
	
	@Column(name = "KONTO_I_ID_KURSGEWINN")
	private Integer kontoIIdKursgewinn;
	
	@Column(name = "KONTO_I_ID_KURSVERLUST")
	private Integer kontoIIdKursverlust;

	@Column(name = "REVERSECHARGEART_I_ID")
	private Integer reversechargeartId ;
	
	public String getMandantCNr() {
		return mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	private static final long serialVersionUID = 1L;

	public Steuerkategorie() {
		super();
	}

	public Steuerkategorie(Integer id, String nr, 
			String mandantCNr, String cBez, Integer finanzamtIID, Integer reversechargeartId) {
		setCNr(nr);
		setIId(id);
		setMandantCNr(mandantCNr);
		setCBez(cBez);
		setFinanzamtIId(finanzamtIID);
		setReversechargeartIId(reversechargeartId);
		setbReversecharge(Helper.getShortFalse());
	}

	public Short getbReversecharge() {
		return bReversecharge;
	}

	public void setbReversecharge(Short bReversecharge) {
		this.bReversecharge = bReversecharge;
	}

	public String getCBez() {
		return cBez;
	}

	public void setCBez(String cBez) {
		this.cBez = cBez;
	}

	public Integer getISort() {
		return iSort;
	}

	public void setISort(Integer iSort) {
		this.iSort = iSort;
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCNr() {
		return this.cNr;
	}

	public void setCNr(String cNr) {
		this.cNr = cNr;
	}

	public void setKontoIIdForderungen(Integer kontoIIdForderungen) {
		this.kontoIIdForderungen = kontoIIdForderungen;
	}

	public Integer getKontoIIdForderungen() {
		return kontoIIdForderungen;
	}

	public void setKontoIIdVerbindlichkeiten(Integer kontoIIdVerbindlichkeiten) {
		this.kontoIIdVerbindlichkeiten = kontoIIdVerbindlichkeiten;
	}

	public Integer getKontoIIdVerbindlichkeiten() {
		return kontoIIdVerbindlichkeiten;
	}

	public void setFinanzamtIId(Integer finanzamtIId) {
		this.finanzamtIId = finanzamtIId;
	}

	public Integer getFinanzamtIId() {
		return finanzamtIId;
	}

	public void setKontoIIdKursgewinn(Integer kontoIIdKursgewinn) {
		this.kontoIIdKursgewinn = kontoIIdKursgewinn;
	}

	public Integer getKontoIIdKursgewinn() {
		return kontoIIdKursgewinn;
	}

	public void setKontoIIdKursverlust(Integer kontoIIdKursverlust) {
		this.kontoIIdKursverlust = kontoIIdKursverlust;
	}

	public Integer getKontoIIdKursverlust() {
		return kontoIIdKursverlust;
	}

	public void setReversechargeartIId(Integer reversechargeartId) {
		this.reversechargeartId = reversechargeartId ;
	}
	
	public Integer getReversechargeartIId() {
		return reversechargeartId ;
	}
}
