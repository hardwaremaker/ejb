/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2022 HELIUM V IT-Solutions GmbH
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

import com.lp.server.partner.ejb.HvTypedQuery;

public class GeschaeftsjahrMandantQuery {
	public static final String ByDateMandant = "GeschaeftsjahrmandantfindByDatum";
	public static final String ByMandant = "GeschaeftsjahrmandantfindAll";
	public static final String ByYear    = "GeschaeftsjahrmandantfindAllByYear";
	public static final String ByYearMandant = "GeschaeftsjahrmandantfindByYear";
	public static final String ByOpenMandant = "GeschaeftsjahrmandantfindByOpen";
	
	public static HvTypedQuery<GeschaeftsjahrMandant> byJahrMandant(EntityManager em, Integer geschaeftsJahr, String mandant) {
		HvTypedQuery<GeschaeftsjahrMandant> gjQuery = new HvTypedQuery<GeschaeftsjahrMandant>(
				em.createNamedQuery(ByYearMandant)) ;
		gjQuery.setParameter("year", geschaeftsJahr) ;
		gjQuery.setParameter("mandant", mandant) ;
		return gjQuery ;		
	}
	
	public static HvTypedQuery<GeschaeftsjahrMandant> byJahr(EntityManager em, Integer geschaeftsJahr) {
		HvTypedQuery<GeschaeftsjahrMandant> gjQuery = new HvTypedQuery<GeschaeftsjahrMandant>(
				em.createNamedQuery(ByYear)) ;
		gjQuery.setParameter("year", geschaeftsJahr) ;
		return gjQuery ;		
	}
		
	public static HvTypedQuery<GeschaeftsjahrMandant> byDatumMandant(EntityManager em, Date date, String mandant) {
		HvTypedQuery<GeschaeftsjahrMandant> gjQuery = new HvTypedQuery<GeschaeftsjahrMandant>(
				em.createNamedQuery(ByDateMandant)) ;
		gjQuery.setParameter("date", date) ;
		gjQuery.setParameter("mandant", mandant) ;
		return gjQuery ;
	}
	
	public static HvTypedQuery<GeschaeftsjahrMandant> byMandant(EntityManager em, String mandant) {
		HvTypedQuery<GeschaeftsjahrMandant> gjQuery = new HvTypedQuery<GeschaeftsjahrMandant>(
				em.createNamedQuery(ByMandant)) ;
		gjQuery.setParameter("mandant", mandant) ;
		return gjQuery ;		
	}

	public static HvTypedQuery<GeschaeftsjahrMandant> byOpenMandant(EntityManager em, String mandant) {
		HvTypedQuery<GeschaeftsjahrMandant> gjQuery = new HvTypedQuery<GeschaeftsjahrMandant>(
				em.createNamedQuery(ByOpenMandant)) ;
		gjQuery.setParameter("mandant", mandant) ;
		return gjQuery ;		
	}

	public static List<GeschaeftsjahrMandant> listByMandant(EntityManager em, String mandant) {
		return byMandant(em, mandant).getResultList() ;
	}
	
	public static GeschaeftsjahrMandant singleByYearMandant(EntityManager em, Integer year, String mandant) {
		HvTypedQuery<GeschaeftsjahrMandant> query = byJahrMandant(em, year, mandant) ;
		
		List<GeschaeftsjahrMandant> gj = query.getResultList() ;
		if(gj == null) return null;
		return gj.size() == 1 ? gj.get(0) : null;
	}
	
	public static List<GeschaeftsjahrMandant> listByYear(EntityManager em, Integer year) {
		return byJahr(em, year).getResultList() ;
	}
	
	public static List<GeschaeftsjahrMandant> listByOpenMandant(EntityManager em, String mandant) {
		return byOpenMandant(em, mandant).getResultList();
	}
}
