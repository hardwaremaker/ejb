package com.lp.server.finanz.ejb;

import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;

public class ReversechargeartQuery {
	public final static String ByMandant = "ReversechargeartByMandant" ;
	public final static String ByCnrMandant = "ReversechargeartByCnrMandant" ;
	public final static String NextISortByMandant = "ReversechargeartNextISortByMandant" ;
	public final static String ByMandantMitVersteckten = "ReversechargeartByMandantMitVersteckten";
	public final static String IIdsByMandant = "ReversechargeartIIdsByMandant";
	public final static String IIdsByMandantMitVersteckten = "ReversechargeartIIdsByMandantMitVersteckten";
	
	public static HvTypedQuery<Reversechargeart> byCnrMandant(EntityManager em, String cnr, String mandantCnr) {
		HvTypedQuery<Reversechargeart> theQuery = new HvTypedQuery<Reversechargeart>(em.createNamedQuery(ByCnrMandant)) ;
		theQuery.setParameter("cnr", cnr) ;
		theQuery.setParameter("mandant", mandantCnr) ;
		return theQuery ;
	}
	
	public static Reversechargeart findByCnrMandant(EntityManager em, String cnr, String mandantCnr) {
		return byCnrMandant(em, cnr, mandantCnr).getSingleResult() ;
	}
	
	public static HvTypedQuery<Reversechargeart> byMandant(EntityManager em, String mandantCnr) {
		HvTypedQuery<Reversechargeart> theQuery = new HvTypedQuery<Reversechargeart>(em.createNamedQuery(ByMandant)) ;
		theQuery.setParameter("mandant", mandantCnr) ;
		return theQuery ;		
	}
	
	public static List<Reversechargeart> listByMandant(EntityManager em, String mandantCnr) {
		return byMandant(em, mandantCnr).getResultList() ;
	}
	
	public static HvTypedQuery<Integer> byMaxISortByMandant(EntityManager em, String mandantCnr) {
		HvTypedQuery<Integer> theQuery = new HvTypedQuery<Integer>(em.createNamedQuery(NextISortByMandant)) ;
		theQuery.setParameter("mandant", mandantCnr) ;
		return theQuery ;			
	}
	
	public static Integer nextISortByMandant(EntityManager em, String mandantCnr) {
		Integer isort = byMaxISortByMandant(em, mandantCnr).getSingleResult() ;
		return isort == null ? 0 : ++isort ;
	}
	
	public static HvTypedQuery<Reversechargeart> byMandantMitVersteckten(EntityManager em, String mandantCnr) {
		HvTypedQuery<Reversechargeart> theQuery = new HvTypedQuery<Reversechargeart>(em.createNamedQuery(ByMandantMitVersteckten)) ;
		theQuery.setParameter("mandant", mandantCnr) ;
		return theQuery ;		
	}
	
	public static List<Reversechargeart> listByMandantMitVersteckten(EntityManager em, String mandantCnr) {
		return byMandantMitVersteckten(em, mandantCnr).getResultList() ;
	}
	
	public static List<Integer> listIIdsByMandant(EntityManager em, String mandantCnr, Boolean mitVersteckten) {
		return new HvTypedQuery<Integer>(em.createNamedQuery(mitVersteckten ? IIdsByMandantMitVersteckten : IIdsByMandant))
				.setParameter("mandant", mandantCnr)
				.getResultList();
	}
}
