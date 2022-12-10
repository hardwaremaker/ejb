package com.lp.server.fertigung.ejbfac;

import com.lp.server.fertigung.service.LosablieferungDto;
import com.lp.server.system.service.TheClientDto;

public class CreateLosablieferungModel {

	private LosablieferungDto losablieferungDto;
	private boolean bErledigt = false;
	private boolean bKommissionierterminal = false;
	private TheClientDto theClientDto;
	
	public CreateLosablieferungModel(LosablieferungDto losablieferungDto, TheClientDto theClientDto) {
		this.losablieferungDto = losablieferungDto;
		this.theClientDto = theClientDto;
	}
	
	public boolean isKommissionierterminal() {
		return bKommissionierterminal;
	}

	public void setBKommissionierterminal(boolean value) {
		bKommissionierterminal = value;
	}
	
	public Integer losIId() {
		return losablieferungDto.getLosIId();
	}
	
	public LosablieferungDto dto() {
		return losablieferungDto;
	}
	
	public boolean isErledigt() {
		return bErledigt;
	}
	
	public void setErledigt(boolean value) {
		bErledigt = value;
	}
	
	public TheClientDto theClientDto() {
		return theClientDto;
	}
	
	public String mandantCnr() {
		return theClientDto().getMandant();
	}

}
