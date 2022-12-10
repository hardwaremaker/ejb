package com.lp.server.angebotstkl.service;

import java.io.Serializable;

public class EinkaufsangebotpositionDtoFuerOptimieren  implements Serializable {

	
	
	
	public EinkaufsangebotpositionDto getEinkaufsangebotpositionDto() {
		return ekagPosDto;
	}
	public void setEinkaufsangebotpositionDto(EinkaufsangebotpositionDto ekagPosDto) {
		this.ekagPosDto = ekagPosDto;
	}
	private EinkaufsangebotpositionDto ekagPosDto= null;
	 
	private PositionlieferantDto guenstigsterLieferantMenge1 = null;
	private PositionlieferantDto guenstigsterLieferantMenge2 = null;
	private PositionlieferantDto guenstigsterLieferantMenge3 = null;
	private PositionlieferantDto guenstigsterLieferantMenge4 = null;
	private PositionlieferantDto guenstigsterLieferantMenge5 = null;
	public PositionlieferantDto getGuenstigsterLieferantMenge1() {
		return guenstigsterLieferantMenge1;
	}
	public void setGuenstigsterLieferantMenge1(PositionlieferantDto guenstigsterLieferantMenge1) {
		this.guenstigsterLieferantMenge1 = guenstigsterLieferantMenge1;
	}
	public PositionlieferantDto getGuenstigsterLieferantMenge2() {
		return guenstigsterLieferantMenge2;
	}
	public void setGuenstigsterLieferantMenge2(PositionlieferantDto guenstigsterLieferantMenge2) {
		this.guenstigsterLieferantMenge2 = guenstigsterLieferantMenge2;
	}
	public PositionlieferantDto getGuenstigsterLieferantMenge3() {
		return guenstigsterLieferantMenge3;
	}
	public void setGuenstigsterLieferantMenge3(PositionlieferantDto guenstigsterLieferantMenge3) {
		this.guenstigsterLieferantMenge3 = guenstigsterLieferantMenge3;
	}
	public PositionlieferantDto getGuenstigsterLieferantMenge4() {
		return guenstigsterLieferantMenge4;
	}
	public void setGuenstigsterLieferantMenge4(PositionlieferantDto guenstigsterLieferantMenge4) {
		this.guenstigsterLieferantMenge4 = guenstigsterLieferantMenge4;
	}
	public PositionlieferantDto getGuenstigsterLieferantMenge5() {
		return guenstigsterLieferantMenge5;
	}
	public void setGuenstigsterLieferantMenge5(PositionlieferantDto guenstigsterLieferantMenge5) {
		this.guenstigsterLieferantMenge5 = guenstigsterLieferantMenge5;
	}
	
}
