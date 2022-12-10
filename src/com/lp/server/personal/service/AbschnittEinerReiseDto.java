package com.lp.server.personal.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.lp.util.LPDatenSubreport;

public class AbschnittEinerReiseDto implements Serializable {
	
	
	private Double fVerrechenbar;

	public Double getFVerrechenbar() {
		return fVerrechenbar;
	}

	public void setFVerrechenbar(Double fVerrechenbar) {
		this.fVerrechenbar = fVerrechenbar;
	}
	
	Integer reiseIId;
	
	public Integer getReiseIId() {
		return reiseIId;
	}

	public void setReiseIId(Integer reiseIId) {
		this.reiseIId = reiseIId;
	}

	private BigDecimal bdAnteiligeKostenAusTageweisenDiaeten=BigDecimal.ZERO;
	
	public BigDecimal getBdAnteiligeKostenAusTageweisenDiaeten() {
		return bdAnteiligeKostenAusTageweisenDiaeten;
	}

	public void setBdAnteiligeKostenAusTageweisenDiaeten(BigDecimal bdAnteiligeKostenAusTageweisenDiaeten) {
		this.bdAnteiligeKostenAusTageweisenDiaeten = bdAnteiligeKostenAusTageweisenDiaeten;
	}

	private Double dDauer;
	
	public Double getdDauer() {
		return dDauer;
	}

	public void setdDauer(Double dDauer) {
		this.dDauer = dDauer;
	}

	private Integer diaetenIId_Haupteintrag;
	
	private String personalartCNr=null;

	public String getPersonalartCNr() {
		return personalartCNr;
	}

	public void setPersonalartCNr(String personalartCNr) {
		this.personalartCNr = personalartCNr;
	}

	public Integer getDiaetenIId_Haupteintrag() {
		return diaetenIId_Haupteintrag;
	}

	public void setDiaetenIId_Haupteintrag(Integer diaetenIId_Haupteintrag) {
		this.diaetenIId_Haupteintrag = diaetenIId_Haupteintrag;
	}

	private Integer diaetenIId;
	
	public Integer getDiaetenIId() {
		return diaetenIId;
	}

	public void setDiaetenIId(Integer diaetenIId) {
		this.diaetenIId = diaetenIId;
	}

	private Integer partnerIId;
	
	public Integer getPartnerIId() {
		return partnerIId;
	}

	public void setPartnerIId(Integer partnerIId) {
		this.partnerIId = partnerIId;
	}
	
	private Integer personalIId;
	
	public Integer getPersonalIId() {
		return personalIId;
	}

