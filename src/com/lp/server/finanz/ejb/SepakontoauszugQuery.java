/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2016 HELIUM V IT-Solutions GmbH
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
package com.lp.server.finanz.ejb;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;

public class SepakontoauszugQuery {

	public final static String ByBankverbindungIIdIAuszug = "SepakontoauszugByBankverbindungIIdIAuszug";
	public final static String ByBankverbindungIIdIAuszugNotStatusCNr = "SepakontoauszugByBankverbindungIIdIAuszugStatusCNr";
	public final static String ByBankverbindungIIdStatusCNrOrderedByIAuszugAsc = "SepakontoauszugByBankverbindungIIdStatusCNrOrderedByIAuszugAsc";
	public final static String ByBankverbindungIIdIAuszugNotStatusCNrTAuszugBetween = "SepakontoauszugByBankverbindungIIdIAuszugStatusCNrTAuszugBetween";
	
	public static HvTypedQuery<Sepakontoauszug> byBankverbindungIIdIAuszug(EntityManager em, Integer bankverbindungIId, Integer iAuszug) {
		HvTypedQuery<Sepakontoauszug> theQuery = new HvTypedQuery<Sepakontoauszug>(em.createNamedQuery(ByBankverbindungIIdIAuszug));
		theQuery.setParameter("auszug", iAuszug);
		theQuery.setParameter("bankverbindung", bankverbindungIId);
		
		return theQuery;
	}
	
	public static Sepakontoauszug findByBankverbindungIIdIAuszug(EntityManager em, Integer bankverbindungIId, Integer iAuszug) {
		return byBankverbindungIIdIAuszug(em, bankverbindungIId, iAuszug).getSingleResult();
	}
	
	public static HvTypedQuery<Sepakontoauszug> byBankverbindungIIdIAuszugNotStatusCNr(EntityManager em, 
			Integer bankverbindungIId, Integer iAuszug, String status) {
		HvTypedQuery<Sepakontoauszug> theQuery = new HvTypedQuery<Sepakontoauszug>(em.createNamedQuery(ByBankverbindungIIdIAuszugNotStatusCNr));
		theQuery.setParameter("auszug", iAuszug);
		theQuery.setParameter("bankverbindung", bankverbindungIId);
		theQuery.setParameter("status", status);
		
		return theQuery;
	}
	
	public static Sepakontoauszug findByBankverbindungIIdIAuszugNotStatusCNr(EntityManager em, 
			Integer bankverbindungIId, Integer iAuszug, String status) {
		return byBankverbindungIIdIAuszugNotStatusCNr(em, bankverbindungIId, iAuszug, status).getSingleResult();
	}
	
	public static HvTypedQuery<Sepakontoauszug> byBankverbindungIIdStatusCNrMinIAuszug(EntityManager em, 
			Integer bankverbindungIId, List<String> stati) {
		HvTypedQuery<Sepakontoauszug> theQuery = new HvTypedQuery<Sepakontoauszug>(em.createNamedQuery(ByBankverbindungIIdStatusCNrOrderedByIAuszugAsc));
		theQuery.setParameter("bankverbindung", bankverbindungIId);
		theQuery.setParameter("stati", stati);
		theQuery.setFirstResult(0).setMaxResults(1);
		
		return theQuery;
	}
	
	public static Sepakontoauszug findByBankverbindungIIdStatusCNrMinIAuszug(EntityManager em, 
			Integer bankverbindungIId, List<String> stati) {
		return byBankverbindungIIdStatusCNrMinIAuszug(em, bankverbindungIId, stati).getSingleResult();
	}
	
	public static Collection<Sepakontoauszug> findByBankverbindungIIdIAuszugNotStatusCNrTAuszug(EntityManager em, 
			Integer bankverbindungIId, Integer iAuszug, String status, Timestamp tMinDate, Timestamp tMaxDate) {
		return new HvTypedQuery<Sepakontoauszug>(em.createNamedQuery(ByBankverbindungIIdIAuszugNotStatusCNrTAuszugBetween))
				.setParameter("auszug", iAuszug)
				.setParameter("bankverbindung", bankverbindungIId)
				.setParameter("status", status)
				.setParameter("mindate", tMinDate)
				.setParameter("maxdate", tMaxDate).getResultList();
	}
}
