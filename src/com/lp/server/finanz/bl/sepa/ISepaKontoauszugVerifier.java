package com.lp.server.finanz.bl.sepa;

import com.lp.server.finanz.service.SepaImportTransformResult;

public interface ISepaKontoauszugVerifier {

	SepaImportTransformResult groupAndVerify(SepaImportTransformResult result);
}
