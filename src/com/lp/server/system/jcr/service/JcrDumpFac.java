package com.lp.server.system.jcr.service;

import java.util.Collection;

import javax.ejb.Remote;
import javax.jcr.RepositoryException;
import javax.naming.NamingException;

@Remote
public interface JcrDumpFac {
	/**
	 * Fuehrt einen Scan des Source-Repositories durch</br>
	 * <p>Es werden dabei alle "Root-Pfade" (Belegtypen) ermittelt.</p>
	 * 
	 * @return eine (leere) Liste von Root-Pfaden
	 * @throws NamingException
	 * @throws RepositoryException
	 */
	JcrScanResult scan() throws NamingException, RepositoryException;
	
	JcrDumpResult dumpPath(String rootPath) throws NamingException, RepositoryException;
	
	/**
	 * Fuehrt einen Dump des Source-Repositories auf das Target-Repository durch</br>
	 * <p>Es werden dabei die Default-Repositories jcr/local (source) und jcr/dump (target) angenommen</p>
	 * 
	 * @return das Ergebnis des Dumps
	 * @throws NamingException
	 * @throws RepositoryException
	 */
	JcrDumpResult dump() throws NamingException, RepositoryException ;
	
	
	/**
	 * Fuehrt einen Dump des Source-Repositories auf das Target-Repository durch
	 * @param sourceConfig die Beschreibung des Source-Repositories
	 * @param targetConfig die Beschreibung des Target-Repositories
	 * 
	 * @return das Ergebnis des Dumps
	 * @throws NamingException
	 * @throws RepositoryException
	 */
	JcrDumpResult dump(JcrDmsConfig sourceConfig, JcrDmsConfig targetConfig) throws NamingException, RepositoryException ;


	JcrScanResult scan(JcrDmsConfig sourceConfig) throws NamingException, RepositoryException;


	JcrDumpResult dumpPath(String rootPath, JcrDmsConfig sourceConfig, JcrDmsConfig targetConfig)
			throws NamingException, RepositoryException;
}
