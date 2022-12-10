package com.lp.util;

import com.lp.server.fertigung.service.LosDto;

public class LinienabrufProduktionAbliefermengeKleinerEinsExc extends EJBExceptionData {

	private static final long serialVersionUID = 7715173920800676288L;

	private LosDto losDto;
	
	public LinienabrufProduktionAbliefermengeKleinerEinsExc(LosDto losDto) {
		setLosDto(losDto);
	}
	
	public LosDto getLosDto() {
		return losDto;
	}
	
	public void setLosDto(LosDto losDto) {
		this.losDto = losDto;
	}
}
