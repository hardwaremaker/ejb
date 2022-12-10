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
package com.lp.server.benutzer.ejb;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.lp.server.system.service.ITablenames;

@NamedQueries( { @NamedQuery(name = "BenutzerfindByCBenutzerkennung", query = "SELECT OBJECT(C) FROM Benutzer c WHERE c.cBenutzerkennung = ?1") })
@Entity
@Table(name = ITablenames.PERS_BENUTZER)
public class Benutzer implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_BENUTZERKENNUNG")
	private String cBenutzerkennung;

	@Column(name = "C_KENNWORT")
	private String cKennwort;

	@Column(name = "B_AENDERN")
	private Short bAendern;

	@Column(name = "B_GESPERRT")
	private Short bGesperrt;

	@Column(name = "T_GUELTIGBIS")
	private Timestamp tGueltigbis;

	@Column(name = "I_FEHLVERSUCHEGEMACHT")
	private Integer iFehlversuchegemacht;

	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "MANDANT_C_NR_DEFAULT")
	private String mandantCNrDefault;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;

	private static final long serialVersionUID = 1L;

	public Benutzer() {
		super();
	}

	public Benutzer(Integer id,
			String benutzerkennung,
			String kennwort,
			Integer personalIIdAnlegen2,
			Integer personalIIdAendern2) {
		setIId(id);
		setTAnlegen(new Timestamp(System.currentTimeMillis()));
		setTAendern(new Timestamp(System.currentTimeMillis()));
		setCBenutzerkennung(benutzerkennung);
		setCKennwort(kennwort);
		setPersonalIIdAnlegen(personalIIdAnlegen2);
		setPersonalIIdAendern(personalIIdAendern2);
		setBAendern(new Short((short) 0));
		setBGesperrt(new Short((short) 0));
	}
	
	public Benutzer(Integer id,
			String benutzerkennung,
			String kennwort,
			Short aendern,
			Short gesperrt,
			Integer personalIIdAnlegen2,
			Integer personalIIdAendern2) {
		setIId(id);
		setTAnlegen(new Timestamp(System.currentTimeMillis()));
		setTAendern(new Timestamp(System.currentTimeMillis()));
		setCBenutzerkennung(benutzerkennung);
		setCKennwort(kennwort);
		setPersonalIIdAnlegen(personalIIdAnlegen2);
		setPersonalIIdAendern(personalIIdAendern2);
		setBAendern(aendern);
		setBGesperrt(gesperrt);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCBenutzerkennung() {
		return this.cBenutzerkennung;
	}

	public void setCBenutzerkennung(String cBenutzerkennung) {
		this.cBenutzerkennung = cBenutzerkennung;
	}

	public String getCKennwort() {
		return this.cKennwort;
	}

	public void setCKennwort(String cKennwort) {
		this.cKennwort = cKennwort;
	}

	public Short getBAendern() {
		return this.bAendern;
	}

	public void setBAendern(Short bAendern) {
		this.bAendern = bAendern;
	}

	public Short getBGesperrt() {
		return this.bGesperrt;
	}

	public void setBGesperrt(Short bGesperrt) {
		this.bGesperrt = bGesperrt;
	}

	public Timestamp getTGueltigbis() {
		return this.tGueltigbis;
	}

	public void setTGueltigbis(Timestamp tGueltigbis) {
		this.tGueltigbis = tGueltigbis;
	}

	public Integer getIFehlversuchegemacht() {
		return this.iFehlversuchegemacht;
	}

	public void setIFehlversuchegemacht(Integer iFehlversuchegemacht) {
		this.iFehlversuchegemacht = iFehlversuchegemacht;
	}

	public Timestamp getTAnlegen() {
		return this.tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	public Timestamp getTAendern() {
		return this.tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public String getMandantCNrDefault() {
		return this.mandantCNrDefault;
	}

	public void setMandantCNrDefault(String mandantCNrDefault) {
		this.mandantCNrDefault = mandantCNrDefault;
	}

	public Integer getPersonalIIdAendern() {
		return this.personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public Integer getPersonalIIdAnlegen() {
		return this.personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}

}
