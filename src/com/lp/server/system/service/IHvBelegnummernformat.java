package com.lp.server.system.service;

public interface IHvBelegnummernformat {

	/**
	 * Liefert die L&auml;nge bzw. Anzahl der Stellen f&uuml;r die laufende Belegnummer
	 * 
	 * @return L&auml;nge der laufenden Belegnummer
	 */
	Integer getLaengeBelegnr();

	/**
	 * Liefert die L&auml;nge bzw. Anzahl der Stellen f&uuml;r die Gesch&auml;ftsjahreszahl
	 * 
	 * @return L&auml;nge der Gesch&auml;ftsjahreszahl
	 */
	Integer getLaengeGj();
	
	/**
	 * Liefert das/die Trennzeichen
	 * 
	 * @return Trennzeichen
	 */
	String getTrennzeichen();
	
	/**
	 * Liefert die (optionale) Mandantkennung.
	 * 
	 * @return (optionale) Mandantkennung
	 */
	String getMandantkennung();
	
	/**
	 * Liefert die Gesamtl&auml;nge einer Belegnummer.</br>
	 * Sie setzt sich aus den L&auml;ngen der Gesch&auml;ftsjahreszahl, des
	 * Trennzeichens, der Mandantkennung und der laufenden Nummer zusammen.
	 * 
	 * @return Gesamtl&auml;nge einer Belegnummer
	 */
	Integer getLaengeGesamt();
}
