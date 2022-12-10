package com.lp.server.rechnung.service;

import com.lp.server.fertigung.service.EnhancedQueryResult;

public class RechnungQueryResult extends EnhancedQueryResult<IRechnungFLRData> {
	private static final long serialVersionUID = 3144344793590606453L;

	public RechnungQueryResult(Object[][] rowData, long rowCount, long indexOfFirstRow,
			long indexOfLastRow, long indexOfSelectedRow) {
		super(rowData, rowCount, indexOfFirstRow, indexOfLastRow, indexOfSelectedRow) ;
	}
}
