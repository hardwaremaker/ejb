package com.lp.server.rechnung.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;

public class AbrechnungsvorschlagDto implements Serializable {
	public String getMandantCNr() {
		return mandantCNr;
	}

	public void setMandantCNr(String mandantCNr) {
		this.mandantCNr = mandantCNr;
	}

	private Integer auftragpositionIId;

	public Integer getAuftragpositionIId() {
		return auftragpositionIId;
	}

	public void setAuftragpositionIId(Integer auftragpositionIId) {
		this.auftragpositionIId = auftragpositionIId;
	}

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

	private BigDecimal nKilometerGesamt;
	private BigDecimal nKilometerVerrechenbar;
	private BigDecimal nKilometerOffen;

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

	private BigDecimal nStundenVerrechenbar;

	public BigDecimal getNBetragOffen() {
		return nBetragOffen;
	}

	public void setNBetragOffen(BigDecimal nBetragOffen) {
		this.nBetragOffen = nBetragOffen;
	}

	public BigDecimal getNStundenOffen() {
		return nStundenOffen;
	}

	public void setNStundenOffen(BigDecimal nStundenOffen) {
		this.nStundenOffen = nStundenOffen;
	}

	private BigDecimal nBetragGesamt;

	public BigDecimal getNBetragGesamt() {
		return nBetragGesamt;
	}

	public void setNBetragGesamt(BigDecimal nBetragGesamt) {
		this.nBetragGesamt = nBetragGesamt;
	}

	public BigDecimal getNStundenGesamt() {
		return nStundenGesamt;
	}

	public void setNStundenGesamt(BigDecimal nStundenGesamt) {
		this.nStundenGesamt = nStundenGesamt;
	}

	private BigDecimal nStundenGesamt;

	private Integer kundeIId;

	public Integer getKundeIId() {
		return kundeIId;
	}

	public void setKundeIId(Integer kundeIId) {
		this.kundeIId = kundeIId;
	}

	private Timestamp tVon;
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

	private Double fVerrechenbar;

	public Double getFVerrechenbar() {
		return fVerrechenbar;
	}

	public void setFVerrechenbar(Double fVerrechenbar) {
		this.fVerrechenbar = fVerrechenbar;
	}

	private Integer personalIId;

	private Integer maschinenzeitdatenIId;

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

	private Integer projektIId;

	public Integer getProjektIId() {
		return projektIId;
	}

	public void setProjektIId(Integer projektIId) {
		this.projektIId = projektIId;
	}

	private Integer telefonzeitenIId;

	public Integer getTelefonzeitenIId() {
		return telefonzeitenIId;
	}

	public void setTelefonzeitenIId(Integer telefonzeitenIId) {
		this.telefonzeitenIId = telefonzeitenIId;
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

	private String waehrungCNrRechnung;
	
	public String getWaehrungCNrRechnung() {
		return waehrungCNrRechnung;
	}

	public void setWaehrungCNrRechnung(String waehrungCNrRechnung) {
		this.waehrungCNrRechnung = waehrungCNrRechnung;
	}

	
	private Integer iId;
	private String mandantCNr;
	private BigDecimal nBetragOffen;
	private BigDecimal nStundenOffen;
	private Timestamp tAnlegen;
	private Integer auftragIId;
	private Integer losIId;
	private Integer zeitdatenIId;
	private Integer reiseIId;
	private Integer auftragszuordnungIId;

	private static final long serialVersionUID = 1L;

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
	private BigDecimal nSpesenGesamt;
	private BigDecimal nSpesenVerrechenbar;
	private BigDecimal nSpesenOffen;

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
	
}
