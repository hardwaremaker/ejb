package com.lp.server.system.ejbfac;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.Singleton;
import javax.sql.ConnectionEvent;
import javax.sql.ConnectionEventListener;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.PooledConnection;

import com.lp.server.system.service.ParameterFac;
import com.lp.server.util.Facade;
import com.lp.util.EJBExceptionLP;
@Singleton
public class ReportConnectionFacBean extends Facade implements ReportConnectionFacLocal {
	private Map<String, ConnectionData> pools ;
	
	@Override
	public void clear() throws SQLException {
		if(pools == null) {
			createPools(); 
		}
		for (ConnectionData connectionData : pools.values()) {
			connectionData.close(); 
		}
		
		pools.clear() ;
	}

	protected void createPools() {
		pools = new HashMap<String, ReportConnectionFacBean.ConnectionData>() ;		
	}
	
	protected ConnectionData peekConnectionData(String uniqueId) throws SQLException {
		if(pools == null) {
			createPools(); 
		}
		return pools.get(uniqueId);
	}
	
	protected ConnectionData getConnectionData(String uniqueId) throws SQLException {
		if(pools == null) {
			createPools(); 
		}
	
		String url = paramReportConnectionUrl() ;
		ConnectionData connectionData = pools.get(uniqueId);
		if(connectionData == null) {
			if(url != null) {
				connectionData = createConnectionData(url);
				pools.put(uniqueId, connectionData) ;
			}
		} else {
			connectionData = connectionData.changeUrl(url);
			if(connectionData == null) {
				pools.put(uniqueId, connectionData) ;
			}
		}

		return connectionData ;
	}
	
	protected ConnectionData createConnectionData(String url) {
		if (url != null) {
			return new ConnectionData(url.trim()) ;
		} 

		throw new EJBExceptionLP(EJBExceptionLP.FEHLER_PARAMETER_IS_NULL,
			ParameterFac.ANWENDERPARAMETER_REPORT_CONNECTION_URL);
	}
	
	@Override
	public Connection getConnection(String uniqueId) throws SQLException {
		ConnectionData connectionData = getConnectionData(uniqueId) ;
		return connectionData == null ? null : connectionData.getConnection() ;
	}

	@Override
	public Connection getConnectionWithEjbEx(String uniqueId) {
		try {
			return getConnection(uniqueId);
		} catch(SQLException e) {
			throw EJBExcFactory.sqlFehler(e, paramReportConnectionUrl());
		}
	}
	
	@Override
	public void closeConnection(String uniqueId, Connection c) throws SQLException {
		ConnectionData connectionData = peekConnectionData(uniqueId);
		if(connectionData == null) {
			myLogger.warn("Tried to close a pooled connection for '" + uniqueId + "' which doesn't exist anymore");
			return;
		}
		
		try {
			if(c != null) {
				c.close();
			}
		} catch(SQLException e) {
			myLogger.info("Closing sql connection failed. (ignored)", e);
		}
		
		connectionData.close();
		pools.remove(uniqueId);
	}
	
	
	/**
	 * Den Anwenderparameter REPORT_CONNECTION_URL laden
	 * 
	 * @return null wenn der Parameter nicht gesetzt bzw.keinen Inhalt hat, 
	 * ansonsten die ge'trim()'te Url
	 */
	private String paramReportConnectionUrl() {
		String url = getParameterFac().getAnwenderparameter(
				ParameterFac.KATEGORIE_ALLGEMEIN,
				ParameterFac.ANWENDERPARAMETER_REPORT_CONNECTION_URL)
				.getCWert();
		if(url != null) {
			url = url.trim() ;
			if(url.length() == 0) {
				url = null ;
			}
		}
		
		return url ;
	}
	
	private class ConnectionData implements ConnectionEventListener {
		private ConnectionPoolDataSource dataSource ;
		private PooledConnection pooledConnection ;
		private String cachedUrl ;
		
		public ConnectionData(String url) {			
			initializeFrom(url);
			cachedUrl = "" + url ;
		}

		public Connection getConnection() throws SQLException {
			return getPooledConnection().getConnection() ;
		}
		
		public void close() throws SQLException {
			if(pooledConnection != null) {
				pooledConnection.close();
			}
			cachedUrl = null ;
			pooledConnection = null ;
			dataSource = null ;
		}
		
		public ConnectionData changeUrl(String url) throws SQLException {
			if(url == null || !url.equals(cachedUrl)) {
				close() ;
				
				if(url == null) return null ; 
				
				initializeFrom(url);
				cachedUrl = "" + url ;
			}
			
			return this ;
		}
		
		protected void initializeFrom(String url) {
			// jdbc:jtds:sqlserver://localhost:1433/LP
			// jdbc:postgresql://localhost:5432/LP
			String host = "" ;
			int port = 0 ;
			String database = "";
 
			try {
				int i = url.indexOf("://");
				int j = url.indexOf(":", i+1);
				host = url.substring(i+3,j);
				i = j+1;
				j = url.indexOf("/", i);		
				port = Integer.parseInt(url.substring(i, j));
				database = url.substring(j+1);
			} catch (NumberFormatException e1) {
				myLogger.warn("NumberFormatException bei '" + url + "'", e1 ) ;
			}
			
			if (host.length()==0 || database.length()==0 || port==0)
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_INVALID_REPORT_URL, "Ungueltige Reportconnection: " + url);

			if (url.contains("jtds")) {
				dataSource = initJtds(host, port, database);
			} else if (url.contains("postgresql")) {
				dataSource = initPsql(host, port, database);
			} else
				throw new EJBExceptionLP(EJBExceptionLP.FEHLER_INVALID_REPORT_URL, "Ungueltige Reportconnection: " + url);
		}
		
		protected ConnectionPoolDataSource initPsql(String host, int port, String database) {
			org.postgresql.ds.PGConnectionPoolDataSource ds = new org.postgresql.ds.PGConnectionPoolDataSource();
			ds.setServerName(host);
			ds.setDatabaseName(database);
			ds.setPortNumber(port);
			ds.setUser("hvguest");
			ds.setPassword("h4gzfdavfs");

			return ds ;
		}		
		
		protected ConnectionPoolDataSource initJtds(String host, int port, String database) {
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
				myLogger.warn("SQLException setting SocketTimeout", e);
			}
		
			return ds ;
		}
		
		protected PooledConnection getPooledConnection() throws SQLException {
			if(pooledConnection == null) {
				pooledConnection = getDataSource().getPooledConnection();
				pooledConnection.addConnectionEventListener(this);
				
				myLogger.info("new pooledConnection");
			}
		
			return pooledConnection ;
		}
		
		protected ConnectionPoolDataSource getDataSource() {
			return dataSource ;
		}
		
		@Override
		public void connectionClosed(ConnectionEvent event) {
			myLogger.info("connectionClosedEvent on " + (event.getSource()), event.getSQLException());
		}
		
		@Override
		public void connectionErrorOccurred(ConnectionEvent event) {
			myLogger.info("connectionErrorEvent on " + (event.getSource()), event.getSQLException());
			
		}
	}
}
