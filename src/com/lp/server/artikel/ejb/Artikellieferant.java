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
package com.lp.server.artikel.ejb;

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
		@NamedQuery(name = "ArtikellieferantfindByArtikellIIdLieferantIId", query = "SELECT OBJECT(o) FROM Artikellieferant o WHERE o.artikelIId = ?1 AND o.lieferantIId = ?2"),
		@NamedQuery(name = "ArtikellieferantfindByArtikellIIdLieferantIIdTPreisgueltigab", query = "SELECT OBJECT(o) FROM Artikellieferant o WHERE o.artikelIId = ?1 AND o.lieferantIId = ?2 AND o.tPreisgueltigab = ?3"),
		@NamedQuery(name = "ArtikellieferantfindByArtikellIIdLieferantIIdTPreisgueltigabKleiner", query = "SELECT OBJECT(o) FROM Artikellieferant o WHERE o.artikelIId = ?1 AND o.lieferantIId = ?2 AND o.tPreisgueltigab <= ?3 ORDER BY o.tPreisgueltigab DESC"),
		@NamedQuery(name = "ArtikellieferantfindByArtikelIId", query = "SELECT OBJECT(o) FROM Artikellieferant o WHERE o.artikelIId = ?1 ORDER BY o.iSort ASC"),
		@NamedQuery(name = "ArtikellieferantfindByCArtikelnrlieferant", query = "SELECT OBJECT(o) FROM Artikellieferant o WHERE o.cArtikelnrlieferant = ?1"),
		@NamedQuery(name = "ArtikellieferantfindByCArtikelnrlieferantLieferantIId", query = "SELECT OBJECT(o) FROM Artikellieferant o WHERE o.cArtikelnrlieferant = ?1 AND o.lieferantIId= ?2"),
		@NamedQuery(name = "ArtikellieferantfindByArtikelIIdTPreisgueltigab", query = "SELECT OBJECT(o) FROM Artikellieferant o WHERE o.artikelIId = ?1 AND o.tPreisgueltigab <= ?2 ORDER BY o.iSort ASC"),
		@NamedQuery(name = "ArtikellieferantejbSelectNextReihung", query = "SELECT MAX (o.iSort) FROM Artikellieferant o WHERE o.artikelIId = ?1"),
		@NamedQuery(name = "ArtikellieferantfindByLieferantIId", query = "SELECT OBJECT(o) FROM Artikellieferant o WHERE o.lieferantIId = ?1") })
