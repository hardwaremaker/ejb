package com.lp.server.stueckliste.ejbfac;

import com.lp.server.partner.fastlanereader.generated.FLRKunde;
import com.lp.server.util.KundeId;

public class ScriptKundeId extends KundeId {
	private static final long serialVersionUID = 4260968049790386864L;
	private FLRKunde flrKunde;
	
	public ScriptKundeId(Integer id, FLRKunde flrKunde) {
		super(id);
		this.flrKunde = flrKunde;
	}
	
	public FLRKunde getFlrKunde() {
		return flrKunde;
	}
}
