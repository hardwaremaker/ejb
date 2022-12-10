package com.lp.server.forecast.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lp.server.artikel.service.ArtikelDto;

public class ForecastpositionProduktionDto extends ForecastpositionDto {
	private static final long serialVersionUID = 5836306749871031865L;

	private List<LinienabrufProduktionDto> linienabrufe;
	private Integer fclieferadresseIId;
	private Map<Integer, ArtikelDto> hmArtikelLinienabrufe;
	private BigDecimal lagerstand;
	private Integer personalIIdKommissionierer;
	private String losCnr;
	private ForecastpositionWare warentyp;

	public ForecastpositionProduktionDto() {
	}

	public List<LinienabrufProduktionDto> getLinienabrufe() {
		if (linienabrufe == null) {
			linienabrufe = new ArrayList<LinienabrufProduktionDto>();
		}
		return linienabrufe;
	}

	public void setLinienabrufe(List<LinienabrufProduktionDto> linienabrufe) {
		this.linienabrufe = linienabrufe;
	}

	public Map<Integer, ArtikelDto> getHmArtikelLinienabrufe() {
		if (hmArtikelLinienabrufe == null) {
			hmArtikelLinienabrufe = new HashMap<Integer, ArtikelDto>();
		}
		return hmArtikelLinienabrufe;
	}

	public void setHmArtikelLinienabrufe(Map<Integer, ArtikelDto> hmArtikel) {
		this.hmArtikelLinienabrufe = hmArtikel;
	}

	public Integer getFclieferadresseIId() {
		return fclieferadresseIId;
	}

	public void setFclieferadresseIId(Integer fclieferadresseIId) {
		this.fclieferadresseIId = fclieferadresseIId;
	}

	public BigDecimal getLagerstand() {
		return lagerstand;
	}

	public void setLagerstand(BigDecimal lagerstand) {
		this.lagerstand = lagerstand;
	}

	public Integer getPersonalIIdKommissionierer() {
		return personalIIdKommissionierer;
	}

	public void setPersonalIIdKommissionierer(Integer personalIIdKommissionierer) {
		this.personalIIdKommissionierer = personalIIdKommissionierer;
	}

	public void setLosCnr(String losCnr) {
		this.losCnr = losCnr;
	}
	
	public String getLosCnr() {
		return losCnr;
	}

	public ForecastpositionWare getWarentyp() {
		return warentyp;
	}

	public void setWarentyp(ForecastpositionWare warentyp) {
		this.warentyp = warentyp;
	}
}
