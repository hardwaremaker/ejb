package com.lp.server.finanz.ejb;

import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;
import com.lp.server.util.HvOptional;
import com.lp.server.util.UvaartId;

public class UvaFormularQuery {

	public static final String ByFinanzamtId = "UvaFormularByFinanzamtId";
	public static final String ByFinanzamtIdUvaartId = "UvaFormularByFinanzamtIdUvaartId";
	
	private final EntityManager em;

	public UvaFormularQuery(final EntityManager em) {
		this.em = em;
	}
	
	private HvTypedQuery<UvaFormular> byFinanzamtId(
			Integer finanzamtId, String mandantCnr) {
		HvTypedQuery<UvaFormular> q = 
				new HvTypedQuery<UvaFormular>(em.createNamedQuery(ByFinanzamtId));
		return q.setParameter(1, finanzamtId)
				.setParameter(2, mandantCnr);
	}
	
	public List<UvaFormular> listFinanzamtId(Integer finanzamtId, String mandantCnr) {
		return byFinanzamtId(finanzamtId, mandantCnr).getResultList();
	}
	
	private HvTypedQuery<UvaFormular> byFinanzamtIdUvaartId(
			Integer finanzamtId, String mandantCnr, UvaartId uvaartId) {
		HvTypedQuery<UvaFormular> q = 
				new HvTypedQuery<UvaFormular>(em.createNamedQuery(ByFinanzamtIdUvaartId));
		return q.setParameter(1, finanzamtId)
				.setParameter(2, mandantCnr)
				.setParameter(3, uvaartId.id());
	}

	public HvOptional<UvaFormular> findPartnerUvaart(Integer finanzamtId, 
			String mandantCnr, UvaartId uvaartId) {
		List<UvaFormular> all = byFinanzamtIdUvaartId(finanzamtId,
				mandantCnr, uvaartId).getResultList();
		if(all.size() != 1) {
			return HvOptional.empty();
		}
		
		return HvOptional.of(all.get(0));
	}
}
