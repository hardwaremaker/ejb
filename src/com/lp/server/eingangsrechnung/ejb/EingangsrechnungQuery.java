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
package com.lp.server.eingangsrechnung.ejb;

import java.sql.Date;
import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.eingangsrechnung.service.EingangsrechnungFac;
import com.lp.server.partner.ejb.HvTypedQuery;
import com.lp.server.util.LieferantId;

public class EingangsrechnungQuery {

	public static final String ByMandantLieferantIIdStatusCNr = "EingangsrechnungfindByMandantLieferantIIdStatusCNr";
	public static final String ByMandantBelegdatumBisStatusCNr = "EingangsrechnungfindByMandantBelegdatumBisStatusCNr";
	public static final String ByMandantStatusCNr = "EingangsrechnungfindByMandantStatusCNr";
	public static final String ByMandantKundendatenStatusCNr = "EingangsrechnungfindByMandantKundendatenStatusCNr";
	public static final String ByCNrMandantCNr = "EingangsrechnungfindByCNrMandantCNr";
	public static final String MaxCNr = "EingangsrechnungMaxCNr";
	public static final String MaxCNrByGeschaeftsjahr = "EingangsrechnungMaxCNrByGeschaeftsjahr";
	public static final String ByIIds = "EingangsrechnungByIIds";
	public static final String ByMandantBelegdatumVonBis = "EingangsrechnungfindByMandantBelegdatumVonBis";
	public static final String ByMandantLieferantBelegdatumVonBis = "EingangsrechnungfindByMandantLieferantBelegdatumVonBis";
	
	public static HvTypedQuery<Eingangsrechnung> byMandantLieferantIIdStatusCNr(EntityManager em, 
			String mandantCNr, Integer lieferantIId, List<String> stati) {
		HvTypedQuery<Eingangsrechnung> theQuery = new HvTypedQuery<Eingangsrechnung>(em.createNamedQuery(ByMandantLieferantIIdStatusCNr));
		theQuery.setParameter("mandant", mandantCNr);
		theQuery.setParameter("lieferant", lieferantIId);
		theQuery.setParameter("stati", stati);
		
		return theQuery;
	}
	
	public static List<Eingangsrechnung> listByMandantLieferantIIdStatusCNr(EntityManager em, 
			String mandantCNr, Integer lieferantIId, List<String> stati) {
		return byMandantLieferantIIdStatusCNr(em, mandantCNr, lieferantIId, stati).getResultList();
	}
	
	public static HvTypedQuery<Eingangsrechnung> byMandantBelegdatumBisStatusCNr(EntityManager em, 
			String mandantCNr, Date belegdatum, List<String> stati) {
		HvTypedQuery<Eingangsrechnung> theQuery = new HvTypedQuery<Eingangsrechnung>(em.createNamedQuery(ByMandantBelegdatumBisStatusCNr));
		theQuery.setParameter("mandant", mandantCNr);
		theQuery.setParameter("belegdatum", belegdatum);
		theQuery.setParameter("stati", stati);
		
		return theQuery;
	}
	
	public static List<Eingangsrechnung> listByMandantBelegdatumBisStatusCNr(EntityManager em, 
			String mandantCNr, Date belegdatum, List<String> stati) {
		return byMandantBelegdatumBisStatusCNr(em, mandantCNr, belegdatum, stati).getResultList();
	}
	
	public static HvTypedQuery<Eingangsrechnung> byMandantStatusCNr(EntityManager em, 
			String mandantCNr, List<String> stati) {
		HvTypedQuery<Eingangsrechnung> theQuery = new HvTypedQuery<Eingangsrechnung>(em.createNamedQuery(ByMandantStatusCNr));
		theQuery.setParameter("mandant", mandantCNr);
		theQuery.setParameter("stati", stati);
		
		return theQuery;
	}
	
	public static List<Eingangsrechnung> listByMandantStatusCNr(EntityManager em, 
			String mandantCNr, List<String> stati) {
		return byMandantStatusCNr(em, mandantCNr, stati).getResultList();
	}
	
	public static HvTypedQuery<Eingangsrechnung> byMandantKundendatenStatusCNr(EntityManager em,
			String mandantCNr, String kundendaten, List<String> stati) {
		HvTypedQuery<Eingangsrechnung> theQuery = new HvTypedQuery<Eingangsrechnung>(em.createNamedQuery(ByMandantKundendatenStatusCNr));
		theQuery.setParameter("mandant", mandantCNr);
		theQuery.setParameter("kundendaten", kundendaten);
		theQuery.setParameter("stati", stati);
		
		return theQuery;
	}
	
	public static List<Eingangsrechnung> listByMandantKundendatenStatusCNr(EntityManager em,
			String mandantCNr, String kundendaten, List<String> stati) {
		return byMandantKundendatenStatusCNr(em, mandantCNr, kundendaten, stati).getResultList();
	}
	
	public static HvTypedQuery<Eingangsrechnung> byCNrMandantCNr(EntityManager em, String cNr, String mandantCNr) {
		HvTypedQuery<Eingangsrechnung> theQuery = new HvTypedQuery<Eingangsrechnung>(em.createNamedQuery(ByCNrMandantCNr));
		theQuery.setParameter(1, cNr);
		theQuery.setParameter(2, mandantCNr);
		return theQuery;
	}
	
	public static Eingangsrechnung resultByCNrMandantCNrOhneZusatzkosten(EntityManager em, String cNr, String mandantCNr) {
		List<Eingangsrechnung> results = byCNrMandantCNr(em, cNr, mandantCNr).getResultList();
		for (Eingangsrechnung er : results) {
			if (!EingangsrechnungFac.EINGANGSRECHNUNGART_ZUSATZKOSTEN.equals(er.getEingangsrechnungartCNr())) {
				return er;
			}
		}
		return null;
	}
	
	public static HvTypedQuery<String> maxCNr(EntityManager em, String mandantCNr) {
		HvTypedQuery<String> theQuery = new HvTypedQuery<String>(em.createNamedQuery(MaxCNr));
		theQuery.setParameter("mandant", mandantCNr);
		
		return theQuery;
	}
	
	public static String resultMaxCNr(EntityManager em, String mandantCNr) {
		return maxCNr(em, mandantCNr).getSingleResultNoEx();
	}
	
	public static String resultMaxCNrByGeschaeftsjahr(EntityManager em, Integer geschaeftsjahr, String mandantCNr) {
		return HvTypedQuery.<String>namedQuery(em, MaxCNrByGeschaeftsjahr, mandantCNr, geschaeftsjahr).getSingleResultNoEx();
	}

	public static List<Eingangsrechnung> listByIIds(EntityManager em, List<Integer> ids) {
		return HvTypedQuery.<Eingangsrechnung>namedQuery(em, ByIIds, ids).getResultList();
	}
	
	public static List<Eingangsrechnung> listByMandantBelegdatumVonBis(EntityManager em, String mandantCNr, Date von, Date bis) {
		return HvTypedQuery.<Eingangsrechnung>namedQuery(em, ByMandantBelegdatumVonBis, mandantCNr, von, bis).getResultList();
	}
	
	public static List<Eingangsrechnung> listByMandantLieferantBelegdatumVonBis(EntityManager em, String mandantCNr, Date von, Date bis, LieferantId lieferantId) {
		return HvTypedQuery.<Eingangsrechnung>namedQuery(em, ByMandantLieferantBelegdatumVonBis, mandantCNr, von, bis, lieferantId.id()).getResultList();
	}
}
