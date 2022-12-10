package com.lp.server.system.ejb;

import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;
import com.lp.server.util.HvOptional;

public class MediastandardQuery {
	public static final String ByCnr = "MediastandardByCnr";

	private final EntityManager em;
	
	public MediastandardQuery(final EntityManager em) {
		this.em = em;
	}
	
	
	public HvOptional<Mediastandard> find(String mandantCnr, String cnr, String localeCnr) {
		HvTypedQuery<Mediastandard> q =
				new HvTypedQuery<Mediastandard>(em.createNamedQuery(ByCnr));
		q.setParameter("mandant", mandantCnr)
			.setParameter("cnr", cnr)
			.setParameter("locale", localeCnr);
		List<Mediastandard> entries = q.getResultList();
		if(entries.size() == 0) return HvOptional.empty();
		return HvOptional.of(entries.get(0));
	}
}
