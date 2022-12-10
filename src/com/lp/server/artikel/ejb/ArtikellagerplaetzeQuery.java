package com.lp.server.artikel.ejb;

import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;

public class ArtikellagerplaetzeQuery {

	public final static String ByArtikelIId = "ArtikellagerplaetzefindByArtikelIId" ;
	public final static String ByArtikelIIdLagerplatzIId = "ArtikellagerplaetzefindByArtikelIIdLagerplatzIId";
	public final static String ByArtikelIIdOrderByISort = "ArtikellagerplaetzefindByArtikelIIdOrderByISort";
	public final static String ByLagerplatzIId = "ArtikellagerplaetzefindByLagerplatzIId";
	
	public static HvTypedQuery<Artikellagerplaetze> byArtikelIId(EntityManager em, Integer artikelIId) {
		HvTypedQuery<Artikellagerplaetze> theQuery = new HvTypedQuery<Artikellagerplaetze>(em.createNamedQuery(ByArtikelIId)) ;
		theQuery.setParameter(1, artikelIId) ;
		return theQuery ;
	}
	
	public static List<Artikellagerplaetze> listByArtikelIId(EntityManager em, Integer artikelIId) {
		return byArtikelIId(em, artikelIId).getResultList() ;
	}
	
	public static HvTypedQuery<Artikellagerplaetze> byArtikelIIdLagerplatzIId(EntityManager em, Integer artikelIId, Integer lagerplatzIId) {
		HvTypedQuery<Artikellagerplaetze> theQuery = new HvTypedQuery<Artikellagerplaetze>(em.createNamedQuery(ByArtikelIIdLagerplatzIId)) ;
		theQuery.setParameter(1, artikelIId);
		theQuery.setParameter(2, lagerplatzIId);
		return theQuery ;
	}
	
	public static Artikellagerplaetze resultByArtikelIIdLagerplatzIIdNoExc(EntityManager em, Integer artikelIId, Integer lagerplatzIId) {
		return byArtikelIIdLagerplatzIId(em, artikelIId, lagerplatzIId).getSingleResultNoEx();
	}
	
	public static HvTypedQuery<Artikellagerplaetze> byArtikelIIdOrderByISort(EntityManager em, Integer artikelIId) {
		HvTypedQuery<Artikellagerplaetze> theQuery = new HvTypedQuery<Artikellagerplaetze>(em.createNamedQuery(ByArtikelIIdOrderByISort)) ;
		theQuery.setParameter(1, artikelIId) ;
		return theQuery ;
	}
	
	public static List<Artikellagerplaetze> listByArtikelIIdOrderByISort(EntityManager em, Integer artikelIId) {
		return byArtikelIIdOrderByISort(em, artikelIId).getResultList() ;
	}
	
	public static HvTypedQuery<Artikellagerplaetze> byLagerplatzIId(EntityManager em, Integer lagerplatzIId) {
		HvTypedQuery<Artikellagerplaetze> theQuery = new HvTypedQuery<Artikellagerplaetze>(em.createNamedQuery(ByLagerplatzIId));
		theQuery.setParameter(1, lagerplatzIId);
		return theQuery;
	}
	
	public static List<Artikellagerplaetze> listByLagerplatzIId(EntityManager em, Integer lagerplatzIId) {
		return byLagerplatzIId(em, lagerplatzIId).getResultList();
	}
}
