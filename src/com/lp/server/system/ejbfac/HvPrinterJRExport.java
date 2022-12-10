package com.lp.server.system.ejbfac;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.OrientationRequested;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import net.sf.jasperreports.engine.type.OrientationEnum;

import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;
import com.lp.server.util.report.JasperPrintLP;

public class HvPrinterJRExport implements HvPrinter {
	private final ILPLogger myLogger = LPLogService.getInstance().getLogger(this.getClass());

	private String printer; 
	
	public HvPrinterJRExport(String printer) {
		this.printer = printer;
	}

	@Override
	public void print(JasperPrintLP print) {
		if (print == null) return;
		
		PrintService printService = findPrintService();
		if (!exists(printService)) return;
		
		printImpl(print.getPrint(), printService);
	}
	
	@Override
	public void print(JasperPrintLP[] prints) {
		PrintService printService = findPrintService();
		if (!exists(printService)) return;

		for (JasperPrintLP print : prints) {
			printImpl(print.getPrint(), printService);
		}
	}
	
	private void printImpl(JasperPrint print, PrintService printService) {
		try {
			JRPrintServiceExporter exporter = new JRPrintServiceExporter();
			// set the report to print
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
			PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();

			// Hochformat
			if (print.getOrientationValue() == OrientationEnum.PORTRAIT) {
				aset.add(OrientationRequested.PORTRAIT);
			}
			// Querformat
			else if (print.getOrientationValue() == OrientationEnum.LANDSCAPE) {
				aset.add(OrientationRequested.LANDSCAPE);
			}

			PrintServiceAttributeSet serviceAttributeSet = printService.getAttributes();

			exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, aset);
			exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, serviceAttributeSet);
			// Erweiterte Druckdialoge zzt nicht anzeigen
			exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE);
			exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE);
			// print it
			exporter.exportReport();
		} catch (Exception ex) {
			myLogger.error("Error during printing jasper print with JRPrintServiceExporter", ex);
			// TODO save error in errorobject
		}
	}

	@Override
	public boolean exists() {
		return exists(findPrintService());
	}
	
	private boolean exists(PrintService printService) {
		if (printService == null) {
			myLogger.warn("Drucker '" + printer + "' konnte nicht gefunden werden");
			return false;
		}
		return true;
	}
	
	private PrintService findPrintService() {
		if (printer == null) return null;
		
		PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
		for (PrintService printService : printServices) {
			if(printer.equals(printService.getName())) {
				return printService ;
			}
		}
		return null;
	}

}
