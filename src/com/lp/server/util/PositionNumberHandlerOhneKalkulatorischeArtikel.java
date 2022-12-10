package com.lp.server.util;

/**
 * Das ganze hier dient dazu, dass die Behandlung von kalkulatorischen Artikeln
 * so wenig Auswirkung wie m&ouml;glich hat.
 * 
 * @author gerold
 */
public class PositionNumberHandlerOhneKalkulatorischeArtikel extends
		PositionNumberHandler {

//	private EntityManager em ;
	
//	public PositionNumberHandlerOhneKalkulatorischeArtikel(EntityManager em) {
//		this.em = em ;
//	}
	
	@Override
	protected boolean isIdent(PositionNumberAdapter adapter) {
		boolean result = super.isIdent(adapter);
		return result && adapter.isIdent();
//		if(result) {
//			Auftragposition position = (Auftragposition) adapter.getAdaptee() ;
//			Artikel a = em.find(Artikel.class, position.getArtikelIId());
//			result = !Helper.short2boolean(a.getBKalkulatorisch()) ;
//		}
//		return result ;
	}
}
