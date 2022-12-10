package com.lp.server.rechnung.service;

import java.io.Serializable;

import com.lp.server.partner.service.BankDto;
import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.PartnerbankDto;

public class LastschriftvorschlagKomplettDto extends LastschriftvorschlagDto implements Serializable {
	private static final long serialVersionUID = -7311037311418221541L;

	public RechnungDto rechnungDto;
	public KundeDto kundeDto;
	public PartnerbankDto partnerbankDto;
	public BankDto bankDto;
	
	public LastschriftvorschlagKomplettDto() {
	}

	public RechnungDto getRechnungDto() {
		return rechnungDto;
	}

	public void setRechnungDto(RechnungDto rechnungDto) {
		this.rechnungDto = rechnungDto;
	}

	public KundeDto getKundeDto() {
		return kundeDto;
	}

	public void setKundeDto(KundeDto kundeDto) {
		this.kundeDto = kundeDto;
	}

	public PartnerbankDto getPartnerbankDto() {
		return partnerbankDto;
	}

	public void setPartnerbankDto(PartnerbankDto partnerbankDto) {
		this.partnerbankDto = partnerbankDto;
	}

	public BankDto getBankDto() {
		return bankDto;
	}

	public void setBankDto(BankDto bankDto) {
		this.bankDto = bankDto;
	}

}
