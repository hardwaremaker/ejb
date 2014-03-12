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
package com.lp.server.personal.ejb;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.cfg.annotations.SetBinder;

@NamedQueries( {
		@NamedQuery(name = "UrlaubsanspruchfindByPersonalIIdIJahr", query = "SELECT OBJECT(C) FROM Urlaubsanspruch c WHERE c.personalIId = ?1 AND c.iJahr = ?2"),
		@NamedQuery(name = "UrlaubsanspruchfindLetztenUrlaubsanspruch", query = "SELECT OBJECT(C) FROM Urlaubsanspruch c WHERE c.personalIId = ?1 AND c.iJahr <= ?2 ORDER BY c.iJahr DESC"),
		@NamedQuery(name = "UrlaubsanspruchfindByPersonalIIdIJahrGroesser", query = "SELECT OBJECT(C) FROM Urlaubsanspruch c WHERE c.personalIId = ?1 AND c.iJahr >= ?2 ORDER BY c.iJahr ASC"),
		@NamedQuery(name = "UrlaubsanspruchfindByPersonalIIdIJahrKleiner", query = "SELECT OBJECT(C) FROM Urlaubsanspruch c WHERE c.personalIId = ?1 AND c.iJahr <= ?2 ORDER BY c.iJahr DESC") })
@Entity
@Table(name = "PERS_URLAUBSANSPRUCH")
public class Urlaubsanspruch implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "I_JAHR")
	private Integer iJahr;

	@Column(name = "F_STUNDEN")
	private Double fStunden;

	@Column(name = "F_STUNDENZUSAETZLICH")
	private Double fStundenzusaetzlich;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "F_TAGE")
	private Double fTage;

	@Column(name = "F_TAGEZUSAETZLICH")
	private Double fTagezusaetzlich;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "PERSONAL_I_ID")
	private Integer personalIId;

	@Column(name = "F_RESTURLAUBJAHRESENDESTUNDEN")
	private Double fResturlaubjahresendestunden;
	
	@Column(name = "F_RESTURLAUBJAHRESENDETAGE")
	private Double fResturlaubjahresendetage;
	
	@Column(name = "B_GESPERRT")
	private Short bGesperrt;

	public Double getFJahresurlaubinwochen() {
		return fJahresurlaubinwochen;
	}

	public void setFJahresurlaubinwochen(Double fJahresurlaubinwochen) {
		this.fJahresurlaubinwochen = fJahresurlaubinwochen;
	}

	@Column(name = "F_JAHRESURLAUBINWOCHEN")
	private Double fJahresurlaubinwochen;
	
	
	private static final long serialVersionUID = 1L;

	public Urlaubsanspruch() {
		super();
	}

	public Urlaubsanspruch(Integer id, Integer personalIId, Integer jahr,
			Double tage, Double stunden, Integer personaIIdAendern,
			Double stundenzusaetzlich, Double tagezusaetzlich,Short bGesperrt, Double fJahresurlaubinwochen) {
		setIId(id);
		setPersonalIId(personalIId);
		setIJahr(jahr);
		setFTage(tage);
		setFStunden(stunden);
		setTAendern(new Timestamp(System.currentTimeMillis()));
		setPersonalIIdAendern(personaIIdAendern);
		setFStundenzusaetzlich(stundenzusaetzlich);
		setFTagezusaetzlich(tagezusaetzlich);
		setBGesperrt(bGesperrt);
		setFJahresurlaubinwochen(fJahresurlaubinwochen);
	}

	public Double getFResturlaubjahresendestunden() {
		return fResturlaubjahresendestunden;
	}

	public void setFResturlaubjahresendestunden(Double resturlaubjahresendestunden) {
		fResturlaubjahresendestunden = resturlaubjahresendestunden;
	}

	public Double getFResturlaubjahresendetage() {
		return fResturlaubjahresendetage;
	}

	public void setFResturlaubjahresendetage(Double resturlaubjahresendetage) {
		fResturlaubjahresendetage = resturlaubjahresendetage;
	}

	public Short getBGesperrt() {
		return bGesperrt;
	}

	public void setBGesperrt(Short gesperrt) {
		bGesperrt = gesperrt;
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getIJahr() {
		return this.iJahr;
	}

	public void setIJahr(Integer iJahr) {
		this.iJahr = iJahr;
	}

	public Double getFStunden() {
		return this.fStunden;
	}

	public void setFStunden(Double fStunden) {
		this.fStunden = fStunden;
	}

	public Double getFStundenzusaetzlich() {
		return this.fStundenzusaetzlich;
	}

	public void setFStundenzusaetzlich(Double fStundenzusaetzlich) {
		this.fStundenzusaetzlich = fStundenzusaetzlich;
	}

	public Timestamp getTAendern() {
		return this.tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public Double getFTage() {
		return this.fTage;
	}

	public void setFTage(Double fTage) {
		this.fTage = fTage;
	}

	public Double getFTagezusaetzlich() {
		return this.fTagezusaetzlich;
	}

	public void setFTagezusaetzlich(Double fTagezusaetzlich) {
		this.fTagezusaetzlich = fTagezusaetzlich;
	}

	public Integer getPersonalIIdAendern() {
		return this.personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public Integer getPersonalIId() {
		return this.personalIId;
	}

	public void setPersonalIId(Integer personalIId) {
		this.personalIId = personalIId;
	}

}
