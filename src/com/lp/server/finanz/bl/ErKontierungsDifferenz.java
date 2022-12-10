package com.lp.server.finanz.bl;

import java.math.BigDecimal;

import com.lp.server.finanz.service.FibuexportDto;
import com.lp.server.util.logger.LogEventPayload;

public class ErKontierungsDifferenz implements LogEventPayload {
	private FibuexportDto[] exportDtos ;
	private BigDecimal differenz ;
	
	public ErKontierungsDifferenz(FibuexportDto[] exportDtos, BigDecimal differenz) {
		this.exportDtos = exportDtos ;
		this.differenz = differenz ;
	}
	
	@Override
	public String asString() {
		StringBuffer s = new StringBuffer("ER-Kontierungsdifferenz [" 
				+ differenz.toPlainString()  + ", " + exportDtos[0].getBelegart() + " " + exportDtos[0].getBelegnummer() + ", OP: " + exportDtos[0].getOPNummer() + " mit " + exportDtos.length + " Eintr\u00e4gen]\n") ;
		for(int i = 1; i < exportDtos.length; i++) {
			s.append("S" + i + ": Soll:" 
				+ exportDtos[i].getSollbetragBD().toPlainString() 
				+ ", Steuer: " + exportDtos[i].getSteuerBD() + "\n") ;
		}
		s.append("H:" + exportDtos[0].getHabenbetragBD().toPlainString()) ;
		
		return s.toString() ;
	}
}
