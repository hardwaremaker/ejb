package com.lp.server.finanz.bl.sepa;

import com.lp.server.finanz.service.SepaKontoauszug;

public class SepaKontoauszugCamt053Verifier extends SepaKontoauszugVerifier {

	public SepaKontoauszugCamt053Verifier(String iban) {
		super(iban);
	}

	@Override
	protected boolean validateIndividualFields(SepaKontoauszug ktoauszug) {
		return validateSalden(ktoauszug);
	}

	@Override
	protected boolean verifyIndividual() {
		return verifyPageNumbers();
	}

}
