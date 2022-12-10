package com.lp.server.auftrag.ejbfac;

import com.lp.server.auftrag.service.AuftragReportFac;

public class ZwsAuftragReportHelper extends ZwsReportHelper {
	public ZwsAuftragReportHelper(Object[] target) {
		super(target);
	}
	 
	@Override
	public int getCBezIndex() {
		return AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_ZWSTEXT;
	}
	 
	@Override
	public int getNettoSummeIndex() {
		return AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_ZWSNETTOSUMME;
	}
	 
	@Override
	public int getCBezsIndex() {
		return AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_ZWSTEXTE;
	}

	@Override
	public int getNettoSummenIndex() {
		return AuftragReportFac.REPORT_AUFTRAGBESTAETIGUNG_ZWSNETTOSUMMEN;
	}
}
