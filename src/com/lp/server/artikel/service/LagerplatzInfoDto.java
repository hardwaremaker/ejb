package com.lp.server.artikel.service;

import java.io.Serializable;
import java.util.List;

public class LagerplatzInfoDto implements Serializable {
	private static final long serialVersionUID = 1608332849184999500L;

	private LagerDto lagerDto;
	private List<LagerplatzDto> lagerplaetze;
	
	public LagerDto getLagerDto() {
		return lagerDto;
	}
	
	public void setLagerDto(LagerDto lagerDto) {
		this.lagerDto = lagerDto;
	}
	
	public List<LagerplatzDto> getLagerplaetze() {
		return lagerplaetze;
	}
	
	public void setLagerplaetze(List<LagerplatzDto> lagerplaetze) {
		this.lagerplaetze = lagerplaetze;
	}
}
