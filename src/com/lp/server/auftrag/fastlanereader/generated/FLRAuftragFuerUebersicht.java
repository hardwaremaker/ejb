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
package com.lp.server.auftrag.fastlanereader.generated;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class FLRAuftragFuerUebersicht implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Integer i_id;

	/** nullable persistent field */
	private String mandant_c_nr;

	/** nullable persistent field */
	private String c_nr;

	/** nullable persistent field */
	private String auftragart_c_nr;

	/** nullable persistent field */
	private Date t_liefertermin;

	/** nullable persistent field */
	private Date t_finaltermin;

	/** nullable persistent field */
	private Date t_belegdatum;

	/** nullable persistent field */
	private String auftragstatus_c_nr;

	/** nullable persistent field */
	private String waehrung_c_nr_auftragswaehrung;

	/** nullable persistent field */
	private Double f_wechselkursmandantwaehrungzuauftragswaehrung;

	/** nullable persistent field */
	private BigDecimal n_gesamtauftragswertinauftragswaehrung;

	/** full constructor */
	public FLRAuftragFuerUebersicht(String mandant_c_nr, String c_nr,
			String auftragart_c_nr, Date t_liefertermin, Date t_finaltermin,
			Date t_belegdatum, String auftragstatus_c_nr,
			String waehrung_c_nr_auftragswaehrung,
			Double f_wechselkursmandantwaehrungzuauftragswaehrung,
			BigDecimal n_gesamtauftragswertinauftragswaehrung) {
		this.mandant_c_nr = mandant_c_nr;
		this.c_nr = c_nr;
		this.auftragart_c_nr = auftragart_c_nr;
		this.t_liefertermin = t_liefertermin;
		this.t_finaltermin = t_finaltermin;
		this.t_belegdatum = t_belegdatum;
		this.auftragstatus_c_nr = auftragstatus_c_nr;
		this.waehrung_c_nr_auftragswaehrung = waehrung_c_nr_auftragswaehrung;
		this.f_wechselkursmandantwaehrungzuauftragswaehrung = f_wechselkursmandantwaehrungzuauftragswaehrung;
		this.n_gesamtauftragswertinauftragswaehrung = n_gesamtauftragswertinauftragswaehrung;
	}

	/** default constructor */
	public FLRAuftragFuerUebersicht() {
	}

	public Integer getI_id() {
		return this.i_id;
	}

	public void setI_id(Integer i_id) {
		this.i_id = i_id;
	}

	public String getMandant_c_nr() {
		return this.mandant_c_nr;
	}

	public void setMandant_c_nr(String mandant_c_nr) {
		this.mandant_c_nr = mandant_c_nr;
	}

	public String getC_nr() {
		return this.c_nr;
	}

	public void setC_nr(String c_nr) {
		this.c_nr = c_nr;
	}

	public String getAuftragart_c_nr() {
		return this.auftragart_c_nr;
	}

	public void setAuftragart_c_nr(String auftragart_c_nr) {
		this.auftragart_c_nr = auftragart_c_nr;
	}

	public Date getT_liefertermin() {
		return this.t_liefertermin;
	}

	public void setT_liefertermin(Date t_liefertermin) {
		this.t_liefertermin = t_liefertermin;
	}

	public Date getT_finaltermin() {
		return this.t_finaltermin;
	}

	public void setT_finaltermin(Date t_finaltermin) {
		this.t_finaltermin = t_finaltermin;
	}

	public Date getT_belegdatum() {
		return this.t_belegdatum;
	}

	public void setT_belegdatum(Date t_belegdatum) {
		this.t_belegdatum = t_belegdatum;
	}

	public String getAuftragstatus_c_nr() {
		return this.auftragstatus_c_nr;
	}

	public void setAuftragstatus_c_nr(String auftragstatus_c_nr) {
		this.auftragstatus_c_nr = auftragstatus_c_nr;
	}

	public String getWaehrung_c_nr_auftragswaehrung() {
		return this.waehrung_c_nr_auftragswaehrung;
	}

	public void setWaehrung_c_nr_auftragswaehrung(
			String waehrung_c_nr_auftragswaehrung) {
		this.waehrung_c_nr_auftragswaehrung = waehrung_c_nr_auftragswaehrung;
	}

	public Double getF_wechselkursmandantwaehrungzuauftragswaehrung() {
		return this.f_wechselkursmandantwaehrungzuauftragswaehrung;
	}

	public void setF_wechselkursmandantwaehrungzuauftragswaehrung(
			Double f_wechselkursmandantwaehrungzuauftragswaehrung) {
		this.f_wechselkursmandantwaehrungzuauftragswaehrung = f_wechselkursmandantwaehrungzuauftragswaehrung;
	}

	public BigDecimal getN_gesamtauftragswertinauftragswaehrung() {
		return this.n_gesamtauftragswertinauftragswaehrung;
	}

	public void setN_gesamtauftragswertinauftragswaehrung(
			BigDecimal n_gesamtauftragswertinauftragswaehrung) {
		this.n_gesamtauftragswertinauftragswaehrung = n_gesamtauftragswertinauftragswaehrung;
	}

	public String toString() {
		return new ToStringBuilder(this).append("i_id", getI_id()).toString();
	}

}
