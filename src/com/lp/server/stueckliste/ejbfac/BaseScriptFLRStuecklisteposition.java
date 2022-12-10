package com.lp.server.stueckliste.ejbfac;

import java.math.BigDecimal;

import com.lp.server.artikel.fastlanereader.generated.FLRArtikelliste;
import com.lp.server.stueckliste.fastlanereader.generated.FLRStuecklisteposition;
import com.lp.server.stueckliste.service.FormelArtikel;
import com.lp.server.stueckliste.service.ItemId;
import com.lp.server.system.ejbfac.EJBExcFactory;
import com.lp.server.util.KundeId;

public class BaseScriptFLRStuecklisteposition extends BaseScriptTyped<FLRStuecklisteposition> {
		
	public BaseScriptFLRStuecklisteposition() {
	}
	
	public BaseScriptFLRStuecklisteposition(FLRStuecklisteposition payload) {
		super(payload);
	}
	
	private FLRStuecklisteposition getPos() {
		return (FLRStuecklisteposition) getPayload();
	}
	
		
	public void setDimension1(Double value) {
		getPos().setF_dimension1(value);
	}
	
	public Double getDimension1() {
		return getPos().getF_dimension1();
	}
	
	public void setDimension2(Double value) {
		getPos().setF_dimension2(value);
	}
	
	public Double getDimension2() {
		return getPos().getF_dimension2();
	}
	
	public void setDimension3(Double value) {
		getPos().setF_dimension3(value);
	}
	
	public Double getDimension3() {
		return getPos().getF_dimension3();
	}
	
	public void setEinheitCnr(String einheitCnr) {
		getPos().setEinheit_c_nr(einheitCnr);
	}

	public String getEinheitCnr() {
		return getPos().getEinheit_c_nr();
	}
	
	public void setMenge(BigDecimal menge) {
		getPos().setN_menge(menge);
	}
	
	public BigDecimal getMenge() {
		return getPos().getN_menge();
	}
	
	public void setArtikel(ItemId itemId) {
		getPos().setFlrartikel(((ScriptItemId)itemId).getFlrArtikel());
	}
	
	public void setArtikel(FLRArtikelliste flrArtikel) {
		getPos().setFlrartikel(flrArtikel);
	}
	
	public FLRArtikelliste getArtikel() {
		return getPos().getFlrartikel();
	}
	
	public void setArtikelCnr(String artikelCnr) {
		setArtikelCnr(getPos().formelArtikel, artikelCnr);
	}
	
	public String getArtikelCnr() {
		return getArtikelCnr(getPos().formelArtikel);
	}
	
	public void setArtikelBezeichnung(String bez, String kbez, String zbez, String zbez2) {
		setArtikelBezeichnung(getPos().formelArtikel, bez, kbez, zbez, zbez2);
	}
	
	public void generiereArtikelCnr(String artikelCnr) {
		generiereArtikelCnr(getPos().formelArtikel, artikelCnr);
	}
	
	public void setUebergeordneteArtikelCnr(String artikelCnr) {
		setArtikelCnr(getPos().formelUebergeordneterArtikel, artikelCnr);
	}

	public String getUebergeordneteArtikelCnr() {
		return getArtikelCnr(getPos().formelUebergeordneterArtikel);
	}

	public void generiereUebergeordneteArtikelCnr(String artikelCnr) {
		generiereArtikelCnr(getPos().formelUebergeordneterArtikel, artikelCnr);
	}
	
	public void setUebergeordneteArtikelBezeichnung(String bez, String kbez, String zbez, String zbez2) {
		setArtikelBezeichnung(getPos().formelUebergeordneterArtikel, bez, kbez, zbez, zbez2);
	}
	
	private void setArtikelCnr(FormelArtikel a, String cnr) {
		a.setCnr(cnr);
	}
	
	private String getArtikelCnr(FormelArtikel a) {
		return a.getCnr();
	}
	
	private void setArtikelBezeichnung(FormelArtikel a, String bez, String kbez, String zbez, String zbez2) {
		a.setcBez(bez);
		a.setcKbez(kbez);
		a.setcZbez(zbez);
		a.setcZbez2(zbez2);		
	}
	
	private void generiereArtikelCnr(FormelArtikel a, String cnr) {
		a.setCnr(cnr);
		a.setGeneriereCnr(true);
	}
	
	public String getKundeKurznr(KundeId kundeId) {
		if(kundeId == null) return null;
		
		ScriptKundeId scriptId = (ScriptKundeId)kundeId;
		if(scriptId.getFlrKunde() == null) return null;
		
		return scriptId.getFlrKunde().getC_kurznr();
	}
	
	public void throwUserMessage(String message) {
		throw EJBExcFactory.formelUserException(
				message, getPos().getStueckliste_i_id(), getPos().getI_id());
	}
}
