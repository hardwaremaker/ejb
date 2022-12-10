package com.lp.server.angebotstkl.service;

import java.io.Serializable;
import java.util.ArrayList;

public class EkagLieferantoptimierenDto implements Serializable {

	private EinkaufsangebotDto ekagDto = null;

	private ArrayList<EinkaufsangebotpositionDtoFuerOptimieren> pos;

	public EinkaufsangebotDto getEkagDto() {
		return ekagDto;
	}

	public void setEkagDto(EinkaufsangebotDto ekagDto) {
		this.ekagDto = ekagDto;
	}

	public ArrayList<EinkaufsangebotpositionDtoFuerOptimieren> getPos() {
		return pos;
	}

	public void setPos(ArrayList<EinkaufsangebotpositionDtoFuerOptimieren> pos) {
		this.pos = pos;
	}

	public EkagLieferantoptimierenDto(EinkaufsangebotDto ekagDto) {

	}

}
