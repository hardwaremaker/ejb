package com.lp.server.forecast.ejb;

import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;

public class LinienabrufQuery {
	
	public static final String ByForecastpositionIId = "LinienabrufFindByForecastpositionIId";
	public static final String CountForecastpositionIId = "CountLinienabrufFindByForecastpositionIId";
	
	public static HvTypedQuery<Linienabruf> byForecastpositionIId(EntityManager em, Integer forecastpositionIId) {
		HvTypedQuery<Linienabruf> theQuery = new HvTypedQuery<Linienabruf>(em.createNamedQuery(ByForecastpositionIId));
		theQuery.setParameter("forecastpositionid", forecastpositionIId);
		return theQuery;
	}

	public static HvTypedQuery<Long> countForecastpositionIId(EntityManager em, Integer forecastpositionIId) {
		HvTypedQuery<Long> theQuery = new HvTypedQuery<Long>(em.createNamedQuery(CountForecastpositionIId));
		theQuery.setParameter("forecastpositionid", forecastpositionIId);
		return theQuery;
	}

	public static List<Linienabruf> listByForecastpositionIId(EntityManager em, Integer forecastpositionIId) {
		return byForecastpositionIId(em, forecastpositionIId).getResultList();
	}
	
	public static Long countByForecastpositionIId(EntityManager em, Integer forecastpositionIId) {
		return countForecastpositionIId(em, forecastpositionIId).getSingleResult();
	}
}
