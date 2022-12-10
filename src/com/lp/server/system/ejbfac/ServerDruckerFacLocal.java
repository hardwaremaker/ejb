package com.lp.server.system.ejbfac;

import javax.ejb.Local;

import com.lp.server.system.service.TheClientDto;
import com.lp.server.system.service.ServerDruckerFac.Format;
import com.lp.server.util.report.JasperPrintLP;

@Local
public interface ServerDruckerFacLocal {

	HvPrinter createHvPrinter(String printername);
	
	void print(JasperPrintLP print, HvPrinter hvPrinter);
	
	void printMehrereSeiten(JasperPrintLP[] prints, HvPrinter hvPrinter);
	
	boolean exists(HvPrinter hvPrinter);
	
	String getPrinterNameByArbeitsplatzparameter(String arbeitsplatzparameter, TheClientDto theClientDto);

	String getPrinterNameByArbeitsplatzparameterOhneExc(String arbeitsplatzparameter, TheClientDto theClientDto);

	HvPrinter createMobileDefaultPrinter(String printerName, TheClientDto theClientDto);
	HvPrinter createMobileDefaultPagePrinter(String printerName, TheClientDto theClientDto);
	HvPrinter createMobileDefaultLabelPrinter(String printerName, TheClientDto theClientDto);
	HvPrinter createMobilePrinter(String printer, Format format, TheClientDto theClientDto);
	
	void printMobile(JasperPrintLP print, HvPrinter hvPrinter);	
	void printMobileMehrere(JasperPrintLP[] prints, HvPrinter hvPrinter);
}
