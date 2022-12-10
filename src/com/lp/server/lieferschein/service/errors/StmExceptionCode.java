package com.lp.server.lieferschein.service.errors;

public enum StmExceptionCode {
	PERSONALNUMMER_UNBEKANNT("personalnummerunbekannt"),
	ANSPRECHPARTNER_FEHLT("ansprechpartnerfehlt"),
	LAGER_UNBEKANNT("lagerunbekannt"),
	EINKAUFSEAN_UNBEKANNT("einkaufseanunbekannt"),
	PROPERTY_FEHLT("propertyfehlt"),
	UNBEKANNTER_FEHLER("unbekannterfehler"), 
	XML_TRANSFORMATION("xmltransformation"), 
	MANDANT_KEIN_KUNDE("mandantkeinkunde"), 
	ARTIKEL_OHNE_MWST_SATZ("artikelohnemwst");
	
	private String value;
	
	StmExceptionCode(String value) {
		this.value = value ;
	}
	
	public String getText() {
		return value ;
	}
	
	public static StmExceptionCode fromString(String text) {
		if(text != null) {
			for (StmExceptionCode status : StmExceptionCode.values()) {
				if(text.equalsIgnoreCase(status.value)) {
					return status ;
				}
			}
		}
		
		throw new IllegalArgumentException("No enum value '" + text + "'") ;	
	}
}
