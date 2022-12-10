/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2016 HELIUM V IT-Solutions GmbH
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
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.lp.util.Helper;

@NamedQueries( { 
	@NamedQuery(name = SepakontoauszugQuery.ByBankverbindungIIdIAuszug, query = "SELECT OBJECT(o) FROM Sepakontoauszug o WHERE o.bankverbindungIId=:bankverbindung AND o.iAuszug=:auszug"),
	@NamedQuery(name = SepakontoauszugQuery.ByBankverbindungIIdIAuszugNotStatusCNr, query = "SELECT OBJECT(o) FROM Sepakontoauszug o WHERE o.bankverbindungIId=:bankverbindung AND o.iAuszug=:auszug AND o.statusCnr<>:status"),
	@NamedQuery(name = SepakontoauszugQuery.ByBankverbindungIIdStatusCNrOrderedByIAuszugAsc, query = "SELECT OBJECT(o) FROM Sepakontoauszug o WHERE o.bankverbindungIId=:bankverbindung AND o.statusCnr IN (:stati) ORDER BY o.iAuszug ASC"),
	@NamedQuery(name = SepakontoauszugQuery.ByBankverbindungIIdIAuszugNotStatusCNrTAuszugBetween, query = 
		"SELECT OBJECT(o) FROM Sepakontoauszug o "
			+ "WHERE o.bankverbindungIId=:bankverbindung "
			+ "AND o.iAuszug=:auszug "
			+ "AND o.statusCnr<>:status "
			+ "AND o.tAuszug>=:mindate "
			+ "AND o.tAuszug<=:maxdate ")
})

@Entity
@Table(name = "FB_SEPAKONTOAUSZUG")
public class Sepakontoauszug implements Serializable {

	private static final long serialVersionUID = -546471376687693943L;

	@Id
	@Column(name = "I_ID")
	@TableGenerator(name="sepakontoauszug_id", table="LP_PRIMARYKEY",
		pkColumnName = "C_NAME", pkColumnValue="sepakontoauszug", valueColumnName="I_INDEX", initialValue = 1, allocationSize = 5)
	@GeneratedValue(strategy = GenerationType.TABLE, generator="sepakontoauszug_id")
	private Integer iId;
	
	@Column(name = "BANKVERBINDUNG_I_ID")
	private Integer bankverbindungIId;

	@Column(name = "I_AUSZUG")
	private Integer iAuszug;
	
	@Column(name = "T_AUSZUG")
	private Timestamp tAuszug;

	@Column(name = "N_ANFANGSSALDO")
	private BigDecimal nAnfangssaldo;
	
	@Column(name = "N_ENDSALDO")
	private BigDecimal nEndsaldo;
	
	@Column(name = "I_VERSION")
	private Integer iVersion;
	
	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;
	
	@Column(name = "O_KONTOAUSZUG")
	private byte[] oKontoauszug;
	
	@Column(name = "C_CAMTFORMAT")
	private String cCamtFormat;

	@Column(name = "T_VERBUCHEN")
	private Timestamp tVerbuchen;

	@Column(name = "PERSONAL_I_ID_VERBUCHEN")
	private Integer personalIIdVerbuchen;

	@Column(name = "STATUS_C_NR")
	private String statusCnr;

	public Sepakontoauszug() {
	}

	public Integer getIId() {
		return iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getIAuszug() {
		return iAuszug;
	}

	public void setIAuszug(Integer iAuszug) {
		this.iAuszug = iAuszug;
	}

	public BigDecimal getNAnfangssaldo() {
		return nAnfangssaldo;
	}

	public void setNAnfangssaldo(BigDecimal nAnfangssaldo) {
		this.nAnfangssaldo = nAnfangssaldo;
	}

	public BigDecimal getNEndsaldo() {
		return nEndsaldo;
	}

	public void setNEndsaldo(BigDecimal nEndsaldo) {
		this.nEndsaldo = nEndsaldo;
	}

	public Integer getIVersion() {
		return iVersion;
	}

	public void setIVersion(Integer iVersion) {
		this.iVersion = iVersion;
	}

	public Timestamp getTAnlegen() {
		return tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	public Timestamp getTAendern() {
		return tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public Integer getPersonalIIdAendern() {
		return personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public Integer getPersonalIIdAnlegen() {
		return personalIIdAnlegen;
	}

	public void setPersonalIIdAnlegen(Integer personalIIdAnlegen) {
		this.personalIIdAnlegen = personalIIdAnlegen;
	}

	public byte[] getOKontoauszug() {
		return Helper.xor(oKontoauszug);
	}

	public void setOKontoauszug(byte[] oKontoauszug) {
		this.oKontoauszug = Helper.xor(oKontoauszug);
	}

	public String getCCamtFormat() {
		return cCamtFormat;
	}

	public void setCCamtFormat(String cCamtFormat) {
		this.cCamtFormat = cCamtFormat;
	}

	public Timestamp getTVerbuchen() {
		return tVerbuchen;
	}

	public void setTVerbuchen(Timestamp tVerbuchen) {
		this.tVerbuchen = tVerbuchen;
	}

	public Integer getPersonalIIdVerbuchen() {
		return personalIIdVerbuchen;
	}

	public void setPersonalIIdVerbuchen(Integer personalIIdVerbuchen) {
		this.personalIIdVerbuchen = personalIIdVerbuchen;
	}

	public Integer getBankverbindungIId() {
		return bankverbindungIId;
	}

	public void setBankverbindungIId(Integer bankverbindungIId) {
		this.bankverbindungIId = bankverbindungIId;
	}

	public Timestamp getTAuszug() {
		return tAuszug;
	}

	public void setTAuszug(Timestamp tAuszug) {
		this.tAuszug = tAuszug;
	}

	public String getStatusCnr() {
		return statusCnr;
	}

	public void setStatusCnr(String statusCnr) {
		this.statusCnr = statusCnr;
	}
	
}
