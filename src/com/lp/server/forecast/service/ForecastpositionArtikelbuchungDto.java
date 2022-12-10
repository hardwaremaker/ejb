package com.lp.server.forecast.service;

public class ForecastpositionArtikelbuchungDto extends ArtikelbuchungDto {
	private static final long serialVersionUID = -7243695776470159650L;

	private Integer forecastpositionIId;
	
	public ForecastpositionArtikelbuchungDto() {
	}

	public Integer getForecastpositionIId() {
		return forecastpositionIId;
	}

	public void setForecastpositionIId(Integer forecastpositionIId) {
		this.forecastpositionIId = forecastpositionIId;
	}

}
