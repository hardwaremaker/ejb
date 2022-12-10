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
package com.lp.server.finanz.bl.sepa;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import com.lp.server.finanz.service.SepaBankTransactionCode;
import com.lp.server.finanz.service.SepaBetrag;
import com.lp.server.finanz.service.SepaBuchung;
import com.lp.server.finanz.service.SepaKontoauszug;
import com.lp.server.finanz.service.SepaKontoinformation;
import com.lp.server.finanz.service.SepaSaldo;
import com.lp.server.finanz.service.SepaSaldoCodeEnum;
import com.lp.server.finanz.service.SepaZahlung;
import com.lp.server.schema.iso20022.camt052V02.CA052AccountReport11;
import com.lp.server.schema.iso20022.camt052V02.CA052AmountAndCurrencyExchange3;
import com.lp.server.schema.iso20022.camt052V02.CA052BankTransactionCodeStructure4;
import com.lp.server.schema.iso20022.camt052V02.CA052CashAccount20;
import com.lp.server.schema.iso20022.camt052V02.CA052CashBalance3;
import com.lp.server.schema.iso20022.camt052V02.CA052DateAndDateTimeChoice;
import com.lp.server.schema.iso20022.camt052V02.CA052Document;
import com.lp.server.schema.iso20022.camt052V02.CA052EntryDetails1;
import com.lp.server.schema.iso20022.camt052V02.CA052EntryTransaction2;
import com.lp.server.schema.iso20022.camt052V02.CA052GroupHeader42;
import com.lp.server.schema.iso20022.camt052V02.CA052ReportEntry2;
import com.lp.util.EJBExceptionLP;
import com.lp.util.EJBSepaImportExceptionLP;
import com.lp.util.FieldInstantiator;
import com.lp.util.SepaXMLFieldInstantiator;

public class SepaImportTransformerCamt052V02 extends SepaImportTransformer {

	CA052Document xmlDocument;
	
	public SepaImportTransformerCamt052V02(String lkz) {
		super(lkz);
	}

	@Override
	protected void setSepaDocument(Object sepaDoc) {
		xmlDocument = (CA052Document) sepaDoc;
		FieldInstantiator<CA052Document> instantiator = new SepaXMLFieldInstantiator<CA052Document>();
		
		try {
			xmlDocument = instantiator.instantiateNullFields(xmlDocument);
		} catch (Exception e) {
			throw new EJBSepaImportExceptionLP(camtFormat.toString(), lkz,
					EJBExceptionLP.FEHLER_SEPAIMPORT_INSTANZIERUNG_DER_NULL_OBJEKTE, 
					EJBSepaImportExceptionLP.SEVERITY_ERROR, new EJBExceptionLP(e));
		}
	}

	@Override
	protected List<SepaKontoauszug> transformImpl() {
		List<SepaKontoauszug> kontoauszuege = new ArrayList<SepaKontoauszug>();
		List<CA052AccountReport11> reportList = xmlDocument.getBkToCstmrAcctRpt().getRpt();
		
		for (CA052AccountReport11 report : reportList) {
			SepaKontoauszug ktoauszug = new SepaKontoauszug();
			ktoauszug.setTypVersion(camtFormat);
			CA052GroupHeader42 groupHeader = xmlDocument.getBkToCstmrAcctRpt().getGrpHdr();
			ktoauszug.setMessageId(groupHeader.getMsgId());
			String pgNb = groupHeader.getMsgPgntn().getPgNb();
			ktoauszug.setSeitennummer(pgNb != null ? Integer.parseInt(pgNb) : null);
			ktoauszug.setbLetzteSeite(groupHeader.getMsgPgntn().isLastPgInd());
			transformCamt(ktoauszug, report);
		
			kontoauszuege.add(ktoauszug);
		}
		
		return kontoauszuege;
	}

	private SepaKontoauszug transformCamt(SepaKontoauszug kontoreport, CA052AccountReport11 report) {
		kontoreport.setAuszugsId(report.getId());
//		kontoreport.setElektronischeAuszugsnr(report.getElctrncSeqNb());
//		kontoreport.setAuszugsnr(report.getLglSeqNb());
		kontoreport.setKontoInfo(extractKontoInformation(report.getAcct()));
		kontoreport.setErstellungsDatum(extractDate(report.getCreDtTm()));
		setAuszugsnummern(kontoreport, report);
		kontoreport.setSalden(extractSalden(report.getBal()));
		kontoreport.setBuchungen(extractBuchungen(report.getNtry()));
		kontoreport.setZusatzinformation(report.getAddtlRptInf());
		
		return kontoreport;
	}
	
