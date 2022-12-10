
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
package com.lp.server.angebotstkl.ejb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
	@NamedQuery(name = "AgstklmengenstaffelSchnellerfassungFindByAgstklIIdNMenge", query = "SELECT OBJECT (o) FROM AgstklmengenstaffelSchnellerfassung o WHERE o.agstklIId = ?1 AND o.nMenge = ?2"),
	@NamedQuery(name = "AgstklmengenstaffelSchnellerfassungFindByAgstklIIdKleinerGleichNMenge", query = "SELECT OBJECT (o) FROM AgstklmengenstaffelSchnellerfassung o WHERE o.agstklIId = ?1 AND o.nMenge <= ?2 ORDER BY o.nMenge DESC"),
	@NamedQuery(name = "AgstklmengenstaffelSchnellerfassungFindByAgstklIId", query = "SELECT OBJECT (o) FROM AgstklmengenstaffelSchnellerfassung o WHERE o.agstklIId = ?1 ORDER BY o.nMenge ASC")
	 })
@Entity
@Table(name = "AS_AGSTKLMENGENSTAFFEL_SCHNELLERFASSUNG")
public class AgstklmengenstaffelSchnellerfassung implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "AGSTKL_I_ID")
	private Integer agstklIId;

	@Column(name = "N_MENGE")
	private BigDecimal nMenge;

	@Column(name = "N_AUFSCHLAG_AZ")
	private BigDecimal nAufschlagAz;

	
	@Column(name = "N_WERT_MATERIAL")
	private BigDecimal nWertMaterial;
	public BigDecimal getNWertMaterial() {
		return nWertMaterial;
	}

	public void setNWertMaterial(BigDecimal nWertMaterial) {
		this.nWertMaterial = nWertMaterial;
	}

	public BigDecimal getNWertAz() {
		return nWertAz;
	}

	public void setNWertAz(BigDecimal nWertAz) {
		this.nWertAz = nWertAz;
	}

	public BigDecimal getNPreisEinheit() {
		return nPreisEinheit;
	}

	public void setNPreisEinheit(BigDecimal nPreisEinheit) {
		this.nPreisEinheit = nPreisEinheit;
	}

	@Column(name = "N_WERT_AZ")
	private BigDecimal nWertAz;
	@Column(name = "N_PREIS_EINHEIT")
	private BigDecimal nPreisEinheit;
	
	
	
	public BigDecimal getNAufschlagAz() {
		return nAufschlagAz;
	}

	public void setNAufschlagAz(BigDecimal nAufschlagAz) {
		this.nAufschlagAz = nAufschlagAz;
	}

	public BigDecimal getNAufschlagMaterial() {
		return nAufschlagMaterial;
	}

	public void setNAufschlagMaterial(BigDecimal nAufschlagMaterial) {
		this.nAufschlagMaterial = nAufschlagMaterial;
	}

	@Column(name = "N_AUFSCHLAG_MATERIAL")
	private BigDecimal nAufschlagMaterial;

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	private static final long serialVersionUID = 1L;

	public AgstklmengenstaffelSchnellerfassung() {

	}

	public AgstklmengenstaffelSchnellerfassung(Integer iId, Integer agstklIId, BigDecimal nMenge,BigDecimal nAufschlagAz,BigDecimal nAufschlagMaterial, BigDecimal nWertMaterial,  BigDecimal nWertAz, BigDecimal nPreisEinheit) {
		setIId(iId);
		setAgstklIId(agstklIId);
		setNMenge(nMenge);
		setNAufschlagAz(nAufschlagAz);
		setNAufschlagMaterial(nAufschlagMaterial);
		setNWertMaterial(nWertMaterial);
		setNWertAz(nWertAz);
		setNPreisEinheit(nPreisEinheit);

	}

	public Integer getAgstklIId() {
		return agstklIId;
	}

	public void setAgstklIId(Integer agstklIId) {
		this.agstklIId = agstklIId;
	}

	public BigDecimal getNMenge() {
		return this.nMenge;
	}

	public void setNMenge(BigDecimal nMenge) {
		this.nMenge = nMenge;
	}

}
