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
package com.lp.server.system.ejb;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries( {
		@NamedQuery(name = "TheClientfindByUserLoggedIn", query = "SELECT OBJECT (o) FROM Theclient o WHERE o.cNr=?1 AND o.tLoggedin <?2 AND o.tLoggedout IS NULL"),
		@NamedQuery(name = "TheClientfindByTLoggedIn", query = "SELECT OBJECT (o) FROM Theclient o WHERE o.tLoggedin>?1 AND o.tLoggedin <?2"),
		@NamedQuery(name = "TheClientfindByCBenutzernameLoggedIn", query = "SELECT OBJECT (o) FROM Theclient o WHERE o.cBenutzername=?1 AND o.tLoggedin >?2 AND o.tLoggedout IS NULL"),
		@NamedQuery(name = "TheClientfindLoggedIn", query = "SELECT OBJECT (o) FROM Theclient o WHERE o.tLoggedout IS NULL")
		})
@Entity
@Table(name = "LP_THECLIENT")
public class Theclient implements Serializable {
	
	@Id
	@Column(name = "C_NR")
	private String cNr;

	@Column(name = "C_BENUTZERNAME")
	private String cBenutzername;

	@Column(name = "C_KENNWORT")
	private String cKennwort;

	@Column(name = "C_MANDANT")
	private String cMandant;

	@Column(name = "C_MANDANTWAEHRUNG")
	private String cMandantwaehrung;

	@Column(name = "I_PERSONAL")
	private Integer iPersonal;

	@Column(name = "C_UILOCALE")
	private String cUilocale;

	@Column(name = "C_MANDANTENLOCALE")
	private String cMandantenlocale;

	@Column(name = "C_KONZERNLOCALE")
	private String cKonzernlocale;

	@Column(name = "I_STATUS")
	private Integer iStatus;

	@Column(name = "T_LOGGEDIN")
	private Timestamp tLoggedin;

	@Column(name = "T_LOGGEDOUT")
	private Timestamp tLoggedout;
	
	@Column(name = "SYSTEMROLLE_I_ID")
	private Integer systemrolleIId;

	private static final long serialVersionUID = 1L;

	public Theclient() {
		super();
	}

	public Theclient(String user,
			String benutzername,
			String kennwortAsString,
			String mandant,
			String mandantenwaehrung,
			Integer personal,
			String locUiAsString,
			String locMandantAsString,
			String locKonzernAsString,
			Timestamp loggedin,
			Integer systemrolleIId) {
		setCNr(user);
		setCMandant(mandant);
		setIPersonal(personal);
		setCUilocale(locUiAsString);
		setCKonzernlocale(locKonzernAsString);
		setCMandantenlocale(locMandantAsString);
		setCBenutzername(benutzername);
		setTLoggedin(loggedin);
		setCKennwort(kennwortAsString);
		setCMandantwaehrung(mandantenwaehrung);
		setSystemrolleIId(systemrolleIId);
	}
	
	public Integer getSystemrolleIId() {
		return systemrolleIId;
	}
	
	public void setSystemrolleIId(Integer systemrolleIId) {
		this.systemrolleIId = systemrolleIId;
	}

	public String getCNr() {
		return this.cNr;
	}

	public void setCNr(String cNr) {
		this.cNr = cNr;
	}

	public String getCBenutzername() {
		return this.cBenutzername;
	}

	public void setCBenutzername(String cBenutzername) {
		this.cBenutzername = cBenutzername;
	}

	public String getCKennwort() {
		return this.cKennwort;
	}

	public void setCKennwort(String cKennwort) {
		this.cKennwort = cKennwort;
	}

	public String getCMandant() {
		return this.cMandant;
	}

	public void setCMandant(String cMandant) {
		this.cMandant = cMandant;
	}

	public String getCMandantwaehrung() {
		return this.cMandantwaehrung;
	}

	public void setCMandantwaehrung(String cMandantwaehrung) {
		this.cMandantwaehrung = cMandantwaehrung;
	}

	public Integer getIPersonal() {
		return this.iPersonal;
	}

	public void setIPersonal(Integer iPersonal) {
		this.iPersonal = iPersonal;
	}

	public String getCUilocale() {
		return this.cUilocale;
	}

	public void setCUilocale(String cUilocale) {
		this.cUilocale = cUilocale;
	}

	public String getCMandantenlocale() {
		return this.cMandantenlocale;
	}

	public void setCMandantenlocale(String cMandantenlocale) {
		this.cMandantenlocale = cMandantenlocale;
	}

	public String getCKonzernlocale() {
		return this.cKonzernlocale;
	}

	public void setCKonzernlocale(String cKonzernlocale) {
		this.cKonzernlocale = cKonzernlocale;
	}

	public Integer getIStatus() {
		return this.iStatus;
	}

	public void setIStatus(Integer iStatus) {
		this.iStatus = iStatus;
	}

	public Timestamp getTLoggedin() {
		return this.tLoggedin;
	}

	public void setTLoggedin(Timestamp tLoggedin) {
		this.tLoggedin = tLoggedin;
	}

	public Timestamp getTLoggedout() {
		return this.tLoggedout;
	}

	public void setTLoggedout(Timestamp tLoggedout) {
		this.tLoggedout = tLoggedout;
	}

}
