package com.lp.server.finanz.service;


public enum KontoartEnum {
	Nichtzutreffend("Nicht zutreffend"),
	Abgaben("Abgaben"),
	Eroeffnungskonto("Eroeffnungskonto"),
	FAZahllastkonto("FA Zahllastkonto"),
	Ustkonto("Ust Konto"),
	UstErwerbsteuerkonto("UST- oder Erwerbssteuerkonto"),
	UstSammelkonto("Ust Sammelkonto"),
	VstKonto("Vst Konto"),
	VstSammelkonto("Vst Sammelkonto") ;
	
	private String value;
	
	KontoartEnum(String value) {
		this.value = value ;
	}
	
	public String getText() {
		return value ;
	}
	
	public static KontoartEnum fromString(String text) {
		if(text != null) {
			for (KontoartEnum status : KontoartEnum.values()) {
				if(text.equalsIgnoreCase(status.value)) {
					return status ;
				}
			}
		}
		
		throw new IllegalArgumentException("No enum value '" + text + "'") ;	
	}
}
