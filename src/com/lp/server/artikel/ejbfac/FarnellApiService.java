package com.lp.server.artikel.ejbfac;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lp.server.artikel.service.PartSearchForbiddenExc;
import com.lp.server.artikel.service.PartSearchUnexpectedResponseExc;
import com.lp.server.partner.bl.partsearch.PartSearchFarnellErrorException;
import com.lp.server.schema.element14.productsearch.FarnellError;
import com.lp.server.schema.element14.productsearch.IFarnellProductSearchParams;
import com.lp.server.shop.ejbfac.HttpTalk;
import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;
import com.lp.util.EJBExceptionLP;

public class FarnellApiService {
	protected final ILPLogger myLogger = LPLogService.getInstance().getLogger(this.getClass());

	private ObjectMapper jsonMapper;
	
	private ObjectMapper getJsonMapper() {
		if(jsonMapper == null) {
			jsonMapper = new ObjectMapper() ;
			jsonMapper
				.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
				.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true)
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		}
		return jsonMapper ;
	}		
	
	public <T> T search(String baseUrl, IFarnellProductSearchParams params, Class<T> resultClass) throws PartSearchUnexpectedResponseExc {
		HttpTalk talk = null;
		try {
			talk = HttpTalkFarnell.get(baseUrl, params.asMap()).acceptJson();
			HttpResponse response = talk.ex();
			int statusCode = response.getStatusLine().getStatusCode();
			myLogger.info("Farnell Api Request: [" + talk.getRequest().getRequestLine().getUri() 
					+ "] with Response: [" + response.toString() + "]");
			if (HttpStatus.SC_OK == statusCode) {
				HttpEntity responseEntity = response.getEntity();
				myLogger.info(responseEntity.toString());
				T result = getJsonMapper().readValue(responseEntity.getContent(), resultClass);
				return result;
			} 
			throw getExcOnUnexpectedStatusCode(talk, response);
		} catch (JsonParseException e) {
			myLogger.error("JsonParseException", e);
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_WEBABFRAGE_ARTIKELLIEFERANT_EXC_WAEHREND_HTTP_REQUEST, e);
		} catch (JsonMappingException e) {
			myLogger.error("JsonMappingException", e);
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_WEBABFRAGE_ARTIKELLIEFERANT_EXC_WAEHREND_HTTP_REQUEST, e);
		} catch (IllegalStateException e) {
			myLogger.error("IllegalStateException", e);
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_WEBABFRAGE_ARTIKELLIEFERANT_EXC_WAEHREND_HTTP_REQUEST, e);
		} catch (IOException e) {
			myLogger.error("IOException", e);
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_WEBABFRAGE_ARTIKELLIEFERANT_EXC_WAEHREND_HTTP_REQUEST, e);
		} catch (URISyntaxException e) {
			myLogger.error("URISyntaxException", e);
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_WEBABFRAGE_ARTIKELLIEFERANT_EXC_WAEHREND_HTTP_REQUEST, e);
		} finally {
			if (talk != null)
				talk.close();
		}
	}
	
	private PartSearchUnexpectedResponseExc getExcOnUnexpectedStatusCode(HttpTalk talk, HttpResponse response) {
		HttpResponseHelper responseHelp = new HttpResponseHelper(response);
		myLogger.error("Unexpected Response [" + responseHelp.statusLine() + " by Farnell Request: [" + talk.getRequest().getRequestLine().getUri() + "]"
				+ "\nResponse-Header: " + responseHelp.headersAsString());
		PartSearchUnexpectedResponseExc exc = new PartSearchUnexpectedResponseExc(
				talk.getRequest().getRequestLine().getUri(), 
				responseHelp.statusCode(), 
				responseHelp.statusLine());
		exc.setApiError(responseHelp.getValueOfHeader("X-Error-Detail-Header"));

		if (HttpStatus.SC_FORBIDDEN == responseHelp.statusCode()) {
			return new PartSearchForbiddenExc(exc);
		}
		
		FarnellError farnellError = transformError(response.getEntity());
		if (farnellError != null && farnellError.getFault() != null) {
			log(farnellError);
			PartSearchFarnellErrorException farnellExc = new PartSearchFarnellErrorException(exc);
			farnellExc.setFarnellError(farnellError);
			return farnellExc;
		}

		return exc;
	}

	private void log(FarnellError farnellError) {
		myLogger.error(farnellError.asString());
	}

	private FarnellError transformError(HttpEntity entity) {
		try {
			org.apache.http.Header contentEncodingHeader = entity.getContentEncoding();
			if (contentEncodingHeader != null) {
				HeaderElement[] encodings = contentEncodingHeader.getElements();
				for (HeaderElement element : encodings) {
					if (element.getName().equalsIgnoreCase("gzip")) {
						entity = new GzipDecompressingEntity(entity);
						String jsonError = EntityUtils.toString(entity, Charset.forName("UTF-8").name());
						FarnellError farnellError = getJsonMapper().readValue(jsonError, FarnellError.class);
						return farnellError;
					}
				}
			}
		} catch (IOException exc) {
			myLogger.error("IOException", exc);
		}
		return null;
	}
}
