package com.lp.server.system.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

import com.lp.util.EJBExceptionLP;

public class JxlImportErgebnis implements Serializable {

	private int iAnzahlErfolgreichImportiert = 0;

	public int getiAnzahlErfolgreichImportiert() {
		return iAnzahlErfolgreichImportiert;
	}

	public TreeMap<Integer, ArrayList<String>> getFehler() {
		return fehler;
	}

	public TreeMap<Integer, Throwable> getExceptions() {
		return exceptions;
	}

	private TreeMap<Integer, ArrayList<String>> fehler = new TreeMap<Integer, ArrayList<String>>();

	private TreeMap<Integer, Throwable> exceptions = new TreeMap<Integer, Throwable>();

	public JxlImportErgebnis(TreeMap<Integer, ArrayList<String>> fehler, TreeMap<Integer, Throwable> exceptions,
			int iAnzahlErfolgreichImportiert) {

		this.fehler = fehler;
		this.exceptions = exceptions;
		this.iAnzahlErfolgreichImportiert = iAnzahlErfolgreichImportiert;

	}
	

	public String getAlleFehlerAlsString() {
		byte[] CRLFAscii = { 13, 10 };
		String s = new String();
		Iterator it = fehler.keySet().iterator();

		while (it.hasNext()) {

			Integer iZeile = (Integer) it.next();

			ArrayList<String> al = fehler.get(iZeile);

			for (int i = 0; i < al.size(); i++) {
				s += al.get(i) + " Zeile " + (iZeile + 1) + new String(CRLFAscii);
			}

		}
		return s;
	}
	

}
