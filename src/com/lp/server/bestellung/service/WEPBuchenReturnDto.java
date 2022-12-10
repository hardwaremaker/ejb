package com.lp.server.bestellung.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeMap;

public class WEPBuchenReturnDto implements Serializable {

	public WEPBuchenReturnDto(ArrayList<Object[]> alDataForFehlmengenAufloesen,
			TreeMap<String,Integer>  tmArtikelIIdsMitKommentar) {
		super();
		this.alDataForFehlmengenAufloesen = alDataForFehlmengenAufloesen;
		this.tmArtikelIIdsMitKommentar = tmArtikelIIdsMitKommentar;
	}

	public ArrayList<Object[]> getAlDataForFehlmengenAufloesen() {
		return alDataForFehlmengenAufloesen;
	}

	public TreeMap<String,Integer> getTmArtikelIIdsMitKommentar() {
		return tmArtikelIIdsMitKommentar;
	}

	ArrayList<Object[]> alDataForFehlmengenAufloesen = null;
	
	TreeMap<String,Integer> tmArtikelIIdsMitKommentar=new TreeMap<String,Integer> ();
	
}
