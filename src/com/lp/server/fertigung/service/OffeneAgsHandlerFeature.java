package com.lp.server.fertigung.service;

public interface OffeneAgsHandlerFeature {
	/**
	 * Mit diesem Feature wird die Auftragsnummer (sofern auftragsbezug) &uuml;bermittelt
	 * und auch das Finaldatum &uuml;berpr&uuml;ft
	 */
	final static String AUFTRAGS_DATUM = "AUFTRAGS_DATUM" ;
	
	/**
	 * Mit diesem Feature wird zus&auml;tzlich die Projektnummer &uuml;bermittelt
	 */
	final static String PROJEKT_NR = "PROJEKT_NR" ;
	
	/**
	 * Mit diesem Feature wird zus&auml;tzlich die Artikelkurzbezeichnung &uuml;bermittelt
	 */
	final static String ARTIKEL_KBEZ = "ARTIKEL_KBEZ" ;
	
	/**
	 * Soll die Istzeit f&uuml;r den jeweiligen Arbeitsgang ermittelt werden?
	 */
	final static String ISTZEIT_ERMITTELN = "ISTZEIT_ERMITTELN";	
	
	/**
	 * Schr&auml;nkt das Ergebnis auf nur den jeweils n&auml;chsten Arbeitsgang
	 * eines Loses ein.
	 */
	final static String NUR_NAECHSTER_AG = "NUR_NAECHSTER_AG";
	
	/**
	 * Mit diesem Feature wird zus&auml;tzlich die abgelieferte Menge &uuml;bermittelt
	 */
	final static String ABGELIEFERTE_MENGE = "ABGELIEFERTE_MENGE";
}
