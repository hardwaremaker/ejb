package com.lp.server.forecast.service;

public class FclieferadresseNokaDto extends FclieferadresseDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7730675859849274682L;

	private FclieferadresseNoka kommissionierTyp;

	public FclieferadresseNoka getKommissionieren() {
		return kommissionierTyp;
	}

	public void setKommissionieren(FclieferadresseNoka kommissionieren) {
		this.kommissionierTyp = kommissionieren;
	}
}
