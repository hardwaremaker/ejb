package com.lp.server.util;

import java.math.BigDecimal;

public interface IArtikelsetPreisUpdate {
//	void updateNettoPreis(Integer positionId, BigDecimal addDifferenz);
	
	/**
	 * Die spezifischen Properties der Entity initialisieren</br>
	 * Beispiel: Die Rechnungsposition hat eine Property NEinzelpreis, 
	 * die natuerlich auch initializiert geh&ouml;rt
	 * 
	 * @param positionEntity ist die generische Belegposition die auf 
	 * die spezifische Entity gecastet werden kann. Die Position wurde 
	 * zuvor im eigenen Kontext gelesen und zur Verf&uuml;gung gestellt.
	 */
	void initializePreis(IBelegVerkaufEntity positionEntity);
	
	/**
	 * Die spezifischen Properties der Entity auf den zum angegebenen
	 * Nettopreis passenden Wert setzen. &Uuml;blicherweise kann einfach
	 * der Preis &uuml;bernommen werden.
	 * @param positionEntity
	 * @param nettoPrice
	 */
	void setPreis(IBelegVerkaufEntity positionEntity, BigDecimal nettoPrice);
	
	/** 
	 * Den zus&auml;tzlich zu verteilenden Preis auf die spezifischen 
	 * Properties der Entity setzen.
	 * @param positionEntity
	 * @param addNettoPrice
	 */
	void addPreis(IBelegVerkaufEntity positionEntity, BigDecimal addNettoPrice);
}
