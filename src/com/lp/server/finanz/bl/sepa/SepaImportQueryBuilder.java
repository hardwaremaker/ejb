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
package com.lp.server.finanz.bl.sepa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.lp.server.finanz.service.SepaBuchung;
import com.lp.server.finanz.service.SepaZahlung;
import com.lp.server.partner.ejb.HvTypedQuery;
import com.lp.server.stueckliste.ejbfac.ImportQueryBuilder;

public abstract class SepaImportQueryBuilder {
	
	public static final String Eingangsrechnung = "eingangsrechnung";
	public static final String Ausgangsrechnung = "rechnung";
	public static final String Zahlungsvorschlag = "zahlungsvorschlag";
	public static final String Partner = "partner";
	public static final String Lastschriftvorschlag = "lastschrift";
	
	public static final String SELECT_EINGANGSRECHNUNG_I_ID = "SELECT " + Eingangsrechnung + ".i_id ";
	public static final String FROM_EINGANGSRECHNUNG = "FROM FLREingangsrechnung " + Eingangsrechnung;
	public static final String FROM_ZAHLUNGSVORSCHLAG = "FROM FLRZahlungsvorschlag " + Zahlungsvorschlag;
	public static final String JOIN_EINGANGSRECHNUNG = 
			"LEFT JOIN " + Zahlungsvorschlag + ".flreingangsrechnung AS " + Eingangsrechnung;
	public static final String JOIN_KUNDE_PARTNER = 
			"LEFT JOIN " + Ausgangsrechnung + ".flrkunde.flrpartner AS " + Partner;
	public static final String JOIN_LIEFERANT_PARTNER = 
			"LEFT JOIN " + Eingangsrechnung + ".flrlieferant.flrpartner AS " + Partner;
	
	public static final String SELECT_AUSGANGSRECHNUNG_I_ID = "SELECT " + Ausgangsrechnung + ".i_id ";
	public static final String FROM_AUSGANGSRECHNUNG = "FROM FLRRechnung " + Ausgangsrechnung;
	public static final String FROM_LASTSCHRIFTVORSCHLAG = "FROM FLRLastschriftvorschlag " + Lastschriftvorschlag;
	
	public static final String JOIN_RECHNUNGREPORT = 
			"LEFT JOIN " + Lastschriftvorschlag + ".flrrechnungreport AS " + Ausgangsrechnung;
	
	protected List<String> froms;
	protected List<String> wheres;
	protected String critValue;
	protected String mandantCNr;
	protected List<String> stati;
	protected List<Object> params;
	
	public SepaImportQueryBuilder(String mandantCNr, List<String> stati) {
		froms = new ArrayList<String>();
		buildFromQuery();
		wheres = new ArrayList<String>();
		this.mandantCNr = mandantCNr;
		this.stati = stati;
		params = new ArrayList<Object>();
	}
	
	public List<Integer> getResultList(EntityManager em, List<Integer> items) {
		List<Integer> results = getResultList(em);
		
		if (items != null && items.size() > 0) {
			results.retainAll(items);
		}
		
		if (results.size() >= 1) {
			return results;
		}
		
		return items; 
	}
	
	/**
	 * Liefert die Ergebnisliste der Ids den gefundenen Eingangsrechnungen,
	 * basierend auf dem jeweiligen Kriterium
	 * 
	 * @param em EntityManager fuer die HSQL-Abfrage
	 * @return die Ergebnisliste aller gefundenen Eingangsrechnung-Ids
	 */
	public List<Integer> getResultList(EntityManager em) {
		buildWhereQuery();
		String sQuery = ImportQueryBuilder.buildQuery(froms, wheres);

		Query query = em.createQuery(sQuery);
		setParameter(query);
		HvTypedQuery<Integer> hvQuery = new HvTypedQuery<Integer>(query);
		return hvQuery.getResultList();
	}
	
	private void setParameter(Query query) {
		Integer count = 0;
		for (Object p : params) {
			query.setParameter(++count, p);
		}
	}
	/**
	 * Initialisiert die benoetigte SELECT FROM Clause
	 */
	protected abstract void buildFromQuery();
	
	protected void buildWhereQuery() {
		buildBasicWheres();
		buildWhereCriterion();
	}

	/**
	 * 
	 */
	protected abstract void buildBasicWheres();

	protected void buildBasicWheres(String flrbeleg) {
		wheres.add(flrbeleg + ".mandant_c_nr = '" + mandantCNr + "'");
		
		StringBuilder statiWhere = new StringBuilder();
		statiWhere.append("(");
		for (String status : stati) {
			statiWhere.append(flrbeleg + ".status_c_nr = '" + status + "'");
			if (stati.indexOf(status) != stati.size()-1) {
				statiWhere.append(" OR ");
			}
		}
		statiWhere.append(")");
		
		wheres.add(statiWhere.toString());
	}
	/**
	 * Bildet die WHERE Clause fuer das aktuelle Kriterium
	 */
	protected abstract void buildWhereCriterion();
	
	/**
	 * Setzt den Variablenwert des aktuellen Kriteriums
	 * 
	 * @param payment Sepa-Zahlung
	 * @param booking Sepa-Buchung
	 * @return false, wenn der benoetigte Variablenwert null ist;
	 * anderenfalls true
	 */
	public boolean setValueForCriterion(SepaZahlung payment, SepaBuchung booking) {
		wheres.clear();
		
		return setValueForCriterionImpl(payment, booking);
	}
	
	protected abstract boolean setValueForCriterionImpl(SepaZahlung payment, SepaBuchung booking);
	
	
	protected void setCritValue(String critValue) {
		this.critValue = critValue;
	}

	protected String getCritValueWithSingleQuotes() {
		return "'" + getCritValue() + "'";
	}
	
	protected String getCritValueWithProzent() {
		return "%" + getCritValue() + "%";
	}

	protected String getCritValue() {
		return critValue;
	}
	
	protected String getLikeClauseForCNrSearch(String flrbeleg) {
		return " LIKE '%' || " + flrbeleg + ".c_nr || '%'";
	}
	
	public abstract boolean isTotalMatch();
	
	protected void addWhere(String where, Object... params) {
		wheres.add(where);
		this.params = new ArrayList<Object>();
		for (Object p : params) {
			this.params.add(p);
		}
	}
}
