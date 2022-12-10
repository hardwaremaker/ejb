package com.lp.server.bestellung.service;

import com.lp.server.fertigung.service.EnhancedQueryResult;

public class BestellvorschlagQueryResult extends EnhancedQueryResult<IBestellvorschlagFLRData> {
	private static final long serialVersionUID = -3645586227995140350L;

	public BestellvorschlagQueryResult(Object[][] rowData, long rowCount, long indexOfFirstRow, long indexOfLastRow,
			long indexOfSelectedRow) {
		super(rowData, rowCount, indexOfFirstRow, indexOfLastRow, indexOfSelectedRow);
	}

}
