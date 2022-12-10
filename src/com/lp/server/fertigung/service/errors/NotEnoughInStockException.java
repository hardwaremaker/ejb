package com.lp.server.fertigung.service.errors;

import java.math.BigDecimal;

public class NotEnoughInStockException extends ImportException {
	private static final long serialVersionUID = 4199978607824751302L;

	private String articlenumber;
	private BigDecimal quantityRequired;
	private BigDecimal quantityInStock;
	private String stock;
	
	public NotEnoughInStockException(String articlenumber, BigDecimal quantityRequired,
			BigDecimal quantityInStock, String stock) {
		super("Not enough in stock for article '" + articlenumber + "': quantity required is " 
			+ quantityRequired + ", quantity in stock '" + stock + "' is " + quantityInStock);
		this.articlenumber = articlenumber;
		this.quantityRequired = quantityRequired;
		this.quantityInStock = quantityInStock;
		this.stock = stock;
	}
	
	public String getArticlenumber() {
		return articlenumber;
	}
	
	public void setArticlenumber(String articlenumber) {
		this.articlenumber = articlenumber;
	}
	
	public BigDecimal getQuantityInStock() {
		return quantityInStock;
	}
	
	public void setQuantityInStock(BigDecimal quantityInStock) {
		this.quantityInStock = quantityInStock;
	}
	
	public BigDecimal getQuantityRequired() {
		return quantityRequired;
	}
	
	public void setQuantityRequired(BigDecimal quantityRequired) {
		this.quantityRequired = quantityRequired;
	}
	
	public String getStock() {
		return stock;
	}
	
	public void setStock(String stock) {
		this.stock = stock;
	}
}
