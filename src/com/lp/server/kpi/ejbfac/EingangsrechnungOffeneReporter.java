package com.lp.server.kpi.ejbfac;

import java.rmi.RemoteException;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.lp.server.eingangsrechnung.ejbfac.EingangsrechnungReportFacBean;
import com.lp.server.eingangsrechnung.service.EingangsrechnungOffeneKriterienDto;
import com.lp.server.eingangsrechnung.service.EingangsrechnungReportFac;
import com.lp.server.kpi.service.KpiReportStorage;
import com.lp.server.util.FacLookup;
import com.lp.server.util.report.JasperPrintLP;

public class EingangsrechnungOffeneReporter extends KpiReporter<EingangsrechnungOffeneKriterienDto> {
	@Override
	protected EingangsrechnungOffeneKriterienDto buildKrit(KpiReportStorage storage) {
		EingangsrechnungOffeneKriterienDto krit = new EingangsrechnungOffeneKriterienDto();
		krit.dStichtag = storage.getDateTo();
		krit.stichtagIsFreigabedatum = true;
		krit.sort = EingangsrechnungReportFac.REPORT_OFFENE_SORT_LIEFERANT;
		krit.zusatzkosten = false;
		krit.mitNichtZugeordnetenBelegen = false;
		krit.setLieferantId(null);
		return krit;
	}

	@Override
	protected JasperPrintLP printImpl(EingangsrechnungOffeneKriterienDto krit, KpiReportStorage storage)
			throws RemoteException, NamingException {
		EingangsrechnungReportFac reportFac = FacLookup.lookup(
				new InitialContext(), EingangsrechnungReportFacBean.class, EingangsrechnungReportFac.class);
		return reportFac.printOffene(krit, storage.theClientDto());
	}
}
