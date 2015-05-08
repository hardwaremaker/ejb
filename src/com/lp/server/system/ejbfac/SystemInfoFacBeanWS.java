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
package com.lp.server.system.ejbfac;

import java.text.MessageFormat;

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
import javax.xml.ws.soap.MTOM;

import org.jboss.wsf.spi.annotation.WebContext;

import com.lp.server.util.Facade;
import com.lp.server.util.ServerConfiguration;
import com.lp.server.util.logger.webservice.WebserviceCallInterceptor;

@Stateless
@MTOM
@WebService(name="IWebshop2SystemInfoServices", serviceName="HeliumVSystemInfoService")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@BindingType(value = "http://schemas.xmlsoap.org/wsdl/soap/http?mtom=true")
@WebContext (urlPattern="/version1/SystemInfoFacBeanRest")  
@Interceptors(WebserviceCallInterceptor.class)
public class SystemInfoFacBeanWS extends Facade implements WebshopSystemInfoServiceInterface {
	@WebMethod
	@Override
	public WebshopPingResult ping() {
		WebshopPingResult result = new WebshopPingResult() ;

		result.setSystemBuildNumber(ServerConfiguration.getBuildNumber().toString()) ;
		result.setTimestamp(System.currentTimeMillis()) ;
		result.setOkay() ;
 		return result ;
	}

	@WebMethod
	@Override
	public WebshopErrorResult error(
			@WebParam(name="error") WebshopError theError) {
		myLogger.error(asString(theError)) ;
		WebshopErrorResult result = new WebshopErrorResult() ;
		result.setOkay() ;
		return result ;
	}
	
	
	private String asString(WebshopError theError) {
		return MessageFormat.format(
				"External Error: id {0}, level {1}, category {2}, text ''{3}'', dataId {4}, errorData ''{5}''",
				new Object[] {
						theError.getId(), theError.getLevel(), theError.getCategory(),
						theError.getText(), theError.getDataId(), theError.getErrorData()
				}) ;
	}
}
