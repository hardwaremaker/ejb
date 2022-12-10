package com.lp.server.kpi.ejbfac;

import java.rmi.RemoteException;

import javax.naming.NamingException;

import com.lp.server.kpi.service.KpiReportStorage;
import com.lp.server.system.service.ReportJournalKriterienDto;
import com.lp.server.util.LPReport;
import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.LPDatenSubreport;

public abstract class KpiReporter<T extends ReportJournalKriterienDto> {
	public KpiReporter() {
	}
	
	public LPDatenSubreport print(KpiReportStorage storage) {
		try {
			T krit = buildKrit(storage);
			krit.setKpiReportStorage(storage);
			JasperPrintLP print = printImpl(krit, storage);
			updateFromPrint(storage, print);
			return print.transformToSubreport();			
		} catch(RemoteException e) {
			ILPLogger myLogger = LPLogService.getInstance().getLogger(this.getClass());
			myLogger.error("RemoteExc", e);
			return null;						
		} catch(NamingException e) {
			ILPLogger myLogger = LPLogService.getInstance().getLogger(this.getClass());
			myLogger.error("NamingExc", e);
			return null;			
		}
	}
	
	protected abstract T buildKrit(KpiReportStorage storage);
	protected abstract JasperPrintLP printImpl(T krit,
			KpiReportStorage storage) throws RemoteException, NamingException;
	
	/**
	 * Den KpiStorage aktualisieren</br>
	 * <p>Der KpiStorage wird an eine Remote-Bean uebergeben, die den Storage
	 * modifiziert. Da eine Remote-Bean dafuer sorgt, dass Uebergabeparameter
	 * unveraendert dem Aufrufer zur Verfuegung steht, gehen die Aenderungen 
	 * verloren. Daher der Umweg darueber, dass der Storage nach dem der 
	 * Report gelaufen ist und so JasperPrintLP erzeugt hat, durch die
	 * in diesem Report gespeicherten Parameter (P_KPI_VARIABLEN) korrekt 
	 * aktualisiert wird (sic).
	 * @param storage wird durch den Druck aktualisiert
	 * @param printLP enthaelt die mapParameter
	 */
	private void updateFromPrint(KpiReportStorage storage, JasperPrintLP printLP ) {
		KpiReportStorage modifiedVariables = (KpiReportStorage) printLP
				.getMapParameters().get(LPReport.P_KPI_VARIABLEN);
		storage.updateFrom(modifiedVariables);		
	}
}

