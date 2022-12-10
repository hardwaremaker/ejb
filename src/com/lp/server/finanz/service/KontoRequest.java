package com.lp.server.finanz.service;

import java.io.Serializable;

public class KontoRequest implements Serializable {
	private static final long serialVersionUID = -7351761485554029288L;
	private String kontoTyp;
	private String kontoCnr;
	private Boolean exist;

	public KontoRequest(String kontoTyp, String kontoCnr) {
		this.setKontoTyp(kontoTyp);
		this.setKontoCnr(kontoCnr);
	}

	public String getKontoTyp() {
		return kontoTyp;
	}

	public void setKontoTyp(String kontoTyp) {
		this.kontoTyp = kontoTyp;
	}

	public String getKontoCnr() {
		return kontoCnr;
	}

	public void setKontoCnr(String kontoCnr) {
		this.kontoCnr = kontoCnr;
	}

	public Boolean getExist() {
		return exist;
	}

	public void setExist(Boolean exist) {
		this.exist = exist;
	}
}

//class SachKontoRequest extends KontoRequest {
//	private static final long serialVersionUID = -281738591949306770L;
//	public SachKontoRequest(String kontoCnr) {
//		super(FinanzServiceFac.KONTOTYP_SACHKONTO, kontoCnr);
//	}
//}
//
//class DebitorenKontoRequest extends KontoRequest {
//	private static final long serialVersionUID = 5007969073389057780L;
//	public DebitorenKontoRequest(String kontoCnr) {
//		super(FinanzServiceFac.KONTOTYP_DEBITOR, kontoCnr);
//	}
//}
//
//class KreditorenKontoRequest extends KontoRequest {
//	private static final long serialVersionUID = -2161230792614628339L;
//	public KreditorenKontoRequest(String kontoCnr) {
//		super(FinanzServiceFac.KONTOTYP_KREDITOR, kontoCnr);
//	}
//}
