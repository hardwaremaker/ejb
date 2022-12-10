package com.lp.server.rechnung.service.sepa.errors;

import com.lp.server.partner.service.KundeDto;
import com.lp.server.rechnung.service.RechnungDto;

public class KeineIbanException extends SepaReException implements ISepaReKundeException {
	private static final long serialVersionUID = -4486981664104490138L;

	private KundeDto kundeDto;
	
	public KeineIbanException(RechnungDto rechnungDto, KundeDto kundeDto) {
		super(rechnungDto, "Kein IBAN bei Bankverbindung des Kunden '" 
				+ kundeDto.getPartnerDto().getCName1nachnamefirmazeile1() + "' hinterlegt");
		setKundeDto(kundeDto);
		setCode(SepaExceptionCode.KEINE_IBAN);
	}
	
	public void setKundeDto(KundeDto kundeDto) {
		this.kundeDto = kundeDto;
	}
	
	public KundeDto getKundeDto() {
		return kundeDto;
	}
}
