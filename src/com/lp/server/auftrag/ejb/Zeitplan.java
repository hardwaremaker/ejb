
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
package com.lp.server.auftrag.ejb;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "AUFT_ZEITPLAN")
public class Zeitplan implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;
	
	@Column(name = "AUFTRAG_I_ID")
	private Integer auftragIId;
	
	@Column(name = "I_TERMIN_VOR_LIEFERTERMIN")
	private Integer iTerminVorLiefertermin;

	@Column(name = "N_MATERIAL")
	private BigDecimal nMaterial;

	@Column(name = "N_MATERIAL_URSPRUNG")
	private BigDecimal nMaterialUrsprung;

	@Column(name = "N_DAUER")
	private BigDecimal nDauer;

	@Column(name = "N_DAUER_URSPRUNG")
	private BigDecimal nDauerUrsprung;
	
	@Column(name = "C_KOMMENTAR")
	private String cKommentar;
	
	@Column(name = "X_TEXT")
	private String xText;
	

	

	private static final long serialVersionUID = 1L;

	public Zeitplan() {
		super();
	}

	public Zeitplan(Integer id, Integer auftragIId, Integer  iTerminVorLiefertermin) {
		setIId(id);
		setAuftragIId(auftragIId);
		setITerminVorLiefertermin(iTerminVorLiefertermin);
	}

	public Integer getAuftragIId() {
		return auftragIId;
	}

	public void setAuftragIId(Integer auftragIId) {
		this.auftragIId = auftragIId;
	}

	public Integer getITerminVorLiefertermin() {
		return iTerminVorLiefertermin;
	}

	public void setITerminVorLiefertermin(Integer iTerminVorLiefertermin) {
		this.iTerminVorLiefertermin = iTerminVorLiefertermin;
	}

	public BigDecimal getNMaterial() {
		return nMaterial;
	}

	public void setNMaterial(BigDecimal nMaterial) {
		this.nMaterial = nMaterial;
	}

	public BigDecimal getNMaterialUrsprung() {
		return nMaterialUrsprung;
	}

	public void setNMaterialUrsprung(BigDecimal nMaterialUrsprung) {
		this.nMaterialUrsprung = nMaterialUrsprung;
	}

	public BigDecimal getNDauer() {
		return nDauer;
	}

	public void setNDauer(BigDecimal nDauer) {
		this.nDauer = nDauer;
	}

	public BigDecimal getNDauerUrsprung() {
		return nDauerUrsprung;
	}

	public void setNDauerUrsprung(BigDecimal nDauerUrsprung) {
		this.nDauerUrsprung = nDauerUrsprung;
	}

	public String getCKommentar() {
		return cKommentar;
	}

	public void setCKommentar(String cKommentar) {
		this.cKommentar = cKommentar;
	}

	public String getXText() {
		return xText;
	}

	public void setXText(String xText) {
		this.xText = xText;
	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}




}
