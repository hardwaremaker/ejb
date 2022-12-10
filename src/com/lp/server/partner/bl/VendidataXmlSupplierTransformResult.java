package com.lp.server.partner.bl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.lp.server.artikel.service.VendidataExportStats;
import com.lp.server.schema.vendidata.suppliers.XMLSuppliers;
import com.lp.util.EJBExceptionLP;

public class VendidataXmlSupplierTransformResult implements Serializable {

	private static final long serialVersionUID = 3193330036344301707L;

	private XMLSuppliers xmlSuppliers;
	private List<EJBExceptionLP> exportErrors;
	private VendidataExportStats exportStats;
	
	public VendidataXmlSupplierTransformResult(List<EJBExceptionLP> exportErrors, VendidataExportStats exportStats) {
		this(null, exportErrors, exportStats);
	}
	
	public VendidataXmlSupplierTransformResult(XMLSuppliers xmlSuppliers, VendidataExportStats exportStats) {
		this(xmlSuppliers, new ArrayList<EJBExceptionLP>(), exportStats);
	}
	
	public VendidataXmlSupplierTransformResult(XMLSuppliers xmlSuppliers, List<EJBExceptionLP> exportErrors, VendidataExportStats exportStats) {
		setXmlSuppliers(xmlSuppliers);
		setExportErrors(exportErrors);
		setExportStats(exportStats);
	}

	public XMLSuppliers getXmlSuppliers() {
		return xmlSuppliers;
	}

	public void setXmlSuppliers(XMLSuppliers xmlSuppliers) {
		this.xmlSuppliers = xmlSuppliers;
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
