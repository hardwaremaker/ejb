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

@NamedQueries( { @NamedQuery(name = "PersonalangehoerigefindByPersonalIIdAngegoerigenartCNrCVorname", query = "SELECT OBJECT(C) FROM Personalangehoerige c WHERE c.personalIId = ?1 AND c.angehoerigenartCNr = ?2 AND c.cVorname = ?3") ,
	@NamedQuery(name = "PersonalangehoerigefindByPersonalIId", query = "SELECT OBJECT(C) FROM Personalangehoerige c WHERE c.personalIId = ?1") })
@Entity
@Table(name = "PERS_PERSONALANGEHOERIGE")
public class Personalangehoerige implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_VORNAME")
	private String cVorname;

	@Column(name = "C_NAME")
	private String cName;

	@Column(name = "T_GEBURTSDATUM")
	private Timestamp tGeburtsdatum;

	@Column(name = "C_SOZIALVERSNR")
	private String cSozialversnr;

	@Column(name = "ANGEHOERIGENART_C_NR")
	private String angehoerigenartCNr;

	@Column(name = "PERSONAL_I_ID")
	private Integer personalIId;

	private static final long serialVersionUID = 1L;

	public Personalangehoerige() {
		super();
	}

	public Personalangehoerige(Integer id, Integer personalIId,
			String angehoerigenartCNr, String vorname) {
		setIId(id);
		setPersonalIId(personalIId);
		setAngehoerigenartCNr(angehoerigenartCNr);
		setCVorname(vorname);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCVorname() {
		return this.cVorname;
	}

	public void setCVorname(String cVorname) {
		this.cVorname = cVorname;
	}

	public String getCName() {
		return this.cName;
	}

	public void setCName(String cName) {
		this.cName = cName;
	}

	public Timestamp getTGeburtsdatum() {
		return this.tGeburtsdatum;
	}

	public void setTGeburtsdatum(Timestamp tGeburtsdatum) {
		this.tGeburtsdatum = tGeburtsdatum;
	}

	public String getCSozialversnr() {
		return this.cSozialversnr;
	}

	public void setCSozialversnr(String cSozialversnr) {
		this.cSozialversnr = cSozialversnr;
	}

	public String getAngehoerigenartCNr() {
		return this.angehoerigenartCNr;
	}

	public void setAngehoerigenartCNr(String angehoerigenartCNr) {
		this.angehoerigenartCNr = angehoerigenartCNr;
	}

	public Integer getPersonalIId() {
		return this.personalIId;
	}

	public void setPersonalIId(Integer personalIId) {
		this.personalIId = personalIId;
	}

}
