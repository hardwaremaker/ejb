package com.lp.server.rechnung.service.sepa.errors;

import com.lp.server.partner.service.BankDto;
import com.lp.server.rechnung.service.RechnungDto;

public class KeineBicException extends SepaReException implements ISepaReBankException {

	private static final long serialVersionUID = -144811235520503550L;

	private BankDto bankDto;
	
	public KeineBicException(RechnungDto rechnungDto, BankDto bankDto) {
		super(rechnungDto, "F\u00FCr Bank '" + bankDto.getPartnerDto().getCName1nachnamefirmazeile1() 
				+ "' ist keine BIC eingetragen.");
		setBankDto(bankDto);
		setCode(SepaExceptionCode.KEINE_BIC);
	}
	
	public void setBankDto(BankDto bankDto) {
		this.bankDto = bankDto;
	}
	
	public BankDto getBankDto() {
		return bankDto;
	}
}
