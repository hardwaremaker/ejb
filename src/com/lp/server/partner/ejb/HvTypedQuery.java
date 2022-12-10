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

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import com.lp.server.util.Validator;

/**
 * Ein "poor man's" Versuch einer typisierten javax.persistence.Query
 * Sie verh&auml;lt sich ident wie ihre normale Query, liefert aber in
 * getResultList() und getSingleResult() das typisierte Ergebnis zurueck.
 * 
 * @author Gerold
 * @param <T> 
 */
public class HvTypedQuery<T> implements Query{
	private Query query ;
	
	public HvTypedQuery(Query untypedQuery) {
		Validator.notNull(untypedQuery, "untypedQuery") ;
		query = untypedQuery ;
	}

	public Query getQuery() {
		return query ;
	}
	
	public int executeUpdate() {
		return query.executeUpdate() ;
	}

	public List<T> getResultList() {
		return (List<T>) query.getResultList() ;
	}

	@Override
	public T getSingleResult() {
		return (T) query.getSingleResult() ;
	}

	@Override
	public HvTypedQuery<T> setFirstResult(int arg0) {
		query.setFirstResult(arg0);
		return this ;
	}

	@Override
	public HvTypedQuery<T> setFlushMode(FlushModeType arg0) {
		query.setFlushMode(arg0);
		return this ;
	}

	@Override
	public HvTypedQuery<T> setHint(String arg0, Object arg1) {
		query.setHint(arg0, arg1);
		return this ;
	}

	@Override
	public HvTypedQuery<T> setMaxResults(int arg0) {
		query.setMaxResults(arg0);
		return this ;
	}

	@Override
	public HvTypedQuery<T> setParameter(String arg0, Object arg1) {
		query.setParameter(arg0, arg1);
		return this ;
	}

	@Override
	public HvTypedQuery<T> setParameter(int arg0, Object arg1) {
		query.setParameter(arg0, arg1);
		return this ;
	}

	@Override
	public HvTypedQuery<T> setParameter(String arg0, Date arg1, TemporalType arg2) {
		query.setParameter(arg0, arg1, arg2);
		return this ;
	}

	@Override
	public Query setParameter(String arg0, Calendar arg1, TemporalType arg2) {
		query.setParameter(arg0,  arg1, arg2);
		return this ;
	}

	@Override
	public Query setParameter(int arg0, Date arg1, TemporalType arg2) {
		query.setParameter(arg0, arg1, arg2) ;
		return this ;
	}

	@Override
	public Query setParameter(int arg0, Calendar arg1, TemporalType arg2) {
		query.setParameter(arg0, arg1, arg2);
		return this ;
	}

	/**
	 * Einen (erwarteten) Ergebnissatz zur&uuml;ckliefern</br>
	 * <p>Gibt es keinen, oder mehr als einen Datensatz, wird null geliefert</p>
	 * @return null wenn es keinen oder mehr als einen Satz gibt, ansonsten den gesuchten Satz
	 */
	public T getSingleResultNoEx() {
		List<T> resultList = (List<T>) query.getResultList() ;
		return (resultList == null || resultList.size() != 1)
				? null : resultList.get(0) ;
	}

	/**
	 * Erzeugt eine <code>HvTypedQuery</code> &uuml;ber einen Namen einen <code>NamedQuery</code>
	 * und ihren ben&ouml;tigten Parametern 
	 * 
	 * @param em EntityManager
	 * @param queryName Name der NamedQuery
	 * @param params Parameter der NamedQuery, die in der &uuml;bermittelten Reihenfolge der erzeugten 
	 * 		Query gesetzt werden (beginnend mit Nummer 1)
	 * @return erzeugte <code>HvTypedQuery</code>
	 */
	public static <T> HvTypedQuery<T> namedQuery(EntityManager em, String queryName, Object... params) {
		HvTypedQuery<T> theQuery = new HvTypedQuery<T>(em.createNamedQuery(queryName));
		int paramNr = 1;
		for (Object p : params) {
			theQuery.setParameter(paramNr++, p);
		}
		return theQuery;
	}
}
