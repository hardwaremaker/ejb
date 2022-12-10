package com.lp.server.stueckliste.ejbfac;

import java.math.BigDecimal;

import com.lp.server.stueckliste.fastlanereader.generated.FLRStuecklisteposition;
import com.lp.service.plscript.ScriptFormula;

public class FLRStuecklistepositionFormula extends ScriptFormula<FLRStuecklisteposition> {
	public FLRStuecklistepositionFormula(Integer id, String src) {
		super(id, src);
	}

	public FLRStuecklistepositionFormula(Integer id, String src, FLRStuecklisteposition payload) {
		super(id, src, payload);
	}

	
	@Override
	public String getResultType() {
		return "java.math.BigDecimal";
	}
	
	@Override
	public void storeResult(Object result) {
		getPayload().setN_menge((BigDecimal) result);
	}
}
