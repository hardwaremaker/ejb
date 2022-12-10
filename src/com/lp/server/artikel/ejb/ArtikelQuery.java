/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2015 HELIUM V IT-Solutions GmbH
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published 
 * by the Free Software Foundation, either version 3 of theLicense, or 
 * (at your option) any later version.
 * 
 * According to sec. 7 of the GNU Affero General Public License, version 3, 
 * the terms of the AGPL are supplemented with the following terms:
 * 
 * "HELIUM V" and "HELIUM 5" are registered trademarks of 
 * HELIUM V IT-Solutions GmbH. The licensing of the program under the 
 * AGPL does not imply a trademark license. Therefore any rights, title and
 * interest in our trademarks remain entirely with us. If you want to propagate
 * modified versions of the Program under the name "HELIUM V" or "HELIUM 5",
 * you may only do so if you have a written permission by HELIUM V IT-Solutions 
 * GmbH (to acquire a permission please contact HELIUM V IT-Solutions
 * at trademark@heliumv.com).
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact: developers@heliumv.com
 ******************************************************************************/
package com.lp.server.artikel.ejb;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;

public class ArtikelQuery {
	public final static String ByMandantCNrShopgruppeIId = "ArtikelfindByMandantCNrShopgruppeIId" ;
	public final static String ByMandantCNrShopgruppeIIdWithDate = "ArtikelfindByMandantCNrShopgruppeIIdTimestamp" ;
	public final static String ByMandantCNrWithDate = "ArtikelfindByMandantCNrTimestamp" ;
	public final static String ByMandantCNr4VendingId = "ArtikelFindByMandantCNr4VendingId";
	public final static String ByMandantCNr4VendingIdNotNull = "ArtikelFindByMandantCNr4VendingIdNotNull";
	public final static String MaxCUl = "ArtikelMaxCUl";
	public final static String ByArtikelnrherstellerMandantCNr = "ByArtikelnrherstellerMandantCNr";
	public final static String ByArtikelCNrSP8207 = "ArtikelfindByMandantCNrSP8207";
	public final static String ByArtikelCNrHerstellerkopplung = "ArtikelfindByCNrHerstellerkopplung";
	
	public static HvTypedQuery<Artikel> byMandantShopgruppeIId(EntityManager em, String mandant, Integer shopgruppeIId) {
		HvTypedQuery<Artikel> itemQuery = new HvTypedQuery<Artikel>(
				em.createNamedQuery(ArtikelQuery.ByMandantCNrShopgruppeIId)) ;
		itemQuery.setParameter("mandant", mandant) ;
		itemQuery.setParameter("id", shopgruppeIId) ;
		return itemQuery ;
	}
	
	public static List<Artikel> listByMandantShopgruppeIId(EntityManager em, String mandant, Integer shopgruppeIId) {
		return byMandantShopgruppeIId(em, mandant, shopgruppeIId).getResultList() ;
	}
	
	public static HvTypedQuery<Artikel> byMandantShopgruppeIIdDate(EntityManager em, String mandant, Integer shopgruppeIId, Timestamp changedStamp) {
		HvTypedQuery<Artikel> itemQuery = new HvTypedQuery<Artikel>(
				em.createNamedQuery(ArtikelQuery.ByMandantCNrShopgruppeIIdWithDate)) ;
		itemQuery.setParameter("mandant", mandant) ;
		itemQuery.setParameter("id", shopgruppeIId) ;
		itemQuery.setParameter("tChanged", changedStamp) ;
		return itemQuery ;
	}

	public static List<Artikel> listByMandantShopgruppeIIdDate(EntityManager em, String mandant, Integer shopgruppeIId, Timestamp changedStamp) {
		return byMandantShopgruppeIIdDate(em, mandant, shopgruppeIId, changedStamp).getResultList() ;
	}
	
	
	public static HvTypedQuery<Artikel> byMandantDate(EntityManager em, String mandant, Timestamp changedStamp) {
		HvTypedQuery<Artikel> itemQuery = new HvTypedQuery<Artikel>(
				em.createNamedQuery(ArtikelQuery.ByMandantCNrWithDate)) ;
		itemQuery.setParameter("mandant", mandant) ;
		itemQuery.setParameter("tChanged", changedStamp) ;
		return itemQuery ;		
	}

	public static List<Artikel> listByMandantDate(EntityManager em, String mandant, Timestamp changedStamp) {
		return byMandantDate(em, mandant, changedStamp).getResultList() ;
	}
	
