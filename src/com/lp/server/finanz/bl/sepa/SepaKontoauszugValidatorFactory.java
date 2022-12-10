package com.lp.server.finanz.bl.sepa;

import com.lp.server.finanz.service.ISepaCamtFormat;
import com.lp.server.finanz.service.SepaCamtFormatEnum;

public class SepaKontoauszugValidatorFactory {

	public SepaKontoauszugValidatorFactory() {
	}

	public SepaKontoauszugValidator getValidator(ISepaCamtFormat camtFormat) {
		if (camtFormat == null) return null;
		
		if (SepaCamtFormatEnum.CAMT052.equals(camtFormat.getCamtFormatEnum())) {
			return new SepaKontoauszugValidatorCamt052();
		}

		if (SepaCamtFormatEnum.CAMT053.equals(camtFormat.getCamtFormatEnum())) {
			return new SepaKontoauszugValidatorCamt053();
		}

		return null;
	}
}
