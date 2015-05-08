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
package com.lp.server.auftrag.ejb;

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

@NamedQueries( {
		@NamedQuery(name = "AuftragpositionfindByAuftrag", query = "SELECT OBJECT (o) FROM Auftragposition o WHERE o.auftragIId=?1 ORDER BY o.iSort"),
		@NamedQuery(name = "AuftragpositionfindByAuftragOffeneMenge", query = "SELECT OBJECT (o) FROM Auftragposition o WHERE o.auftragIId = ?1 AND o.nOffenemenge IS NOT NULL AND o.nOffenemenge > 0"),
		@NamedQuery(name = "AuftragpositionfindByAuftragISort", query = "SELECT OBJECT (o) FROM Auftragposition o WHERE o.auftragIId = ?1 AND o.iSort = ?2"),
		@NamedQuery(name = "AuftragpositionejbSelectMaxISort", query = "SELECT MAX (o.iSort) FROM Auftragposition o WHERE o.auftragIId = ?1"),
		@NamedQuery(name = "AuftragpositionejbSelectMinISort", query = "SELECT MIN (o.iSort) FROM Auftragposition o WHERE o.auftragIId = ?1"),
		@NamedQuery(name = "AuftragpositionfindByAuftragIIdNMengeNotNull", query = "SELECT OBJECT (o) FROM Auftragposition o WHERE o.auftragIId=?1 AND o.nMenge IS NOT NULL"),
		@NamedQuery(name = "AuftragpositionfindByAuftragIIdAuftragpositionsartCNr", query = "SELECT OBJECT (o) FROM Auftragposition o WHERE o.auftragIId=?1 AND o.auftragpositionartCNr=?2 ORDER BY o.iSort"),
		@NamedQuery(name = "AuftragpositionfindByAuftragPositiveMenge", query = "SELECT OBJECT (o) FROM Auftragposition o WHERE o.auftragIId=?1 AND o.nMenge IS NOT NULL AND o.nMenge>0"),
		@NamedQuery(name = "AuftragpositionfindByAuftragNegativeMenge", query = "SELECT OBJECT (o) FROM Auftragposition o WHERE o.auftragIId=?1 AND o.nMenge IS NOT NULL AND o.nMenge<0 AND o.positionIIdArtikelset IS NULL"),
		@NamedQuery(name = "AuftragpositionfindByPositionIIdArtikelset", query = "SELECT OBJECT (o) FROM Auftragposition o WHERE o.positionIIdArtikelset=?1 AND o.nMenge IS NOT NULL AND o.nMenge>0 ORDER BY o.iSort ASC"),
		@NamedQuery(name = "AuftragpositionfindByPositionIIdArtikelsetAll", query = "SELECT OBJECT (o) FROM Auftragposition o WHERE o.positionIIdArtikelset=?1"),
		@NamedQuery(name = "AuftragpositionfindByAuftragIIdArtikelIId", query = "SELECT OBJECT (o) FROM Auftragposition o WHERE o.auftragIId=?1 AND o.artikelIId=?2 AND o.nMenge>0"),
		@NamedQuery(name = "AuftragpositionfindByAuftragIIdAuftragpositionIIdRahmenposition", query = "SELECT OBJECT (o) FROM Auftragposition o WHERE o.auftragIId=?1 AND o.auftragpositionIIdRahmenposition=?2"),
		@NamedQuery(name = "AuftragpositionfindByPositionIId", query = "SELECT OBJECT (o) FROM Auftragposition o WHERE o.positionIId=?1 AND o.nMenge IS NOT NULL AND o.nMenge>0"),
		@NamedQuery(name = "getWertAuftragpositionartPosition", query = "SELECT sum(o.nMenge*o.nNettogesamtpreisplusversteckteraufschlag) FROM Auftragposition o WHERE o.positionIId=?1 AND o.auftragpositionartCNr IN (?2,?3)"),
		@NamedQuery(name = "AuftragpositionfindByAuftragpositionIIdRahmenposition", query = "SELECT OBJECT (o) FROM Auftragposition o WHERE o.auftragpositionIIdRahmenposition=?1"),
		@NamedQuery(name = Auftragposition.QueryFindIZwsByVonBisIId, query = "SELECT OBJECT (o) FROM Auftragposition o WHERE o.zwsVonPosition= :iid OR o.zwsBisPosition= :iid") })
