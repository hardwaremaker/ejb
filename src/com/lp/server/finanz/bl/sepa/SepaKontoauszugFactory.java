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
package com.lp.server.finanz.bl.sepa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.lp.server.finanz.service.ISepaCamtFormat;
import com.lp.server.finanz.service.SepaBankTransactionCode;
import com.lp.server.finanz.service.SepaBankTransactionCodeV1;
import com.lp.server.finanz.service.SepaBankTransactionCodeV2;
import com.lp.server.finanz.service.SepaBankTransactionCodeV3;
import com.lp.server.finanz.service.SepaBetrag;
import com.lp.server.finanz.service.SepaBetragV1;
import com.lp.server.finanz.service.SepaBetragV2;
import com.lp.server.finanz.service.SepaBetragV3;
import com.lp.server.finanz.service.SepaBuchung;
import com.lp.server.finanz.service.SepaBuchungV1;
import com.lp.server.finanz.service.SepaBuchungV2;
import com.lp.server.finanz.service.SepaBuchungV3;
import com.lp.server.finanz.service.SepaCamtFormat052;
import com.lp.server.finanz.service.SepaCamtFormat053;
import com.lp.server.finanz.service.SepaCamtFormatEnum;
import com.lp.server.finanz.service.SepaCamtVersionEnum;
import com.lp.server.finanz.service.SepaHabenBetrag;
import com.lp.server.finanz.service.SepaKontoauszug;
import com.lp.server.finanz.service.SepaKontoauszugV1;
import com.lp.server.finanz.service.SepaKontoauszugV2;
import com.lp.server.finanz.service.SepaKontoauszugV3;
import com.lp.server.finanz.service.SepaKontoauszugVersionEnum;
import com.lp.server.finanz.service.SepaKontoinformation;
import com.lp.server.finanz.service.SepaKontoinformationV1;
import com.lp.server.finanz.service.SepaKontoinformationV2;
import com.lp.server.finanz.service.SepaKontoinformationV3;
import com.lp.server.finanz.service.SepaSaldo;
import com.lp.server.finanz.service.SepaSaldoCodeEnum;
import com.lp.server.finanz.service.SepaSaldoV1;
import com.lp.server.finanz.service.SepaSaldoV2;
import com.lp.server.finanz.service.SepaSaldoV3;
import com.lp.server.finanz.service.SepaSollBetrag;
import com.lp.server.finanz.service.SepaSpesen;
import com.lp.server.finanz.service.SepaSpesenV3;
import com.lp.server.finanz.service.SepaZahlung;
import com.lp.server.finanz.service.SepaZahlungV1;
import com.lp.server.finanz.service.SepaZahlungV2;
import com.lp.server.finanz.service.SepaZahlungV3;
import com.lp.util.EJBExceptionLP;

public class SepaKontoauszugFactory {

