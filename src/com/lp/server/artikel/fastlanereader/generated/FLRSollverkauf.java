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
package com.lp.server.artikel.fastlanereader.generated;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class FLRSollverkauf implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Integer artikel_i_id;

	/** nullable persistent field */
	private Double f_aufschlag;

	/** nullable persistent field */
	private Double f_sollverkauf;

	/** full constructor */
	public FLRSollverkauf(Double f_aufschlag, Double f_sollverkauf) {
		this.f_aufschlag = f_aufschlag;
		this.f_sollverkauf = f_sollverkauf;
	}

	/** default constructor */
	public FLRSollverkauf() {
	}

	public Integer getArtikel_i_id() {
		return this.artikel_i_id;
	}

	public void setArtikel_i_id(Integer artikel_i_id) {
		this.artikel_i_id = artikel_i_id;
	}

	public Double getF_aufschlag() {
		return this.f_aufschlag;
	}

	public void setF_aufschlag(Double f_aufschlag) {
		this.f_aufschlag = f_aufschlag;
	}

	public Double getF_sollverkauf() {
		return this.f_sollverkauf;
	}

	public void setF_sollverkauf(Double f_sollverkauf) {
		this.f_sollverkauf = f_sollverkauf;
	}

	public String toString() {
		return new ToStringBuilder(this).append("artikel_i_id",
				getArtikel_i_id()).toString();
	}

}
