/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2014 HELIUM V IT-Solutions GmbH
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published 
 * by the Free Software Foundation, either version 3 of theLicense, or 
 * (at your option) any later version.
 * 
 * According to sec. 7 of the GNU Affero General Public License, version 3, 
 * the terms of the AGPL are supplemented with the following terms:
 * 
 * "HELIUM V" and "HELIUM 5" are registered trademarks of 
 * HELIUM V IT-Solutions GmbH. The licensing of the program under the 
 * AGPL does not imply a trademark license. Therefore any rights, title and
 * interest in our trademarks remain entirely with us. If you want to propagate
 * modified versions of the Program under the name "HELIUM V" or "HELIUM 5",
 * you may only do so if you have a written permission by HELIUM V IT-Solutions 
 * GmbH (to acquire a permission please contact HELIUM V IT-Solutions
 * at trademark@heliumv.com).
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contact: developers@heliumv.com
 ******************************************************************************/
package com.lp.server.personal.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;

import com.lp.util.LPDatenSubreport;

public class ZeileMonatsabrechnungDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer iKw;
	private Integer iTag;
	private String sTag;
	private Time tVon;
	private BigDecimal bdUestd50;
	public BigDecimal getBdUestd200() {
		return bdUestd200;
	}

	public void setBdUestd200(BigDecimal bdUestd200) {
		this.bdUestd200 = bdUestd200;
	}

	
	private LPDatenSubreport subreportZulagen;
	
	public LPDatenSubreport getSubreportZulagen() {
		return subreportZulagen;
	}

	public void setSubreportZulagen(LPDatenSubreport subreportZulagen) {
		this.subreportZulagen = subreportZulagen;
	}


	private BigDecimal bdUestd200;
	private BigDecimal bdMehrstunden;
	private Time tBis;
	private BigDecimal bdUestd100;
	private BigDecimal bdUnter;
	private BigDecimal bdSoll;
	private BigDecimal bdIst;
	private BigDecimal bdUestd50Tageweise;
	private BigDecimal bdDiff;
	private String sBemerkung;
	private String sZeitmodell;
	public String getSZeitmodell() {
		return sZeitmodell;
	}

	public void setSZeitmodell(String sZeitmodell) {
		this.sZeitmodell = sZeitmodell;
	}

	private String sTagesart;
	public String getSTagesart() {
		return sTagesart;
	}

	public void setSTagesart(String sTagesart) {
		this.sTagesart = sTagesart;
	}

	private BigDecimal bdZA;
	private BigDecimal bdFeiertag;
	private BigDecimal bdArzt;
	private BigDecimal bdBehoerde;
	private BigDecimal bdKrank;
	private BigDecimal bdKindkrank;
	private BigDecimal bdSonstigeBezahlt;
	private BigDecimal bdSonstigeNichtBezahlt;
	public BigDecimal getBdKindkrank() {
		return bdKindkrank;
	}

	public void setBdKindkrank(BigDecimal bdKindkrank) {
		this.bdKindkrank = bdKindkrank;
	}

	private BigDecimal bdUrlaubTage;
	private BigDecimal bdReise;
	private BigDecimal bdUrlaubStunden;
	private String sZusatzbezeichnung;
	private Timestamp tDatum;
	private Integer iJahr;
	
	public Integer getIJahr() {
		return iJahr;
	}

	public void setIJahr(Integer jahr) {
		iJahr = jahr;
	}

	public Integer getIKw() {
		return iKw;
	}

	public Integer getITag() {
		return iTag;
	}

	public String getSTag() {
		return sTag;
	}

	public Time getTVon() {
		return tVon;
	}

	public Time getTBis() {
		return tBis;
	}

	public BigDecimal getBdUnter() {
		return bdUnter;
	}

	public BigDecimal getBdSoll() {
		return bdSoll;
	}

	public BigDecimal getBdIst() {
		return bdIst;
	}

	public BigDecimal getBdDiff() {
		return bdDiff;
	}

	public String getSBemerkung() {
		return sBemerkung;
	}

	public BigDecimal getBdZA() {
		return bdZA;
	}

	public BigDecimal getBdFeiertag() {
		return bdFeiertag;
	}

	public BigDecimal getBdArzt() {
		return bdArzt;
	}

	public BigDecimal getBdBehoerde() {
		return bdBehoerde;
	}

	public BigDecimal getBdKrank() {
		return bdKrank;
	}

	public BigDecimal getBdSonstigeBezahlt() {
		return bdSonstigeBezahlt;
	}

	public BigDecimal getBdSonstigeNichtBezahlt() {
		return bdSonstigeNichtBezahlt;
	}

	public BigDecimal getBdUrlaubTage() {
		return bdUrlaubTage;
	}

	public BigDecimal getBdUrlaubStunden() {
		return bdUrlaubStunden;
	}

	public String getSZusatzbezeichnung() {
		return sZusatzbezeichnung;
	}

	public Timestamp getTDatum() {
		return tDatum;
	}

	public BigDecimal getBdReise() {
		return bdReise;
	}

	public BigDecimal getBdUestd100() {
		return bdUestd100;
	}

	public BigDecimal getBdUestd50() {
		return bdUestd50;
	}

	public BigDecimal getBdMehrstunden() {
		return bdMehrstunden;
	}

	public BigDecimal getBdUestd50Tageweise() {
		return bdUestd50Tageweise;
	}

	public BigDecimal getBdUestd100Steuerfrei() {
		return bdUestd100Steuerfrei;
	}

	public BigDecimal getBdUestd50TageweiseSteuerfrei() {
		return bdUestd50TageweiseSteuerfrei;
	}

	public void setIKw(Integer iKw) {
		this.iKw = iKw;
	}

	public void setITag(Integer iTag) {
		this.iTag = iTag;
	}

	public void setSTag(String sTag) {
		this.sTag = sTag;
	}

	public void setTVon(Time tVon) {
		this.tVon = tVon;
	}

	public void setTBis(Time tBis) {
		this.tBis = tBis;
	}

	public void setBdUnter(BigDecimal bdUnter) {
		this.bdUnter = bdUnter;
	}

	public void setBdSoll(BigDecimal bdSoll) {
		this.bdSoll = bdSoll;
	}

	public void setBdIst(BigDecimal bdIst) {
		this.bdIst = bdIst;
	}

	public void setBdDiff(BigDecimal bdDiff) {
		this.bdDiff = bdDiff;
	}

	public void setSBemerkung(String sBemerkung) {
		this.sBemerkung = sBemerkung;
	}

	public void setBdZA(BigDecimal bdZA) {
		this.bdZA = bdZA;
	}

	public void setBdFeiertag(BigDecimal bdFeiertag) {
		this.bdFeiertag = bdFeiertag;
	}

	public void setBdArzt(BigDecimal bdArzt) {
		this.bdArzt = bdArzt;
	}

	public void setBdBehoerde(BigDecimal bdBehoerde) {
		this.bdBehoerde = bdBehoerde;
	}

	public void setBdKrank(BigDecimal bdKrank) {
		this.bdKrank = bdKrank;
	}

	public void setBdSonstigeBezahlt(BigDecimal bdSonstigeBezahlt) {
		this.bdSonstigeBezahlt = bdSonstigeBezahlt;
	}

	public void setBdSonstigeNichtBezahlt(BigDecimal bdSonstigeNichtBezahlt) {
		this.bdSonstigeNichtBezahlt = bdSonstigeNichtBezahlt;
	}

	public void setBdUrlaubTage(BigDecimal bdUrlaubTage) {
		this.bdUrlaubTage = bdUrlaubTage;
	}

	public void setBdUrlaubStunden(BigDecimal bdUrlaubStunden) {
		this.bdUrlaubStunden = bdUrlaubStunden;
	}

	private BigDecimal bdUestd50TageweiseSteuerfrei;

	public void setSZusatzbezeichnung(String sZusatzbezeichnung) {
		this.sZusatzbezeichnung = sZusatzbezeichnung;
	}

	public void setTDatum(Timestamp tDatum) {
		this.tDatum = tDatum;
	}

	private BigDecimal bdUestd100Steuerfrei;

	public void setBdReise(BigDecimal bdReise) {
		this.bdReise = bdReise;
	}

	public void setBdUestd100(BigDecimal bdUestd100) {
		this.bdUestd100 = bdUestd100;
	}

	public void setBdUestd50(BigDecimal bdUestd50) {
		this.bdUestd50 = bdUestd50;
	}

	public void setBdMehrstunden(BigDecimal bdMehrstunden) {
		this.bdMehrstunden = bdMehrstunden;
	}

	public void setBdUestd50Tageweise(BigDecimal bdUestd50Tageweise) {
		this.bdUestd50Tageweise = bdUestd50Tageweise;
	}

	public void setBdUestd100Steuerfrei(BigDecimal bdUestd100Steuerfrei) {
		this.bdUestd100Steuerfrei = bdUestd100Steuerfrei;
	}

	public void setBdUestd50TageweiseSteuerfrei(
			BigDecimal bdUestd50TageweiseSteuerfrei) {
		this.bdUestd50TageweiseSteuerfrei = bdUestd50TageweiseSteuerfrei;
	}

}
