package com.lp.server.rechnung.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class DauerUndZeitraumDto implements Serializable {

	private BigDecimal bdDauer = BigDecimal.ZERO;

	boolean bVerdichtetNachArtikel = true;
	
	public void setBdDauer(BigDecimal bdDauer) {
		this.bdDauer = bdDauer;
	}

	public BigDecimal getBdDauer() {
		return bdDauer;
	}

	Map<Integer,ArrayList<ZeitVonBisUndDauer>> mZeitraeume = new LinkedHashMap<Integer,ArrayList<ZeitVonBisUndDauer>>();
	
	
	public Map<Integer,ArrayList<ZeitVonBisUndDauer>> getMZeitraeume() {
		return mZeitraeume;
	}
	public ArrayList<ZeitVonBisUndDauer> getAlZeitraeume(Integer zeitdatenIId) {
		return mZeitraeume.get(zeitdatenIId);
	}

	public DauerUndZeitraumDto() {

	}

	public DauerUndZeitraumDto(BigDecimal bdDauer) {

		this.bdDauer = bdDauer;
	}

	public void add2Zeitraeume(Integer zeitdatenIId, Timestamp tVon, Timestamp tBis, BigDecimal bdDauer) {

		 ArrayList<ZeitVonBisUndDauer> alZeitraeume=null;
		if(mZeitraeume.containsKey(zeitdatenIId)){
			alZeitraeume=mZeitraeume.get(zeitdatenIId);
		}else {
			alZeitraeume=new ArrayList<ZeitVonBisUndDauer>();
		}
		
		ZeitVonBisUndDauer tTemp = new ZeitVonBisUndDauer();
		tTemp.tVon=tVon;
		tTemp.tBis=tBis;
		tTemp.bdStunden=bdDauer;
		
		
		if (!alZeitraeume.contains(tTemp)) {
			alZeitraeume.add(tTemp);
		}
		
		mZeitraeume.put(zeitdatenIId, alZeitraeume);
	}

	public void add2Dauer(BigDecimal bdDauer) {
		this.bdDauer = this.bdDauer.add(bdDauer);
	}
}
