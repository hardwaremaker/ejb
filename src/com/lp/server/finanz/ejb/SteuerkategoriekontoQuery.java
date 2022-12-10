package com.lp.server.finanz.ejb;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;
import com.lp.server.util.HvOptional;

public class SteuerkategoriekontoQuery {
	public static final String BySteuerkategorieIId = "SteuerkategoriekontoBySteuerkategorieIId";
	public static final String BySteuerkategorieIIdAll = "SteuerkategoriekontoBySteuerkategorieIIdAll";
	public static final String BySteuerkategorieIIdWithMwstsatzBezIId = "SteuerkategoriekontoBySteuerkategorieIIdandMwStSatzBeziid";
	public static final String ByKontoIIdWithMwstsatzBezIId = "SteuerkategoriekontoByKontoIIdandMwStSatzBeziid";
	public static final String ByKontoIId = "SteuerkategoriekontoByKontoIId";
	public static final String ByCompound = "SteuerkategoriekontoByCompound";
	public static final String ByDate = "SteuerkategoriekontoZuDatum";
	
	private final EntityManager em;
	
	public SteuerkategoriekontoQuery(final EntityManager em) {
		this.em = em;
	}
	
	private HvTypedQuery<Steuerkategoriekonto> byCompoundKey(
			Integer steuerkategorieId, Integer mwstsatzbezId, Timestamp gueltigAb) {
		HvTypedQuery<Steuerkategoriekonto> q = 
				new HvTypedQuery<Steuerkategoriekonto>(em.createNamedQuery(ByCompound));
		return q.setParameter(1, steuerkategorieId)
				.setParameter(2, mwstsatzbezId)
				.setParameter(3, gueltigAb);
	}

	private HvTypedQuery<Steuerkategoriekonto> bySteuerkategorieAll(
			Integer steuerkategorieId) {
		HvTypedQuery<Steuerkategoriekonto> q = 
				new HvTypedQuery<Steuerkategoriekonto>(em.createNamedQuery(BySteuerkategorieIIdAll));
		return q.setParameter(1, steuerkategorieId);
	}

	private HvTypedQuery<Steuerkategoriekonto> bySteuerkategorie(
			Integer steuerkategorieId, Timestamp gueltigAb) {
		HvTypedQuery<Steuerkategoriekonto> q = 
				new HvTypedQuery<Steuerkategoriekonto>(em.createNamedQuery(BySteuerkategorieIId));
		return q.setParameter(1, steuerkategorieId)
				.setParameter(2, gueltigAb);
	}
	
	private HvTypedQuery<Steuerkategoriekonto> byDate(
			Integer steuerkategorieId, Integer mwstsatzbezId, Timestamp gueltigAb) {
		HvTypedQuery<Steuerkategoriekonto> q = 
				new HvTypedQuery<Steuerkategoriekonto>(em.createNamedQuery(ByDate));
		return q.setParameter(1, steuerkategorieId)
				.setParameter(2, mwstsatzbezId)
				.setParameter(3, gueltigAb);
	}

	private HvTypedQuery<Steuerkategoriekonto> byKontoId(
			Integer kontoId, Integer mwstsatzbezId, Timestamp gueltigAm) {
		HvTypedQuery<Steuerkategoriekonto> q = 
				new HvTypedQuery<Steuerkategoriekonto>(em.createNamedQuery(ByKontoIIdWithMwstsatzBezIId));
		return q.setParameter(1, kontoId)
				.setParameter(2, mwstsatzbezId)
				.setParameter(3, gueltigAm);
	}

	private HvTypedQuery<Steuerkategoriekonto> byKontoId(
			Integer kontoId, Timestamp gueltigAm) {
		HvTypedQuery<Steuerkategoriekonto> q = 
				new HvTypedQuery<Steuerkategoriekonto>(em.createNamedQuery(ByKontoIId));
		return q.setParameter(1, kontoId)
				.setParameter(2, gueltigAm);
	}

	public HvOptional<Steuerkategoriekonto> find(
			Integer steuerkategorieId, Integer mwstsatzbezId, Timestamp gueltigAb) {
		Steuerkategoriekonto konto = byCompoundKey(steuerkategorieId, 
				mwstsatzbezId, gueltigAb).getSingleResultNoEx();
		return HvOptional.ofNullable(konto);
	}
	
	public List<Steuerkategoriekonto> listSteuerkategorie(
			Integer steuerkategorieId, Timestamp gueltigAm) {
		return bySteuerkategorie(steuerkategorieId, gueltigAm).getResultList();		
	}
	
	public List<Steuerkategoriekonto> listSteuerkategorieAll(Integer steuerkategorieId) {
		return bySteuerkategorieAll(steuerkategorieId).getResultList();
	}
	
	public HvOptional<Steuerkategoriekonto> findZuDatum(
			Integer steuerkategorieId, Integer mwstsatzbezId, Timestamp gueltigAm) {
		List<Steuerkategoriekonto> entries = byDate(steuerkategorieId, 
				mwstsatzbezId, gueltigAm).getResultList();
		if(entries.size() == 0) return HvOptional.empty();		
		return HvOptional.of(entries.get(0));
	}
	
	public List<Steuerkategoriekonto> listKontoId(Integer kontoId, 
			Integer mwstsatzBezId, Timestamp gueltigAm) {
		return byKontoId(kontoId, mwstsatzBezId, gueltigAm).getResultList();
	}
	
	public List<Steuerkategoriekonto> listKontoId(Integer kontoId, Timestamp gueltigAm) {
		return byKontoId(kontoId, gueltigAm).getResultList();		
	}
}
