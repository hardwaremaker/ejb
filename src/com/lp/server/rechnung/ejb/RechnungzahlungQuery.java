package com.lp.server.rechnung.ejb;

import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;

public class RechnungzahlungQuery {

	public final static String ByIAuszug = "RechnungzahlungfindByIAuszug";

	public static List<Rechnungzahlung> byIAuszug(EntityManager em, Integer iAuszug) {
		return HvTypedQuery.<Rechnungzahlung>namedQuery(em, ByIAuszug, iAuszug).getResultList();
	}
}
