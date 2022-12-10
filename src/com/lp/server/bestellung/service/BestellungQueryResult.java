package com.lp.server.bestellung.service;

import com.lp.server.fertigung.service.EnhancedQueryResult;

public class BestellungQueryResult extends EnhancedQueryResult<IBestellungFLRData> {
	private static final long serialVersionUID = 5827926884991376076L;

	public BestellungQueryResult(Object[][] rowData, long rowCount, long indexOfFirstRow, long indexOfLastRow,
			long indexOfSelectedRow) {
		super(rowData, rowCount, indexOfFirstRow, indexOfLastRow, indexOfSelectedRow);
	}

}
