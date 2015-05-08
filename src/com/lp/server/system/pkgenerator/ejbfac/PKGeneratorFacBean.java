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
package com.lp.server.system.pkgenerator.ejbfac;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import org.jboss.annotation.ejb.Service;

import com.lp.server.system.pkgenerator.ejb.Sequence;
import com.lp.server.system.pkgenerator.service.PKGeneratorFac;
import com.lp.server.util.Facade;
import com.lp.util.EJBExceptionLP;

/**
 * <p>
 * </p>
 * <p>
 * </p>
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * <p>
 * Erstellungsdatum <I>25. 10. 2004</I>
 * </p>
 * 
 * @author Martin Bluehweis
 * @version 1.0
 */

/*
 * Achtung PK Generator ben&ouml;tigt eine eigene Transaction -> daher Bean Managed!
 */

@Service
@TransactionManagement(TransactionManagementType.BEAN)
public class PKGeneratorFacBean extends Facade implements PKGeneratorFac {

	@PersistenceContext(unitName="ejb")
	private EntityManager em;
	//AD
	//@PersistenceUnit(unitName="ejb" ) private EntityManagerFactory emf;
	//private EntityManager em = null;
	///AD
	private UserTransaction tx;   
	@Resource  
	SessionContext stCtx; 
	
	private class Entry {
		Sequence sequence;
		int last;
	};

	private java.util.Hashtable<String, Entry> _entries = new java.util.Hashtable<String, Entry>();
	private int _blockSize = 10;
	private int _retryCount = 5;

/*AD
 	@PostConstruct
	public void Init() {
		em = emf.createEntityManager();
	}
	
	@PreDestroy
	public void Destroy() {
		if (em.isOpen()) {
			em.close();
		}
	}
*/
	
	/**
	 * Den naechsten Primaerschluessel holen
	 * 
	 * @param name
	 *            String
	 * @return Integer
	 */

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Integer getNextPrimaryKey(String name) {
		Sequence sequence = null;
		try {
			// Die Strings muessen Lower Case sein
			if (!name.equals(name.toLowerCase())) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PK_GENERATOR,
						new IllegalArgumentException("Invalid PK Sequence: "
								+ name));
			}
			Entry entry = _entries.get(name);
			if (entry == null) {
				// add an entry to the sequence table
				entry = new Entry();
				sequence = em.find(Sequence.class, name);
				if(sequence == null){
					sequence = new Sequence(name);
					tx = stCtx.getUserTransaction(); 
					try {
						tx.begin();
						//AD utx.begin();
						em.persist(sequence);
						em.flush();
						//AD utx.commit();
						tx.commit();
					} catch (Exception e) {
						try {
							tx.rollback();
							//AD utx.rollback();
						} catch (IllegalStateException e1) {
							myLogger.logData("PK Rollback failed " + e1.getMessage());
						} catch (SecurityException e1) {
							myLogger.logData("PK Rollback failed " + e1.getMessage());
						//AD} catch (SystemException e1) {
						//AD	myLogger.logData("PK Rollback failed " + e1.getMessage());
						}
						throw new EJBExceptionLP(
								EJBExceptionLP.FEHLER_PK_GENERATOR, e);
					} 
				}
				entry.sequence = sequence;
				_entries.put(name, entry);
			}

//			if (name.equals("land")) System.out.println("land_i_id"+entry.last);

			sequence = entry.sequence;
			if (entry.last % _blockSize == 0) {
				for (int retry = 0; true; retry++) {
					try {
						sequence = em.find(Sequence.class, name);
						if (sequence == null) {
							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_PK_GENERATOR, "Fehler im PKGenerator. Es gibt keine PKs f\u00FCr " + name);
						}
						entry.last = sequence.getValueAfterIncrementingBy(_blockSize).intValue();
						tx = stCtx.getUserTransaction(); 
						tx.begin();
						//AD utx.begin();
						em.merge(sequence);
						em.flush();
						//AD utx.commit();
						tx.commit();
						
						entry.sequence=sequence;
						break;
					} catch (Throwable e) {
						try {
							//AD utx.rollback();
							tx.rollback();
						} catch (IllegalStateException e1) {
							myLogger.logData("PK Rollback failed");
						} catch (SecurityException e1) {
							myLogger.logData("PK Rollback failed");
						//AD } catch (SystemException e1) {
						//AD	myLogger.logData("PK Rollback failed");
						}

						if (retry < _retryCount) {
							// we hit a concurrency exception, so try again...
							// myLogger.logData("RETRYING");
							continue;
						} else {
							// we tried too many times, so fail...
							throw new EJBExceptionLP(
									EJBExceptionLP.FEHLER_PK_GENERATOR, "Versuch PK zu holen schlug mehrmals fehl. Zu hohe Anzahl an erneuten Versuchen");
						}
					}
				}
			}
			++entry.last;

			// System.out.println("Tabelle "+name+" Key: "+entry.last +" Hash:" + this.hashCode());

//			if (name.equals("land")) System.out.println("land_i_id: "+entry.last +" Hash:" + this.hashCode());

			return new Integer(entry.last);
		} catch (Exception e) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PK_GENERATOR, e);
		}
	}

	/**
	 * Den letzten vergebenen Primaerschluessel holen
	 * 
	 * @param name
	 *            String
	 * @return Integer
	 */
	public Integer getLastPrimaryKey(String name) {

		Entry entry = _entries.get(name);

		if (entry == null) {
			// myLogger.logData("No sequence defined for: " + name);
			return new Integer(0);
		}
		// myLogger.logData("Last sequence number for: " + name + ": " +
		// (entry.last));
		return new Integer(entry.last);
	}

	/**
	 * Abfragen, ob eine Primaerschluesselsequenz existiert
	 * 
	 * @param name
	 *            String
	 * @return Integer
	 */
	public boolean existsSequence(String name) {
		Sequence sq = em.find(Sequence.class, name);
		if (sq != null)
			return true;
		else
			return false;

	}

	/**
	 * Eine Sequenz erzeugen, falls sie noch nicht existiert
	 * 
	 * @param name
	 *            String
	 */
	public void createSequenceIfNotExists(String name)
		throws EJBExceptionLP {
		if (!existsSequence(name)) {
			Sequence sq = new Sequence(name);
			tx = stCtx.getUserTransaction(); 
			try {
				tx.begin();
				em.persist(sq);
				em.flush();
				tx.commit();
			} catch (Exception e) {
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PK_GENERATOR, e);
			}
		}
	}
}
