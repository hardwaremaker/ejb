package com.lp.server.kpi.service;

import java.rmi.RemoteException;
import java.sql.Date;

import javax.ejb.Remote;

import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;

@Remote
public interface KpiReportFac {
	final static String REPORT_MODUL = "finanz";

	final static String REPORT_KPI = REPORT_MODUL + "_kpi.jasper";

	JasperPrintLP printKpi(Date von, Date bis, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;
}
