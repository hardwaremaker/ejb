package com.lp.server.util;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;

import net.sf.jasperreports.engine.data.ListOfArrayDataSource;

public class ReportSqlExecutor implements Serializable {
	private static final long serialVersionUID = -5873251120833451738L;
	private static ILPLogger myLogger = LPLogService.getInstance().getLogger(ReportSqlExecutor.class);
	private Connection sqlConnection ;
	private Statement statement ;
	private boolean _closeStatement;
	
	public ReportSqlExecutor(Connection sqlConnection) {
		this.sqlConnection = sqlConnection ;
		this._closeStatement = true;
	}
	
	public Object execute(String sql) {
//		Object[] results = executeImpl(sql, 1);
		
		ResultSetEvaluator e = new ArrayResultSetEvaluator(1);	
		Object[] results = evaluate(sql, e).asArray();
		return results == null ? null : results[0];
/*		
		if(sqlConnection == null) {
			myLogger.warn("SQL Query '" + sql + " kann kein Ergebnis liefern, da es keine Connection gibt (falsche Konfiguration?). Es wird null verwendet!") ;
			return null ;
		}

		ResultSet rs = null ;
		
		try {
			if(statement == null) {
				statement = sqlConnection.createStatement() ;
			}
			if(statement.execute(sql)) {
				rs = statement.getResultSet();
				ResultSetMetaData rsmd = rs.getMetaData();

				if (rs.next()) {
					if (rsmd.getColumnCount() > 0) {
						return rs.getObject(1);
					}
				}
			} else  {
				return null ;
			}

			myLogger.warn("SQL Query '" + sql + " hat kein Ergebnis gebracht. Es wird null verwendet!") ;
			return null ;
		} catch(SQLException e) {
			myLogger.error("SQLException on executing '" + sql + "'", e);
			return null ;
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					myLogger.error("Resultset Close: " + e.getMessage());
				}
			}
		}
*/
	}
	
	public Object[] executes(String sql) { 
//		Object[] v = executeImpl(sql, 999);
		ResultSetEvaluator e = new ArrayResultSetEvaluator(999);	
		Object[] v = evaluate(sql, e).asArray();
		return v;
	}
	
	public ListOfArrayDataSource subreport(String sql, String[] columnNames) {
		ResultSetEvaluator e = new ListResultSetEvaluator(columnNames.length);	
		List<Object[]> v = evaluate(sql, e).asList();
		return new ListOfArrayDataSource(v, columnNames);
	}
	
	private ResultSetEvaluator evaluate(String sql, ResultSetEvaluator evaluator) {
		myLogger.info("Executing '" + sql + "'...");
		if(sqlConnection == null) {
			myLogger.warn("SQL Query '" + sql + " kann kein Ergebnis liefern, da es keine Connection gibt (falsche Konfiguration?). Es wird null verwendet!") ;
			return evaluator;
		}

		try {
			if(sqlConnection.isClosed()) {
				myLogger.error("My sqlConnection is closed? (sql='" + sql + "') at (" + sqlConnection.toString() + ")");
			}
		} catch(SQLException e) {
			myLogger.error("SQLException (closingTest)", e);
			return evaluator;
		} 

		ResultSet rs = null ;
		
		try {
			if(_closeStatement || (statement == null)) {
				statement = sqlConnection.createStatement() ;
			}

			boolean hasResult = false;
			if(statement.execute(sql)) {
				rs = statement.getResultSet();
				hasResult = evaluator.eval(rs);
			}

			if(!hasResult) {
				myLogger.warn("SQL Query '" + sql +
						"' hat kein Ergebnis gebracht. Es wird null verwendet!") ;				
			}
		} catch(SQLException e) {
			myLogger.error("SQLException on executing '" + sql + "'", e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch(SQLException e) {
					myLogger.error("Resultset Close: " + e.getMessage());
				}
			}
			if(_closeStatement && statement != null) {
				try {
					statement.close();
				} catch(SQLException e) {
					myLogger.error("Can't close statement", e);
				}
				statement = null;
			}
		}
		return evaluator;
	}
	
