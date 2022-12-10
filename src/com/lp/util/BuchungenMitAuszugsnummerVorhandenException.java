package com.lp.util;

import com.lp.server.finanz.service.SepakontoauszugDto;

public class BuchungenMitAuszugsnummerVorhandenException extends EJBExceptionData {

	private static final long serialVersionUID = 7185828618599413483L;

	private SepakontoauszugDto sepakontoauszugDto; 
	
	public BuchungenMitAuszugsnummerVorhandenException(SepakontoauszugDto sepakontoauszugDto) {
		this.sepakontoauszugDto = sepakontoauszugDto;
	}

	public SepakontoauszugDto getSepakontoauszugDto() {
		return sepakontoauszugDto;
	}

	public void setSepakontoauszugDto(SepakontoauszugDto sepakontoauszugDto) {
		this.sepakontoauszugDto = sepakontoauszugDto;
	}

}
