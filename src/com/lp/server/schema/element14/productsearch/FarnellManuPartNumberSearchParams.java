package com.lp.server.schema.element14.productsearch;

import java.sql.Timestamp;

public class FarnellManuPartNumberSearchParams extends DefaultFarnellProductSearchParams {

	public FarnellManuPartNumberSearchParams(String term, String apiKey, String store) {
		super(term, apiKey, store);
	}
	
	public FarnellManuPartNumberSearchParams(String term, String apiKey, String store, String customerId, String signature, Timestamp timestamp) {
		super(term, apiKey, store, customerId, signature, timestamp);
	}

	@Override
	public void setTerm(String term) {
		super.setTerm(TermPrefix.ManufacturerPartNumber + term);
	}
	
	@Override
	public String getOperationName() {
		return OperationName.ManufacturerPartNumber;
	}
}
