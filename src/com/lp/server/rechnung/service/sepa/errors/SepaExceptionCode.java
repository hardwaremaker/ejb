package com.lp.server.rechnung.service.sepa.errors;


public enum SepaExceptionCode {
	KEINE_BANKVERBINDUNG("keinebankverbindung"),
	KEINE_BIC("keinebic"),
	KEINE_GLAEUBIGERID("keineglaeubigerid"),
	KEINE_IBAN("keineiban"),
	KEINE_IBAN_MANDANT_BANKVERBINDUNG("keineibanmandant"),
	KEINE_LASTSCHRIFT_BANKVERBINDUNG("keinelastschriftbankverbindung"),
	KEINE_MANDATSNUMMER("keinemandatsnummer"),
	KEIN_LASTSCHRIFT_ZAHLUNGSZIEL("keinlastschriftzahlungsziel"),
	MANDATSNUMMER_ABGELAUFEN("mandatsnummerabgelaufen"),
	MANDATSNUMMER_KEINDATUM("mandatsnummerkeindatum"),
	KEIN_SEPAVERZEICHNIS("keinsepaverzeichnis"),
	UNBEKANNT("unbekannt");
	
	private String value;
	
	SepaExceptionCode(String value) {
		this.value = value ;
	}
	
	public String getText() {
		return value ;
	}
	
	public static SepaExceptionCode fromString(String text) {
		if(text != null) {
			for (SepaExceptionCode status : SepaExceptionCode.values()) {
				if(text.equalsIgnoreCase(status.value)) {
					return status ;
				}
			}
		}
		
		throw new IllegalArgumentException("No enum value '" + text + "'") ;	
	}
}
