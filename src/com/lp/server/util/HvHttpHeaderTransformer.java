package com.lp.server.util;

import org.apache.http.Header;
import org.apache.http.HttpResponse;

public class HvHttpHeaderTransformer implements IHttpHeaderConstants {

	private HvHttpHeader hvHeader;
	
	public HvHttpHeader process(HttpResponse response) {
		hvHeader = new HvHttpHeader();
		extractKeys(response);
		
		return hvHeader;
	}
	
	private void extractKeys(HttpResponse response) {
		if (response == null) return;
		
		Header[] headers = response.getAllHeaders();
		if (headers == null) return;
		
		for (Header header : headers) {
			if (X_HV_ERROR_CODE.equalsIgnoreCase(header.getName())) {
				hvHeader.setErrorCode(asInteger(header.getValue()));
			} else if (X_HV_ERROR_CODE_EXTENDED.equalsIgnoreCase(header.getName())) {
				hvHeader.setErrorCodeExtended(asInteger(header.getValue()));
			} else if (X_HV_ERROR_CODE_DESCRIPTION.equalsIgnoreCase(header.getName())) {
				hvHeader.setErrorCodeDescription(header.getValue());
			} else if (X_HV_ERROR_KEY.equalsIgnoreCase(header.getName())) {
				hvHeader.setErrorKey(header.getValue());
			} else if (X_HV_ERROR_VALUE.equalsIgnoreCase(header.getName())) {
				hvHeader.setErrorValue(header.getValue());
			} else if (X_HV_ERROR_CODE_TRANSLATED.equalsIgnoreCase(header.getName())) {
				hvHeader.setErrorTranslated(header.getValue());
			} else if (X_HV_ADDITIONAL_ERROR_KEY.equalsIgnoreCase(header.getName())) {
				hvHeader.setAdditionalErrorKey(header.getValue());
			} else if (X_HV_ADDITIONAL_ERROR_VALUE.equalsIgnoreCase(header.getName())) {
				hvHeader.setAdditionalErrorValue(header.getValue());
			}
		}
		
		hvHeader.setStatusCode(response.getStatusLine() != null ? 
				response.getStatusLine().getStatusCode() : null);
		hvHeader.setStatusReason(response.getStatusLine() != null ?
				response.getStatusLine().getReasonPhrase() : null);
	}

	private Integer asInteger(String value) {
		if (value == null) return null;
		
		try {
			Integer eecI = Integer.parseInt(value);
			return eecI;
		} catch(NumberFormatException exc) {
			return null;
		}
	}
	
}
