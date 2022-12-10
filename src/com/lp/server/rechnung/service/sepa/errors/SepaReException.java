package com.lp.server.rechnung.service.sepa.errors;

import com.lp.server.rechnung.service.RechnungDto;

public class SepaReException extends SepaException implements ISepaReException {
	private static final long serialVersionUID = -7270899240516557866L;

	private RechnungDto rechnungDto;

	public SepaReException(RechnungDto rechnungDto, String message, Throwable throwable) {
		super(message, throwable);
		setRechnungDto(rechnungDto);
	}

	public SepaReException(RechnungDto rechnungDto, String message) {
		this(rechnungDto, message, null);
	}

	public SepaReException(RechnungDto rechnungDto, Throwable throwable) {
		this(rechnungDto, null, throwable);
	}

	public RechnungDto getRechnungDto() {
		return rechnungDto;
	}
	
	public void setRechnungDto(RechnungDto rechnungDto) {
		this.rechnungDto = rechnungDto;
	}
}
