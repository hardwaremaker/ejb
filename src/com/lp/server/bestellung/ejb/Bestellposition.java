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

import com.lp.server.util.IISort;
import com.lp.server.util.IPositionIIdArtikelset;
import com.lp.util.Helper;

@NamedQueries( {
		@NamedQuery(name = "BestellpositionejbSelectMaxISort", query = "SELECT MAX (o.iSort) FROM Bestellposition o WHERE o.bestellungIId=?1"),
		@NamedQuery(name = "BestellpositionfindAll", query = "SELECT OBJECT(o) FROM Bestellposition o ORDER BY o.iId"),
		@NamedQuery(name = "BestellpositionfindByBestellung", query = "SELECT OBJECT (o) FROM Bestellposition o WHERE o.bestellungIId=?1 ORDER BY o.iSort"),
		@NamedQuery(name = "BestellpositionfindByArtikelOrderByTAuftragsbestaetigungstermin", query = "SELECT OBJECT (o) FROM Bestellposition o WHERE o.artikelIId=?1 AND o.tAuftragsbestaetigungstermin is not null ORDER BY o.tAuftragsbestaetigungstermin ASC"),
		@NamedQuery(name = "BestellpositionfindByBestellungIIdBestellpositionIIdRahmenposition", query = "SELECT OBJECT (o) FROM Bestellposition o WHERE o.bestellungIId=?1 AND o.bestellpositionIIdRahmenposition=?2"),
		@NamedQuery(name = "BestellpositionfindByPositionIIdArtikelset", query = "SELECT OBJECT (o) FROM Bestellposition o WHERE o.positionIIdArtikelset=?1 AND o.nMenge IS NOT NULL AND o.nMenge>0 ORDER BY o.iSort ASC"),
		@NamedQuery(name = "BestellpositionfindByBestellungMengeNotNull", query = "SELECT OBJECT (o) FROM Bestellposition o WHERE o.bestellungIId=?1 AND o.nMenge IS NOT NULL"),
		@NamedQuery(name = "BestellpositionfindByLossollmaterialIId", query = "SELECT OBJECT (o) FROM Bestellposition o WHERE o.lossollmaterialIId=?1"),
		@NamedQuery(name = "BestellpositionfindByBestellpositionIIdRahmenposition", query = "SELECT OBJECT (o) FROM Bestellposition o WHERE o.bestellpositionIIdRahmenposition=?1") })