	private Object[] executeImpl(String sql, int maxResults) {
		myLogger.info("Executing '" + sql + "'...");
		if(sqlConnection == null) {
			myLogger.warn("SQL Query '" + sql + " kann kein Ergebnis liefern, da es keine Connection gibt (falsche Konfiguration?). Es wird null verwendet!") ;
			return null;
		}

		try {
			if(sqlConnection.isClosed()) {
				myLogger.error("My sqlConnection is closed? (sql='" + sql + "') at (" + sqlConnection.toString() + ")");
			}
		} catch(SQLException e) {
			myLogger.error("SQLException (closingTest)", e);
			return null;
		} 

		ResultSet rs = null ;
		
		try {
			if(_closeStatement || (statement == null)) {
				statement = sqlConnection.createStatement() ;
			}

			if(statement.execute(sql)) {
				rs = statement.getResultSet();
				Object[] result = evaluteResultSet(rs, maxResults);
				if(result != null) return result;
			}

			myLogger.warn("SQL Query '" + sql + " hat kein Ergebnis gebracht. Es wird null verwendet!") ;
			return null ;
		} catch(SQLException e) {
			myLogger.error("SQLException on executing '" + sql + "'", e);
			return null ;
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					myLogger.error("Resultset Close: " + e.getMessage());
				}
			}
			if(_closeStatement && statement != null) {
				try {
					statement.close();
				} catch(SQLException e) {
					myLogger.error("Can't close statement", e);
				}
				statement = null;
			}
		}
	}
	
	private Object[] evaluteResultSet(
			ResultSet rs, int maxColumns) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();

		if (rs.next()) {
			int max = Math.min(maxColumns, rsmd.getColumnCount());
			if(max > 0) {
				Object[] result = new Object[max];
				for(int i = 0; i < max; i++) {
					result[i] = rs.getObject(i + 1);
				}
				
				return result;
			}
		}
		
		return null;
	}

	
	public void close() {
		if(statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				myLogger.error("Statement Close: " + e.getMessage());
			} finally {
				statement = null;
			}
		}			
	
//		myLogger.warn("having connection " + (sqlConnection != null) + ".");
		if(sqlConnection != null) {
			try {
//				myLogger.warn("closing connection '" + sqlConnection.hashCode() + " -> " + sqlConnection.toString() + "'...");
				sqlConnection.close();
			} catch (SQLException e) {
				myLogger.error("Connection Close: " + e.getMessage());
			} finally {
				sqlConnection = null;				
			}
		}		
	}
	
	private abstract class ResultSetEvaluator {
		protected final int maxColumns;
		protected ResultSetEvaluator(int maxColumns) {
			this.maxColumns = maxColumns;
		}
		
		public abstract boolean eval(ResultSet rs) throws SQLException;
		
		public abstract Object[] asArray();
		public abstract List<Object[]> asList();
	}
	
	private class ArrayResultSetEvaluator extends ResultSetEvaluator {
		private Object[] result = null;
		
		public ArrayResultSetEvaluator(int maxColumns) {
			super(maxColumns);
		}
		
		public boolean eval(ResultSet rs) throws SQLException {
			boolean hasResult = false;
			ResultSetMetaData rsmd = rs.getMetaData();

			if (rs.next()) {
				int max = Math.min(maxColumns, rsmd.getColumnCount());
				if(max > 0) {
					result = new Object[max];
					for(int i = 0; i < max; i++) {
						result[i] = rs.getObject(i + 1);
					}
					hasResult = true;
				}
			}
			return hasResult;
		}
		
		public Object[] asArray() {
			return result;
		}
		public List<Object[]> asList() {
			return null;
		}
	}
	
	private class ListResultSetEvaluator extends ResultSetEvaluator {
		private List<Object[]> result = null;
		
		public ListResultSetEvaluator(int maxColumns) {
			super(maxColumns);
		}
		
		public boolean eval(ResultSet rs) throws SQLException {
			ResultSetMetaData rsmd = rs.getMetaData();
			int resultColumns = rsmd.getColumnCount();
			if(resultColumns != maxColumns) {
				myLogger.warn("Expected " + maxColumns + " columns, but got " + resultColumns + "!");
				if(resultColumns > maxColumns) {
					resultColumns = maxColumns;
				}
			}

			result = new ArrayList<Object[]>();
			while (rs.next()) {
				Object[] row = new Object[maxColumns];
				for(int i = 0; i < resultColumns; i++) {
					row[i] = rs.getObject(i + 1);
				}
				
				result.add(row);
			}
			return true;
		}
		
		public Object[] asArray() {
			return null;
		}
		public List<Object[]> asList() {
			return result;
		}
	}
}
