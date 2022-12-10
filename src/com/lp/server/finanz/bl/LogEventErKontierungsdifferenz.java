package com.lp.server.finanz.bl;

import com.lp.server.util.logger.LogEventDto;


public class LogEventErKontierungsdifferenz extends
		LogEventDto<ErKontierungsDifferenz> {
	private static final long serialVersionUID = -5890972067333257549L;

	public LogEventErKontierungsdifferenz(ErKontierungsDifferenz kontierungsdifferenz) {
		setPayLoad(kontierungsdifferenz);
	}
}
