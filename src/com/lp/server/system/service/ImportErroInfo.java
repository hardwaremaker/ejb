package com.lp.server.system.service;

import java.io.Serializable;
import java.util.ArrayList;

public class ImportErroInfo implements Serializable {

	private byte[] CRLFAscii = { 13, 10 };

	private ArrayList<String> errors = new ArrayList<String>();

	private ArrayList<String> info = new ArrayList<String>();

	public void add2Error(String s) {
		errors.add(s);
	}

	public void add2Info(String s) {
		info.add(s);
	}

	public boolean sindFehlerVorhanden() {
		if (errors.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean sindInfosVorhanden() {
		if (info.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean sindFehlerOoderInfosVorhanden() {
		if (info.size() > 0 || errors.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public String getErrorsAsString() {

		String zeilen = "";

		for (String zeile : errors) {
			zeilen += zeile + new String(CRLFAscii);
		}

		return zeilen;

	}

	public String getInfosAsString() {

		String zeilen = "";

		for (String zeile : info) {
			zeilen += zeile + new String(CRLFAscii);
		}

		return zeilen;

	}

	public String getErrorAndInfosAsString() {

		String zeilen = "";

		if (sindFehlerVorhanden()) {
			zeilen += "Fehler:" + new String(CRLFAscii);

			for (String zeile : errors) {
				zeilen += zeile + new String(CRLFAscii);
			}
		}
		if (sindInfosVorhanden()) {
			zeilen += "Info:" + new String(CRLFAscii);

			for (String zeile : info) {
				zeilen += zeile + new String(CRLFAscii);
			}
		}
		return zeilen;

	}

}
