package com.lp.server.fertigung.service;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.Remote;

import com.lp.server.system.service.TheClientDto;

@Remote
public interface FertigungImportFac {
	
	static class GSKassenFieldnames {
		public static final String RECHNUNGSDATUM = "Rech.-Datum";
		public static final String POSITIONSNUMMER = "Pos-Nr.";
		public static final String RECHNUNGSNUMMER = "Rech.-Nr.";
		public static final String ARTIKELNUMMER = "Artikel-Nr.";
		public static final String ARTIKELBEZEICHNUNG = "Artikel";
		public static final String MENGE = "Menge";
		public static final String ABSCHLUSSDATUM = "Datum";
		public static final String EINZELPREIS = "E-Preis";
		public static final String GESAMTPREIS = "G-Preis";
		public static final String ZAHLUNGSART = "Zahlungsart";
		public static final String PLU = "PLU";
		public static final String HAUPTWARENGRUPPE = "Hauptwarengruppe";
	}

	public ImportPruefergebnis pruefeLosimportXLS(byte[] xlsDatei,
			TheClientDto theClientDto);
	public void importiereLoseXLS(ImportPruefergebnis importPruefergebnis,
			TheClientDto theClientDto);
	
	public VendidataArticleConsumptionImportResult importVendidataArticleConsumptionXML(
			String xmlContent, boolean checkOnly, TheClientDto theClientDto);

	public VendidataArticleConsumptionImportResult importVendidataArticleConsumptionXML(
			String xmlContent, String payloadReference, boolean checkOnly, TheClientDto theClientDto);

	public VendidataArticleConsumptionImportResult importVendidataExpiredProductsXML(
			String xmlContent, boolean checkOnly, TheClientDto theClientDto);
	
	public VendidataArticleConsumptionImportResult importArtikelConsumption( 
			List<VendidataArticleConsumption> consumption, TheClientDto theClientDto);
	
	public VendidataArticleConsumptionImportResult importFehlmengeFromVendidataArtikelConsumption( 
			VendidataArticleConsumption consumption, TheClientDto theClientDto);
	
	VerbrauchsartikelImportResult importCsvVerbrauchsartikel(
			List<String[]> allLines, boolean checkOnly,
			TheClientDto theClientDto) throws RemoteException;
}
