package com.lp.server.kpi.ejbfac;

import java.rmi.RemoteException;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.lp.server.kpi.service.KpiReportStorage;
import com.lp.server.rechnung.ejbfac.RechnungReportFacBean;
import com.lp.server.rechnung.service.RechnungReportFac;
import com.lp.server.rechnung.service.ReportRechnungJournalKriterienDto;
import com.lp.server.util.FacLookup;
import com.lp.server.util.report.JasperPrintLP;

public class RechnungOffeneReporter extends KpiReporter<ReportRechnungJournalKriterienDto> {
	@Override
	protected ReportRechnungJournalKriterienDto buildKrit(KpiReportStorage storage) {
		ReportRechnungJournalKriterienDto krit = new ReportRechnungJournalKriterienDto();
		krit.iSortierung = ReportRechnungJournalKriterienDto.KRIT_SORT_NACH_PARTNER;
		krit.setTStichtag(storage.getDateTo());
		krit.setBVerwendeStatistikAdresse(false);
		krit.setBGutschriftenBeruecksichtigen(true);
		return krit;
	}

	@Override
	protected JasperPrintLP printImpl(ReportRechnungJournalKriterienDto krit, KpiReportStorage storage)
			throws RemoteException, NamingException {
		RechnungReportFac reportFac = FacLookup.lookup(
				new InitialContext(), RechnungReportFacBean.class, RechnungReportFac.class);
		return reportFac.printRechnungenOffene(krit, storage.theClientDto());
	}
}
