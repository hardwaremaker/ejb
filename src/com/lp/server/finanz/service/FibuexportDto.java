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

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;

import com.lp.server.partner.service.PartnerDto;
import com.lp.server.system.service.KostenstelleDto;
import com.lp.server.system.service.MwstsatzDto;
import com.lp.server.system.service.ZahlungszielDto;

/**
 * <p>
 * Diese Klasse kuemmert sich um den datenexport in eine externe fibu
 * </p>
 * 
 * <p>
 * Copyright Logistik Pur Software GmbH (c) 2004-2007
 * </p>
 * 
 * <p>
 * Erstellung: Martin Bluehweis; 30.09.05
 * </p>
 * 
 * <p>
 * 
 * @author $Author: adi $
 *         </p>
 * 
 * @version not attributable Date $Date: 2012/06/20 12:19:51 $
 */
public class FibuexportDto {
	private KontoDto konto = null;
	private KontoDto gegenkonto = null;
	private String belegnummer = null;
	// Offene Posten Nummer: AR/GS: Belegnummer, ER: Lieferantenrechnungsnummer
	private String sOPNummer = null;
	// Belegdatum
	private Date belegdatum = null;
	// Valutadatum AR/GS: Belegdatum, ER: Freigabedatum
	private Date valutadatum = null;
	// Mandantenwaehrung
	private String waehrung = null;

	private BigDecimal sollbetrag = null;
	private BigDecimal habenbetrag = null;
	private BigDecimal steuerbetrag = null;
	private BigDecimal steuerbetragFW = null;
	// belegwaehrung
	private String fremdwaehrung = null;
	private BigDecimal sollbetragFW = null;
	private BigDecimal habenbetragFW = null;

	private KostenstelleDto kost = null;

	private String belegart = null;

	private String laenderartCNr = null;

	private String text = null;

	private String uidNummer = null;

	private String satzart = null;
	private MwstsatzDto mwstsatz = null;
	private String steucod = null;
	private String bucod = null;
	private Integer periode = null;
	private BigDecimal kurs = null;
	private BigDecimal fwbetrag = null;
	private BigDecimal fwsteuer = null;
	private String zziel = null;
	private String skontopz = null;
	private String skontotage = null;
	private String skontopz2 = null;
	private String skontotage2 = null;
	private String vertnr = null;
	private String auftkz = null;
	private String auftnr = null;
	private String zmart = null;
	private String verbuchkz = null;
	private ZahlungszielDto zahlungszielDto;
	private PartnerDto partnerDto = null;
	private Integer iGeschaeftsjahr;
	private String sExterneBelegnummer = null;
	private Integer debitorenKontoIIdUebersteuert = null;
	private boolean bReverseCharge = false;
	
	public FibuexportDto() {
	}

	public void setGkto(KontoDto gkto) {
		this.gegenkonto = gkto;
	}

	public void setKonto(KontoDto konto) {
		this.konto = konto;
	}

	public void setKost(KostenstelleDto kost) {
		this.kost = kost;
	}

	public void setKurs(BigDecimal kurs) {
		this.kurs = kurs;
	}

	public void setMwstsatz(MwstsatzDto mwst) {
		this.mwstsatz = mwst;
	}

	public void setPeriode(Integer periode) {
		this.periode = periode;
	}

	public void setSatzart(String satzart) {
		this.satzart = satzart;
	}

	public void setSteucod(String steucod) {
		this.steucod = steucod;
	}

	public void setSteuer(BigDecimal steuer) {
		this.steuerbetrag = steuer;
	}
	
