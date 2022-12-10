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
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import com.lp.server.partner.ejb.HvTypedQuery;

public class LagerbewegungQuery {
	public final static  String ByLagerbewegungfindByArtikelIIdBAbgangBelegdatumCount = "LagerbewegungfindByArtikelIIdBAbgangBelegdatumCount" ;
	public final static String ByBelegartCNrArtikelIIdCSeriennrchargennr = "LagerbewegungfindByBelegartCNrArtikelIIdCSeriennrchargennr";
	public final static String ByBelegartCNrArtikelIIdCSeriennrchargennrIBelegartIds = "LagerbewegungfindByBelegartCNrArtikelIIdCSeriennrchargennrIBelegartIds";
	public final static String ByBelegartCNrArtikelIIdCSeriennrchargennrIBelegartIdsNurAbgaenge = "LagerbewegungfindByBelegartCNrArtikelIIdCSeriennrchargennrIBelegartIdsNurAbgaenge";
	
	public static Long countByArtikelIIdDate(EntityManager em, Integer artikelIId, Timestamp youngestDate) {
		HvTypedQuery<Long> itemQuery = new HvTypedQuery<Long>(
				em.createNamedQuery(ByLagerbewegungfindByArtikelIIdBAbgangBelegdatumCount)) ;
		itemQuery.setParameter("artikelId", artikelIId) ;
		itemQuery.setParameter("belegDatum", youngestDate) ;
		
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
	
	public static HvTypedQuery<Lagerbewegung> byBelegartCNrArtikelIIdCSeriennrchargennr(
			EntityManager em, String belegartCnr, Integer artikelIId, String cSeriennrchargennr) {
		HvTypedQuery<Lagerbewegung> theQuery = new HvTypedQuery<Lagerbewegung>(em.createNamedQuery(ByBelegartCNrArtikelIIdCSeriennrchargennr));
		theQuery.setParameter("belegartnr", belegartCnr);
		theQuery.setParameter("artikelId", artikelIId);
		theQuery.setParameter("serienChargennr", cSeriennrchargennr);
		return theQuery;
	}
	
	public static List<Lagerbewegung> listByBelegartCNrArtikelIIdCSeriennrchargennr(
			EntityManager em, String belegartCnr, Integer artikelIId, String cSeriennrchargennr) {
		return byBelegartCNrArtikelIIdCSeriennrchargennr(em, belegartCnr, artikelIId, cSeriennrchargennr).getResultList();
	}
	
	public static HvTypedQuery<Lagerbewegung> byBelegartCNrArtikelIIdCSeriennrchargennrIBelegartIds(
			EntityManager em, String belegartCnr, Integer artikelIId, String cSeriennrchargennr, List<Integer> belegartids) {
		HvTypedQuery<Lagerbewegung> theQuery = new HvTypedQuery<Lagerbewegung>(em.createNamedQuery(ByBelegartCNrArtikelIIdCSeriennrchargennrIBelegartIds));
		theQuery.setParameter("belegartnr", belegartCnr);
		theQuery.setParameter("artikelId", artikelIId);
		theQuery.setParameter("serienChargennr", cSeriennrchargennr);
		theQuery.setParameter("belegartids", belegartids);
		return theQuery;
	}
	
	public static HvTypedQuery<Lagerbewegung> byBelegartCNrArtikelIIdCSeriennrchargennrIBelegartIdsNurAbgaenge(
			EntityManager em, String belegartCnr, Integer artikelIId, String cSeriennrchargennr, List<Integer> belegartids) {
		HvTypedQuery<Lagerbewegung> theQuery = new HvTypedQuery<Lagerbewegung>(em.createNamedQuery(ByBelegartCNrArtikelIIdCSeriennrchargennrIBelegartIdsNurAbgaenge));
		theQuery.setParameter("belegartnr", belegartCnr);
		theQuery.setParameter("artikelId", artikelIId);
		theQuery.setParameter("serienChargennr", cSeriennrchargennr);
		theQuery.setParameter("belegartids", belegartids);
		return theQuery;
	}

	
	public static List<Lagerbewegung> listByBelegartCNrArtikelIIdCSeriennrchargennrIBelegartIds(
			EntityManager em, String belegartCnr, Integer artikelIId, String cSeriennrchargennr, List<Integer> belegartids) {
		return byBelegartCNrArtikelIIdCSeriennrchargennrIBelegartIds(em, belegartCnr, artikelIId, cSeriennrchargennr, belegartids).getResultList();
	}
	public static List<Lagerbewegung> listByBelegartCNrArtikelIIdCSeriennrchargennrIBelegartIdsNurAbgaenge(
			EntityManager em, String belegartCnr, Integer artikelIId, String cSeriennrchargennr, List<Integer> belegartids) {
		return byBelegartCNrArtikelIIdCSeriennrchargennrIBelegartIdsNurAbgaenge(em, belegartCnr, artikelIId, cSeriennrchargennr, belegartids).getResultList();
	}
	
} 
