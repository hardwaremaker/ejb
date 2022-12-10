package com.lp.service.edifact.schema;

import java.math.BigDecimal;

/**
 * Price Information
 * 
 * @author ghp
 */
public class PriInfo {
	private String qualifier;
	private BigDecimal price;
	private String typeCoded;
	private String typeQualifier;
	private BigDecimal basis;
	private String unitQualifier;
	private String sublinePriceChangeType;
	
	public String getQualifier() {
		return qualifier;
	}
	public void setQualifier(String qualifier) {
		this.qualifier = qualifier;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public String getTypeCoded() {
		return typeCoded;
	}
	public void setTypeCoded(String typeCoded) {
		this.typeCoded = typeCoded;
	}
	public String getTypeQualifier() {
		return typeQualifier;
	}
	public void setTypeQualifier(String typeQualifier) {
		this.typeQualifier = typeQualifier;
	}
	public BigDecimal getBasis() {
		return basis;
	}
	public void setBasis(BigDecimal basis) {
		this.basis = basis;
	}
	public String getUnitQualifier() {
		return unitQualifier;
	}
	public void setUnitQualifier(String unitQualifier) {
		this.unitQualifier = unitQualifier;
	}
	public String getSublinePriceChangeType() {
		return sublinePriceChangeType;
	}
	public void setSublinePriceChangeType(String sublinePriceChangeType) {
		this.sublinePriceChangeType = sublinePriceChangeType;
	}
}
