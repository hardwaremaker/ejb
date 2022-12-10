package com.lp.server.eingangsrechnung.ejb;

import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;

public class EingangsrechnungzahlungQuery {

	public static final String ByIAuszug = "EingangsrechnungzahlungFindByIAuszug";
	
	public static List<Eingangsrechnungzahlung> byIAuszug(EntityManager em, Integer iAuszug) {
		return HvTypedQuery.<Eingangsrechnungzahlung>namedQuery(em, ByIAuszug, iAuszug).getResultList();
	}
}
