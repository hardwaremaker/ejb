package com.lp.util;

import com.lp.server.partner.service.KundeDto;
import com.lp.server.partner.service.LieferantDto;

public class FremdsystemnrNichtNummerischException extends EJBExceptionData {
	private static final long serialVersionUID = 7877478153047261502L;

	private KundeDto kundeDto;
	private LieferantDto lieferantDto;
	
	public FremdsystemnrNichtNummerischException(KundeDto kundeDto) {
		setKundeDto(kundeDto);
	}
	
	public FremdsystemnrNichtNummerischException(LieferantDto lieferantDto) {
		setLieferantDto(lieferantDto);
	}

	public KundeDto getKundeDto() {
		return kundeDto;
	}

	public void setKundeDto(KundeDto kundeDto) {
		this.kundeDto = kundeDto;
	}

	public LieferantDto getLieferantDto() {
		return lieferantDto;
	}

	public void setLieferantDto(LieferantDto lieferantDto) {
		this.lieferantDto = lieferantDto;
	}
}
