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
package com.lp.server.system.pkgenerator.format;

import java.io.Serializable;

/**
 * <p>
 * Logistik Pur Belegnummer
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * <p>
 * Erstellungsdatum <I>25. 10. 2004</I>
 * </p>
 * 
 * @author Martin Bluehweis
 * @version 1.0
 */
public class LpBelegnummer implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer primaryKey;
	private Integer geschaeftsJahr;
	private String mandantKuerzel;
	private Integer belegNummer;

	/**
	 * Konstruktor
	 * 
	 * @param primaryKey
	 *            Integer
	 * @param geschaeftsJahr
	 *            Integer
	 * @param mandantKuerzel
	 *            String
	 * @param belegNummer
	 *            Integer
	 */
	public LpBelegnummer(Integer primaryKey, Integer geschaeftsJahr,
			String mandantKuerzel, Integer belegNummer) {
		this.primaryKey = primaryKey;
		this.geschaeftsJahr = geschaeftsJahr;
		this.mandantKuerzel = mandantKuerzel;
		this.belegNummer = belegNummer;
	}

	/**
	 * Standardkonstruktor fuer die Formatierung von Belegnummern Dazu wird der
	 * Primary Key nicht benoetigt
	 * 
	 * @param geschaeftsJahr
	 *            Integer
	 * @param mandantKuerzel
	 *            String
	 * @param belegNummer
	 *            Integer
	 */
	public LpBelegnummer(Integer geschaeftsJahr, String mandantKuerzel,
			Integer belegNummer) {
		this.geschaeftsJahr = geschaeftsJahr;
		this.mandantKuerzel = mandantKuerzel;
		this.belegNummer = belegNummer;
	}

	public Integer getGeschaeftsJahr() {
		return geschaeftsJahr;
	}

	public Integer getBelegNummer() {
		return belegNummer;
	}

	public String getMandantKuerzel() {
		return mandantKuerzel;
	}

	public Integer getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(Integer primaryKey) {
		this.primaryKey = primaryKey;
	}

	public void setGeschaeftsJahr(Integer geschaeftsJahr) {
		this.geschaeftsJahr = geschaeftsJahr;
	}

	public void setBelegNummer(Integer lfdNummer) {
		this.belegNummer = lfdNummer;
	}

	public void setMandantKuerzel(String mandantKuerzel) {
		this.mandantKuerzel = mandantKuerzel;
	}

	public boolean equals(Object o) {
		if (o instanceof LpBelegnummer) {
			LpBelegnummer bnr = (LpBelegnummer) o;
			return (this.geschaeftsJahr.equals(bnr.geschaeftsJahr)
					&& this.primaryKey.equals(bnr.primaryKey)
					&& this.belegNummer.equals(bnr.belegNummer) && this.mandantKuerzel
					.equals(bnr.mandantKuerzel));
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 37 * result + geschaeftsJahr.hashCode();
		result = 37 * result + primaryKey.hashCode();
		result = 37 * result + belegNummer.hashCode();
		result = 37 * result + mandantKuerzel.hashCode() ;
		return result;		
	}
}