	public static HvTypedQuery<Artikel> byMandantCNr4VendingId(EntityManager em, String mandant, String fourVendingId) {
		HvTypedQuery<Artikel> theQuery = new HvTypedQuery<Artikel>(em.createNamedQuery(ArtikelQuery.ByMandantCNr4VendingId));
		return theQuery
					.setParameter("mandant", mandant)
					.setParameter("fourVendingId", fourVendingId);
	}
	
	public static Artikel resultByMandantCNr4VendingId(EntityManager em, String mandant, String fourVendingId) {
		return byMandantCNr4VendingId(em, mandant, fourVendingId).getSingleResultNoEx();
	}
	
	public static HvTypedQuery<Artikel> byMandantCNr4VendingIdNotNull(EntityManager em, String mandant) {
		HvTypedQuery<Artikel> theQuery = new HvTypedQuery<Artikel>(em.createNamedQuery(ArtikelQuery.ByMandantCNr4VendingIdNotNull));
		return theQuery.setParameter("mandant", mandant);
	}
	
	public static List<Artikel> listByMandantCNr4VendingIdNotNull(EntityManager em, String mandant) {
		return byMandantCNr4VendingIdNotNull(em, mandant).getResultList();
	}
	
	public static HvTypedQuery<Integer> maxCUl(EntityManager em) {
		HvTypedQuery<Integer> theQuery = new HvTypedQuery<Integer>(em.createNamedQuery(ArtikelQuery.MaxCUl));
		
		return theQuery;
	}
	
	public static Integer resultMaxCUl(EntityManager em) {
		return maxCUl(em).getSingleResultNoEx();
	}
	
	public static HvTypedQuery<Artikel> byVerkaufsEanMandantCnr(EntityManager em, String ean, String mandant) {
		HvTypedQuery<Artikel> theQuery = new HvTypedQuery<Artikel>(
				em.createNamedQuery("ArtikelfindByCVerkaufseannrMandantCNr"));
		return theQuery
			.setParameter(1, ean)
			.setParameter(2, mandant); 
	}

	public static List<Artikel> listByVerkaufsEanMandantCnr(EntityManager em, String ean, String mandant) {
		return byVerkaufsEanMandantCnr(em, ean, mandant).getResultList() ;
	}

	public static HvTypedQuery<Artikel> byVerpackungsEanMandantCnr(EntityManager em, String ean, String mandant) {
		HvTypedQuery<Artikel> theQuery = new HvTypedQuery<Artikel>(
				em.createNamedQuery("ArtikelfindByCVerpackungseannrMandantCNr"));
		return theQuery
			.setParameter(1, ean)
			.setParameter(2, mandant); 
	}

	public static List<Artikel> listByVerpackungsEanMandantCnr(EntityManager em, String ean, String mandant) {
		return byVerpackungsEanMandantCnr(em, ean, mandant).getResultList() ;
	}
	
	public static HvTypedQuery<Artikel> byArtikelnrherstellerMandantCnr(EntityManager em, String artikelnrhersteller, String mandant) {
		HvTypedQuery<Artikel> theQuery = new HvTypedQuery<Artikel>(em.createNamedQuery(ByArtikelnrherstellerMandantCNr));
		return theQuery
				.setParameter("cnr", artikelnrhersteller)
				.setParameter("mandant", mandant);			
	}
	
	public static List<Artikel> listByArtikelnrherstellerMandantCnr(EntityManager em, String artikelnrhersteller, String mandant) {
		return byArtikelnrherstellerMandantCnr(em, artikelnrhersteller, mandant).getResultList();
	}
	
	public static HvTypedQuery<Artikel> byArtikelnrSP8207(EntityManager em, String cnr, String mandant) {
		HvTypedQuery<Artikel> theQuery = new HvTypedQuery<Artikel>(em.createNamedQuery(ByArtikelCNrSP8207));
		return theQuery
				.setParameter("cnr", cnr.trim() + "%")
				.setParameter("mandant", mandant);			
	}
	
	public static List<Artikel> listArtikelnrSP8207(EntityManager em, String cnr, String mandant) {
		return byArtikelnrSP8207(em, cnr, mandant).getResultList();
	}
	
	public static HvTypedQuery<Artikel> byArtikelCNrHerstellerkopplung(EntityManager em, String cnr, String herstellerKuerzel) {
		HvTypedQuery<Artikel> theQuery = new HvTypedQuery<Artikel>(em.createNamedQuery(ByArtikelCNrHerstellerkopplung));
		return theQuery.setParameter("cNr", cnr).setParameter("herstellerKuerzel", herstellerKuerzel);
	}
	
	public static List<Artikel> listByCNrHerstellerkopplung(EntityManager em, String cnr, String herstellerKuerzel) {
		return byArtikelCNrHerstellerkopplung(em, cnr, herstellerKuerzel).getResultList();		
	}
}
 