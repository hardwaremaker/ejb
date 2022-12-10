package com.lp.server.lieferschein.service;

public interface ILieferscheinFLRData {

	void setStatusCnr(String statusCnr);
	String getStatusCnr();

	void setKundeIdLieferadresse(Integer kundeId);
	Integer getKundeIdLieferadresse();
}
