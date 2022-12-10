package com.lp.server.finanz.service;

import java.io.Serializable;

public class BuchungInfoDto implements Serializable {
	private static final long serialVersionUID = -7000766922404559330L;

	private BuchungDto buchungDto;
	private FibuexportDto[] exportDtos;
	
	public BuchungInfoDto() {
	}

	public BuchungInfoDto(BuchungDto buchungDto) {
		setBuchungDto(buchungDto);
	}
	
	public BuchungInfoDto(BuchungDto buchungDto, FibuexportDto[] exportDtos) {
		setBuchungDto(buchungDto);
		setExportDtos(exportDtos);
	}
	
	public BuchungDto getBuchungDto() {
		return buchungDto;
	}

	public void setBuchungDto(BuchungDto buchungDto) {
		this.buchungDto = buchungDto;
	}

	public FibuexportDto[] getExportDtos() {
		return exportDtos;
	}

	public void setExportDtos(FibuexportDto[] exportDtos) {
		this.exportDtos = exportDtos;
	}
	
	public boolean hatWarnungen() {
		if(exportDtos == null || exportDtos.length == 0) return false;
		for (FibuexportDto fibuexportDto : exportDtos) {
			if(fibuexportDto.getUstWarnungDto()
					.hasWarnung()) return true;
		}
		return false;
	}
}
