package com.lp.server.artikel.service;

public class ArtikelsperrenSperrenDto extends ArtikelsperrenDto {
	private static final long serialVersionUID = 2771868657290338076L;

	private SperrenDto sperrenDto;
	
	public SperrenDto getSperrenDto() {
		return sperrenDto;
	}
	
	public void setSperrenDto(SperrenDto sperrenDto) {
		this.sperrenDto = sperrenDto;
	}
}
