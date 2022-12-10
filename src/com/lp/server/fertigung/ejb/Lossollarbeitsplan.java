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
package com.lp.server.fertigung.ejb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.lp.server.artikel.ejb.Artikel;

@NamedQueries({
		@NamedQuery(name = "LossollarbeitsplanfindByLosIId", query = "SELECT OBJECT(o) FROM Lossollarbeitsplan o WHERE o.losIId=?1 ORDER BY o.iArbeitsgangnummer ASC,o.iUnterarbeitsgang ASC"),
		@NamedQuery(name = "LossollarbeitsplanfindByLossollmaterialIId", query = "SELECT OBJECT(o) FROM Lossollarbeitsplan o WHERE o.lossollmaterialIId=?1"),
		@NamedQuery(name = LossollarbeitsplanQuery.ByLosIIdIArbeitsgangnummer, query = "SELECT OBJECT(o) FROM Lossollarbeitsplan o WHERE o.losIId=?1 AND o.iArbeitsgangnummer=?2 ORDER BY o.iUnterarbeitsgang"),
		@NamedQuery(name = "LossollarbeitsplanfindByLosIIdIArbeitsgangnummerNaechsterHauptarbeitsgang", query = "SELECT OBJECT(o) FROM Lossollarbeitsplan o WHERE o.losIId=?1 AND o.iArbeitsgangnummer>?2 ORDER BY o.iArbeitsgangnummer,o.iUnterarbeitsgang"),
		@NamedQuery(name = LossollarbeitsplanQuery.ByLosIIdIArbeitsgangnummerIUnterarbeitsgang, query = "SELECT OBJECT(o) FROM Lossollarbeitsplan o WHERE o.losIId=?1 AND o.iArbeitsgangnummer=?2 AND o.iUnterarbeitsgang=?3"),
		@NamedQuery(name = "LossollarbeitsplanejbSelectNextReihung", query = "SELECT MAX (arbeitsplan.iArbeitsgangnummer) FROM Lossollarbeitsplan AS arbeitsplan WHERE arbeitsplan.losIId = ?1"),
		@NamedQuery(name = "LossollarbeitsplanfindByLosIIdArtikelIIdTaetigkeit", query = "SELECT OBJECT(o) FROM Lossollarbeitsplan o WHERE o.losIId=?1 AND o.artikelIIdTaetigkeit=?2 ORDER BY o.iArbeitsgangnummer ASC"),
		@NamedQuery(name = LossollarbeitsplanQuery.ByLosIIdArtklaIIds, query = "SELECT OBJECT(o) FROM Lossollarbeitsplan o LEFT JOIN o.artikelTaetigkeit a WHERE o.losIId=:losId AND a.artklaIId IN (:artklaIds) ORDER BY o.iArbeitsgangnummer ASC,o.iUnterarbeitsgang ASC") })
