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
package com.lp.server.artikel.ejb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "WW_TRUMPHTOPSLOG")
public class Trumphtopslog implements Serializable {
	@Id
	@Column(name = "I_ID")
	private Integer iId;

	@Column(name = "C_IMPORTFILENAME")
	private String cImportfilename;

	@Column(name = "C_ERROR")
	private String cError;

	@Column(name = "N_GEWICHT")
	private BigDecimal nGewicht;

	@Column(name = "N_GESTPREISNEU")
	private BigDecimal nGestpreisneu;

	@Column(name = "I_BEARBEITUNGSZEIT")
	private Integer iBearbeitungszeit;

	@Column(name = "T_ANLEGEN")
	private Timestamp tAnlegen;

	@Column(name = "ARTIKEL_I_ID")
	private Integer artikelIId;

	@Column(name = "ARTIKEL_I_ID_MATERIAL")
	private Integer artikelIIdMaterial;

	private static final long serialVersionUID = 1L;

	public Trumphtopslog() {
		super();
	}

	public Trumphtopslog(Integer id, String importfilename, String error,
			Integer artikelIId, Integer artikelIIdMaterial2,
			BigDecimal gewicht, BigDecimal gestpreisneu,
			Integer bearbeitungszeit) {

		setIId(id);
		setCImportfilename(importfilename);
		setCError(error);
		setArtikelIId(artikelIId);
		setArtikelIIdMaterial(artikelIIdMaterial2);
		setNGewicht(gewicht);
		setNGestpreisneu(gestpreisneu);
		setIBearbeitungszeit(bearbeitungszeit);
		setTAnlegen(new Timestamp(System.currentTimeMillis()));

	}

	public Integer getIId() {
		return this.iId;
	}

	public void setIId(Integer iId) {
		this.iId = iId;
	}

	public String getCImportfilename() {
		return this.cImportfilename;
	}

	public void setCImportfilename(String cImportfilename) {
		this.cImportfilename = cImportfilename;
	}

	public String getCError() {
		return this.cError;
	}

	public void setCError(String cError) {
		this.cError = cError;
	}

	public BigDecimal getNGewicht() {
		return this.nGewicht;
	}

	public void setNGewicht(BigDecimal nGewicht) {
		this.nGewicht = nGewicht;
	}

	public BigDecimal getNGestpreisneu() {
		return this.nGestpreisneu;
	}

	public void setNGestpreisneu(BigDecimal nGestpreisneu) {
		this.nGestpreisneu = nGestpreisneu;
	}

	public Integer getIBearbeitungszeit() {
		return this.iBearbeitungszeit;
	}

	public void setIBearbeitungszeit(Integer iBearbeitungszeit) {
		this.iBearbeitungszeit = iBearbeitungszeit;
	}

	public Timestamp getTAnlegen() {
		return this.tAnlegen;
	}

	public void setTAnlegen(Timestamp tAnlegen) {
		this.tAnlegen = tAnlegen;
	}

	public Integer getArtikelIId() {
		return this.artikelIId;
	}

	public void setArtikelIId(Integer artikelIId) {
		this.artikelIId = artikelIId;
	}

	public Integer getArtikelIIdMaterial() {
		return this.artikelIIdMaterial;
	}

	public void setArtikelIIdMaterial(Integer artikelIIdMaterial) {
		this.artikelIIdMaterial = artikelIIdMaterial;
	}

}
