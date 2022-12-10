package com.lp.server.schema.element14.productsearch;

import java.sql.Timestamp;

public class FarnellKeywordSearchParams extends DefaultFarnellProductSearchParams {

	public FarnellKeywordSearchParams(String term, String apiKey, String store) {
		super(term, apiKey, store);
	}
	
	public FarnellKeywordSearchParams(String term, String apiKey, String store, String customerId, String signature, Timestamp timestamp) {
		super(term, apiKey, store, customerId, signature, timestamp);
	}


	@Override
	public void setTerm(String term) {
		super.setTerm(TermPrefix.Keyword + term);
	}
	
	@Override
	public String getOperationName() {
		return OperationName.Keyword;
	}
}
