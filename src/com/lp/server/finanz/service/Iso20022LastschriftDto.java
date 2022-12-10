package com.lp.server.finanz.service;

public class Iso20022LastschriftDto extends Iso20022NachrichtDto {
	private static final long serialVersionUID = 4260582647928453985L;

	public Iso20022LastschriftDto() {
		super(Type.LASTSCHRIFT);
	}

}
