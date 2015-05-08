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
package com.lp.server.artikel.service;

import java.io.Serializable;
import java.math.BigDecimal;

import com.lp.util.Helper;

/**
 * <p>
 * In diesem Dto werden alle bei der Verkaufspreisfindung benoetigten Felder
 * transportiert.
 * <p>
 * Copyright Logistik Pur GmbH (c) 2004
 * </p>
 * <p>
 * Erstellungsdatum 2004-08-01
 * </p>
 * <p>
 * </p>
 * 
 * @author uli walch
 * @version 1.0
 */
public class VerkaufspreisDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** Artikeleinzelpreis. Der empfohlene Verkaufspreis in Mandantenwaehrung. */
	public BigDecimal einzelpreis;
	/** Artikeleinzelpreis in Mandantenwaehrung * Rabattsatz. */
	public BigDecimal rabattsumme;
	/** Artikeleinzelpreis in Mandantenwaehrung * Rabattsatz * Zusatzrabattsatz. */
	private BigDecimal nZusatzrabattsumme;
	/**
	 * Artikeleinzelpreis in Mandantenwaehrung - Rabattsumme - Zusatzrabattsatz.
	 */
	public BigDecimal nettopreis;
	/** Nettopreis in Mandantenwaehrung * Mwstsatz. */
	public BigDecimal mwstsumme;
	/** Nettopreis in Mandantenwaehrung + Mwstsumme. */
	public BigDecimal bruttopreis;
	public boolean bKommtVonFixpreis = false;

	public BigDecimal bdMaterialzuschlag = new BigDecimal(0);
	/**
	 * Der Rabattsatz, der auf diesen Artikel gewaehrt wird. Bestimmt ueber
	 * VKPF.
	 */
	public Double rabattsatz;
	/**
	 * Der Zusatzrabattsatz, der auf diesen Artikel gewaehrt wird. Bestimmt
	 * durch den Benutzer.
	 */
	private Double ddZusatzrabattsatz;
	/** Der gueltige Mwstsatz. Kommt als Parameter in die VKPF. */
	public Integer mwstsatzIId;

	/** null wenn in Mandantenw&auml;hrung, sonst W&auml;hrungskennzeichen */
	public String waehrungCNr;

	/** tempor&auml;r der Kurs f&uuml;r den Bestpreis vergleich */
	public BigDecimal tempKurs;

	public VerkaufspreisDto() {
		initialize();
	}

	public void initialize() {
		einzelpreis = new BigDecimal(0);
		rabattsumme = new BigDecimal(0);
		nZusatzrabattsumme = new BigDecimal(0);
		nettopreis = new BigDecimal(0);
		mwstsumme = new BigDecimal(0);
		bruttopreis = new BigDecimal(0);

		rabattsatz = new Double(0);
		ddZusatzrabattsatz = new Double(0);
		mwstsatzIId = null;
		waehrungCNr = null;
		tempKurs = new BigDecimal(1);
	}

	public String toString() {
		StringBuffer buff = new StringBuffer("VerkaufspreisDto");
		buff.append("\n  Einzelpreis : ").append(einzelpreis)
				.append("\n  Rabattsumme : ").append(rabattsumme)
				.append("\n  Zusatzrabattsumme : ").append(nZusatzrabattsumme)
				.append("\n  Matwerialzuschlag : ").append(bdMaterialzuschlag)
				.append("\n  Nettopreis  : ").append(nettopreis)
				.append("\n  Mwstsumme   : ").append(mwstsumme)
				.append("\n  Bruttopreis : ").append(bruttopreis)
				.append("\n  Rabattsatz  : ").append(rabattsatz)
				.append("\n  Zusatzrabattsatz  : ").append(ddZusatzrabattsatz)
				.append("\n  Mwstsatz Id : ").append(mwstsatzIId.toString())
				.append("\n  W\u00E4hrungCNr  : ")
				.append((waehrungCNr == null ? "" : waehrungCNr))
				.append("\n  Kurs        : ").append(tempKurs.toString());

		return buff.toString();
	}

	public boolean isEmpty() {
		boolean bIsEmpty = false;

		if (einzelpreis.compareTo(new BigDecimal(0)) == 0
				&& rabattsumme.compareTo(new BigDecimal(0)) == 0
				&& nZusatzrabattsumme.compareTo(new BigDecimal(0)) == 0
				&& nettopreis.compareTo(new BigDecimal(0)) == 0
				&& mwstsumme.compareTo(new BigDecimal(0)) == 0
				&& bruttopreis.compareTo(new BigDecimal(0)) == 0
				&& rabattsatz.compareTo(new Double(0)) == 0
				&& ddZusatzrabattsatz.compareTo(new Double(0)) == 0
				&& mwstsatzIId == null) {
			bIsEmpty = true;
		}

		return bIsEmpty;
	}

	public BigDecimal getNZusatzrabattsumme() {
		return this.nZusatzrabattsumme;
	}

	public void setNZusatzrabattsumme(BigDecimal nZusatzrabattsummeI) {
		this.nZusatzrabattsumme = nZusatzrabattsummeI;
	}

	public Double getDdZusatzrabattsatz() {
		return this.ddZusatzrabattsatz;
	}

	public void setDdZusatzrabattsatz(Double ddZusatzrabattsatzI) {
		this.ddZusatzrabattsatz = ddZusatzrabattsatzI;
	}

	public BigDecimal getNettpreisOhneMaterialzuschlag() {
		if (bdMaterialzuschlag != null) {
			return nettopreis.subtract(bdMaterialzuschlag);
		} else {
			return nettopreis;
		}

	}

	public BigDecimal getNetto2Compare() {
		if (waehrungCNr != null) {
			return Helper.rundeKaufmaennisch(
					nettopreis.divide(tempKurs, 4, BigDecimal.ROUND_HALF_EVEN),
					2);
		}
		return nettopreis;
	}

}
