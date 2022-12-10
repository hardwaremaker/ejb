package com.lp.server.angebot.ejbfac;

import com.lp.server.angebot.service.AngebotReportFac;
import com.lp.server.auftrag.ejbfac.ZwsReportHelper;

public class ZwsAngebotReportHelper extends ZwsReportHelper {
	public ZwsAngebotReportHelper(Object[] target) {
		super(target);
	}
	
	@Override
	public int getCBezIndex() {
		return AngebotReportFac.REPORT_ANGEBOT_ZWSTEXT;
	}
	 
	@Override
	public int getNettoSummeIndex() {
		return AngebotReportFac.REPORT_ANGEBOT_ZWSNETTOSUMME;
	}
	
	@Override
	public int getCBezsIndex() {
		return AngebotReportFac.REPORT_ANGEBOT_ZWSTEXTE;
	}

	@Override
	public int getNettoSummenIndex() {
		return AngebotReportFac.REPORT_ANGEBOT_ZWSNETTOSUMMEN;
	}
}
