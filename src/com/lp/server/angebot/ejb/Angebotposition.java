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
package com.lp.server.angebot.ejb;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.lp.server.util.IISort;
import com.lp.server.util.IPositionIIdArtikelset;
import com.lp.server.util.IZwsPosition;

@NamedQueries({
		@NamedQuery(name = "AngebotpositionejbSelectMaxISort", query = "SELECT MAX (o.iSort) FROM Angebotposition o WHERE o.angebotIId=?1"),
		@NamedQuery(name = "AngebotpositionfindByAngebotIId", query = "SELECT OBJECT (o) FROM Angebotposition o WHERE o.angebotIId=?1 ORDER BY o.iSort"),
		@NamedQuery(name = "AngebotpositionfindByAngebotIIdOhneAlternative", query = "SELECT OBJECT (o) FROM Angebotposition o WHERE o.angebotIId=?1 AND o.bAlternative=0 ORDER BY o.iSort"),
		@NamedQuery(name = "AngebotpositionfindByAngebotIIdAngebotpositionsartCNr", query = "SELECT OBJECT (o) FROM Angebotposition o WHERE o.angebotIId=?1 AND o.angebotpositionartCNr=?2 ORDER BY o.iSort"),
		@NamedQuery(name = "AngebotpositionfindByPositionIId", query = "SELECT OBJECT (o) FROM Angebotposition o WHERE o.positionIId=?1 AND o.nMenge IS NOT NULL AND o.nMenge>0"),
		@NamedQuery(name = "AngebotpositionpositionfindByPositionIIdArtikelset", query = "SELECT OBJECT (o) FROM Angebotposition o WHERE o.positionIIdArtikelset=?1 AND o.nMenge IS NOT NULL AND o.nMenge>0 ORDER BY o.iSort"),
		@NamedQuery(name = "getWertAngebotpositionartPosition", query = "SELECT sum(o.nMenge*o.nNettogesamtpreisplusversteckteraufschlag) FROM Angebotposition o WHERE o.positionIId=?1 AND o.angebotpositionartCNr IN (?2,?3)"),
		@NamedQuery(name = "AngebotpositionfindByAngebotIIdISort", query = "SELECT OBJECT (o) FROM Angebotposition o WHERE o.angebotIId=?1 AND o.iSort=?2"),
		@NamedQuery(name = Angebotposition.QueryFindIZwsByVonBisIId, query = "SELECT OBJECT (o) FROM Angebotposition o WHERE o.zwsVonPosition= :iid OR o.zwsBisPosition= :iid") })
