package com.lp.server.rechnung.ejb;

import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;

public class LastschriftvorschlagQuery {

	public static final String ByCAuftraggeberreferenz = "LastschriftvorschlagFindByCAuftraggeberreferenz";
	public static final String ByMahnlaufIId = "LastschriftvorschlagFindByMahnlaufIId";
	
	public LastschriftvorschlagQuery() {
	}

	public static HvTypedQuery<Lastschriftvorschlag> byCAuftraggeberreferenz(
			EntityManager em, String auftraggeberreferenz) {
		HvTypedQuery<Lastschriftvorschlag> query = new HvTypedQuery<Lastschriftvorschlag>(
				em.createNamedQuery(ByCAuftraggeberreferenz));
		query.setParameter("referenz", auftraggeberreferenz);
		
		return query;
	}
	
	public static List<Lastschriftvorschlag> listByCAuftraggeberreferenz(
			EntityManager em, String auftraggeberreferenz) {
		return byCAuftraggeberreferenz(em, auftraggeberreferenz).getResultList();
	}
	
	public static HvTypedQuery<Lastschriftvorschlag> byMahnlaufIId(
			EntityManager em, Integer mahnlaufIId) {
		HvTypedQuery<Lastschriftvorschlag> query = new HvTypedQuery<Lastschriftvorschlag>(
				em.createNamedQuery(ByMahnlaufIId));
		query.setParameter("mahnlaufIId", mahnlaufIId);
		
		return query;
	}
	
	public static List<Lastschriftvorschlag> listByMahnlaufIId(
			EntityManager em, Integer mahnlaufIId) {
		return byMahnlaufIId(em, mahnlaufIId).getResultList();
	}
}
