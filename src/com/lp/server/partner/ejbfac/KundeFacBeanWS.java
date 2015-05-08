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
package com.lp.server.partner.ejbfac;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.MTOM;

import org.jboss.wsf.spi.annotation.WebContext;

import com.lp.server.system.service.WebshopAuthHeader;
import com.lp.server.util.Facade;
import com.lp.server.util.logger.webservice.WebserviceCallInterceptor;

@Stateless
@MTOM
@WebService(name="IWebshop2CustomerServices", serviceName="HeliumVCustomerService")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@BindingType(value = "http://schemas.xmlsoap.org/wsdl/soap/http?mtom=true")
@WebContext (urlPattern="/version1/KundeFacBeanRest")  
@Interceptors(WebserviceCallInterceptor.class)
public class KundeFacBeanWS extends Facade implements WebshopCustomerServiceInterface {
	private WebshopCustomerServiceFacLocal delegate ;

	@Resource
	WebServiceContext context ;

	public KundeFacBeanWS() {
		delegate = getWebshopCustomerServiceFac() ;
	}

	@Override
	@WebMethod
	public WebshopCustomersResult getCustomers(
			@WebParam WebshopAuthHeader header) {
		return delegate.getCustomers(header);
	}

	@Override
	@WebMethod
	public WebshopCustomerResult getCustomerById(
			@WebParam WebshopAuthHeader header,
			@WebParam(name="customerId") Integer id) {
		return delegate.getCustomerById(header, id) ;
	}

	@Override
	@WebMethod
	public WebshopCustomersResult getCustomersChanged(
			@WebParam WebshopAuthHeader header,
			@WebParam(name="changedDateTime") String changedDateTime) {
		return delegate.getCustomersChanged(header, changedDateTime);
	}

	@Override
	@WebMethod
	public WebshopPricelistResult getPricelistById(
			@WebParam WebshopAuthHeader header,
			@WebParam(name="pricelistId") Integer id) {
		return delegate.getPricelistById(header, id);
	}

	@Override
	@WebMethod
	public WebshopPricelistsResult getPricelists(
			@WebParam WebshopAuthHeader header) {
		return delegate.getPricelists(header) ;
	}
	
	
}