	public void setPersonalIId(Integer personalIId) {
		this.personalIId = personalIId;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public Timestamp gettBeginn() {
		return tBeginn;
	}

	public void settBeginn(Timestamp tBeginn) {
		this.tBeginn = tBeginn;
	}

	public Timestamp gettEnde() {
		return tEnde;
	}

	public void settEnde(Timestamp tEnde) {
		this.tEnde = tEnde;
	}

	public String getKommentar() {
		return kommentar;
	}

	public void setKommentar(String kommentar) {
		this.kommentar = kommentar;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public Integer getEntfernungInKm() {
		return entfernungInKm;
	}

	public void setEntfernungInKm(Integer entfernungInKm) {
		this.entfernungInKm = entfernungInKm;
	}

	public String getLand() {
		return land;
	}

	public void setLand(String land) {
		this.land = land;
	}

	public BigDecimal getSpesen() {
		return spesen;
	}

	public void setSpesen(BigDecimal spesen) {
		this.spesen = spesen;
	}

	public boolean isAusland() {
		return ausland;
	}

	public void setAusland(boolean ausland) {
		this.ausland = ausland;
	}

	public boolean isEchtesEnde() {
		return echtesEnde;
	}

	public void setEchtesEnde(boolean echtesEnde) {
		this.echtesEnde = echtesEnde;
	}

	public BigDecimal getTagessatz() {
		return tagessatz;
	}

	public void setTagessatz(BigDecimal tagessatz) {
		this.tagessatz = tagessatz;
	}

	public BigDecimal getStundensatz() {
		return stundensatz;
	}

	public void setStundensatz(BigDecimal stundensatz) {
		this.stundensatz = stundensatz;
	}

	public BigDecimal getMindestsatz() {
		return mindestsatz;
	}

	public void setMindestsatz(BigDecimal mindestsatz) {
		this.mindestsatz = mindestsatz;
	}

	public Integer getAbstunden() {
		return abstunden;
	}

	public void setAbstunden(Integer abstunden) {
		this.abstunden = abstunden;
	}

	public String getFahrzeug_privat() {
		return fahrzeug_privat;
	}

	public void setFahrzeug_privat(String fahrzeug_privat) {
		this.fahrzeug_privat = fahrzeug_privat;
	}

	public Integer getFahrzeugIId() {
		return fahrzeugIId;
	}

	public void setFahrzeugIId(Integer fahrzeugIId) {
		this.fahrzeugIId = fahrzeugIId;
	}

	public boolean isFehlerInKm() {
		return fehlerInKm;
	}

	public void setFehlerInKm(boolean fehlerInKm) {
		this.fehlerInKm = fehlerInKm;
	}

	public String getLkz() {
		return lkz;
	}

	public void setLkz(String lkz) {
		this.lkz = lkz;
	}

	public String getBelegartCNr() {
		return belegartCNr;
	}

	public void setBelegartCNr(String belegartCNr) {
		this.belegartCNr = belegartCNr;
	}

	public Integer getBelegartIId() {
		return belegartIId;
	}

	public void setBelegartIId(Integer belegartIId) {
		this.belegartIId = belegartIId;
	}

	public LPDatenSubreport getSubreportZusaetzlicheSpesen() {
		return subreportZusaetzlicheSpesen;
	}

	public void setSubreportZusaetzlicheSpesen(LPDatenSubreport subreportZusaetzlicheSpesen) {
		this.subreportZusaetzlicheSpesen = subreportZusaetzlicheSpesen;
	}

	private String tag;
	private Timestamp tBeginn;
	private Timestamp tEnde;
	
	private Timestamp tErledigt;
	

	public Timestamp getTErledigt() {
		return tErledigt;
	}

	public void setTErledigt(Timestamp tErledigt) {
		this.tErledigt = tErledigt;
	}

	private String kommentar;

	private String partner;

	private Integer entfernungInKm;

	private String land;

	private BigDecimal spesen;

	private boolean ausland;
	private boolean echtesEnde;
	private BigDecimal tagessatz;
	private BigDecimal stundensatz;
	private BigDecimal mindestsatz;
	
	private Integer iMitfahrer;
	
	public Integer getMitfahrer() {
		return iMitfahrer;
	}

	public void setMitfahrer(Integer iMitfahrer) {
		this.iMitfahrer = iMitfahrer;
	}

	private BigDecimal diaeten=BigDecimal.ZERO;
	
	private BigDecimal differenzDiaetenWennNichImBeginnLand=BigDecimal.ZERO;
	
	
	public BigDecimal getDifferenzDiaetenWennNichImBeginnLand() {
		return differenzDiaetenWennNichImBeginnLand;
	}

	public void setDifferenzDiaetenWennNichImBeginnLand(BigDecimal differenzDiaetenWennNichImBeginnLand) {
		this.differenzDiaetenWennNichImBeginnLand = differenzDiaetenWennNichImBeginnLand;
	}

	public BigDecimal getDiaeten() {
		return diaeten;
	}

	public void setDiaeten(BigDecimal diaeten) {
		this.diaeten = diaeten;
	}

	private Integer abstunden;
	private String fahrzeug_privat;
	private Integer fahrzeugIId;
	private boolean fehlerInKm;
	private String lkz;
	private String lkzHaupteintrag;
	public String getLkzHaupteintrag() {
		return lkzHaupteintrag;
	}

	public void setLkzHaupteintrag(String lkzHaupteintrag) {
		this.lkzHaupteintrag = lkzHaupteintrag;
	}

	private String belegartCNr;
	private String belegnummer;
	public String getBelegnummer() {
		return belegnummer;
	}

	public void setBelegnummer(String belegnummer) {
		this.belegnummer = belegnummer;
	}

	private Integer belegartIId;

	private LPDatenSubreport subreportZusaetzlicheSpesen;
	
	private String scriptname;

	public String getScriptname() {
		return scriptname;
	}

	public void setScriptname(String scriptname) {
		this.scriptname = scriptname;
	}
	
	
	private String fahrzeug_firma;
	private String fahrzeug_firma_kennzeichen;
	private BigDecimal fahrzeug_firma_kmkosten;
	private String fahrzeug_firma_verwendungsart;

	public String getFahrzeug_firma_verwendungsart() {
		return fahrzeug_firma_verwendungsart;
	}

	public void setFahrzeug_firma_verwendungsart(String fahrzeug_firma_verwendungsart) {
		this.fahrzeug_firma_verwendungsart = fahrzeug_firma_verwendungsart;
	}

	public String getFahrzeug_firma() {
		return fahrzeug_firma;
	}

	public void setFahrzeug_firma(String fahrzeug_firma) {
		this.fahrzeug_firma = fahrzeug_firma;
	}

	public String getFahrzeug_firma_kennzeichen() {
		return fahrzeug_firma_kennzeichen;
	}

	public void setFahrzeug_firma_kennzeichen(String fahrzeug_firma_kennzeichen) {
		this.fahrzeug_firma_kennzeichen = fahrzeug_firma_kennzeichen;
	}

	public BigDecimal getFahrzeug_firma_kmkosten() {
		return fahrzeug_firma_kmkosten;
	}

	public void setFahrzeug_firma_kmkosten(BigDecimal fahrzeug_firma_kmkosten) {
		this.fahrzeug_firma_kmkosten = fahrzeug_firma_kmkosten;
	}

}
