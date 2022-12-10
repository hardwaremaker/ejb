package com.lp.util;

import java.io.Serializable;

import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.finanz.service.SteuerkategorieDto;
import com.lp.server.rechnung.service.RechnungDto;

public class SteuerkategorieBasiskontoException extends BelegdataBase implements Serializable {
	private static final long serialVersionUID = -5248453644299809789L;

	private SteuerkategorieDto steuerkategorieDto ;
	
	public SteuerkategorieBasiskontoException(SteuerkategorieDto stkDto) {
		setSteuerkategorieDto(stkDto);
	}
	
	public SteuerkategorieBasiskontoException(EingangsrechnungDto erDto, SteuerkategorieDto stkDto) {
		super(erDto) ;
		setSteuerkategorieDto(stkDto); 
	}

	public SteuerkategorieBasiskontoException(RechnungDto rechnungDto, SteuerkategorieDto stkDto) {
		super(rechnungDto) ;
		setSteuerkategorieDto(stkDto); 
	}

	public SteuerkategorieDto getSteuerkategorieDto() {
		return steuerkategorieDto;
	}

	public void setSteuerkategorieDto(SteuerkategorieDto steuerkategorieDto) {
		this.steuerkategorieDto = steuerkategorieDto;
	}
	
//	@Override
//	public Object[] asArray() {
//		return new Object[]{getSteuerkategorieDto()};
//	}
}
