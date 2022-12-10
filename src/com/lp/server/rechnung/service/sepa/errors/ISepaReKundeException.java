package com.lp.server.rechnung.service.sepa.errors;

import com.lp.server.partner.service.KundeDto;

public interface ISepaReKundeException extends ISepaReException {

	KundeDto getKundeDto();
}
