package com.lp.server.lieferschein.service.errors;

public class StmUnbekannteEanExc extends StmException {
	private static final long serialVersionUID = 5359531014601737617L;

	private String ean;
	
	public StmUnbekannteEanExc(String ean) {
		super("Unbekannte EAN '" + ean + "'");
		setEan(ean);
		setExcCode(StmExceptionCode.EINKAUFSEAN_UNBEKANNT);
	}
	
	public void setEan(String ean) {
		this.ean = ean;
	}
	
	public String getEan() {
		return ean;
	}
}