	public SepaKontoauszug getSepaKontoauszug(SepaKontoauszugVersionEnum version, Object kontoauszugObject) {
		
		if (SepaKontoauszugVersionEnum.EINS.equals(version)) {
			return convertToKontoauszug((SepaKontoauszugV1) kontoauszugObject);
		} else if (SepaKontoauszugVersionEnum.ZWEI.equals(version)) {
			SepaKontoauszugV2Mapper v2Mapper = new SepaKontoauszugV2Mapper();
			return v2Mapper.map((SepaKontoauszugV2)kontoauszugObject);
		} else if (SepaKontoauszugVersionEnum.DREI.equals(version)) {
			SepaKontoauszugV3Mapper v3Mapper = new SepaKontoauszugV3Mapper();
			return v3Mapper.map((SepaKontoauszugV3)kontoauszugObject);
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET, 
					"Kein SepaKontoauszug definiert f\u00FCr Version " + version.getValue());
		}
	}
	
	public Serializable getSepaKontoauszugObject(SepaKontoauszugVersionEnum version, SepaKontoauszug kontoauszug) {
		
		if (SepaKontoauszugVersionEnum.EINS.equals(version)) {
			return convertToKontoauszugV1(kontoauszug);
		} else if (SepaKontoauszugVersionEnum.ZWEI.equals(version)) {
			SepaKontoauszugV2Mapper v2Mapper = new SepaKontoauszugV2Mapper();
			return v2Mapper.map(kontoauszug);
		} else if (SepaKontoauszugVersionEnum.DREI.equals(version)) {
			SepaKontoauszugV3Mapper v3Mapper = new SepaKontoauszugV3Mapper();
			return v3Mapper.map(kontoauszug);
		} else {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_NOT_IMPLEMENTED_YET, 
					"Kein SepaKontoauszug definiert f\u00FCr Version " + version.getValue());
		}
	}

	private SepaKontoauszug convertToKontoauszug(SepaKontoauszugV1 source) {
		SepaKontoauszug converted = new SepaKontoauszug();
		converted.setAuszugsId(source.getCAuszugId());
		converted.setAuszugsnr(source.getBdAuszugsNr());
		converted.setbLetzteSeite(source.getBIstLetzteSeite());
		converted.setElektronischeAuszugsnr(source.getBdElektronischeAuszugsNr());
		converted.setEmpfaenger(source.getCEmpfaenger());
		converted.setErstellungsDatum(source.getDErstellung());
		converted.setMessageId(source.getCMessageId());
		converted.setSeitennummer(source.getISeitennummer());
		converted.setTypVersion(getCamtFormat(source.getCCampVersion()));
		
		converted.setSalden(getSepaSalden(source.getSalden()));
		converted.setKontoInfo(getSepaKontoinformation(source.getKontoinformation()));
		converted.setBuchungen(getSepaBuchungen(source.getBuchungen()));
		
		return converted;
	}
	
	private ISepaCamtFormat getCamtFormat(String sCampFormat) {
		String[] tokens = sCampFormat.split("\\.");
		if (tokens == null || tokens.length != 4) return null;
		
		try {
			SepaCamtFormatEnum camtFormatEnum = SepaCamtFormatEnum.fromString(tokens[0] + "." + tokens[1]);
			
			ISepaCamtFormat camtFormat = null;
			if (SepaCamtFormatEnum.CAMT052.equals(camtFormatEnum)) {
				camtFormat = new SepaCamtFormat052();
				camtFormat.setCamtVersionEnum(SepaCamtVersionEnum.fromString(tokens[3]));
			}
			if (SepaCamtFormatEnum.CAMT053.equals(camtFormatEnum)) {
				camtFormat = new SepaCamtFormat053();
				camtFormat.setCamtVersionEnum(SepaCamtVersionEnum.fromString(tokens[3]));
			}
			
			return camtFormat;
		} catch (IllegalArgumentException ex) {
			throw new EJBExceptionLP(EJBExceptionLP.FEHLER_SEPAIMPORT_AUSGEWAEHLTES_CAMT_FORMAT_STIMMT_NICHT_UEBEREIN, ex);
		}
	}

	private List<SepaSaldo> getSepaSalden(List<SepaSaldoV1> saldenV1) {
		List<SepaSaldo> salden = new ArrayList<SepaSaldo>();
		if (saldenV1 == null) return salden;
		
		for (SepaSaldoV1 saldoV1 : saldenV1) {
			SepaSaldo saldo = new SepaSaldo();
			saldo.setDatum(saldoV1.getDDatum());
			saldo.setSaldocode(SepaSaldoCodeEnum.fromString(saldoV1.getCSaldocode()));
			saldo.setWaehrung(saldoV1.getCWaehrung());
			saldo.setBetrag(getSepaBetrag(saldoV1.getBetrag()));
			
			salden.add(saldo);
		}
		
		return salden;
	}

	private SepaKontoinformation getSepaKontoinformation(SepaKontoinformationV1 kontoinfoV1) {
		if (kontoinfoV1 == null) return null;
		
		SepaKontoinformation kontoinfo = new SepaKontoinformation();
		kontoinfo.setBic(kontoinfoV1.getCBic());
		kontoinfo.setIban(kontoinfoV1.getCIban());
		kontoinfo.setName(kontoinfoV1.getCName());
		
		return kontoinfo;
	}

	private List<SepaBuchung> getSepaBuchungen(List<SepaBuchungV1> buchungenV1) {
		List<SepaBuchung> buchungen = new ArrayList<SepaBuchung>();
		
		for (SepaBuchungV1 buchungV1 : buchungenV1) {
			SepaBuchung buchung = new SepaBuchung();
			buchung.setBankreferenz(buchungV1.getCBankreferenz());
			buchung.setBetrag(getSepaBetrag(buchungV1.getBetrag()));
			buchung.setBuchungscode(getSepaBuchungscode(buchungV1.getBuchungscode()));
			buchung.setBuchungsdatum(buchungV1.getDBuchungsdatum());
			buchung.setStatus(buchungV1.getCStatus());
			buchung.setValutadatum(buchungV1.getDValutadatum());
			buchung.setWaehrung(buchungV1.getCWaehrung());
			buchung.setZahlungen(getSepaZahlungen(buchungV1.getZahlungen()));
			
			buchungen.add(buchung);
		}
		
		return buchungen;
	}

	private List<SepaZahlung> getSepaZahlungen(List<SepaZahlungV1> zahlungenV1) {
		List<SepaZahlung> zahlungen = new ArrayList<SepaZahlung>();
		if (zahlungenV1 == null) return zahlungen;
		
		for (SepaZahlungV1 zahlungV1 : zahlungenV1) {
			SepaZahlung zahlung = new SepaZahlung();
			zahlung.setAuftraggeberreferenz(zahlungV1.getcAuftraggeberreferenz());
			zahlung.setBankreferenz(zahlungV1.getcBankreferenz());
			zahlung.setBeteiligter(getSepaKontoinformation(zahlungV1.getKontoinformation()));
			zahlung.setBetrag(getSepaBetrag(zahlungV1.getBetrag()));
			zahlung.setBuchungscode(getSepaBuchungscode(zahlungV1.getBuchungscode()));
			zahlung.setVerwendungszweck(zahlungV1.getcVerwendungszweck());
			zahlung.setZahlungsreferenz(zahlungV1.getcZahlungsreferenz());
			zahlung.setEnthalteneBuchungen(1);
			
			zahlungen.add(zahlung);
		}
		
		return zahlungen;
	}

	private SepaBankTransactionCode getSepaBuchungscode(SepaBankTransactionCodeV1 buchungscodeV1) {
		if (buchungscodeV1 == null) return null;
		
		SepaBankTransactionCode buchungscode = new SepaBankTransactionCode();
		buchungscode.setCode(buchungscodeV1.getCCode());
		buchungscode.setFamilie(buchungscodeV1.getCFamilie());
		
		return buchungscode;
	}

	private SepaBetrag getSepaBetrag(SepaBetragV1 betragV1) {
		if (betragV1 == null) return null;
		
		SepaBetrag betrag;
		if (betragV1.getBIstHaben()) {
			betrag = new SepaHabenBetrag(betragV1.getBdWert());
		} else {
			betrag = new SepaSollBetrag(betragV1.getBdWert());
		}
		return betrag;
	}

	private SepaKontoauszugV1 convertToKontoauszugV1(SepaKontoauszug source) {
		SepaKontoauszugV1 converted = new SepaKontoauszugV1();
		
		converted.setCAuszugId(source.getAuszugsId());
		converted.setBdAuszugsNr(source.getAuszugsnr());
		converted.setBIstLetzteSeite(source.getbLetzteSeite());
		converted.setBdElektronischeAuszugsNr(source.getElektronischeAuszugsnr());
		converted.setCEmpfaenger(source.getEmpfaenger());
		converted.setDErstellung(source.getErstellungsDatum());
		converted.setCMessageId(source.getMessageId());
		converted.setISeitennummer(source.getSeitennummer());
		converted.setCCampVersion(source.getTypVersion() != null ? source.getTypVersion().toString() : null);
		
		converted.setSalden(getSepaSaldenV1(source.getSalden()));
		converted.setKontoinformation(getSepaKontoinformationV1(source.getKontoInfo()));
		converted.setBuchungen(getSepaBuchungenV1(source.getBuchungen()));

		return converted;
	}

	private List<SepaBuchungV1> getSepaBuchungenV1(List<SepaBuchung> buchungen) {
		List<SepaBuchungV1> buchungenV1 = new ArrayList<SepaBuchungV1>();
		
		for (SepaBuchung buchung : buchungen) {
			SepaBuchungV1 buchungV1 = new SepaBuchungV1();
			buchungV1.setCBankreferenz(buchung.getBankreferenz());
			buchungV1.setBetrag(getSepaBetragV1(buchung.getBetrag()));
			buchungV1.setBuchungscode(getSepaBuchungscodeV1(buchung.getBuchungscode()));
			buchungV1.setDBuchungsdatum(buchung.getBuchungsdatum());
			buchungV1.setCStatus(buchung.getStatus());
			buchungV1.setDValutadatum(buchung.getValutadatum());
			buchungV1.setCWaehrung(buchung.getWaehrung());
			buchungV1.setZahlungen(getSepaZahlungenV1(buchung.getZahlungen()));
			
			buchungenV1.add(buchungV1);
		}
		
		return buchungenV1;
	}

	private List<SepaZahlungV1> getSepaZahlungenV1(List<SepaZahlung> zahlungen) {
		List<SepaZahlungV1> zahlungenV1 = new ArrayList<SepaZahlungV1>();
		if (zahlungen == null) return zahlungenV1;
		
		for (SepaZahlung zahlung : zahlungen) {
			SepaZahlungV1 zahlungV1 = new SepaZahlungV1();
			zahlungV1.setcAuftraggeberreferenz(zahlung.getAuftraggeberreferenz());
			zahlungV1.setcBankreferenz(zahlung.getBankreferenz());
			zahlungV1.setKontoinformation(getSepaKontoinformationV1(zahlung.getBeteiligter()));
			zahlungV1.setBetrag(getSepaBetragV1(zahlung.getBetrag()));
			zahlungV1.setBuchungscode(getSepaBuchungscodeV1(zahlung.getBuchungscode()));
			zahlungV1.setcVerwendungszweck(zahlung.getVerwendungszweck());
			zahlungV1.setcZahlungsreferenz(zahlung.getZahlungsreferenz());
			
			zahlungenV1.add(zahlungV1);
		}
		
		return zahlungenV1;
	}

	private SepaBankTransactionCodeV1 getSepaBuchungscodeV1(SepaBankTransactionCode buchungscode) {
		if (buchungscode == null) return null;
		
		SepaBankTransactionCodeV1 buchungscodeV1 = new SepaBankTransactionCodeV1();
		buchungscodeV1.setCCode(buchungscode.getCode());
		buchungscodeV1.setCFamilie(buchungscode.getFamilie());
		
		return buchungscodeV1;
	}

	private SepaKontoinformationV1 getSepaKontoinformationV1(SepaKontoinformation kontoInfo) {
		if (kontoInfo == null) return null;
		
		SepaKontoinformationV1 kontoinfoV1 = new SepaKontoinformationV1();
		kontoinfoV1.setCBic(kontoInfo.getBic());
		kontoinfoV1.setCIban(kontoInfo.getIban());
		kontoinfoV1.setCName(kontoInfo.getName());
		
		return kontoinfoV1;
	}

	private List<SepaSaldoV1> getSepaSaldenV1(List<SepaSaldo> salden) {
		List<SepaSaldoV1> saldenV1 = new ArrayList<SepaSaldoV1>();
		if (salden == null) return saldenV1;
		
		for (SepaSaldo saldo : salden) {
			SepaSaldoV1 saldoV1 = new SepaSaldoV1();
			saldoV1.setDDatum(saldo.getDatum());
			saldoV1.setCSaldocode(saldo.getSaldocode().getValue());
			saldoV1.setCWaehrung(saldo.getWaehrung());
			saldoV1.setBetrag(getSepaBetragV1(saldo.getBetrag()));
			
			saldenV1.add(saldoV1);
		}
		
		return saldenV1;
	}

	private SepaBetragV1 getSepaBetragV1(SepaBetrag betrag) {
		if (betrag == null) return null;
		
		SepaBetragV1 betragV1 = new SepaBetragV1();
		betragV1.setBdWert(betrag.getWert());
		betragV1.setBIstHaben(betrag.isHaben());
		return betragV1;
	}
	
	private class SepaKontoauszugV3Mapper {
		
		public SepaKontoauszugV3 map(SepaKontoauszug source) {
			SepaKontoauszugV3 version3 = new SepaKontoauszugV3();
			
			version3.setAuszugId(source.getAuszugsId());
			version3.setAuszugsNr(source.getAuszugsnr());
			version3.setCamtVersion(source.getTypVersion() != null ? source.getTypVersion().toString() : null);
			version3.setElektronischeAuszugsNr(source.getElektronischeAuszugsnr());
			version3.setEmpfaenger(source.getEmpfaenger());
			version3.setErstellungsdatum(source.getErstellungsDatum());
			version3.setIsLetzteSeite(source.getbLetzteSeite());
			version3.setMessageId(source.getMessageId());
			version3.setSeitennummer(source.getSeitennummer());
			version3.setVersion(SepaKontoauszugVersionEnum.DREI);
			version3.setZusatzinformation(source.getZusatzinformation());

			version3.setKontoinformation(mapKontoinformation(source.getKontoInfo()));
			version3.setBuchungen(mapBuchungen(source.getBuchungen()));
			version3.setSalden(mapSalden(source.getSalden()));
			
			return version3;
		}

		private List<SepaBuchungV3> mapBuchungen(List<SepaBuchung> buchungen) {
			List<SepaBuchungV3> list = new ArrayList<SepaBuchungV3>();
			if (buchungen == null) return list;
			
			for (SepaBuchung buchung : buchungen) {
				SepaBuchungV3 buchungV3 = new SepaBuchungV3();
				buchungV3.setBankreferenz(buchung.getBankreferenz());
				buchungV3.setBetrag(mapBetrag(buchung.getBetrag()));
				buchungV3.setBuchungscode(mapBuchungscode(buchung.getBuchungscode()));
				buchungV3.setBuchungsdatum(buchung.getBuchungsdatum());
				buchungV3.setStatus(buchung.getStatus());
				buchungV3.setValutadatum(buchung.getValutadatum());
				buchungV3.setWaehrung(buchung.getWaehrung());
				buchungV3.setZahlungen(mapZahlungen(buchung.getZahlungen()));
				buchungV3.setZusatzinformation(buchung.getZusatzinformation());
				
				list.add(buchungV3);
			}
			
			return list;
		}

		private List<SepaZahlungV3> mapZahlungen(List<SepaZahlung> zahlungen) {
			List<SepaZahlungV3> list = new ArrayList<SepaZahlungV3>();
			if (zahlungen == null) return list;
			
			for (SepaZahlung zahlung : zahlungen) {
				SepaZahlungV3 zahlungV3 = new SepaZahlungV3();
				zahlungV3.setAuftraggeberreferenz(zahlung.getAuftraggeberreferenz());
				zahlungV3.setBankreferenz(zahlung.getBankreferenz());
				zahlungV3.setBestandsreferenzKunde(zahlung.getBestandsreferenzKunde());
				zahlungV3.setBetrag(mapBetrag(zahlung.getBetrag()));
				zahlungV3.setBuchungscode(mapBuchungscode(zahlung.getBuchungscode()));
				zahlungV3.setEnthalteneBuchungen(zahlung.getEnthalteneBuchungen());
				zahlungV3.setGlaeubigerId(zahlung.getGlaeubigerId());
				zahlungV3.setKontoinformation(mapKontoinformation(zahlung.getBeteiligter()));
				zahlungV3.setMandatsreferenz(zahlung.getMandatsreferenz());
				zahlungV3.setVerwendungszweck(zahlung.getVerwendungszweck());
				zahlungV3.setZahlungsreferenz(zahlung.getZahlungsreferenz());
				zahlungV3.setZusatzinformation(zahlung.getZusatzinformation());
				zahlungV3.setSpesen(mapSpesen(zahlung.getSpesen()));
				
				list.add(zahlungV3);
			}
			return list;
		}

		private List<SepaSpesenV3> mapSpesen(List<SepaSpesen> spesen) {
			List<SepaSpesenV3> spesenV3 = new ArrayList<SepaSpesenV3>();
			for (SepaSpesen entry : spesen) {
				SepaSpesenV3 entryV3 = new SepaSpesenV3();
				entryV3.setBetrag(mapBetrag(entry.getBetrag()));
				entryV3.setInfo(entry.getInfo());
				spesenV3.add(entryV3);
			}
			
			return spesenV3;
		}

		private SepaBankTransactionCodeV3 mapBuchungscode(SepaBankTransactionCode buchungscode) {
			if (buchungscode == null) return null;
			
			SepaBankTransactionCodeV3 codeV3 = new SepaBankTransactionCodeV3();
			codeV3.setCode(buchungscode.getCode());
			codeV3.setFamilie(buchungscode.getFamilie());
			codeV3.setSubFamilie(buchungscode.getSubFamilie());
			codeV3.setProprietaererCode(buchungscode.getProprietaererCode());
			return codeV3;
		}

		private SepaKontoinformationV3 mapKontoinformation(SepaKontoinformation kontoInfo) {
			if (kontoInfo == null) return null;
			
			SepaKontoinformationV3 kontoInfoV3 = new SepaKontoinformationV3();
			kontoInfoV3.setBic(kontoInfo.getBic());
			kontoInfoV3.setIban(kontoInfo.getIban());
			kontoInfoV3.setName(kontoInfo.getName());
			
			return kontoInfoV3;
		}

		private List<SepaSaldoV3> mapSalden(List<SepaSaldo> salden) {
			List<SepaSaldoV3> list = new ArrayList<SepaSaldoV3>();
			if (salden == null) return list;
			
			for (SepaSaldo saldo : salden) {
				SepaSaldoV3 saldoV3 = new SepaSaldoV3();
				saldoV3.setDatum(saldo.getDatum());
				saldoV3.setSaldocode(saldo.getSaldocode().getValue());
				saldoV3.setWaehrung(saldo.getWaehrung());
				saldoV3.setBetrag(mapBetrag(saldo.getBetrag()));
				list.add(saldoV3);
			}
			return list;
		}

		private SepaBetragV3 mapBetrag(SepaBetrag betrag) {
			if (betrag == null) return null;
			
			SepaBetragV3 betragV3 = new SepaBetragV3();
			betragV3.setIsHaben(betrag.isHaben());
			betragV3.setWert(betrag.getWert());
			return betragV3;
		}
		
		public SepaKontoauszug map(SepaKontoauszugV3 version3) {
			SepaKontoauszug kontoauszug = new SepaKontoauszug();
			
			kontoauszug.setAuszugsId(version3.getAuszugId());
			kontoauszug.setAuszugsnr(version3.getAuszugsNr());
			kontoauszug.setTypVersion(getCamtFormat(version3.getCamtVersion()));
			kontoauszug.setElektronischeAuszugsnr(version3.getElektronischeAuszugsNr());
			kontoauszug.setEmpfaenger(version3.getEmpfaenger());
			kontoauszug.setErstellungsDatum(version3.getErstellungsdatum());
			kontoauszug.setbLetzteSeite(version3.getIsLetzteSeite());
			kontoauszug.setMessageId(version3.getMessageId());
			kontoauszug.setSeitennummer(version3.getSeitennummer());
			kontoauszug.setZusatzinformation(version3.getZusatzinformation());
			
			kontoauszug.setSalden(mapSaldenV3(version3.getSalden()));
			kontoauszug.setKontoInfo(mapKontoinformationV3(version3.getKontoinformation()));
			kontoauszug.setBuchungen(mapBuchungenV3(version3.getBuchungen()));
			
			return kontoauszug;
		}

		private List<SepaBuchung> mapBuchungenV3(List<SepaBuchungV3> buchungen) {
			List<SepaBuchung> list = new ArrayList<SepaBuchung>();
			if (buchungen == null) return list;
			
			for (SepaBuchungV3 buchungV3 : buchungen) {
				SepaBuchung buchung = new SepaBuchung();
				buchung.setBankreferenz(buchungV3.getBankreferenz());
				buchung.setBetrag(mapBetragV3(buchungV3.getBetrag()));
				buchung.setBuchungscode(mapBuchungscodeV3(buchungV3.getBuchungscode()));
				buchung.setBuchungsdatum(buchungV3.getBuchungsdatum());
				buchung.setStatus(buchungV3.getStatus());
				buchung.setValutadatum(buchung.getValutadatum());
				buchung.setWaehrung(buchungV3.getWaehrung());
				buchung.setZahlungen(mapZahlungenV3(buchungV3.getZahlungen()));
				buchung.setZusatzinformation(buchungV3.getZusatzinformation());
				list.add(buchung);
			}
			return list;
		}

		private List<SepaZahlung> mapZahlungenV3(List<SepaZahlungV3> zahlungenV3) {
			List<SepaZahlung> zahlungen = new ArrayList<SepaZahlung>();
			if (zahlungenV3 == null) return zahlungen;
			
			for (SepaZahlungV3 zahlungV3 : zahlungenV3) {
				SepaZahlung zahlung = new SepaZahlung();
				zahlung.setAuftraggeberreferenz(zahlungV3.getAuftraggeberreferenz());
				zahlung.setBankreferenz(zahlungV3.getBankreferenz());
				zahlung.setBeteiligter(mapKontoinformationV3(zahlungV3.getKontoinformation()));
				zahlung.setBetrag(mapBetragV3(zahlungV3.getBetrag()));
				zahlung.setBuchungscode(mapBuchungscodeV3(zahlungV3.getBuchungscode()));
				zahlung.setVerwendungszweck(zahlungV3.getVerwendungszweck());
				zahlung.setZahlungsreferenz(zahlungV3.getZahlungsreferenz());
				
				zahlung.setBestandsreferenzKunde(zahlungV3.getBestandsreferenzKunde());
				zahlung.setEnthalteneBuchungen(zahlungV3.getEnthalteneBuchungen());
				zahlung.setGlaeubigerId(zahlungV3.getGlaeubigerId());
				zahlung.setMandatsreferenz(zahlungV3.getMandatsreferenz());
				zahlung.setZusatzinformation(zahlungV3.getZusatzinformation());
				zahlung.getSpesen().addAll(mapSpesenV3(zahlungV3.getSpesen()));
				
				zahlungen.add(zahlung);
				
			}
			return zahlungen;
		}

		private List<SepaSpesen> mapSpesenV3(List<SepaSpesenV3> spesenV3) {
			List<SepaSpesen> spesen = new ArrayList<SepaSpesen>();
			if (spesenV3 == null || spesenV3.isEmpty())
				return spesen;
			
			for (SepaSpesenV3 entryV3 : spesenV3) {
				SepaSpesen entry = new SepaSpesen();
				entry.setBetrag(mapBetragV3(entryV3.getBetrag()));
				entry.setInfo(entryV3.getInfo());
				spesen.add(entry);
			}
			
			return spesen;
		}

		private SepaBankTransactionCode mapBuchungscodeV3(SepaBankTransactionCodeV3 buchungscodeV3) {
			if (buchungscodeV3 == null) return null;
			
			SepaBankTransactionCode buchungscode = new SepaBankTransactionCode();
			buchungscode.setCode(buchungscodeV3.getCode());
			buchungscode.setFamilie(buchungscodeV3.getFamilie());
			buchungscode.setSubFamilie(buchungscodeV3.getSubFamilie());
			buchungscode.setProprietaererCode(buchungscodeV3.getProprietaererCode());
			return buchungscode;
		}

		private SepaKontoinformation mapKontoinformationV3(SepaKontoinformationV3 kontoinfoV3) {
			if (kontoinfoV3 == null) return null;
			
			SepaKontoinformation kontoinfo = new SepaKontoinformation();
			kontoinfo.setBic(kontoinfoV3.getBic());
			kontoinfo.setIban(kontoinfoV3.getIban());
			kontoinfo.setName(kontoinfoV3.getName());
			
			return kontoinfo;
		}

		private List<SepaSaldo> mapSaldenV3(List<SepaSaldoV3> salden) {
			List<SepaSaldo> list = new ArrayList<SepaSaldo>();
			if (salden == null) return list;
			
			for (SepaSaldoV3 saldoV3 : salden) {
				SepaSaldo saldo = new SepaSaldo();
				saldo.setDatum(saldoV3.getDatum());
				saldo.setSaldocode(SepaSaldoCodeEnum.fromString(saldoV3.getSaldocode()));
				saldo.setWaehrung(saldoV3.getWaehrung());
				saldo.setBetrag(mapBetragV3(saldoV3.getBetrag()));
				
				list.add(saldo);
			}
			
			return list;
		}

		private SepaBetrag mapBetragV3(SepaBetragV3 betragV3) {
			if (betragV3 == null) return null;
			
			SepaBetrag betrag;
			if (betragV3.getIsHaben()) {
				betrag = new SepaHabenBetrag(betragV3.getWert());
			} else {
				betrag = new SepaSollBetrag(betragV3.getWert());
			}
			return betrag;
		}
	}
	
	private class SepaKontoauszugV2Mapper {
		
		public SepaKontoauszugV2 map(SepaKontoauszug source) {
			SepaKontoauszugV2 version2 = new SepaKontoauszugV2();
			
			version2.setAuszugId(source.getAuszugsId());
			version2.setAuszugsNr(source.getAuszugsnr());
			version2.setCamtVersion(source.getTypVersion() != null ? source.getTypVersion().toString() : null);
			version2.setElektronischeAuszugsNr(source.getElektronischeAuszugsnr());
			version2.setEmpfaenger(source.getEmpfaenger());
			version2.setErstellungsdatum(source.getErstellungsDatum());
			version2.setIsLetzteSeite(source.getbLetzteSeite());
			version2.setMessageId(source.getMessageId());
			version2.setSeitennummer(source.getSeitennummer());
			version2.setVersion(SepaKontoauszugVersionEnum.ZWEI);

			version2.setKontoinformation(mapKontoinformation(source.getKontoInfo()));
			version2.setBuchungen(mapBuchungen(source.getBuchungen()));
			version2.setSalden(mapSalden(source.getSalden()));
			
			return version2;
		}

		private List<SepaBuchungV2> mapBuchungen(List<SepaBuchung> buchungen) {
			List<SepaBuchungV2> list = new ArrayList<SepaBuchungV2>();
			if (buchungen == null) return list;
			
			for (SepaBuchung buchung : buchungen) {
				SepaBuchungV2 buchungV2 = new SepaBuchungV2();
				buchungV2.setBankreferenz(buchung.getBankreferenz());
				buchungV2.setBetrag(mapBetrag(buchung.getBetrag()));
				buchungV2.setBuchungscode(mapBuchungscode(buchung.getBuchungscode()));
				buchungV2.setBuchungsdatum(buchung.getBuchungsdatum());
				buchungV2.setStatus(buchung.getStatus());
				buchungV2.setValutadatum(buchung.getValutadatum());
				buchungV2.setWaehrung(buchung.getWaehrung());
				buchungV2.setZahlungen(mapZahlungen(buchung.getZahlungen()));
				
				list.add(buchungV2);
			}
			
			return list;
		}

		private List<SepaZahlungV2> mapZahlungen(List<SepaZahlung> zahlungen) {
			List<SepaZahlungV2> list = new ArrayList<SepaZahlungV2>();
			if (zahlungen == null) return list;
			
			for (SepaZahlung zahlung : zahlungen) {
				SepaZahlungV2 zahlungV2 = new SepaZahlungV2();
				zahlungV2.setAuftraggeberreferenz(zahlung.getAuftraggeberreferenz());
				zahlungV2.setBankreferenz(zahlung.getBankreferenz());
				zahlungV2.setBestandsreferenzKunde(zahlung.getBestandsreferenzKunde());
				zahlungV2.setBetrag(mapBetrag(zahlung.getBetrag()));
				zahlungV2.setBuchungscode(mapBuchungscode(zahlung.getBuchungscode()));
				zahlungV2.setEnthalteneBuchungen(zahlung.getEnthalteneBuchungen());
				zahlungV2.setGlaeubigerId(zahlung.getGlaeubigerId());
				zahlungV2.setKontoinformation(mapKontoinformation(zahlung.getBeteiligter()));
				zahlungV2.setMandatsreferenz(zahlung.getMandatsreferenz());
				zahlungV2.setVerwendungszweck(zahlung.getVerwendungszweck());
				zahlungV2.setZahlungsreferenz(zahlung.getZahlungsreferenz());
				
				list.add(zahlungV2);
			}
			return list;
		}

		private SepaBankTransactionCodeV2 mapBuchungscode(SepaBankTransactionCode buchungscode) {
			if (buchungscode == null) return null;
			
			SepaBankTransactionCodeV2 codeV2 = new SepaBankTransactionCodeV2();
			codeV2.setCode(buchungscode.getCode());
			codeV2.setFamilie(buchungscode.getFamilie());
			codeV2.setSubFamilie(buchungscode.getSubFamilie());
			return codeV2;
		}

		private SepaKontoinformationV2 mapKontoinformation(SepaKontoinformation kontoInfo) {
			if (kontoInfo == null) return null;
			
			SepaKontoinformationV2 kontoInfoV2 = new SepaKontoinformationV2();
			kontoInfoV2.setBic(kontoInfo.getBic());
			kontoInfoV2.setIban(kontoInfo.getIban());
			kontoInfoV2.setName(kontoInfo.getName());
			
			return kontoInfoV2;
		}

		private List<SepaSaldoV2> mapSalden(List<SepaSaldo> salden) {
			List<SepaSaldoV2> list = new ArrayList<SepaSaldoV2>();
			if (salden == null) return list;
			
			for (SepaSaldo saldo : salden) {
				SepaSaldoV2 saldoV2 = new SepaSaldoV2();
				saldoV2.setDatum(saldo.getDatum());
				saldoV2.setSaldocode(saldo.getSaldocode().getValue());
				saldoV2.setWaehrung(saldo.getWaehrung());
				saldoV2.setBetrag(mapBetrag(saldo.getBetrag()));
				list.add(saldoV2);
			}
			return list;
		}

		private SepaBetragV2 mapBetrag(SepaBetrag betrag) {
			if (betrag == null) return null;
			
			SepaBetragV2 betragV2 = new SepaBetragV2();
			betragV2.setIsHaben(betrag.isHaben());
			betragV2.setWert(betrag.getWert());
			return betragV2;
		}
		
		public SepaKontoauszug map(SepaKontoauszugV2 version2) {
			SepaKontoauszug kontoauszug = new SepaKontoauszug();
			
			kontoauszug.setAuszugsId(version2.getAuszugId());
			kontoauszug.setAuszugsnr(version2.getAuszugsNr());
			kontoauszug.setTypVersion(getCamtFormat(version2.getCamtVersion()));
			kontoauszug.setElektronischeAuszugsnr(version2.getElektronischeAuszugsNr());
			kontoauszug.setEmpfaenger(version2.getEmpfaenger());
			kontoauszug.setErstellungsDatum(version2.getErstellungsdatum());
			kontoauszug.setbLetzteSeite(version2.getIsLetzteSeite());
			kontoauszug.setMessageId(version2.getMessageId());
			kontoauszug.setSeitennummer(version2.getSeitennummer());
			
			kontoauszug.setSalden(mapSaldenV2(version2.getSalden()));
			kontoauszug.setKontoInfo(mapKontoinformationV2(version2.getKontoinformation()));
			kontoauszug.setBuchungen(mapBuchungenV2(version2.getBuchungen()));
			
			return kontoauszug;
		}

		private List<SepaBuchung> mapBuchungenV2(List<SepaBuchungV2> buchungen) {
			List<SepaBuchung> list = new ArrayList<SepaBuchung>();
			if (buchungen == null) return list;
			
			for (SepaBuchungV2 buchungV2 : buchungen) {
				SepaBuchung buchung = new SepaBuchung();
				buchung.setBankreferenz(buchungV2.getBankreferenz());
				buchung.setBetrag(mapBetragV2(buchungV2.getBetrag()));
				buchung.setBuchungscode(mapBuchungscodeV2(buchungV2.getBuchungscode()));
				buchung.setBuchungsdatum(buchungV2.getBuchungsdatum());
				buchung.setStatus(buchungV2.getStatus());
				buchung.setValutadatum(buchung.getValutadatum());
				buchung.setWaehrung(buchungV2.getWaehrung());
				buchung.setZahlungen(mapZahlungenV2(buchungV2.getZahlungen()));
				list.add(buchung);
			}
			return list;
		}

		private List<SepaZahlung> mapZahlungenV2(List<SepaZahlungV2> zahlungenV2) {
			List<SepaZahlung> zahlungen = new ArrayList<SepaZahlung>();
			if (zahlungenV2 == null) return zahlungen;
			
			for (SepaZahlungV2 zahlungV2 : zahlungenV2) {
				SepaZahlung zahlung = new SepaZahlung();
				zahlung.setAuftraggeberreferenz(zahlungV2.getAuftraggeberreferenz());
				zahlung.setBankreferenz(zahlungV2.getBankreferenz());
				zahlung.setBeteiligter(mapKontoinformationV2(zahlungV2.getKontoinformation()));
				zahlung.setBetrag(mapBetragV2(zahlungV2.getBetrag()));
				zahlung.setBuchungscode(mapBuchungscodeV2(zahlungV2.getBuchungscode()));
				zahlung.setVerwendungszweck(zahlungV2.getVerwendungszweck());
				zahlung.setZahlungsreferenz(zahlungV2.getZahlungsreferenz());
				
				zahlung.setBestandsreferenzKunde(zahlungV2.getBestandsreferenzKunde());
				zahlung.setEnthalteneBuchungen(zahlungV2.getEnthalteneBuchungen());
				zahlung.setGlaeubigerId(zahlungV2.getGlaeubigerId());
				zahlung.setMandatsreferenz(zahlungV2.getMandatsreferenz());
				
				zahlungen.add(zahlung);
				
			}
			return zahlungen;
		}

		private SepaBankTransactionCode mapBuchungscodeV2(SepaBankTransactionCodeV2 buchungscodeV2) {
			if (buchungscodeV2 == null) return null;
			
			SepaBankTransactionCode buchungscode = new SepaBankTransactionCode();
			buchungscode.setCode(buchungscodeV2.getCode());
			buchungscode.setFamilie(buchungscodeV2.getFamilie());
			buchungscode.setSubFamilie(buchungscodeV2.getSubFamilie());
			return buchungscode;
		}

		private SepaKontoinformation mapKontoinformationV2(SepaKontoinformationV2 kontoinfoV2) {
			if (kontoinfoV2 == null) return null;
			
			SepaKontoinformation kontoinfo = new SepaKontoinformation();
			kontoinfo.setBic(kontoinfoV2.getBic());
			kontoinfo.setIban(kontoinfoV2.getIban());
			kontoinfo.setName(kontoinfoV2.getName());
			
			return kontoinfo;
		}

		private List<SepaSaldo> mapSaldenV2(List<SepaSaldoV2> salden) {
			List<SepaSaldo> list = new ArrayList<SepaSaldo>();
			if (salden == null) return list;
			
			for (SepaSaldoV2 saldoV2 : salden) {
				SepaSaldo saldo = new SepaSaldo();
				saldo.setDatum(saldoV2.getDatum());
				saldo.setSaldocode(SepaSaldoCodeEnum.fromString(saldoV2.getSaldocode()));
				saldo.setWaehrung(saldoV2.getWaehrung());
				saldo.setBetrag(mapBetragV2(saldoV2.getBetrag()));
				
				list.add(saldo);
			}
			
			return list;
		}

		private SepaBetrag mapBetragV2(SepaBetragV2 betragV2) {
			if (betragV2 == null) return null;
			
			SepaBetrag betrag;
			if (betragV2.getIsHaben()) {
				betrag = new SepaHabenBetrag(betragV2.getWert());
			} else {
				betrag = new SepaSollBetrag(betragV2.getWert());
			}
			return betrag;
		}
	}
}
