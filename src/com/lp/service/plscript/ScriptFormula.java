package com.lp.service.plscript;

public abstract class ScriptFormula<T> {
	private Integer id;
	private String src;
	private T payload;
	private ScriptReportLogging reportLogger;
	
	public ScriptFormula(Integer id, String src) {
		reportLogger = new ScriptReportLogging();
		setId(id);
		setSrc(src);
	}
	
	public ScriptFormula(Integer id, String src, T payload) {
		reportLogger = new ScriptReportLogging();
		setId(id);
		setSrc(src);
		setPayload(payload);
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public T getPayload() {
		return payload;
	}

	public void setPayload(T payload) {
		this.payload = payload;
	}
	
	public ScriptReportLogging getReportLogger() {
		return reportLogger;
	}
	
	/**
	 * Fully qualified Classname ... java.lang.String, java.math.BigDecimal
	 * 
	 * @return den vollstaendigen Klassennamen des Typs des Ergebnisses
	 */
	public abstract String getResultType();
	public abstract void storeResult(Object result);
}
