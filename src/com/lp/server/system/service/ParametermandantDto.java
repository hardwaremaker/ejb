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
package com.lp.server.system.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.TreeMap;

import javax.persistence.Transient;

import com.lp.server.personal.service.ZeitdatenDto;
import com.lp.server.util.IMultipleKeyfields;
import com.lp.util.EJBExceptionLP;

@HvDtoLogClass(name = HvDtoLogClass.PARAMETERMANDANT)
public class ParametermandantDto implements Serializable, IMultipleKeyfields {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String cNr;
	private String mandantCMandant;
	private String cWert;
	private Timestamp tAendern;
	private Integer personalIIdAendern;
	private String cBemerkungsmall;
	private String cBemerkunglarge;
	private String cKategorie;
	private String cDatentyp;

	private TreeMap<java.sql.Timestamp, String> tmWerteGueltigab = null;

	@HvDtoLogIgnore
	public TreeMap<java.sql.Timestamp, String> getTmWerteGueltigab() {
		return tmWerteGueltigab;
	}

	@HvDtoLogIgnore
	public void setTmWerteGueltigab(
			TreeMap<java.sql.Timestamp, String> tmWerteGueltigab) {
		this.tmWerteGueltigab = tmWerteGueltigab;
	}

	public String getCNr() {
		return cNr;
	}

	public void setCNr(String cNr) {
		this.cNr = cNr;
	}

	public String getMandantCMandant() {
		return mandantCMandant;
	}

	public void setMandantCMandant(String mandantCMandant) {
		this.mandantCMandant = mandantCMandant;
	}

	public String getCWert() {
		return cWert;
	}

	@HvDtoLogIgnore
	public static ParametermandantDto clone(ParametermandantDto orig) {
		ParametermandantDto klon = new ParametermandantDto();
		klon.setCBemerkunglarge(orig.getCBemerkunglarge());
		klon.setCBemerkungsmall(orig.getCBemerkungsmall());
		klon.setCDatentyp(orig.getCDatentyp());
		klon.setCKategorie(orig.getCKategorie());
		klon.setCNr(orig.getCNr());
		klon.setCWert(orig.getCWert());
		klon.setMandantCMandant(orig.getMandantCMandant());
		klon.setPersonalIIdAendern(orig.getPersonalIIdAendern());
		klon.setTAendern(orig.getTAendern());
		klon.setTmWerteGueltigab(orig.getTmWerteGueltigab());

		return klon;
	}

	@HvDtoLogIgnore
	public String getCWertZumZeitpunkt(java.sql.Timestamp tZeitpunkt) {

		if (tZeitpunkt == null) {
			return cWert;
		} else {
			if (tmWerteGueltigab == null || tmWerteGueltigab.size() == 0) {
				return cWert;

			} else {
				Iterator<java.sql.Timestamp> it = tmWerteGueltigab.keySet()
						.iterator();

				java.sql.Timestamp tZeitpunktTemp = null;

				while (it.hasNext()) {

					java.sql.Timestamp tKey = it.next();

					if (tKey.getTime() <= tZeitpunkt.getTime()) {
						tZeitpunktTemp = tKey;
					}

				}

				if (tZeitpunktTemp == null) {
					return cWert;
				} else {
					return tmWerteGueltigab.get(tZeitpunktTemp);
				}

			}
		}

	}

