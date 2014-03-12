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
package com.lp.server.system.ejb;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "LP_ANWENDER")
public class Anwender implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "I_BUILDNUMMERDB")
	private Integer iBuildnummerdb;

	@Column(name = "C_VERSIONDB")
	private String cVersiondb;

	@Column(name = "I_BUILDNUMMERCLIENTVON")
	private Integer iBuildnummerclientvon;

	@Column(name = "I_BUILDNUMMERCLIENTBIS")
	private Integer iBuildnummerclientbis;

	@Column(name = "I_BUILDNUMMERSERVERVON")
	private Integer iBuildnummerservervon;

	@Column(name = "I_BUILDNUMMERSERVERBIS")
	private Integer iBuildnummerserverbis;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "MANDANT_C_NR_HAUPTMANDANT")
	private String mandantCNrHauptmandant;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "T_ABLAUF")
	private Timestamp tAblauf;
	
	@Column(name = "O_CODE")
	private byte[] oCode; 
	
 	@Column(name= "O_HASH")
	private byte[] oHash;

 	@Column(name = "O_HINTERGRUND")
	private byte[] oHintergrund;
 	
	private static final long serialVersionUID = 1L;

	public Anwender() {
		super();
	}

	public Anwender(Integer id,
			Integer buildnummerDB, 
			String versionDB,
			Integer buildnummerClientVon,
			Integer buildnummerClientBis,
			Integer buildnummerServerVon,
			Integer buildnummerServerBis) {
		setIBuildnummerdb(buildnummerDB);
		setIBuildnummerservervon(buildnummerServerVon);
		setIBuildnummerserverbis(buildnummerServerBis);
		setIBuildnummerclientvon(buildnummerClientVon);
		setTAendern(new Timestamp(System.currentTimeMillis()));
		setIBuildnummerclientbis(buildnummerClientBis);
		setIId(id);
		setCVersiondb(versionDB);
	}

	public byte[] getOHintergrund() {
		return oHintergrund;
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getIBuildnummerdb() {
		return this.iBuildnummerdb;
	}

	public void setIBuildnummerdb(Integer iBuildnummerdb) {
		this.iBuildnummerdb = iBuildnummerdb;
	}

	public String getCVersiondb() {
		return this.cVersiondb;
	}

	public void setCVersiondb(String cVersiondb) {
		this.cVersiondb = cVersiondb;
	}

	public Integer getIBuildnummerclientvon() {
		return this.iBuildnummerclientvon;
	}

	public void setIBuildnummerclientvon(Integer iBuildnummerclientvon) {
		this.iBuildnummerclientvon = iBuildnummerclientvon;
	}

	public Integer getIBuildnummerclientbis() {
		return this.iBuildnummerclientbis;
	}

	public void setIBuildnummerclientbis(Integer iBuildnummerclientbis) {
		this.iBuildnummerclientbis = iBuildnummerclientbis;
	}

	public Integer getIBuildnummerservervon() {
		return this.iBuildnummerservervon;
	}

	public void setIBuildnummerservervon(Integer iBuildnummerservervon) {
		this.iBuildnummerservervon = iBuildnummerservervon;
	}

	public Integer getIBuildnummerserverbis() {
		return this.iBuildnummerserverbis;
	}

	public void setIBuildnummerserverbis(Integer iBuildnummerserverbis) {
		this.iBuildnummerserverbis = iBuildnummerserverbis;
	}

	public Timestamp getTAendern() {
		return this.tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public String getMandantCNrHauptmandant() {
		return this.mandantCNrHauptmandant;
	}

	public void setMandantCNrHauptmandant(String mandantCNrHauptmandant) {
		this.mandantCNrHauptmandant = mandantCNrHauptmandant;
	}

	public Integer getPersonalIIdAendern() {
		return this.personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public Timestamp getTAblauf() {
		return tAblauf;
	}

	public byte[] getOCode() {
		return oCode;
	}

	public byte[] getOHash() {
		return oHash;
	}

}
