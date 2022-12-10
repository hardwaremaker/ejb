package com.lp.util;

import com.lp.server.finanz.service.SepakontoauszugDto;

public class SepakontoauszugNichtImAktuellenGJException extends EJBExceptionData {
	private static final long serialVersionUID = -5215827941452701585L;

	private SepakontoauszugDto sepakontoauszugDto;
	private Integer geschaeftsjahr;

	public SepakontoauszugNichtImAktuellenGJException(SepakontoauszugDto sepakontoauszugDto, Integer geschaeftsjahr) {
		this.sepakontoauszugDto = sepakontoauszugDto;
		this.geschaeftsjahr = geschaeftsjahr;
	}
	
	public SepakontoauszugDto getSepakontoauszugDto() {
		return sepakontoauszugDto;
	}

	public void setSepakontoauszugDto(SepakontoauszugDto sepakontoauszugDto) {
		this.sepakontoauszugDto = sepakontoauszugDto;
	}

	public Integer getGeschaeftsjahr() {
		return geschaeftsjahr;
	}
	
	public void setGeschaeftsjahr(Integer geschaeftsjahr) {
		this.geschaeftsjahr = geschaeftsjahr;
	}
}
