package com.lp.server.kpi.service;

import com.lp.server.kpi.ejbfac.AblieferungsstatistikReporter;
import com.lp.server.kpi.ejbfac.ArbeitszeitstatistikReporter;
import com.lp.server.kpi.ejbfac.AuftragAlleReporter;
import com.lp.server.kpi.ejbfac.EingangsrechnungOffeneReporter;
import com.lp.server.kpi.ejbfac.RechnungAlleReporter;
import com.lp.server.kpi.ejbfac.RechnungOffeneReporter;
import com.lp.server.kpi.ejbfac.ReklamationFehlerartenReporter;
import com.lp.util.LPDatenSubreport;

public class HelperKpiReport {

	private HelperKpiReport() {
	}
	
	public static void setVar(KpiReportStorage storage, String key, Object value) {
		System.out.println("setVar(" + key + "," + value + ")");
		storage.put(key, value);
	}
	
	public static Object getVar(KpiReportStorage storage, String key) {
		Object result = storage.get(key);
		System.out.println("getVar(" + key + ") = " + result.toString() + ".");
		return result;
	}
	
	public static LPDatenSubreport printAuftragAlle(
			KpiReportStorage storage) {
		return new AuftragAlleReporter().print(storage);
	}
	
	public static LPDatenSubreport printRechnungAlle(KpiReportStorage storage) {
		return new RechnungAlleReporter().print(storage);
	}
	
	public static LPDatenSubreport printRechnungOffene(KpiReportStorage storage) {
		return new RechnungOffeneReporter().print(storage);
	}
	
	public static LPDatenSubreport printAblieferungsstatistik(KpiReportStorage storage) {
		return new AblieferungsstatistikReporter(false).print(storage);
	}
	
	public static LPDatenSubreport printAblieferungsstatistik(
			boolean nurKopflose, KpiReportStorage storage) {
		return new AblieferungsstatistikReporter(nurKopflose).print(storage);
	}
	
	public static LPDatenSubreport printArbeitszeitstatistik(KpiReportStorage storage) {
		return new ArbeitszeitstatistikReporter().print(storage);
	}
	
	public static LPDatenSubreport printFehlerarten(KpiReportStorage storage) {
		return new ReklamationFehlerartenReporter().print(storage);
	}
	
	public static LPDatenSubreport printEingangsrechnungOffene(KpiReportStorage storage) {
		return new EingangsrechnungOffeneReporter().print(storage);
	}
	
	
}