@Entity
@Table(name = "BES_BESTELLPOSITION")
public class Bestellposition implements Serializable, IISort, IPositionIIdArtikelset {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "I_SORT")
	private Integer iSort;

	@Column(name = "B_NETTOPREISUEBERSTEUERT")
	private Short bNettopreisuebersteuert;

	@Column(name = "C_BEZ")
	private String cBez;

	@Column(name = "C_ZBEZ")
	private String cZbez;

	@Column(name = "B_ARTIKELBEZUEBERSTEUERT")
	private Short bArtikelbezuebersteuert;

	@Column(name = "C_TEXTINHALT")
	private String cTextinhalt;

	@Column(name = "N_MENGE")
	private BigDecimal nMenge;

	@Column(name = "F_RABATTSATZ")
	private Double fRabattsatz;

	@Column(name = "B_MWSTSATZUEBERSTEUERT")
	private Short bMwstsatzuebersteuert;

	@Column(name = "N_NETTOEINZELPREIS")
	private BigDecimal nNettoeinzelpreis;

	@Column(name = "N_RABATTBETRAG")
	private BigDecimal nRabattbetrag;

	@Column(name = "N_NETTOGESAMTPREIS")
	private BigDecimal nNettogesamtpreis;

	@Column(name = "N_NETTOGESAMTPREISMINUSRABATTE")
	private BigDecimal nNettogesamtpreisminusrabatte;

	@Column(name = "N_MATERIALZUSCHLAG")
	private BigDecimal nMaterialzuschlag;

	@Column(name = "KUNDE_I_ID")
	private Integer kundeIId;

	public Integer getKundeIId() {
		return kundeIId;
	}

	public void setKundeIId(Integer kundeIId) {
		this.kundeIId = kundeIId;
	}

	@Column(name = "LOSSOLLMATERIAL_I_ID")
	private Integer lossollmaterialIId;

	public Integer getLossollmaterialIId() {
		return lossollmaterialIId;
	}

	public void setLossollmaterialIId(Integer lossollmaterialIId) {
		this.lossollmaterialIId = lossollmaterialIId;
	}

	public BigDecimal getNMaterialzuschlag() {
		return nMaterialzuschlag;
	}

	public void setNMaterialzuschlag(BigDecimal materialzuschlag) {
		nMaterialzuschlag = materialzuschlag;
	}

	@Column(name = "T_UEBERSTEUERTERLIEFERTERMIN")
	private Timestamp tUebersteuerterliefertermin;

	@Column(name = "B_DRUCKEN")
	private Short bDrucken;

	@Column(name = "N_OFFENEMENGE")
	private BigDecimal nOffenemenge;

	@Column(name = "T_AUFTRAGSBESTAETIGUNGSTERMIN")
	private Date tAuftragsbestaetigungstermin;

	@Column(name = "C_ABKOMMENTAR")
	private String cAbkommentar;

	@Column(name = "C_ABNUMMER")
	private String cAbnummer;

	@Column(name = "N_FIXKOSTEN")
	private BigDecimal nFixkosten;

	@Column(name = "N_FIXKOSTENGELIEFERT")
	private BigDecimal nFixkostengeliefert;

	@Column(name = "T_MANUELLVOLLSTAENDIGGELIEFERT")
	private Timestamp tManuellvollstaendiggeliefert;

	@Column(name = "T_LIEFERTERMINBESTAETIGT")
	private Timestamp tLieferterminbestaetigt;

	@Column(name = "BESTELLPOSITION_I_ID_RAHMENPOSITION")
	private Integer bestellpositionIIdRahmenposition;

	@Column(name = "BESTELLPOSITIONART_C_NR")
	private String bestellpositionartCNr;

	@Column(name = "BESTELLPOSITIONSTATUS_C_NR")
	private String bestellpositionstatusCNr;

	@Column(name = "BESTELLUNG_I_ID")
	private Integer bestellungIId;

	@Column(name = "EINHEIT_C_NR")
	private String einheitCNr;

	@Column(name = "MEDIASTANDARD_I_ID")
	private Integer mediastandardIId;

	@Column(name = "MWSTSATZ_I_ID")
	private Integer mwstsatzIId;

	@Column(name = "ARTIKEL_I_ID")
	private Integer artikelIId;

	@Column(name = "T_ABTERMIN_AENDERN")
	private Timestamp tAbterminAendern;

	@Column(name = "PERSONAL_I_ID_ABTERMIN_AENDERN")
	private Integer personalIIdAbterminAendern;

	@Column(name = "POSITION_I_ID_ARTIKELSET")
	private Integer positionIIdArtikelset;

	public Integer getPositionIIdArtikelset() {
		return positionIIdArtikelset;
	}

	public void setPositionIIdArtikelset(Integer positionIIdArtikelset) {
		this.positionIIdArtikelset = positionIIdArtikelset;
	}

	@Column(name = "PERSONAL_I_ID_LIEFERTERMINBESTAETIGT")
	private Integer personalIIdLieferterminbestaetigt;

	public Timestamp getTLieferterminbestaetigt() {
		return tLieferterminbestaetigt;
	}

	public void setTLieferterminbestaetigt(Timestamp lieferterminbestaetigt) {
		tLieferterminbestaetigt = lieferterminbestaetigt;
	}

	public Integer getPersonalIIdLieferterminbestaetigt() {
		return personalIIdLieferterminbestaetigt;
	}

	public void setPersonalIIdLieferterminbestaetigt(
			Integer personalIIdLieferterminbestaetigt) {
		this.personalIIdLieferterminbestaetigt = personalIIdLieferterminbestaetigt;
	}

	@Column(name = "T_ABURSPRUNGSTERMIN")
	private Timestamp tAbursprungstermin;

	@Column(name = "C_ANGEBOTNUMMER")
	private String cAngebotnummer;

	public String getCAngebotnummer() {
		return cAngebotnummer;
	}

	public void setCAngebotnummer(String angebotnummer) {
		cAngebotnummer = angebotnummer;
	}

	private static final long serialVersionUID = 1L;

	public Bestellposition() {
		super();
	}

	public Bestellposition(Integer id, Integer bestellungIId, Integer sort,
			String bestellpositionartCNr, String bestellpositionstatusCNr) {
		setIId(id);
		setBestellungIId(bestellungIId);
		setISort(sort);
		setBestellpositionartCNr(bestellpositionartCNr);
		setBArtikelbezuebersteuert(Helper.boolean2Short(false));
		setBMwstsatzuebersteuert(Helper.boolean2Short(false));
		setBDrucken(Helper.boolean2Short(false));
		setBNettopreisuebersteuert(Helper.boolean2Short(false));
		Timestamp t = new Timestamp(System.currentTimeMillis());
		setTUebersteuerterliefertermin(t);
		setBestellpositionstatusCNr(bestellpositionstatusCNr);
	}

	public Bestellposition(Integer id, Integer bestellungIId,
			String bestellpositionartCNr, Short artikelbezuebersteuert,
			Short mwstsatzuebersteuert, Timestamp uebersteuerterliefertermin,
			Short drucken, String bestellpositionstatusCNr,
			Short bNettopreisuebersteuert) {
		setIId(id);
		setBestellpositionartCNr(bestellpositionartCNr);
		setBestellungIId(bestellungIId);
		setBArtikelbezuebersteuert(artikelbezuebersteuert);
		setBMwstsatzuebersteuert(mwstsatzuebersteuert);
		setBDrucken(drucken);
		setTUebersteuerterliefertermin(uebersteuerterliefertermin);
		setBestellpositionstatusCNr(bestellpositionstatusCNr);
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

	public Short getBArtikelbezuebersteuert() {
		return this.bArtikelbezuebersteuert;
	}

	public void setBArtikelbezuebersteuert(Short bArtikelbezuebersteuert) {
		this.bArtikelbezuebersteuert = bArtikelbezuebersteuert;
	}

	public String getCTextinhalt() {
		return this.cTextinhalt;
	}

	public void setCTextinhalt(String cTextinhalt) {
		this.cTextinhalt = cTextinhalt;
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

	public BigDecimal getNNettogesamtpreisminusrabatte() {
		return this.nNettogesamtpreisminusrabatte;
	}

	public void setNNettogesamtpreisminusrabatte(
			BigDecimal nNettogesamtpreisminusrabatte) {
		this.nNettogesamtpreisminusrabatte = nNettogesamtpreisminusrabatte;
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

	public BigDecimal getNOffenemenge() {
		return this.nOffenemenge;
	}

	public void setNOffenemenge(BigDecimal nOffenemenge) {
		this.nOffenemenge = nOffenemenge;
	}

	public Date getTAuftragsbestaetigungstermin() {
		return this.tAuftragsbestaetigungstermin;
	}

	public void setTAuftragsbestaetigungstermin(
			Date tAuftragsbestaetigungstermin) {
		this.tAuftragsbestaetigungstermin = tAuftragsbestaetigungstermin;
	}

	public String getCAbkommentar() {
		return this.cAbkommentar;
	}

	public void setCAbkommentar(String cAbkommentar) {
		this.cAbkommentar = cAbkommentar;
	}

	public String getCAbnummer() {
		return this.cAbnummer;
	}

	public void setCAbnummer(String cAbnummer) {
		this.cAbnummer = cAbnummer;
	}

	public BigDecimal getNFixkosten() {
		return this.nFixkosten;
	}

	public void setNFixkosten(BigDecimal nFixkosten) {
		this.nFixkosten = nFixkosten;
	}

	public BigDecimal getNFixkostengeliefert() {
		return this.nFixkostengeliefert;
	}

	public void setNFixkostengeliefert(BigDecimal nFixkostengeliefert) {
		this.nFixkostengeliefert = nFixkostengeliefert;
	}

	public Timestamp getTManuellvollstaendiggeliefert() {
		return this.tManuellvollstaendiggeliefert;
	}

	public void setTManuellvollstaendiggeliefert(
			Timestamp tManuellvollstaendiggeliefert) {
		this.tManuellvollstaendiggeliefert = tManuellvollstaendiggeliefert;
	}

	public Integer getBestellpositionIIdRahmenposition() {
		return this.bestellpositionIIdRahmenposition;
	}

	public void setBestellpositionIIdRahmenposition(
			Integer bestellpositionIIdRahmenposition) {
		this.bestellpositionIIdRahmenposition = bestellpositionIIdRahmenposition;
	}

	public String getBestellpositionartCNr() {
		return this.bestellpositionartCNr;
	}

	public void setBestellpositionartCNr(String bestellpositionartCNr) {
		this.bestellpositionartCNr = bestellpositionartCNr;
	}

	public String getBestellpositionstatusCNr() {
		return this.bestellpositionstatusCNr;
	}

	public void setBestellpositionstatusCNr(String bestellpositionstatusCNr) {
		this.bestellpositionstatusCNr = bestellpositionstatusCNr;
	}

	public Integer getBestellungIId() {
		return this.bestellungIId;
	}

	public void setBestellungIId(Integer bestellungIId) {
		this.bestellungIId = bestellungIId;
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

	public Timestamp getTAbterminAendern() {
		return this.tAbterminAendern;
	}

	public void setTAbterminAendern(Timestamp tAbterminAendern) {
		this.tAbterminAendern = tAbterminAendern;
	}

	public Integer getPersonalIIdAbterminAendern() {
		return this.personalIIdAbterminAendern;
	}

	public void setPersonalIIdAbterminAendern(Integer personalIIdAbterminAendern) {
		this.personalIIdAbterminAendern = personalIIdAbterminAendern;
	}

	public Timestamp getTAbursprungstermin() {
		return this.tAbursprungstermin;
	}

	public void setTAbursprungstermin(Timestamp abursprungstermin) {
		tAbursprungstermin = abursprungstermin;
	}

	public Short getBNettopreisuebersteuert() {
		return this.bNettopreisuebersteuert;
	}

	public void setBNettopreisuebersteuert(Short bNettopreisuebersteuert) {
		this.bNettopreisuebersteuert = bNettopreisuebersteuert;
	}

}
