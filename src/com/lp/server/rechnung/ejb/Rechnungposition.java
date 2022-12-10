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
package com.lp.server.rechnung.ejb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.lp.server.rechnung.service.RechnungFac;
import com.lp.server.util.IBelegVerkaufEntity;
import com.lp.server.util.IISort;
import com.lp.server.util.IPositionIIdArtikelset;
import com.lp.server.util.IZwsPosition;

@NamedQueries({
		@NamedQuery(name = "RechnungPositionfindByRechnungIId", query = "SELECT OBJECT(o) FROM Rechnungposition o WHERE o.rechnungIId=?1 ORDER BY o.iSort ASC"),
		@NamedQuery(name = "RechnungPositionfindByLieferscheinIId", query = "SELECT OBJECT(o) FROM Rechnungposition o WHERE o.lieferscheinIId=?1"),
		@NamedQuery(name = "RechnungPositionfindByArtikelIId", query = "SELECT OBJECT(o) FROM Rechnungposition o WHERE o.artikelIId=?1"),
		@NamedQuery(name = "RechnungPositionfindByRechnungIIdAuftragpositionIId", query = "SELECT OBJECT (o) FROM Rechnungposition o WHERE o.rechnungIId=?1 AND o.auftragpositionIId=?2"),
		@NamedQuery(name = "RechnungPositionfindByAuftragpositionIId", query = "SELECT OBJECT (o) FROM Rechnungposition o WHERE o.auftragpositionIId=?1"),
		@NamedQuery(name = "RechnungPositionfindByRechnungIIdArtikelIId", query = "SELECT OBJECT (o) FROM Rechnungposition o WHERE o.rechnungIId=?1 AND o.artikelIId=?2"),
		@NamedQuery(name = "RechnungpositionfindByPositionartCNr", query = "SELECT OBJECT (o) FROM Rechnungposition o WHERE o.positionsartCNr=?1 AND o.nMenge IS NOT NULL"),
		@NamedQuery(name = "RechnungpositionfindByRechnungIIdPositionsartCNr", query = "SELECT OBJECT (o) FROM Rechnungposition o WHERE o.rechnungIId=?1 AND o.positionsartCNr=?2 ORDER BY o.iSort"),
		@NamedQuery(name = "RechnungPositionejbSelectMaxISort", query = "SELECT MAX (o.iSort) FROM Rechnungposition AS o WHERE o.rechnungIId = ?1"),
		@NamedQuery(name = "RechnungPositionfindRechnungIIdISort", query = "SELECT OBJECT (o) FROM Rechnungposition o WHERE o.rechnungIId=?1 AND o.iSort=?2"),
		@NamedQuery(name = "RechnungPositionfindByPositionIId", query = "SELECT OBJECT (o) FROM Rechnungposition o WHERE o.positionIId=?1 AND o.nMenge IS NOT NULL AND o.nMenge>0"),
		@NamedQuery(name = "RechnungPositionfindByPositionIIdZugehoerig", query = "SELECT OBJECT (o) FROM Rechnungposition o WHERE o.positionIIdZugehoerig=?1 AND o.nMenge IS NOT NULL ORDER BY o.iSort"),
		@NamedQuery(name = "RechnungPositionfindByPositionIIdArtikelset", query = "SELECT OBJECT (o) FROM Rechnungposition o WHERE o.positionIIdArtikelset=?1 AND o.nMenge IS NOT NULL AND o.nMenge>0 ORDER BY o.iSort ASC"),
		@NamedQuery(name = "getWertRechnungpositionartPosition", query = "SELECT sum(o.nMenge*o.nNettoeinzelpreisplusaufschlag) FROM Rechnungposition o WHERE o.positionIId=?1 AND o.positionsartCNr IN (?2,?3)"),
		@NamedQuery(name = "RechnungPositionfindPositionIIdISort", query = "SELECT OBJECT (o) FROM Rechnungposition o WHERE o.positionIId=?1 AND o.iSort=?2"),
		@NamedQuery(name = Rechnungposition.QueryFindIZwsByVonBisIId, query = "SELECT OBJECT (o) FROM Rechnungposition o WHERE o.zwsVonPosition= :iid OR o.zwsBisPosition= :iid"),
		@NamedQuery(name = RechnungpositionQuery.ByRechnungIIdLieferscheinpositionen, query = "SELECT OBJECT(o) FROM Rechnungposition o WHERE o.rechnungIId= :iid AND o.positionsartCNr = '"
				+ RechnungFac.POSITIONSART_RECHNUNG_LIEFERSCHEIN
				+ "' ORDER BY o.iSort ASC") })
