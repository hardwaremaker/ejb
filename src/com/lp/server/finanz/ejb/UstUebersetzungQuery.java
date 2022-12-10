package com.lp.server.finanz.ejb;

import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;

public class UstUebersetzungQuery {

	public final static String ByMandant = "EingangsrechnungSteuercodeFindByMandant";
	
	public static HvTypedQuery<UstUebersetzung> byMandant(EntityManager em, String mandant) {
		HvTypedQuery<UstUebersetzung> theQuery = new HvTypedQuery<UstUebersetzung>(em.createNamedQuery(ByMandant));
		theQuery.setParameter("mandant", mandant);
		return theQuery;
	}
	
	public static List<UstUebersetzung> listByMandant(EntityManager em, String mandant) {
		return byMandant(em, mandant).getResultList();
	}
}
