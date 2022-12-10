package com.lp.server.rechnung.service.sepa.errors;

import com.lp.server.finanz.service.BankverbindungDto;
import com.lp.server.finanz.service.KontoDto;

public interface ISepaBankverbindungException extends ISepaException {

	BankverbindungDto getBankverbindungDto();
	
	KontoDto getKontoDto();
}
