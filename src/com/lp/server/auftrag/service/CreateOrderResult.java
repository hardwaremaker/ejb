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
package com.lp.server.auftrag.service;

import javax.xml.bind.annotation.XmlRootElement;

import com.lp.server.artikel.service.BaseRequestResult;

@XmlRootElement(name = "CreateOrderResult")
public class CreateOrderResult extends BaseRequestResult {

	public static final int ERROR_EMPTY_ORDER   = 1002 ;
	public static final int ERROR_UNMARSHALLING = 1003 ;
	public static final int ERROR_CUSTOMER_NOT_FOUND = 1004 ;
	public static final int ERROR_ORDER_NOT_CREATED  = 1005 ;
	
	public static final int ERROR_JAXB_EXCEPTION     = 1006 ;
	public static final int ERROR_SAX_EXCEPTION      = 1007 ;
	
	private String orderCnr ;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5501693516653397148L;

	public CreateOrderResult() {
		super(BaseRequestResult.ERROR_NOT_INITIALIZED, "") ;
	}
	
	public CreateOrderResult(int status) {
		super(status, "") ;
	}
	
	public CreateOrderResult(int status, String description) {
		super(status, description) ;
	}
	
	public String getOrderNumber() {
		return orderCnr ;
	}
	
	public void setOrderNumber(String orderNumber) {
		orderCnr = orderNumber ;
	}
}