@Entity
@Table(name = "AUFT_AUFTRAGPOSITION")
public class Auftragposition implements Serializable, IISort, IPositionIIdArtikelset, IZwsPosition {
	
	public final static String QueryFindIZwsByVonBisIId = "AuftragpositionFindIZwsByVonBisIId" ;
	
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

	@Column(name = "N_OFFENEMENGE")
	private BigDecimal nOffenemenge;

	@Column(name = "F_RABATTSATZ")
	private Double fRabattsatz;

	@Column(name = "B_RABATTSATZUEBERSTEUERT")
	private Short bRabattsatzuebersteuert;

	@Column(name = "F_ZUSATZRABATTSATZ")
	private Double fZusatzrabattsatz;

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

	@Column(name = "T_UEBERSTEUERTERLIEFERTERMIN")
	private Timestamp tUebersteuerterliefertermin;

	@Column(name = "B_DRUCKEN")
	private Short bDrucken;

	@Column(name = "C_ZBEZ")
	private String cZbez;

	@Column(name = "C_SERIENNRCHARGENNR")
	private String cSeriennrchargennr;

	@Column(name = "N_OFFENERAHMENMENGE")
	private BigDecimal nOffenerahmenmenge;

	@Column(name = "POSITION_I_ID")
	private Integer positionIId;

	@Column(name = "TYP_C_NR")
	private String typCNr;

	@Column(name = "AUFTRAG_I_ID")
	private Integer auftragIId;

	@Column(name = "AUFTRAGPOSITION_I_ID_RAHMENPOSITION")
	private Integer auftragpositionIIdRahmenposition;

	@Column(name = "AUFTRAGPOSITIONART_C_NR")
	private String auftragpositionartCNr;

	@Column(name = "AUFTRAGPOSITIONSTATUS_C_NR")
	private String auftragpositionstatusCNr;

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
	
	@Column(name = "N_EINKAUFPREIS") 
	private BigDecimal nEinkaufpreis;

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
	@Column(name = "LIEFERANT_I_ID")
	private Integer lieferantIId;
	public Integer getLieferantIId() {
		return lieferantIId;
	}

	public void setLieferantIId(Integer lieferantIId) {
		this.lieferantIId = lieferantIId;
	}

	
	
	public Integer getKostentraegerIId() {
		return kostentraegerIId;
	}

	public void setKostentraegerIId(Integer kostentraegerIId) {
		this.kostentraegerIId = kostentraegerIId;
	}
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

	@Column(name = "POSITION_I_ID_ARTIKELSET")
	private Integer positionIIdArtikelset;

	public Integer getPositionIIdArtikelset() {
		return positionIIdArtikelset;
	}

	public void setPositionIIdArtikelset(Integer positionIIdArtikelset) {
		this.positionIIdArtikelset = positionIIdArtikelset;
	}
	@Column(name = "C_LVPOSITION")
	private String cLvposition;
	
	public String getCLvposition() {
		return cLvposition;
	}

	public void setCLvposition(String cLvposition) {
		this.cLvposition = cLvposition;
	}
	private static final long serialVersionUID = 1L;

	public Auftragposition(Integer iId, Integer auftragIId, Integer iSort,
			String auftragpositionartCNr, String auftragpositionstatusCNr, Short bNettopreisuebersteuert) {
		// iIdAuftragposition,auftragpositionDtoI.getAuftragIId(),
		// auftragpositionDtoI.getISort(),
		// auftragpositionDtoI.getAuftragpositionartCNr(),auftragpositionDtoI.
		// getAuftragpositionstatusCNr());
		setIId(iId);
		setAuftragIId(auftragIId);
		setISort(iSort);
		setAuftragpositionartCNr(auftragpositionartCNr);
		setAuftragpositionstatusCNr(auftragpositionstatusCNr);
		
		// CMP alle NOT NULL und default felder hier setzen
	    setBArtikelbezeichnungUebersteuert(new Short( (short) 0));
	    setBRabattsatzuebersteuert(new Short( (short) 0));
	    setBMwstsatzuebersteuert(new Short( (short) 0));
	    setTUebersteuerterliefertermin(new Timestamp(System.currentTimeMillis()));
	    setBDrucken(new Short( (short) 0));
	    setBNettopreisuebersteuert(bNettopreisuebersteuert);
	}
	
