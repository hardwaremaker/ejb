package com.lp.server.angebotstkl.ejb;

import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;

public class WebFindChipsQuery {

	public static final String ByMandantCNr = "WebFindChipsFindByMandantCNr";
	public static final String ByMandantCNrWithNullLieferanten = "WebFindChipsFindByMandantCNrWithNullLieferanten";
	public static final String ByDistributorId = "WebFindChipsFindByDistributorId";
	
	public static HvTypedQuery<WebFindChips> byMandantCNr(EntityManager em, String mandantCNr) {
		HvTypedQuery<WebFindChips> theQuery = new HvTypedQuery<WebFindChips>(em.createNamedQuery(ByMandantCNr));
		theQuery.setParameter("mandantCNr", mandantCNr);
		
		return theQuery;
	}
	
	public static List<WebFindChips> listByMandantCNr(EntityManager em, String mandantCNr) {
		return byMandantCNr(em, mandantCNr).getResultList();
	}
	
	public static HvTypedQuery<WebFindChips> byMandantCNrWithNullLieferanten(EntityManager em, String mandantCNr) {
		HvTypedQuery<WebFindChips> theQuery = new HvTypedQuery<WebFindChips>(em.createNamedQuery(ByMandantCNrWithNullLieferanten));
		theQuery.setParameter("mandantCNr", mandantCNr);
		
		return theQuery;
	}
	
	public static List<WebFindChips> listByMandantCNrWithNullLieferanten(EntityManager em, String mandantCNr) {
		return byMandantCNrWithNullLieferanten(em, mandantCNr).getResultList();
	}
	
	public static HvTypedQuery<WebFindChips> byByDistributorId(EntityManager em, String distributorId) {
		HvTypedQuery<WebFindChips> theQuery = new HvTypedQuery<WebFindChips>(em.createNamedQuery(ByDistributorId));
		theQuery.setParameter("distributorId", distributorId);
		
		return theQuery;
	}
	
	public static WebFindChips resultByDistributorId(EntityManager em, String distributorId) {
		return byByDistributorId(em, distributorId).getSingleResult();
	}
	
}
