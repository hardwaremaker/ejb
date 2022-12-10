package com.lp.server.fertigung.service;

public interface ILosFLRData {
	
	String getStatusCnr();
	void setStatusCnr(String statusCnr);
	
	String getKundeName();
	void setKundeName(String kundeName);
	
	String getStklArtikelbezeichnung();
	void setStklArtikelbezeichnung(String bez);

	String getStklArtikelbezeichnung2();
	void setStklArtikelbezeichnung2(String bez2);
	
	Long getLosBeginnMs();
	void setLosBeginnMs(Long losBeginnMs);
	
	Long getLosEndeMs();
	void setLosEndeMs(Long losEndeMs);
	
	String getFertigungsort();
	void setFertigungsort(String fertigungsort);
	
	String getArtikelCnr();
	void setArtikelCnr(String artikelCnr);
	
	String getAuftragCnr();
	void setAuftragCnr(String auftragCnr);
}
