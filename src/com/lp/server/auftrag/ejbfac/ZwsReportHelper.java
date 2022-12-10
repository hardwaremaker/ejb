package com.lp.server.auftrag.ejbfac;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public abstract class ZwsReportHelper {
	private Object[] reportRow;
	
	public ZwsReportHelper(Object[] target) {
		this.reportRow = target;
	}
	
	public abstract int getCBezIndex();
	public abstract int getNettoSummeIndex();
	public abstract int getCBezsIndex();
	public abstract int getNettoSummenIndex();
	
	public void setupZwsText(String zwsCBez) {
		if(reportRow[getCBezIndex()] == null) {
			reportRow[getCBezIndex()] = zwsCBez;
		} else {
			String s = (String) reportRow[getCBezIndex()] + "\n" + zwsCBez;
			reportRow[getCBezIndex()] = s;
		}
		
		addCBez(zwsCBez);
	}
	
	public void setupNettoSumme(BigDecimal nettoSumme) {
		reportRow[getNettoSummeIndex()] = nettoSumme;
		
		addNettoSumme(nettoSumme);
	}
	
	public List<String> addCBez(String cbez) {
		reportRow[getCBezsIndex()] = addZwsSortedValue(
				(List<String>) reportRow[getCBezsIndex()], cbez);
		return (List<String>) reportRow[getCBezsIndex()];
	}
	
	public List<BigDecimal> addNettoSumme(BigDecimal nettoSumme) {
		reportRow[getNettoSummenIndex()] = addZwsSortedValue(
				(List<BigDecimal>) reportRow[getNettoSummenIndex()], nettoSumme);
		return (List<BigDecimal>) reportRow[getCBezsIndex()];
	}
	
	public String[] cbezs() {
		return reportRow[getCBezsIndex()] == null
				? null 
				: ((List<String>)reportRow[getCBezsIndex()]).toArray(new String[0]);
	}
	
	public BigDecimal[] nettoSummen() {
		return reportRow[getNettoSummenIndex()] == null 
				? null
				:  ((List<BigDecimal>)reportRow[getNettoSummenIndex()]).toArray(
						new BigDecimal[0]);						
	}

	private <T> List<T> addZwsSortedValue(List<T> values, T newValue) {
		if(values == null) {
			values = new ArrayList<T>();
			values.add(newValue);
		} else {
			values.add(0, newValue);
		}
		
		return values;
	}
}
