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
 *******************************************************************************/
package com.lp.util.report;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import com.lp.server.finanz.service.KontoDto;
import com.lp.server.util.logger.LogEventPayload;

public class UstVerprobungRow implements LogEventPayload {

	private BigDecimal betrag;
	private BigDecimal steuer;
	private String steuerart;
	private Integer mwstSatzBezIId;
	private BigDecimal steuersatz;
	private String mwstSatzBezCBez;
	private String kontoCBez;
	private String kontoCNr;
	private String gegenkontoCBez;
	private String gegenkontoCNr;
	private String steuerkategorieCBez;
	private int steuerkategorieISort;
	private Timestamp datum;
	private Integer reversechargeartId ;
	private String reversechargeartCnr ;
	private String reversechargeartCBez ;
	private String uvaartCnr;
	private String gegenkontoUvaartCnr;
	private Integer buchungDetailId;
	private Integer buchungId;
	private KontoDto kontoDto;
	private KontoDto gegenKontoDto;
	private String buchungText;
	private String buchungBelegnummer;
	private String belegartCnr;
	private Timestamp belegDatum;
	private Integer kontoMwstSatzId;
	private BigDecimal kontoSteuersatz;
	private Integer gegenkontoMwstSatzId;
	private BigDecimal gegenkontoSteuersatz;
	
	public BigDecimal getBetrag() {
		return betrag;
	}
	public void setBetrag(BigDecimal betrag) {
		this.betrag = betrag;
	}
	
	public void setSteuer(BigDecimal steuer) {
		this.steuer = steuer;
	}
	public BigDecimal getSteuer() {
		return steuer;
	}
	public void setSteuerart(String steuerart) {
		this.steuerart = steuerart;
	}
	public String getSteuerart() {
		return steuerart;
	}
	public Integer getMwstSatzBezIId() {
		return mwstSatzBezIId;
	}
	public void setMwstSatzBezIId(Integer mwstSatzBezIId) {
		this.mwstSatzBezIId = mwstSatzBezIId;
	}
	public String getMwstSatzBezCBez() {
		return mwstSatzBezCBez;
	}
	public void setMwstSatzBezCBez(String mwstSatzBezCBez) {
		this.mwstSatzBezCBez = mwstSatzBezCBez;
	}
	public String getKontoCNr() {
		return getKontoDto().getCNr();
	}
	public void setKontoCNr(String kontoCNr) {
		this.kontoCNr = kontoCNr;
	}
	public String getGegenkontoCNr() {
		return getGegenKontoDto() != null  ? getGegenKontoDto().getCNr() : null ;
	}
	public void setGegenkontoCNr(String gegenkontoCNr) {
		this.gegenkontoCNr = gegenkontoCNr;
	}
	public String getKontoCBez() {
		return getKontoDto().getCBez();
	}
	public void setKontoCBez(String kontoCBez) {
		this.kontoCBez = kontoCBez;
	}
	public String getGegenkontoCBez() {
		return getGegenKontoDto() != null  ? getGegenKontoDto().getCBez() : null;
	}
	public void setGegenkontoCBez(String gegenkontoCBez) {
		this.gegenkontoCBez = gegenkontoCBez;
	}
	public BigDecimal getSteuersatz() {
		return steuersatz;
	}
	public void setSteuersatz(Double steuersatz) {
		this.steuersatz = BigDecimal.valueOf(steuersatz);
	}
	public void setSteuersatz(BigDecimal steuersatz) {
		this.steuersatz = steuersatz;
	}
	public String getSteuerkategorieCBez() {
		return steuerkategorieCBez;
	}
	public void setSteuerkategorieCBez(String steuerkategorieCBez) {
		this.steuerkategorieCBez = steuerkategorieCBez;
	}
	public int getSteuerkategorieISort() {
		return steuerkategorieISort;
	}
	public void setSteuerkategorieISort(int steuerkategorieISort) {
		this.steuerkategorieISort = steuerkategorieISort;
	}
	public Timestamp getDatum() {
		return datum;
	}
	public void setDatum(Timestamp datum) {
		this.datum = datum;
	}
	public Integer getReversechargeartId() {
		return reversechargeartId;
	}
	public void setReversechargeartId(Integer reversechargeartId) {
		this.reversechargeartId = reversechargeartId;
	}
	public String getReversechargeartCnr() {
		return reversechargeartCnr;
	}
	public void setReversechargeartCnr(String reversechargeartCnr) {
		this.reversechargeartCnr = reversechargeartCnr;
	}
	public String getReversechargeartCBez() {
		return reversechargeartCBez;
	}
	public void setReversechargeartCBez(String reversechargeartCBez) {
		this.reversechargeartCBez = reversechargeartCBez;
	}
	public String getUvaartCnr() {
		return uvaartCnr;
	}
	public void setUvaartCnr(String uvaartCnr) {
		this.uvaartCnr = uvaartCnr;
	}
	public String getGegenkontoUvaartCnr() {
		return gegenkontoUvaartCnr;
	}
	public void setGegenkontoUvaartCnr(String gegenkontoUvaartCnr) {
		this.gegenkontoUvaartCnr = gegenkontoUvaartCnr;
	}	
	public Integer getBuchungDetailId() {
		return buchungDetailId;
	}
	public void setBuchungDetailId(Integer buchungDetailId) {
		this.buchungDetailId = buchungDetailId;
	}

