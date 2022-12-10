package com.lp.server.schema.element14.productsearch;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;

public abstract class DefaultFarnellProductSearchParams implements IFarnellProductSearchParams {
	
	private SimpleDateFormat timestampFormat;
	private String term;
	private String apiKey;
	private String signature;
	private String customerId;
	private String storeinfo;
	private String timestamp;
	
	public DefaultFarnellProductSearchParams(String term, String apiKey) {
		setTerm(term);
		setApiKey(apiKey);
	}
	
	public DefaultFarnellProductSearchParams(String term, String apiKey, String store) {
		this(term, apiKey);
		setStoreinfo(store);
	}
	
	public DefaultFarnellProductSearchParams(String term, String apiKey, String store, String customerId, String signature, Timestamp timestamp) {
		this(term, apiKey, store);
		setCustomerId(customerId);
		setSignature(signature);
		setTimestamp(timestamp);
	}

	private SimpleDateFormat getTimestampFormat() {
		if (timestampFormat == null) {
			timestampFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
			TimeZone gmtTime = TimeZone.getTimeZone("GMT");
			timestampFormat.setTimeZone(gmtTime);
		}
		return timestampFormat;
	}
	
	@Override
	public String getTerm() {
		return term;
	}
	
	public void setTerm(String term) {
		this.term = term;
	}
	
	@Override
	public String getStoreinfoId() {
		return storeinfo != null ? storeinfo : "de.farnell.com";
	}
	
	public void setStoreinfo(String storeinfo) {
		this.storeinfo = storeinfo;
	}
	
	@Override
	public String getApiKey() {
		return apiKey;
	}
	
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	@Override
	public String getResponseDataFormat() {
		return "JSON";
	}

	@Override
	public String getOffset() {
		return "0";
	}

	@Override
	public String getNumberOfResults() {
		return "1";
	}

	@Override
	public String getRefinementsFilters() {
		return null;
	}

	@Override
	public String getResponseGroup() {
		return "large";
	}
	
	@Override
	public String getSignature() {
		return signature;
	}
	
	public void setSignature(String signature) {
		this.signature = signature;
	}
	
	@Override
	public String getCustomerId() {
		return customerId;
	}
	
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	@Override
	public String getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = getTimestampFormat().format(timestamp);
	}
	
	public Map<String, String> asMap() {
		Map<String, String> map = new HashMap<String, String>();
		if (getApiKey() != null)
			map.put(Param.ApiKey, getApiKey());
		
		if (getNumberOfResults() != null)
			map.put(Param.NumberOfResults, getNumberOfResults());
		
		if (getOffset() != null)
			map.put(Param.Offset, getOffset());
		
		if (getRefinementsFilters() != null)
			map.put(Param.RefinementFilters, getRefinementsFilters());
		
		if (getResponseDataFormat() != null)
			map.put(Param.ResponseDataFormat, getResponseDataFormat());
		
		if (getResponseGroup() != null)
			map.put(Param.ResponseGroup, getResponseGroup());
		
		if (getStoreinfoId() != null)
			map.put(Param.StoreId, getStoreinfoId());
		
		if (getTerm() != null)
			map.put(Param.Term, getTerm());
		
		if (getSignature() != null)
			map.put(Param.Signature, getSignature());
		
		if (getCustomerId() != null)
			map.put(Param.CustomerId, getCustomerId());
		
		if (getTimestamp() != null)
			map.put(Param.Timestamp, getTimestamp());

		return map;
	}
	
	public String asString() {
		StringBuilder builder = new StringBuilder();
		for (Entry<String, String> entry : asMap().entrySet()) {
			builder.append(entry.getKey())
				.append("=")
				.append(entry.getValue())
				.append(", ");
		}
		return builder.toString();
	}
}
