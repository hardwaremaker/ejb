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
package com.lp.server.lieferschein.ejb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

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
		@NamedQuery(name = "LieferscheinpositionfindByLieferschein", query = "SELECT OBJECT(o) FROM Lieferscheinposition o WHERE o.lieferscheinIId=?1 ORDER BY o.iSort"),
		@NamedQuery(name = "LieferscheinpositionfindByLieferscheinIIdAuftragpositionIId", query = "SELECT OBJECT (o) FROM Lieferscheinposition o WHERE o.lieferscheinIId=?1 AND o.auftragpositionIId=?2"),
		@NamedQuery(name = "LieferscheinpositionfindPositiveByAuftragpositionIIdArtikelIId", query = "SELECT OBJECT (o) FROM Lieferscheinposition o WHERE o.auftragpositionIId=?1 AND o.artikelIId=?2 AND o.nMenge>0"),
		@NamedQuery(name = "LieferscheinpositionejbSelectMaxISort", query = "SELECT MAX (o.iSort) FROM Lieferscheinposition o WHERE o.lieferscheinIId = ?1"),
		@NamedQuery(name = "LieferscheinpositionfindByAuftragposition", query = "SELECT OBJECT (o) FROM Lieferscheinposition o WHERE o.auftragpositionIId=?1"),
		@NamedQuery(name = "LieferscheinpositionfindByLieferscheinMenge", query = "SELECT OBJECT (o) FROM Lieferscheinposition o WHERE o.lieferscheinIId = ?1 AND o.nMenge IS NOT NULL"),
		@NamedQuery(name = "LieferscheinpositionpositionfindByLieferscheinIIdLieferscheinpositionartCNr", query = "SELECT OBJECT (o) FROM Lieferscheinposition o WHERE o.lieferscheinIId=?1 AND o.lieferscheinpositionartCNr=?2 ORDER BY o.iSort"),
		@NamedQuery(name = "LieferscheinpositionfindByLieferscheinISort", query = "SELECT OBJECT (o) FROM Lieferscheinposition o WHERE o.lieferscheinIId = ?1 AND o.iSort = ?2"),
		@NamedQuery(name = "LieferscheinpositionfindByLieferscheinIIdPositionIId", query = "SELECT OBJECT (o) FROM Lieferscheinposition o WHERE o.lieferscheinIId = ?1 AND o.iId = ?2"),
		@NamedQuery(name = "LieferscheinpositionfindByPositionIId", query = "SELECT OBJECT (o) FROM Lieferscheinposition o WHERE o.positionIId=?1 AND o.nMenge IS NOT NULL AND o.nMenge>0"),
		@NamedQuery(name = "LieferscheinpositionfindByPositionIIdArtikelset", query = "SELECT OBJECT (o) FROM Lieferscheinposition o WHERE o.positionIIdArtikelset=?1 AND o.nMenge IS NOT NULL AND o.nMenge>0 ORDER BY o.iSort ASC"),
		@NamedQuery(name = "LieferscheinpositionfindByPositionartCNr", query = "SELECT OBJECT (o) FROM Lieferscheinposition o WHERE o.lieferscheinpositionartCNr=?1 AND o.nMenge IS NOT NULL"),
		@NamedQuery(name = "getWertLieferscheinpositionartPosition", query = "SELECT sum(o.nMenge*o.nNettogesamtpreisplusversteckteraufschlag) FROM Lieferscheinposition o WHERE o.positionIId=?1 AND o.lieferscheinpositionartCNr IN (?2,?3)"),
		@NamedQuery(name = "LieferscheinpositionfindByPositionIIdISort", query = "SELECT OBJECT (o) FROM Lieferscheinposition o WHERE o.positionIId = ?1 AND o.iSort = ?2"),
		@NamedQuery(name = Lieferscheinposition.QueryFindIZwsByVonBisIId, query = "SELECT OBJECT (o) FROM Lieferscheinposition o WHERE o.zwsVonPosition= :iid OR o.zwsBisPosition= :iid")})
@Entity
@Table(name = "LS_LIEFERSCHEINPOSITION")
public class Lieferscheinposition implements Serializable, IISort, IPositionIIdArtikelset, IZwsPosition  {