	public Integer getBuchungId() {
		return buchungId;
	}
	public void setBuchungId(Integer buchungId) {
		this.buchungId = buchungId;
	}
	public KontoDto getKontoDto() {
		return kontoDto;
	}
	public void setKontoDto(KontoDto kontoDto) {
		this.kontoDto = kontoDto;
	}
	public KontoDto getGegenKontoDto() {
		return gegenKontoDto;
	}
	public void setGegenKontoDto(KontoDto gegenKontoDto) {
		this.gegenKontoDto = gegenKontoDto;
	}

//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		UstVerprobungRow other = (UstVerprobungRow) obj;
//		if (betrag == null) {
//			if (other.betrag != null)
//				return false;
//		} else if (!betrag.equals(other.betrag))
//			return false;
//		if (datum == null) {
//			if (other.datum != null)
//				return false;
//		} else if (!datum.equals(other.datum))
//			return false;
//		if (gegenkontoCBez == null) {
//			if (other.gegenkontoCBez != null)
//				return false;
//		} else if (!gegenkontoCBez.equals(other.gegenkontoCBez))
//			return false;
//		if (gegenkontoCNr == null) {
//			if (other.gegenkontoCNr != null)
//				return false;
//		} else if (!gegenkontoCNr.equals(other.gegenkontoCNr))
//			return false;
//		if (kontoCBez == null) {
//			if (other.kontoCBez != null)
//				return false;
//		} else if (!kontoCBez.equals(other.kontoCBez))
//			return false;
//		if (kontoCNr == null) {
//			if (other.kontoCNr != null)
//				return false;
//		} else if (!kontoCNr.equals(other.kontoCNr))
//			return false;
//		if (mwstSatzBezCBez == null) {
//			if (other.mwstSatzBezCBez != null)
//				return false;
//		} else if (!mwstSatzBezCBez.equals(other.mwstSatzBezCBez))
//			return false;
//		if (mwstSatzBezIId == null) {
//			if (other.mwstSatzBezIId != null)
//				return false;
//		} else if (!mwstSatzBezIId.equals(other.mwstSatzBezIId))
//			return false;
//		if (steuer == null) {
//			if (other.steuer != null)
//				return false;
//		} else if (!steuer.equals(other.steuer))
//			return false;
//		if (steuerart == null) {
//			if (other.steuerart != null)
//				return false;
//		} else if (!steuerart.equals(other.steuerart))
//			return false;
//		if (steuerkategorieCBez == null) {
//			if (other.steuerkategorieCBez != null)
//				return false;
//		} else if (!steuerkategorieCBez.equals(other.steuerkategorieCBez))
//			return false;
//		if (steuerkategorieISort != other.steuerkategorieISort)
//			return false;
//		if (steuersatz == null) {
//			if (other.steuersatz != null)
//				return false;
//		} else if (!steuersatz.equals(other.steuersatz))
//			return false;
//		return true;
//	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((betrag == null) ? 0 : betrag.hashCode());
		result = prime * result + ((datum == null) ? 0 : datum.hashCode());
		result = prime * result + ((gegenkontoCBez == null) ? 0 : gegenkontoCBez.hashCode());
		result = prime * result + ((gegenkontoCNr == null) ? 0 : gegenkontoCNr.hashCode());
		result = prime * result + ((gegenkontoUvaartCnr == null) ? 0 : gegenkontoUvaartCnr.hashCode());
		result = prime * result + ((kontoCBez == null) ? 0 : kontoCBez.hashCode());
		result = prime * result + ((kontoCNr == null) ? 0 : kontoCNr.hashCode());
		result = prime * result + ((mwstSatzBezCBez == null) ? 0 : mwstSatzBezCBez.hashCode());
		result = prime * result + ((mwstSatzBezIId == null) ? 0 : mwstSatzBezIId.hashCode());
		result = prime * result + ((reversechargeartCBez == null) ? 0 : reversechargeartCBez.hashCode());
		result = prime * result + ((reversechargeartCnr == null) ? 0 : reversechargeartCnr.hashCode());
		result = prime * result + ((reversechargeartId == null) ? 0 : reversechargeartId.hashCode());
		result = prime * result + ((steuer == null) ? 0 : steuer.hashCode());
		result = prime * result + ((steuerart == null) ? 0 : steuerart.hashCode());
		result = prime * result + ((steuerkategorieCBez == null) ? 0 : steuerkategorieCBez.hashCode());
		result = prime * result + steuerkategorieISort;
		result = prime * result + ((steuersatz == null) ? 0 : steuersatz.hashCode());
		result = prime * result + ((uvaartCnr == null) ? 0 : uvaartCnr.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UstVerprobungRow other = (UstVerprobungRow) obj;
		if (betrag == null) {
			if (other.betrag != null)
				return false;
		} else if (!betrag.equals(other.betrag))
			return false;
		if (datum == null) {
			if (other.datum != null)
				return false;
		} else if (!datum.equals(other.datum))
			return false;
		if (gegenkontoCBez == null) {
			if (other.gegenkontoCBez != null)
				return false;
		} else if (!gegenkontoCBez.equals(other.gegenkontoCBez))
			return false;
		if (gegenkontoCNr == null) {
			if (other.gegenkontoCNr != null)
				return false;
		} else if (!gegenkontoCNr.equals(other.gegenkontoCNr))
			return false;
		if (gegenkontoUvaartCnr == null) {
			if (other.gegenkontoUvaartCnr != null)
				return false;
		} else if (!gegenkontoUvaartCnr.equals(other.gegenkontoUvaartCnr))
			return false;
		if (kontoCBez == null) {
			if (other.kontoCBez != null)
				return false;
		} else if (!kontoCBez.equals(other.kontoCBez))
			return false;
		if (kontoCNr == null) {
			if (other.kontoCNr != null)
				return false;
		} else if (!kontoCNr.equals(other.kontoCNr))
			return false;
		if (mwstSatzBezCBez == null) {
			if (other.mwstSatzBezCBez != null)
				return false;
		} else if (!mwstSatzBezCBez.equals(other.mwstSatzBezCBez))
			return false;
		if (mwstSatzBezIId == null) {
			if (other.mwstSatzBezIId != null)
				return false;
		} else if (!mwstSatzBezIId.equals(other.mwstSatzBezIId))
			return false;
		if (reversechargeartCBez == null) {
			if (other.reversechargeartCBez != null)
				return false;
		} else if (!reversechargeartCBez.equals(other.reversechargeartCBez))
			return false;
		if (reversechargeartCnr == null) {
			if (other.reversechargeartCnr != null)
				return false;
		} else if (!reversechargeartCnr.equals(other.reversechargeartCnr))
			return false;
		if (reversechargeartId == null) {
			if (other.reversechargeartId != null)
				return false;
		} else if (!reversechargeartId.equals(other.reversechargeartId))
			return false;
		if (steuer == null) {
			if (other.steuer != null)
				return false;
		} else if (!steuer.equals(other.steuer))
			return false;
		if (steuerart == null) {
			if (other.steuerart != null)
				return false;
		} else if (!steuerart.equals(other.steuerart))
			return false;
		if (steuerkategorieCBez == null) {
			if (other.steuerkategorieCBez != null)
				return false;
		} else if (!steuerkategorieCBez.equals(other.steuerkategorieCBez))
			return false;
		if (steuerkategorieISort != other.steuerkategorieISort)
			return false;
		if (steuersatz == null) {
			if (other.steuersatz != null)
				return false;
		} else if (!steuersatz.equals(other.steuersatz))
			return false;
		if (uvaartCnr == null) {
			if (other.uvaartCnr != null)
				return false;
		} else if (!uvaartCnr.equals(other.uvaartCnr))
			return false;
		
		if(kontoMwstSatzId == null) {
			if(other.kontoMwstSatzId != null) {
				return false;
			}
		} else if(!kontoMwstSatzId.equals(other.kontoMwstSatzId)) {
			return false;
		}
		if(gegenkontoMwstSatzId == null) {
			if(other.gegenkontoMwstSatzId != null) {
				return false;
			}
		} else if(!gegenkontoMwstSatzId.equals(other.gegenkontoMwstSatzId)) {
			return false;
		}
		return true;
	}
	
