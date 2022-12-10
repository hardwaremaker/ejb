package com.lp.server.rechnung.service.sepa.errors;

import com.lp.server.system.service.MandantDto;

public interface ISepaMandantException extends ISepaException {

	MandantDto getMandantDto();
}
