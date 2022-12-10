package com.lp.server.fertigung.ejb;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;
import com.lp.server.util.LosId;

public class LossollmaterialQuery {

	public final static String ByLosIId = "LossollmaterialfindByLosIId";
	public final static String ByLosIIdArtklaIIds = "LossollmaterialfindByLosIIdArtklaIIds";
	
	public static List<Lossollmaterial> listByLosIId(EntityManager em, LosId losId) {
		return HvTypedQuery.<Lossollmaterial>namedQuery(em, ByLosIId, losId.id()).getResultList();
	}
	
	public static List<Lossollmaterial> listByLosIIdArtklaIIds(EntityManager em, LosId losId, Collection<Integer> artikelklasseIds) {
		return HvTypedQuery.<Lossollmaterial>namedQuery(em, ByLosIIdArtklaIIds)
				.setParameter("losId",losId.id())
				.setParameter("artklaIds", artikelklasseIds)
				.getResultList();
	}
}
