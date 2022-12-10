package com.lp.server.angebotstkl.service;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;

import javax.ejb.Remote;

import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.report.JasperPrintLP;
import com.lp.util.EJBExceptionLP;
import com.lp.util.LPDatenSubreport;

@Remote
public interface AngebotstklreportFac {

	public JasperPrintLP printAngebotstkl(Integer iIdAngebotstkl, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printAngebotstklmenenstaffel(Integer iIdAngebotstkl, Timestamp tVergleichsdatum, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public JasperPrintLP printEinkaufsangebot(Integer einkaufsangebotIId, int iSortierung, Timestamp tGeplanterFertigungstermin, TheClientDto theClientDto)
			throws EJBExceptionLP, RemoteException;

	public void alteVersionInDokumentenablageKopierenUndAgstklAufGeaendertStellen(Integer agstklIId,
			TheClientDto theClientDto);
	public JasperPrintLP printVergleich(Integer einkaufsangebotIId, int iSortierung, TheClientDto theClientDto);
	
	public final static String REPORT_MODUL = "angebotstkl";

	public final static String REPORT_ANGEBOTSTUECKLISTE = "as_angebotstkl.jasper";
	public final static String REPORT_EINKAUFSANGEBOT = "as_einkaufsangebot.jasper";
	public final static String REPORT_ANGEBOTSTUECKLISTEMENGENSTAFFEL = "as_angebotstklmengenstaffel.jasper";
	public final static String REPORT_VERGLEICH = "as_vergleich.jasper";
	
	//NUR FUER MAILVERSAND
	public final static String REPORT_EKAGAMIL = "as_egakmail.jasper";
	
	
	public LPDatenSubreport getSubreportMaterial(TheClientDto theClientDto, Timestamp tVergleichsdatum,
			AgstklDto agstklDto)  throws RemoteException;
	public BigDecimal getSummeMaterial(AgstklDto agstklDto, TheClientDto theClientDto) throws RemoteException;
	
	
}
