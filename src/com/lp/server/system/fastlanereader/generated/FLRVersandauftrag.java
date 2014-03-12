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
package com.lp.server.system.fastlanereader.generated;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.lp.server.partner.fastlanereader.generated.FLRPartner;

/** @author Hibernate CodeGenerator */
public class FLRVersandauftrag implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	/** identifier field */
	private Integer i_id;

	/** nullable persistent field */
	private String c_empfaenger;

	/** nullable persistent field */
	private String c_betreff;

	/** nullable persistent field */
	private String c_absenderadresse;

	/** nullable persistent field */
	private Date t_sendezeitpunktwunsch;

	/** nullable persistent field */
	private String belegart_c_nr;

	/** nullable persistent field */
	private Integer i_belegiid;

	/** nullable persistent field */
	private Integer personal_i_id;

	/** nullable persistent field */
	private String status_c_nr;
	
	private Timestamp t_sendezeitpunkt;

	/** nullable persistent field */
	private FLRPartner flrempfaenger;

	/** nullable persistent field */
	private FLRPartner flrsender;

	/** full constructor */
	public FLRVersandauftrag(String c_empfaenger, String c_betreff,
			String c_absenderadresse, Date t_sendezeitpunktwunsch,
			String belegart_c_nr, Integer i_belegiid, Integer personal_i_id,
			String status_c_nr, Timestamp t_sendezeitpunkt, FLRPartner flrempfaenger, FLRPartner flrsender) {
		this.c_empfaenger = c_empfaenger;
		this.c_betreff = c_betreff;
		this.c_absenderadresse = c_absenderadresse;
		this.t_sendezeitpunktwunsch = t_sendezeitpunktwunsch;
		this.belegart_c_nr = belegart_c_nr;
		this.i_belegiid = i_belegiid;
		this.personal_i_id = personal_i_id;
		this.status_c_nr = status_c_nr;
		this.t_sendezeitpunkt = t_sendezeitpunkt;
		this.flrempfaenger = flrempfaenger;
		this.flrsender = flrsender;
	}

	/** default constructor */
	public FLRVersandauftrag() {
	}

	public Integer getI_id() {
		return this.i_id;
	}

	public void setI_id(Integer i_id) {
		this.i_id = i_id;
	}

	public String getC_empfaenger() {
		return this.c_empfaenger;
	}

	public void setC_empfaenger(String c_empfaenger) {
		this.c_empfaenger = c_empfaenger;
	}

	public String getC_betreff() {
		return this.c_betreff;
	}

	public void setC_betreff(String c_betreff) {
		this.c_betreff = c_betreff;
	}

	public String getC_absenderadresse() {
		return this.c_absenderadresse;
	}

	public void setC_absenderadresse(String c_absenderadresse) {
		this.c_absenderadresse = c_absenderadresse;
	}

	public Date getT_sendezeitpunktwunsch() {
		return this.t_sendezeitpunktwunsch;
	}

	public void setT_sendezeitpunktwunsch(Date t_sendezeitpunktwunsch) {
		this.t_sendezeitpunktwunsch = t_sendezeitpunktwunsch;
	}

	public String getBelegart_c_nr() {
		return this.belegart_c_nr;
	}

	public void setBelegart_c_nr(String belegart_c_nr) {
		this.belegart_c_nr = belegart_c_nr;
	}

	public Integer getI_belegiid() {
		return this.i_belegiid;
	}

	public void setI_belegiid(Integer i_belegiid) {
		this.i_belegiid = i_belegiid;
	}

	public Integer getPersonal_i_id() {
		return this.personal_i_id;
	}

	public void setPersonal_i_id(Integer personal_i_id) {
		this.personal_i_id = personal_i_id;
	}

	public String getStatus_c_nr() {
		return this.status_c_nr;
	}

	public void setStatus_c_nr(String status_c_nr) {
		this.status_c_nr = status_c_nr;
	}
	
	public Timestamp getT_sendezeitpunkt() {
		return t_sendezeitpunkt;
	}

	public void setT_sendezeitpunkt(Timestamp t_sendezeitpunkt) {
		this.t_sendezeitpunkt = t_sendezeitpunkt;
	}

	public FLRPartner getFlrempfaenger() {
		return this.flrempfaenger;
	}

	public void setFlrempfaenger(FLRPartner flrempfaenger) {
		this.flrempfaenger = flrempfaenger;
	}

	public FLRPartner getFlrsender() {
		return this.flrsender;
	}

	public void setFlrsender(FLRPartner flrsender) {
		this.flrsender = flrsender;
	}

	public String toString() {
		return new ToStringBuilder(this).append("i_id", getI_id()).toString();
	}

}
