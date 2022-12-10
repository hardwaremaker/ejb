package com.lp.server.bestellung.service;

public interface IBestellungFLRData {

	void setStatusCnr(String statusCnr);
	String getStatusCnr();

	void setLieferantId(Integer lieferantId);
	Integer getLieferantId();
}
