package com.lp.server.bestellung.service;

import java.io.Serializable;
import java.util.ArrayList;

public class RueckgabeUeberleitungDto implements Serializable {


	Boolean bErfolgreich=false;
	java.util.TreeMap tmBetroffeneArtikel=null;
	
	public Boolean getbErfolgreich() {
		return bErfolgreich;
	}

	public void setbErfolgreich(Boolean bErfolgreich) {
		this.bErfolgreich = bErfolgreich;
	}

	public java.util.TreeMap getTmBetroffeneArtikel() {
		return tmBetroffeneArtikel;
	}

	public RueckgabeUeberleitungDto(boolean bErfolgreich, java.util.TreeMap tmBetroffeneArtikel){
		this.tmBetroffeneArtikel=tmBetroffeneArtikel;
		this.bErfolgreich=bErfolgreich;
	}
	public RueckgabeUeberleitungDto(boolean bErfolgreich, java.util.TreeMap tmBetroffeneArtikel, ArrayList<BestellungDto> angelegteBestellungen){
		this.tmBetroffeneArtikel=tmBetroffeneArtikel;
		this.bErfolgreich=bErfolgreich;
		this.angelegteBestellungen=angelegteBestellungen;
	}
	
	ArrayList<BestellungDto> angelegteBestellungen = new ArrayList();

	public ArrayList<BestellungDto> getAngelegteBestellungen() {
		return angelegteBestellungen;
	}
	
	public RueckgabeUeberleitungDto(){
		this.tmBetroffeneArtikel=new java.util.TreeMap();
		this.angelegteBestellungen=new ArrayList<BestellungDto>();
	}
	
	
	public void add(RueckgabeUeberleitungDto rueckgabeUeberleitungDto) {
		tmBetroffeneArtikel.putAll(rueckgabeUeberleitungDto.getTmBetroffeneArtikel());
		angelegteBestellungen.addAll(rueckgabeUeberleitungDto.getAngelegteBestellungen());
	}
}
