package com.lp.server.rechnung.ejbfac;

import com.lp.server.auftrag.ejbfac.ZwsReportHelper;

public class ZwsRechnungReportHelper extends ZwsReportHelper {
	public ZwsRechnungReportHelper(Object[] reportRow) {
		super(reportRow);
	}
	 
	@Override
	public int getCBezIndex() {
		return RechnungReportFacBean.RECHNUNG_FELD_ZWSTEXT;
	}
	
	@Override
	public int getNettoSummeIndex() {
		return RechnungReportFacBean.RECHNUNG_FELD_ZWSNETTOSUMME;
	}
	
	@Override
	public int getCBezsIndex() {
		return RechnungReportFacBean.RECHNUNG_FELD_ZWSTEXTE;
	}

	@Override
	public int getNettoSummenIndex() {
		return RechnungReportFacBean.RECHNUNG_FELD_ZWSNETTOSUMMEN;
	}
}
