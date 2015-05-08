/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2015 HELIUM V IT-Solutions GmbH
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published 
 * by the Free Software Foundation, either version 3 of theLicense, or 
 * (at your option) any later version.
 * 
 * According to sec. 7 of the GNU Affero General Public License, version 3, 
 * the terms of the AGPL are supplemented with the following terms:
 * 
 * "HELIUM V" and "HELIUM 5" are registered trademarks of 
 * HELIUM V IT-Solutions GmbH. The licensing of the program under the 
 * AGPL does not imply a trademark license. Therefore any rights, title and
 * interest in our trademarks remain entirely with us. If you want to propagate
 * modified versions of the Program under the name "HELIUM V" or "HELIUM 5",
 * you may only do so if you have a written permission by HELIUM V IT-Solutions 
 * GmbH (to acquire a permission please contact HELIUM V IT-Solutions
 * at trademark@heliumv.com).
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact: developers@heliumv.com
 ******************************************************************************/
package com.lp.server.system.ejbfac;

import java.rmi.RemoteException;

import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.OrientationRequested;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import net.sf.jasperreports.engine.type.OrientationEnum;

import com.lp.server.system.service.ServerDruckerFac;
import com.lp.server.util.Facade;
import com.lp.server.util.report.JasperPrintLP;

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
@Stateless
public class ServerDruckerFacBean extends Facade implements ServerDruckerFac{

	
	@WebMethod
	@WebResult(name="sPrinters")
	public String[] getServerPrinters() throws RemoteException {
		PrintService[] printers = PrintServiceLookup.lookupPrintServices(null,null);
		String [] sPrinters = new String[printers.length];
		for(int i=0;i<printers.length;i++){
			sPrinters[i] = printers[i].getName();
		}
		if (sPrinters != null){
			return sPrinters;
		} else {
			return new String[]{"Kein Drucker gefunden"};
		}
	}

	@WebMethod
	@WebResult(name="sDefaultPrinter")
	public String getServerStandardPrinter() throws RemoteException {
		if (PrintServiceLookup.lookupDefaultPrintService() != null){
			return PrintServiceLookup.lookupDefaultPrintService().getName();
		} else {
			return "";
		}
	}
	
	public static void print(JasperPrintLP toPrint, PrintService printerToUse)
	throws Throwable {
		JasperPrint print = toPrint.getPrint();
		if (print.getPages().size() > 0) {
			try {
				JRPrintServiceExporter exporter = new JRPrintServiceExporter();
				// set the report to print
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
				PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();

				PrintService printer = printerToUse;
				if (printer != null) {
					// Hochformat
					
					
					if (print.getOrientationValue() == OrientationEnum.PORTRAIT) {
						aset.add(OrientationRequested.PORTRAIT);
					}
					// Querformat
					else if (print.getOrientationValue() == OrientationEnum.LANDSCAPE) {
						aset.add(OrientationRequested.LANDSCAPE);
					}

					PrintServiceAttributeSet serviceAttributeSet = printer
					.getAttributes();

					exporter
					.setParameter(
							JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET,
							aset);
					exporter
					.setParameter(
							JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET,
							serviceAttributeSet);
					// Erweiterte Druckdialoge zzt nicht anzeigen
					exporter
					.setParameter(
							JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG,
							Boolean.FALSE);
					exporter
					.setParameter(
							JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG,
							Boolean.FALSE);
					// print it
					exporter.exportReport();
				}
			} catch (Exception ex) {

			}
		}
	}

}
