package com.lp.util.barcode;

public enum HvBarcodeTyp {
	Unbekannt("Unbekannt"),
	LosKombiAg("$V"),
	AuftragKombiTaetigkeit("$U"),
	LosKombiTaetigkeit("$W"),
	MaschineStopp("$SP"),
	Los("$L");
	
	private HvBarcodeTyp(String value) {
		this.value = value;
	}
	
	private String value;
	
	public String getText() {
		return value;
	}
	
	public static HvBarcodeTyp fromString(String text) {
		if (text != null) {
			for (HvBarcodeTyp typ : HvBarcodeTyp.values()) {
				if (text.equals(typ.value)) {
					return typ;
				}
			}
		}
		return HvBarcodeTyp.Unbekannt;
	}
}
