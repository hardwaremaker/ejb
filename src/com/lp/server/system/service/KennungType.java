package com.lp.server.system.service;

public enum KennungType {
	OrdersNadBuyer("ORDERS-NAD+BY", "Edifact Bestellung K\u00e4ufer", false),
	OrdersNadDelivery("ORDERS-NAD+DP", "Edifact Bestellung Lieferempf\u00e4nger", false),
	OrdersNadInvoice("ORDERS-NAD+IV", "Edifact Bestellung Rechnungsempf\u00e4nger", false),
	ZugferdBuyerTradePartyID("ZUGFeRD-BuyerTradeParty-ID", "ZUGFeRD Identifikation des K\u00E4ufers", true);

	
	private String value;
	private String description;
	private boolean unique;
	
	KennungType(String value, String description, boolean unique){
		this.value = value;
		this.description = description;
		this.unique = unique;
	}

	public String getText() {
		return value ;
	}
	
	public String getDescription() {
		return description;
	}

	public boolean isUnique() {
		return unique;
	}
	
	public static KennungType fromString(String text) {
		if(text != null) {
			for (KennungType status : KennungType.values()) {
				if(text.equalsIgnoreCase(status.value) || text.equals(status.toString())) {
					return status ;
				}
			}
		}
		
		throw new IllegalArgumentException("No enum value '" + text + "'") ;
	}
}
