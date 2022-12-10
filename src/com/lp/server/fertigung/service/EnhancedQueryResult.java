package com.lp.server.fertigung.service;

import com.lp.server.util.fastlanereader.service.query.QueryResult;

public class EnhancedQueryResult<T> extends QueryResult {
	private static final long serialVersionUID = -5484912654345023689L;
	private T[] flrData = null ;
	
	public EnhancedQueryResult(Object[][] rowData, long rowCount, long indexOfFirstRow,
			long indexOfLastRow, long indexOfSelectedRow) {
		super(rowData, rowCount, indexOfFirstRow, indexOfLastRow, indexOfSelectedRow) ;
	}
	
	public void setFlrData(T[] additionalFlrData) {
		flrData = additionalFlrData ;
	}
	
	public T[] getFlrData() {
		return flrData ;
	}
	
	@Override
	public boolean hasFlrData() {
		return flrData != null ;
	}
}
