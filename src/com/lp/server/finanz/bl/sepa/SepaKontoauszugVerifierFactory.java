package com.lp.server.finanz.bl.sepa;

import com.lp.server.finanz.service.ISepaCamtFormat;
import com.lp.server.finanz.service.SepaCamtFormatEnum;

public class SepaKontoauszugVerifierFactory {

	public ISepaKontoauszugVerifier getVerifier(ISepaCamtFormat camtFormat, String iban) {
		if (SepaCamtFormatEnum.CAMT052.equals(camtFormat.getCamtFormatEnum())) {
			return new SepaKontoauszugCamt052Verifier(iban);
		}
		if (SepaCamtFormatEnum.CAMT053.equals(camtFormat.getCamtFormatEnum())) {
			return new SepaKontoauszugCamt053Verifier(iban);
		}
		
		throw new IllegalArgumentException("No verifier for camtFormat '" + camtFormat.getCamtFormatEnum() + "'");
	}
}
