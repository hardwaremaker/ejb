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
package com.lp.util;

/**
 * 
 * @author Gerold
 */

public class EJBLineNumberExceptionLP extends EJBExceptionLP {
	private static final long serialVersionUID = 1L;

	private Integer linenumber ;
	private Integer severity ;
	private String  filename;
	
	public static final Integer SEVERITY_NONE    = 0 ;
	public static final Integer SEVERITY_DEBUG   = 1 ;
	public static final Integer SEVERITY_INFO    = 2 ;
	public static final Integer SEVERITY_WARNING = 3 ;
	public static final Integer SEVERITY_ERROR   = 4 ;

	public EJBLineNumberExceptionLP(Integer linenumber, Integer severity, EJBExceptionLP e) {
		super(e) ;
		setLinenumber(linenumber) ;
		setSeverity(severity) ;
	}

	public EJBLineNumberExceptionLP(String filename, Integer linenumber, Integer severity, EJBExceptionLP e) {
		super(e) ;
		setFilename(filename);
		setLinenumber(linenumber) ;
		setSeverity(severity) ;
	}

	public EJBLineNumberExceptionLP(Integer linenumber, EJBExceptionLP e) {
		super(e) ;
		setLinenumber(linenumber) ;
	}

	public EJBLineNumberExceptionLP(String filename, Integer linenumber, EJBExceptionLP e) {
		super(e) ;
		setFilename(filename);
		setLinenumber(linenumber) ;
	}
	
	public Integer getLinenumber() {
		return linenumber ;
	}
	
	public void setLinenumber(Integer linenumber) {
		this.linenumber = linenumber ;
	}	
	
	public void setSeverity(Integer severity) {
		this.severity = severity ;
	}
	
	public Integer getSeverity() {
		return severity ;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	public boolean hasFilename() {
		return filename != null;
	}
}
