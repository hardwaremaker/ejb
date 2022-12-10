package com.lp.server.eingangsrechnung.service;

public abstract class PaymentInfoVVM extends VendidataPaymentInfo {
	private static final long serialVersionUID = -1335521099674503267L;

	private String vvmInfoText; 

	public PaymentInfoVVM() {
	}

	@Override
	public Boolean isVVM() {
		return Boolean.TRUE;
	}

	@Override
	public Boolean isCustomer() {
		return Boolean.FALSE;
	}

	public void setVvmInfoText(String vvmInfoText) {
		this.vvmInfoText = vvmInfoText;
	}
	
	public String getVvmInfoText() {
		return vvmInfoText;
	}
}
