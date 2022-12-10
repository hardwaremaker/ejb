package com.lp.util;

import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.rechnung.service.RechnungDto;

public class BelegdataBase extends EJBExceptionData {
	private static final long serialVersionUID = 1123741742716107589L;

	private EingangsrechnungDto eingangsrechnungDto ;
	private RechnungDto rechnungDto ;
	
	private String belegart ;
	private String belegnummer ;
	
	protected BelegdataBase() {
		setBelegart("") ;
		setBelegnummer("");
	}
	
	protected BelegdataBase(EingangsrechnungDto eingangsrechnungDto) {
		setEingangsrechnungDto(eingangsrechnungDto);
		setBelegart(eingangsrechnungDto.getEingangsrechnungartCNr());
		setBelegnummer(eingangsrechnungDto.getCNr());
	}

	protected BelegdataBase(RechnungDto rechnungDto) {
		setRechnungDto(rechnungDto);
		setBelegart(rechnungDto.getBelegartCNr());
		setBelegnummer(rechnungDto.getCNr());
	}
	
	public String getBelegart() {
		return belegart;
	}

	public void setBelegart(String belegart) {
		this.belegart = belegart != null ? belegart.trim() : null ;
	}

	public String getBelegnummer() {
		return belegnummer;
	}

	public void setBelegnummer(String belegnummer) {
		this.belegnummer = belegnummer;
	}

	public EingangsrechnungDto getEingangsrechnungDto() {
		return eingangsrechnungDto;
	}

	public void setEingangsrechnungDto(EingangsrechnungDto eingangsrechnungDto) {
		this.eingangsrechnungDto = eingangsrechnungDto;
	}

	public RechnungDto getRechnungDto() {
		return rechnungDto;
	}

	public void setRechnungDto(RechnungDto rechnungDto) {
		this.rechnungDto = rechnungDto;
	}
}