	public boolean equalsAusgenommenBetraege(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UstVerprobungRow other = (UstVerprobungRow) obj;
		if (gegenkontoCBez == null) {
			if (other.gegenkontoCBez != null)
				return false;
		} else if (!gegenkontoCBez.equals(other.gegenkontoCBez))
			return false;
		if (gegenkontoCNr == null) {
			if (other.gegenkontoCNr != null)
				return false;
		} else if (!gegenkontoCNr.equals(other.gegenkontoCNr))
			return false;
		if (gegenkontoUvaartCnr == null) {
			if (other.gegenkontoUvaartCnr != null)
				return false;
		} else if (!gegenkontoUvaartCnr.equals(other.gegenkontoUvaartCnr))
			return false;
		if (kontoCBez == null) {
			if (other.kontoCBez != null)
				return false;
		} else if (!kontoCBez.equals(other.kontoCBez))
			return false;
		if (kontoCNr == null) {
			if (other.kontoCNr != null)
				return false;
		} else if (!kontoCNr.equals(other.kontoCNr))
			return false;
		if (mwstSatzBezCBez == null) {
			if (other.mwstSatzBezCBez != null)
				return false;
		} else if (!mwstSatzBezCBez.equals(other.mwstSatzBezCBez))
			return false;
		if (mwstSatzBezIId == null) {
			if (other.mwstSatzBezIId != null)
				return false;
		} else if (!mwstSatzBezIId.equals(other.mwstSatzBezIId))
			return false;
		if (reversechargeartCBez == null) {
			if (other.reversechargeartCBez != null)
				return false;
		} else if (!reversechargeartCBez.equals(other.reversechargeartCBez))
			return false;
		if (reversechargeartCnr == null) {
			if (other.reversechargeartCnr != null)
				return false;
		} else if (!reversechargeartCnr.equals(other.reversechargeartCnr))
			return false;
		if (reversechargeartId == null) {
			if (other.reversechargeartId != null)
				return false;
		} else if (!reversechargeartId.equals(other.reversechargeartId))
			return false;
		if (steuerart == null) {
			if (other.steuerart != null)
				return false;
		} else if (!steuerart.equals(other.steuerart))
			return false;
		if (steuerkategorieCBez == null) {
			if (other.steuerkategorieCBez != null)
				return false;
		} else if (!steuerkategorieCBez.equals(other.steuerkategorieCBez))
			return false;
		if (steuerkategorieISort != other.steuerkategorieISort)
			return false;
		if (steuersatz == null) {
			if (other.steuersatz != null)
				return false;
		} else if (!steuersatz.equals(other.steuersatz))
			return false;
		if (uvaartCnr == null) {
			if (other.uvaartCnr != null)
				return false;
		} else if (!uvaartCnr.equals(other.uvaartCnr))
			return false;
		if(kontoMwstSatzId == null) {
			if(other.kontoMwstSatzId != null) {
				return false;
			}
		} else if(!kontoMwstSatzId.equals(other.kontoMwstSatzId)) {
			return false;
		}
		if(gegenkontoMwstSatzId == null) {
			if(other.gegenkontoMwstSatzId != null) {
				return false;
			}
		} else if(!gegenkontoMwstSatzId.equals(other.gegenkontoMwstSatzId)) {
			return false;
		}

		return true;
	}

//	public boolean equalsAusgenommenBetraege(UstVerprobungRow obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		UstVerprobungRow other = (UstVerprobungRow) obj;
//		if (gegenkontoCBez == null) {
//			if (other.gegenkontoCBez != null)
//				return false;
//		} else if (!gegenkontoCBez.equals(other.gegenkontoCBez))
//			return false;
//		if (gegenkontoCNr == null) {
//			if (other.gegenkontoCNr != null)
//				return false;
//		} else if (!gegenkontoCNr.equals(other.gegenkontoCNr))
//			return false;
//		if (kontoCBez == null) {
//			if (other.kontoCBez != null)
//				return false;
//		} else if (!kontoCBez.equals(other.kontoCBez))
//			return false;
//		if (kontoCNr == null) {
//			if (other.kontoCNr != null)
//				return false;
//		} else if (!kontoCNr.equals(other.kontoCNr))
//			return false;
//		if (mwstSatzBezCBez == null) {
//			if (other.mwstSatzBezCBez != null)
//				return false;
//		} else if (!mwstSatzBezCBez.equals(other.mwstSatzBezCBez))
//			return false;
//		if (mwstSatzBezIId == null) {
//			if (other.mwstSatzBezIId != null)
//				return false;
//		} else if (!mwstSatzBezIId.equals(other.mwstSatzBezIId))
//			return false;
//		if (steuerart == null) {
//			if (other.steuerart != null)
//				return false;
//		} else if (!steuerart.equals(other.steuerart))
//			return false;
//		if (steuerkategorieCBez == null) {
//			if (other.steuerkategorieCBez != null)
//				return false;
//		} else if (!steuerkategorieCBez.equals(other.steuerkategorieCBez))
//			return false;
//		if (steuerkategorieISort != other.steuerkategorieISort)
//			return false;
//		if (steuersatz == null) {
//			if (other.steuersatz != null)
//				return false;
//		} else if (!steuersatz.equals(other.steuersatz))
//			return false;
//		return true;
//	}
	
