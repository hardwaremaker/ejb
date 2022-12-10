package com.lp.server.partner.ejb;

import java.util.List;

import javax.persistence.EntityManager;

public class PartnerbankQuery {
	
	public final static String ByIban = "PartnerbankfindByIban";
	public final static String PartnerIIdsByIban = "PartnerbankPartnerIIdsByIban";

	public static final String ByESRUndKontonumer = "PartnerbankFindByESRUndKontonumer";
	public static HvTypedQuery<Partnerbank> byESRUndKontonummer(EntityManager em, String esr, String kontonummer) {
		HvTypedQuery<Partnerbank> theQuery = new HvTypedQuery<Partnerbank>(em.createNamedQuery(ByESRUndKontonumer));
		theQuery.setParameter("esr", esr);
		theQuery.setParameter("cKtonr", kontonummer);
		
		return theQuery;
	}
	
	public static HvTypedQuery<Partnerbank> byIban(EntityManager em, String iban) {
		HvTypedQuery<Partnerbank> theQuery = new HvTypedQuery<Partnerbank>(em.createNamedQuery(ByIban));
		theQuery.setParameter("iban", iban);
		return theQuery;
	}

	public static List<Partnerbank> listByIban(EntityManager em, String iban) {
		return byIban(em, iban).getResultList();
	}
	
	public static HvTypedQuery<Integer> partnerIIdsByIban(EntityManager em, String iban) {
		HvTypedQuery<Integer> theQuery = new HvTypedQuery<Integer>(em.createNamedQuery(PartnerIIdsByIban));
		theQuery.setParameter("iban", iban);
		return theQuery;
	}
	
	public static List<Integer> listPartnerIIdsByIban(EntityManager em, String iban) {
		return partnerIIdsByIban(em, iban).getResultList();
	}
	
	public static List<Partnerbank> listByESRUndKontonummer(EntityManager em, String esr, String kontonummer) {
		return byESRUndKontonummer(em, esr,kontonummer).getResultList();
	}

}
