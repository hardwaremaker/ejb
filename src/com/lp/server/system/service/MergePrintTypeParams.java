package com.lp.server.system.service;

import java.io.Serializable;

import net.sf.jasperreports.engine.JasperPrint;

public class MergePrintTypeParams implements Serializable {
	private static final long serialVersionUID = -7703481543138404019L;

	private JasperPrint jrPrint;
	private String druckType;
	
	public MergePrintTypeParams(JasperPrint jrPrint, String druckType) {
		setJrPrint(jrPrint);
		setDruckType(druckType);
	}
	
	public JasperPrint getJrPrint() {
		return jrPrint;
	}
	
	public void setJrPrint(JasperPrint jrPrint) {
		this.jrPrint = jrPrint;
	}
	
	public String getDruckType() {
		return druckType;
	}
	
	public void setDruckType(String druckType) {
		this.druckType = druckType;
	}
}
