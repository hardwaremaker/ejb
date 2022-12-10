package com.lp.server.artikel.service;


/**
 * Ein Adapter zum Artikelkommentar um Gemeinsamkeiten in Soap-Daten und BmeCat abzudecken.
 * @author Gerold
 */
public class WebArtikelkommentarDto extends ArtikelkommentarDto {
	private static final long serialVersionUID = 1732504007491579474L;

	private ArtikelkommentarDto kommentarDto ;
	
	public WebArtikelkommentarDto() {
		kommentarDto = new ArtikelkommentarDto() ;
	}
	
	public WebArtikelkommentarDto(ArtikelkommentarDto artikelkommentarDto) {
		this.kommentarDto = artikelkommentarDto ;
	}
	
	@Override
	public Integer getArtikelIId() {
		return kommentarDto.getArtikelIId();
	}
	
	@Override
	public Integer getArtikelkommentarartIId() {
		return kommentarDto.getArtikelkommentarartIId();
	}

	@Override
	public ArtikelkommentardruckDto[] getArtikelkommentardruckDto() {
		return kommentarDto.getArtikelkommentardruckDto();
	}	
	
	@Override
	public ArtikelkommentarsprDto getArtikelkommentarsprDto() {
		return kommentarDto.getArtikelkommentarsprDto();
	}
	
	@Override
	public Short getBDefaultbild() {
		return kommentarDto.getBDefaultbild();
	}

	@Override
	public String getDatenformatCNr() {
		return kommentarDto.getDatenformatCNr();
	}
	
	@Override
	public Integer getIArt() {
		return kommentarDto.getIArt();
	}
	
	@Override
	public Integer getIId() {
		return kommentarDto.getIId();
	}
	
	@Override
	public Integer getISort() {
		return kommentarDto.getISort();
	}
	
	@Override
	public void setArtikelIId(Integer artikelIId) {
		kommentarDto.setArtikelIId(artikelIId);
	}

	@Override
	public void setArtikelkommentarartIId(Integer artikelkommentarartIId) {
		kommentarDto.setArtikelkommentarartIId(artikelkommentarartIId);
	}
	
	@Override
	public void setArtikelkommentardruckDto(
			ArtikelkommentardruckDto[] artikelkommentardruckDto) {
		kommentarDto.setArtikelkommentardruckDto(artikelkommentardruckDto);
	}
	
	@Override
	public void setArtikelkommentarsprDto(
			ArtikelkommentarsprDto artikelkommentarsprDto) {
		kommentarDto.setArtikelkommentarsprDto(artikelkommentarsprDto);
	}

	@Override
	public void setBDefaultbild(Short bDefaultbild) {
		kommentarDto.setBDefaultbild(bDefaultbild);
	}

	@Override
	public void setDatenformatCNr(String datenformatCNr) {
		kommentarDto.setDatenformatCNr(datenformatCNr);
	}

	@Override
	public void setIArt(Integer iArt) {
		kommentarDto.setIArt(iArt);
	}

	@Override
	public void setIId(Integer iId) {
		kommentarDto.setIId(iId);
	}	
	
	@Override
	public void setISort(Integer iSort) {
		kommentarDto.setISort(iSort);
	}

	@Override
	public String toString() {
		return kommentarDto.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		return kommentarDto.equals(obj);
	}
	
	@Override
	public int hashCode() {
		return kommentarDto.hashCode();
	}
	
	public String getImageUrl() {
		ArtikelkommentarsprDto sprDto = getArtikelkommentarsprDto() ;
		if(sprDto == null) return null ;

		return getIId().toString() + "-" + sprDto.getCDateiname() ;		
	}
}
