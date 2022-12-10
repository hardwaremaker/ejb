package com.lp.server.util.logger;

import com.lp.server.artikel.ejbfac.LagerbewegungPreisaenderung;


public class LogEventLagerbewegungPreisaenderung extends
		LogEventDto<LagerbewegungPreisaenderung> {
	private static final long serialVersionUID = -7694027791404580169L;

	public LogEventLagerbewegungPreisaenderung(LagerbewegungPreisaenderung payload) {
		setPayLoad(payload);
	}
}
