package com.lp.server.forecast.service;

import javax.ejb.Remote;

import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.report.JasperPrintLP;

@Remote
public interface ForecastReportFac {

	public final static String REPORT_MODUL = "forecast";

	public final static String REPORT_BESCHAFFUNG = "fc_beschaffung.jasper";
	public final static String REPORT_UEBERSICHT = "fc_uebersicht.jasper";
	public final static String REPORT_DELTALISTE = "fc_deltaliste.jasper";
	public final static String REPORT_GEPLANTERUMSATZ = "fc_geplanterumsatz.jasper";
	public final static String REPORT_LINIENABRUFE = "fc_linienabrufe.jasper";
	public final static String REPORT_FORECASTPOSITIONEN = "fc_forecastpositionen.jasper";
	public final static String REPORT_LIEFERSITUATION = "fc_liefersituation.jasper";
	
	public final int BESCHAFFUNG_SORTIERUNG_AUFTRAG_STRUKTUR = 0;
	public final int BESCHAFFUNG_SORTIERUNG_LIEFERANT = 1;
	

	
	public final int GEPLANTERUMSATZ_OPTION_ARTIKEL = 0;
	public final int GEPLANTERUMSATZ_OPTION_ARTIKELGRUPPE = 1;
	
	public JasperPrintLP printBeschaffung(Integer forecastauftragIId,String forecastartCNr,String statusCNr, 
			int iSortierung, TheClientDto theClientDto);
	public JasperPrintLP printForecastUebersicht( TheClientDto theClientDto);
	public JasperPrintLP printDeltaliste(Integer fclieferadresseIId, boolean bMitStuecklistePositionen, TheClientDto theClientDto);
	public JasperPrintLP printLinienabrufe(java.sql.Date dAusliefertermin,TheClientDto theClientDto);
	public JasperPrintLP printForecastpositionen(Integer artikelIId,boolean bAlleMandanten,
			TheClientDto theClientDto);
	
	public JasperPrintLP printLiefersituation(Integer forecastauftragIId,
			TheClientDto theClientDto);
	
	public JasperPrintLP printGeplanterUmsatz(Integer forecastIId,int iOption, java.sql.Timestamp tVon,
			java.sql.Timestamp tBis, String artikelNrVon, String artikelNrBis,
			TheClientDto theClientDto);
	
}
