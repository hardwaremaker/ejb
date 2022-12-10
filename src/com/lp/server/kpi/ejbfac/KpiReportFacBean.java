package com.lp.server.kpi.ejbfac;


import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;

import com.lp.server.kpi.service.KpiReportFac;
import com.lp.server.kpi.service.KpiReportStorage;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.LPReport;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.server.util.report.TimingInterceptor;
import com.lp.util.EJBExceptionLP;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

@Stateless
@TransactionAttribute(TransactionAttributeType.NEVER)
@Interceptors(TimingInterceptor.class)
public class KpiReportFacBean extends LPReport implements KpiReportFac, JRDataSource {

	private List<Map<String, Object>> reportData;
	
	@Override
	public Object getFieldValue(JRField field) throws JRException {
		return reportData.get(index).get(field.getName());
	}

	@Override
	public boolean next() throws JRException {
		index++;
		return index < reportData.size();
	}
	
	@Override
	public JasperPrintLP printKpi(
			java.sql.Date von, java.sql.Date bis, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException {
		KpiReportStorage kpiStore = new KpiReportStorage(
				von, bis, theClientDto);
		KpiParams params = new KpiParams();
		params.addStore(kpiStore);
		
		reportData = new ArrayList<Map<String,Object>>();
		index = -1;
		Map<String, Object> entry = new HashMap<String, Object>();
		entry.put("F_VON", new java.util.Date(von.getTime()));
		entry.put("F_BIS", new java.util.Date(bis.getTime()));	
		reportData.add(entry);

		initJRDS(params.asMap(), REPORT_MODUL, REPORT_KPI,
				theClientDto.getMandant(),
				theClientDto.getLocUi(), theClientDto);
		return getReportPrint();
	}
	
	private class KpiParams {
		private final static String P_CREATED = "_CREATED";
		private final static String P_STORAGE = LPReport.P_KPI_VARIABLEN;
		
		private Map<String, Object> params;
		
		public KpiParams() {
			params = new HashMap<String, Object>();			
			put(P_CREATED, getTimestamp().toString());
		}
		
		public KpiReportStorage addStore(KpiReportStorage storage) {
			return (KpiReportStorage) put(P_STORAGE, storage);
		}
		
		public Object put(String key, Object value) {
			return params.put(key, value);
		}
		
		public Object get(String key) {
			return params.get(key);
		}
			
		public Map<String, Object> asMap() {
			return params;
		}
	}
}
