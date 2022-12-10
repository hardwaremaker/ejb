package com.lp.server.util.logger;

import com.lp.server.lieferschein.service.LieferscheinDto;

public class LogEventLieferschein extends LogEventDto<LieferscheinDto> {
	private static final long serialVersionUID = -889095130415157183L;

	public LogEventLieferschein(LieferscheinDto lieferscheinDto) {
		setPayLoad(lieferscheinDto);
	}
}
