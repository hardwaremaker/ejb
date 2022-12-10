package com.lp.server.finanz.service;

public class Iso20022ZahlungauftragDto extends Iso20022NachrichtDto {
	private static final long serialVersionUID = -7203277337616815419L;

	public Iso20022ZahlungauftragDto() {
		super(Type.ZAHLUNGSAUFTRAG);
	}

}
