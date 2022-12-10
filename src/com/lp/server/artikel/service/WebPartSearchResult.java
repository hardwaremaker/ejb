package com.lp.server.artikel.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WebPartSearchResult implements Serializable {
	private static final long serialVersionUID = 9124045280926955611L;

	private String searchValue;
	private List<WebPart> parts;
	private boolean executed;
	
	public WebPartSearchResult() {
		executed = false;
	}

	public List<WebPart> getParts() {
		if (parts == null) {
			parts = new ArrayList<WebPart>();
		}
		return parts;
	}
	
	public void setParts(List<WebPart> parts) {
		this.parts = parts;
	}
	
	public Integer getNumberOfResults() {
		return getParts().size();
	}

	public String getSearchValue() {
		return searchValue;
	}
	
	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}
	
	public boolean hasSingleResult() {
		return getNumberOfResults() == 1;
	}
	
	public boolean hasMultipleResults() {
		return getNumberOfResults() > 1;
	}
	
	public boolean hasNoResult() {
		return getNumberOfResults() == 0;
	}
	
	public void setExecuted(boolean executed) {
		this.executed = executed;
	}
	
	public boolean wasExecuted() {
		return executed;
	}
}
