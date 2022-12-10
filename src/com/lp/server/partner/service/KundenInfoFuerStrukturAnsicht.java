package com.lp.server.partner.service;

import java.io.Serializable;

public class KundenInfoFuerStrukturAnsicht implements Serializable {
	KundeDto kundeDto = null;
	Boolean bHasLieferadresse=false;
	
	public KundenInfoFuerStrukturAnsicht(KundeDto kundeDto,Boolean bHasLieferadresse) {
		this.kundeDto=kundeDto;
		this.bHasLieferadresse=bHasLieferadresse;
	}

	public KundeDto getKundeDto() {
		return kundeDto;
	}

	public void setKundeDto(KundeDto kundeDto) {
		this.kundeDto = kundeDto;
	}

	public Boolean getBHasLieferadresse() {
		return bHasLieferadresse;
	}

	public void setBHasLieferadresse(Boolean bHasLieferadresse) {
		this.bHasLieferadresse = bHasLieferadresse;
	}
	
}
