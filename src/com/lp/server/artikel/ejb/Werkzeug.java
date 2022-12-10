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
package com.lp.server.artikel.ejb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({ @NamedQuery(name = "WerkzeugfindByCNr", query = "SELECT OBJECT(o) FROM Werkzeug o WHERE o.cNr=?1") })
@Entity
@Table(name = "WW_WERKZEUG")
public class Werkzeug implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_NR")
	private String cNr;

	@Column(name = "LIEFERANT_I_ID")
	private Integer lieferantIId;

	@Column(name = "LAGERPLATZ_I_ID")
	private Integer lagerplatzIId;

	@Column(name = "X_KOMMENTAR")
	private String xKommentar;

	@Column(name = "T_KAUFDATUM")
	private Timestamp tKaufdatum;

	@Column(name = "N_KAUFPREIS")
	private BigDecimal nKaufpreis;

	@Column(name = "MANDANT_C_NR_STANDORT")
	private String mandantCNrStandort;

	@Column(name = "C_BEZ")
	private String cBez;

	private static final long serialVersionUID = 1L;

	public Werkzeug() {
		super();
	}

	public Werkzeug(Integer id, String cNr, String mandantCNrStandort) {
		setIId(id);
		setCNr(cNr);
		setMandantCNrStandort(mandantCNrStandort);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
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
