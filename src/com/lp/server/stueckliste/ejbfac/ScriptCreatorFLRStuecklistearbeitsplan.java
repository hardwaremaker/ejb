package com.lp.server.stueckliste.ejbfac;

import com.lp.service.plscript.ScriptCreatorJavax;

public class ScriptCreatorFLRStuecklistearbeitsplan extends ScriptCreatorJavax {

	@Override
	protected String getSuperclassName() {
		return BaseScriptFLRStuecklistearbeitsplan.class.getName();
	}
}
