/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2014 HELIUM V IT-Solutions GmbH
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

import java.util.List;

import javax.persistence.EntityManager;

public class PartnerQuery  {
	public final static String ByUID = "PartnerFindByUID" ;
	public final static String ByLowerCName1 = "LowerPartnerfindByCName1" ;
	public final static String ByKbez = "PartnerFindByKbez" ;
	
	/**
	 * Einen Partner anhand seiner UID Nummer ermitteln
	 * 
	 * @param em der EntityManager
	 * @param uidNummer die UID
	 * @return query
	 */
	public static HvTypedQuery<Partner> byUID(EntityManager em, String uidNummer) {
		HvTypedQuery<Partner> theQuery = new HvTypedQuery<Partner>(em.createNamedQuery(ByUID)) ;
		theQuery.setParameter("uid", uidNummer.trim().replace(" ", "")) ;
		return theQuery ;
	}
	
	public static HvTypedQuery<Partner> byLowerCName1(EntityManager em, String partnerCName1) {
		HvTypedQuery<Partner> theQuery = new HvTypedQuery<Partner>(em.createNamedQuery(ByLowerCName1)) ;
		theQuery.setParameter("cname", partnerCName1.toLowerCase()) ;
		return theQuery ;
	}
	
	public static List<Partner> listByLowerCName1(EntityManager em, String partnerCName1) {
		return byLowerCName1(em, partnerCName1).getResultList() ;
	}
	
	public static HvTypedQuery<Partner> byKbez(EntityManager em, String kbez) {
		HvTypedQuery<Partner> theQuery = new HvTypedQuery<Partner>(em.createNamedQuery(ByKbez)) ;
		theQuery.setParameter("kbez", kbez.toLowerCase()) ;
		return theQuery ;
	}
	
	public static List<Partner> listByKbez(EntityManager em, String kbez) {
		return byKbez(em, kbez).getResultList() ;
	}
	
	public static List<Partner> listByUid(EntityManager em, String uidNummer) {
		return byUID(em, uidNummer).getResultList() ;
	}
}
