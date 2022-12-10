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
package com.lp.server.rechnung.ejb;

import java.sql.Date;
import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;

public class RechnungQuery {

	public static final String ByMandantKundeIIdStatusCNr = "RechnungfindByMandantKundeIIdStatusCNr";
	public static final String ByMandantBelegdatumBisStatusCNr = "RechnungfindByMandantBelegdatumBisStatusCNr";
	public static final String ByMandantStatusCNr = "RechnungfindByMandantStatusCNr";
	public static final String ByCNrMehrereRechnungartCNrMandant = "RechnungfindByCNrMehrereRechnungartCNrMandant";
	
	public static HvTypedQuery<Rechnung> byMandantKundeIIdStatusCNr(EntityManager em, 
			String mandantCNr, Integer kundeIId, List<String> stati) {
		HvTypedQuery<Rechnung> theQuery = new HvTypedQuery<Rechnung>(em.createNamedQuery(ByMandantKundeIIdStatusCNr));
		theQuery.setParameter("mandant", mandantCNr);
		theQuery.setParameter("kunde", kundeIId);
		theQuery.setParameter("stati", stati);
		
		return theQuery;
	}
	
	public static List<Rechnung> listByMandantKundeIIdStatusCNr(EntityManager em, 
			String mandantCNr, Integer kundeIId, List<String> stati) {
		return byMandantKundeIIdStatusCNr(em, mandantCNr, kundeIId, stati).getResultList();
	}
	
	public static HvTypedQuery<Rechnung> byMandantBelegdatumBisStatusCNr(EntityManager em, 
			String mandantCNr, Date belegdatum, List<String> stati) {
		HvTypedQuery<Rechnung> theQuery = new HvTypedQuery<Rechnung>(em.createNamedQuery(ByMandantBelegdatumBisStatusCNr));
		theQuery.setParameter("mandant", mandantCNr);
		theQuery.setParameter("belegdatum", belegdatum);
		theQuery.setParameter("stati", stati);
		
		return theQuery;
	}
	
	public static List<Rechnung> listByMandantBelegdatumBisStatusCNr(EntityManager em, 
			String mandantCNr, Date belegdatum, List<String> stati) {
		return byMandantBelegdatumBisStatusCNr(em, mandantCNr, belegdatum, stati).getResultList();
	}
	
	public static HvTypedQuery<Rechnung> byMandantStatusCNr(EntityManager em, 
			String mandantCNr, List<String> stati) {
		HvTypedQuery<Rechnung> theQuery = new HvTypedQuery<Rechnung>(em.createNamedQuery(ByMandantStatusCNr));
		theQuery.setParameter("mandant", mandantCNr);
		theQuery.setParameter("stati", stati);
		
		return theQuery;
	}
	
	public static List<Rechnung> listByMandantStatusCNr(EntityManager em, 
			String mandantCNr, List<String> stati) {
		return byMandantStatusCNr(em, mandantCNr, stati).getResultList();
	}
	
	public static HvTypedQuery<Rechnung> byCNrMehrereRechnungartCNrMandant(EntityManager em, String cNr, List<String> rechnungarten, String mandantCNr) {
		HvTypedQuery<Rechnung> theQuery = new HvTypedQuery<Rechnung>(em.createNamedQuery(ByCNrMehrereRechnungartCNrMandant));
		theQuery.setParameter("cnr", cNr);
		theQuery.setParameter("rechnungarten", rechnungarten);
		theQuery.setParameter("mandant", mandantCNr);
		
		return theQuery;
	}
	
	public static Rechnung resultByCNrMehrereRechnungartCNrMandant(EntityManager em, String cNr, List<String> rechnungarten, String mandantCNr) {
		return byCNrMehrereRechnungartCNrMandant(em, cNr, rechnungarten, mandantCNr).getSingleResultNoEx();
	}
}
