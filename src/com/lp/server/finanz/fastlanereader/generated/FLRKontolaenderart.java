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
package com.lp.server.finanz.fastlanereader.generated;

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.lp.server.system.fastlanereader.generated.FLRMandant;

/** @author Hibernate CodeGenerator */
public class FLRKontolaenderart implements Serializable {
	private static final long serialVersionUID = 1L;

	/** identifier field */
//	private FLRKontolaenderartPK id_comp;

	/** nullable persistent field */
	private com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto flrkonto;

	/** nullable persistent field */
	private com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto flrkonto_uebersetzt;

	private FLRReversechargeart flrreversechargeart ;
	
	private FLRMandant flrmandant;
	
	private FLRFinanzLaenderart flrlaenderart;
	
//	private FLRFinanzFinanzamt flrfinanzamt;
	
	private Integer finanzamt_i_id;
	
    private Timestamp gueltigAb;
    
    private Integer i_id;


	/** full constructor */
 /*   
	public FLRKontolaenderart(
			FLRKontolaenderartPK id_comp,
			FLRFinanzKonto flrkonto,
			FLRFinanzKonto flrkonto_uebersetzt,
			FLRReversechargeart flrreversechargeart) {
		this.id_comp = id_comp;
		this.flrkonto = flrkonto;
		this.flrkonto_uebersetzt = flrkonto_uebersetzt;
		this.setFlrreversechargeart(flrreversechargeart) ;
	}
*/
    
    public FLRKontolaenderart(
    		Integer i_id, FLRFinanzKonto flrkonto, FLRFinanzKonto flrkonto_uebersetzt,
    		FLRReversechargeart flrreversechargeart, FLRFinanzLaenderart flrlaenderart,
    		FLRMandant flrmandant, Integer finanzamt_i_id, Timestamp gueltigAb) {
    	this.i_id = i_id;
    	this.flrkonto = flrkonto;
    	this.flrkonto_uebersetzt = flrkonto_uebersetzt;
    	this.flrreversechargeart = flrreversechargeart;
    	this.flrmandant = flrmandant;
    	this.finanzamt_i_id = finanzamt_i_id;
    	this.flrlaenderart = flrlaenderart;
    }
    
	/** default constructor */
	public FLRKontolaenderart() {
	}

	/** minimal constructor */
/*	
	public FLRKontolaenderart(FLRKontolaenderartPK id_comp) {
		this.id_comp = id_comp;
	}

	public FLRKontolaenderartPK getId_comp() {
		return this.id_comp;
	}

	public void setId_comp(FLRKontolaenderartPK id_comp) {
		this.id_comp = id_comp;
	}
*/
	public FLRKontolaenderart(Integer iid) {
		this.i_id = iid;
	}
	
	public com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto getFlrkonto() {
		return this.flrkonto;
	}

	public void setFlrkonto(
			com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto flrkonto) {
		this.flrkonto = flrkonto;
	}

	public com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto getFlrkonto_uebersetzt() {
		return this.flrkonto_uebersetzt;
	}

	public void setFlrkonto_uebersetzt(
			com.lp.server.finanz.fastlanereader.generated.FLRFinanzKonto flrkonto_uebersetzt) {
		this.flrkonto_uebersetzt = flrkonto_uebersetzt;
	}

	public void setFinanzamt_i_id(Integer finanzamt_i_id) {
		this.finanzamt_i_id = finanzamt_i_id;
	}
	
	public Integer getFinanzamt_i_id() {
		return this.finanzamt_i_id;
	}
	
	public void setFlrmandant(FLRMandant flrmandant) {
		this.flrmandant = flrmandant;
	}
	
	public FLRMandant getFlrmandant() {
		return this.flrmandant;
	}
	
	public void setFlrlaenderart(FLRFinanzLaenderart flrlaenderart) {
		this.flrlaenderart = flrlaenderart;
	}
	
	public FLRFinanzLaenderart getFlrlaenderart() {
		return this.flrlaenderart;
	}
	
	public String toString() {
/*		
		return new ToStringBuilder(this).append("id_comp", getId_comp())
				.toString();
*/
		return new ToStringBuilder(this).append("i_id", getI_id()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof FLRKontolaenderart))
			return false;
		FLRKontolaenderart castOther = (FLRKontolaenderart) other;
		return new EqualsBuilder().append(this.getI_id(), castOther.getI_id()).isEquals();
/*		
		return new EqualsBuilder().append(this.getId_comp(),
				castOther.getId_comp()).isEquals();
*/				
	}

	public int hashCode() {
/*
 * 		return new HashCodeBuilder().append(getId_comp()).toHashCode();
 */
		return new HashCodeBuilder().append(getI_id()).toHashCode();
	}

	public FLRReversechargeart getFlrreversechargeart() {
		return flrreversechargeart;
	}

	public void setFlrreversechargeart(FLRReversechargeart flrreversechargeart) {
		this.flrreversechargeart = flrreversechargeart;
	}
	
	public Timestamp getGueltigAb() {
		return gueltigAb;
	}

	public void setGueltigAb(Timestamp gueltigAb) {
		this.gueltigAb = gueltigAb;
	}

	public Integer getI_id() {
		return i_id;
	}

	public void setI_id(Integer i_id) {
		this.i_id = i_id;
	}
}
