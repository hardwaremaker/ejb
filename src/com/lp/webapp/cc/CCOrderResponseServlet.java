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
package com.lp.webapp.cc;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.InvalidFileNameException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.lp.server.artikel.service.BaseRequestResult;
import com.lp.server.auftrag.ejbfac.AuftragFacBeanRest;
import com.lp.server.auftrag.service.CreateOrderResult;
import com.lp.server.system.service.WebshopAuthHeader;
import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;
import com.lp.util.Helper;

public class CCOrderResponseServlet extends HttpServlet {
	protected final ILPLogger myLogger = LPLogService.getInstance().getLogger(this.getClass());
	
	public final static String PARAM_USER = "user" ;
	public final static String PARAM_PASSWORD = "password" ;
	public final static String PARAM_COUNTRY = "country" ;
	public final static String PARAM_LANGUAGE = "language" ;
	public final static String PARAM_WEBSHOP  = "webshop" ;
	
	public final static String PARAM_TOKEN = "token" ;
	
	public final static String CONFIG_MAXSIZE = "maxsize" ;

	/**
	 * 
	 */
	private static final long serialVersionUID = -2200501777795781736L;

	private WebshopAuthHeader authHeader = null ;
	private long maxUploadSize = 1000000l ;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		authHeader = new WebshopAuthHeader() ;
		authHeader.setUser(config.getInitParameter(PARAM_USER)) ;
		authHeader.setPassword(config.getInitParameter(PARAM_PASSWORD)) ;
		authHeader.setIsoCountry(config.getInitParameter(PARAM_COUNTRY)) ;
		authHeader.setIsoLanguage(config.getInitParameter(PARAM_LANGUAGE)) ;
		authHeader.setShopName(config.getInitParameter(PARAM_WEBSHOP)) ;
		
		String xmlMaxSize = config.getInitParameter(CONFIG_MAXSIZE) ;
		if(null != xmlMaxSize && xmlMaxSize.trim().length() != 0) {
			try {
				maxUploadSize = Long.parseLong(xmlMaxSize) ;
			} catch(NumberFormatException e) {				
			}
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		PrintWriter out = resp.getWriter() ;
	    out.println(
	    	"<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">\n" +
	    	"<HTML>\n" +
	    	"<HEAD><TITLE>Helium V CleverCure Order Response</TITLE></HEAD>\n" +
	    	"<BODY>\n" +
	    		"<H1>Instructions for Helium V CleverCure OrderResponse Request</H1>\n" +
	    		"<p>You have to POST a XML OpenTrans 1.0 document to this url</p>" +
	    		"<p>Set parameter <i>token</i> to the value given to you." +
	    	"</BODY></HTML>");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse response)
			throws ServletException, IOException {

		ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory()) ;
		upload.setSizeMax(maxUploadSize) ;
		
		if(!ServletFileUpload.isMultipartContent(req)) {
			myLogger.info("Received request without form/multipart data. Aborting.") ;
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST) ;
			return ;
		}
		
		FileItem file = null ;
		
		try {
			List<FileItem> files = upload.parseRequest(req) ;
			if(files.size() != 1) {
				response.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE) ;
				return ;
			}
			
			file = files.get(0) ;			
			processOrder(req, response, file);
		} catch(FileUploadException e) {
			myLogger.error("Upload exception: ", e) ;
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST) ;
		} catch(Exception e) {
			myLogger.error("Processing file exception: ", e) ;	
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR) ;
			
			saveXmlFile(file) ;
		}
	}

	private void saveXmlFile(FileItem file) {
		if(null == file) return ;
		
		String fileName = null ;
		try {
			fileName = file.getName() ;
			if(fileName != null) {
				fileName = fileName.replace('\\', '_') ;
				fileName = fileName.replace(':', '_') ;
				fileName = fileName.replace('/', '_') ;
			}
		} catch(InvalidFileNameException e) {
			fileName = ".xml" ;
		}
		try {
			file.write(new File(System.getProperty("java.io.tmpdir"),
					"CCOr_" + System.currentTimeMillis() + "_" + fileName)) ;			
		} catch(Exception e) {
			myLogger.error("Couldn't write file '" + file.getName() + "'", e) ;
		}
	}
	
	private void processOrder(HttpServletRequest req, HttpServletResponse response, FileItem file) {
		authHeader.setToken(req.getParameter(PARAM_TOKEN)) ;
		CreateOrderResult result = processEjbOrder(response, file) ;
		
		int httpStatus = getHttpStatusforEjbStatus(result) ;
		response.setStatus(httpStatus) ;
		
		myLogger.info("Returning httpStatus '" + 
				httpStatus + "' for request '" +
				file.getName() + "'. Status (" + result.getRc() + ")") ;
		
		if(!(httpStatus == HttpServletResponse.SC_CREATED
				|| httpStatus == HttpServletResponse.SC_OK)) {
			saveXmlFile(file) ;
		}
	}
	
	private int getHttpStatusforEjbStatus(CreateOrderResult result) {		
		if(Helper.isOneOf(result.getRc(), new int[]{
				CreateOrderResult.ERROR_EMPTY_ORDER, CreateOrderResult.ERROR_JAXB_EXCEPTION,
				CreateOrderResult.ERROR_SAX_EXCEPTION, CreateOrderResult.ERROR_UNMARSHALLING})) {
			return  HttpServletResponse.SC_BAD_REQUEST ;
		}
		
		if(result.getRc() == CreateOrderResult.ERROR_AUTHENTIFICATION) {
			return HttpServletResponse.SC_FORBIDDEN ;											
		}
		
		if(result.getRc() == CreateOrderResult.ERROR_CUSTOMER_NOT_FOUND) {
			return HttpServletResponse.SC_NOT_FOUND ;
		}
		
		if(result.getRc() >= CreateOrderResult.ERROR_EJB_EXCEPTION) {
			return HttpServletResponse.SC_INTERNAL_SERVER_ERROR ;						
		}
		
		if(result.getRc() == BaseRequestResult.OKAY) {
			return HttpServletResponse.SC_CREATED ;
		}

		return HttpServletResponse.SC_EXPECTATION_FAILED ;
	}
	
	private CreateOrderResult processEjbOrder(
			HttpServletResponse response, FileItem file) {
		myLogger.info("Receiving post with filename '" + file.getName() + "' (" + file.getSize() +") bytes.") ;				

		AuftragFacBeanRest a = new AuftragFacBeanRest() ;
		CreateOrderResult result = a.createOrder(authHeader, new String(file.get())) ;

		myLogger.info("Processed post with filename '" + 
				file.getName() + "'. Status " + result.getRc()) ;
		return result ;
	}
}
