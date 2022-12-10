package com.lp.server.personal.service;

public enum HvmaLizenzEnum {
	Offline("Offline"),
	Online("Online"),
	Kpi("KPI"),
	WaWi_SK("WaWi_SK");
	
	HvmaLizenzEnum(String value) {
		this.value = value;
	}
	
	public String getText() {
		return value;
	}
	
	public static HvmaLizenzEnum fromString(String text) {
		if(text != null) {
			for (HvmaLizenzEnum status : HvmaLizenzEnum.values()) {
				if(text.equalsIgnoreCase(status.value)) {
					return status ;
				}
			}
		}
		throw new IllegalArgumentException("No enum value '" + text + "'") ;
	}
	
	public static HvmaLizenzEnum lookup(String id) {
		for (HvmaLizenzEnum status : HvmaLizenzEnum.values()) {
			if (status.toString().equalsIgnoreCase(id)) {
				return status;
			}
		}
		throw new IllegalArgumentException("No enum '" + id + "'");
	}
	
	private String value;
}
