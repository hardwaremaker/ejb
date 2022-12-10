package com.lp.server.eingangsrechnung.service;

public abstract class PaymentInfoCustomer extends VendidataPaymentInfo {
	private static final long serialVersionUID = -1378273067900980585L;

	public PaymentInfoCustomer() {
	}

	@Override
	public Boolean isVVM() {
		return Boolean.FALSE;
	}

	@Override
	public Boolean isCustomer() {
		return Boolean.TRUE;
	}

}
