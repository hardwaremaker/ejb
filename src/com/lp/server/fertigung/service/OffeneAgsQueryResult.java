package com.lp.server.fertigung.service;

public class OffeneAgsQueryResult extends EnhancedQueryResult<IOffeneAgsFLRData> {
	private static final long serialVersionUID = 8680288117589212798L;

	public OffeneAgsQueryResult(Object[][] rowData, long rowCount,
			long indexOfFirstRow, long indexOfLastRow, long indexOfSelectedRow) {
		super(rowData, rowCount, indexOfFirstRow, indexOfLastRow, indexOfSelectedRow);
	}
}
