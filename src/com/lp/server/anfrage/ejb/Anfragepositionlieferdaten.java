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
package com.lp.server.anfrage.ejb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries( {
		@NamedQuery(name = "AnfragepositionlieferdatenfindByAnfrageposition", query = "SELECT OBJECT (o) FROM Anfragepositionlieferdaten o WHERE o.anfragepositionIId=?1"),
		@NamedQuery(name = "AnfragepositionlieferdatenfindByAnfragepositionIIdErfasst", query = "SELECT OBJECT (o) FROM Anfragepositionlieferdaten o WHERE o.anfragepositionIId=?1 AND o.bErfasst=?2") })
@Entity
@Table(name = "ANF_ANFRAGEPOSITIONLIEFERDATEN")
public class Anfragepositionlieferdaten implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "I_ANLIEFERZEIT")
	private Integer iAnlieferzeit;

	@Column(name = "N_ANLIEFERMENGE")
	private BigDecimal nAnliefermenge;

	@Column(name = "N_NETTOGESAMTPREIS")
	private BigDecimal nNettogesamtpreis;

	@Column(name = "N_NETTOGESAMTPREISMINUSRABATT")
	private BigDecimal nNettogesamtpreisminusrabatt;

	@Column(name = "B_ERFASST")
	private Short bErfasst;

	@Column(name = "ANFRAGEPOSITION_I_ID")
	private Integer anfragepositionIId;

	@Column(name = "C_BEZBEILIEFERANT")
	private String cBezbeilieferant;
	@Column(name = "C_ARTIKELNRLIEFERANT")
	private String cArtikelnrlieferant;
	@Column(name = "N_VERPACKUNGSEINHEIT")
	private BigDecimal nVerpackungseinheit;
	@Column(name = "N_STANDARDMENGE")
	private BigDecimal nStandardmenge;
	
	@Column(name = "ZERTIFIKATART_I_ID")
	private Integer zertifikatartIId;
	
	public Integer getZertifikatartIId() {
		return zertifikatartIId;
	}

	public void setZertifikatartIId(Integer zertifikatartIId) {
		this.zertifikatartIId = zertifikatartIId;
	}

	@Column(name = "T_PREISGUELTIGAB")
	private Timestamp tPreisgueltigab;

	
	public Timestamp getTPreisgueltigab() {
		return tPreisgueltigab;
	}

	public void setTPreisgueltigab(Timestamp tPreisgueltigab) {
		this.tPreisgueltigab = tPreisgueltigab;
	}

	public String getCBezbeilieferant() {
		return cBezbeilieferant;
	}

	public void setCBezbeilieferant(String bezbeilieferant) {
		cBezbeilieferant = bezbeilieferant;
	}

	public String getCArtikelnrlieferant() {
		return cArtikelnrlieferant;
	}

	public void setCArtikelnrlieferant(String artikelnrlieferant) {
		cArtikelnrlieferant = artikelnrlieferant;
	}

	public BigDecimal getNVerpackungseinheit() {
		return nVerpackungseinheit;
	}

	public void setNVerpackungseinheit(BigDecimal verpackungseinheit) {
		nVerpackungseinheit = verpackungseinheit;
	}

	public BigDecimal getNStandardmenge() {
		return nStandardmenge;
	}

	public void setNStandardmenge(BigDecimal standardmenge) {
		nStandardmenge = standardmenge;
	}

	public BigDecimal getNMindestbestellmenge() {
		return nMindestbestellmenge;
	}

	public void setNMindestbestellmenge(BigDecimal mindestbestellmenge) {
		nMindestbestellmenge = mindestbestellmenge;
	}

	@Column(name = "N_MINDESTBESTELLMENGE")
	private BigDecimal nMindestbestellmenge;
	
	private static final long serialVersionUID = 1L;

	public Anfragepositionlieferdaten() {
		super();
	}

	public Anfragepositionlieferdaten(Integer idAnfragepositionlieferdaten,
			Integer anfragepositionIId, Integer anlieferzeit,
			BigDecimal anliefermenge, BigDecimal nettogesamtpreis) {
		setIId(idAnfragepositionlieferdaten);
		setAnfragepositionIId(anfragepositionIId);
		setIAnlieferzeit(anlieferzeit);
		setNAnliefermenge(anliefermenge);
		setBErfasst(new Short( (short) 0));
		setNNettogesamtpreis(nettogesamtpreis);
	}
	
	public Anfragepositionlieferdaten(Integer idAnfragepositionlieferdaten,
			Integer anfragepositionIId, Integer anlieferzeit,
			BigDecimal anliefermenge, BigDecimal nettogesamtpreis, Short erfasst) {
		setIId(idAnfragepositionlieferdaten);
		setAnfragepositionIId(anfragepositionIId);
		setIAnlieferzeit(anlieferzeit);
		setNAnliefermenge(anliefermenge);
		setBErfasst(erfasst);
		setNNettogesamtpreis(nettogesamtpreis);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getIAnlieferzeit() {
		return this.iAnlieferzeit;
	}

	public void setIAnlieferzeit(Integer iAnlieferzeit) {
		this.iAnlieferzeit = iAnlieferzeit;
	}

	public BigDecimal getNAnliefermenge() {
		return this.nAnliefermenge;
	}

	public void setNAnliefermenge(BigDecimal nAnliefermenge) {
		this.nAnliefermenge = nAnliefermenge;
	}

	public BigDecimal getNNettogesamtpreis() {
		return this.nNettogesamtpreis;
	}

	public void setNNettogesamtpreis(BigDecimal nNettogesamtpreis) {
		this.nNettogesamtpreis = nNettogesamtpreis;
	}

	public BigDecimal getNNettogesamtpreisminusrabatt() {
		return this.nNettogesamtpreisminusrabatt;
	}

	public void setNNettogesamtpreisminusrabatt(
			BigDecimal nNettogesamtpreisminusrabatt) {
		this.nNettogesamtpreisminusrabatt = nNettogesamtpreisminusrabatt;
	}

	public Short getBErfasst() {
		return this.bErfasst;
	}

	public void setBErfasst(Short bErfasst) {
		this.bErfasst = bErfasst;
	}

	public Integer getAnfragepositionIId() {
		return this.anfragepositionIId;
	}

	public void setAnfragepositionIId(Integer anfragepositionIId) {
		this.anfragepositionIId = anfragepositionIId;
	}

}