@Entity
@Table(name = "FERT_LOSSOLLARBEITSPLAN")
public class Lossollarbeitsplan implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "L_RUESTZEIT")
	private Long lRuestzeit;

	@Column(name = "L_STUECKZEIT")
	private Long lStueckzeit;

	@Column(name = "N_GESAMTZEIT")
	private BigDecimal nGesamtzeit;

	@Column(name = "I_ARBEITSGANGNUMMER")
	private Integer iArbeitsgangnummer;

	@Column(name = "I_UNTERARBEITSGANG")
	private Integer iUnterarbeitsgang;

	@Column(name = "I_MASCHINENVERSATZTAGE")
	private Integer iMaschinenversatztage;

	@Column(name = "I_MASCHINENVERSATZTAGE_AUS_STUECKLISTE")
	private Integer iMaschinenversatztageAusStueckliste;

	public Integer getIMaschinenversatztageAusStueckliste() {
		return iMaschinenversatztageAusStueckliste;
	}

	public void setIMaschinenversatztageAusStueckliste(Integer iMaschinenversatztageAusStueckliste) {
		this.iMaschinenversatztageAusStueckliste = iMaschinenversatztageAusStueckliste;
	}

	@Column(name = "I_MASCHINENVERSATZ_MS")
	private Integer iMaschinenversatzMs;

	@Column(name = "N_PPM")
	private BigDecimal nPpm;

	public BigDecimal getNPpm() {
		return nPpm;
	}

	public void setNPpm(BigDecimal nPpm) {
		this.nPpm = nPpm;
	}

	public Integer getIMaschinenversatzMs() {
		return iMaschinenversatzMs;
	}

	public void setIMaschinenversatzMs(Integer iMaschinenversatzMs) {
		this.iMaschinenversatzMs = iMaschinenversatzMs;
	}

	@Column(name = "APKOMMENTAR_I_ID")
	private Integer apkommentarIId;

	public Integer getApkommentarIId() {
		return apkommentarIId;
	}

	public void setApkommentarIId(Integer apkommentarIId) {
		this.apkommentarIId = apkommentarIId;
	}

	@Column(name = "F_FORTSCHRITT")
	private Double fFortschritt;

	public Double getFFortschritt() {
		return fFortschritt;
	}

	public void setFFortschritt(Double fFortschritt) {
		this.fFortschritt = fFortschritt;
	}

	@Column(name = "LOSSOLLMATERIAL_I_ID")
	private Integer lossollmaterialIId;

	public Integer getLossollmaterialIId() {
		return lossollmaterialIId;
	}

	public void setLossollmaterialIId(Integer lossollmaterialIId) {
		this.lossollmaterialIId = lossollmaterialIId;
	}

	public Integer getIMaschinenversatztage() {
		return iMaschinenversatztage;
	}

	public void setIMaschinenversatztage(Integer maschinenversatztage) {
		iMaschinenversatztage = maschinenversatztage;
	}

	public Integer getIUnterarbeitsgang() {
		return iUnterarbeitsgang;
	}

	public void setIUnterarbeitsgang(Integer unterarbeitsgang) {
		iUnterarbeitsgang = unterarbeitsgang;
	}

	@Column(name = "T_AENDERN")
	private Timestamp tAendern;

	@Column(name = "X_TEXT")
	private String xText;

	@Column(name = "C_KOMENTAR")
	private String cKomentar;

	@Column(name = "AGART_C_NR")
	private String agartCNr;

	@Column(name = "I_AUFSPANNUNG")
	private Integer iAufspannung;

	public String getAgartCNr() {
		return agartCNr;
	}

	public void setAgartCNr(String agartCNr) {
		this.agartCNr = agartCNr;
	}

	public Integer getIAufspannung() {
		return iAufspannung;
	}

	public void setIAufspannung(Integer aufspannung) {
		iAufspannung = aufspannung;
	}

	
	@Column(name = "I_REIHUNG")
	private Integer iReihung;
	
	public Integer getIReihung() {
		return iReihung;
	}

	public void setIReihung(Integer iReihung) {
		this.iReihung = iReihung;
	}

	@Column(name = "B_NACHTRAEGLICH")
	private Short bNachtraeglich;

	@Column(name = "B_FERTIG")
	private Short bFertig;

	@Column(name = "B_AUTOENDEBEIGEHT")
	private Short bAutoendebeigeht;

	@Column(name = "B_NURMASCHINENZEIT")
	private Short bNurmaschinenzeit;

	public Short getBNurmaschinenzeit() {
		return bNurmaschinenzeit;
	}

	public void setBNurmaschinenzeit(Short nurmaschinenzeit) {
		bNurmaschinenzeit = nurmaschinenzeit;
	}

	public Short getBAutoendebeigeht() {
		return bAutoendebeigeht;
	}

	public void setBAutoendebeigeht(Short autoendebeigeht) {
		bAutoendebeigeht = autoendebeigeht;
	}

	public Short getBFertig() {
		return bFertig;
	}

	public void setBFertig(Short fertig) {
		bFertig = fertig;
	}

	@Column(name = "LOS_I_ID")
	private Integer losIId;

	@Column(name = "MASCHINE_I_ID")
	private Integer maschineIId;

	@Column(name = "PERSONAL_I_ID_AENDERN")
	private Integer personalIIdAendern;

	@Column(name = "PERSONAL_I_ID_ZUGEORDNETER")
	private Integer personalIIdZugeordneter;

	public Integer getPersonalIIdZugeordneter() {
		return personalIIdZugeordneter;
	}

	public void setPersonalIIdZugeordneter(Integer personalIIdZugeordneter) {
		this.personalIIdZugeordneter = personalIIdZugeordneter;
	}



	@Column(name = "ARTIKEL_I_ID_TAETIGKEIT")
	private Integer artikelIIdTaetigkeit;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ARTIKEL_I_ID_TAETIGKEIT", referencedColumnName = "I_ID", insertable = false, updatable = false)
	private Artikel artikelTaetigkeit;
	
	@Column(name = "T_AGBEGINN_BERECHNET")
	private Timestamp  tAgbeginnBerechnet;
	
	
	public Timestamp getTAgbeginnBerechnet() {
		return tAgbeginnBerechnet;
	}

	public void setTAgbeginnBerechnet(Timestamp tAgbeginnBerechnet) {
		this.tAgbeginnBerechnet = tAgbeginnBerechnet;
	}
	
	
	@Column(name = "T_FERTIG")
	private Timestamp  tFertig;
	@Column(name = "PERSONAL_I_ID_FERTIG")
	private Integer personalIIdFertig;
	
	public Timestamp getTFertig() {
		return tFertig;
	}

	public void setTFertig(Timestamp tFertig) {
		this.tFertig = tFertig;
	}

	public Integer getPersonalIIdFertig() {
		return personalIIdFertig;
	}

	public void setPersonalIIdFertig(Integer personalIIdFertig) {
		this.personalIIdFertig = personalIIdFertig;
	}

	private static final long serialVersionUID = 1L;

	public Lossollarbeitsplan() {
		super();
	}

	public Lossollarbeitsplan(Integer id, Integer losIId,
			Integer artikelIIdTaetigkeit2, Long ruestzeit, Long stueckzeit,
			BigDecimal gesamtzeit, Integer arbeitsgangnummer,
			Integer personalIIdAendern2, Short nachtraeglich, Short bFertig,
			Short autoendebeigeht, Short bNurmaschinenzeit, Integer iReihung) {
		setIId(id);
		setLosIId(losIId);
		setArtikelIIdTaetigkeit(artikelIIdTaetigkeit2);
		setNGesamtzeit(gesamtzeit);
		setIArbeitsgangnummer(arbeitsgangnummer);
		setPersonalIIdAendern(personalIIdAendern2);
		setBNachtraeglich(nachtraeglich);
		// Setzen der NOT NULL felder
		Timestamp now = new Timestamp(System.currentTimeMillis());
		this.setTAendern(now);
		setLRuestzeit(ruestzeit);
		setLStueckzeit(stueckzeit);
		setBFertig(bFertig);
		setBAutoendebeigeht(autoendebeigeht);
		setBNurmaschinenzeit(bNurmaschinenzeit);
		setIReihung(iReihung);
		

	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Long getLRuestzeit() {
		return this.lRuestzeit;
	}

	public void setLRuestzeit(Long lRuestzeit) {
		this.lRuestzeit = lRuestzeit;
	}

	public Long getLStueckzeit() {
		return this.lStueckzeit;
	}

	public void setLStueckzeit(Long lStueckzeit) {
		this.lStueckzeit = lStueckzeit;
	}

	public BigDecimal getNGesamtzeit() {
		return this.nGesamtzeit;
	}

	public void setNGesamtzeit(BigDecimal nGesamtzeit) {
		this.nGesamtzeit = nGesamtzeit;
	}

	public Integer getIArbeitsgangnummer() {
		return this.iArbeitsgangnummer;
	}

	public void setIArbeitsgangnummer(Integer iArbeitsgangnummer) {
		this.iArbeitsgangnummer = iArbeitsgangnummer;
	}

	public Timestamp getTAendern() {
		return this.tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public String getXText() {
		return this.xText;
	}

	public void setXText(String xText) {
		this.xText = xText;
	}

	public String getCKomentar() {
		return this.cKomentar;
	}

	public void setCKomentar(String cKomentar) {
		this.cKomentar = cKomentar;
	}

	public Short getBNachtraeglich() {
		return this.bNachtraeglich;
	}

	public void setBNachtraeglich(Short bNachtraeglich) {
		this.bNachtraeglich = bNachtraeglich;
	}

	public Integer getLosIId() {
		return this.losIId;
	}

	public void setLosIId(Integer losIId) {
		this.losIId = losIId;
	}

	public Integer getMaschineIId() {
		return this.maschineIId;
	}

	public void setMaschineIId(Integer maschineIId) {
		this.maschineIId = maschineIId;
	}

	public Integer getPersonalIIdAendern() {
		return this.personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public Integer getArtikelIIdTaetigkeit() {
		return this.artikelIIdTaetigkeit;
	}

	public void setArtikelIIdTaetigkeit(Integer artikelIIdTaetigkeit) {
		this.artikelIIdTaetigkeit = artikelIIdTaetigkeit;
	}

}
