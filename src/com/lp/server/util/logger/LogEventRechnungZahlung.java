package com.lp.server.util.logger;

import com.lp.server.rechnung.service.RechnungzahlungDto;


public class LogEventRechnungZahlung extends LogEventDto<RechnungzahlungDto> {
	private static final long serialVersionUID = -4180513333335401785L;

	public LogEventRechnungZahlung(RechnungzahlungDto rechnungzahlungDto) {
		setPayLoad(rechnungzahlungDto);
	}
}
