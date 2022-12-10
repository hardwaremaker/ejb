package com.lp.server.util.logger;

import com.lp.server.finanz.service.KontoDto;


public class LogEventKonto extends LogEventDto<KontoDto> {

	public LogEventKonto(KontoDto kontoDto) {
		setPayLoad(kontoDto);
	}
}