	public void setSteuerFW(BigDecimal steuerFW) {
		this.steuerbetragFW = steuerFW;
	}
	public void setBelegart(String belegart) {
		this.belegart = belegart;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setVerbuchkz(String verbuchkz) {
		this.verbuchkz = verbuchkz;
	}

	public void setVertnr(String vertnr) {
		this.vertnr = vertnr;
	}

	public void setZmart(String zmart) {
		this.zmart = zmart;
	}

	public void setAuftkz(String auftkz) {
		this.auftkz = auftkz;
	}

	public void setAuftnr(String auftnr) {
		this.auftnr = auftnr;
	}

	public void setBelegdatum(java.util.Date belegdatum) {
		this.belegdatum = belegdatum;
	}

	public void setValutadatum(java.util.Date valutadatum) {
		this.valutadatum = valutadatum;
	}

	public void setBelegnr(String belegnr) {
		this.belegnummer = belegnr;
	}

	public void setWaehrung(String waehrung) {
		this.waehrung = waehrung;
	}

	public void setFremdwaehrung(String fremdwaehrung) {
		this.fremdwaehrung = fremdwaehrung;
	}

	public void setSollbetrag(BigDecimal sollbetrag) {
		this.sollbetrag = sollbetrag;
	}

	public void setSollbetragFW(BigDecimal sollbetragFW) {
		this.sollbetragFW = sollbetragFW;
	}

	public void setHabenbetrag(BigDecimal habenbetrag) {
		this.habenbetrag = habenbetrag;
	}

	public void setHabenbetragFW(BigDecimal habenbetragFW) {
		this.habenbetragFW = habenbetragFW;
	}

	public void setBucod(String bucod) {
		this.bucod = bucod;
	}

	public void setFwbetrag(BigDecimal fwbetrag) {
		this.fwbetrag = fwbetrag;
	}

	public void setFwsteuer(BigDecimal fwsteuer) {
		this.fwsteuer = fwsteuer;
	}

	public void setZahlungszielDto(ZahlungszielDto zzDto) {
		if (zzDto != null) {
			this.zahlungszielDto = zzDto;
			if (zzDto.getAnzahlZieltageFuerNetto() != null) {
				this.zziel = zzDto.getAnzahlZieltageFuerNetto().toString();
			}
			if (zzDto.getSkontoAnzahlTage1() != null) {
				this.skontotage = zzDto.getSkontoAnzahlTage1().toString();
			}
			if (zzDto.getSkontoAnzahlTage2() != null) {
				this.skontotage2 = zzDto.getSkontoAnzahlTage2().toString();
			}
			if (zzDto.getSkontoProzentsatz1() != null) {
				this.skontopz = formatNumber(zzDto.getSkontoProzentsatz1());
			}
			if (zzDto.getSkontoProzentsatz2() != null) {
				this.skontopz2 = formatNumber(zzDto.getSkontoProzentsatz2());
			}
		}
	}

	public void setLaenderartCNr(String laenderartCNr) {
		this.laenderartCNr = laenderartCNr;
	}

	public void setUidNummer(String uidNummer) {
		this.uidNummer = uidNummer;
	}

	public String getAuftkz() {
		return auftkz;
	}

	public String getAuftnr() {
		return auftnr;
	}

	public String getBelegnummer() {
		return belegnummer;
	}

	public String getBucod() {
		return bucod;
	}

	public String getGegenkonto() {
		if (gegenkonto != null) {
			return gegenkonto.getCNr();
		} else {
			return "";
		}
	}

	public KontoDto getGegenkontoDto() {
		return gegenkonto;
	}

	public String getKontonummer() {
		if (konto != null) {
			return konto.getCNr();
		} else {
			return "";
		}
	}

	public KontoDto getKontoDto() {
		return konto;
	}

	public String getKostenstelle() {
		if (kost != null) {
			return kost.getCNr();
		} else {
			return "";
		}
	}

	public KostenstelleDto getKostDto() {
		return kost;
	}

	public String getSatzart() {
		return satzart;
	}

	public String getSkontopz() {
		return skontopz;
	}

	public String getSkontopz2() {
		return skontopz2;
	}

	public String getSkontotage() {
		return skontotage;
	}

	public String getSkontotage2() {
		return skontotage2;
	}

	public String getSteucod() {
		return steucod;
	}

	public String getBelegart() {
		return belegart;
	}

	public String getText() {
		return text;
	}

	public String getVerbuchkz() {
		return verbuchkz;
	}

	public String getVertnr() {
		return vertnr;
	}

	public ZahlungszielDto getZahlungszielDto() {
		return zahlungszielDto;
	}

	public String getZmart() {
		return zmart;
	}

	public String getZziel() {
		return zziel;
	}

	public String getSteuerbetrag() {
		if (steuerbetrag != null) {
			return formatNumber(steuerbetrag);
		} else {
			return "";
		}
	}
	
	public String getSteuerbetragFW() {
		if (steuerbetragFW != null) {
			return formatNumber(steuerbetragFW);
		} else {
			return "";
		}
	}

	public BigDecimal getSteuerBD() {
		return steuerbetrag;
	}
	
	public BigDecimal getSteuerFWBD() {
		return steuerbetragFW;
	}

	public String getPeriode() {
		if (periode != null) {
			return formatNumber(periode);
		} else {
			return "";
		}
	}

	public MwstsatzDto getMwstsatz() {
		return mwstsatz;
	}

	public BigDecimal getKurs() {
		return kurs;
	}

	public String getFwsteuer() {
		if (fwsteuer != null) {
			return formatNumber(fwsteuer);
		} else {
			return "";
		}
	}

	public String getFwbetrag() {
		if (fwbetrag != null) {
			return formatNumber(fwbetrag);
		} else {
			return "";
		}
	}

	public String getSollbetrag() {
		if (sollbetrag != null) {
			return formatNumber(sollbetrag);
		} else {
			return "";
		}
	}

	public String getSollbetragFW() {
		if (sollbetragFW != null) {
			return formatNumber(sollbetragFW);
		} else {
			return "";
		}
	}

	public BigDecimal getSollbetragFWBD() {
		return sollbetragFW;
	}

	public String getHabenbetrag() {
		if (habenbetrag != null) {
			return formatNumber(habenbetrag);
		} else {
			return "";
		}
	}
	
	public String getPositionsbetragLeitwaehrung(){
		if (habenbetrag != null) {
			return formatNumber(habenbetrag.add(this.getSteuerBD()));
		} else {
			return "";
		}
	}
	
	public BigDecimal getPositionsbetragLeitwaehrungBD(){
			return habenbetrag.add(this.getSteuerBD());
	}
	
	public String getPositionsbetragBelegwaehrung(){
		if(habenbetragFW!=null){
			return formatNumber(habenbetragFW.add(this.getSteuerFWBD()));
		} else {
			return "";
		}
	}
	


	public String getHabenbetragFW() {
		if (habenbetragFW != null) {
			return formatNumber(habenbetragFW);
		} else {
			return "";
		}
	}
	
	

	public BigDecimal getHabenbetragFWBD() {
		return habenbetragFW;
	}

	public BigDecimal getSollbetragBD() {
		return sollbetrag;
	}

	public BigDecimal getHabenbetragBD() {
		return habenbetrag;
	}

	public Date getBelegdatum() {
		return belegdatum;
	}

	public Date getValutadatum() {
		return valutadatum;
	}

	public String getOPNummer() {
		return sOPNummer;
	}

	public void setOPNummer(String sOPNummer) {
		this.sOPNummer = sOPNummer;
	}

	public String getWaehrung() {
		return waehrung;
	}

	public String getFremdwaehrung() {
		return fremdwaehrung;
	}

	public String getLaenderartCNr() {
		return laenderartCNr;
	}

	public String getUidNummer() {
		// WH 04.04.06 IMS 1901 alle Leerzeichen wegfiltern
		if (uidNummer != null) {
			return uidNummer.replaceAll(" ", "").toUpperCase();
		} else {
			return uidNummer;
		}
	}

	public PartnerDto getPartnerDto() {
		return partnerDto;
	}

	public void setPartnerDto(PartnerDto partnerDto) {
		this.partnerDto = partnerDto;
	}

	public Integer getIGeschaeftsjahr() {
		return iGeschaeftsjahr;
	}

	public void setIGeschaeftsjahr(Integer iGeschaeftsjahr) {
		this.iGeschaeftsjahr = iGeschaeftsjahr;
	}

	public static String formatNumber(Number n) {
		if (n != null) {
			NumberFormat nf = new DecimalFormat("#########0.00");
			return nf.format(n.doubleValue());
		} else {
			return "";
		}
	}

	public String getSExterneBelegnummer() {
		return sExterneBelegnummer;
	}

	public void setSExterneBelegnummer(String sExterneBelegnummer) {
		this.sExterneBelegnummer = sExterneBelegnummer;
	}

	public void setDebitorenKontoIIdUebersteuert(
			Integer debitorenKontoIIdUebersteuert) {
		this.debitorenKontoIIdUebersteuert = debitorenKontoIIdUebersteuert;
	}

	public Integer getDebitorenKontoIIdUebersteuert() {
		return debitorenKontoIIdUebersteuert;
	}

	public void setBReverseCharge(boolean bReverseCharge) {
		this.bReverseCharge = bReverseCharge;
	}

	public boolean isBReverseCharge() {
		return bReverseCharge;
	}

}
