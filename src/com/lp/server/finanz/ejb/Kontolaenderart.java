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
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.lp.server.util.Validator;

@NamedQueries( 
{
	@NamedQuery(
			name = KontolaenderartQuery.ByCompound,
			query = "SELECT OBJECT(o) FROM Kontolaenderart o " +
				"WHERE o.mandantCNr=?1 AND o.kontoIId=?2 AND o.finanzamtIId=?3 " +
				"AND o.laenderartCNr=?4 AND o.reversechargeartId=?5 " +
				"AND o.tGueltigAb=?6"),
	@NamedQuery(
			name = KontolaenderartQuery.ByDate,
			query = "SELECT OBJECT(o) FROM Kontolaenderart o " +
				"WHERE o.mandantCNr=?1 AND o.kontoIId=?2 AND o.finanzamtIId=?3 " +
				"AND o.laenderartCNr=?4 AND o.reversechargeartId=?5 " +
				"AND o.tGueltigAb<=?6 ORDER BY o.tGueltigAb DESC"),
	@NamedQuery(
			name = KontolaenderartQuery.ByUebersetzt,
			query = "SELECT OBJECT(o) FROM Kontolaenderart o " +
				"WHERE o.mandantCNr=?1 AND o.finanzamtIId =?2 " +
				"AND o.reversechargeartId=?3 AND o.kontoIIdUebersetzt=?4 " +
				"AND o.tGueltigAb<=?5 ORDER BY o.tGueltigAb DESC"),
	@NamedQuery(
			name = KontolaenderartQuery.ByAll,
			query = "SELECT OBJECT(o) FROM Kontolaenderart o " +
					"WHERE o.mandantCNr=?1 AND o.finanzamtIId =?2 " +
					"AND o.kontoIId=?3 "),
})

@Entity
@Table(name = "FB_KONTOLAENDERART")
public class Kontolaenderart implements Serializable {
//	@EmbeddedId
//	private KontolaenderartPK pk;

	@Id
	@Column(name = "I_ID")
	private Integer iId;
	
	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "KONTO_I_ID_UEBERSETZT")
	private Integer kontoIIdUebersetzt;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;

	@Column(name = "KONTO_I_ID")
	private Integer kontoIId;

	@Column(name = "LAENDERART_C_NR")
	private String laenderartCNr;

	@Column(name = "FINANZAMT_I_ID")
	private Integer finanzamtIId;
	
	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;

	@Column(name = "REVERSECHARGEART_I_ID")
	private Integer reversechargeartId ;
	
	@Column(name = "T_GUELTIGAB")
	private Timestamp tGueltigAb;
		
	
	private static final long serialVersionUID = 1L;

	public Kontolaenderart() {
		super();
	}

	public Kontolaenderart(Integer iId) {
		this.iId = iId;
	}
	
	public Kontolaenderart(Integer kontoIId, String laenderartCNr, Integer finanzamtIId, 
			String mandantCNr, Integer reversechargeartId, Integer kontoIIdUebersetzt, Integer personalIIdAnlegen,
			Integer personalIIdAendern, Timestamp gueltigAb) {
//		setPk(new KontolaenderartPK(kontoIId, laenderartCNr, finanzamtIId, mandantCNr, reversechargeartId));
		setKontoIId(kontoIId);
		setLaenderartCNr(laenderartCNr);
		setFinanzamtIId(finanzamtIId);
		setMandantCNr(mandantCNr);
		setReversechargeartId(reversechargeartId);
		setKontoIIdUebersetzt(kontoIIdUebersetzt);
		setTGueltigAb(gueltigAb);
		setPersonalIIdAnlegen(personalIIdAnlegen);
		setPersonalIIdAendern(personalIIdAendern);
		// Setzen der NOT NULL felder
		Timestamp now = new Timestamp(System.currentTimeMillis());
		this.setTAendern(now);
		this.setTAnlegen(now);
	}

//	public KontolaenderartPK getPk() {
//		return this.pk;
//	}
//
//	public void setPk(KontolaenderartPK pk) {
//		this.pk = pk;
//	}

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

	public Integer getKontoIIdUebersetzt() {
		return this.kontoIIdUebersetzt;
	}

	public void setKontoIIdUebersetzt(Integer kontoIIdUebersetzt) {
		this.kontoIIdUebersetzt = kontoIIdUebersetzt;
	}

	public Integer getKontoIId() {
		return this.kontoIId;
	}

	public void setKontoIId(Integer kontoIId) {
		this.kontoIId = kontoIId;
	}

	public String getLaenderartCNr() {
		return this.laenderartCNr;
	}

	public void setLaenderartCNr(String laenderartCNr) {
		this.laenderartCNr = laenderartCNr;
	}

	public void setFinanzamtIId(Integer finanzamtIId) {
		this.finanzamtIId = finanzamtIId;
	}

	public Integer getFinanzamtIId() {
		return finanzamtIId;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public String getMandantCNr() {
		return mandantCNr;
	}

	
	public Integer getReversechargeartId() {
		return reversechargeartId ;
	}
	
	public void setReversechargeartId(Integer reversechargeartId) {
		this.reversechargeartId = reversechargeartId ;
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
	
	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Timestamp getTGueltigAb() {
		return tGueltigAb;
	}
	
	public void setTGueltigAb(Timestamp gueltigAb) {
		Validator.notNull(gueltigAb, "gueltigAb");
		tGueltigAb = gueltigAb;
	}
}
