package com.lp.util;

import java.io.Serializable;

import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.finanz.service.KontoDto;
import com.lp.server.rechnung.service.RechnungDto;

public class SteuerkategorieDefinitionFehltException extends BelegdataBase implements Serializable {
	private static final long serialVersionUID = 6171746145025624214L;

	private KontoDto kontoDto ;
	
	public SteuerkategorieDefinitionFehltException(KontoDto kontoDto) {
		setKontoDto(kontoDto);
	}
	
	public SteuerkategorieDefinitionFehltException(EingangsrechnungDto erDto, KontoDto kontoDto) {
		super(erDto) ;
		setKontoDto(kontoDto);
	}

	public SteuerkategorieDefinitionFehltException(RechnungDto rechnungDto, KontoDto kontoDto) {
		super(rechnungDto) ; 
		setKontoDto(kontoDto);
	}

//	@Override
//	public Object[] asArray() {
//		return new Object[]{getKontoDto().getCNr()};
//	}
	
	public KontoDto getKontoDto() {
		return kontoDto;
	}
	public void setKontoDto(KontoDto kontoDto) {
		this.kontoDto = kontoDto;
	}
}
