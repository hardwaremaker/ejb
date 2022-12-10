package com.lp.server.forecast.ejb;

import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;

public class ForecastpositionQuery {

	public final static String ByForecastauftragIId = "ForecastpositionFindByForecastauftragIId";
	public final static String ByForecastauftragIIdArtikelIId = "ForecastpositionFindByForecastauftragIIdArtikelIId";
	public final static String CountForecastauftragIIdArtikelIId = "CountForecastpositionFindByForecastauftragIIdArtikelIId";
	
	public static HvTypedQuery<Forecastposition> byForecastauftragIId(EntityManager em, Integer forecastauftragIId) {
		HvTypedQuery<Forecastposition> query = new HvTypedQuery<Forecastposition>(
				em.createNamedQuery(ForecastpositionQuery.ByForecastauftragIId));
		query.setParameter(1, forecastauftragIId);
		return query;
	}

	public static HvTypedQuery<Forecastposition> byForecastauftragIIdArtikelId(
			EntityManager em, Integer forecastauftragId, Integer artikelId) {
		HvTypedQuery<Forecastposition> query = new HvTypedQuery<Forecastposition>(
				em.createNamedQuery(ForecastpositionQuery.ByForecastauftragIIdArtikelIId));
		return query
			.setParameter(1, forecastauftragId)
			.setParameter(2, artikelId);	
	}
	
	public static HvTypedQuery<Long> countForecastauftragIIdArtikelId(
			EntityManager em, Integer forecastauftragId, Integer artikelId) {
		HvTypedQuery<Long> query = new HvTypedQuery<Long>(
				em.createNamedQuery(ForecastpositionQuery.ByForecastauftragIId));
		return query
			.setParameter(1, forecastauftragId)
			.setParameter(2, artikelId);	
	}
	public static List<Forecastposition> listByForecastauftragIId(EntityManager em, Integer forecastauftragIId) {
		return byForecastauftragIId(em, forecastauftragIId).getResultList();
	}
	
	public static List<Forecastposition> listByForecastauftragIIdArtikelIId(
			EntityManager em, Integer forecastauftragId, Integer artikelId) {
		return byForecastauftragIIdArtikelId(em, forecastauftragId, artikelId).getResultList();
	}
	
	public static Long countByForecastauftragIIdArtikelIId(
			EntityManager em, Integer forecastauftragId, Integer artikelId) {
		return countForecastauftragIIdArtikelId(em, forecastauftragId, artikelId).getSingleResult();
	}
}
