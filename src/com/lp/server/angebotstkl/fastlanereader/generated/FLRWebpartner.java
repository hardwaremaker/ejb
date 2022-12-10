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
package com.lp.server.angebotstkl.fastlanereader.generated;

import java.io.Serializable;

import com.lp.server.partner.fastlanereader.generated.FLRLieferant;

public class FLRWebpartner implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer i_id;
	
	private Integer webabfrage_i_id;
	
	private Integer lieferant_i_id;
	
	private FLRLieferant flrlieferant;
	
	public FLRWebpartner() {
	}
	
	public FLRWebpartner(Integer webabfrage_i_id, Integer lieferant_i_id, FLRLieferant flrlieferant) {
		this.webabfrage_i_id = webabfrage_i_id;
		this.lieferant_i_id = lieferant_i_id;
		this.flrlieferant = flrlieferant;
	}

	public Integer getI_id() {
		return i_id;
	}

	public void setI_id(Integer i_id) {
		this.i_id = i_id;
	}

	public Integer getWebabfrage_i_id() {
		return webabfrage_i_id;
	}

	public void setWebabfrage_i_id(Integer webabfrage_i_id) {
		this.webabfrage_i_id = webabfrage_i_id;
	}

	public Integer getLieferant_i_id() {
		return lieferant_i_id;
	}

	public void setLieferant_i_id(Integer lieferant_i_id) {
		this.lieferant_i_id = lieferant_i_id;
	}

	public FLRLieferant getFlrlieferant() {
		return flrlieferant;
	}

	public void setFlrlieferant(FLRLieferant flrlieferant) {
		this.flrlieferant = flrlieferant;
	}

}
