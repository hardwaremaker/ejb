package com.lp.server.eingangsrechnung.service;


public class PaymentInfoVVMSettled extends PaymentInfoVVM {
	private static final long serialVersionUID = 3771554978311299247L;
	
	private SettledPaymentType settledPaymentType;

	public PaymentInfoVVMSettled(SettledPaymentType type) {
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
