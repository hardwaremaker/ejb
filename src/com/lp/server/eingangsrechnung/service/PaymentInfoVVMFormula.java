package com.lp.server.eingangsrechnung.service;

public class PaymentInfoVVMFormula extends PaymentInfoVVM {
	private static final long serialVersionUID = -3787924223740805641L;

	private String formelMitWerten;
	
	public PaymentInfoVVMFormula() {
	}

	@Override
	public String getKommentar() {
		return getFormelMitWerten();
	}

	@Override
	public String getBezeichnung() {
		return getVvmInfoText();
	}

	public void setFormelMitWerten(String formelMitWerten) {
		this.formelMitWerten = formelMitWerten;
	}
	
	public String getFormelMitWerten() {
		return formelMitWerten;
	}

	@Override
	public boolean isBezeichnungToken() {
		return false;
	}
}
