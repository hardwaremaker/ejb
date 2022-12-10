package com.lp.server.util.logger;

import com.lp.server.finanz.service.BelegbuchungDto;

public class LogEventBelegbuchung extends LogEventDto<BelegbuchungDto> {
	private static final long serialVersionUID = -3245302251420682660L;

	public LogEventBelegbuchung(BelegbuchungDto belegbuchungDto) {
		setPayLoad(belegbuchungDto);
	}
}