@Entity
@Table(name = "WW_ARTIKELLIEFERANT")
public class Artikellieferant implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_BEZBEILIEFERANT")
	private String cBezbeilieferant;

	@Column(name = "C_ARTIKELNRLIEFERANT")
	private String cArtikelnrlieferant;

	@Column(name = "B_HERSTELLERBEZ")
	private Short bHerstellerbez;

	@Column(name = "B_WEBSHOP")
	private Short bWebshop;

	@Column(name = "N_EINZELPREIS")
	private BigDecimal nEinzelpreis;

	@Column(name = "F_RABATT")
	private Double fRabatt;

	@Column(name = "N_NETTOPREIS")
	private BigDecimal nNettopreis;

	@Column(name = "F_STANDARDMENGE")
	private Double fStandardmenge;

	@Column(name = "F_MINDESTBESTELLMENGE")
	private Double fMindestbestellmenge;

	@Column(name = "EINHEIT_C_NR_VPE")
	private String einheitCNrVpe;

	
	public String getEinheitCNrVpe() {
		return einheitCNrVpe;
	}

	public void setEinheitCNrVpe(String einheitCNrVpe) {
		this.einheitCNrVpe = einheitCNrVpe;
	}

	@Column(name = "N_VERPACKUNGSEINHEIT")
	private BigDecimal nVerpackungseinheit;

	@Column(name = "N_FIXKOSTEN")
	private BigDecimal nFixkosten;

	@Column(name = "C_RABATTGRUPPE")
	private String cRabattgruppe;

	@Column(name = "T_PREISGUELTIGAB")
	private Timestamp tPreisgueltigab;
	
	@Column(name = "T_PREISGUELTIGBIS")
	private Timestamp tPreisgueltigbis;

	@Column(name = "C_ANGEBOTNUMMER")
	private String cAngebotnummer;
	
	@Column(name = "ZERTIFIKATART_I_ID")
	private Integer zertifikatartIId;
	
	@Column(name = "C_WEBLINK")
	private String cWeblink;
	
	public String getCWeblink() {
		return cWeblink;
	}

	public void setCWeblink(String cWeblink) {
		this.cWeblink = cWeblink;
	}

	public Integer getZertifikatartIId() {
		return zertifikatartIId;
	}

	public void setZertifikatartIId(Integer zertifikatartIId) {
		this.zertifikatartIId = zertifikatartIId;
	}

	public Timestamp getTPreisgueltigbis() {
		return tPreisgueltigbis;
	}

	public void setTPreisgueltigbis(Timestamp preisgueltigbis) {
		tPreisgueltigbis = preisgueltigbis;
	}

	public String getCAngebotnummer() {
		return cAngebotnummer;
	}

	public void setCAngebotnummer(String angebotnummer) {
		cAngebotnummer = angebotnummer;
	}

	@Column(name = "I_SORT")
	private Integer iSort;

	@Column(name = "I_WIEDERBESCHAFFUNGSZEIT")
	private Integer iWiederbeschaffungszeit;

	@Column(name = "LIEFERANT_I_ID")
	private Integer lieferantIId;

	@Column(name = "ARTIKEL_I_ID")
	private Integer artikelIId;

	@Column(name = "B_RABATTBEHALTEN")
	private Short bRabattbehalten;


	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;
	
	public Timestamp getTAendern() {
		return tAendern;
	}

	public void setTAendern(Timestamp aendern) {
		tAendern = aendern;
	}

	public Integer getPersonalIIdAendern() {
		return personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public Short getBRabattbehalten() {
		return this.bRabattbehalten;
	}

	public void setBRabattbehalten(Short bRabattbehalten) {
		this.bRabattbehalten = bRabattbehalten;
	}
	
	private static final long serialVersionUID = 1L;

	public Artikellieferant() {
		super();
	}

	public Artikellieferant(Integer artikelIId,
			Integer lieferantIId,
			Timestamp preisgueltigab,
			Integer sort, Integer id,Integer personalIIdAendern, Timestamp tAendern) {
		setArtikelIId(artikelIId);
		setBHerstellerbez(new Short((short) 0));
		setBRabattbehalten(new Short((short) 0));
		setBWebshop(new Short((short) 0));
		setIId(id);
		setLieferantIId(lieferantIId);
		setISort(sort);
		setTPreisgueltigab(preisgueltigab);
		setPersonalIIdAendern(personalIIdAendern);
		setTAendern(tAendern);
	}
	
	

	public Short getBWebshop() {
		return bWebshop;
	}

	public void setBWebshop(Short webshop) {
		bWebshop = webshop;
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCBezbeilieferant() {
		return this.cBezbeilieferant;
	}

	public void setCBezbeilieferant(String cBezbeilieferant) {
		this.cBezbeilieferant = cBezbeilieferant;
	}

	public String getCArtikelnrlieferant() {
		return this.cArtikelnrlieferant;
	}

	public void setCArtikelnrlieferant(String cArtikelnrlieferant) {
		this.cArtikelnrlieferant = cArtikelnrlieferant;
	}

	public Short getBHerstellerbez() {
		return this.bHerstellerbez;
	}

	public void setBHerstellerbez(Short bHerstellerbez) {
		this.bHerstellerbez = bHerstellerbez;
	}

	public BigDecimal getNEinzelpreis() {
		return this.nEinzelpreis;
	}

	public void setNEinzelpreis(BigDecimal nEinzelpreis) {
		this.nEinzelpreis = nEinzelpreis;
	}

	public Double getFRabatt() {
		return this.fRabatt;
	}

	public void setFRabatt(Double fRabatt) {
		this.fRabatt = fRabatt;
	}

	public BigDecimal getNNettopreis() {
		return this.nNettopreis;
	}

	public void setNNettopreis(BigDecimal nNettopreis) {
		this.nNettopreis = nNettopreis;
	}

	public Double getFStandardmenge() {
		return this.fStandardmenge;
	}

	public void setFStandardmenge(Double fStandardmenge) {
		this.fStandardmenge = fStandardmenge;
	}

	public Double getFMindestbestellmenge() {
		return this.fMindestbestellmenge;
	}

	public void setFMindestbestellmenge(Double fMindestbestellmenge) {
		this.fMindestbestellmenge = fMindestbestellmenge;
	}

	public BigDecimal getNVerpackungseinheit() {
		return this.nVerpackungseinheit;
	}

	public void setNVerpackungseinheit(BigDecimal nVerpackungseinheit) {
		this.nVerpackungseinheit = nVerpackungseinheit;
	}

	public BigDecimal getNFixkosten() {
		return this.nFixkosten;
	}

	public void setNFixkosten(BigDecimal nFixkosten) {
		this.nFixkosten = nFixkosten;
	}

	public String getCRabattgruppe() {
		return this.cRabattgruppe;
	}

	public void setCRabattgruppe(String cRabattgruppe) {
		this.cRabattgruppe = cRabattgruppe;
	}

	public Timestamp getTPreisgueltigab() {
		return this.tPreisgueltigab;
	}

	public void setTPreisgueltigab(Timestamp tPreisgueltigab) {
		this.tPreisgueltigab = tPreisgueltigab;
	}

	public Integer getISort() {
		return this.iSort;
	}

	public void setISort(Integer iSort) {
		this.iSort = iSort;
	}

	public Integer getIWiederbeschaffungszeit() {
		return this.iWiederbeschaffungszeit;
	}

	public void setIWiederbeschaffungszeit(Integer iWiederbeschaffungszeit) {
		this.iWiederbeschaffungszeit = iWiederbeschaffungszeit;
	}

	public Integer getLieferantIId() {
		return this.lieferantIId;
	}

	public void setLieferantIId(Integer lieferantIId) {
		this.lieferantIId = lieferantIId;
	}

	public Integer getArtikelIId() {
		return this.artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

}
