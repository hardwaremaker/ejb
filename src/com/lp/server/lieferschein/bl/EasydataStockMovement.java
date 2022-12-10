package com.lp.server.lieferschein.bl;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class EasydataStockMovement implements Serializable {
	private static final long serialVersionUID = 1672845660828269628L;

	private Integer serialNumber;
	private Integer personalNumber;
	private Timestamp tourTime;
	private Timestamp loginTime;
	private Timestamp logoutTime;
	private Integer autoIndex;
	private String stock;
	private String targetStock;
	private List<EasydataProduct> products;
	
	public EasydataStockMovement() {
	}

	public Integer getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(Integer serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Integer getPersonalNumber() {
		return personalNumber;
	}

	public void setPersonalNumber(Integer personalNumber) {
		this.personalNumber = personalNumber;
	}

	public Timestamp getTourTime() {
		return tourTime;
	}

	public void setTourTime(Timestamp tourTime) {
		this.tourTime = tourTime;
	}

	public Timestamp getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Timestamp loginTime) {
		this.loginTime = loginTime;
	}

	public Timestamp getLogoutTime() {
		return logoutTime;
	}

	public void setLogoutTime(Timestamp logoutTime) {
		this.logoutTime = logoutTime;
	}

	public Integer getAutoIndex() {
		return autoIndex;
	}

	public void setAutoIndex(Integer autoIndex) {
		this.autoIndex = autoIndex;
	}

	public String getStock() {
		return stock;
	}

	public Integer getStockAsInteger() {
		try {
			if (getStock() == null) return null;
			
			return Integer.parseInt(getStock().length() > 5 ? 
					getStock().substring(getStock().length() - 5) : getStock());
		} catch (NumberFormatException ex) {
			return null;
		}
	}
	
	public void setStock(String stock) {
		this.stock = stock;
	}

	public String getTargetStock() {
		return targetStock;
	}

	public Integer getTargetStockAsInteger() {
		try {
			return Integer.parseInt(getTargetStock());
		} catch (NumberFormatException ex) {
			return null;
		}
	}
	
	public void setTargetStock(String targetStock) {
		this.targetStock = targetStock;
	}

	public List<EasydataProduct> getProducts() {
		if (products == null) {
			products = new ArrayList<EasydataProduct>();
		}
		return products;
	}
}
