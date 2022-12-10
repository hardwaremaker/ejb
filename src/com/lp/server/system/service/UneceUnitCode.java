package com.lp.server.system.service;

public enum UneceUnitCode {
	UNIT("C62", SystemFac.EINHEIT_STUECK),
	METER("MTR", SystemFac.EINHEIT_METER),
	KILOGRAM("KGM", SystemFac.EINHEIT_KILOGRAMM),
	LITRE("LTR", SystemFac.EINHEIT_LITER),
	HOUR("HUR", SystemFac.EINHEIT_STUNDE),
	ROLL("RO", "Rolle          "),
	BAG("PK", "Beutel         "),
	PACK("PK", "Packung        ");
	
	private String code;
	private String hvValue;
	
	private UneceUnitCode(String code, String hvValue) {
		this.code = code;
		this.hvValue = hvValue;
	}
	
	public String getCode() {
		return code;
	}

	public static UneceUnitCode fromString(String text) {
		if (text != null) {
			for (UneceUnitCode status : UneceUnitCode.values()) {
				if (text.equalsIgnoreCase(status.hvValue)) {
					return status;
				}
			}
		}
		throw new IllegalArgumentException("No enum value '" + text + "'");
	}
	
	public static UneceUnitCode fromStringTrimmed(String text) {
		if (text != null) {
			text = text.trim();
			for (UneceUnitCode status : UneceUnitCode.values()) {
				if (text.equalsIgnoreCase(status.hvValue.trim())) {
					return status;
				}
			}
		}
		throw new IllegalArgumentException("No enum value '" + text + "'");
	}
}
