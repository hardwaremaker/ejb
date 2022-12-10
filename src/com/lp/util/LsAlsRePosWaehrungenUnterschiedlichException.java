package com.lp.util;

import com.lp.server.lieferschein.service.LieferscheinDto;

import com.lp.server.rechnung.service.RechnungDto;

public class LsAlsRePosWaehrungenUnterschiedlichException extends EJBExceptionData {
	private static final long serialVersionUID = -2635818215769936269L;

	private RechnungDto rechungDto;
	private LieferscheinDto lieferscheinDto;
	
	public LsAlsRePosWaehrungenUnterschiedlichException(RechnungDto rechnungDto, LieferscheinDto lieferscheinDto) {
		setRechungDto(rechnungDto);
		setLieferscheinDto(lieferscheinDto);
	}
	
	public void setRechungDto(RechnungDto rechungDto) {
		this.rechungDto = rechungDto;
	}
	
	public RechnungDto getRechungDto() {
		return rechungDto;
	}
	
	public void setLieferscheinDto(LieferscheinDto lieferscheinDto) {
		this.lieferscheinDto = lieferscheinDto;
	}
	
	public LieferscheinDto getLieferscheinDto() {
		return lieferscheinDto;
	}
}
