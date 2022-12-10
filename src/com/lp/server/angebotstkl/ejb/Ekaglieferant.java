
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
package com.lp.server.angebotstkl.ejb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
		@NamedQuery(name = "EkaglieferantFindByEinkaufsangebotIIdLieferantIId", query = "SELECT OBJECT (o) FROM Ekaglieferant o WHERE o.einkaufsangebotIId = ?1 AND o.lieferantIId = ?2"),
		@NamedQuery(name = "EkaglieferantFindByEinkaufsangebotIId", query = "SELECT OBJECT (o) FROM Ekaglieferant o WHERE o.einkaufsangebotIId = ?1")})
@Entity
@Table(name = "AS_EKAGLIEFERANT")
public class Ekaglieferant implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "EINKAUFSANGEBOT_I_ID")
	private Integer einkaufsangebotIId;

	@Column(name = "T_VERSAND")
	private Timestamp tVersand;
	
	public Timestamp getTVersand() {
		return tVersand;
	}

	public void setTVersand(Timestamp tVersand) {
		this.tVersand = tVersand;
	}

	@Column(name = "T_IMPORT")
	private Timestamp tImport;
	
	@Column(name = "C_ANGEBOTSNUMMER")
	private String cAngebotsnummer;

	@Column(name = "WAEHRUNG_C_NR")
	private String waehrungCNr;
	
	public String getWaehrungCNr() {
		return waehrungCNr;
	}

	public void setWaehrungCNr(String waehrungCNr) {
		this.waehrungCNr = waehrungCNr;
	}

	@Column(name = "N_AUFSCHLAG")
	private BigDecimal nAufschlag;
	
	public BigDecimal getNAufschlag() {
		return nAufschlag;
	}

	public void setNAufschlag(BigDecimal nAufschlag) {
		this.nAufschlag = nAufschlag;
	}

	public Timestamp getTImport() {
		return tImport;
	}

	public void setTImport(Timestamp tImport) {
		this.tImport = tImport;
	}

	public String getCAngebotsnummer() {
		return cAngebotsnummer;
	}

	public void setCAngebotsnummer(String cAngebotsnummer) {
		this.cAngebotsnummer = cAngebotsnummer;
	}

	public Integer getEinkaufsangebotIId() {
		return einkaufsangebotIId;
	}

	public void setEinkaufsangebotIId(Integer einkaufsangebotIId) {
		this.einkaufsangebotIId = einkaufsangebotIId;
	}

	public Integer getLieferantIId() {
		return lieferantIId;
	}

	public void setLieferantIId(Integer lieferantIId) {
		this.lieferantIId = lieferantIId;
	}

	@Column(name = "LIEFERANT_I_ID")
	private Integer lieferantIId;

	@Column(name = "ANSPRECHPARTNER_I_ID")
	private Integer ansprechpartnerIId;
	
	public Integer getAnsprechpartnerIId() {
		return ansprechpartnerIId;
	}

	public void setAnsprechpartnerIId(Integer ansprechpartnerIId) {
		this.ansprechpartnerIId = ansprechpartnerIId;
	}
	
	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	private static final long serialVersionUID = 1L;

	public Ekaglieferant() {

	}

	public Ekaglieferant(Integer iId, Integer einkaufsangebotIId, Integer lieferantIId, String waehrungCNr, BigDecimal nAufschlag) {
		setIId(iId);
		setWaehrungCNr(waehrungCNr);
		setEinkaufsangebotIId(einkaufsangebotIId);
		setLieferantIId(lieferantIId);
		setNAufschlag(nAufschlag);
	}

}