	public final static String QueryFindIZwsByVonBisIId = "LieferscheinpositionFindIZwsByVonBisIId" ;
	
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "I_SORT")
	private Integer iSort;

	@Column(name = "C_BEZ")
	private String cBez;

	@Column(name = "B_ARTIKELBEZEICHNUNGUEBERSTEUERT")
	private Short bArtikelbezeichnunguebersteuert;

	@Column(name = "X_TEXTINHALT")
	private String xTextinhalt;

	@Column(name = "N_MENGE")
	private BigDecimal nMenge;

	@Column(name = "F_RABATTSATZ")
	private Double fRabattsatz;

	@Column(name = "B_RABATTSATZUEBERSTEUERT")
	private Short bRabattsatzuebersteuert;

	@Column(name = "F_ZUSATZRABATTSATZ")
	private Double fZusatzrabattsatz;

	@Column(name = "F_KUPFERZUSCHLAG")
	private Double fKupferzuschlag;

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

	@Column(name = "N_NETTOGESAMTPREISPLUSVERSTECKTERAUFSCHLAGMINUSRABATT")
	private BigDecimal nNettogesamtpreisplusversteckteraufschlagminusrabatt;

	@Column(name = "N_MWSTBETRAG")
	private BigDecimal nMwstbetrag;

	@Column(name = "N_BRUTTOGESAMTPREIS")
	private BigDecimal nBruttogesamtpreis;

	@Column(name = "C_ZBEZ")
	private String cZbez;

	@Column(name = "POSITION_I_ID")
	private Integer positionIId;
	
	@Column(name = "LAGER_I_ID")
	private Integer lagerIId;

	public Integer getLagerIId() {
		return lagerIId;
	}

	public void setLagerIId(Integer lagerIId) {
		this.lagerIId = lagerIId;
	}

	@Column(name = "C_LVPOSITION")
	private String cLvposition;
	
	@Column(name = "N_MATERIALZUSCHLAG")
	private BigDecimal nMaterialzuschlag;
	
	public BigDecimal getNMaterialzuschlag() {
		return nMaterialzuschlag;
	}

	public void setNMaterialzuschlag(BigDecimal materialzuschlag) {
		nMaterialzuschlag = materialzuschlag;
	}
	@Column(name = "T_MATERIALZUSCHLAG_DATUM")
	private Timestamp tMaterialzuschlagDatum ;
	

	public Timestamp getTMaterialzuschlagDatum() {
		return tMaterialzuschlagDatum;
	}

	public void setTMaterialzuschlagDatum(Timestamp tMaterialzuschlagDatum) {
		this.tMaterialzuschlagDatum = tMaterialzuschlagDatum;
	}

	@Column(name = "N_MATERIALZUSCHLAG_KURS")
	private BigDecimal nMaterialzuschlagKurs ;

	
	public BigDecimal getNMaterialzuschlagKurs() {
		return nMaterialzuschlagKurs;
	}

	public void setNMaterialzuschlagKurs(BigDecimal nMaterialzuschlagKurs) {
		this.nMaterialzuschlagKurs = nMaterialzuschlagKurs;
	}
	public String getCLvposition() {
		return cLvposition;
	}

	public void setCLvposition(String cLvposition) {
		this.cLvposition = cLvposition;
	}
	
	@Column(name = "KOSTENTRAEGER_I_ID")
	private Integer kostentraegerIId;
		
	@Column(name = "ZWSVONPOSITION_I_ID")
	private Integer zwsVonPosition ;

	@Column(name = "ZWSBISPOSITION_I_ID")
	private Integer zwsBisPosition ;
	
	@Column(name = "N_ZWSNETTOSUMME") 
	private BigDecimal zwsNettoSumme ;
	
	@Column(name = "B_ZWSPOSITIONSPREISZEIGEN")
	private Short bZwsPositionspreisZeigen ;
	
	
	public Integer getKostentraegerIId() {
		return kostentraegerIId;
	}

	public void setKostentraegerIId(Integer kostentraegerIId) {
		this.kostentraegerIId = kostentraegerIId;
	}
	
	public Integer getVerleihIId() {
		return verleihIId;
	}

	public void setVerleihIId(Integer verleihIId) {
		this.verleihIId = verleihIId;
	}

	@Column(name = "VERLEIH_I_ID")
	private Integer verleihIId;

	@Column(name = "POSITION_I_ID_ARTIKELSET")
	private Integer positionIIdArtikelset;

	public Integer getPositionIIdArtikelset() {
		return positionIIdArtikelset;
	}

	public void setPositionIIdArtikelset(Integer positionIIdArtikelset) {
		this.positionIIdArtikelset = positionIIdArtikelset;
	}

	@Column(name = "TYP_C_NR")
	private String typCNr;

	@Column(name = "AUFTRAGPOSITION_I_ID")
	private Integer auftragpositionIId;

	@Column(name = "EINHEIT_C_NR")
	private String einheitCNr;

	@Column(name = "MEDIASTANDARD_I_ID")
	private Integer mediastandardIId;

	@Column(name = "MWSTSATZ_I_ID")
	private Integer mwstsatzIId;

	@Column(name = "LIEFERSCHEIN_I_ID")
	private Integer lieferscheinIId;

	@Column(name = "LIEFERSCHEINPOSITIONART_C_NR")
	private String lieferscheinpositionartCNr;

	@Column(name = "ARTIKEL_I_ID")
	private Integer artikelIId;

	@Column(name = "B_NETTOPREISUEBERSTEUERT")
	private Short bNettopreisuebersteuert;
	
	@Column(name = "B_KEINLIEFERREST")
	private Short bKeinlieferrest;

	
	
	public Short getBKeinlieferrest() {
		return bKeinlieferrest;
	}

	public void setBKeinlieferrest(Short bKeinlieferrest) {
		this.bKeinlieferrest = bKeinlieferrest;
	}

	private static final long serialVersionUID = 1L;

	public Lieferscheinposition() {
		super();
	}

	public Lieferscheinposition(Integer id, Integer lieferscheinIId,
			Integer sort, String lieferscheinpositionartCNr,
			Short bNettopreisuebersteuert,Short bKeinlieferrest) {
		setIId(id);
		setLieferscheinIId(lieferscheinIId);
		setISort(sort);
		setLieferscheinpositionartCNr(lieferscheinpositionartCNr);
		// CMP alle NOT NULL und default felder hier setzen
		this.setBArtikelbezeichnunguebersteuert(new Short((short) 0));
		this.setBRabattsatzuebersteuert(new Short((short) 0));
		this.setBMwstsatzuebersteuert(new Short((short) 0));
		this.setFKupferzuschlag(new Double(0));
		setBNettopreisuebersteuert(bNettopreisuebersteuert);
		this.setBKeinlieferrest(bKeinlieferrest);
	}

	public Lieferscheinposition(Integer id, Integer lieferscheinIId,
			Integer sort, String lieferscheinpositionartCNr,
			Short artikelbezeichnunguebersteuert, Short rabattsatzuebersteuert,
			Short mwstsatzuebersteuert) {
		setIId(id);
		setLieferscheinIId(lieferscheinIId);
		setISort(sort);
		setLieferscheinpositionartCNr(lieferscheinpositionartCNr);
		// CMP alle NOT NULL und default felder hier setzen
		setBArtikelbezeichnunguebersteuert(artikelbezeichnunguebersteuert);
		setBRabattsatzuebersteuert(rabattsatzuebersteuert);
		setBMwstsatzuebersteuert(mwstsatzuebersteuert);
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

	public Short getBArtikelbezeichnunguebersteuert() {
		return this.bArtikelbezeichnunguebersteuert;
	}

	public void setBArtikelbezeichnunguebersteuert(
			Short bArtikelbezeichnunguebersteuert) {
		this.bArtikelbezeichnunguebersteuert = bArtikelbezeichnunguebersteuert;
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

	public Double getFZusatzrabattsatz() {
		return this.fZusatzrabattsatz;
	}

	public void setFZusatzrabattsatz(Double fZusatzrabattsatz) {
		this.fZusatzrabattsatz = fZusatzrabattsatz;
	}

	public Double getFKupferzuschlag() {
		return this.fKupferzuschlag;
	}

	public void setFKupferzuschlag(Double fKupferzuschlag) {
		this.fKupferzuschlag = fKupferzuschlag;
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

	public BigDecimal getNNettogesamtpreisplusversteckteraufschlagminusrabatt() {
		return this.nNettogesamtpreisplusversteckteraufschlagminusrabatt;
	}

	public void setNNettogesamtpreisplusversteckteraufschlagminusrabatt(
			BigDecimal nNettogesamtpreisplusversteckteraufschlagminusrabatt) {
		this.nNettogesamtpreisplusversteckteraufschlagminusrabatt = nNettogesamtpreisplusversteckteraufschlagminusrabatt;
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

	public String getCZbez() {
		return this.cZbez;
	}

	public void setCZbez(String cZbez) {
		this.cZbez = cZbez;
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

	public Integer getAuftragpositionIId() {
		return this.auftragpositionIId;
	}

	public void setAuftragpositionIId(Integer auftragpositionIId) {
		this.auftragpositionIId = auftragpositionIId;
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

	public Integer getLieferscheinIId() {
		return this.lieferscheinIId;
	}

	public void setLieferscheinIId(Integer lieferscheinIId) {
		this.lieferscheinIId = lieferscheinIId;
	}

	public String getLieferscheinpositionartCNr() {
		return this.lieferscheinpositionartCNr;
	}

	public void setLieferscheinpositionartCNr(String lieferscheinpositionartCNr) {
		this.lieferscheinpositionartCNr = lieferscheinpositionartCNr;
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
	
	public Short getBZwsPositionspreisZeigen() {
		return bZwsPositionspreisZeigen;
	}

	public void setBZwsPositionspreisZeigen(Short bZwsPositionpreisZeigen) {
		this.bZwsPositionspreisZeigen = bZwsPositionpreisZeigen;
	}	
}
