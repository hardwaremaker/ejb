
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
import javax.persistence.Table;

import com.lp.util.Helper;

@Entity
@Table(name = "RECH_ABRECHNUNGSVORSCHLAG")
public class Abrechnungsvorschlag implements Serializable {
	public String getMandantCNr() {
		return mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	public BigDecimal getNBetragOffen() {
		return nBetragOffen;
	}

	public void setNBetragOffen(BigDecimal nBetragOffen) {
		if (nBetragOffen != null) {
			nBetragOffen = Helper.rundeKaufmaennisch(nBetragOffen, 2);
		}
		this.nBetragOffen = nBetragOffen;
	}

	public BigDecimal getNStundenOffen() {
		return nStundenOffen;
	}

	public void setNStundenOffen(BigDecimal nStundenOffen) {
		nStundenOffen = Helper.rundeKaufmaennisch(nStundenOffen, 3);
		this.nStundenOffen = nStundenOffen;
	}

	public Integer getLosIId() {
		return losIId;
	}

	public void setLosIId(Integer losIId) {
		this.losIId = losIId;
	}

	public Integer getZeitdatenIId() {
		return zeitdatenIId;
	}

	public void setZeitdatenIId(Integer zeitdatenIId) {
		this.zeitdatenIId = zeitdatenIId;
	}

	public Integer getReiseIId() {
		return reiseIId;
	}

	public void setReiseIId(Integer reiseIId) {
		this.reiseIId = reiseIId;
	}

	public Integer getAuftragszuordnungIId() {
		return auftragszuordnungIId;
	}

	public void setAuftragszuordnungIId(Integer auftragszuordnungIId) {
		this.auftragszuordnungIId = auftragszuordnungIId;
	}

	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "MANDANT_C_NR")
	private String mandantCNr;

	@Column(name = "WAEHRUNG_C_NR_RECHNUNG")
	private String waehrungCNrRechnung;
	
	public String getWaehrungCNrRechnung() {
		return waehrungCNrRechnung;
	}

	public void setWaehrungCNrRechnung(String waehrungCNrRechnung) {
		this.waehrungCNrRechnung = waehrungCNrRechnung;
	}

	@Column(name = "AUFTRAGPOSITION_I_ID")
	private Integer auftragpositionIId;

	public Integer getAuftragpositionIId() {
		return auftragpositionIId;
	}

	public void setAuftragpositionIId(Integer auftragpositionIId) {
		this.auftragpositionIId = auftragpositionIId;
	}

	@Column(name = "N_BETRAG_VERRECHENBAR")
	private BigDecimal nBetragVerrechenbar;

	public BigDecimal getNBetragVerrechenbar() {
		return nBetragVerrechenbar;
	}

	public void setNBetragVerrechenbar(BigDecimal nBetragVerrechenbar) {
		this.nBetragVerrechenbar = nBetragVerrechenbar;
	}

	public BigDecimal getNStundenVerrechenbar() {
		return nStundenVerrechenbar;
	}

	public void setNStundenVerrechenbar(BigDecimal nStundenVerrechenbar) {
		this.nStundenVerrechenbar = nStundenVerrechenbar;
	}

	@Column(name = "N_KILOMETER_GESAMT")
	private BigDecimal nKilometerGesamt;
	@Column(name = "N_KILOMETER_VERRECHENBAR")
	private BigDecimal nKilometerVerrechenbar;
	@Column(name = "N_KILOMETER_OFFEN")
	private BigDecimal nKilometerOffen;
	
	
	@Column(name = "N_SPESEN_GESAMT")
	private BigDecimal nSpesenGesamt;
	public BigDecimal getNSpesenGesamt() {
		return nSpesenGesamt;
	}

	public void setNSpesenGesamt(BigDecimal nSpesenGesamt) {
		this.nSpesenGesamt = nSpesenGesamt;
	}

	public BigDecimal getNSpesenVerrechenbar() {
		return nSpesenVerrechenbar;
	}

	public void setNSpesenVerrechenbar(BigDecimal nSpesenVerrechenbar) {
		this.nSpesenVerrechenbar = nSpesenVerrechenbar;
	}

	public BigDecimal getNSpesenOffen() {
		return nSpesenOffen;
	}

	public void setNSpesenOffen(BigDecimal nSpesenOffen) {
		this.nSpesenOffen = nSpesenOffen;
	}

	@Column(name = "N_SPESEN_VERRECHENBAR")
	private BigDecimal nSpesenVerrechenbar;
	@Column(name = "N_SPESEN_OFFEN")
	private BigDecimal nSpesenOffen;

	public BigDecimal getNKilometerGesamt() {
		return nKilometerGesamt;
	}

	public void setNKilometerGesamt(BigDecimal nKilometerGesamt) {
		this.nKilometerGesamt = nKilometerGesamt;
	}

	public BigDecimal getNKilometerVerrechenbar() {
		return nKilometerVerrechenbar;
	}

	public void setNKilometerVerrechenbar(BigDecimal nKilometerVerrechenbar) {
		this.nKilometerVerrechenbar = nKilometerVerrechenbar;
	}

