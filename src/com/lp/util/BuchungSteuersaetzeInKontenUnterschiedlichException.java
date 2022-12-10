package com.lp.util;

import java.util.Date;

import com.lp.server.finanz.service.BuchungDto;
import com.lp.server.finanz.service.KontoDto;

public class BuchungSteuersaetzeInKontenUnterschiedlichException extends EJBExceptionData {
	private static final long serialVersionUID = 6091842263463605287L;
	private Date vonDate;
	private Date bisDate;
	private KontoDto kontoDto;
	private KontoDto gegenKontoDto;
	private BuchungDto buchungDto;
	
	public BuchungSteuersaetzeInKontenUnterschiedlichException(Date vonDate, 
			Date bisDate, KontoDto kontoDto, KontoDto gegenKontoDto, BuchungDto buchungDto) {
		setVonDate(vonDate); 
		setBisDate(bisDate);
		setKontoDto(kontoDto);
		setGegenKontoDto(gegenKontoDto);
		setBuchungDto(buchungDto);
	}

	public Date getVonDate() {
		return vonDate;
	}

	public void setVonDate(Date vonDate) {
		this.vonDate = vonDate;
	}

	public Date getBisDate() {
		return bisDate;
	}

	public void setBisDate(Date bisDate) {
		this.bisDate = bisDate;
	}

	public BuchungDto getBuchungDto() {
		return buchungDto;
	}

	public void setBuchungDto(BuchungDto buchungDto) {
		this.buchungDto = buchungDto;
	}

	public KontoDto getKontoDto() {
		return kontoDto;
	}

	public void setKontoDto(KontoDto kontoDto) {
		this.kontoDto = kontoDto;
	}

	public KontoDto getGegenKontoDto() {
		return gegenKontoDto;
	}

	public void setGegenKontoDto(KontoDto gegenKontoDto) {
		this.gegenKontoDto = gegenKontoDto;
	}
}
