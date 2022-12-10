package com.lp.server.stueckliste.ejbfac;

import com.lp.service.plscript.ScriptCreatorJavax;

public class ScriptCreatorFLRStuecklisteposition extends ScriptCreatorJavax {

	@Override
	protected String getSuperclassName() {
		return BaseScriptFLRStuecklisteposition.class.getName();
	}
}
