package com.lp.server.artikel.service;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;

public class KopfGruppeMitUntergruppen implements Serializable {
	private static final long serialVersionUID = 1L;

	private HashSet<Integer> hs_i_id_gruppe = new HashSet<Integer>();

	protected KopfGruppeMitUntergruppen() {
	}

	public KopfGruppeMitUntergruppen(Integer kopfIId,
			HashSet<Integer> untergruppen) {
		hs_i_id_gruppe = untergruppen;
		hs_i_id_gruppe.add(kopfIId);
	}


	public void addId(Integer i_id) {
		hs_i_id_gruppe.add(i_id);
	}

	public String erzeuge_IN_Fuer_Query() {
		String s = "(";

		Iterator<Integer> it = hs_i_id_gruppe.iterator();

		while (it.hasNext()) {
			Integer i_id = it.next();

			s += i_id;

			if (it.hasNext()) {
				s += ",";
			}
		}

		s += ")";

		return s;
	}

}
