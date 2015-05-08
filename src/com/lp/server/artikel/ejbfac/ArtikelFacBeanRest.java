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
package com.lp.server.artikel.ejbfac;

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
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.MTOM;

import org.jboss.wsf.spi.annotation.WebContext;

import com.lp.server.system.service.WebshopAuthHeader;
import com.lp.server.util.Facade;
import com.lp.server.util.logger.webservice.WebserviceCallInterceptor;

@MTOM
@WebService(name="IWebshop2ItemServices", serviceName="HeliumVItemServiceOpenTrans")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@BindingType(value = "http://schemas.xmlsoap.org/wsdl/soap/http?mtom=true")
@Stateless
// @Path(ArtikelFacBeanRest.BASE_PATH)
@WebContext (urlPattern="/version2/ArtikelFacBeanRest")  
@Interceptors(WebserviceCallInterceptor.class)
public class ArtikelFacBeanRest extends Facade implements WebshopItemServiceInterface {

	/**
	 * Rootpath zum Itemservice
	 */
	public static final String BASE_PATH = "/itemservice/v1" ;

	/**
	 * Path zur Resource "items"
	 */
	public static final String ITEM_PATH = "/items/" ;

	/**
	 * Path zur Resource "itemimages"
	 */
	public static final String ITEMIMAGE_PATH = "/itemimages/" ;

	private WebshopItemServiceInterface delegate ;

	@Resource
	WebServiceContext context ;
	
	public ArtikelFacBeanRest() {
		// delegate = new WebshopItemServiceMock() ;
		delegate = getWebshopItemServiceFac() ;
	}
	
	@Override
	@WebMethod
	public ShopgroupsResult getShopGroupsFindAll(
//			@WebParam(header = true) WebshopAuthHeader header) {
			@WebParam  WebshopAuthHeader header) {
		return delegate.getShopGroupsFindAll(header) ;
	}

	@Override
	@WebMethod
	public ShopgroupsResult getShopGroupsFindAllChanged(
			@WebParam WebshopAuthHeader header, 
			@WebParam(name="changedDateTime") String changedDate) {
		return delegate.getShopGroupsFindAllChanged(header, changedDate) ;
	}

	@Override
	@WebMethod
	public ShopgroupsFlatResult getShopGroupsFlatFindAll(
			@WebParam WebshopAuthHeader header) {
		return delegate.getShopGroupsFlatFindAll(header) ;
	}

	@Override
	@WebMethod
	public ShopgroupsFlatResult getShopGroupsFlatFindAllChanged(
			@WebParam WebshopAuthHeader header, 
			@WebParam(name="changedDateTime") String changedDate) {
		return delegate.getShopGroupsFlatFindAllChanged(header, changedDate);
	}

	@GET
	@Override
	@WebMethod
	public ShopgroupResult getShopGroupFindByCnr(
			@WebParam WebshopAuthHeader header,
			@WebParam(name="cnr") String cnr) {
		return delegate.getShopGroupFindByCnr(header, cnr) ;
	}
	
	@GET
	@Override
	@WebMethod
	public ShopgroupsResult getShopGroupsFindByCnrChanged(
			@WebParam WebshopAuthHeader header, 
			@WebParam(name="cnr") String rootShopgruppe,
			@WebParam(name="changedDateTime") String changedDate) {
		return delegate.getShopGroupsFindByCnrChanged(header, rootShopgruppe, changedDate);
	}
	
	@GET
	@Override
	@WebMethod
	public ShopgroupsResult getShopGroupsFindByIdChanged(
			@WebParam WebshopAuthHeader header, 
			@WebParam(name="id") Integer rootShopgruppeIId,
			@WebParam(name="changedDateTime") String changedDate) {
		return delegate.getShopGroupsFindByIdChanged(header, rootShopgruppeIId, changedDate);
	}

	@Override
	@WebMethod
	public ShopgroupResult getShopGroupFindById(
			@WebParam WebshopAuthHeader header, 
			@WebParam(name="id") Integer id) {
		return delegate.getShopGroupFindById(header, id) ;
	}

	
 	@GET
	@Path(ITEM_PATH + "{cnr}")
	@WebMethod
	@Override
	public WebshopItemResult getItemFindByCnr(
			@WebParam WebshopAuthHeader header,
			@PathParam("cnr") @WebParam(name="cnr") String name) {
		return delegate.getItemFindByCnr(header, name) ;
	}

 	@GET
	@Path(ITEM_PATH + "{id}")
	@WebMethod
	@Override
	public WebshopItemResult getItemFindById(
			@WebParam WebshopAuthHeader header,
			@PathParam("id") @WebParam(name="id") Integer id) {
		return delegate.getItemFindById(header, id) ;
	}
 	
 	@GET
	@Path(ITEM_PATH)
 	@Override
	@WebMethod	
	public WebshopItemsResult getItems(
			@WebParam WebshopAuthHeader header) {
		return delegate.getItems(header);
	}


 	@GET
 	@Path(ITEM_PATH + "{changedDateTime}")
 	@WebMethod
	@Override
	public WebshopItemsResult getItemsChanged(
			@WebParam WebshopAuthHeader header,
			@PathParam("changedDateTime") @WebParam(name="changedDateTime") String changedDate) {
		return delegate.getItemsChanged(header, changedDate) ;
	}

	@GET
	@Path(ITEMIMAGE_PATH + "{name}")
	@WebMethod
	@Override
	public WebshopItemImageResult getItemImage(
			@WebParam WebshopAuthHeader header,
			@PathParam("name") @WebParam(name="itemImageName") String itemImageName) {
		return delegate.getItemImage(header, itemImageName);
	}
}
