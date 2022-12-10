package com.lp.server.util.logger;

import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;

public class LogEventEingangsrechnung extends LogEventDto<EingangsrechnungDto> {
	private static final long serialVersionUID = -4435372374235570187L;

	public LogEventEingangsrechnung(EingangsrechnungDto erDto) {
		setPayLoad(erDto);
	}
}
