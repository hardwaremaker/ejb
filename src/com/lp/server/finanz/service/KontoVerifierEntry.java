package com.lp.server.finanz.service;

import java.io.Serializable;
import java.util.Map;

public class KontoVerifierEntry implements Map.Entry<SteuerkategorieEntry, SteuerkategorieEntry>, Serializable {
	private static final long serialVersionUID = 120896016706680017L;
	private SteuerkategorieEntry key ;
	private SteuerkategorieEntry value ;
	
	public KontoVerifierEntry(SteuerkategorieEntry knownEntry, SteuerkategorieEntry duplicateEntry) {
		this.key = knownEntry ;
		this.value = duplicateEntry ;
	}
	
	@Override
	public SteuerkategorieEntry getKey() {
		return key ;
	}		

	@Override
	public SteuerkategorieEntry getValue() {
		return value ;
	}
	
	@Override
	public SteuerkategorieEntry setValue(SteuerkategorieEntry arg0) {
		SteuerkategorieEntry previousEntry = this.value ;
		this.value = arg0 ;
		return previousEntry ;
	}
	
	private String toKontoString(SteuerkategorieEntry entry) {
		return "Konto " + entry.getKontoCnr() + ", \"" + entry.getKontoBez() + "\"" ; 
	}
	
	private String toStringImpl(String prefix, SteuerkategorieEntry entry) {
		SteuerkategorieDto katDto = entry.getKategorie() ;
		return prefix   
			+ ": " + entry.getKontoVerwendung() 
			+ " in Kategorie: " + katDto.getCNr().trim() + " \"" + katDto.getCBez().trim() + "\"."
			+ ", Mwst.Bez.: \"" + entry.getMwstsatzBezeichnung() + "\" (Id:" + entry.getKategorieKonto().getMwstsatzbezIId() + ")" 
			; 
	}
	
	public String toString() {
		return toKontoString(getKey()) 
			+ ", " + toStringImpl("definiert f\u00fcr", getKey()) 
			+ " " + toStringImpl("Verwendet in", getValue()) ;
	}
}
