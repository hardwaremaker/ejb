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
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries( 
		{ @NamedQuery(name = "BuchungResetUvaverprobung", query = "UPDATE Buchung o SET o.uvaverprobungIId=null WHERE o.uvaverprobungIId=?1"),
		  @NamedQuery(name = Buchung.QueryBuchungfindByGeschaeftsjahrDatumAutomatik, query = "SELECT OBJECT(o) FROM Buchung o WHERE o.iGeschaeftsjahr=:geschaeftsjahr" +
		  		" AND o.tBuchungsdatum=:buchungsDatum AND o.bAutomatischeBuchung=1 AND o.tStorniert IS NULL ORDER BY o.iId"),
		  @NamedQuery(name = Buchung.QueryBuchungfindByGeschaeftsjahrDatumAutomatikGV, query = "SELECT OBJECT(o) FROM Buchung o WHERE o.iGeschaeftsjahr=:geschaeftsjahr" +
			  		" AND o.tBuchungsdatum=:buchungsDatum AND o.bAutomatischeBuchungGV=1 AND o.tStorniert IS NULL ORDER BY o.iId") })

@Entity
@Table(name = "FB_BUCHUNG")
public class Buchung implements Serializable {
	private static final long serialVersionUID = -2258915296828518861L;

	public static final String QueryBuchungfindByGeschaeftsjahrDatumAutomatik = "QueryBuchungfindByGeschaeftsjahrDatumAutomatik";
	public static final String QueryParameterGeschaeftsjahr = "geschaeftsjahr";
	public static final String QueryParameterBuchungsDatum = "buchungsDatum";
	public static final String QueryBuchungfindByGeschaeftsjahrDatumAutomatikGV = "QueryBuchungfindByGeschaeftsjahrDatumAutomatikGV";

	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "BUCHUNGSART_C_NR")
	private String buchungsartCNr;

	@Column(name = "T_BUCHUNGSDATUM")
	private Date tBuchungsdatum;

	@Column(name = "C_TEXT")
	private String cText;

	@Column(name = "KOSTENSTELLE_I_ID")
	private Integer kostenstelleIId;

	@Column(name = "C_BELEGNUMMER")
	private String cBelegnummer;

	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "GESCHAEFTSJAHR_I_GESCHAEFTSJAHR")
	private Integer iGeschaeftsjahr;

	@Column(name = "T_STORNIERT")
	private Timestamp tStorniert;
	
	@Column(name = "PERSONAL_I_ID_STORNIERT")
	private Integer personalIIdStorniert;
	
	@Column(name = "UVAVERPROBUNG_I_ID")
	private Integer uvaverprobungIId;

	@Column(name = "BELEGART_C_NR")
	private String belegartCNr;
	
	@Column(name = "B_AUTOMBUCHUNG")
	private Short bAutomatischeBuchung ;
	
	@Column(name = "B_AUTOMBUCHUNG_EB")
	private Short bAutomatischeBuchungEB;

	@Column(name = "B_AUTOMBUCHUNG_GV")
	private Short bAutomatischeBuchungGV;

	public Buchung() {
		super();
	}

	public Buchung(Integer id, String buchungsartCNr, Date buchungsdatum,
			String text, Integer kostenstelleIId, String belegnummer,
			Integer personalIIdAnlegen, Integer personalIIdAendern,
			Integer geschaeftsjahr) {
		setIId(id);
		setBuchungsartCNr(buchungsartCNr);
		setTBuchungsdatum(buchungsdatum);
		setCText(text);
		setKostenstelleIId(kostenstelleIId);
		setCBelegnummer(belegnummer);
		setPersonalIIdAnlegen(personalIIdAnlegen);
		setPersonalIIdAendern(personalIIdAendern);
		// Setzen der NOT NULL felder
	    Timestamp now = new Timestamp(System.currentTimeMillis());
	    this.setTAendern(now);
	    this.setTAnlegen(now);
		setGeschaeftsjahr(geschaeftsjahr);
		
		setbAutomatischeBuchung((short)0) ;
		setbAutomatischeBuchungEB((short)0) ;
		setbAutomatischeBuchungGV((short)0) ;
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Date getTBuchungsdatum() {
		return this.tBuchungsdatum;
	}

	public void setTBuchungsdatum(Date tBuchungsdatum) {
		this.tBuchungsdatum = tBuchungsdatum;
	}

	public String getCText() {
		return this.cText;
	}

	public void setCText(String cText) {
		this.cText = cText;
	}

	public String getCBelegnummer() {
		return this.cBelegnummer;
	}

	public void setCBelegnummer(String cBelegnummer) {
		this.cBelegnummer = cBelegnummer;
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

	public String getBuchungsartCNr() {
		return this.buchungsartCNr;
	}

	public void setBuchungsartCNr(String buchungsartCNr) {
		this.buchungsartCNr = buchungsartCNr;
	}

	public Integer getGeschaeftsjahr() {
		return this.iGeschaeftsjahr;
	}

	public void setGeschaeftsjahr(Integer iGeschaeftsjahr) {
		this.iGeschaeftsjahr = iGeschaeftsjahr;
	}

	public Integer getKostenstelleIId() {
		return this.kostenstelleIId;
	}

	public void setKostenstelleIId(Integer kostenstelleIId) {
		this.kostenstelleIId = kostenstelleIId;
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

	public void setTStorniert(Timestamp tStorniert) {
		this.tStorniert = tStorniert;
	}

	public Timestamp getTStorniert() {
		return tStorniert;
	}

	public void setPersonalIIdStorniert(Integer personalIIdStorniert) {
		this.personalIIdStorniert = personalIIdStorniert;
	}

	public Integer getPersonalIIdStorniert() {
		return personalIIdStorniert;
	}

	public void setUvaverprobungIId(Integer uvaverprobungIId) {
		this.uvaverprobungIId = uvaverprobungIId;
	}

	public Integer getUvaverprobungIId() {
		return uvaverprobungIId;
	}

	public void setBelegartCNr(String belegartCNr) {
		this.belegartCNr = belegartCNr;
	}

	public String getBelegartCNr() {
		return belegartCNr;
	}

	public Short getbAutomatischeBuchung() {
		return bAutomatischeBuchung;
	}

	public void setbAutomatischeBuchung(Short bAutomatischeBuchung) {
		this.bAutomatischeBuchung = bAutomatischeBuchung;
	}

	public void setAutomatischeBuchung(boolean automatischeBuchung) {
		this.bAutomatischeBuchung = new Short(automatischeBuchung ? (short)1 : (short) 0);
	}
	
	public boolean isAutomatischeBuchung() {
		if(null == bAutomatischeBuchung) return false ;
		return 0 != bAutomatischeBuchung ;
	}
	
	public Short getbAutomatischeBuchungEB() {
		return bAutomatischeBuchungEB;
	}

	public void setbAutomatischeBuchungEB(Short bAutomatischeBuchungEB) {
		this.bAutomatischeBuchungEB = bAutomatischeBuchungEB;
	}

	public void setAutomatischeBuchungEB(boolean automatischeBuchung) {
		this.bAutomatischeBuchungEB = new Short(automatischeBuchung ? (short)1 : (short) 0);
	}
	
	public boolean isAutomatischeBuchungEB() {
		return 0 != bAutomatischeBuchungEB ;		
	}	

	public Short getbAutomatischeBuchungGV() {
		return bAutomatischeBuchungGV;
	}

	public void setbAutomatischeBuchungGV(Short bAutomatischeBuchungGV) {
		this.bAutomatischeBuchungGV = bAutomatischeBuchungGV;
	}

	public void setAutomatischeBuchungGV(boolean automatischeBuchungGV) {
		this.bAutomatischeBuchungGV = new Short(automatischeBuchungGV ? (short)1 : (short) 0);
	}
	
	public boolean isAutomatischeBuchungGV() {
		return 0 != bAutomatischeBuchungGV ;		
	}	

}
