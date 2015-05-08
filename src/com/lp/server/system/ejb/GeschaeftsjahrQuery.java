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
package com.lp.server.system.ejb;

import java.sql.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import com.lp.server.partner.ejb.HvTypedQuery;

public class GeschaeftsjahrQuery {
	public final static String ByDateMandant = "GeschaeftsjahrfindByDatum" ;
	public final static String ByAll = "GeschaeftsjahrfindAll" ;
	public final static String ByYear = "GeschaeftsjahrfindByYear" ;
	
	public static HvTypedQuery<Geschaeftsjahr> byJahr(EntityManager em, Integer geschaeftsJahr) {
		HvTypedQuery<Geschaeftsjahr> gjQuery = new HvTypedQuery<Geschaeftsjahr>(
				em.createNamedQuery(ByYear)) ;
		gjQuery.setParameter("year", geschaeftsJahr) ;
		return gjQuery ;		
	}
	
	public static HvTypedQuery<Geschaeftsjahr> byDatumMandant(EntityManager em, Date date) {
		HvTypedQuery<Geschaeftsjahr> gjQuery = new HvTypedQuery<Geschaeftsjahr>(
				em.createNamedQuery(ByDateMandant)) ;
		gjQuery.setParameter("date", date) ;
			return gjQuery ;
	}
	
	public static HvTypedQuery<Geschaeftsjahr> byAll(EntityManager em, String mandant) {
		HvTypedQuery<Geschaeftsjahr> gjQuery = new HvTypedQuery<Geschaeftsjahr>(
				em.createNamedQuery(ByAll)) ;
		return gjQuery ;		
	}
	
	public static List<Geschaeftsjahr> listByAll(EntityManager em, String mandant) {
		return byAll(em, mandant).getResultList() ;
	}
	
	public static Geschaeftsjahr singleByYear(EntityManager em, Integer year) {
		HvTypedQuery<Geschaeftsjahr> query = byJahr(em, year) ;
		
		try {
			Geschaeftsjahr gj = query.getSingleResult() ;
			return gj ;
		} catch(NoResultException e) {
		} catch(NonUniqueResultException e) {			
		}
		
		return null ;
	}
}
