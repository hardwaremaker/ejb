package com.lp.server.rechnung.service.sepa.errors;

import com.lp.server.rechnung.service.RechnungDto;

public class KeinLastschriftZahlungsziel extends SepaReException {
	private static final long serialVersionUID = 6101472272515937417L;

	public KeinLastschriftZahlungsziel(RechnungDto rechnungDto) {
		super(rechnungDto, "Zahlungsziel der Rechnung '" + rechnungDto.getCNr() + "' ist keine Lastschrift");
		setCode(SepaExceptionCode.KEIN_LASTSCHRIFT_ZAHLUNGSZIEL);
	}
	
}
