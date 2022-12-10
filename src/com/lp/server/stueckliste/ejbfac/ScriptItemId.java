package com.lp.server.stueckliste.ejbfac;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikelliste;
import com.lp.server.stueckliste.service.ItemId;

public class ScriptItemId extends ItemId {
	private static final long serialVersionUID = 3742279174074540414L;
	
	private FLRArtikelliste flrArtikel;
	
	public ScriptItemId(Integer id, FLRArtikelliste flrArtikel) {
		super(id);
		this.flrArtikel = flrArtikel;
	}
	
	public FLRArtikelliste getFlrArtikel() {
		return flrArtikel;
	}
}
