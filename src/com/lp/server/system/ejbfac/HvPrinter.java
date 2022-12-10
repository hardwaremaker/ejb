package com.lp.server.system.ejbfac;

import com.lp.server.util.report.JasperPrintLP;

public interface HvPrinter {

	void print(JasperPrintLP print);

	void print(JasperPrintLP[] prints);
	
	boolean exists();
	
	//TODO getError
}
