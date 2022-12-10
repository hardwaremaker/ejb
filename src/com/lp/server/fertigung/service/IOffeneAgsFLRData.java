package com.lp.server.fertigung.service;

import java.math.BigDecimal;

public interface IOffeneAgsFLRData {
	Integer getAuftragId() ;
	String getAuftragCnr() ;
	boolean isOverdue() ;
	Long getFinalTermin() ;
	String getLosProjekt() ;
	String getTaetigkeitArtikelKBez() ;
	
	String getPartlistItemCnr() ;
	String getPartlistItemDescription() ;
	String getPartlistItemShortDescription() ;
	
	BigDecimal getActualTime();
	boolean isStartTimeMoveable();
	boolean isFinishTimeMoveable();
	BigDecimal getOpenQuantity();
	Long getLosEndeTermin();
}
