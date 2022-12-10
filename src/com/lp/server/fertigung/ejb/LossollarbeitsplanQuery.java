package com.lp.server.fertigung.ejb;

import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;
import com.lp.server.util.LosId;

public class LossollarbeitsplanQuery {

	public static final String ByLosIIdIArbeitsgangnummerIUnterarbeitsgang = "LossollarbeitsplanfindByLosIIdIArbeitsgangnummerIUnterarbeitsgang";
	public static final String ByLosIIdIArbeitsgangnummer = "LossollarbeitsplanfindByLosIIdIArbeitsgangnummer";
	public static final String ByLosIIdArtklaIIds = "LossollarbeitsplanfindByLosIIdArtklaIIds";
	
	public static HvTypedQuery<Lossollarbeitsplan> byLosIIdArbeitsgangUnterarbeitsgang(EntityManager em, 
			Integer losIId, Integer arbeitsgangnummer, Integer unterarbeitsgangnummer) {
		HvTypedQuery<Lossollarbeitsplan> theQuery = new HvTypedQuery<Lossollarbeitsplan>(em.createNamedQuery(ByLosIIdIArbeitsgangnummerIUnterarbeitsgang));
		theQuery.setParameter(1, losIId);
		theQuery.setParameter(2, arbeitsgangnummer);
		theQuery.setParameter(3, unterarbeitsgangnummer);
		
		return theQuery;
	}
	
	public static List<Lossollarbeitsplan> listByLosIIdArbeitsgangUnterarbeitsgang(EntityManager em, 
			Integer losIId, Integer arbeitsgangnummer, Integer unterarbeitsgangnummer) {
		HvTypedQuery<Lossollarbeitsplan> theQuery = byLosIIdArbeitsgangUnterarbeitsgang(em, losIId, arbeitsgangnummer, unterarbeitsgangnummer);
		return theQuery.getResultList();
	}
	
	public static HvTypedQuery<Lossollarbeitsplan> byLosIIdArbeitsgang(EntityManager em, 
			Integer losIId, Integer arbeitsgangnummer) {
		HvTypedQuery<Lossollarbeitsplan> theQuery = new HvTypedQuery<Lossollarbeitsplan>(em.createNamedQuery(ByLosIIdIArbeitsgangnummer));
		theQuery.setParameter(1, losIId);
		theQuery.setParameter(2, arbeitsgangnummer);
		
		return theQuery;
	}
	
	public static List<Lossollarbeitsplan> listByLosIIdArbeitsgang(EntityManager em, 
			Integer losIId, Integer arbeitsgangnummer) {
		HvTypedQuery<Lossollarbeitsplan> theQuery = byLosIIdArbeitsgang(em, losIId, arbeitsgangnummer);
		return theQuery.getResultList();
	}

	public static List<Lossollarbeitsplan> listByLosIIdArtklaIIds(EntityManager em, LosId losId,
			List<Integer> artikelklasseIds) {
		return HvTypedQuery.<Lossollarbeitsplan>namedQuery(em, ByLosIIdArtklaIIds)
				.setParameter("losId",losId.id())
				.setParameter("artklaIds", artikelklasseIds)
				.getResultList();
	}
	
}
