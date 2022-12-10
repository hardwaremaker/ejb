package com.lp.server.rechnung.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class ZeileFuerAbrechnungsvorschlagUeberleitungVerdichtetNachArtikelOderAuftragspositionDto implements Serializable {
	BigDecimal bdZuverrechnenVomClient = null;

	BigDecimal bdPreisVomClient = null;

	private DauerUndZeitraumDto dauerUndZeitraumDto;

	public BigDecimal getBdPreisVomClient() {
		return bdPreisVomClient;
	}

	public void setBdPreisVomClient(BigDecimal bdPreisVomClient) {
		this.bdPreisVomClient = bdPreisVomClient;
	}

	String kommentarVomClient = null;

	public String getKommentarVomClient() {
		return kommentarVomClient;
	}

	public void setKommentarVomClient(String kommentarVomClient) {
		this.kommentarVomClient = kommentarVomClient;
	}

	public BigDecimal getBdZuverrechnenVomClient() {
		return bdZuverrechnenVomClient;
	}

	public void setBdZuverrechnenVomClient(BigDecimal bdZuverrechnenVomClient) {
		this.bdZuverrechnenVomClient = bdZuverrechnenVomClient;
	}

	public BigDecimal getBdOffen() {
		return bdOffen;
	}

	private Integer artikelIId;

	public Integer getArtikelIId() {
		return artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

	private Integer auftragspositionIId;

	public Integer getAuftragspositionIId() {
		return auftragspositionIId;
	}

	private BigDecimal bdOffen = BigDecimal.ZERO;
	private ArrayList<AbrechnungsvorschlagDto> alBetroffeneAV = new ArrayList<AbrechnungsvorschlagDto>();

	public ZeileFuerAbrechnungsvorschlagUeberleitungVerdichtetNachArtikelOderAuftragspositionDto(Integer artikelIId,
			Integer auftragspositionIId, BigDecimal bdOffen, AbrechnungsvorschlagDto avDto,
			DauerUndZeitraumDto dauerUndZeitraumDto) {
		this.artikelIId = artikelIId;
		this.auftragspositionIId = auftragspositionIId;
		this.bdOffen = bdOffen;
		this.dauerUndZeitraumDto = dauerUndZeitraumDto;
		alBetroffeneAV.add(avDto);

	}

	public DauerUndZeitraumDto getDauerUndZeitraumDto() {
		return dauerUndZeitraumDto;
	}

	public ArrayList<AbrechnungsvorschlagDto> getAlBetroffeneAV() {
		return alBetroffeneAV;
	}

	public void add2Offen(BigDecimal bdOffen) {
		this.bdOffen = this.bdOffen.add(bdOffen);
	}

	public void add2Zeitraeume(Integer zeitdatenIId, Timestamp tVon, Timestamp tBis, BigDecimal bdDauer) {

		if (dauerUndZeitraumDto == null) {
			dauerUndZeitraumDto = new DauerUndZeitraumDto();
		}

		dauerUndZeitraumDto.add2Zeitraeume(zeitdatenIId, tVon, tBis, bdDauer);
	}

	public void add2BetroffenAV(AbrechnungsvorschlagDto avDto) {
		boolean bGefunden = false;
		for (int i = 0; i < alBetroffeneAV.size(); i++) {
			if (alBetroffeneAV.get(i).getIId().equals(avDto.getIId())) {
				bGefunden = true;
			}
		}
		if (bGefunden == false) {
			alBetroffeneAV.add(avDto);
		}

	}

}
