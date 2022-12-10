package com.lp.server.finanz.bl.sepa;

import com.lp.server.finanz.service.SepaKontoauszug;

public class SepaKontoauszugCamt052Verifier extends SepaKontoauszugVerifier {

	public SepaKontoauszugCamt052Verifier(String iban) {
		super(iban);
	}

	@Override
	protected boolean validateIndividualFields(SepaKontoauszug ktoauszug) {
		return ktoauszug.hatSalden() ? validateSalden(ktoauszug) : true;
	}

	@Override
	protected boolean verifyIndividual() {
		return true;
	}

}
