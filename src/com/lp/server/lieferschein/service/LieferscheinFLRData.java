package com.lp.server.lieferschein.service;

import java.io.Serializable;

public class LieferscheinFLRData implements ILieferscheinFLRData, Serializable {
	private static final long serialVersionUID = 1610185626827017794L;

	private String statusCnr;
	private Integer kundeIdLieferadresse;
	
	public LieferscheinFLRData() {
	}

	@Override
	public void setStatusCnr(String statusCnr) {
		this.statusCnr = statusCnr;
	}

	@Override
	public String getStatusCnr() {
		return statusCnr;
	}

	@Override
	public void setKundeIdLieferadresse(Integer kundeId) {
		this.kundeIdLieferadresse = kundeId;
	}

	@Override
	public Integer getKundeIdLieferadresse() {
		return kundeIdLieferadresse;
	}

}
