package com.lp.server.kpi.ejbfac;

import java.rmi.RemoteException;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.lp.server.kpi.service.KpiReportStorage;
import com.lp.server.reklamation.ejbfac.ReklamationReportFacBean;
import com.lp.server.reklamation.service.ReklamationFehlerartenJournalKriterienDto;
import com.lp.server.reklamation.service.ReklamationReportFac;
import com.lp.server.util.FacLookup;
import com.lp.server.util.report.JasperPrintLP;

public class ReklamationFehlerartenReporter extends KpiReporter<ReklamationFehlerartenJournalKriterienDto> {

	@Override
	protected ReklamationFehlerartenJournalKriterienDto buildKrit(KpiReportStorage storage) {
		ReklamationFehlerartenJournalKriterienDto krit = new ReklamationFehlerartenJournalKriterienDto();
		krit.dVon = storage.getDateFrom();
		krit.dBis = storage.getDateTo();
		krit.mitFertigung = true;
		krit.mitLieferant = true;
		krit.mitKunde     = true;
		krit.gruppierung  = ReklamationReportFac.SORTIERUNG_FEHLERART_FEHLERART;
		krit.nurBerechtigt = false;
		return krit;
	}

	@Override
	protected JasperPrintLP printImpl(ReklamationFehlerartenJournalKriterienDto krit, KpiReportStorage storage)
			throws RemoteException, NamingException {
		ReklamationReportFac reportFac = FacLookup.lookup(new InitialContext(),
				ReklamationReportFacBean.class, ReklamationReportFac.class);
		return reportFac.printFehlerarten(krit, storage.theClientDto());
	}
}
