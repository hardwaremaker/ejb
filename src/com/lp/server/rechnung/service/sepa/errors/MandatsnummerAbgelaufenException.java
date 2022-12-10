package com.lp.server.rechnung.service.sepa.errors;

import com.lp.server.partner.service.KundeDto;
import com.lp.server.rechnung.service.RechnungDto;

public class MandatsnummerAbgelaufenException extends SepaReException implements ISepaReKundeException {
	private static final long serialVersionUID = -8026081930712253766L;

	private KundeDto kundeDto;
	
	public MandatsnummerAbgelaufenException(RechnungDto rechnungDto, KundeDto kundeDto) {
		super(rechnungDto, "Sepa-Mandatsnummer von '" + kundeDto.getPartnerDto().getCName1nachnamefirmazeile1() + "' ist abgelaufen.");
		setKundeDto(kundeDto);
		setCode(SepaExceptionCode.MANDATSNUMMER_ABGELAUFEN);
	}

	public void setKundeDto(KundeDto kundeDto) {
		this.kundeDto = kundeDto;
	}
	
	public KundeDto getKundeDto() {
		return kundeDto;
	}
}
