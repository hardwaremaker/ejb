package com.lp.server.finanz.service;

import java.io.Serializable;

import com.lp.server.system.service.ImportProgressDto;

public class SepaImportInfoDto implements Serializable {

	private static final long serialVersionUID = 3222550574531362695L;

	private ImportProgressDto progressDto;

	public SepaImportInfoDto() {
	}

	public SepaImportInfoDto(ImportProgressDto progressDto) {
		setProgressDto(progressDto);
	}

	public ImportProgressDto getProgressDto() {
		return progressDto;
	}

	public void setProgressDto(ImportProgressDto progressDto) {
		this.progressDto = progressDto;
	}
	
}
