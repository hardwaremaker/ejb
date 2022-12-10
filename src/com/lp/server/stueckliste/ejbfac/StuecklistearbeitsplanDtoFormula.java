package com.lp.server.stueckliste.ejbfac;

import com.lp.service.plscript.ScriptFormula;

public class StuecklistearbeitsplanDtoFormula extends ScriptFormula<Void> {
	public StuecklistearbeitsplanDtoFormula(Integer id, String src) {
		super(id, src);
	}

	@Override
	public String getResultType() {
		return "java.lang.Long";
	}
	
	@Override
	public void storeResult(Object result) {
	}
}
