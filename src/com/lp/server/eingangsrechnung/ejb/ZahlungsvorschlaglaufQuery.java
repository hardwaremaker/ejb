package com.lp.server.eingangsrechnung.ejb;

import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;

public class ZahlungsvorschlaglaufQuery {

	public static final String ByMandantCnrTGespeichertIsNull = "ZahlungsvorschlaglaufFindByMandantCnrTGespeichertIsNull";
	
	public static HvTypedQuery<Zahlungsvorschlaglauf> byMandantCnrTGespeichertIsNull(String mandantCnr, EntityManager em) {
		HvTypedQuery<Zahlungsvorschlaglauf> theQuery = new HvTypedQuery<Zahlungsvorschlaglauf>(
				em.createNamedQuery(ByMandantCnrTGespeichertIsNull));
		theQuery.setParameter("mandant", mandantCnr);
		return theQuery;
	}
	
	public static List<Zahlungsvorschlaglauf> listByMandantCnrTGespeichertIsNull(String mandantCnr, EntityManager em) {
		return byMandantCnrTGespeichertIsNull(mandantCnr, em).getResultList();
	}
}
