/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2016 HELIUM V IT-Solutions GmbH
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
package com.lp.server.finanz.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SepaKontoauszug implements Serializable {
	
	private static final long serialVersionUID = 2717735197536883531L;

	public static String CREDITOR = "CRDT";
	public static String DEBITOR = "DBIT";
	
	private ISepaCamtFormat camtFormat;
	
	private Date erstellungsDatum;
	/**
	 * verwendet, wenn Empfaenger nicht Kontoinhaber
	 */
	private String empfaenger;
	/**
	 * technische Referenz der Uebermittlungsdatei
	 */
	private String messageId; //<MsgId>
	/**
	 * eindeutige, nicht wiederholende Referenz des Ktoauszugs
	 */
	private String auszugsId; //<Stmt><Id>
	private Integer seitennummer; //<MsgPgntn><PgNb>
	private Boolean bLetzteSeite; //<MsgPgntn><LastPgInd>
	private BigDecimal elektronischeAuszugsnr; //<ElctrncSeqNb>
	private BigDecimal auszugsnr; //<LqlSeqNb>
	private SepaKontoinformation kontoInfo;
	private List<SepaSaldo> salden; //<Bal>
	private Map<SepaSaldoCodeEnum, SepaSaldo> saldenMap;
	private List<SepaBuchung> buchungen; //<Ntry>
	private String zusatzinformation; //<AddtlStmtInf>
	
	public SepaKontoauszug() {
		super();
		saldenMap = new HashMap<SepaSaldoCodeEnum, SepaSaldo>();
	}

	public ISepaCamtFormat getTypVersion() {
		return camtFormat;
	}

	public void setTypVersion(ISepaCamtFormat camtFormat) {
		this.camtFormat = camtFormat;
	}

	public Date getErstellungsDatum() {
		return erstellungsDatum;
	}

	public void setErstellungsDatum(Date erstellungsDatum) {
		this.erstellungsDatum = erstellungsDatum;
	}

	public String getEmpfaenger() {
		return empfaenger;
	}

	public void setEmpfaenger(String empfaenger) {
		this.empfaenger = empfaenger;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getAuszugsId() {
		return auszugsId;
	}

	public void setAuszugsId(String id) {
		this.auszugsId = id;
	}

	public BigDecimal getElektronischeAuszugsnr() {
		return elektronischeAuszugsnr;
	}

	public void setElektronischeAuszugsnr(BigDecimal elektronischeAuszugsnr) {
		this.elektronischeAuszugsnr = elektronischeAuszugsnr;
	}

	public BigDecimal getAuszugsnr() {
//		if (auszugsnr == null || auszugsnr.equals(new BigDecimal(0))) {
//			if (elektronischeAuszugsnr == null || elektronischeAuszugsnr.equals(new BigDecimal(0))) {
//				return null;
//			} else {
//				return elektronischeAuszugsnr;
//			}
//		}
		return auszugsnr;
	}

	public void setAuszugsnr(BigDecimal auszugsnr) {
		this.auszugsnr = auszugsnr;
	}

	public SepaKontoinformation getKontoInfo() {
		return kontoInfo;
	}

	public void setKontoInfo(SepaKontoinformation kontoInfo) {
		this.kontoInfo = kontoInfo;
	}

	public List<SepaSaldo> getSalden() {
		return salden;
	}

	public void setSalden(List<SepaSaldo> salden) {
		this.salden = salden;
		saldenMap.clear();
		for (SepaSaldo saldo : getSalden()) {
			saldenMap.put(saldo.getSaldocode(), saldo);
		}
	}

	public SepaSaldo getStartSaldo() {
		SepaSaldo saldo = saldenMap.get(SepaSaldoCodeEnum.PRCD);
		if (saldo != null) return saldo;
		
		saldo = saldenMap.get(SepaSaldoCodeEnum.OPBD);
		if (saldo != null) return saldo;

		return null;
	}

	public SepaSaldo getEndSaldo() {
		SepaSaldo saldo = saldenMap.get(SepaSaldoCodeEnum.CLBD);
		if (saldo != null) return saldo;
		
		saldo = saldenMap.get(SepaSaldoCodeEnum.CLAV);
		if (saldo != null) return saldo;

		return null;
	}

	public List<SepaBuchung> getBuchungen() {
		return buchungen;
	}

	public void setBuchungen(List<SepaBuchung> buchungen) {
		this.buchungen = buchungen;
	}

	public void addBuchung(SepaBuchung buchung) {
		this.buchungen.add(buchung);
	}

	public Integer getSeitennummer() {
		return seitennummer;
	}

	public void setSeitennummer(Integer seitennummer) {
		this.seitennummer = seitennummer;
	}

	public Boolean getbLetzteSeite() {
		return bLetzteSeite;
	}

	public void setbLetzteSeite(Boolean bLetzteSeite) {
		this.bLetzteSeite = bLetzteSeite;
	}

	public Boolean hatSalden() {
		return salden != null && !salden.isEmpty();
	}

	@Override
	public String toString() {
		return "\nSepaKontoauszug [\nmessageId=" + messageId + ", \nerstellungsDatum=" + erstellungsDatum
				+ ", \nempfaenger=" + empfaenger + ", \nauszugsId=" + auszugsId
				+ ", \nelektronischeAuszugsnr=" + elektronischeAuszugsnr
				+ ", \nauszugsnr=" + auszugsnr + ",\nzusatzinformation=" + zusatzinformation + "\nkontoInfo=" + kontoInfo
				+ ", \nsalden=" + salden + ", \nbuchungen=" + buchungen + "]";
	}

	public String getZusatzinformation() {
		return zusatzinformation;
	}

	public void setZusatzinformation(String zusatzinformation) {
		this.zusatzinformation = zusatzinformation;
	}

}
