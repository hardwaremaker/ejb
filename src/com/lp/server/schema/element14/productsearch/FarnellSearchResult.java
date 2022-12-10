package com.lp.server.schema.element14.productsearch;

import java.io.Serializable;
import java.util.List;

public class FarnellSearchResult implements Serializable {
	private static final long serialVersionUID = 9050093340908975890L;

	private Integer numberOfResults;
	private List<FarnellProduct> products;
	
	public FarnellSearchResult() {
	}

	public Integer getNumberOfResults() {
		return numberOfResults;
	}
	
	public void setNumberOfResults(Integer numberOfResults) {
		this.numberOfResults = numberOfResults;
	}
	
	public List<FarnellProduct> getProducts() {
		return products;
	}
	
	public void setProducts(List<FarnellProduct> products) {
		this.products = products;
	}
}
