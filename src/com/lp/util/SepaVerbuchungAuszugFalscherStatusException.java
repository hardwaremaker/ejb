package com.lp.util;

import com.lp.server.finanz.service.SepakontoauszugDto;

public class SepaVerbuchungAuszugFalscherStatusException extends EJBExceptionData {
	private static final long serialVersionUID = 3693593134012241787L;

	private SepakontoauszugDto sepakontoauszugDto;
	private String verbuchungsstatus;
	
	public SepaVerbuchungAuszugFalscherStatusException(SepakontoauszugDto sepakontoauszugDto, String verbuchungsstatus) {
		this.sepakontoauszugDto = sepakontoauszugDto;
		this.verbuchungsstatus = verbuchungsstatus;
	}

	public SepakontoauszugDto getSepakontoauszugDto() {
		return sepakontoauszugDto;
	}
	
	public void setSepakontoauszugDto(SepakontoauszugDto sepakontoauszugDto) {
		this.sepakontoauszugDto = sepakontoauszugDto;
	}
	
	public String getVerbuchungsstatus() {
		return verbuchungsstatus;
	}
	
	public void setVerbuchungsstatus(String verbuchungsstatus) {
		this.verbuchungsstatus = verbuchungsstatus;
	}
}
