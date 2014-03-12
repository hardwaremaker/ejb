/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2014 HELIUM V IT-Solutions GmbH
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
package com.lp.util;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * Basis fuer die Preisberechnung verschiedener Belegarten. <br>
 * Aufgrunf der fachlichen Anforderungen ist es notwendig, dass Belegpositionen
 * mit Preisinformationen in verschiedenen Varianten enthalten. <br>
 * Zusaetzlich ist die Waehrung der Preisinformationen hinterlegt.
 * </p>
 * <p>
 * Copyright Logistik Pur GmbH (c) 2005
 * </p>
 * <p>
 * Erstellungsdatum 2005-03-17
 * </p>
 * <p>
 * </p>
 * 
 * @author uli walch
 * @version 1.0
 */
public class BelegpositionPreisDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public BigDecimal bdNettoeinzelpreis;
	public BigDecimal bdNettoeinzelpreisPlusAufschlaege;
	public BigDecimal bdRabattsumme;
	public BigDecimal bdNettogesamtpreis;
	public BigDecimal bdNettogesamtpreisPlusAufschlaege;
	public BigDecimal bdNettogesamtpreisPlusAufschlaegeMinusAbschlaege;
	public BigDecimal bdMwstsumme;
	public BigDecimal bdBruttogesamtpreis;

	public String sWaehrung;
	public Double ddRabattsatz;
	public Integer iIdMwstsatz;

	public BelegpositionPreisDto() {
		initialize();
	}

	public void initialize() {
		bdNettoeinzelpreis = new BigDecimal(0);
		bdNettoeinzelpreisPlusAufschlaege = new BigDecimal(0);
		bdRabattsumme = new BigDecimal(0);
		bdNettogesamtpreis = new BigDecimal(0);
		bdNettogesamtpreisPlusAufschlaege = new BigDecimal(0);
		bdNettogesamtpreisPlusAufschlaegeMinusAbschlaege = new BigDecimal(0);
		bdMwstsumme = new BigDecimal(0);
		bdBruttogesamtpreis = new BigDecimal(0);

		sWaehrung = null;
		ddRabattsatz = new Double(0);
		iIdMwstsatz = new Integer(0);
	}

	public boolean isEmpty() {
		boolean bIsEmpty = false;

		if (bdNettoeinzelpreis.compareTo(new BigDecimal(0)) == 0
				&& bdNettoeinzelpreisPlusAufschlaege
						.compareTo(new BigDecimal(0)) == 0
				&& bdRabattsumme.compareTo(new BigDecimal(0)) == 0
				&& bdNettogesamtpreis.compareTo(new BigDecimal(0)) == 0
				&& bdNettogesamtpreisPlusAufschlaege
						.compareTo(new BigDecimal(0)) == 0
				&& bdNettogesamtpreisPlusAufschlaegeMinusAbschlaege
						.compareTo(new BigDecimal(0)) == 0
				&& bdMwstsumme.compareTo(new BigDecimal(0)) == 0
				&& bdBruttogesamtpreis.compareTo(new BigDecimal(0)) == 0
				&& sWaehrung == null && ddRabattsatz.doubleValue() == 0
				&& iIdMwstsatz.intValue() == 0) {
			bIsEmpty = true;
		}

		return bIsEmpty;
	}
}
