package com.lp.server.stueckliste.ejbfac;


import com.lp.server.artikel.fastlanereader.generated.FLRArtikel;
import com.lp.server.stueckliste.fastlanereader.generated.FLRStuecklistearbeitsplan;
import com.lp.server.system.ejbfac.EJBExcFactory;

public class BaseScriptFLRStuecklistearbeitsplan extends BaseScriptTyped<FLRStuecklistearbeitsplan> {
			
	public BaseScriptFLRStuecklistearbeitsplan() {
	}
	
	public BaseScriptFLRStuecklistearbeitsplan(FLRStuecklistearbeitsplan payload) {
		super(payload);
	}
	
	private FLRStuecklistearbeitsplan getAp() {
		return (FLRStuecklistearbeitsplan) getPayload();
	}
	
	
//	public void setArtikel(ItemId itemId) {
////		pos.setFlrartikel(((ScriptItemId)itemId).getFlrArtikel());
//		throw new IllegalArgumentException("not yet implemented");
//	}
	
	public void setStueckzeit(Long stueckzeit) {
		getAp().setL_stueckzeit(stueckzeit);
	}
	
	public Long getStueckzeit() {
		return getAp().getL_stueckzeit();
	}
	
	public void setRuestzeit(Long ruestzeit) {
		getAp().setL_ruestzeit(ruestzeit);
	}
	
	public Long getRuestzeit() {
		return getAp().getL_ruestzeit();
	}

	public void setArtikel(FLRArtikel flrArtikel) {
		getAp().setFlrartikel(flrArtikel);
	}
	
	public FLRArtikel getArtikel() {
		return getAp().getFlrartikel();
	}
	
	public void throwUserMessage(String message) {
		throw EJBExcFactory.formelUserException(
				message, getAp().getStueckliste_i_id(), getAp().getI_id());
	}
}
