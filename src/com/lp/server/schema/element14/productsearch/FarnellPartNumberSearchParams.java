package com.lp.server.schema.element14.productsearch;

import java.sql.Timestamp;

public class FarnellPartNumberSearchParams extends DefaultFarnellProductSearchParams {

	public FarnellPartNumberSearchParams(String term, String apiKey, String store) {
		super(term, apiKey, store);
	}
	
	public FarnellPartNumberSearchParams(String term, String apiKey, String store, String customerId, String signature, Timestamp timestamp) {
		super(term, apiKey, store, customerId, signature, timestamp);
	}

	@Override
	public void setTerm(String term) {
		super.setTerm(TermPrefix.PartNumber + term);
	}
	
	@Override
	public String getOperationName() {
		return OperationName.PartNumber;
	}
}
