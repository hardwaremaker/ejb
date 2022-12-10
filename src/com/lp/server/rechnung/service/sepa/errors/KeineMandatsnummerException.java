package com.lp.server.rechnung.service.sepa.errors;

import com.lp.server.partner.service.KundeDto;
import com.lp.server.rechnung.service.RechnungDto;

public class KeineMandatsnummerException extends SepaReException implements ISepaReKundeException {
	private static final long serialVersionUID = -5053703260601883508L;

	private KundeDto kundeDto;
	
	public KeineMandatsnummerException(RechnungDto rechnungDto, KundeDto kundeDto) {
		super(rechnungDto, "Keine Sepa-Mandatsnummer in Bankverbindung von '" 
				+ kundeDto.getPartnerDto().getCName1nachnamefirmazeile1() + "' gefunden.");
		setKundeDto(kundeDto);
		setCode(SepaExceptionCode.KEINE_MANDATSNUMMER);
	}

	public void setKundeDto(KundeDto kundeDto) {
		this.kundeDto = kundeDto;
	}
	
	public KundeDto getKundeDto() {
		return kundeDto;
	}
}
