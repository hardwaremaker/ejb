package com.lp.server.finanz.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SepaImportProperties implements Serializable {
	private static final long serialVersionUID = -751245964437383713L;

	private Integer iAuszug;
	private List<SepaImportSourceData> sources;
	private BankverbindungDto bvDto;
	
	public SepaImportProperties() {
	}

	public Integer getAuszugsnummer() {
		return iAuszug;
	}

	public void setAuszugsnummer(Integer iAuszug) {
		this.iAuszug = iAuszug;
	}

	public BankverbindungDto getBankverbindungDto() {
		return bvDto;
	}

	public void setBankverbindungDto(BankverbindungDto bvDto) {
		this.bvDto = bvDto;
	}
	
	public List<SepaImportSourceData> getSources() {
		if (sources == null) {
			sources = new ArrayList<SepaImportSourceData>();
		}
		return sources;
	}
	
	public void setSources(List<SepaImportSourceData> sources) {
		this.sources = sources;
	}
}
