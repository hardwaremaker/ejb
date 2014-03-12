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

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class FLRPaneldaten implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	/** identifier field */
	private Integer i_id;

	/** nullable persistent field */
	private String c_key;

	/** nullable persistent field */
	private String x_inhalt;

	/** nullable persistent field */
	private String panel_c_nr;

	/** nullable persistent field */
	private com.lp.server.system.fastlanereader.generated.FLRPanelbeschreibung flrpanelbeschreibung;

	/** full constructor */
	public FLRPaneldaten(
			String c_key,
			String x_inhalt,
			String panel_c_nr,
			com.lp.server.system.fastlanereader.generated.FLRPanelbeschreibung flrpanelbeschreibung) {
		this.c_key = c_key;
		this.x_inhalt = x_inhalt;
		this.panel_c_nr = panel_c_nr;
		this.flrpanelbeschreibung = flrpanelbeschreibung;
	}

	/** default constructor */
	public FLRPaneldaten() {
	}

	public Integer getI_id() {
		return this.i_id;
	}

	public void setI_id(Integer i_id) {
		this.i_id = i_id;
	}

	public String getC_key() {
		return this.c_key;
	}

	public void setC_key(String c_key) {
		this.c_key = c_key;
	}

	public String getX_inhalt() {
		return this.x_inhalt;
	}

	public void setX_inhalt(String x_inhalt) {
		this.x_inhalt = x_inhalt;
	}

	public String getPanel_c_nr() {
		return this.panel_c_nr;
	}

	public void setPanel_c_nr(String panel_c_nr) {
		this.panel_c_nr = panel_c_nr;
	}

	public com.lp.server.system.fastlanereader.generated.FLRPanelbeschreibung getFlrpanelbeschreibung() {
		return this.flrpanelbeschreibung;
	}

	public void setFlrpanelbeschreibung(
			com.lp.server.system.fastlanereader.generated.FLRPanelbeschreibung flrpanelbeschreibung) {
		this.flrpanelbeschreibung = flrpanelbeschreibung;
	}

	public String toString() {
		return new ToStringBuilder(this).append("i_id", getI_id()).toString();
	}

}
