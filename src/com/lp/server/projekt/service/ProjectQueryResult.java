package com.lp.server.projekt.service;

import com.lp.server.fertigung.service.EnhancedQueryResult;

public class ProjectQueryResult extends EnhancedQueryResult<IProjektFLRData> {
	private static final long serialVersionUID = -8205828667057583050L;

	public ProjectQueryResult(Object[][] rowData, long rowCount, long indexOfFirstRow,
			long indexOfLastRow, long indexOfSelectedRow) {
		super(rowData, rowCount, indexOfFirstRow, indexOfLastRow, indexOfSelectedRow) ;
	}
}
