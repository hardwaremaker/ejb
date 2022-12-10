package com.lp.server.fertigung.ejb;

import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;

public class LosQuery {

	public final static String ByForecastpositionIId = "LosFindByForecastpositionIId";
	public final static String ByStuecklisteIIdFertigungsgruppeIIdStatusCnr = "LosfindByStuecklisteIIdFertigungsgruppeIIdStatusCnr";
	
	public static HvTypedQuery<Los> byForecastpositionIId(EntityManager em, Integer forecastpositionIId) {
		HvTypedQuery<Los> theQuery = new HvTypedQuery<Los>(em.createNamedQuery(ByForecastpositionIId));
		theQuery.setParameter("id", forecastpositionIId);
		return theQuery;
	}

	public static Los resultByForecastpositionIId(EntityManager em, Integer forecastpositionIId) {
		return byForecastpositionIId(em, forecastpositionIId).getSingleResultNoEx();
	}

	public static HvTypedQuery<Los> byStuecklisteIIdStatusCnr(EntityManager em, 
			Integer stuecklisteIId, Integer fertigungsgruppeIId, List<String> statusCnr) {
		HvTypedQuery<Los> theQuery = new HvTypedQuery<Los>(em.createNamedQuery(ByStuecklisteIIdFertigungsgruppeIIdStatusCnr));
		theQuery.setParameter("id", stuecklisteIId);
		theQuery.setParameter("fertigungsgruppeId", fertigungsgruppeIId);
		theQuery.setParameter("stati", statusCnr);
		return theQuery;
	}
	
	public static List<Los> listByStuecklisteIIdStatusCnr(EntityManager em, 
			Integer stuecklisteIId, Integer fertigungsgruppeIId, List<String> statusCnr) {
		return byStuecklisteIIdStatusCnr(em, stuecklisteIId, fertigungsgruppeIId, statusCnr).getResultList();
	}
}
