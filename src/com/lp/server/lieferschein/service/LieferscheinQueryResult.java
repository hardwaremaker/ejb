package com.lp.server.lieferschein.service;

import com.lp.server.fertigung.service.EnhancedQueryResult;

public class LieferscheinQueryResult extends EnhancedQueryResult<ILieferscheinFLRData> {
	private static final long serialVersionUID = 8870056641792468149L;

	public LieferscheinQueryResult(Object[][] rowData, long rowCount, long indexOfFirstRow, long indexOfLastRow,
			long indexOfSelectedRow) {
		super(rowData, rowCount, indexOfFirstRow, indexOfLastRow, indexOfSelectedRow);
	}

}
