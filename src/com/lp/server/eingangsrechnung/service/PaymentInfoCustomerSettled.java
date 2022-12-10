package com.lp.server.eingangsrechnung.service;


public class PaymentInfoCustomerSettled extends PaymentInfoCustomer {
	private static final long serialVersionUID = 70884217506300431L;

	public SettledPaymentType settledPaymentType;
	
	public PaymentInfoCustomerSettled(SettledPaymentType type) {
		this.settledPaymentType = type;
	}

	@Override
	public String getKommentar() {
		return null;
	}

	@Override
	public String getBezeichnung() {
		if (SettledPaymentType.SINGLE_PAYMENT.equals(settledPaymentType)) {
			return "er.import.vendidata.einmalzahlung";
		} else if (SettledPaymentType.FLATCHARGE_PAYMENT.equals(settledPaymentType)) {
			return "er.import.vendidata.pauschale";
		} else if (SettledPaymentType.PAYMENT.equals(settledPaymentType)) {
			return "er.import.vendidata.pauschale";
		}	
		return "";
	}

	@Override
	public boolean isBezeichnungToken() {
		return true;
	}
}
