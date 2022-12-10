package com.lp.server.fertigung.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.TreeMap;

import com.lp.server.artikel.service.ArtikelDto;
import com.lp.server.stueckliste.service.StuecklisteDto;

public class TraceImportDto implements Serializable{

	private Integer stuecklisteIId;
	
	private StuecklisteDto stuecklisteDto;
	
	private ArtikelDto artikelDtoMaterial;
	
	public ArtikelDto getArtikelDtoMaterial() {
		return artikelDtoMaterial;
	}
	public void setArtikelDtoMaterial(ArtikelDto artikelDtoMaterial) {
		this.artikelDtoMaterial = artikelDtoMaterial;
	}
	public StuecklisteDto getStuecklisteDto() {
		return stuecklisteDto;
	}
	public void setStuecklisteDto(StuecklisteDto stuecklisteDto) {
		this.stuecklisteDto = stuecklisteDto;
	}
	
	private BigDecimal anzahlPcb;
	
	public BigDecimal getAnzahlPcb() {
		return anzahlPcb;
	}
	public void setAnzahlPcb(BigDecimal anzahlPcb) {
		this.anzahlPcb = anzahlPcb;
	}

	private String pcbNummer;
	
	public String getPcbNummer() {
		return pcbNummer;
	}
	public void setPcbNummer(String pcbNummer) {
		this.pcbNummer = pcbNummer;
	}

	private String chargennummer;
	private BigDecimal zuBuchendeMenge;
	private BigDecimal mengeAufHauptlager;
	public BigDecimal getMengeAufHauptlager() {
		return mengeAufHauptlager;
	}
	public void setMengeAufHauptlager(BigDecimal mengeAufHauptlager) {
		this.mengeAufHauptlager = mengeAufHauptlager;
	}
	private TreeMap<String,LossollmaterialDto>  sollmaterialoffeneLose=new TreeMap<String,LossollmaterialDto> ();
	
	public Integer getStuecklisteIId() {
		return stuecklisteIId;
	}
	public void setStuecklisteIId(Integer stuecklisteIId) {
		this.stuecklisteIId = stuecklisteIId;
	}
	public String getChargennummer() {
		return chargennummer;
	}
	public void setChargennummer(String chargennummer) {
		this.chargennummer = chargennummer;
	}
	public BigDecimal getZuBuchendeMenge() {
		return zuBuchendeMenge;
	}
	public void setZuBuchendeMenge(BigDecimal zuBuchendeMenge) {
		this.zuBuchendeMenge = zuBuchendeMenge;
	}
	public TreeMap<String,LossollmaterialDto> getSollmaterialoffeneLose() {
		return sollmaterialoffeneLose;
	}

}
