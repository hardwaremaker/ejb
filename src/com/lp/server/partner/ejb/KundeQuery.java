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
package com.lp.server.partner.ejb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.util.KennungId;


public class KundeQuery {
	/**
	 * Suche Kunden &uuml;ber seine Lieferantennummer im angegebenen Mandanten
	 */
	public static final String ByLieferantCnrMandantCnr = "KundefindByLieferantCnrMandantCnr" ;

	/**
	 * Suche alle Kunden im angegebenen Mandanten
	 */
	public static final String ByMandantCnr = "KundefindByMandantCnr" ;
	
	
	public static final String ByMandantCnrTimestamp = "KundefindByMandantCnrTimestamp" ;
	
	public static final String ByPartnerId = "KundefindByPartnerId" ;
	public static final String ByCountPartnerId = "KundefindByCountPartnerId" ;
	
	/**
	 * Sucht den Kunden &uuml;ber seine Kundennummer
	 */
	public static final String ByKundenummer = "KundefindByKundenummer" ;
	public static final String BycFremdsystemnrcNrMandant = "KundefindBycFremdsystemnrcNrMandant";
	public static final String MaxFremdsystemnr = "KundeMaxFremdsystemnr";
	public static final String IIdsByPartnerIds = "KundeIIdsfindByPartnerIds" ;
	
	public static final String ByKennung = "KundefindByKennung";
	
	
	public static HvTypedQuery<Kunde> byLieferantCnrMandantCnr(EntityManager em, String lieferantCnr, String mandantCnr) {
		HvTypedQuery<Kunde> q = new HvTypedQuery<Kunde>(em.createNamedQuery(ByLieferantCnrMandantCnr)) ;
		q.setParameter("lieferantCnr", lieferantCnr.trim()) ;
		q.setParameter("mandantCnr", mandantCnr.trim()) ;
		return q ;
	}

	public static HvTypedQuery<Kunde> byMandantCnr(EntityManager em, String mandantCnr) {
		HvTypedQuery<Kunde> q = new HvTypedQuery<Kunde>(em.createNamedQuery(ByMandantCnr)) ;
		q.setParameter("mandantCnr", mandantCnr.trim()) ;
		return q ;
	}

	/**
	 * Eine Liste alle Kunden im angegebenen Mandanten
	 * 
	 * @param em
	 * @param mandantCnr der zu beruecksichtigende Mandant
	 * @return Eine (leere) Liste aller Kunden im angegebenen Mandanten
	 */
	public static List<Kunde> listByMandantCnr(EntityManager em, String mandantCnr) {
		return byMandantCnr(em, mandantCnr).getResultList() ;
	}

	public static HvTypedQuery<Kunde> byPartnerId(EntityManager em, Integer partnerId) {
		HvTypedQuery<Kunde> q = new HvTypedQuery<Kunde>(em.createNamedQuery(ByPartnerId)) ;
		q.setParameter("partnerId", partnerId) ;
		return q ;		
	}

	public static HvTypedQuery<Integer> byPartnerCountId(EntityManager em, Integer partnerId) {
		HvTypedQuery<Integer> q = new HvTypedQuery<Integer>(em.createNamedQuery(ByCountPartnerId)) ;
		q.setParameter("partnerId", partnerId) ;
		return q ;		
	}
	
	/**
	 * Eine Liste aller Kunden die der angegebenen PartnerId zugeordnet sind
	 * 
	 * @param em
	 * @param partnerId
	 * @return Eine (leere) Liste aller Kunden, die dem angegebenen Partner zugeordnet sind 
	 */
	public static List<Kunde> listByPartnerId(EntityManager em, Integer partnerId) {
		return byPartnerId(em, partnerId).getResultList() ;
	}
	
	public static int countByPartnerId(EntityManager em, Integer partnerId) {
		Integer count = byPartnerCountId(em, partnerId).getSingleResult() ;
		return count == null ? 0 : count ; 
	}
	
	public static HvTypedQuery<Kunde> byKundenummer(EntityManager em, Integer kundenummer) {
		HvTypedQuery<Kunde> query = new HvTypedQuery<Kunde>(em.createNamedQuery(ByKundenummer)) ;
		query.setParameter("kundenummer", kundenummer) ;
		return query ;
	}
	
	public static Kunde findByKundenummer(EntityManager em, Integer kundenummer) {
		return byKundenummer(em, kundenummer).getSingleResultNoEx() ;
	}
	
	public static HvTypedQuery<Kunde> byFremdsystemnummer(EntityManager em, String fremdsystemnummer, String mandantCnr) {
		HvTypedQuery<Kunde> query = new HvTypedQuery<Kunde>(em.createNamedQuery(BycFremdsystemnrcNrMandant));
		query.setParameter(1, fremdsystemnummer);
		query.setParameter(2, mandantCnr);
		return query;
	}
	
	public static List<Kunde> listByFremdsystemnummer(EntityManager em, String fremdsystemnummer, String mandantCnr) {
		return byFremdsystemnummer(em, fremdsystemnummer, mandantCnr).getResultList();
	}

	public static HvTypedQuery<Integer> maxFremdsystemnr(EntityManager em) {
		HvTypedQuery<Integer> theQuery = new HvTypedQuery<Integer>(em.createNamedQuery(KundeQuery.MaxFremdsystemnr));
		return theQuery;
	}
	
	public static Integer resultMaxFremdsystemnr(EntityManager em) {
		return maxFremdsystemnr(em).getSingleResultNoEx();
	}
	
	public static HvTypedQuery<Integer> iIdsByPartnerIds(EntityManager em, Collection<Integer> partnerIds) {
		HvTypedQuery<Integer> q = new HvTypedQuery<Integer>(em.createNamedQuery(IIdsByPartnerIds)) ;
		q.setParameter("partnerIds", partnerIds) ;
		return q;		
	}
	
	public static List<Integer> listIIdsByIban(EntityManager em, String iban) {
		List<Integer> partnerIds = PartnerbankQuery.listPartnerIIdsByIban(em, iban);
		if (partnerIds.isEmpty()) return new ArrayList<Integer>();
		
		return iIdsByPartnerIds(em, partnerIds).getResultList();
	}
	
	public static List<Kunde> listByKennung(
			EntityManager em, KennungId kennungId, String value) {
		return new HvTypedQuery<Kunde>(
				em.createNamedQuery(KundeQuery.ByKennung))
				.setParameter("kennungid", kennungId.id())
				.setParameter("value", value)
				.getResultList();
	}
}
