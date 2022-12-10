package com.lp.util;

import java.util.List;

import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.finanz.service.SepakontoauszugDto;
import com.lp.server.rechnung.service.RechnungDto;

public class SepaZahlungenMitAuszugsnummerVorhandenException extends EJBExceptionData {
	private static final long serialVersionUID = 5389506338090595305L;

	private SepakontoauszugDto sepakontoauszugDto;
	private List<RechnungDto> rechnungen;
	private List<EingangsrechnungDto> eingangsrechnungen;

	public SepaZahlungenMitAuszugsnummerVorhandenException(SepakontoauszugDto sepakontoauszugDto,
			List<RechnungDto> rechnungzahlungen, List<EingangsrechnungDto> eingangsrechnungzahlungen) {
		super();
		this.sepakontoauszugDto = sepakontoauszugDto;
		this.rechnungen = rechnungzahlungen;
		this.eingangsrechnungen = eingangsrechnungzahlungen;
	}

	public SepakontoauszugDto getSepakontoauszugDto() {
		return sepakontoauszugDto;
	}
	public void setSepakontoauszugDto(SepakontoauszugDto sepakontoauszugDto) {
		this.sepakontoauszugDto = sepakontoauszugDto;
	}
	
	public List<RechnungDto> getRechnungen() {
		return rechnungen;
	}
	public void setRechnungen(List<RechnungDto> rechnungen) {
		this.rechnungen = rechnungen;
	}
	
	public List<EingangsrechnungDto> getEingangsrechnungen() {
		return eingangsrechnungen;
	}
	public void setEingangsrechnungen(List<EingangsrechnungDto> eingangsrechnungen) {
		this.eingangsrechnungen = eingangsrechnungen;
	}
}
