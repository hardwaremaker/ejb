package com.lp.server.rechnung.service.sepa.errors;

public interface ISepaException {

	SepaExceptionCode getCode();
	
	String getMessage();
}
