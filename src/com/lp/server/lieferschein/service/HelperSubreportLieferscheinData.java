package com.lp.server.lieferschein.service;

import java.io.Serializable;

import com.lp.util.LPDatenSubreport;

public class HelperSubreportLieferscheinData implements Serializable {
	private static final long serialVersionUID = -3541277879751701203L;

	private LPDatenSubreport subreportDaten ;
	private Integer statusTeillieferung;

	public LPDatenSubreport getSubreportDaten() {
		return subreportDaten;
	}
	public void setSubreportDaten(LPDatenSubreport subreportDaten) {
		this.subreportDaten = subreportDaten;
	}
	public Integer getStatusTeillieferung() {
		return statusTeillieferung;
	}
	public void setStatusTeillieferung(Integer statusTeillieferung) {
		this.statusTeillieferung = statusTeillieferung;
	}
}
