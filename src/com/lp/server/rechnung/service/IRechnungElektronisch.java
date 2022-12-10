package com.lp.server.rechnung.service;


public interface IRechnungElektronisch {
	public Integer getVersandwegId() ;
	public void setVersandwegId(Integer versandwegId) ;

	public Integer getPartnerId() ;
	public void setPartnerId(Integer partnerId) ;
	
	RechnungDto getRechnungDto();
	void setRechnungDto(RechnungDto rechnungDto);
}
