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

import com.lp.server.system.service.ArbeitsplatzparameterDto;
import com.lp.server.system.service.ParameterFac;
import com.lp.server.system.service.ServerDruckerFac;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.Facade;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.Helper;

import net.sf.jasperreports.engine.JasperPrint;

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
@Stateless
public class ServerDruckerFacBean extends Facade implements ServerDruckerFac, ServerDruckerFacLocal {
	
	@WebMethod
	@WebResult(name="sPrinters")
	public String[] getServerPrinters() throws RemoteException {
		PrintService[] printers = PrintServiceLookup.lookupPrintServices(null,null);
		if (printers.length < 1) {
			return new String[]{"Kein Drucker gefunden"};
		}
		
		String [] sPrinters = new String[printers.length];
		for(int i=0;i<printers.length;i++){
			sPrinters[i] = printers[i].getName();
		}
		
		return sPrinters;
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
	
	@Override
	public HvPrinter createHvPrinter(String printer) {
		if (Helper.isStringEmpty(printer)) 
			return new HvPrinterDummy();
		
		if (!isPrintingPrinter(printer)) {
			myLogger.info("Druckername '" + printer + "' wurde als nicht zu drucken erkannt => HvPrinterDummy wird erstellt");
			return new HvPrinterDummy();
		}
		
		if (printer.startsWith(PROXY_PROTOCOL_PREFIX)) {
			myLogger.info("Druckername '" + printer + "' wurde als Proxy-Druckername erkannt => HvPrinterProxy wird erstellt");
			return new HvPrinterProxy(printer);
		}
		
		myLogger.info("Druckername '" + printer + "' wurde als lokaler Drucker erkannt => HvPrinterJRExport wird erstellt");
		return new HvPrinterJRExport(printer);
	}
	
	private boolean isPrintingPrinter(String printer) {
		return !(NICHT_DRUCKEN.equals(printer) ||
				KEIN_DRUCKER_GEFUNDEN.equals(printer) ||
				printer.endsWith(DONT_PRINT));
	}

	@Override
	public boolean exists(HvPrinter hvPrinter) {
		return hvPrinter.exists();
	}
	
	@Override
	public void printMehrereSeiten(JasperPrintLP[] prints, HvPrinter hvPrinter) {
		if (prints == null) return;
		
		for (JasperPrintLP print : prints) {
			print(print, hvPrinter);
		}
	}
	
	@Override
	public void print(JasperPrintLP printLP, HvPrinter hvPrinter) {
		if (printLP == null || hvPrinter == null) return;
		
		JasperPrint print = printLP.getPrint();
		if (print.getPages().size() > 0) {
			hvPrinter.print(printLP);
		}
	}
	
	
	@Override
	public String getPrinterNameByArbeitsplatzparameter(String arbeitsplatzparameter, TheClientDto theClientDto) {
		ArbeitsplatzDruckerResult result = getPrinterNameByArbeitsplatzparameterOhneExcImpl(arbeitsplatzparameter, theClientDto);
		
		if (result.hasPrinterName()) {
			return result.getPrinterName();
		}

		throw EJBExcFactory.keinDruckernameHinterlegt(result.getPcName(), arbeitsplatzparameter);
	}

	@Override
	public String getPrinterNameByArbeitsplatzparameterOhneExc(String arbeitsplatzparameter,
			TheClientDto theClientDto) {
		ArbeitsplatzDruckerResult result = getPrinterNameByArbeitsplatzparameterOhneExcImpl(arbeitsplatzparameter, theClientDto);
		
		return result.getPrinterName();
	}
	
	private ArbeitsplatzDruckerResult getPrinterNameByArbeitsplatzparameterOhneExcImpl(String arbeitsplatzparameter,
			TheClientDto theClientDto) {
		String pcname = "";
		int index = theClientDto.getBenutzername().indexOf('|');
		if (index > 0) {
			pcname = theClientDto.getBenutzername().substring(index + 1).trim();
		}
		ArbeitsplatzparameterDto apDto = getBenutzerServicesFac()
				.holeArbeitsplatzparameter(pcname, arbeitsplatzparameter);
		
		return apDto != null ? new ArbeitsplatzDruckerResult(pcname, apDto.getCWert()) 
				: new ArbeitsplatzDruckerResult(pcname);
	}
	
	public HvPrinter createMobileDefaultPrinter(String printerName, TheClientDto theClientDto) {
		return createMobileDefaultPagePrinter(printerName, theClientDto);
	}
	
	public HvPrinter createMobileDefaultPagePrinter(String printerName, TheClientDto theClientDto) { 
		return createMobilePrinter(printerName, Format.Page, theClientDto);
	}
	
	public HvPrinter createMobileDefaultLabelPrinter(String printerName, TheClientDto theClientDto) {
		return createMobilePrinter(printerName, Format.Label, theClientDto);
	}
	
	public HvPrinter createMobilePrinter(String printer, Format format, TheClientDto theClientDto) {
		printer = getDefaultPrintername(printer, format, theClientDto);

		if (Helper.isStringEmpty(printer) || printer.equals(".")) {
			return new HvPrinterMissing();
		}
		
		if (!isPrintingPrinter(printer)) {
			myLogger.info("Druckername '" + printer + "' wurde als nicht zu drucken erkannt => HvPrinterDummy wird erstellt");
			return new HvPrinterDummy();
		}
		
		if (printer.startsWith(PROXY_PROTOCOL_PREFIX)) {
			myLogger.info("Druckername '" + printer + "' wurde als Proxy-Druckername erkannt => HvPrinterProxy wird erstellt");
			return new HvPrinterProxy(printer);
		}
		
		myLogger.info("Druckername '" + printer + "' wurde als lokaler Drucker erkannt => HvPrinterJRExport wird erstellt");
		return new HvPrinterJRExport(printer);
	}

	private String getDefaultPrintername(String optionalPrinter, Format format, TheClientDto theClientDto) {
		if(Helper.isStringEmpty(optionalPrinter)) {
			optionalPrinter = getPrinterNameByArbeitsplatzparameterOhneExc(
					format.equals(Format.Page) 
						? ParameterFac.ARBEITSPLATZPARAMETER_DEFAULT_MOBIL_DRUCKERNAME_SEITE
						: ParameterFac.ARBEITSPLATZPARAMETER_DEFAULT_MOBIL_DRUCKERNAME_ETIKETT, theClientDto);
		}

		if(Helper.isStringEmpty(optionalPrinter)) {
			optionalPrinter = format.equals(Format.Page)
					? getParameterFac().getDefaultMobilDruckernameSeite(theClientDto.getMandant())
					: getParameterFac().getDefaultMobilDruckernameEtikett(theClientDto.getMandant());
		}
		return optionalPrinter;
	}
	
	public void printMobile(JasperPrintLP print, HvPrinter hvPrinter) {
		if(print == null || hvPrinter == null) return;

		JasperPrint jprint = print.getPrint();
		if (jprint.getPages().size() > 0) {
			hvPrinter.print(print);
		}
	}
	
	// TODO Das ist noch nicht wirklich gut!
	// Problem: bei printMobile wird geprueft, ob der Druck ueberhaupt seiten hat,
	// beim printMobileMehrere kann das nicht ueberprueft werden. 
	// Daher sollte eigentlich auch der hvPrinter.print() das selbst pruefen
	@Override
	public void printMobileMehrere(JasperPrintLP[] prints, HvPrinter hvPrinter) {
		if(prints == null || prints.length == 0 || hvPrinter == null) return;
		hvPrinter.print(prints);
	}
	
	private class ArbeitsplatzDruckerResult {
		private String pcName;
		private String printerName;
		
		public ArbeitsplatzDruckerResult(String pcName, String printerName) {
			this.pcName = pcName;
			this.printerName = printerName;
		}
		public ArbeitsplatzDruckerResult(String pcName) {
			this.pcName = pcName;
		}		
		public String getPcName() {
			return pcName;
		}
		public String getPrinterName() {
			return printerName;
		}
		public boolean hasPrinterName() {
			return !Helper.isStringEmpty(getPrinterName());
		}
	}
}
