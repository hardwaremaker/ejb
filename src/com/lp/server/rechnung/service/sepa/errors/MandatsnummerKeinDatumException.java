package com.lp.server.rechnung.service.sepa.errors;

import com.lp.server.partner.service.KundeDto;
import com.lp.server.rechnung.service.RechnungDto;

public class MandatsnummerKeinDatumException extends SepaReException implements ISepaReKundeException {
	private static final long serialVersionUID = -1045506264265467124L;

	private KundeDto kundeDto;
	
	public MandatsnummerKeinDatumException(RechnungDto rechnungDto, KundeDto kundeDto) {
		super(rechnungDto, "Sepa-Mandatsnummer von '" + kundeDto.getPartnerDto().getCName1nachnamefirmazeile1() 
				+ "' hat kein G\u00FCltigkeitsdatum.");
		setKundeDto(kundeDto);
		setCode(SepaExceptionCode.MANDATSNUMMER_KEINDATUM);
	}

	public void setKundeDto(KundeDto kundeDto) {
		this.kundeDto = kundeDto;
	}
	
	public KundeDto getKundeDto() {
		return kundeDto;
	}
}
