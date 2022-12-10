package com.lp.server.lieferschein.ejb;

import java.sql.Date;
import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;

public class LieferscheinQuery {
	
	public static final String ByKundeIIdLieferadresseMandantCNrBelegdatum = "LieferscheinfindByKundeIIdLieferadresseMandantCNrBelegdatum";
	public static final String ByKundeIIdLieferadresseMandantCNrStatusCNr = "LieferscheinfindByKundeIIdLieferadresseMandantCNrStatusCNr";
	
	public static HvTypedQuery<Lieferschein> byKundeIIdLieferadresseMandantCNrBelegdatum(EntityManager em, 
			Integer kundeIIdLieferadresse, String mandantCnr, Date belegdatum) {
		HvTypedQuery<Lieferschein> theQuery = new HvTypedQuery<Lieferschein>(em.createNamedQuery(ByKundeIIdLieferadresseMandantCNrBelegdatum));
		theQuery.setParameter("kundeId", kundeIIdLieferadresse);
		theQuery.setParameter("mandant", mandantCnr);
		theQuery.setParameter("belegdatum", belegdatum);
		
		return theQuery;
	}
	
	public static List<Lieferschein> listByKundeIIdLieferadresseMandantCNrBelegdatum(EntityManager em, 
			Integer kundeIIdLieferadresse, String mandantCnr, Date belegdatum) {
		return byKundeIIdLieferadresseMandantCNrBelegdatum(em, kundeIIdLieferadresse, mandantCnr, belegdatum).getResultList();
	}

	public static HvTypedQuery<Lieferschein> byKundeIIdLieferadresseMandantCNrStatusCNr(EntityManager em, 
			Integer kundeIIdLieferadresse, String mandantCnr, String statusCnr) {
		HvTypedQuery<Lieferschein> theQuery = new HvTypedQuery<Lieferschein>(em.createNamedQuery(ByKundeIIdLieferadresseMandantCNrStatusCNr));
		theQuery.setParameter("kundeId", kundeIIdLieferadresse);
		theQuery.setParameter("mandant", mandantCnr);
		theQuery.setParameter("statusCnr", statusCnr);
		
		return theQuery;
	}
	
	public static List<Lieferschein> listByKundeIIdLieferadresseMandantCNrStatusCNr(EntityManager em, 
			Integer kundeIIdLieferadresse, String mandantCnr, String statusCnr) {
		return byKundeIIdLieferadresseMandantCNrStatusCNr(em, kundeIIdLieferadresse, mandantCnr, statusCnr).getResultList();
	}

}
