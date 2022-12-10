package com.lp.server.eingangsrechnung.service;

import com.lp.server.system.service.BelegDocExportProperties;
import com.lp.server.util.HvOptional;
import com.lp.server.util.LieferantId;

public class ErDocExportProperties extends BelegDocExportProperties {
	private static final long serialVersionUID = -7676019515695295052L;

	private LieferantId supplierId;
	
	public ErDocExportProperties() {
		super();
	}
	
	public void setSupplierId(LieferantId supplierId) {
		this.supplierId = supplierId;
	}
	public HvOptional<LieferantId> getSupplierId() {
		return HvOptional.ofNullable(supplierId);
	}
}
