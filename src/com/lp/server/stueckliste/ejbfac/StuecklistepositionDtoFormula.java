package com.lp.server.stueckliste.ejbfac;

import com.lp.service.plscript.ScriptFormula;

public class StuecklistepositionDtoFormula extends ScriptFormula<Void> {
	public StuecklistepositionDtoFormula(Integer id, String src) {
		super(id, src);
	}
	

	@Override
	public String getResultType() {
		return "java.math.BigDecimal";
	}

	@Override
	public void storeResult(Object result) {
	}
}