	public Auftragposition(Integer iId,
			Integer auftragIId,
			String auftragpositionartCNr,
			Short artikelbezeichnunguebersteuert,
			Short rabattsatzuebersteuert,
			Short mwstsatzuebersteuert,
			Timestamp uebersteuerterliefertermin,
			Short drucken) {
		setIId(iId);
		setAuftragIId(auftragIId);
		setAuftragpositionartCNr(auftragpositionartCNr);
	    setBArtikelbezeichnungUebersteuert(artikelbezeichnunguebersteuert);
	    setBRabattsatzuebersteuert(rabattsatzuebersteuert);
	    setBMwstsatzuebersteuert(mwstsatzuebersteuert);
	    setTUebersteuerterliefertermin(uebersteuerterliefertermin);
	    setBDrucken(drucken);
	}
	
	public Auftragposition() {
		
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

	public String getCBezeichnung() {
		return this.cBez;
	}

	public void setCBezeichnung(String cBez) {
		this.cBez = cBez;
	}

	public Short getBArtikelbezeichnungUebersteuert() {
		return this.bArtikelbezeichnunguebersteuert;
	}

	public void setBArtikelbezeichnungUebersteuert(
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

	public BigDecimal getNOffeneMenge() {
		return this.nOffenemenge;
	}

	public void setNOffeneMenge(BigDecimal nOffenemenge) {
		this.nOffenemenge = nOffenemenge;
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

	public Timestamp getTUebersteuerterliefertermin() {
		return this.tUebersteuerterliefertermin;
	}

	public void setTUebersteuerterliefertermin(
			Timestamp tUebersteuerterliefertermin) {
		this.tUebersteuerterliefertermin = tUebersteuerterliefertermin;
	}

	public Short getBDrucken() {
		return this.bDrucken;
	}

	public void setBDrucken(Short bDrucken) {
		this.bDrucken = bDrucken;
	}

	public String getCZusatzbezeichnung() {
		return this.cZbez;
	}

	public void setCZusatzbezeichnung(String cZbez) {
		this.cZbez = cZbez;
	}

	public String getCSeriennrchargennr() {
		return this.cSeriennrchargennr;
	}

	public void setCSeriennrchargennr(String cSeriennrchargennr) {
		this.cSeriennrchargennr = cSeriennrchargennr;
	}

	public BigDecimal getNOffenerahmenmenge() {
		return this.nOffenerahmenmenge;
	}

	public void setNOffenerahmenmenge(BigDecimal nOffenerahmenmenge) {
		this.nOffenerahmenmenge = nOffenerahmenmenge;
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

	public Integer getAuftragIId() {
		return this.auftragIId;
	}

	public void setAuftragIId(Integer auftragIId) {
		this.auftragIId = auftragIId;
	}

	public Integer getAuftragpositionIIdRahmenposition() {
		return this.auftragpositionIIdRahmenposition;
	}

	public void setAuftragpositionIIdRahmenposition(
			Integer auftragpositionIIdRahmenposition) {
		this.auftragpositionIIdRahmenposition = auftragpositionIIdRahmenposition;
	}

	public String getAuftragpositionartCNr() {
		return this.auftragpositionartCNr;
	}

	public void setAuftragpositionartCNr(String auftragpositionartCNr) {
		this.auftragpositionartCNr = auftragpositionartCNr;
	}

	public String getAuftragpositionstatusCNr() {
		return this.auftragpositionstatusCNr;
	}

	public void setAuftragpositionstatusCNr(String auftragpositionstatusCNr) {
		this.auftragpositionstatusCNr = auftragpositionstatusCNr;
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

	public BigDecimal getNEinkaufpreis() {
		return this.nEinkaufpreis;
	}

	public void setNEinkaufpreis(BigDecimal nEinkaufpreis) {
		this.nEinkaufpreis = nEinkaufpreis;
	}

	@Override
	public Integer getZwsVonPosition() {
		return zwsVonPosition ;
	}

	@Override
	public void setZwsVonPosition(Integer zwsVonPosition) {
		this.zwsVonPosition = zwsVonPosition ;
	}

	@Override
	public Integer getZwsBisPosition() {
		return zwsBisPosition ;
	}

	@Override
	public void setZwsBisPosition(Integer zwsBisPosition) {
		this.zwsBisPosition = zwsBisPosition ;
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
