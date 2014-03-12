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
package com.lp.server.bestellung.ejb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries( {
		@NamedQuery(name = "BSMahnungfindByBSMahnlaufIId", query = "SELECT OBJECT(o) FROM Bsmahnung o WHERE o.bsmahnlaufIId=?1"),
		@NamedQuery(name = "BSMahnungfindByBestellungIId", query = "SELECT OBJECT(o) FROM Bsmahnung o WHERE o.bestellungIId=?1 ORDER BY o.tMahndatum DESC"),
		@NamedQuery(name = "BSMahnungfindByBestellungBSMahnstufe", query = "SELECT OBJECT(o) FROM Bsmahnung o WHERE o.bestellungIId=?1 AND  o.bsmahnustufeIId=?2"),
		@NamedQuery(name = "BSMahnungfindByBestellposition", query = "SELECT OBJECT(o) FROM Bsmahnung o WHERE o.bestellpositionIId=?1"),
		@NamedQuery(name = "BSMahnungfindBymahnlaufIIdLieferantIIdMandantCNr", query = "SELECT OBJECT(o)  FROM Bsmahnung o, Bestellung x WHERE o.bestellungIId = x.iId AND x.lieferantIIdBestelladresse=?2 AND o.bsmahnlaufIId =?1 AND o.mandantCNr =?3 ") })
@Entity
@Table(name = "BES_BSMAHNUNG")
public class Bsmahnung implements Serializable {
	
	
	@Id
	@Column(name="I_ID")
	private Integer iID;
	
	@Column(name="MANDANT_C_NR")
	private String mandantCNr;

	@Column(name = "T_MAHNDATUM")
	private Date tMahndatum;

	@Column(name = "T_GEDRUCKT")
	private Timestamp tGedruckt;

	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "N_OFFENEMENGE")
	private BigDecimal nOffenemenge;

	@Column(name = "BESTELLPOSITION_I_ID")
	private Integer bestellpositionIId;

	@Column(name = "BESTELLUNG_I_ID")
	private Integer bestellungIId;

	@Column(name = "BSMAHNLAUF_I_ID")
	private Integer bsmahnlaufIId;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "PERSONAL_I_ID_ANLEGEN")
	private Integer personalIIdAnlegen;

	@Column(name = "PERSONAL_I_ID_GEDRUCKT")
	private Integer personalIIdGedruckt;
	
	@Column(name= "BSMAHNSTUFE_I_ID")
	private Integer bsmahnustufeIId;
	

	private static final long serialVersionUID = 1L;

	public Bsmahnung() {
		super();
	}

	public Bsmahnung(Integer id,
			String mandantCNr,
			Integer mahnlaufIId,
			Integer mahnstufeIId,
			Integer bestellungIId,
			Date mahndatum,
			Integer personalIIdAnlegen,
			Integer personalIIdAendern,
			Integer bestellpositionIId) {
		if(bestellungIId.equals(new Integer(40556))){
			int u=0;
		}
		setIId(id);
		setMandantCNr(mandantCNr);
		setBestellpositionIId(bestellpositionIId);
		setPersonalIIdAendern(personalIIdAendern);
		setPersonalIIdAnlegen(personalIIdAnlegen);
		setTMahndatum(mahndatum);
		setBestellungIId(bestellungIId);
		setMahnstufeIId(mahnstufeIId);
		setBsmahnlaufIId(mahnlaufIId);
		setTAendern(new Timestamp(System.currentTimeMillis()));
		setTAnlegen(new Timestamp(System.currentTimeMillis()));
	}

	public Integer getIId(){
		return this.iID;
	}
	
	public void setIId(Integer id){
		this.iID=id;
	}

	public Integer getMahnstufeIId() {
		return this.bsmahnustufeIId;
	}

	public void setMahnstufeIId(Integer mahnstufeIId) {
		this.bsmahnustufeIId = mahnstufeIId;
	}

	public Date getTMahndatum() {
		return this.tMahndatum;
	}

	public void setTMahndatum(Date tMahndatum) {
		this.tMahndatum = tMahndatum;
	}

	public Timestamp getTGedruckt() {
		return this.tGedruckt;
	}

	public void setTGedruckt(Timestamp tGedruckt) {
		this.tGedruckt = tGedruckt;
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

	public BigDecimal getNOffenemenge() {
		return this.nOffenemenge;
	}

	public void setNOffenemenge(BigDecimal nOffenemenge) {
		this.nOffenemenge = nOffenemenge;
	}

	public Integer getBestellpositionIId() {
		return this.bestellpositionIId;
	}

	public void setBestellpositionIId(Integer bestellpositionIId) {
		this.bestellpositionIId = bestellpositionIId;
	}

	public Integer getBestellungIId() {
		return this.bestellungIId;
	}

	public void setBestellungIId(Integer bestellungIId) {
		this.bestellungIId = bestellungIId;
	}

	public Integer getBsmahnlaufIId() {
		return this.bsmahnlaufIId;
	}

	public void setBsmahnlaufIId(Integer bsmahnlaufiid) {
		this.bsmahnlaufIId = bsmahnlaufiid;
	}

	public String getMandantCNr() {
		return this.mandantCNr;
	}

	public void setMandantCNr(String mandant) {
		this.mandantCNr = mandant;
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

	public Integer getPersonalIIdGedruckt() {
		return this.personalIIdGedruckt;
	}

	public void setPersonalIIdGedruckt(Integer personalIIdGedruckt) {
		this.personalIIdGedruckt = personalIIdGedruckt;
	}

}
