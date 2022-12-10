package com.lp.server.personal.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.TreeMap;

import com.lp.util.Helper;

public class ReisezeitenEinerPersonDto implements Serializable {

	
	public ReisezeitenEinerPersonDto(){
		
	}
	
	ArrayList<ZeilensummenEinesTages> zeilensummenEinesTages = new ArrayList<ZeilensummenEinesTages>();
	
	public ArrayList<ZeilensummenEinesTages> getZeilensummenEinesTages() {
		return zeilensummenEinesTages;
	}

	public void setZeilensummenEinesTages(ArrayList<ZeilensummenEinesTages> zeilensummenEinesTages) {
		this.zeilensummenEinesTages = zeilensummenEinesTages;
	}

	ArrayList<AbschnittEinerReiseDto> alAbschnitteEinerReise = new ArrayList<AbschnittEinerReiseDto>();

	public void setAlAbschnitteEinerReise(ArrayList<AbschnittEinerReiseDto> alAbschnitteEinerReise) {
		this.alAbschnitteEinerReise = alAbschnitteEinerReise;
	}

	public ArrayList<AbschnittEinerReiseDto> getAlAbschnitteEinerReise() {
		return alAbschnitteEinerReise;
	}

}
