package com.lp.server.kpi.ejbfac;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.lp.server.fertigung.ejbfac.FertigungReportFacBean;
import com.lp.server.fertigung.service.AblieferstatistikJournalKriterienDto;
import com.lp.server.fertigung.service.FertigungReportFac;
import com.lp.server.kpi.service.KpiReportStorage;
import com.lp.server.util.FacLookup;
import com.lp.server.util.report.JasperPrintLP;

public class AblieferungsstatistikReporter extends KpiReporter<AblieferstatistikJournalKriterienDto> {
	private final AblieferstatistikJournalKriterienDto krit;
	
	public AblieferungsstatistikReporter(boolean nurKopflose) {
		krit = new AblieferstatistikJournalKriterienDto();
		if(nurKopflose) {
			krit.optionArtikel = FertigungReportFac.ABLIEFERSTATISTIK_OPTION_ARTIKEL_NUR_KOPFLOSE;
		}
	}
	
	@Override
	protected AblieferstatistikJournalKriterienDto buildKrit(KpiReportStorage storage) {
		krit.dVon = storage.getDateFrom();
		krit.dBis = storage.getDateTo();
		return krit;
	}

	@Override
	protected JasperPrintLP printImpl(
			AblieferstatistikJournalKriterienDto krit, KpiReportStorage storage) throws NamingException {
		FertigungReportFac reportFac = FacLookup.lookup(
				new InitialContext(), FertigungReportFacBean.class, FertigungReportFac.class);
		return reportFac.printAblieferungsstatistik(
				krit, storage.theClientDto());
	}
}
