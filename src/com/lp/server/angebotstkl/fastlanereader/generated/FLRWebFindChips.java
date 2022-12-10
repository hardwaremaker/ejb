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

public class FLRWebFindChips implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer webpartner_i_id;
	
	private String c_distributor;
	
	private String c_name;
	
	private FLRWebpartner flrwebpartner;

	public FLRWebFindChips() {
	}

	public FLRWebFindChips(Integer webpartner_i_id, String c_distributor, String c_name, FLRWebpartner flrwebpartner) {
		this.webpartner_i_id = webpartner_i_id;
		this.c_distributor = c_distributor;
		this.c_name = c_name;
		this.flrwebpartner = flrwebpartner;
	}

	public Integer getWebpartner_i_id() {
		return webpartner_i_id;
	}

	public void setWebpartner_i_id(Integer webpartner_i_id) {
		this.webpartner_i_id = webpartner_i_id;
	}

	public String getC_distributor() {
		return c_distributor;
	}

	public void setC_distributor(String c_distributor) {
		this.c_distributor = c_distributor;
	}

	public String getC_name() {
		return c_name;
	}

	public void setC_name(String c_name) {
		this.c_name = c_name;
	}

	public FLRWebpartner getFlrwebpartner() {
		return flrwebpartner;
	}

	public void setFlrwebpartner(FLRWebpartner flrwebpartner) {
		this.flrwebpartner = flrwebpartner;
	}

}
