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
import java.sql.Timestamp;

public class WerkzeugDto implements Serializable {
	/**
	 * 
	 */
	private Integer iId;
	private String cNr;
	private Integer lieferantIId;
	private Integer lagerplatzIId;
	private String xKommentar;
	private Timestamp tKaufdatum;
	private BigDecimal nKaufpreis;
	private String mandantCNrStandort;
	private String cBez;

	private static final long serialVersionUID = 1L;

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getBezeichnung() {
		StringBuffer sbBez = new StringBuffer();
		if (getCBez() != null) {
			if (getCBez() != null
					&& getCBez().length() > 0) {
				sbBez.append(getCBez());
			} else {
				sbBez.append(getCNr());
			}
		} else {
			sbBez.append(getCNr());
		}
		return sbBez.toString();
	}
	
	public String getCBez() {
		return this.cBez;
	}

	public void setCBez(String cBez) {
		this.cBez = cBez;
	}

	public String getCNr() {
		return cNr;
	}

	public void setCNr(String cNr) {
		this.cNr = cNr;
	}

	public Integer getLieferantIId() {
		return lieferantIId;
	}

	public void setLieferantIId(Integer lieferantIId) {
		this.lieferantIId = lieferantIId;
	}

	public Integer getLagerplatzIId() {
		return lagerplatzIId;
	}

	public void setLagerplatzIId(Integer lagerplatzIId) {
		this.lagerplatzIId = lagerplatzIId;
	}

	public String getXKommentar() {
		return xKommentar;
	}

	public void setXKommentar(String xKommentar) {
		this.xKommentar = xKommentar;
	}

	public Timestamp getTKaufdatum() {
		return tKaufdatum;
	}

	public void setTKaufdatum(Timestamp tKaufdatum) {
		this.tKaufdatum = tKaufdatum;
	}

	public BigDecimal getNKaufpreis() {
		return nKaufpreis;
	}

	public void setNKaufpreis(BigDecimal nKaufpreis) {
		this.nKaufpreis = nKaufpreis;
	}

	public String getMandantCNrStandort() {
		return mandantCNrStandort;
	}

	public void setMandantCNrStandort(String mandantCNrStandort) {
		this.mandantCNrStandort = mandantCNrStandort;
	}

}
