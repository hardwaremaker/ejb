package com.lp.server.partner.bl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.lp.server.artikel.service.VendidataExportStats;
import com.lp.server.schema.vendidata.customers.XMLCustomers;
import com.lp.util.EJBExceptionLP;

public class VendidataXmlCustomerTransformResult implements Serializable {

	private static final long serialVersionUID = -4731051805080311516L;

	private XMLCustomers xmlCustomers;
	private List<EJBExceptionLP> exportErrors;
	private VendidataExportStats exportStats;
	
	public VendidataXmlCustomerTransformResult() {
	}

	public VendidataXmlCustomerTransformResult(XMLCustomers xmlCustomers, VendidataExportStats exportStats) {
		this(xmlCustomers, new ArrayList<EJBExceptionLP>(), exportStats);
	}

	public VendidataXmlCustomerTransformResult(List<EJBExceptionLP> exportErrors, VendidataExportStats exportStats) {
		this(null, exportErrors, exportStats);
	}
	
	public VendidataXmlCustomerTransformResult(XMLCustomers xmlCustomers, List<EJBExceptionLP> exportErrors, VendidataExportStats exportStats) {
		setXmlCustomers(xmlCustomers);
		setExportErrors(exportErrors);
		setExportStats(exportStats);
	}

	public XMLCustomers getXmlCustomers() {
		return xmlCustomers;
	}

	public void setXmlCustomers(XMLCustomers xmlCustomers) {
		this.xmlCustomers = xmlCustomers;
	}

	public List<EJBExceptionLP> getExportErrors() {
		return exportErrors;
	}

	public void setExportErrors(List<EJBExceptionLP> exportErrors) {
		this.exportErrors = exportErrors;
	}

	public VendidataExportStats getExportStats() {
		return exportStats;
	}

	public void setExportStats(VendidataExportStats exportStats) {
		this.exportStats = exportStats;
	}
}