	@HvDtoLogIgnore
	public Object getCWertAsObject() {

		Object ret = null;
		if (this.getCDatentyp().equals("java.lang.String")) {
			ret = getCWert();
		} else if (this.getCDatentyp().equals("java.lang.Integer")) {
			ret = new Integer(cWert);
		} else if (this.getCDatentyp().equals("java.lang.Boolean")) {
			ret = new Boolean(Integer.parseInt(getCWert()) != 0);
		} else if (this.getCDatentyp().equals("java.math.BigDecimal")) {
			ret = new BigDecimal(getCWert());
		} else if (this.getCDatentyp().equals("java.lang.Double")) {
			ret = new Double(getCWert());
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DATEN_INKOMPATIBEL,
					new Exception());
		}
		return ret;
	}

	@HvDtoLogIgnore
	public Object getCWertAsObjectZumZeitpunkt(java.sql.Timestamp tZeitpunkt) {

		Object ret = null;
		if (this.getCDatentyp().equals("java.lang.String")) {
			ret = getCWertZumZeitpunkt(tZeitpunkt);
		} else if (this.getCDatentyp().equals("java.lang.Integer")) {
			ret = new Integer(getCWertZumZeitpunkt(tZeitpunkt));
		} else if (this.getCDatentyp().equals("java.lang.Boolean")) {
			ret = new Boolean(
					Integer.parseInt(getCWertZumZeitpunkt(tZeitpunkt)) != 0);
		} else if (this.getCDatentyp().equals("java.math.BigDecimal")) {
			ret = new BigDecimal(getCWertZumZeitpunkt(tZeitpunkt));
		} else if (this.getCDatentyp().equals("java.lang.Double")) {
			ret = new Double(getCWertZumZeitpunkt(tZeitpunkt));
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DATEN_INKOMPATIBEL,
					new Exception());
		}
		return ret;
	}

	/**
	 * Den Wert als Boolean zurueckliefern sofern der Parameter vom Typ Boolean
	 * ist.
	 * 
	 * @return boolean des Parameterwerts
	 */
	public Boolean asBoolean() {
		if ("java.lang.Boolean".equals(getCDatentyp())
				|| "java.lang.Integer".equals(getCDatentyp())) {
			return new Boolean(Integer.parseInt(getCWert()) != 0);
		}

		throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DATEN_INKOMPATIBEL,
				new Exception());
	}

	/**
	 * Den CWert als Integer zur&uuml;ckliefern, sofern der Parameter vom Typ
	 * Integer ist.
	 * 
	 * @return den Integer-Wert sofern parseable und Datentyp java.lang.Integer
	 */
	public Integer asInteger() {
		if ("java.lang.Integer".equals(getCDatentyp())) {
			return new Integer(getCWert());
		}

		throw new EJBExceptionLP(EJBExceptionLP.FEHLER_DATEN_INKOMPATIBEL,
				new Exception());
	}

	public void setCWert(String cWert) {
		this.cWert = cWert;
	}

	public Timestamp getTAendern() {
		return tAendern;
	}

	public void setTAendern(Timestamp tAendern) {
		this.tAendern = tAendern;
	}

	public Integer getPersonalIIdAendern() {
		return personalIIdAendern;
	}

	public void setPersonalIIdAendern(Integer personalIIdAendern) {
		this.personalIIdAendern = personalIIdAendern;
	}

	public String getCBemerkungsmall() {
		return cBemerkungsmall;
	}

	public void setCBemerkungsmall(String cBemerkungsmall) {
		this.cBemerkungsmall = cBemerkungsmall;
	}

	public String getCBemerkunglarge() {
		return cBemerkunglarge;
	}

	public String getCKategorie() {
		return cKategorie;
	}

	public String getCDatentyp() {
		return cDatentyp;
	}

	public void setCBemerkunglarge(String cBemerkunglarge) {
		this.cBemerkunglarge = cBemerkunglarge;
	}

	public void setCKategorie(String cKategorie) {
		this.cKategorie = cKategorie;
	}

	public void setCDatentyp(String cDatentyp) {
		this.cDatentyp = cDatentyp;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ParametermandantDto)) {
			return false;
		}
		ParametermandantDto that = (ParametermandantDto) obj;
		if (!(that.cNr == null ? this.cNr == null : that.cNr.equals(this.cNr))) {
			return false;
		}
		if (!(that.mandantCMandant == null ? this.mandantCMandant == null
				: that.mandantCMandant.equals(this.mandantCMandant))) {
			return false;
		}
		if (!(that.cWert == null ? this.cWert == null : that.cWert
				.equals(this.cWert))) {
			return false;
		}
		if (!(that.tAendern == null ? this.tAendern == null : that.tAendern
				.equals(this.tAendern))) {
			return false;
		}
		if (!(that.personalIIdAendern == null ? this.personalIIdAendern == null
				: that.personalIIdAendern.equals(this.personalIIdAendern))) {
			return false;
		}
		if (!(that.cBemerkungsmall == null ? this.cBemerkungsmall == null
				: that.cBemerkungsmall.equals(this.cBemerkungsmall))) {
			return false;
		}
		if (!(that.cBemerkunglarge == null ? this.cBemerkunglarge == null
				: that.cBemerkunglarge.equals(this.cBemerkunglarge))) {
			return false;
		}
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.cNr.hashCode();
		result = 37 * result + this.mandantCMandant.hashCode();
		result = 37 * result + this.cWert.hashCode();
		result = 37 * result + this.tAendern.hashCode();
		result = 37 * result + this.personalIIdAendern.hashCode();
		result = 37 * result + this.cBemerkungsmall.hashCode();
		result = 37 * result + this.cBemerkunglarge.hashCode();
		return result;
	}

	public String toString() {
		String returnString = "";
		returnString += cNr;
		returnString += ", " + mandantCMandant;
		returnString += ", " + cWert;
		returnString += ", " + tAendern;
		returnString += ", " + personalIIdAendern;
		returnString += ", " + cBemerkungsmall;
		returnString += ", " + cBemerkunglarge;
		return returnString;
	}

	@Override
	@HvDtoLogIgnore
	@Transient
	public String[] getMKValue() {
		return new String[] { getCNr(), getCKategorie(), getMandantCMandant() };
	}
}
