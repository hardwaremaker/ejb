package com.lp.server.finanz.service;

import java.io.Serializable;
import java.util.List;

public class BuchungKompakt implements Serializable {

	private static final long serialVersionUID = 84909966300930640L;

	private BuchungDto buchungDto;
	private List<Iso20022BuchungdetailDto> buchungdetailList;

	public BuchungKompakt() {
	}
	
	public BuchungKompakt(BuchungDto buchungDto, List<Iso20022BuchungdetailDto> buchungdetailList) {
		this.buchungDto = buchungDto;
		this.buchungdetailList = buchungdetailList;
	}

	public BuchungDto getBuchungDto() {
		return buchungDto;
	}

	public void setBuchungDto(BuchungDto buchungDto) {
		this.buchungDto = buchungDto;
	}

	public List<Iso20022BuchungdetailDto> getBuchungdetailList() {
		return buchungdetailList;
	}

	public void setBuchungdetailList(List<Iso20022BuchungdetailDto> buchungdetailList) {
		this.buchungdetailList = buchungdetailList;
	}
	
	
}
