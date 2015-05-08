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
package com.lp.server.fertigung.ejb;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries( {
	@NamedQuery(name = "LosgutschlechtFindByLossollarbeitsplanIId", query = "SELECT OBJECT(o) FROM Losgutschlecht o WHERE o.lossollarbeitsplanIId = ?1"),
	@NamedQuery(name = "LosgutschlechtFindByZeitdatenIId", query = "SELECT OBJECT(o) FROM Losgutschlecht o WHERE o.zeitdatenIId = ?1"),
	@NamedQuery(name = "LosgutschlechtFindByMaschinenzeitdatenIId", query = "SELECT OBJECT(o) FROM Losgutschlecht o WHERE o.maschinenzeitdatenIId = ?1")})
	

@Entity
@Table(name = "FERT_LOSGUTSCHLECHT")
public class Losgutschlecht implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "ZEITDATEN_I_ID")
	private Integer zeitdatenIId;

	@Column(name = "MASCHINENZEITDATEN_I_ID")
	private Integer maschinenzeitdatenIId;
	

	public Integer getMaschinenzeitdatenIId() {
		return maschinenzeitdatenIId;
	}

	public void setMaschinenzeitdatenIId(Integer maschinenzeitdatenIId) {
		this.maschinenzeitdatenIId = maschinenzeitdatenIId;
	}

	@Column(name = "LOSSOLLARBEITSPLAN_I_ID")
	private Integer lossollarbeitsplanIId;
	@Column(name = "FEHLER_I_ID")
	private Integer fehlerIId;
	@Column(name = "C_KOMMENTAR")
	private String cKommentar;


	public Integer getFehlerIId() {
		return fehlerIId;
	}

	public void setFehlerIId(Integer fehlerIId) {
		this.fehlerIId = fehlerIId;
	}

	public String getCKommentar() {
		return cKommentar;
	}

	public void setCKommentar(String cKommentar) {
		this.cKommentar = cKommentar;
	}

	@Column(name = "N_GUT")
	private BigDecimal nGut;
	@Column(name = "N_SCHLECHT")
	private BigDecimal nSchlecht;
	@Column(name = "N_INARBEIT")
	private BigDecimal nInarbeit;

	public Integer getZeitdatenIId() {
		return zeitdatenIId;
	}

	public void setZeitdatenIId(Integer zeitdatenIId) {
		this.zeitdatenIId = zeitdatenIId;
	}



	public Integer getLossollarbeitsplanIId() {
		return lossollarbeitsplanIId;
	}

	public void setLossollarbeitsplanIId(Integer lossollarbeitsplanIId) {
		this.lossollarbeitsplanIId = lossollarbeitsplanIId;
	}

	public BigDecimal getNGut() {
		return nGut;
	}

	public void setNGut(BigDecimal gut) {
		nGut = gut;
	}

	public BigDecimal getNSchlecht() {
		return nSchlecht;
	}

	public void setNSchlecht(BigDecimal schlecht) {
		nSchlecht = schlecht;
	}

	public BigDecimal getNInarbeit() {
		return nInarbeit;
	}

	public void setNInarbeit(BigDecimal inarbeit) {
		nInarbeit = inarbeit;
	}

	private static final long serialVersionUID = 1L;

	public Losgutschlecht() {
		super();
	}

	public Losgutschlecht(Integer id,

	Integer lossollarbeitsplanIId, BigDecimal nGut,
			BigDecimal nSchlecht, BigDecimal nInarbeit) {
		setIId(id);
		setLossollarbeitsplanIId(lossollarbeitsplanIId);
		setZeitdatenIId(zeitdatenIId);
		setNGut(nGut);
		setNSchlecht(nSchlecht);
		setNInarbeit(nInarbeit);
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

}
