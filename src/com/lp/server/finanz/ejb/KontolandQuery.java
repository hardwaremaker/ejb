package com.lp.server.finanz.ejb;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;
import com.lp.server.util.HvOptional;

public class KontolandQuery {
	public static final String ByCompound = "KontolandByCompound";
	public static final String ByDate = "KontolandByDate";
	
	private final EntityManager em;
	
	public KontolandQuery(final EntityManager em) {
		this.em = em;
	}
	
	public HvOptional<Kontoland> find(Integer kontoId, Integer landId, Timestamp gueltigAb) {
		HvTypedQuery<Kontoland> q = 
				new HvTypedQuery<Kontoland>(em.createNamedQuery(ByCompound));
		q.setParameter(1, kontoId)
				.setParameter(2, landId)
				.setParameter(3, gueltigAb);
		Kontoland konto = q.getSingleResultNoEx();
		return HvOptional.ofNullable(konto);		
	}
	
	public HvOptional<Kontoland> findZuDatum(Integer kontoId, Integer landId, Timestamp gueltigAb) {
		HvTypedQuery<Kontoland> q = 
				new HvTypedQuery<Kontoland>(em.createNamedQuery(ByDate));
		q.setParameter(1, kontoId)
				.setParameter(2, landId)
				.setParameter(3, gueltigAb);
		List<Kontoland> entries = q.getResultList();
		if(entries.size() == 0) return HvOptional.empty();		
		return HvOptional.of(entries.get(0));
	}
}