	public BigDecimal getNKilometerOffen() {
		return nKilometerOffen;
	}

	public void setNKilometerOffen(BigDecimal nKilometerOffen) {
		this.nKilometerOffen = nKilometerOffen;
	}

	@Column(name = "N_STUNDEN_VERRECHENBAR")
	private BigDecimal nStundenVerrechenbar;

	@Column(name = "N_BETRAG_OFFEN")
	private BigDecimal nBetragOffen;

	@Column(name = "N_STUNDEN_OFFEN")
	private BigDecimal nStundenOffen;

	@Column(name = "N_BETRAG_GESAMT")
	private BigDecimal nBetragGesamt;

	public BigDecimal getNBetragGesamt() {
		return nBetragGesamt;
	}

	public void setNBetragGesamt(BigDecimal nBetragGesamt) {
		nBetragGesamt = Helper.rundeKaufmaennisch(nBetragGesamt, 2);
		this.nBetragGesamt = nBetragGesamt;
	}

	public BigDecimal getNStundenGesamt() {
		return nStundenGesamt;
	}

	public void setNStundenGesamt(BigDecimal nStundenGesamt) {
		nStundenGesamt = Helper.rundeKaufmaennisch(nStundenGesamt, 3);
		this.nStundenGesamt = nStundenGesamt;
	}

	@Column(name = "N_STUNDEN_GESAMT")
	private BigDecimal nStundenGesamt;

	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "AUFTRAG_I_ID")
	private Integer auftragIId;

	@Column(name = "LOS_I_ID")
	private Integer losIId;

	@Column(name = "PERSONAL_I_ID")
	private Integer personalIId;

	@Column(name = "MASCHINENZEITDATEN_I_ID")
	private Integer maschinenzeitdatenIId;

	@Column(name = "B_VERRECHNET")
	private Short bVerrechnet;

	public Integer getPersonalIId() {
		return personalIId;
	}

	public void setPersonalIId(Integer personalIId) {
		this.personalIId = personalIId;
	}

	public Integer getMaschinenzeitdatenIId() {
		return maschinenzeitdatenIId;
	}

	public void setMaschinenzeitdatenIId(Integer maschinenzeitdatenIId) {
		this.maschinenzeitdatenIId = maschinenzeitdatenIId;
	}

	public Short getBVerrechnet() {
		return bVerrechnet;
	}

	public void setBVerrechnet(Short bVerrechnet) {
		this.bVerrechnet = bVerrechnet;
	}

	@Column(name = "KUNDE_I_ID")
	private Integer kundeIId;

	public Integer getKundeIId() {
		return kundeIId;
	}

	public void setKundeIId(Integer kundeIId) {
		this.kundeIId = kundeIId;
	}

	@Column(name = "F_VERRECHENBAR")
	private Double fVerrechenbar;

	public Double getFVerrechenbar() {
		return fVerrechenbar;
	}

	public void setFVerrechenbar(Double fVerrechenbar) {
		this.fVerrechenbar = fVerrechenbar;
	}

	@Column(name = "ZEITDATEN_I_ID")
	private Integer zeitdatenIId;

	@Column(name = "REISE_I_ID")
	private Integer reiseIId;

	@Column(name = "PROJEKT_I_ID")
	private Integer projektIId;

	public Integer getProjektIId() {
		return projektIId;
	}

	public void setProjektIId(Integer projektIId) {
		this.projektIId = projektIId;
	}

	@Column(name = "AUFTRAGSZUORDNUNG_I_ID")
	private Integer auftragszuordnungIId;

	@Column(name = "TELEFONZEITEN_I_ID")
	private Integer telefonzeitenIId;

	public Integer getTelefonzeitenIId() {
		return telefonzeitenIId;
	}

	public void setTelefonzeitenIId(Integer telefonzeitenIId) {
		this.telefonzeitenIId = telefonzeitenIId;
	}

	@Column(name = "T_VON")
	private Timestamp tVon;
	@Column(name = "T_BIS")
	private Timestamp tBis;

	public Timestamp getTVon() {
		return tVon;
	}

	public void setTVon(Timestamp tVon) {
		this.tVon = tVon;
	}

	public Timestamp getTBis() {
		return tBis;
	}

	public void setTBis(Timestamp tBis) {
		this.tBis = tBis;
	}

	private static final long serialVersionUID = 1L;

	public Abrechnungsvorschlag() {
		super();
	}

	public Abrechnungsvorschlag(Integer id, String mandantCNr, Timestamp tVon, Timestamp tAnlegen, Short bVerrechnet,
			Double fVerrechenbar, String waehrungCNrRechnung) {
		setIId(id);
		setMandantCNr(mandantCNr);
		setTVon(tVon);
		setTAnlegen(tAnlegen);
		setBVerrechnet(bVerrechnet);
		setFVerrechenbar(fVerrechenbar);
		setWaehrungCNrRechnung(waehrungCNrRechnung);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public Timestamp getTAnlegen() {
		return this.tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	public Integer getAuftragIId() {
		return this.auftragIId;
	}

	public void setAuftragIId(Integer auftragIId) {
		this.auftragIId = auftragIId;
	}

}
