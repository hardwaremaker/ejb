package com.lp.server.fertigung.ejb;

import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;
import com.lp.server.util.LosId;

public class LoslagerentnahmeQuery {
	
	public static final String ByLosIId = "LoslagerentnahmefindByLosIId";
	
	public static List<Loslagerentnahme> listByLosIId(EntityManager em, LosId losId) {
		return HvTypedQuery.<Loslagerentnahme>namedQuery(em, ByLosIId, losId.id()).getResultList();
	}

}
