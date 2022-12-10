package com.lp.server.fertigung.service;

public class LosQueryResult extends EnhancedQueryResult<ILosFLRData> {
	private static final long serialVersionUID = -4053973660893910322L;

	public LosQueryResult(Object[][] rowData, long rowCount, long indexOfFirstRow,
			long indexOfLastRow, long indexOfSelectedRow) {
		super(rowData, rowCount, indexOfFirstRow, indexOfLastRow, indexOfSelectedRow) ;
	}
}