@Entity
@Table(name = "ANGB_ANGEBOTPOSITION")
public class Angebotposition implements Serializable, IISort, IPositionIIdArtikelset, IZwsPosition  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4167625547868900948L;
	public final static String QueryFindIZwsByVonBisIId = "AngebotpositionFindIZwsByVonBisIId" ;

	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "I_SORT")
	private Integer iSort;

	@Column(name = "C_BEZ")
	private String cBez;

	@Column(name = "N_GESTEHUNGSPREIS")
	private BigDecimal nGestehungspreis;

	@Column(name = "X_TEXTINHALT")
	private String xTextinhalt;

	@Column(name = "N_MENGE")
	private BigDecimal nMenge;

	@Column(name = "F_RABATTSATZ")
	private Double fRabattsatz;

	@Column(name = "B_RABATTSATZUEBERSTEUERT")
	private Short bRabattsatzuebersteuert;

	@Column(name = "B_MWSTSATZUEBERSTEUERT")
	private Short bMwstsatzuebersteuert;

	@Column(name = "N_NETTOEINZELPREIS")
	private BigDecimal nNettoeinzelpreis;

	@Column(name = "N_NETTOEINZELPREISPLUSVERSTECKTERAUFSCHLAG")
	private BigDecimal nNettoeinzelpreisplusversteckteraufschlag;

	@Column(name = "N_RABATTBETRAG")
	private BigDecimal nRabattbetrag;

	@Column(name = "N_NETTOGESAMTPREIS")
	private BigDecimal nNettogesamtpreis;

	@Column(name = "N_NETTOGESAMTPREISPLUSVERSTECKTERAUFSCHLAG")
	private BigDecimal nNettogesamtpreisplusversteckteraufschlag;

	@Column(name = "N_NETTOGESAMTPREISPLUSVERSTECKTERAUFSCHLAGMINUSRABATTE")
	private BigDecimal nNettogesamtpreisplusversteckteraufschlagminusrabatte;

	@Column(name = "N_MWSTBETRAG")
	private BigDecimal nMwstbetrag;

	@Column(name = "N_BRUTTOGESAMTPREIS")
	private BigDecimal nBruttogesamtpreis;

	@Column(name = "F_ZUSATZRABATTSATZ")
	private Double fZusatzrabattsatz;

	@Column(name = "N_GESAMTWERTAGSTKLINANGEBOTSWAEHRUNG")
	private BigDecimal nGesamtwertagstklinangebotswaehrung;

	@Column(name = "C_ZBEZ")
	private String cZbez;

	@Column(name = "B_ALTERNATIVE")
	private Short bAlternative;

	@Column(name = "POSITION_I_ID")
	private Integer positionIId;

	@Column(name = "TYP_C_NR")
	private String typCNr;

	@Column(name = "ANGEBOT_I_ID")
	private Integer angebotIId;
	
	@Column(name = "N_MATERIALZUSCHLAG")
	private BigDecimal nMaterialzuschlag;
	
	public BigDecimal getNMaterialzuschlag() {
		return nMaterialzuschlag;
	}

	public void setNMaterialzuschlag(BigDecimal materialzuschlag) {
		nMaterialzuschlag = materialzuschlag;
	}
	
	public Integer getVerleihIId() {
		return verleihIId;
	}

	public void setVerleihIId(Integer verleihIId) {
		this.verleihIId = verleihIId;
	}

	@Column(name = "VERLEIH_I_ID")
	private Integer verleihIId;

	@Column(name = "KOSTENTRAEGER_I_ID")
	private Integer kostentraegerIId;
	
	
	public Integer getKostentraegerIId() {
		return kostentraegerIId;
	}

	public void setKostentraegerIId(Integer kostentraegerIId) {
		this.kostentraegerIId = kostentraegerIId;
	}

	@Column(name = "ANGEBOTPOSITIONART_C_NR")
	private String angebotpositionartCNr;

	@Column(name = "AGSTKL_I_ID")
	private Integer agstklIId;

	@Column(name = "EINHEIT_C_NR")
	private String einheitCNr;

	@Column(name = "MEDIASTANDARD_I_ID")
	private Integer mediastandardIId;

	@Column(name = "MWSTSATZ_I_ID")
	private Integer mwstsatzIId;

	@Column(name = "ARTIKEL_I_ID")
	private Integer artikelIId;

	@Column(name = "B_NETTOPREISUEBERSTEUERT")
	private Short bNettopreisuebersteuert;

	@Column(name = "POSITION_I_ID_ARTIKELSET")
	private Integer positionIIdArtikelset;

	@Column(name = "ZWSVONPOSITION_I_ID")
	private Integer zwsVonPosition ;

	@Column(name = "ZWSBISPOSITION_I_ID")
	private Integer zwsBisPosition ;
	
	@Column(name = "N_ZWSNETTOSUMME") 
	private BigDecimal zwsNettoSumme ;

	
	public Integer getPositionIIdArtikelset() {
		return positionIIdArtikelset;
	}

	@Column(name = "C_LVPOSITION")
	private String cLvposition;
	
	public String getCLvposition() {
		return cLvposition;
	}

	public void setCLvposition(String cLvposition) {
		this.cLvposition = cLvposition;
	}

	public void setPositionIIdArtikelset(Integer positionIIdArtikelset) {
		this.positionIIdArtikelset = positionIIdArtikelset;
	}

	public Angebotposition(Integer iId, Integer angebotIId, Integer iSort,
			String angebotpositionartCNr, BigDecimal nGestehungspreis,
			Integer mwstsatzIId, Short bNettopreisuebersteuert) {
		setIId(iId);
		setAngebotIId(angebotIId);
		setISort(iSort);
		setAngebotpositionartCNr(angebotpositionartCNr);
		setNGestehungspreis(nGestehungspreis);
		// CMP alle NOT NULL und default felder hier setzen
		setBRabattsatzuebersteuert(new Short((short) 0));
		setBMwstsatzuebersteuert(new Short((short) 0));
		setBAlternative(new Short((short) 0));
		setMwstsatzIId(mwstsatzIId);
		setBNettopreisuebersteuert(bNettopreisuebersteuert);
	}

	public Angebotposition(Integer iId, Integer angebotIId, Integer iSort,
			String angebotpositionartCNr, Short rabattsatzuebersteuert,
			Short mwstsatzuebersteuert, Short alternative) {
		setIId(iId);
		setAngebotIId(angebotIId);
		setISort(iSort);
		setAngebotpositionartCNr(angebotpositionartCNr);
		setBRabattsatzuebersteuert(rabattsatzuebersteuert);
		setBMwstsatzuebersteuert(mwstsatzuebersteuert);
		setBAlternative(alternative);
	}

	public Angebotposition() {

	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Integer getISort() {
		return this.iSort;
	}

	public void setISort(Integer iSort) {
		this.iSort = iSort;
	}

	public String getCBez() {
		return this.cBez;
	}

	public void setCBez(String cBez) {
		this.cBez = cBez;
	}

	public BigDecimal getNGestehungspreis() {
		return this.nGestehungspreis;
	}

	public void setNGestehungspreis(BigDecimal nGestehungspreis) {
		this.nGestehungspreis = nGestehungspreis;
	}

	public String getXTextinhalt() {
		return this.xTextinhalt;
	}

	public void setXTextinhalt(String xTextinhalt) {
		this.xTextinhalt = xTextinhalt;
	}

	public BigDecimal getNMenge() {
		return this.nMenge;
	}

	public void setNMenge(BigDecimal nMenge) {
		this.nMenge = nMenge;
	}

	public Double getFRabattsatz() {
		return this.fRabattsatz;
	}

	public void setFRabattsatz(Double fRabattsatz) {
		this.fRabattsatz = fRabattsatz;
	}

	public Short getBRabattsatzuebersteuert() {
		return this.bRabattsatzuebersteuert;
	}

	public void setBRabattsatzuebersteuert(Short bRabattsatzuebersteuert) {
		this.bRabattsatzuebersteuert = bRabattsatzuebersteuert;
	}

	public Short getBMwstsatzuebersteuert() {
		return this.bMwstsatzuebersteuert;
	}

	public void setBMwstsatzuebersteuert(Short bMwstsatzuebersteuert) {
		this.bMwstsatzuebersteuert = bMwstsatzuebersteuert;
	}

	public BigDecimal getNNettoeinzelpreis() {
		return this.nNettoeinzelpreis;
	}

	public void setNNettoeinzelpreis(BigDecimal nNettoeinzelpreis) {
		this.nNettoeinzelpreis = nNettoeinzelpreis;
	}

	public BigDecimal getNNettoeinzelpreisplusversteckteraufschlag() {
		return this.nNettoeinzelpreisplusversteckteraufschlag;
	}

	public void setNNettoeinzelpreisplusversteckteraufschlag(
			BigDecimal nNettoeinzelpreisplusversteckteraufschlag) {
		this.nNettoeinzelpreisplusversteckteraufschlag = nNettoeinzelpreisplusversteckteraufschlag;
	}

	public BigDecimal getNRabattbetrag() {
		return this.nRabattbetrag;
	}

	public void setNRabattbetrag(BigDecimal nRabattbetrag) {
		this.nRabattbetrag = nRabattbetrag;
	}

	public BigDecimal getNNettogesamtpreis() {
		return this.nNettogesamtpreis;
	}

	public void setNNettogesamtpreis(BigDecimal nNettogesamtpreis) {
		this.nNettogesamtpreis = nNettogesamtpreis;
	}

	public BigDecimal getNNettogesamtpreisplusversteckteraufschlag() {
		return this.nNettogesamtpreisplusversteckteraufschlag;
	}

	public void setNNettogesamtpreisplusversteckteraufschlag(
			BigDecimal nNettogesamtpreisplusversteckteraufschlag) {
		this.nNettogesamtpreisplusversteckteraufschlag = nNettogesamtpreisplusversteckteraufschlag;
	}

	public BigDecimal getNNettogesamtpreisplusversteckteraufschlagminusrabatte() {
		return this.nNettogesamtpreisplusversteckteraufschlagminusrabatte;
	}

	public void setNNettogesamtpreisplusversteckteraufschlagminusrabatte(
			BigDecimal nNettogesamtpreisplusversteckteraufschlagminusrabatte) {
		this.nNettogesamtpreisplusversteckteraufschlagminusrabatte = nNettogesamtpreisplusversteckteraufschlagminusrabatte;
	}

	public BigDecimal getNMwstbetrag() {
		return this.nMwstbetrag;
	}

	public void setNMwstbetrag(BigDecimal nMwstbetrag) {
		this.nMwstbetrag = nMwstbetrag;
	}

	public BigDecimal getNBruttogesamtpreis() {
		return this.nBruttogesamtpreis;
	}

	public void setNBruttogesamtpreis(BigDecimal nBruttogesamtpreis) {
		this.nBruttogesamtpreis = nBruttogesamtpreis;
	}

	public Double getFZusatzrabattsatz() {
		return this.fZusatzrabattsatz;
	}

	public void setFZusatzrabattsatz(Double fZusatzrabattsatz) {
		this.fZusatzrabattsatz = fZusatzrabattsatz;
	}

	public BigDecimal getNGesamtwertagstklinangebotswaehrung() {
		return this.nGesamtwertagstklinangebotswaehrung;
	}

	public void setNGesamtwertagstklinangebotswaehrung(
			BigDecimal nGesamtwertagstklinangebotswaehrung) {
		this.nGesamtwertagstklinangebotswaehrung = nGesamtwertagstklinangebotswaehrung;
	}

	public String getCZbez() {
		return this.cZbez;
	}

	public void setCZbez(String cZbez) {
		this.cZbez = cZbez;
	}

	public Short getBAlternative() {
		return this.bAlternative;
	}

	public void setBAlternative(Short bAlternative) {
		this.bAlternative = bAlternative;
	}

	public Integer getPositionIId() {
		return this.positionIId;
	}

	public void setPositionIId(Integer positionIId) {
		this.positionIId = positionIId;
	}

	public String getTypCNr() {
		return this.typCNr;
	}

	public void setTypCNr(String typCNr) {
		this.typCNr = typCNr;
	}

	public Integer getAngebotIId() {
		return this.angebotIId;
	}

	public void setAngebotIId(Integer angebotIId) {
		this.angebotIId = angebotIId;
	}

	public String getAngebotpositionartCNr() {
		return this.angebotpositionartCNr;
	}

	public void setAngebotpositionartCNr(String angebotpositionartCNr) {
		this.angebotpositionartCNr = angebotpositionartCNr;
	}

	public Integer getAgstklIId() {
		return this.agstklIId;
	}

	public void setAgstklIId(Integer agstklIId) {
		this.agstklIId = agstklIId;
	}

	public String getEinheitCNr() {
		return this.einheitCNr;
	}

	public void setEinheitCNr(String einheitCNr) {
		this.einheitCNr = einheitCNr;
	}

	public Integer getMediastandardIId() {
		return this.mediastandardIId;
	}

	public void setMediastandardIId(Integer mediastandardIId) {
		this.mediastandardIId = mediastandardIId;
	}

	public Integer getMwstsatzIId() {
		return this.mwstsatzIId;
	}

	public void setMwstsatzIId(Integer mwstsatzIId) {
		this.mwstsatzIId = mwstsatzIId;
	}

	public Integer getArtikelIId() {
		return this.artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

	public Short getBNettopreisuebersteuert() {
		return this.bNettopreisuebersteuert;
	}

	public void setBNettopreisuebersteuert(Short bNettopreisuebersteuert) {
		this.bNettopreisuebersteuert = bNettopreisuebersteuert;
	}

	public Integer getZwsVonPosition() {
		return zwsVonPosition;
	}

	public void setZwsVonPosition(Integer zwsVonPosition) {
		this.zwsVonPosition = zwsVonPosition;
	}

	public Integer getZwsBisPosition() {
		return zwsBisPosition;
	}

	public void setZwsBisPosition(Integer zwsBisPosition) {
		this.zwsBisPosition = zwsBisPosition;
	}

	public BigDecimal getZwsNettoSumme() {
		return zwsNettoSumme;
	}

	public void setZwsNettoSumme(BigDecimal zwsNettoSumme) {
		this.zwsNettoSumme = zwsNettoSumme;
	}	
}