	private void setAuszugsnummern(SepaKontoauszug kontoreport,	CA052AccountReport11 report) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String sCreationDate = dateFormat.format(kontoreport.getErstellungsDatum());
		BigDecimal auszugsnummer = new BigDecimal(sCreationDate);
		if (kontoreport.getKontoInfo().getIban().startsWith("AT")) {
			kontoreport.setElektronischeAuszugsnr(report.getElctrncSeqNb());
			kontoreport.setAuszugsnr(auszugsnummer);
		} else {
			kontoreport.setElektronischeAuszugsnr(auszugsnummer);
			kontoreport.setAuszugsnr(report.getLglSeqNb());
		}
	}

	private List<SepaBuchung> extractBuchungen(List<CA052ReportEntry2> bookEntries) {
		List<SepaBuchung> buchungen = new ArrayList<SepaBuchung>();
		
		for (CA052ReportEntry2 bookEntry : bookEntries) {
			SepaBuchung buchung = new SepaBuchung();
			
			buchung.setBetrag(createSepaBetrag(bookEntry.getAmt().getValue(), 
					bookEntry.getCdtDbtInd().name()));
			buchung.setWaehrung(bookEntry.getAmt().getCcy());
			buchung.setBuchungsdatum(extractDate(bookEntry.getValDt()));
			buchung.setValutadatum(extractDate(bookEntry.getValDt()));
			buchung.setBankreferenz(bookEntry.getAcctSvcrRef());
			buchung.setStatus(bookEntry.getSts().name());
			buchung.setZusatzinformation(bookEntry.getAddtlNtryInf());
			buchung.setZahlungen(extractZahlungen(bookEntry.getNtryDtls(), buchung));
			buchung.setBuchungscode(extractBuchungscode(bookEntry.getBkTxCd()));
			
			buchungen.add(buchung);
		}
		
		return buchungen;
	}

	private List<SepaZahlung> extractZahlungen(List<CA052EntryDetails1> ntryDtls, SepaBuchung buchungskopf) {
		if (ntryDtls.isEmpty() && buchungskopf.getBetrag() != null && buchungskopf.getBetrag().getWert().signum() != 0) {
			List<SepaZahlung> zahlungen = new ArrayList<SepaZahlung>();
			SepaZahlung sepaZahlung = extractEinzelzahlungOhneDetails(buchungskopf);
			zahlungen.add(sepaZahlung);
			return zahlungen;
		}

		for (CA052EntryDetails1 entryDetail : ntryDtls) {
			if (entryDetail.getTxDtls().isEmpty()) {
				return extractSammelzahlung(entryDetail, buchungskopf);
			} else {
				return extractEinzelzahlungen(entryDetail, buchungskopf);
			}
		}
		return new ArrayList<SepaZahlung>();
	}
	
	private SepaZahlung extractEinzelzahlungOhneDetails(SepaBuchung buchungskopf) {
		SepaZahlung zahlung = new SepaZahlung();
		zahlung.setBetrag(buchungskopf.getBetrag());
		zahlung.setEnthalteneBuchungen(1);
		zahlung.setZusatzinformation(buchungskopf.getZusatzinformation());
		buchungskopf.setZusatzinformation(null);
		
		return zahlung;
	}

	private List<SepaZahlung> extractEinzelzahlungen(
			CA052EntryDetails1 entryDetail, SepaBuchung buchungskopf) {
		List<SepaZahlung> zahlungen = new ArrayList<SepaZahlung>();

		for (CA052EntryTransaction2 txDetail : entryDetail.getTxDtls()) {
			SepaZahlung zahlung = new SepaZahlung();
			
			zahlung.setEnthalteneBuchungen(1);
			zahlung.setBetrag(extractSepaBetrag(buchungskopf, txDetail.getAmtDtls()));
			zahlung.setBeteiligter(extractZahlungsbeteiligter(txDetail, buchungskopf));
			zahlung.setAuftraggeberreferenz(txDetail.getRefs().getEndToEndId());
			zahlung.setBankreferenz(txDetail.getRefs().getAcctSvcrRef());
			zahlung.setMandatsreferenz(txDetail.getRefs().getMndtId());
			zahlung.setBuchungscode(extractBuchungscode(txDetail.getBkTxCd()));
			zahlung.setVerwendungszweck(concatListOfStrings(txDetail.getRmtInf().getUstrd()));
			zahlung.setZahlungsreferenz(txDetail.getRmtInf().getStrd().size() > 0 ?
					txDetail.getRmtInf().getStrd().get(0).getCdtrRefInf().getRef() : null);
			zahlung.setZusatzinformation(txDetail.getAddtlTxInf());
			
			if(buchungskopf.isHabenBuchung()) {
				if (!txDetail.getRltdPties().getCdtr().getId().getPrvtId().getOthr().isEmpty()) {
					zahlung.setGlaeubigerId(txDetail.getRltdPties().getCdtr().getId().getPrvtId().getOthr().get(0).getId());
				}
			}
			zahlungen.add(zahlung);
		}
		return zahlungen;
	}

	private List<SepaZahlung> extractSammelzahlung(
			CA052EntryDetails1 entryDetail, SepaBuchung buchungskopf) {
		List<SepaZahlung> zahlungen = new ArrayList<SepaZahlung>();
		
		SepaZahlung zahlung = new SepaZahlung();
		zahlung.setBetrag(buchungskopf.getBetrag());
		zahlung.setEnthalteneBuchungen(Integer.parseInt(entryDetail.getBtch().getNbOfTxs()));
		zahlungen.add(zahlung);
		
		return zahlungen;
	}

	private SepaKontoinformation extractZahlungsbeteiligter(
			CA052EntryTransaction2 txDetail, SepaBuchung buchungskopf) {
		SepaKontoinformation ktoInfo = new SepaKontoinformation();
		
		if (buchungskopf.isSollBuchung()) {
			ktoInfo.setName(txDetail.getRltdPties().getDbtr().getNm());
			ktoInfo.setIban(txDetail.getRltdPties().getDbtrAcct().getId().getIBAN());
			ktoInfo.setBic(txDetail.getRltdAgts().getDbtrAgt().getFinInstnId().getBIC());
		} else if (buchungskopf.isHabenBuchung()) {
			ktoInfo.setName(txDetail.getRltdPties().getCdtr().getNm());
			ktoInfo.setIban(txDetail.getRltdPties().getCdtrAcct().getId().getIBAN());
			ktoInfo.setBic(txDetail.getRltdAgts().getCdtrAgt().getFinInstnId().getBIC());
		}
		
		return ktoInfo;
	}

	private SepaBetrag extractSepaBetrag(SepaBuchung buchungskopf,
			CA052AmountAndCurrencyExchange3 amtDtls) {

		if (amtDtls.getInstdAmt().getAmt().getValue() != null) {
			return createSepaBetrag(amtDtls.getInstdAmt().getAmt().getValue(), 
				buchungskopf.isHabenBuchung() ? SepaKontoauszug.DEBITOR :
					(buchungskopf.isSollBuchung() ? SepaKontoauszug.CREDITOR : ""));
		}

		if (amtDtls.getTxAmt().getAmt().getValue() != null) {
			return createSepaBetrag(amtDtls.getInstdAmt().getAmt().getValue(), 
				buchungskopf.isHabenBuchung() ? SepaKontoauszug.DEBITOR :
					(buchungskopf.isSollBuchung() ? SepaKontoauszug.DEBITOR : ""));
		}

		return null;
	}

	private List<SepaSaldo> extractSalden(List<CA052CashBalance3> balList) {
		List<SepaSaldo> salden = new ArrayList<SepaSaldo>();
		
		for (CA052CashBalance3 balance : balList) {
			SepaSaldo saldo = new SepaSaldo();
			
			saldo.setSaldocode(SepaSaldoCodeEnum.fromString(balance.getTp().getCdOrPrtry().getCd().name()));
			saldo.setBetrag(createSepaBetrag(balance.getAmt().getValue(), balance.getCdtDbtInd().name()));
			saldo.setWaehrung(balance.getAmt().getCcy());
			saldo.setDatum(extractDate(balance.getDt()));
			
			salden.add(saldo);
		}
		
		return salden;
	}

	private SepaKontoinformation extractKontoInformation(CA052CashAccount20 acct) {
		SepaKontoinformation ktoInfo = new SepaKontoinformation();
		
		ktoInfo.setName(acct.getOwnr().getNm());
		ktoInfo.setIban(acct.getId().getIBAN());
		ktoInfo.setBic(acct.getSvcr().getFinInstnId().getBIC());
		
		return ktoInfo;
	}

	private SepaBankTransactionCode extractBuchungscode(
			CA052BankTransactionCodeStructure4 bkTxCd) {
		SepaBankTransactionCode buchungscode = new SepaBankTransactionCode();
		
		buchungscode.setCode(bkTxCd.getDomn().getCd() != null ? 
				bkTxCd.getDomn().getCd() : bkTxCd.getPrtry().getCd());
		buchungscode.setFamilie(bkTxCd.getDomn().getFmly().getCd());
		buchungscode.setSubFamilie(bkTxCd.getDomn().getFmly().getSubFmlyCd());
		
		return buchungscode;
	}

	private Date extractDate(CA052DateAndDateTimeChoice valDt) {
		XMLGregorianCalendar xmlCalendar = valDt.getDt() != null ? 
				valDt.getDt() : valDt.getDtTm();
				
		return extractDate(xmlCalendar);
	}


	@Override
	public void validateMsgRecipient() throws EJBSepaImportExceptionLP {
		// TODO Auto-generated method stub

	}

	@Override
	public void validateElectronicSeqNumber() throws EJBSepaImportExceptionLP {
		// TODO Auto-generated method stub

	}

	@Override
	public void validateLegalSeqNumber() throws EJBSepaImportExceptionLP {
		// TODO Auto-generated method stub

	}

	@Override
	public void validateAccountServicer() throws EJBSepaImportExceptionLP {
		// TODO Auto-generated method stub

	}

	@Override
	public void validateEntryBankTransactionCode()
			throws EJBSepaImportExceptionLP {
		// TODO Auto-generated method stub

	}

	@Override
	public void validateTransactionDtlsBankTransactionCode()
			throws EJBSepaImportExceptionLP {
		// TODO Auto-generated method stub

	}

}
