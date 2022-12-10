package com.lp.server.kpi.ejbfac;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.Calendar;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.lp.server.kpi.service.KpiReportStorage;
import com.lp.server.personal.ejbfac.ZeiterfassungReportFacBean;
import com.lp.server.personal.service.ArbeitszeitstatistikJournalKriterienDto;
import com.lp.server.personal.service.ZeiterfassungReportFac;
import com.lp.server.util.FacLookup;
import com.lp.server.util.report.JasperPrintLP;

public class ArbeitszeitstatistikReporter extends KpiReporter<ArbeitszeitstatistikJournalKriterienDto> {

	@Override
	protected ArbeitszeitstatistikJournalKriterienDto buildKrit(KpiReportStorage storage) {
		ArbeitszeitstatistikJournalKriterienDto krit = new ArbeitszeitstatistikJournalKriterienDto();
		krit.dVon = storage.getDateFrom();
		// Die Arbeitszeitstatistik rechnet selbst einen Tag weniger, deshalb hier +1!
		
		krit.dBis = addOneDay(storage.getDateTo());
		return krit;
	}

	private Date addOneDay(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, 1);
		return new Date(c.getTimeInMillis());
	}
	
	@Override
	protected JasperPrintLP printImpl(
			ArbeitszeitstatistikJournalKriterienDto krit, 
			KpiReportStorage storage) throws RemoteException, NamingException {
		ZeiterfassungReportFac reportFac = FacLookup.lookup(
				new InitialContext(), ZeiterfassungReportFacBean.class, ZeiterfassungReportFac.class);
		return reportFac.printArbeitszeitstatistik(krit, storage.theClientDto());
	}
}
