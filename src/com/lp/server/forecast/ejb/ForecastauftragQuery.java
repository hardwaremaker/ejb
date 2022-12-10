package com.lp.server.forecast.ejb;

import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;

public class ForecastauftragQuery {
	public static final String ByForecastlieferadresseStatusCNr = "ForecastauftragFindByForecastlieferadresseIIdStatusCNr";

	public static HvTypedQuery<Forecastauftrag> byForecastlieferadresseIIdStatusCNr(EntityManager em, Integer fclieferadresseIId, 
			 String statusCNr) {
		HvTypedQuery<Forecastauftrag> query = new HvTypedQuery<Forecastauftrag>(
				em.createNamedQuery(ForecastauftragQuery.ByForecastlieferadresseStatusCNr));
		query.setParameter(1, fclieferadresseIId);
		query.setParameter(2, statusCNr);
		
		return query;
	}

	public static List<Forecastauftrag> listByForecastlieferadresseIIdStatusCNr(EntityManager em, Integer fclieferadresseIId, 
			 String statusCNr) {
		return byForecastlieferadresseIIdStatusCNr(em, fclieferadresseIId, statusCNr).getResultList();
	}

}
