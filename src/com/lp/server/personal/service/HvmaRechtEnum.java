package com.lp.server.personal.service;

public enum HvmaRechtEnum {
	// Online
	Artikel("Artikel"),
	Fertigung("Fertigung"),
	Wareneingang("Wareneingang"),
	Lieferung("Lieferung"),
	Zeiterfassung("Zeiterfassung"),
	
	// Offline
	Materialbedarf("Materialbedarf"),
	DokumenteErfassen("Dokumente erfassen"),
	Kommissionierliste("Kommissionierliste"),
	Packliste("Packliste"),
	DarfAuftragsKopfZeitBuchen("DarfAuftragsKopfZeitBuchen");

	HvmaRechtEnum(String value) {
		this.value = value;
	}
	
	public String getText() {
		return value;
	}
	
	public static HvmaRechtEnum fromString(String text) {
		if(text != null) {
			for (HvmaRechtEnum status : HvmaRechtEnum.values()) {
				if(text.equalsIgnoreCase(status.value)) {
					return status ;
				}
			}
		}
		throw new IllegalArgumentException("No enum value '" + text + "'") ;
	}
	
	public static HvmaRechtEnum lookup(String id) {
		for (HvmaRechtEnum status : HvmaRechtEnum.values()) {
			if (status.toString().equalsIgnoreCase(id)) {
				return status;
			}
		}
		throw new IllegalArgumentException("No enum '" + id + "'");
	}
	
	private String value;
}
