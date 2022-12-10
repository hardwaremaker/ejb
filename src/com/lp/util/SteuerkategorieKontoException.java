package com.lp.util;

import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.finanz.service.SteuerkategoriekontoDto;
import com.lp.server.rechnung.service.RechnungDto;

public class SteuerkategorieKontoException extends BelegdataBase {
	private static final long serialVersionUID = -6909995051193644222L;

	private SteuerkategoriekontoDto steuerkategoriekontoDto ;
	private Integer reversechargeartId ;

	public SteuerkategorieKontoException(Integer reversechargeartId, SteuerkategoriekontoDto stkkDto) {
		setSteuerkategoriekontoDto(stkkDto); 
		setReversechargeartId(reversechargeartId);
	}
	
	public SteuerkategorieKontoException(EingangsrechnungDto erDto, Integer reversechargeartId, SteuerkategoriekontoDto stkkDto) {
		super(erDto) ;
		setSteuerkategoriekontoDto(stkkDto); 
		setReversechargeartId(reversechargeartId);
	}

	public SteuerkategorieKontoException(RechnungDto rechnungDto, Integer reversechargeartId, SteuerkategoriekontoDto stkkDto) {
		super(rechnungDto) ;
		setSteuerkategoriekontoDto(stkkDto); 
		setReversechargeartId(reversechargeartId);
	}
	
	public SteuerkategoriekontoDto getSteuerkategoriekontoDto() {
		return steuerkategoriekontoDto;
	}

	public void setSteuerkategoriekontoDto(SteuerkategoriekontoDto steuerkategoriekontoDto) {
		this.steuerkategoriekontoDto = steuerkategoriekontoDto;
	}

	public Integer getReversechargeartId() {
		return reversechargeartId;
	}

	public void setReversechargeartId(Integer reversechargeartId) {
		this.reversechargeartId = reversechargeartId;
	}
}
