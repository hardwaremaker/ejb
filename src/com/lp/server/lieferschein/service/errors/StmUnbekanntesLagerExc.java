package com.lp.server.lieferschein.service.errors;

public class StmUnbekanntesLagerExc extends StmException {
	private static final long serialVersionUID = 6645084933872831297L;

	private Integer lager;
	
	public StmUnbekanntesLagerExc(Integer tourLagerNr) {
		super("Unbekanntes Lager '" + tourLagerNr + "'");
		setLager(tourLagerNr);
		setExcCode(StmExceptionCode.LAGER_UNBEKANNT);
	}
	
	public Integer getLager() {
		return lager;
	}
	
	public void setLager(Integer lager) {
		this.lager = lager;
	}
}
