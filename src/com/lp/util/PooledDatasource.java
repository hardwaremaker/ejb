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
package com.lp.util;

import java.sql.SQLException;

import javax.sql.ConnectionPoolDataSource;

public class PooledDatasource {
	private static final PooledDatasource OBJ = new PooledDatasource();
	//private static net.sourceforge.jtds.jdbcx.JtdsDataSource ds;
//	private static Object datasource;
	private static ConnectionPoolDataSource datasource ;
	private static boolean initialized = false;
	private static javax.sql.PooledConnection pc = null;
	private static String cachedConnectionUrl = null ;
			
	private PooledDatasource() {
	}

	public static PooledDatasource getInstance() {
		return OBJ;
	}
	
	protected synchronized void initializeFrom(String url) {
		// jdbc:jtds:sqlserver://localhost:1433/LP
		// jdbc:postgresql://localhost:5432/LP
		String host;
		int port;
		String database;
		host = "";
		port = 0;
		database = "";

		try {
			int i = url.indexOf("://");
			int j = url.indexOf(":", i+1);
			host = url.substring(i+3,j);
			i = j+1;
			j = url.indexOf("/", i);		
			port = Integer.parseInt(url.substring(i, j));
			database = url.substring(j+1);
		} catch (NumberFormatException e1) {
			e1.printStackTrace();
		}
		
		if (host.length()==0 || database.length()==0 || port==0)
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_INVALID_REPORT_URL, "Ungueltige Reportconnection: " + url);

		if (url.contains("jtds")) {
			initJtds(host, port, database);
		} else if (url.contains("postgresql")) {
			initPsql(host, port, database);
		} else
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_INVALID_REPORT_URL, "Ungueltige Reportconnection: " + url);

		cachedConnectionUrl = url ;			
	}

	protected synchronized void initPsql(String host, int port, String database) {
		org.postgresql.ds.PGConnectionPoolDataSource ds = new org.postgresql.ds.PGConnectionPoolDataSource();
		ds.setServerName(host);
		ds.setDatabaseName(database);
		ds.setPortNumber(port);
		ds.setUser("hvguest");
		ds.setPassword("h4gzfdavfs");

		datasource = ds;
		pc = null ;
		initialized = true;
	}
	
	protected synchronized void initJtds(String host, int port, String database) {
		net.sourceforge.jtds.jdbcx.JtdsDataSource ds = new net.sourceforge.jtds.jdbcx.JtdsDataSource();
		
		String instance = null;
		if (database.toLowerCase().contains("instance")) {
			int i = database.toLowerCase().indexOf("instance");
			int j = database.indexOf(";", i);
			if (j==-1)
				instance = database.substring(i+9);
			else
				instance = database.substring(i+9, j);
			database = database.substring(0,i-1);
		}
		ds.setServerName(host);
		ds.setDatabaseName(database);
		if (instance != null)
			ds.setInstance(instance);
		ds.setPortNumber(port);
		ds.setUser("hvguest");
		ds.setPassword("h4gzfdavfs");
		ds.setMaxStatements(5);

		try {
			ds.setSocketTimeout(300);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		datasource = ds;
		pc = null ;
		initialized = true;
	}

	public boolean isInitalized() {
		return initialized;
	}
	
	public javax.sql.PooledConnection getPooledConnection() throws SQLException {
//		return ((ConnectionPoolDataSource)datasource).getPooledConnection();
		return datasource.getPooledConnection() ;
	}
	
	public synchronized java.sql.Connection getConnection() throws SQLException {
		if (pc == null) {
//			pc = ((ConnectionPoolDataSource)datasource).getPooledConnection();
			pc = datasource.getPooledConnection() ;
		}
		return pc.getConnection();
	}
	
	/**
	 * Setzt die Connection-URL</br>
	 * <p>&Auml;ndert sich die URL wird die darunterliegende Datasource neu 
	 * aufgebaut. Ansonsten wird eine bereits bestehende verwendet</p>
	 * @param url die neue Database-Connection-URL. Kann auch null sein.
	 */
	public synchronized void setUrl(String url) {
		if(url == null) {
			cachedConnectionUrl = null ;
			initialized = false ;
			pc = null ;				
			datasource = null ;
		} else {
			if(!url.equals(cachedConnectionUrl)) {
				initializeFrom(url) ;
			}
			
//			if(cachedConnectionUrl == null) {
//				initializeFrom(url) ;
//			} else {
//				if(url.compareTo(cachedConnectionUrl) != 0) {
//					initializeFrom(url) ;
//				}				
//			}
		}			
	}
}
