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
package com.lp.server.personal.fastlanereader.generated;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class FLRZutrittsmodelltagdetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	/** identifier field */
	private Integer i_id;

	/** nullable persistent field */
	private Integer zutrittsmodelltag_i_id;

	/** nullable persistent field */
	private Date u_offenvon;

	/** nullable persistent field */
	private Date u_offenbis;

	/** nullable persistent field */
	private String zutrittsoeffnungsart_c_nr;

	/** nullable persistent field */
	private Short b_restdestages;

	/** nullable persistent field */
	private com.lp.server.personal.fastlanereader.generated.FLRZutrittsmodelltag flrzutrittsmodelltag;

	/** full constructor */
	public FLRZutrittsmodelltagdetail(
			Integer zutrittsmodelltag_i_id,
			Date u_offenvon,
			Date u_offenbis,
			String zutrittsoeffnungsart_c_nr,
			Short b_restdestages,
			com.lp.server.personal.fastlanereader.generated.FLRZutrittsmodelltag flrzutrittsmodelltag) {
		this.zutrittsmodelltag_i_id = zutrittsmodelltag_i_id;
		this.u_offenvon = u_offenvon;
		this.u_offenbis = u_offenbis;
		this.zutrittsoeffnungsart_c_nr = zutrittsoeffnungsart_c_nr;
		this.b_restdestages = b_restdestages;
		this.flrzutrittsmodelltag = flrzutrittsmodelltag;
	}

	/** default constructor */
	public FLRZutrittsmodelltagdetail() {
	}

	public Integer getI_id() {
		return this.i_id;
	}

	public void setI_id(Integer i_id) {
		this.i_id = i_id;
	}

	public Integer getZutrittsmodelltag_i_id() {
		return this.zutrittsmodelltag_i_id;
	}

	public void setZutrittsmodelltag_i_id(Integer zutrittsmodelltag_i_id) {
		this.zutrittsmodelltag_i_id = zutrittsmodelltag_i_id;
	}

	public Date getU_offenvon() {
		return this.u_offenvon;
	}

	public void setU_offenvon(Date u_offenvon) {
		this.u_offenvon = u_offenvon;
	}

	public Date getU_offenbis() {
		return this.u_offenbis;
	}

	public void setU_offenbis(Date u_offenbis) {
		this.u_offenbis = u_offenbis;
	}

	public String getZutrittsoeffnungsart_c_nr() {
		return this.zutrittsoeffnungsart_c_nr;
	}

	public void setZutrittsoeffnungsart_c_nr(String zutrittsoeffnungsart_c_nr) {
		this.zutrittsoeffnungsart_c_nr = zutrittsoeffnungsart_c_nr;
	}

	public Short getB_restdestages() {
		return this.b_restdestages;
	}

	public void setB_restdestages(Short b_restdestages) {
		this.b_restdestages = b_restdestages;
	}

	public com.lp.server.personal.fastlanereader.generated.FLRZutrittsmodelltag getFlrzutrittsmodelltag() {
		return this.flrzutrittsmodelltag;
	}

	public void setFlrzutrittsmodelltag(
			com.lp.server.personal.fastlanereader.generated.FLRZutrittsmodelltag flrzutrittsmodelltag) {
		this.flrzutrittsmodelltag = flrzutrittsmodelltag;
	}

	public String toString() {
		return new ToStringBuilder(this).append("i_id", getI_id()).toString();
	}

}
