package com.lp.server.finanz.ejb;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;
import com.lp.server.util.HvOptional;

public class KontolaenderartQuery {
	public static final String ByCompound = "KontolaenderartByCompound";
	public static final String ByDate = "KontolaenderartZuDatum";
	public static final String ByUebersetzt = "KontolaenderartfindBykontoIIdUebersetzt";
	public static final String ByAll = "KontolaenderartByAll";
	
	private final EntityManager em;
	
	public KontolaenderartQuery(final EntityManager em) {
		this.em = em;
	}


	public HvOptional<Kontolaenderart> find(
			String mandantCnr, Integer kontoId, Integer finanzamtId,
			String laenderartCnr, Integer reversechargeartId, Timestamp gueltigAb) {
		HvTypedQuery<Kontolaenderart> q = 
				new HvTypedQuery<Kontolaenderart>(em.createNamedQuery(ByCompound));
		q.setParameter(1, mandantCnr)
				.setParameter(2, kontoId)
				.setParameter(3, finanzamtId)
				.setParameter(4,  laenderartCnr)
				.setParameter(5, reversechargeartId)
				.setParameter(6, gueltigAb);
		Kontolaenderart konto = q.getSingleResultNoEx();
		return HvOptional.ofNullable(konto);
	}
	
	
	public HvOptional<Kontolaenderart> findZuDatum(
			String mandantCnr, Integer kontoId, Integer finanzamtId,
			String laenderartCnr, Integer reversechargeartId, Timestamp gueltigZum) {
		HvTypedQuery<Kontolaenderart> q = 
				new HvTypedQuery<Kontolaenderart>(em.createNamedQuery(ByDate));
		q.setParameter(1, mandantCnr)
				.setParameter(2, kontoId)
				.setParameter(3, finanzamtId)
				.setParameter(4,  laenderartCnr)
				.setParameter(5, reversechargeartId)
				.setParameter(6, gueltigZum);	
		List<Kontolaenderart> entries = q.getResultList();
		if(entries.size() == 0) return HvOptional.empty();		
		return HvOptional.of(entries.get(0));
	}
	
	public List<Kontolaenderart> listUebersetzt(
			String mandantCnr, Integer uebersetzesKontoId, Integer finanzamtId,
			Integer reversechargeartId, Timestamp gueltigZum) {
		HvTypedQuery<Kontolaenderart> q = 
				new HvTypedQuery<Kontolaenderart>(em.createNamedQuery(ByUebersetzt));
		q.setParameter(1, mandantCnr)
				.setParameter(2, finanzamtId)
				.setParameter(3, reversechargeartId)
				.setParameter(4, uebersetzesKontoId)
				.setParameter(5, gueltigZum);	
		return q.getResultList();
	}
	
	public List<Kontolaenderart> listAll(
			String mandantCnr, Integer kontoId, Integer finanzamtId) {
		HvTypedQuery<Kontolaenderart> q = 
				new HvTypedQuery<Kontolaenderart>(em.createNamedQuery(ByAll));
		q.setParameter(1, mandantCnr)
				.setParameter(2, finanzamtId)
				.setParameter(3, kontoId);	
		return q.getResultList();
	}
}
