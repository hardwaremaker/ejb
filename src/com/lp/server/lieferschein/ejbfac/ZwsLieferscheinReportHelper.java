package com.lp.server.lieferschein.ejbfac;

import com.lp.server.auftrag.ejbfac.ZwsReportHelper;
import com.lp.server.lieferschein.service.LieferscheinReportFac;

public class ZwsLieferscheinReportHelper extends ZwsReportHelper {

	public ZwsLieferscheinReportHelper(Object[] reportRow) {
		super(reportRow);
	}
 
	@Override
	public int getCBezIndex() {
		return LieferscheinReportFac.REPORT_LIEFERSCHEIN_ZWSTEXT;
	}
	
	@Override
	public int getNettoSummeIndex() {
		return LieferscheinReportFac.REPORT_LIEFERSCHEIN_ZWSNETTOSUMME;
	}
	
	@Override
	public int getCBezsIndex() {
		return LieferscheinReportFac.REPORT_LIEFERSCHEIN_ZWSTEXTE;
	}

	@Override
	public int getNettoSummenIndex() {
		return LieferscheinReportFac.REPORT_LIEFERSCHEIN_ZWSNETTOSUMMEN;
	}
}
