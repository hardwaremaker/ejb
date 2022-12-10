package com.lp.server.system.ejb;

import java.util.List;

import javax.persistence.EntityManager;

import com.lp.server.partner.ejb.HvTypedQuery;
import com.lp.server.system.ejbfac.EJBExcFactory;
import com.lp.server.system.service.ITablenames;
import com.lp.util.EJBExceptionLP;

public class EntityValidator {

	/**
	 * Pr&uuml;ft, ob das Resultat der NamedQuery ein oder mehrere Ergebnisse liefert.
	 * 
	 * @param em EntityManager
	 * @param queryName Name der NamedQuery
	 * @param params Parameter der NamedQuery, die in der &uuml;bermittelten Reihenfolge der erzeugten 
	 * 		Query gesetzt werden (beginnend mit Nummer 1)
	 * @return <code>true</code> wenn das Resultat der NamedQuery ein oder mehrere Ergebnisse liefert. Sonst <code>false</code>
	 */
	public static boolean validateEntityExistsNoEx(EntityManager em, String queryName, Object... params) {
		return !resultList(em, queryName, params).isEmpty();
	}
	
	/**
	 * Pr&uuml;ft, ob das Resultat der NamedQuery ein oder mehrere Ergebnisse liefert.</br>
	 * Ist dem so, so wird eine entsprechende {@link EJBExceptionLP} geworfen.
	 * 
	 * @param tablename Tabellenname der zu pr&uuml;fenden Entity. @see {@link ITablenames}
	 * @param em EntityManager
	 * @param queryName Name der NamedQuery
	 * @param params Parameter der NamedQuery, die in der &uuml;bermittelten Reihenfolge der erzeugten 
	 * 		Query gesetzt werden (beginnend mit Nummer 1)
	 */
	public static void validateEntityExists(String tablename, EntityManager em, String queryName, Object... params) {
		List<?> entities = resultList(em, queryName, params);
		
		if (entities.isEmpty()) {
			return;
		}
		
		if (entities.size() == 1) {
			throw EJBExcFactory.duplicateUniqueKey(tablename, params);
		}
		
		throw EJBExcFactory.noUniqueResults(tablename, params);
	}
	
	private static List<?> resultList(EntityManager em, String queryName, Object... params) {
		HvTypedQuery<?> hvQuery = HvTypedQuery.namedQuery(em, queryName, params);
		return hvQuery.getResultList();
	}
}
