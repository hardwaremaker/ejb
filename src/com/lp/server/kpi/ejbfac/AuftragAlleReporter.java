package com.lp.server.kpi.ejbfac;

import java.rmi.RemoteException;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.lp.server.auftrag.ejbfac.AuftragReportFacBean;
import com.lp.server.auftrag.service.AuftragReportFac;
import com.lp.server.kpi.service.KpiReportStorage;
import com.lp.server.system.service.ReportJournalKriterienDto;
import com.lp.server.util.FacLookup;
import com.lp.server.util.report.JasperPrintLP;

public class AuftragAlleReporter extends KpiReporter<ReportJournalKriterienDto> {

	@Override
	protected ReportJournalKriterienDto buildKrit(KpiReportStorage storage) {
		ReportJournalKriterienDto krit = new ReportJournalKriterienDto();
		krit.dVon = storage.getDateFrom();
		krit.dBis = storage.getDateTo();
		krit.iSortierung = ReportJournalKriterienDto.KRIT_SORT_NACH_VERTRETER;
		return krit;
	}

	@Override
	protected JasperPrintLP printImpl(ReportJournalKriterienDto krit, KpiReportStorage storage)
			throws RemoteException, NamingException {
		AuftragReportFac reportFac = FacLookup.lookup(new InitialContext(), 
				AuftragReportFacBean.class, AuftragReportFac.class);
		return reportFac.printAuftragAlle(krit, storage.theClientDto());
	}
}
