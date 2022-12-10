package com.lp.server.finanz.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Iso20022PaymentsDto implements Serializable {
	private static final long serialVersionUID = 125502175467113871L;

	private Iso20022StandardDto standardDto;
	private List<Iso20022ZahlungauftragDto> zahlungsauftragDtos;
	private List<Iso20022LastschriftDto> lastschriftDtos;
	
	public Iso20022StandardDto getStandardDto() {
		return standardDto;
	}
	public void setStandardDto(Iso20022StandardDto standardDto) {
		this.standardDto = standardDto;
	}
	
	public List<Iso20022ZahlungauftragDto> getZahlungsauftragDtos() {
		if (zahlungsauftragDtos == null) {
			zahlungsauftragDtos = new ArrayList<Iso20022ZahlungauftragDto>();
		}
		return zahlungsauftragDtos;
	}
	
	public List<Iso20022LastschriftDto> getLastschriftDtos() {
		if (lastschriftDtos == null) {
			lastschriftDtos = new ArrayList<Iso20022LastschriftDto>();
		}
		return lastschriftDtos;
	}
}
