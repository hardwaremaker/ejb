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
package com.lp.server.finanz.ejb;

import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;
import com.lp.util.Helper;

public class BankverbindungQuery {

	public static final String ByMandantCNr = "BankverbindungfindByMandantCnr";
	public static final String ByMandantCNrBFuerSepaLastschrift = "BankverbindungfindByMandantCNrBFuerSepaLastschrift";
	public static final String ByBankIIdMandantCNrCIban = "BankverbindungfindByBankIIdMandantCNrCIban";
	public static final String ByBankIIdMandantCNrCKontonummer = "BankverbindungfindByBankIIdMandantCNrCKontonummer";
	
	public static HvTypedQuery<Bankverbindung> byMandantCNr(EntityManager em, String mandantCNr) {
		HvTypedQuery<Bankverbindung> theQuery = new HvTypedQuery<Bankverbindung>(em.createNamedQuery(ByMandantCNr));
		theQuery.setParameter("mandant", mandantCNr);
		
		return theQuery;
	}
	
	public static List<Bankverbindung> listByMandantCNr(EntityManager em, String mandantCNr) {
		return byMandantCNr(em, mandantCNr).getResultList();
	}
	
	public static HvTypedQuery<Bankverbindung> byMandantCNrBFuerSepaLastschrift(EntityManager em, String mandantCnr, boolean fuerSepaLastschrift) {
		HvTypedQuery<Bankverbindung> theQuery = new HvTypedQuery<Bankverbindung>(em.createNamedQuery(ByMandantCNrBFuerSepaLastschrift));
		theQuery.setParameter("mandant", mandantCnr);
		theQuery.setParameter("fuerSepaLastschrift", Helper.boolean2Short(fuerSepaLastschrift));
		return theQuery;
	}
	
	public static List<Bankverbindung> listByMandantCNrBFuerSepaLastschrift(
			EntityManager em, String mandantCnr, boolean fuerSepaLastschrift) {
		return byMandantCNrBFuerSepaLastschrift(em, mandantCnr, fuerSepaLastschrift).getResultList();
	}
	
	public static HvTypedQuery<Bankverbindung> byBankIIdMandantCNrCIban(EntityManager em, String mandantCnr, Integer bankIId, String cIban) {
		HvTypedQuery<Bankverbindung> theQuery = new HvTypedQuery<Bankverbindung>(em.createNamedQuery(ByBankIIdMandantCNrCIban));
		theQuery.setParameter("mandant", mandantCnr);
		theQuery.setParameter("bank", bankIId);
		theQuery.setParameter("iban", cIban);
		return theQuery;
	}
	
	public static Bankverbindung resultByBankIIdMandantCNrCIban(EntityManager em, String mandantCnr, Integer bankIId, String cIban) {
		return byBankIIdMandantCNrCIban(em, mandantCnr, bankIId, cIban).getSingleResultNoEx();
	}

	public static HvTypedQuery<Bankverbindung> byBankIIdMandantCNrCKontonummer(EntityManager em, String mandantCnr, Integer bankIId, String cKontonummer) {
		HvTypedQuery<Bankverbindung> theQuery = new HvTypedQuery<Bankverbindung>(em.createNamedQuery(ByBankIIdMandantCNrCKontonummer));
		theQuery.setParameter(2, mandantCnr);
		theQuery.setParameter(1, bankIId);
		theQuery.setParameter(3, cKontonummer);
		return theQuery;
	}
	
	public static Bankverbindung resultByBankIIdMandantCNrCKontonummer(EntityManager em, String mandantCnr, Integer bankIId, String cKontonummer) {
		return byBankIIdMandantCNrCKontonummer(em, mandantCnr, bankIId, cKontonummer).getSingleResultNoEx();
	}
	
}
