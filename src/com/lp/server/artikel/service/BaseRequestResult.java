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
package com.lp.server.artikel.service;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "BaseRequestResult")
public class BaseRequestResult implements Serializable {
	private static final long serialVersionUID = -7761745606748170893L;

	private int rc ;
	private String description ;
	
	public static final int OKAY = 0 ;
	public static final int ERROR_NOT_INITIALIZED = 1 ;
	public static final int ERROR_NULL_PARAMETER = 2 ;
	public static final int ERROR_RMI_EXCEPTION = 10 ;

	public static final int ERROR_AUTHENTIFICATION  = 100 ;
	
	public static final int ERROR_NOT_FOUND = 1001 ;

	public static final int ERROR_EJB_EXCEPTION = 10000 ;

	public BaseRequestResult() {
		this(ERROR_NOT_INITIALIZED, "") ;
	}
	
	public BaseRequestResult(int rc, String description) {
		setRc(rc) ;
		setDescription(description) ;
	}
	
	public int getRc() {
		return rc;
	}
	public void setRc(int rc) {
		this.rc = rc;
	}
	
	public void setOkay() {
		setRc(OKAY) ;
		setDescription("") ;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description == null ? "" : description ;
	}	
}