	@Override
	public String asString() {
		return new StringBuffer("UstV: [") 
				.append("Datum: ").append(new Date(getDatum().getTime()).toString()) 
				.append(", Betrag: ").append((getBetrag() != null ? getBetrag().toPlainString() : "<keiner>"))
				.append(", UST: ").append((getSteuer() != null ? getSteuer().toPlainString() : "<keine>"))
				.append(", Konto: ").append((getKontoCNr() != null ? (getKontoCNr() + " " + getKontoCBez()) : "<keines>"))
				.append(", Gegenkonto: ").append((getGegenkontoCNr() != null ? (getGegenkontoCNr() + " " + getGegenkontoCBez()) : "<keines>")) 
				.append(", Steuerart: ").append(getSteuerart() + " " + getSteuerkategorieCBez()) 
				.append(", Reversecharge: ").append(getReversechargeartCnr())
				.append(", Uvaart: ").append((getUvaartCnr() != null ? getUvaartCnr() : "<keine>"))
				.append(", Uvaartgegenkonto: ").append((getGegenkontoUvaartCnr() != null ? getGegenkontoUvaartCnr() : "<keine>"))
				.append(", BuchungId: ").append((getBuchungId() != null ? getBuchungId() : "<keine>"))
				.append("]").toString();		
	}
	public String getBuchungText() {
		return buchungText;
	}
	public void setBuchungText(String buchungText) {
		this.buchungText = buchungText;
	}
	public String getBuchungBelegnummer() {
		return buchungBelegnummer;
	}
	public void setBuchungBelegnummer(String buchungBelegnummer) {
		this.buchungBelegnummer = buchungBelegnummer;
	}
	public String getBelegartCnr() {
		return belegartCnr;
	}
	public void setBelegartCnr(String buchungArtCnr) {
		this.belegartCnr = buchungArtCnr;
	}
	public Timestamp getBelegDatum() {
		return belegDatum;
	}
	public void setBelegDatum(Timestamp belegDatum) {
		this.belegDatum = belegDatum;
	}
	public Integer getKontoMwstsatzId() {
		return kontoMwstSatzId;
	}
	public void setKontoMwstsatzId(Integer mwstSatzIdAusKonto) {
		this.kontoMwstSatzId = mwstSatzIdAusKonto;
	}
	public BigDecimal getKontoSteuersatz() {
		return kontoSteuersatz;
	}
	public void setKontoSteuersatz(BigDecimal steuersatzAusKonto) {
		this.kontoSteuersatz = steuersatzAusKonto;
	}
	public BigDecimal getGegenkontoSteuersatz() {
		return gegenkontoSteuersatz;
	}
	public void setGegenkontoSteuersatz(BigDecimal gegenkontoSteuersatz) {
		this.gegenkontoSteuersatz = gegenkontoSteuersatz;
	}
	public Integer getGegenkontoMwstsatzId() {
		return gegenkontoMwstSatzId;
	}
	public void setGegenkontoMwstsatzId(Integer gegenkontoMwstSatzId) {
		this.gegenkontoMwstSatzId = gegenkontoMwstSatzId;
	}
}
