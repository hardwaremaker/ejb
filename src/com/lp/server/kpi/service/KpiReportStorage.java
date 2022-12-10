package com.lp.server.kpi.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.lp.server.system.service.TheClientDto;

public class KpiReportStorage implements Serializable {
	private static final long serialVersionUID = -1209834677055271534L;

	private Map<String, Object> values;
	private TheClientDto theClientDto;
	private java.sql.Date dateFrom;
	private java.sql.Date dateTo;
	
	public KpiReportStorage(java.sql.Date von,
			java.sql.Date bis, TheClientDto theClientDto) {
		values = new HashMap<String, Object>();
		this.theClientDto = theClientDto;
		this.dateFrom = von;
		this.dateTo = bis;
	}
	
	public java.sql.Date getDateFrom() {
		return this.dateFrom;
	}
	
	public java.sql.Date getDateTo() {
		return this.dateTo;
	}
	
	public Object put(String key, Object value) {
		System.out.println("put(" + key + "," + value + ")");
//		return values.put(key, value);
		values.put(key, value);
		return values.get(key);
	}
	
	public Object get(String key) {
		Object result = values.get(key);
		System.out.println("get(" + key + ") = " + result + ".");
		return result;
	}
	
	public Set<String> keys() {
		return values.keySet();
	}
	
	public int size() {
		return values.size();
	}
	
	public TheClientDto theClientDto() {
		return theClientDto;
	}
	
	public void updateFrom(KpiReportStorage other) {
		System.out.println("updateStorage()");
		values.putAll(other.values);
	}
}
