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
package com.lp.server.artikel.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class SeriennrChargennrAufLagerDto implements Serializable {
	/**
	 * 
	 */

	private Long keyFuerSortierung = null;

	public Long getKeyFuerSortierung() {

		if (keyFuerSortierung == null) {

			// Nachsehen, ob Ziffernteil vorhanden
			int iBeginn = -1;
			int iEnde = -1;
			if (getCSeriennrChargennr() != null) {
				for (int u = 0; u < getCSeriennrChargennr().length(); u++) {

					char c = getCSeriennrChargennr().charAt(u);
					if (c == '0' || c == '1' || c == '2' || c == '3'
							|| c == '4' || c == '5' || c == '6' || c == '7'
							|| c == '8' || c == '9') {
						if (iBeginn == -1) {
							iBeginn = u;
							continue;
						}
						if (iBeginn > -1) {
							iEnde = u;
						}

					} else {
						// Wenns schon ein Beginn gab, dann Abbruch
						if (iBeginn > -1) {
							break;
						}
					}

				}
			}
			// Nun als Integer konvertieren
			if (iBeginn > -1 && iEnde > -1) {
				keyFuerSortierung = new Long(getCSeriennrChargennr().substring(
						iBeginn, iEnde + 1));
			} else {
				keyFuerSortierung = 999999999999999999L;
			}

		}

		return keyFuerSortierung;
	}

	private static final long serialVersionUID = 1L;
	private BigDecimal nMenge;
	private Short bSeriennr;
	private String cSeriennrChargennr;
	private Timestamp tBuchungszeit;

	public BigDecimal getNMenge() {
		return nMenge;
	}

	public String getCSeriennrChargennr() {
		return cSeriennrChargennr;
	}

	public Short getBSeriennr() {
		return bSeriennr;
	}

	public Timestamp getTBuchungszeit() {
		return tBuchungszeit;
	}

	public void setNMenge(BigDecimal nMenge) {
		this.nMenge = nMenge;
	}

	public void setCSeriennrChargennr(String cSeriennrChargennr) {
		this.cSeriennrChargennr = cSeriennrChargennr;
	}

	public void setBSeriennr(Short bSeriennr) {
		this.bSeriennr = bSeriennr;
	}

	public void setTBuchungszeit(Timestamp tBuchungszeit) {
		this.tBuchungszeit = tBuchungszeit;
	}

}
