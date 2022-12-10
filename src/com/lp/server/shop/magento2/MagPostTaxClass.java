package com.lp.server.shop.magento2;

public class MagPostTaxClass {
	private MagTaxClass taxClass;
	
	public MagPostTaxClass(MagTaxClass taxClass) {
		setTaxClass(taxClass);
	}
	
	public MagTaxClass getTaxClass() {
		return taxClass;
	}

	public void setTaxClass(MagTaxClass taxClass) {
		this.taxClass = taxClass;
	}
}