@Entity
@Table(name = "RECH_RECHNUNGPOSITION")
public class Rechnungposition implements Serializable, IISort,
		IPositionIIdArtikelset, IZwsPosition, IBelegVerkaufEntity {

	public final static String QueryFindIZwsByVonBisIId = "RechnungpositionFindIZwsByVonBisIId";

	private static final long serialVersionUID = -4927863990321643083L;

	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "I_SORT")
	private Integer iSort;

	@Column(name = "C_BEZ")
	private String cBez;

	@Column(name = "C_ZBEZ")
	private String cZbez;

	@Column(name = "X_TEXTINHALT")
	private String xTextinhalt;

	@Column(name = "N_MENGE")
	private BigDecimal nMenge;

	@Column(name = "B_DRUCKEN")
	private Short bDrucken;

	@Column(name = "F_KUPFERZUSCHLAG")
	private Double fKupferzuschlag;

	@Column(name = "F_RABATTSATZ")
	private Double fRabattsatz;

	@Column(name = "B_RABATTSATZUEBERSTEUERT")
	private Short bRabattsatzuebersteuert;

	@Column(name = "B_MWSTSATZUEBERSTEUERT")
	private Short bMwstsatzuebersteuert;

	@Column(name = "N_EINZELPREIS")
	private BigDecimal nEinzelpreis;

	@Column(name = "N_EINZELPREISPLUSAUFSCHLAG")
	private BigDecimal nEinzelpreisplusaufschlag;

	@Column(name = "N_NETTOEINZELPREIS")
	private BigDecimal nNettoeinzelpreis;

	@Column(name = "N_NETTOEINZELPREISPLUSAUFSCHLAG")
	private BigDecimal nNettoeinzelpreisplusaufschlag;

	@Column(name = "N_NETTOEINZELPREISPLUSAUFSCHLAGMINUSRABATT")
	private BigDecimal nNettoeinzelpreisplusaufschlagminusrabatt;

	@Column(name = "N_BRUTTOEINZELPREIS")
	private BigDecimal nBruttoeinzelpreis;

	@Column(name = "F_ZUSATZRABATTSATZ")
	private Double fZusatzrabattsatz;

	@Column(name = "POSITION_I_ID")
	private Integer positionIId;

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

	@Column(name = "POSITIONSART_C_NR")
	private String positionsartCNr;

	@Column(name = "LIEFERSCHEIN_I_ID")
	private Integer lieferscheinIId;

	@Column(name = "RECHNUNG_I_ID")
	private Integer rechnungIId;

	@Column(name = "RECHNUNG_I_ID_GUTSCHRIFT")
	private Integer rechnungIIdGutschrift;

	@Column(name = "RECHNUNGPOSITION_I_ID")
	private Integer rechnungpositionIId;

	@Column(name = "ARTIKEL_I_ID")
	private Integer artikelIId;

	@Column(name = "B_NETTOPREISUEBERSTEUERT")
	private Short bNettopreisuebersteuert;

	@Column(name = "KOSTENTRAEGER_I_ID")
	private Integer kostentraegerIId;

	@Column(name = "ZWSVONPOSITION_I_ID")
	private Integer zwsVonPosition;

	@Column(name = "ZWSBISPOSITION_I_ID")
	private Integer zwsBisPosition;

	@Column(name = "N_DIM_MENGE")
	private BigDecimal nDimMenge;
	public BigDecimal getNDimMenge() {
		return nDimMenge;
	}

	public void setNDimMenge(BigDecimal nDimMenge) {
		this.nDimMenge = nDimMenge;
	}

	public BigDecimal getNDimHoehe() {
		return nDimHoehe;
	}

	public void setNDimHoehe(BigDecimal nDimHoehe) {
		this.nDimHoehe = nDimHoehe;
	}

	public BigDecimal getNDimBreite() {
		return nDimBreite;
	}

	public void setNDimBreite(BigDecimal nDimBreite) {
		this.nDimBreite = nDimBreite;
	}

	public BigDecimal getNDimTiefe() {
		return nDimTiefe;
	}

	public void setNDimTiefe(BigDecimal nDimTiefe) {
		this.nDimTiefe = nDimTiefe;
	}

	@Column(name = "N_DIM_HOEHE")
	private BigDecimal nDimHoehe;

	@Column(name = "N_DIM_BREITE")
	private BigDecimal nDimBreite;

	@Column(name = "N_DIM_TIEFE")
	private BigDecimal nDimTiefe;
	
	@Column(name = "C_LVPOSITION")
	private String cLvposition;

	public String getCLvposition() {
		return cLvposition;
	}

	public void setCLvposition(String cLvposition) {
		this.cLvposition = cLvposition;
	}

	@Column(name = "N_MATERIALZUSCHLAG")
	private BigDecimal nMaterialzuschlag;

	public BigDecimal getNMaterialzuschlag() {
		return nMaterialzuschlag;
	}

	public void setNMaterialzuschlag(BigDecimal materialzuschlag) {
		nMaterialzuschlag = materialzuschlag;
	}

	@Column(name = "T_MATERIALZUSCHLAG_DATUM")
	private Timestamp tMaterialzuschlagDatum;

	public Timestamp getTMaterialzuschlagDatum() {
		return tMaterialzuschlagDatum;
	}

	public void setTMaterialzuschlagDatum(Timestamp tMaterialzuschlagDatum) {
		this.tMaterialzuschlagDatum = tMaterialzuschlagDatum;
	}

	@Column(name = "N_MATERIALZUSCHLAG_KURS")
	private BigDecimal nMaterialzuschlagKurs;

	public BigDecimal getNMaterialzuschlagKurs() {
		return nMaterialzuschlagKurs;
	}

	public void setNMaterialzuschlagKurs(BigDecimal nMaterialzuschlagKurs) {
		this.nMaterialzuschlagKurs = nMaterialzuschlagKurs;
	}

	@Column(name = "N_ZWSNETTOSUMME")
	private BigDecimal zwsNettoSumme;

	@Column(name = "B_ZWSPOSITIONSPREISZEIGEN")
	private Short bZwsPositionspreisZeigen;

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

	@Column(name = "POSITION_I_ID_ZUGEHOERIG")
	private Integer positionIIdZugehoerig;

	public Integer getPositionIIdZugehoerig() {
		return positionIIdZugehoerig;
	}

	public void setPositionIIdZugehoerig(Integer positionIIdZugehoerig) {
		this.positionIIdZugehoerig = positionIIdZugehoerig;
	}

	public Rechnungposition() {
		super();
	}

	public Rechnungposition(Integer id, Integer rechnungIId, Integer sort,
			Short drucken, Short rabattsatzuebersteuert,
			Short mwstsatzuebersteuert, Short bNettopreisuebersteuert) {
		setIId(id);
		setRechnungIId(rechnungIId);
		setISort(sort);
		setBDrucken(drucken);
		setBRabattsatzuebersteuert(rabattsatzuebersteuert);
		setBMwstsatzuebersteuert(mwstsatzuebersteuert);
		setBNettopreisuebersteuert(bNettopreisuebersteuert);
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

	public String getCZbez() {
		return this.cZbez;
	}

	public void setCZbez(String cZbez) {
		this.cZbez = cZbez;
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

	public Short getBDrucken() {
		return this.bDrucken;
	}

	public void setBDrucken(Short bDrucken) {
		this.bDrucken = bDrucken;
	}

	public Double getFKupferzuschlag() {
		return this.fKupferzuschlag;
	}

	public void setFKupferzuschlag(Double fKupferzuschlag) {
		this.fKupferzuschlag = fKupferzuschlag;
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

	public BigDecimal getNEinzelpreis() {
		return this.nEinzelpreis;
	}

	public void setNEinzelpreis(BigDecimal nEinzelpreis) {
		this.nEinzelpreis = nEinzelpreis;
	}

	public BigDecimal getNEinzelpreisplusaufschlag() {
		return this.nEinzelpreisplusaufschlag;
	}

	public void setNEinzelpreisplusaufschlag(
			BigDecimal nEinzelpreisplusaufschlag) {
		this.nEinzelpreisplusaufschlag = nEinzelpreisplusaufschlag;
	}

	public BigDecimal getNNettoeinzelpreis() {
		return this.nNettoeinzelpreis;
	}

	public void setNNettoeinzelpreis(BigDecimal nNettoeinzelpreis) {
		this.nNettoeinzelpreis = nNettoeinzelpreis;
	}

	public BigDecimal getNNettoeinzelpreisplusaufschlag() {
		return this.nNettoeinzelpreisplusaufschlag;
	}

	public void setNNettoeinzelpreisplusaufschlag(
			BigDecimal nNettoeinzelpreisplusaufschlag) {
		this.nNettoeinzelpreisplusaufschlag = nNettoeinzelpreisplusaufschlag;
	}

	public BigDecimal getNNettoeinzelpreisplusaufschlagminusrabatt() {
		return this.nNettoeinzelpreisplusaufschlagminusrabatt;
	}

	public void setNNettoeinzelpreisplusaufschlagminusrabatt(
			BigDecimal nNettoeinzelpreisplusaufschlagminusrabatt) {
		this.nNettoeinzelpreisplusaufschlagminusrabatt = nNettoeinzelpreisplusaufschlagminusrabatt;
	}

	public BigDecimal getNBruttoeinzelpreis() {
		return this.nBruttoeinzelpreis;
	}

	public void setNBruttoeinzelpreis(BigDecimal nBruttoeinzelpreis) {
		this.nBruttoeinzelpreis = nBruttoeinzelpreis;
	}

	public Double getFZusatzrabattsatz() {
		return this.fZusatzrabattsatz;
	}

	public void setFZusatzrabattsatz(Double fZusatzrabattsatz) {
		this.fZusatzrabattsatz = fZusatzrabattsatz;
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

	public String getPositionsartCNr() {
		return this.positionsartCNr;
	}

	public void setPositionsartCNr(String positionsartCNr) {
		this.positionsartCNr = positionsartCNr;
	}

	public Integer getLieferscheinIId() {
		return this.lieferscheinIId;
	}

	public void setLieferscheinIId(Integer lieferscheinIId) {
		this.lieferscheinIId = lieferscheinIId;
	}

	public Integer getRechnungIId() {
		return this.rechnungIId;
	}

	public void setRechnungIId(Integer rechnungIId) {
		this.rechnungIId = rechnungIId;
	}

	public Integer getRechnungIIdGutschrift() {
		return this.rechnungIIdGutschrift;
	}

	public void setRechnungIIdGutschrift(Integer rechnungIIdGutschrift) {
		this.rechnungIIdGutschrift = rechnungIIdGutschrift;
	}

	public Integer getRechnungpositionIId() {
		return this.rechnungpositionIId;
	}

	public void setRechnungpositionIId(Integer rechnungpositionIId) {
		this.rechnungpositionIId = rechnungpositionIId;
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

	@Override
	public BigDecimal getNNettogesamtpreisplusversteckteraufschlagminusrabatte() {
		return getNNettoeinzelpreisplusaufschlagminusrabatt();
	}

	@Override
	public void setNNettogesamtpreisplusversteckteraufschlagminusrabatte(
			BigDecimal nNettogesamtpreisplusversteckteraufschlagminusrabatte) {
		setNNettoeinzelpreisplusaufschlagminusrabatt(nNettogesamtpreisplusversteckteraufschlagminusrabatte);
	}

	@Override
	public void setNNettogesamtpreisplusversteckteraufschlag(BigDecimal nettogesamt) {
		setNNettoeinzelpreisplusaufschlag(nettogesamt);
	}

	@Override
	public void setNMwstbetrag(BigDecimal mwstbetrag) {
	}

	@Override
	public void setNRabattbetrag(BigDecimal rabattbetrag) {
	}

	
	@Override
	public BigDecimal getNNettogesamtpreisplusversteckteraufschlag() {
		return getNNettoeinzelpreisplusaufschlag();
	}

	@Override
	public void setNBruttogesamtpreis(BigDecimal bruttogesamt) {
	}

	@Override
	public BigDecimal getNBruttogesamtpreis() {
		return BigDecimal.ZERO;
	}

	@Override
	public BigDecimal getNNettogesamtpreis() {
		return getNNettoeinzelpreis();
	}
	@Override
	public void setNNettogesamtpreis(BigDecimal nettoGesamt) {
	}
}
