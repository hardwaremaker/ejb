package com.lp.server.fertigung.service;

import java.io.Serializable;
import java.util.ArrayList;

public class ImportPruefergebnis implements Serializable {

	/**
	 * 
	 */
	ArrayList<String> alFehler = new ArrayList<String>();
	ArrayList<String> alWarnung = new ArrayList<String>();

	private static final long serialVersionUID = 1L;
	ArrayList<LosDto> losDtos = new ArrayList<LosDto>();

	public ArrayList<LosDto> getLosDtos() {
		return losDtos;
	}

	public boolean hatFehler() {

		if (alFehler.size() > 0) {
			return true;
		} else {
			return false;
		}

	}

	public boolean hatWarnings() {
		if (alWarnung.size() > 0) {
			return true;
		} else {
			return false;
		}

	}

	public void addLosDto(LosDto losDto) {
		losDtos.add(losDto);

	}

	public void addFehler(String fehler) {
		alFehler.add(fehler);

	}

	
	
	public ArrayList<String> getAlFehler() {
		return alFehler;
	}

	public ArrayList<String> getAlWarnung() {
		return alWarnung;
	}

	public void addWarnung(String warnung) {
		alWarnung.add(warnung);

	}
}
