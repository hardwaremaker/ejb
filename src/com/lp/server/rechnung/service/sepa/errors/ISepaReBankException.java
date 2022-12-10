package com.lp.server.rechnung.service.sepa.errors;

import com.lp.server.partner.service.BankDto;

public interface ISepaReBankException extends ISepaReException {

	BankDto getBankDto();
}
