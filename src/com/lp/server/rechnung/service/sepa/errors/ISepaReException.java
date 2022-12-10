package com.lp.server.rechnung.service.sepa.errors;

import com.lp.server.rechnung.service.RechnungDto;

public interface ISepaReException extends ISepaException {

	RechnungDto getRechnungDto();
}
