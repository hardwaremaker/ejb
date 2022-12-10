package com.lp.server.rechnung.service;

import java.math.BigDecimal;

public interface IRechnungFLRData {
	void setKundeId(Integer kundeId) ;
	Integer getKundeId() ;
	
	void setGrossValue(BigDecimal grossValue) ;
	BigDecimal getGrossValue() ;
	
	void setNetValue(BigDecimal netValue) ;
	BigDecimal getNetValue() ;	
	
	void setOpenGrossValue(BigDecimal openValue) ;
	BigDecimal getOpenGrossValue() ;
	
	void setProject(String projectOrOrder) ;
	String getProject() ;
	
	void setCurrency(String currency) ;
	String getCurrency() ;
	
	void setStatusCnr(String statusCnr) ;
	String getStatusCnr() ;
}
