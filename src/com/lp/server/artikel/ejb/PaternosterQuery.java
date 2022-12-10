package com.lp.server.artikel.ejb;

import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;

public class PaternosterQuery {

	public static final String ByLagerIIds = "PaternosterfindByLagerIIds";
	
	public static List<Paternoster> listByLagerIIds(EntityManager em, List<Integer> lagerIIds) {
		return HvTypedQuery.<Paternoster>namedQuery(em, ByLagerIIds, lagerIIds).getResultList();
	}
}
