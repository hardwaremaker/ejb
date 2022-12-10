package com.lp.server.artikel.ejb;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import com.lp.server.partner.ejb.HvTypedQuery;

public class LagerzugangursprungQuery {

	public static final String CountEntries = "LagerzugangursprungCountEntries";
	
	public static Long count(EntityManager em) {
		HvTypedQuery<Long> itemQuery = new HvTypedQuery<Long>(em.createNamedQuery(CountEntries)) ;
		
		Long count = 1l ;
		try {
			count = itemQuery.getSingleResult();
			return count ;
		} catch(NonUniqueResultException e) {
			System.out.println("e " + e.getMessage()) ;
		} catch(NoResultException e) {
			System.out.println("e " + e.getMessage()) ;
		}
		
		return count ;
	}
}
