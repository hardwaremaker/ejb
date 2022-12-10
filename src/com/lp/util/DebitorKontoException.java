package com.lp.util;

import com.lp.server.partner.service.KundeDto;

public class DebitorKontoException extends EJBExceptionData {
	private static final long serialVersionUID = -2892730084535669581L;

	private KundeDto kundeDto ;
	
	public DebitorKontoException(KundeDto kundeDto) {
		setKundeDto(kundeDto) ;
	}

	public KundeDto getKundeDto() {
		return kundeDto;
	}

	public void setKundeDto(KundeDto kundeDto) {
		this.kundeDto = kundeDto;
	}
}
