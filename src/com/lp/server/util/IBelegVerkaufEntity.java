package com.lp.server.util;

import java.math.BigDecimal;

public interface IBelegVerkaufEntity {
	/**
	 * Der Nettogesamtpreis mit verstecktem Aufschlag minus Rabatten
	 * Heisst zwar *GESAMT* wird aber als *EINZEL* Preis verwendet,
	 * d.h. der Preis bezieht sich immer auf ein(!) Stueck
	 * @return
	 */
	BigDecimal getNNettogesamtpreisplusversteckteraufschlagminusrabatte();
	void setNNettogesamtpreisplusversteckteraufschlagminusrabatte(
			BigDecimal nNettogesamtpreisplusversteckteraufschlagminusrabatte);
	
	void setNNettoeinzelpreis(BigDecimal nettoEinzelpreis);
	/**
	 * Der Netto*EINZEL*Preis mit verstecktem Aufschlag</br>
	 * <p>Heisst zwar gesamt, wird aber immer als einzel-preis verwendet
	 * @param nettogesamt
	 */
	void setNNettogesamtpreisplusversteckteraufschlag(BigDecimal nettogesamt);
	void setNBruttogesamtpreis(BigDecimal bruttogesamt);
	void setNMaterialzuschlag(BigDecimal materialzuschlag);
	void setNMwstbetrag(BigDecimal mwstbetrag);
	void setNRabattbetrag(BigDecimal rabattbetrag);
	void setNNettogesamtpreis(BigDecimal nettoGesamt);

	BigDecimal getNNettoeinzelpreis();
	BigDecimal getNNettogesamtpreisplusversteckteraufschlag();
	BigDecimal getNBruttogesamtpreis();	
	BigDecimal getNMaterialzuschlag();
	BigDecimal getNNettogesamtpreis();

	Integer getArtikelIId();
	BigDecimal getNMenge();
	Integer getMwstsatzIId();
}
