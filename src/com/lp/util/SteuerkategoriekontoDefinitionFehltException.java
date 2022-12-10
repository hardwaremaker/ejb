package com.lp.util;

import java.io.Serializable;

import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.finanz.service.SteuerkategorieDto;
import com.lp.server.rechnung.service.RechnungDto;

public class SteuerkategoriekontoDefinitionFehltException extends BelegdataBase implements Serializable {
	private static final long serialVersionUID = 5678221676208738773L;

	private SteuerkategorieDto steuerkategorieDto ;
	private Integer mwstsatzbezId ;
	
	public SteuerkategoriekontoDefinitionFehltException(SteuerkategorieDto stkDto, Integer mwstsatzbezId) {
		setSteuerkategorieDto(stkDto);
		setMwstsatzbezId(mwstsatzbezId);
	}

	public SteuerkategoriekontoDefinitionFehltException(EingangsrechnungDto erDto, SteuerkategorieDto stkDto, Integer mwstsatzbezId) {
		super(erDto);
		setSteuerkategorieDto(stkDto);
		setMwstsatzbezId(mwstsatzbezId);
	}

	public SteuerkategoriekontoDefinitionFehltException(RechnungDto rechnungDto, SteuerkategorieDto stkDto, Integer mwstsatzbezId) {
		super(rechnungDto);
		setSteuerkategorieDto(stkDto);
		setMwstsatzbezId(mwstsatzbezId);
	}
	
	public SteuerkategorieDto getSteuerkategorieDto() {
		return steuerkategorieDto;
	}

	public void setSteuerkategorieDto(SteuerkategorieDto steuerkategorieDto) {
		this.steuerkategorieDto = steuerkategorieDto;
	}

	public Integer getMwstsatzbezId() {
		return mwstsatzbezId;
	}

	public void setMwstsatzbezId(Integer mwstsatzbezId) {
		this.mwstsatzbezId = mwstsatzbezId;
	}
}
