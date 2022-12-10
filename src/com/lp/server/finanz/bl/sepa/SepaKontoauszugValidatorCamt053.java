package com.lp.server.finanz.bl.sepa;

import java.util.List;

import com.lp.server.finanz.service.SepaKontoauszug;

public class SepaKontoauszugValidatorCamt053 extends SepaKontoauszugValidator {

	public SepaKontoauszugValidatorCamt053() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void validateSpecific(List<SepaKontoauszug> ktoauszuege) {
		for (SepaKontoauszug ktoauszug : ktoauszuege) {
			validateBetraegeUndSalden(ktoauszug);
		}
	}

}
