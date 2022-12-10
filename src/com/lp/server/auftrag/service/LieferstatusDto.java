package com.lp.server.auftrag.service;

import java.io.Serializable;

public class LieferstatusDto implements Serializable {
	private Integer iId;

	private Integer auftragpositionIId;

	public Integer getAuftragpositionIId() {
		return auftragpositionIId;
	}

	public void setAuftragpositionIId(Integer auftragpositionIId) {
		this.auftragpositionIId = auftragpositionIId;
	}

	private Integer lieferscheinpositionIId;

	public Integer getLieferscheinpositionIId() {
		return lieferscheinpositionIId;
	}

	public void setLieferscheinpositionIId(Integer lieferscheinpositionIId) {
		this.lieferscheinpositionIId = lieferscheinpositionIId;
	}

	private static final long serialVersionUID = 1L;

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}
	
	private Integer rechnungpositionIId;

	public Integer getRechnungpositionIId() {
		return rechnungpositionIId;
	}

	public void setRechnungpositionIId(Integer rechnungpositionIId) {
		this.rechnungpositionIId = rechnungpositionIId;
	}

}
