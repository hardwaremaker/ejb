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
import java.sql.Date;
import java.util.List;

public class SepaBuchung implements Serializable {

	private static final long serialVersionUID = -574583865343252865L;

	private SepaBetrag betrag;
	private String waehrung;
	private String status; //<Sts>
	private Date buchungsdatum; //<BookgDt>
	private Date valutadatum; //<ValDt>
	private String bankreferenz; //<AcctSvcrRef>
	private SepaBankTransactionCode buchungscode; //<BkTxCd>
	private List<SepaZahlung> zahlungen;

	private String zusatzinformation; //<AddtlNtryInf>

	public SepaBuchung() {
		super();
	}

	public SepaBetrag getBetrag() {
		return betrag;
	}

	public void setBetrag(SepaBetrag betrag) {
		this.betrag = betrag;
	}

	public String getWaehrung() {
		return waehrung;
	}

	public void setWaehrung(String waehrung) {
		this.waehrung = waehrung;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getBuchungsdatum() {
		return buchungsdatum;
	}

	public void setBuchungsdatum(Date buchungsdatum) {
		this.buchungsdatum = buchungsdatum;
	}

	public Date getValutadatum() {
		return valutadatum;
	}

	public void setValutadatum(Date valutadatum) {
		this.valutadatum = valutadatum;
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

	public List<SepaZahlung> getZahlungen() {
		return zahlungen;
	}

	public void setZahlungen(List<SepaZahlung> zahlungen) {
		this.zahlungen = zahlungen;
	}

	public void addZahlung(SepaZahlung zahlung) {
		zahlungen.add(zahlung);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SepaBuchung ")
			.append("[betrag=").append(betrag)
			.append(", waehrung=").append(waehrung)
			.append(", status=").append(status)
			.append(", buchungsdatum=").append(buchungsdatum)
			.append(", valutadatum=").append(valutadatum)
			.append(", bankreferenz=").append(bankreferenz)
			.append(", buchungscode=").append(buchungscode)
			.append(", zusatzinformation=").append(zusatzinformation)
			.append(", zahlungen = ").append(zahlungen).append("]");
		return builder.toString();
//		return "SepaBuchung [\n\tbetrag=" + betrag + ", \n\twaehrung=" + waehrung
//				+ ", \n\tstatus=" + status
//				+ ", \n\tbuchungsdatum=" + buchungsdatum + ", \n\tvalutadatum="
//				+ valutadatum + ", \n\tbankreferenz=" + bankreferenz + ", \n\tzahlungen=" + zahlungen + "]";
	}

	public Boolean isHabenBuchung() {
		if(betrag != null) {
			return betrag.isHaben();
		}
		return false;
	}

	public Boolean isSollBuchung() {
		if(betrag != null) {
			return betrag.isSoll();
		}
		return false;
	}

	public String getZusatzinformation() {
		return zusatzinformation;
	}

	public void setZusatzinformation(String zusatzinformation) {
		this.zusatzinformation = zusatzinformation;
	}
	
}
