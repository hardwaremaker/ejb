package com.lp.server.util.logger;

import com.lp.util.report.UstVerprobungRow;

public class LogEventUstVerprobungRow extends LogEventDto<UstVerprobungRow> {
	private static final long serialVersionUID = -6953452082511856020L;

	public LogEventUstVerprobungRow(UstVerprobungRow ustRow) {
		setPayLoad(ustRow);
	}
}
