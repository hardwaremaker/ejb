package com.lp.server.personal.service;

import com.lp.server.fertigung.service.EnhancedQueryResult;

public class MaschineQueryResult extends EnhancedQueryResult<IMaschineFLRData> {

	private static final long serialVersionUID = -4546675052204573857L;

	public MaschineQueryResult(Object[][] rowData, long rowCount,
			long indexOfFirstRow, long indexOfLastRow, long indexOfSelectedRow) {
		super(rowData, rowCount, indexOfFirstRow, indexOfLastRow, indexOfSelectedRow);
	}

}
