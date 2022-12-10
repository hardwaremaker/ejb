package com.lp.server.partner.service;

import java.io.Serializable;



public class HelperFuerPartnerGoto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static String KUNDE="KUNDE";
	public static String LIEFERANT="LIEFERANT";
	public static String PARTNER="PARTNER";
	public static String PERSONAL="PERSONAL";
	public static String MANDANT="MANDANT";
	
	public String mandantCNr=null;
	
	Integer ansprechpartnerIId=null;
	
	public Integer getAnsprechpartnerIId() {
		return ansprechpartnerIId;
	}


	public void setAnsprechpartnerIId(Integer ansprechpartnerIId) {
		this.ansprechpartnerIId = ansprechpartnerIId;
	}
	Integer iId=null;
	public Integer getiId() {
		return iId;
	}
	String art=null;
	public HelperFuerPartnerGoto(Integer iId,String art, String mandantCNr) {
		this.iId=iId;
		this.art=art;
		this.mandantCNr=mandantCNr;
	}
	public HelperFuerPartnerGoto(Integer iId,String art, String mandantCNr, Integer ansprechpartnerIId) {
		this.iId=iId;
		this.art=art;
		this.mandantCNr=mandantCNr;
		this.ansprechpartnerIId=ansprechpartnerIId;
	}
	
	public boolean isKunde() {
		if(art.equals(KUNDE)) {
			return true;
		}
		return false;
	}
	public boolean isLieferant() {
		if(art.equals(LIEFERANT)) {
			return true;
		}
		return false;
	}
	public boolean isPartner() {
		if(art.equals(PARTNER)) {
			return true;
		}
		return false;
	}
	public boolean isPersonal() {
		if(art.equals(PERSONAL)) {
			return true;
		}
		return false;
	}
	public boolean isMandant() {
		if(art.equals(MANDANT)) {
			return true;
		}
		return false;
	}
}
