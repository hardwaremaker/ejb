package com.lp.server.system.ejb;

import java.util.Collection;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;
import com.lp.server.util.HvOptional;
import com.lp.server.util.MwstsatzId;
import com.lp.server.util.ReversechargeartId;

public class MwstsatzCodeQuery {

	public static final String ByMwstsatzIIdReversechargeartIId = "MwstsatzCodefindByMwstsatzIIdReversechargeartIId";
	public static final String IIdsByMwstsatzIId = "MwstsatzCodefindByMwstsatzIId";
	
	public static HvOptional<MwstsatzCode> findByMwstsatzIIdReversechargeartIId(EntityManager em, MwstsatzId mwstsatzId, ReversechargeartId reversechargeartId) {
		return HvOptional.ofNullable(new HvTypedQuery<MwstsatzCode>(em.createNamedQuery(ByMwstsatzIIdReversechargeartIId))
				.setParameter("mwstsatzId", mwstsatzId.id())
				.setParameter("rcartId", reversechargeartId.id())
				.getSingleResultNoEx());
	}
	
	public static Collection<Integer> findIIdByMwstsatzIId(EntityManager em, MwstsatzId mwstsatzId) {
		return new HvTypedQuery<Integer>(em.createNamedQuery(IIdsByMwstsatzIId))
				.setParameter("mwstsatzId", mwstsatzId.id())
				.getResultList();
	}
}
