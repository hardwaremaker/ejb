package com.lp.server.finanz.ejbfac;

import java.math.BigDecimal;

import com.lp.server.finanz.service.BuchenFac;
import com.lp.server.finanz.service.BuchungdetailDto;

public class BuchungdetailUtils {

	public static BuchungdetailDto createGegenbuchung(BuchungdetailDto detailDto) {
		BuchungdetailDto other = (BuchungdetailDto) detailDto.clone() ;
		other.swapSollHaben();
		other.swapKonten() ;
		return other ;
	}
	
	public static BuchungdetailDto soll(Integer kontoIId, Integer kontoIIdGegenkonto, BigDecimal nBetrag, BigDecimal nUst) {
		return new BuchungdetailDto(BuchenFac.SollBuchung, kontoIId, kontoIIdGegenkonto, nBetrag, nUst) ;		
	}

	public static BuchungdetailDto haben(Integer kontoIId, Integer kontoIIdGegenkonto, BigDecimal nBetrag, BigDecimal nUst) {
		return new BuchungdetailDto(BuchenFac.HabenBuchung, kontoIId, kontoIIdGegenkonto, nBetrag, nUst) ;		
	}

	public static BuchungdetailDto haben(Integer kontoIId, BigDecimal nBetrag, BigDecimal nUst) {
		return new BuchungdetailDto(BuchenFac.HabenBuchung, kontoIId, null, nBetrag, nUst) ;		
	}
}
