package com.lp.server.system.ejbfac;

import java.sql.Connection;
import java.sql.SQLException;

import javax.ejb.Local;

@Local
public interface ReportConnectionFacLocal {
	/**
	 * L&ouml;scht s&auml;mtliche zwischengespeicherten Objekte</br>
	 * <p>Sorgt somit daf&uuml;r dass beim n&auml;chsten Aufruf von 
	 * {@link #getConnection(String)} komplett neu aufgebaut wird.
	 * @throws SQLException 
	 */
	void clear() throws SQLException ;
	
	/**
	 * Holt eine SQL Connection
	 * 
	 * @param uniqueId - am besten theClientDto.getIDUser()
	 * @return die Connection
	 * @throws SQLException
	 */
	Connection getConnection(String uniqueId) throws SQLException ;
	
	/**
	 * Holt eine SQL Connection</br>
	 * <p>Wird beim &Ouml;ffnen der Verbindung eine SQLException
	 * geworfen, wird diese als EJBExceptionLP.FEHLER_SQL_EXCEPTION_MIT_INFO
	 * als Runtime-Exception weitergegeben.
	 * 
	 * @param mandantCnr f&uuml;r diesen Mandanten
	 * @return die Connection
	 */
	Connection getConnectionWithEjbEx(String mandantCnr);

	/**
	 * Schliesst f&uuml;r die uniqueId die SQL-Connection und entfernt
	 * die physische Datenbankverbindung aus dem Pool.
	 * 
	 * @param uniqueId - am besten theClientDto.getIDUser()
	 * @param c die Connection
	 * @throws SQLException
	 */
	void closeConnection(String uniqueId, Connection c) throws SQLException;
}
