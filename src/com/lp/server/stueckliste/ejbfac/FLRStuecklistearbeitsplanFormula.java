package com.lp.server.stueckliste.ejbfac;

import com.lp.server.stueckliste.fastlanereader.generated.FLRStuecklistearbeitsplan;
import com.lp.service.plscript.ScriptFormula;

public class FLRStuecklistearbeitsplanFormula extends ScriptFormula<FLRStuecklistearbeitsplan> {
	public FLRStuecklistearbeitsplanFormula(Integer id, String src) {
		super(id, src);
	}
	
	public FLRStuecklistearbeitsplanFormula(Integer id, String src, FLRStuecklistearbeitsplan payload) {
		super(id, src, payload);
	}
	
	
	@Override
	public String getResultType() {
		return "java.lang.Long";
	}
	
	@Override
	public void storeResult(Object result) {
		getPayload().setL_stueckzeit((Long) result);
	}
}
