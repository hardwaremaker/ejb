package com.lp.server.forecast.service;

public class LinienabrufArtikelbuchungDto extends ArtikelbuchungDto {
	private static final long serialVersionUID = -3166979979124884946L;

	private Integer linienabrufIId;
	
	public LinienabrufArtikelbuchungDto() {
	}

	public Integer getLinienabrufIId() {
		return linienabrufIId;
	}

	public void setLinienabrufIId(Integer linienabrufIId) {
		this.linienabrufIId = linienabrufIId;
	}
}
