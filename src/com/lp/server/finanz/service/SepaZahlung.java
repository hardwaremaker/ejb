/*******************************************************************************
 * HELIUM V, Open Source ERP software for sustained success
 * at small and medium-sized enterprises.
 * Copyright (C) 2004 - 2015 HELIUM V IT-Solutions GmbH
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
import java.util.ArrayList;
import java.util.List;

public class SepaZahlung implements Serializable {

	private static final long serialVersionUID = -1226091171197143333L;

	/**
	 * Bei einer Sollbuchung, der Zahlungsempfaenger.
	 * Bei einer Habenbuchung, der Zahlungspflichtige.
	 */
	private SepaKontoinformation beteiligter;
	private SepaBetrag betrag;
	private String verwendungszweck; //<NtryDtls><TxDtls><RmtInf><Ustrd>
	private String zahlungsreferenz; //<NtryDtls><TxDtls><RmtInf><Strd><CdtrRefInf>
	/**
	 * Auftraggeberreferenz, gleiche wie im Zahlungsauftrag (pain.001)
	 */
	private String auftraggeberreferenz; //<NtryDtls><TxDtls><Refs><EndToEndId>
	private String bankreferenz; //<NtryDtls><TxDtls><Refs><AcctSvcrRef>
	private SepaBankTransactionCode buchungscode; //<NtryDtls><TxDtls><BkTxCd>
	private String glaeubigerId; //<NtryDtls><TxDtls><RltdPties><Cdtr><Id><PrvtId><Othr><Id>
	/*
	 * Sammlerinformation
	 */
	private Integer enthalteneBuchungen; //<NtryDtls><Btch><NbOfTxs>
	private String mandatsreferenz; //<NtryDtls><TxDtls><Refs><MndtId>
	private String bestandsreferenzKunde; //<NtryDtls><Btch><PmtInfId>
	
	private String zusatzinformation; //<NtryDtls><TxDtls><AddtlTxInf>
	private List<SepaSpesen> spesen; //<NtryDtls><TxDtls><Chrgs>

	public SepaZahlung() {
		super();
	}

	public SepaKontoinformation getBeteiligter() {
		if (beteiligter == null) {
			beteiligter = new SepaKontoinformation();
			beteiligter.setName("unbekannt");
		}
		return beteiligter;
	}

	public void setBeteiligter(SepaKontoinformation beteiligter) {
		this.beteiligter = beteiligter;
	}

	public SepaBetrag getBetrag() {
		return betrag;
	}

	public void setBetrag(SepaBetrag betrag) {
		this.betrag = betrag;
	}

	public String getVerwendungszweck() {
		return verwendungszweck;
	}

	public void setVerwendungszweck(String verwendungszweck) {
		this.verwendungszweck = verwendungszweck;
	}

	public String getZahlungsreferenz() {
		return zahlungsreferenz;
	}

	public void setZahlungsreferenz(String zahlungsreferenz) {
		this.zahlungsreferenz = zahlungsreferenz;
	}

	public String getAuftraggeberreferenz() {
		return auftraggeberreferenz;
	}

	public void setAuftraggeberreferenz(String auftraggeberreferenz) {
		this.auftraggeberreferenz = auftraggeberreferenz;
	}

	public String getBankreferenz() {
		return bankreferenz;
	}

	public void setBankreferenz(String bankreferenz) {
		this.bankreferenz = bankreferenz;
	}

	public SepaBankTransactionCode getBuchungscode() {
		return buchungscode;
	}

	public void setBuchungscode(SepaBankTransactionCode buchungscode) {
		this.buchungscode = buchungscode;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("\n\tSepaZahlung [\n\t\tbeteiligter=").append(beteiligter)
			.append(", \n\t\tbetrag=").append(betrag)
			.append(", \n\t\tauftraggeberreferenz=").append(auftraggeberreferenz)
			.append(", \n\t\tbankreferenz=").append(bankreferenz)
			.append(", \n\t\tverwendungszweck=").append(verwendungszweck)
			.append(", \n\t\tzahlungsreferenz=").append(zahlungsreferenz)
			.append(", \n\t\tbuchungscode=").append(buchungscode)
			.append(", \n\t\tenthalteneBuchungen=").append(enthalteneBuchungen)
			.append(", \n\t\tbestandsreferenzKunde=").append(bestandsreferenzKunde)
			.append(", \n\t\tmandatsreferenz=").append(mandatsreferenz)
			.append(", \n\t\tglaeubigerId=").append(glaeubigerId)
			.append(", \n\t\tzusatzinformation=").append(zusatzinformation)
			.append("]");
		return builder.toString();
	}
	
	public boolean isSammler() {
		return getBestandsreferenzKunde() != null 
				|| (getEnthalteneBuchungen() != null && getEnthalteneBuchungen() > 1);
	}
	
	public Integer getEnthalteneBuchungen() {
		return enthalteneBuchungen;
	}

	public void setEnthalteneBuchungen(Integer enthalteneBuchungen) {
		this.enthalteneBuchungen = enthalteneBuchungen;
	}

	public String getMandatsreferenz() {
		return mandatsreferenz;
	}
	
	public void setMandatsreferenz(String mandatsreferenz) {
		this.mandatsreferenz = mandatsreferenz;
	}
	
	public String getGlaeubigerId() {
		return glaeubigerId;
	}
	
	public void setGlaeubigerId(String glaeubigerId) {
		this.glaeubigerId = glaeubigerId;
	}
	
	public String getBestandsreferenzKunde() {
		return bestandsreferenzKunde;
	}
	
	public void setBestandsreferenzKunde(String bestandsreferenzKunde) {
		this.bestandsreferenzKunde = bestandsreferenzKunde;
	}

	public String getZusatzinformation() {
		return zusatzinformation;
	}

	public void setZusatzinformation(String zusatzinformation) {
		this.zusatzinformation = zusatzinformation;
	}
	
	public List<SepaSpesen> getSpesen() {
		if (spesen == null) {
			spesen = new ArrayList<SepaSpesen>();
		}
		return spesen;
	}
	
}
