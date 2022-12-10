package com.lp.util;

import com.lp.server.partner.service.LieferantDto;

public class KreditorKontoException extends EJBExceptionData {

	private static final long serialVersionUID = 6815540990294017305L;

	private LieferantDto lieferantDto;
	
	public KreditorKontoException(LieferantDto lieferantDto) {
		this.setLieferantDto(lieferantDto);
	}

	public LieferantDto getLieferantDto() {
		return lieferantDto;
	}

	public void setLieferantDto(LieferantDto lieferantDto) {
		this.lieferantDto = lieferantDto;
	}

}
