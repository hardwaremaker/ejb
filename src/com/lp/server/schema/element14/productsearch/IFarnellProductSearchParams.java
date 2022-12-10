package com.lp.server.schema.element14.productsearch;

import java.util.Map;

public interface IFarnellProductSearchParams {
	class TermPrefix {
		public static final String PartNumber = "id:";
		public static final String Keyword = "any:";
		public static final String ManufacturerPartNumber = "manuPartNum:";
	}
	
	class Param {
		public static final String ApiKey = "callInfo.apiKey";
		public static final String Term = "term";
		public static final String StoreId = "storeInfo.id";
		public static final String NumberOfResults = "resultsSettings.numberOfResults";
		public static final String Offset = "resultsSettings.offset";
		public static final String RefinementFilters = "resultsSettings.refinements.filters";
		public static final String ResponseGroup = "resultsSettings.responseGroup";
		public static final String ResponseDataFormat = "callInfo.responseDataFormat";
		public static final String Signature = "userInfo.signature";
		public static final String CustomerId = "userInfo.customerId";
		public static final String Timestamp = "userInfo.timestamp";
	}
	
	class OperationName {
		public static final String PartNumber = "searchByPremierFarnellPartNumber";
		public static final String Keyword = "searchByKeyword";
		public static final String ManufacturerPartNumber = "searchByManufacturerPartNumber";
	}
	
	String getTerm();
	String getStoreinfoId();
	String getApiKey();
	String getResponseDataFormat();
	String getOffset();
	String getNumberOfResults();
	String getRefinementsFilters();
	String getResponseGroup();
	String getSignature();
	String getCustomerId();
	String getTimestamp();
	Map<String, String> asMap();
	
	String getOperationName();
}
