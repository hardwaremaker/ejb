package com.lp.server.artikel.service;

import java.io.Serializable;

import com.lp.server.personal.service.ZeitdatenDto;

public class LumiQuoteArtikelDto implements Serializable {
	private String ipn;
	private String mpn;

	public String getIpn() {
		return ipn;
	}

	public void setIpn(String ipn) {
		this.ipn = ipn;
	}

	public String getMpn() {
		return mpn;
	}

	public void setMpn(String mpn) {
		this.mpn = mpn;
	}

	public String getHersteller() {
		return hersteller;
	}

	public void setHersteller(String hersteller) {
		this.hersteller = hersteller;
	}

	public String getBeschreibung() {
		return beschreibung;
	}

	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}

	public String getBauform() {
		return bauform;
	}

	public void setBauform(String bauform) {
		this.bauform = bauform;
	}

	private String hersteller;
	private String beschreibung;
	private String bauform;

	public String[] getColumnNames() {

		String[] colNamen = new String[] { "IPN", "MPN", "Manufacturer", "Description", "Package" };
		return colNamen;
	}

	public String[] zeileToStringArray() {
		return new String[] { getIpn(), getMpn(), getHersteller(), getBeschreibung(), getBauform() };
	}

	public static LumiQuoteArtikelDto clone(LumiQuoteArtikelDto orig) {
		LumiQuoteArtikelDto klon = new LumiQuoteArtikelDto();
		klon.setBauform(orig.getBauform());
		klon.setBeschreibung(orig.getBeschreibung());
		klon.setHersteller(orig.getHersteller());
		klon.setIpn(orig.getIpn());
		klon.setMpn(orig.getMpn());
		return klon;
	}

}
