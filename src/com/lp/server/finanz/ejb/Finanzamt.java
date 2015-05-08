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
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
		@NamedQuery(name = "FinanzamtfindAll", query = "SELECT OBJECT(o) FROM Finanzamt o"),
		@NamedQuery(name = "FinanzamtfindByMandantCNr", query = "SELECT OBJECT(o) FROM Finanzamt o WHERE o.mandantCNr = ?1"),
		@NamedQuery(name = "FinanzamtfindByPartnerIIdMandantCNr", query = "SELECT OBJECT(O) FROM Finanzamt o WHERE o.partnerIId=?1 AND o.mandantCNr = ?2") })
@Entity
@Table(name = "LP_FINANZAMT")
public class Finanzamt implements Serializable {
	@EmbeddedId
	private FinanzamtPK pk;

	@Column(name = "C_STEUERNUMMER")
	private String cSteuernummer;

	@Column(name = "C_REFERAT")
	private String cReferat;

	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "MANDANT_C_NR", insertable = false, updatable = false)
	private String mandantCNr;

	@Column(name = "PARTNER_I_ID", insertable = false, updatable = false)
	private Integer partnerIId;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;

	@Column(name = "I_FORMULARNUMMER")
	private Integer iFormularnummer;

	@Column(name = "B_UMSATZRUNDEN")
	private Short bUmsatzrunden;

	@Column(name = "KONTO_I_ID_EBSACHKONTEN")
	private Integer kontoIIdEbsachkonten;

	@Column(name = "KONTO_I_ID_EBDEBITOREN")
	private Integer kontoIIdEbdebitoren;
	
	@Column(name = "KONTO_I_ID_EBKREDITOREN")
	private Integer kontoIIdEbkreditoren;


	@Column(name = "KONTO_I_ID_ANZAHLUNG_ERHALT_VERR")
	private Integer kontoIIdAnzahlungErhaltenVerr;
	@Column(name = "KONTO_I_ID_ANZAHLUNG_ERHALT")
	private Integer kontoIIdAnzahlungErhaltenBezahlt;
	
	@Column(name = "KONTO_I_ID_ANZAHLUNG_GELEIST_VERR")
	private Integer kontoIIdAnzahlungGegebenVerr;
	@Column(name = "KONTO_I_ID_ANZAHLUNG_GELEIST")
	private Integer kontoIIdAnzahlungGegebenBeazhlt;

	@Column(name = "KONTO_I_ID_RC_ANZAHLUNG_GEGEBEN_VERR")
	private Integer kontoIIdRCAnzahlungGegebenVerr;
	@Column(name = "KONTO_I_ID_RC_ANZAHLUNG_GEGEBEN_BEZA")
	private Integer kontoIIdRCAnzahlungGegebenBezahlt;
	
	@Column(name = "KONTO_I_ID_RC_ANZAHLUNG_ERHALT_VERR")
	private Integer kontoIIdRCAnzahlungErhaltenVerr;
	@Column(name = "KONTO_I_ID_RC_ANZAHLUNG_ERHALT_BEZA")
	private Integer kontoIIdRCAnzahlungErhaltenBezahlt;

	public Integer getKontoIIdRCAnzahlungGegebenVerr() {
		return kontoIIdRCAnzahlungGegebenVerr;
	}

	public void setKontoIIdRCAnzahlungGegebenVerr(
			Integer kontoIIdRCAnzahlungGegebenVerr) {
		this.kontoIIdRCAnzahlungGegebenVerr = kontoIIdRCAnzahlungGegebenVerr;
	}

	public Integer getKontoIIdRCAnzahlungGegebenBezahlt() {
		return kontoIIdRCAnzahlungGegebenBezahlt;
	}

	public void setKontoIIdRCAnzahlungGegebenBezahlt(
			Integer kontoIIdRCAnzahlungGegebenBezahlt) {
		this.kontoIIdRCAnzahlungGegebenBezahlt = kontoIIdRCAnzahlungGegebenBezahlt;
	}

	public Integer getKontoIIdRCAnzahlungErhaltenVerr() {
		return kontoIIdRCAnzahlungErhaltenVerr;
	}

	public void setKontoIIdRCAnzahlungErhaltenVerr(
			Integer kontoIIdRCAnzahlungErhaltenVerr) {
		this.kontoIIdRCAnzahlungErhaltenVerr = kontoIIdRCAnzahlungErhaltenVerr;
	}

	public Integer getKontoIIdRCAnzahlungErhaltenBezahlt() {
		return kontoIIdRCAnzahlungErhaltenBezahlt;
	}

	public void setKontoIIdRCAnzahlungErhaltenBezahlt(
			Integer kontoIIdRCAnzahlungErhaltenBeza) {
		this.kontoIIdRCAnzahlungErhaltenBezahlt = kontoIIdRCAnzahlungErhaltenBeza;
	}

	public Integer getKontoIIdEbsachkonten() {
		return kontoIIdEbsachkonten;
	}
	
	public void setKontoIIdEbsachkonten(Integer kontoIIdEbsachkonten) {
		this.kontoIIdEbsachkonten = kontoIIdEbsachkonten;
	}

	public Integer getKontoIIdEbdebitoren() {
		return kontoIIdEbdebitoren;
	}

	public void setKontoIIdEbdebitoren(Integer kontoIIdEbdebitoren) {
		this.kontoIIdEbdebitoren = kontoIIdEbdebitoren;
	}

	public Integer getKontoIIdEbkreditoren() {
		return kontoIIdEbkreditoren;
	}

	public void setKontoIIdEbkreditoren(Integer kontoIIdEbkreditoren) {
		this.kontoIIdEbkreditoren = kontoIIdEbkreditoren;
	}

	
	public Integer getIFormularnummer() {
		return iFormularnummer;
	}

	public void setIFormularnummer(Integer iFormularnummer) {
		this.iFormularnummer = iFormularnummer;
	}

	private static final long serialVersionUID = 1L;

	public Finanzamt() {
		super();
	}

	public Finanzamt(Integer partnerIId, java.lang.String mandantCNr, Short bUmsatzRunden,
			Integer personalIIdAnlegen, Integer personalIIdAendern) {
		FinanzamtPK finanzamtPK = new FinanzamtPK(partnerIId, mandantCNr);
		setPk(finanzamtPK);
		setPartnerIId(partnerIId);
		setMandantCNr(mandantCNr);
		setBUmsatzRunden(bUmsatzRunden);
		// Setzen der NOT NULL Felder
		Timestamp t = new Timestamp(System.currentTimeMillis());
		setTAendern(t);
		setTAnlegen(t);
		setPersonalIIdAnlegen(personalIIdAnlegen);
		setPersonalIIdAendern(personalIIdAendern);
	}

	public FinanzamtPK getPk() {
		return this.pk;
	}

	public void setPk(FinanzamtPK pk) {
		this.pk = pk;
	}

	public String getCSteuernummer() {
		return this.cSteuernummer;
	}

	public void setCSteuernummer(String cSteuernummer) {
		this.cSteuernummer = cSteuernummer;
	}

	public String getCReferat() {
		return this.cReferat;
	}

	public void setCReferat(String cReferat) {
		this.cReferat = cReferat;
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

	public String getMandantCNr() {
		return this.mandantCNr;
	}

	public void setMandantCNr(String mandant) {
		this.mandantCNr = mandant;
	}

	public Integer getPartnerIId() {
		return this.partnerIId;
	}

	public void setPartnerIId(Integer partnerIId) {
		this.partnerIId = partnerIId;
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

	public void setBUmsatzRunden(Short bUmsatzrunden) {
		this.bUmsatzrunden = bUmsatzrunden;
	}

	public Short getBUmsatzRunden() {
		return bUmsatzrunden;
	}

	public void setKontoIIdAnzahlungErhaltenVerr(
			Integer kontoIIdAnzahlungErhaltenVerr) {
		this.kontoIIdAnzahlungErhaltenVerr = kontoIIdAnzahlungErhaltenVerr;
	}

	public Integer getKontoIIdAnzahlungErhaltenVerr() {
		return kontoIIdAnzahlungErhaltenVerr;
	}

	public void setKontoIIdAnzahlungErhaltenBezahlt(Integer kontoIIdAnzahlungErhaltenBezahlt) {
		this.kontoIIdAnzahlungErhaltenBezahlt = kontoIIdAnzahlungErhaltenBezahlt;
	}

	public Integer getKontoIIdAnzahlungErhaltenBezahlt() {
		return kontoIIdAnzahlungErhaltenBezahlt;
	}

	public void setKontoIIdAnzahlungGegebenVerr(
			Integer kontoIIdAnzahlungGegebenVerr) {
		this.kontoIIdAnzahlungGegebenVerr = kontoIIdAnzahlungGegebenVerr;
	}

	public Integer getKontoIIdAnzahlungGegebenVerr() {
		return kontoIIdAnzahlungGegebenVerr;
	}

	public void setKontoIIdAnzahlungGegebenBezahlt(Integer kontoIIdAnzahlungGegebenBeazhlt) {
		this.kontoIIdAnzahlungGegebenBeazhlt = kontoIIdAnzahlungGegebenBeazhlt;
	}

	public Integer getKontoIIdAnzahlungGegebenBezahlt() {
		return kontoIIdAnzahlungGegebenBeazhlt;
	}

}
