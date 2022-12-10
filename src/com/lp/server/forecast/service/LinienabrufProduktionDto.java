package com.lp.server.forecast.service;

import java.util.ArrayList;
import java.util.List;

public class LinienabrufProduktionDto extends LinienabrufDto {

	private static final long serialVersionUID = -3344171375286955619L;

	private List<LinienabrufArtikelDto> linienabrufArtikel;
	
	public LinienabrufProduktionDto() {
	}

	public List<LinienabrufArtikelDto> getLinienabrufArtikel() {
		if (linienabrufArtikel == null) {
			linienabrufArtikel = new ArrayList<LinienabrufArtikelDto>();
		}
		return linienabrufArtikel;
	}

	public void setLinienabrufArtikel(List<LinienabrufArtikelDto> linienabrufArtikel) {
		this.linienabrufArtikel = linienabrufArtikel;
	}

}
