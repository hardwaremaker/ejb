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
import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang.StringUtils;

import com.lp.server.finanz.service.SepaBankTransactionCode;
import com.lp.server.finanz.service.SepaBetrag;
import com.lp.server.finanz.service.SepaBuchung;
import com.lp.server.finanz.service.SepaKontoauszug;
import com.lp.server.finanz.service.SepaKontoinformation;
import com.lp.server.finanz.service.SepaSaldo;
import com.lp.server.finanz.service.SepaSaldoCodeEnum;
import com.lp.server.finanz.service.SepaSpesen;
import com.lp.server.finanz.service.SepaZahlung;
import com.lp.server.schema.iso20022.camt053V02.CAAccountStatement2;
import com.lp.server.schema.iso20022.camt053V02.CABankTransactionCodeStructure4;
import com.lp.server.schema.iso20022.camt053V02.CACashAccount20;
import com.lp.server.schema.iso20022.camt053V02.CACashBalance3;
import com.lp.server.schema.iso20022.camt053V02.CAChargesInformation6;
import com.lp.server.schema.iso20022.camt053V02.CACreditDebitCode;
import com.lp.server.schema.iso20022.camt053V02.CADateAndDateTimeChoice;
import com.lp.server.schema.iso20022.camt053V02.CADocument;
import com.lp.server.schema.iso20022.camt053V02.CAEntryDetails1;
import com.lp.server.schema.iso20022.camt053V02.CAEntryTransaction2;
import com.lp.server.schema.iso20022.camt053V02.CAPartyIdentification32;
import com.lp.server.schema.iso20022.camt053V02.CAReportEntry2;
import com.lp.util.EJBExceptionLP;
import com.lp.util.EJBSepaImportCountryExceptionLP;
import com.lp.util.EJBSepaImportExceptionLP;
import com.lp.util.FieldInstantiator;
import com.lp.util.Helper;
import com.lp.util.SepaXMLFieldInstantiator;

public class SepaImportTransformerCamt053V02 extends SepaImportTransformer {

	private CADocument xmlDocument;
	
	public SepaImportTransformerCamt053V02(String lkz) {
		super(lkz);
	}

	protected void setSepaDocument(Object sepaDoc) {
		xmlDocument = (CADocument) sepaDoc;
		FieldInstantiator<CADocument> instantiator = new SepaXMLFieldInstantiator<CADocument>();
		
		try {
			xmlDocument = instantiator.instantiateNullFields(xmlDocument);
		} catch (Exception e) {
			throw new EJBSepaImportExceptionLP(camtFormat.toString(), lkz, EJBExceptionLP.FEHLER_SEPAIMPORT_INSTANZIERUNG_DER_NULL_OBJEKTE, 
					EJBSepaImportExceptionLP.SEVERITY_ERROR, new EJBExceptionLP(e));
		}
	}

	@Override
	protected List<SepaKontoauszug> transformImpl() {
		List<SepaKontoauszug> kontoauszuege = new ArrayList<SepaKontoauszug>();
		List<CAAccountStatement2> stmtList = xmlDocument.getBkToCstmrStmt().getStmt();
		
		for (CAAccountStatement2 stmt : stmtList) {
			SepaKontoauszug ktoauszug = transformCamt(stmt);
			ktoauszug.setTypVersion(camtFormat);
			ktoauszug.setMessageId(xmlDocument.getBkToCstmrStmt().getGrpHdr().getMsgId());
			String pgNb = xmlDocument.getBkToCstmrStmt().getGrpHdr().getMsgPgntn().getPgNb();
			ktoauszug.setSeitennummer(pgNb != null ? Integer.parseInt(pgNb) : null);
			ktoauszug.setbLetzteSeite(xmlDocument.getBkToCstmrStmt().getGrpHdr().getMsgPgntn().isLastPgInd());
			ktoauszug.setZusatzinformation(stmt.getAddtlStmtInf());
			kontoauszuege.add(ktoauszug);
		}
		return kontoauszuege;
	}

	/**
	 * Extrahiert einen XML-Kontoauszug der Version 02
	 * 
	 * @param stmt Wurzelobjekt des XML-Kontoauszugs
	 * @return den transformierten Kontoauszug
	 */
	protected SepaKontoauszug transformCamt(CAAccountStatement2 stmt) {
		SepaKontoauszug kontoauszug = new SepaKontoauszug();
		kontoauszug.setAuszugsId(stmt.getId());
		kontoauszug.setElektronischeAuszugsnr(stmt.getElctrncSeqNb());
		kontoauszug.setAuszugsnr(stmt.getLglSeqNb());
		kontoauszug.setErstellungsDatum(extractDate(stmt.getCreDtTm()));
		kontoauszug.setKontoInfo(extractKontoInformation(stmt.getAcct()));
		kontoauszug.setSalden(extractSalden(stmt.getBal()));
		kontoauszug.setBuchungen(extractBuchungen(stmt.getNtry()));
		
		return kontoauszug;
	}

	protected List<SepaBuchung> extractBuchungen(List<CAReportEntry2> xmlBookEntries) {
		List<SepaBuchung> buchungen = new ArrayList<SepaBuchung>();
		
		for (CAReportEntry2 xmlBookEntry : xmlBookEntries) {
			SepaBuchung buchung = new SepaBuchung();
			
			buchung.setBetrag(createSepaBetrag(xmlBookEntry.getAmt().getValue(), 
					xmlBookEntry.getCdtDbtInd().name()));
			buchung.setWaehrung(xmlBookEntry.getAmt().getCcy());
			buchung.setBuchungsdatum(extractDate(xmlBookEntry.getBookgDt()));
			buchung.setValutadatum(extractDate(xmlBookEntry.getValDt()));
			buchung.setBankreferenz(xmlBookEntry.getAcctSvcrRef());
			buchung.setStatus(xmlBookEntry.getSts().name());
			buchung.setZusatzinformation(xmlBookEntry.getAddtlNtryInf());
			buchung.setZahlungen(extractZahlungen(xmlBookEntry.getNtryDtls(), buchung));
			buchung.setBuchungscode(extractBuchungscode(xmlBookEntry.getBkTxCd()));
			
			buchungen.add(buchung);
		}
		
		return buchungen;
	}

	/**
	 * Extrahiert die Zahlungen einer Buchung aus den XML-Klassen
	 * 
	 * @param xmlBookEntryDetails 
	 * @return Liste aller Sepa-Zahlungen
	 */
	protected List<SepaZahlung> extractZahlungen(List<CAEntryDetails1> xmlBookEntryDetails, SepaBuchung buchungskopf) {
		if (xmlBookEntryDetails.isEmpty() && buchungskopf.getBetrag() != null && buchungskopf.getBetrag().getWert().signum() != 0) {
			List<SepaZahlung> zahlungen = new ArrayList<SepaZahlung>();
			SepaZahlung sepaZahlung = extractEinzelbuchungOhneDetails(buchungskopf);
			zahlungen.add(sepaZahlung);
			return zahlungen;
		}
		
		for (CAEntryDetails1 xmlDetail : xmlBookEntryDetails) {
			if (xmlDetail.getTxDtls().isEmpty()) {
				return extractSammelbuchung(xmlDetail, buchungskopf);
			} else {
				return extractEinzelbuchungen(xmlDetail, buchungskopf);
			}
		}
		
		return new ArrayList<SepaZahlung>();
	}
	
	private SepaZahlung extractEinzelbuchungOhneDetails(SepaBuchung buchungskopf) {
		SepaZahlung zahlung = new SepaZahlung();
		zahlung.setBetrag(buchungskopf.getBetrag());
		zahlung.setEnthalteneBuchungen(1);
		zahlung.setZusatzinformation(buchungskopf.getZusatzinformation());
		buchungskopf.setZusatzinformation(null);
		
		return zahlung;
	}

	private List<SepaZahlung> extractEinzelbuchungen(CAEntryDetails1 xmlDetail,
			SepaBuchung buchungskopf) {
		List<SepaZahlung> zahlungen = new ArrayList<SepaZahlung>();

		for (CAEntryTransaction2 xmlTxDetail : xmlDetail.getTxDtls()) {
			SepaZahlung zahlung = new SepaZahlung();
			zahlung.setEnthalteneBuchungen(1);
			setupZahlungBetragSpesen(buchungskopf, zahlung, xmlTxDetail);
			zahlung.setBeteiligter(extractZahlungsbeteiligter(xmlTxDetail, buchungskopf));
			zahlung.setAuftraggeberreferenz(xmlTxDetail.getRefs().getEndToEndId());
			zahlung.setBankreferenz(xmlTxDetail.getRefs().getAcctSvcrRef());
			zahlung.setMandatsreferenz(xmlTxDetail.getRefs().getMndtId());
			zahlung.setBuchungscode(extractBuchungscode(xmlTxDetail.getBkTxCd()));
			zahlung.setVerwendungszweck(concatListOfStrings(xmlTxDetail.getRmtInf().getUstrd()));
			zahlung.setZahlungsreferenz(xmlTxDetail.getRmtInf().getStrd().size() > 0 ? 
						xmlTxDetail.getRmtInf().getStrd().get(0).getCdtrRefInf().getRef() : null);
			zahlung.setZusatzinformation(xmlTxDetail.getAddtlTxInf());
			
			if(buchungskopf.isHabenBuchung()) {
				if (!xmlTxDetail.getRltdPties().getCdtr().getId().getPrvtId().getOthr().isEmpty()) {
					zahlung.setGlaeubigerId(xmlTxDetail.getRltdPties().getCdtr().getId().getPrvtId().getOthr().get(0).getId());
				}
			}
			zahlungen.add(zahlung);
		}
		
		return zahlungen;
	}

	private List<SepaSpesen> extractSpesen(CAEntryTransaction2 xmlTxDetail, SepaBuchung buchungskopf) {
		List<SepaSpesen> spesen = new ArrayList<SepaSpesen>();
		
		for (CAChargesInformation6 charge : xmlTxDetail.getChrgs()) {
			SepaSpesen entry = new SepaSpesen();
			CACreditDebitCode cdCode = charge.getCdtDbtInd();
			if (cdCode != null) {
				entry.setBetrag(createSepaBetrag(charge.getAmt().getValue(), cdCode.name()));
			} else {
				entry.setBetrag(createSepaBetrag(charge.getAmt().getValue(), 
						buchungskopf.isHabenBuchung() ? SepaKontoauszug.DEBITOR :
						(buchungskopf.isSollBuchung() ? SepaKontoauszug.CREDITOR : "")));
			}
			entry.setInfo(charge.getTp().getPrtry().getId());
			spesen.add(entry);
		}
		return spesen;
	}

	private List<SepaZahlung> extractSammelbuchung(CAEntryDetails1 xmlDetail,
			SepaBuchung buchungskopf) {
		List<SepaZahlung> zahlungen = new ArrayList<SepaZahlung>();
		
		SepaZahlung zahlung = new SepaZahlung();
		zahlung.setBetrag(buchungskopf.getBetrag());
		zahlung.setEnthalteneBuchungen(Integer.parseInt(xmlDetail.getBtch().getNbOfTxs()));
		zahlungen.add(zahlung);
		
		return zahlungen;
	}

	/**
	 * Extrahiert den Buchungscode
	 * 
	 * @param bkTxCd
	 * @return Buchungscode
	 */
	protected SepaBankTransactionCode extractBuchungscode(
			CABankTransactionCodeStructure4 bkTxCd) {

		SepaBankTransactionCode buchungscode = new SepaBankTransactionCode();
		
		buchungscode.setCode(bkTxCd.getDomn().getCd() != null ? bkTxCd.getDomn().getCd() : bkTxCd.getPrtry().getCd());
		buchungscode.setFamilie(bkTxCd.getDomn().getFmly().getCd());
		buchungscode.setSubFamilie(bkTxCd.getDomn().getFmly().getSubFmlyCd());
		
		return buchungscode;
	}

	private void setupZahlungBetragSpesen(SepaBuchung buchungskopf, SepaZahlung zahlung,
			CAEntryTransaction2 txDetail) {
		zahlung.getSpesen().addAll(extractSpesen(txDetail, buchungskopf));

		// umgerechneter Auftragsbetrag in Ktowaehrung, OHNE Spesen
		BigDecimal amount = txDetail.getAmtDtls().getCntrValAmt().getAmt().getValue();
		if(amount != null) {
			SepaBetrag sepaBetrag =  createSepaBetrag(amount, 
					buchungskopf.isHabenBuchung() ? SepaKontoauszug.DEBITOR : 
						(buchungskopf.isSollBuchung() ? SepaKontoauszug.CREDITOR : ""));
			zahlung.setBetrag(addSpesen(sepaBetrag, zahlung.getSpesen()));
			return;
		}

		// Buchungsbetrag in Ktowaehrung, MIT Spesen
		amount = txDetail.getAmtDtls().getTxAmt().getAmt().getValue();
		if(amount != null) {
			zahlung.setBetrag(createSepaBetrag(amount, 
					buchungskopf.isHabenBuchung() ? SepaKontoauszug.DEBITOR : 
						(buchungskopf.isSollBuchung() ? SepaKontoauszug.CREDITOR : "")));
			return;
		}
		
		// Auftragsbetrag in Auftragswaehrung, OHNE Spesen
		amount = txDetail.getAmtDtls().getInstdAmt().getAmt().getValue();
		if(amount != null) {
			if (!buchungskopf.getWaehrung().equals(txDetail.getAmtDtls().getInstdAmt().getAmt().getCcy())) {
				throw new EJBSepaImportExceptionLP(
						EJBExceptionLP.FEHLER_SEPAIMPORT_WAEHRUNG_AUFTRAGSBETRAG, 
						EJBSepaImportExceptionLP.SEVERITY_ERROR, buchungskopf.getWaehrung(), txDetail.getAmtDtls().getInstdAmt().getAmt().getCcy());
			}
			SepaBetrag sepaBetrag = createSepaBetrag(amount, 
					buchungskopf.isHabenBuchung() ? SepaKontoauszug.DEBITOR : 
						(buchungskopf.isSollBuchung() ? SepaKontoauszug.CREDITOR : ""));
			zahlung.setBetrag(addSpesen(sepaBetrag, zahlung.getSpesen()));
		}
	}

	protected SepaKontoinformation extractZahlungsbeteiligter(
			CAEntryTransaction2 xmlTxDetail, SepaBuchung buchungskopf) {
		
		SepaKontoinformation ktoInfo = new SepaKontoinformation();
		
		if(buchungskopf.isSollBuchung()) {
			ktoInfo.setName(extractPartyName(xmlTxDetail.getRltdPties().getDbtr()));
			ktoInfo.setIban(xmlTxDetail.getRltdPties().getDbtrAcct().getId().getIBAN());
			ktoInfo.setBic(xmlTxDetail.getRltdAgts().getDbtrAgt().getFinInstnId().getBIC());
		} else if(buchungskopf.isHabenBuchung()) {
			ktoInfo.setName(extractPartyName(xmlTxDetail.getRltdPties().getCdtr()));
			ktoInfo.setIban(xmlTxDetail.getRltdPties().getCdtrAcct().getId().getIBAN());
			ktoInfo.setBic(xmlTxDetail.getRltdAgts().getCdtrAgt().getFinInstnId().getBIC());
		}
		
		return ktoInfo;
	}

	private String extractPartyName(CAPartyIdentification32 party) {
		return !Helper.isStringEmpty(party.getNm())
				? party.getNm()
				: StringUtils.join(party.getPstlAdr().getAdrLine().iterator(), " ");
	}

	/**
	 * Extrahiert die Salden aus den XML-Klassen
	 * 
	 * @param xmlBalances Liste der Salden als XML-Objekte
	 * @return Liste von den verwendeten Salden
	 */
	protected List<SepaSaldo> extractSalden(List<CACashBalance3> xmlBalances) {
		List<SepaSaldo> salden = new ArrayList<SepaSaldo>();
		
		for (CACashBalance3 xmlBalance : xmlBalances) {
			SepaSaldo saldo = new SepaSaldo();
			saldo.setSaldocode(SepaSaldoCodeEnum.fromString(xmlBalance.getTp().getCdOrPrtry().getCd().name()));
			saldo.setBetrag(createSepaBetrag(xmlBalance.getAmt().getValue(),
					xmlBalance.getCdtDbtInd().name()));
			saldo.setWaehrung(xmlBalance.getAmt().getCcy());
			saldo.setDatum(extractDate(xmlBalance.getDt()));

			salden.add(saldo);
		}
		
		return salden;
	}

	/**
	 * Extrahiert die Kontoinformation aus den XML-Klassen
	 * 
	 * @param xmlAccount XML-Objekt
	 * @return SEPA Kontoinformation
	 */
	protected SepaKontoinformation extractKontoInformation(
			CACashAccount20 xmlAccount) {

		SepaKontoinformation ktoInfo = new SepaKontoinformation();
		
		ktoInfo.setName(xmlAccount.getOwnr().getNm());
		ktoInfo.setIban(xmlAccount.getId().getIBAN());
		ktoInfo.setBic(xmlAccount.getSvcr().getFinInstnId().getBIC());
		
		return ktoInfo;
	}
	
	/**
	 * Extrahiert das Datum aus der XML-Klasse {@link CADateAndDateTimeChoice} 
	 * und wandelt es in ein {@link Date} um.
	 * 
	 * @param xmlDtAndDtmChoice
	 * @return extrahiertes Datum
	 */
	protected Date extractDate(CADateAndDateTimeChoice xmlDtAndDtmChoice) {
		XMLGregorianCalendar xmlCalendar = xmlDtAndDtmChoice.getDt() != null ? 
				xmlDtAndDtmChoice.getDt() : xmlDtAndDtmChoice.getDtTm();
				
		return extractDate(xmlCalendar);
	}
	
	@Override
	public void validateMsgRecipient() throws EJBSepaImportExceptionLP {

		try {
			if(xmlDocument.getBkToCstmrStmt().getGrpHdr().getMsgRcpt().getId().getOrgId().getBICOrBEI() == null &&
					xmlDocument.getBkToCstmrStmt().getGrpHdr().getMsgRcpt().getId().getOrgId().getOthr().isEmpty() &&
					xmlDocument.getBkToCstmrStmt().getGrpHdr().getMsgRcpt().getNm() == null) {
				throw new EJBSepaImportCountryExceptionLP(camtFormat.toString(), "<GrpHdr><MsgRcpt>", 
						lkz, EJBSepaImportExceptionLP.SEVERITY_WARNING);
			}
		} catch (NullPointerException e) {
			throw new EJBSepaImportCountryExceptionLP(camtFormat.toString(), "<GrpHdr><MsgRcpt>", 
					lkz, EJBSepaImportExceptionLP.SEVERITY_ERROR, e);
		}
	}

	@Override
	public void validateElectronicSeqNumber() throws EJBSepaImportExceptionLP {
		try {
			for (CAAccountStatement2 stmt : xmlDocument.getBkToCstmrStmt().getStmt()) {
				if(stmt.getElctrncSeqNb() == null) {
					throw new EJBSepaImportCountryExceptionLP(camtFormat.toString(), "<Stmt><ElctrncSeqNb>", 
							lkz, EJBSepaImportExceptionLP.SEVERITY_ERROR);
				}
			}
		} catch (NullPointerException e) {
			throw new EJBSepaImportCountryExceptionLP(camtFormat.toString(), "<Stmt><ElctrncSeqNb>", 
					lkz, EJBSepaImportExceptionLP.SEVERITY_ERROR, e);
		}
	}

	@Override
	public void validateLegalSeqNumber() throws EJBSepaImportExceptionLP {
		try {
			for (CAAccountStatement2 stmt : xmlDocument.getBkToCstmrStmt().getStmt()) {
				if(stmt.getLglSeqNb() == null) {
					throw new EJBSepaImportCountryExceptionLP(camtFormat.toString(), "<Stmt><LglSeqNb>", 
							lkz, EJBSepaImportExceptionLP.SEVERITY_ERROR);
				}
			}
		} catch (NullPointerException e) {
			throw new EJBSepaImportCountryExceptionLP(camtFormat.toString(), "<Stmt><LglSeqNb>", 
					lkz, EJBSepaImportExceptionLP.SEVERITY_ERROR, e);
		}
	}

	@Override
	public void validateAccountServicer() throws EJBSepaImportExceptionLP {
		try {
			for (CAAccountStatement2 stmt : xmlDocument.getBkToCstmrStmt().getStmt()) {
				if(stmt.getAcct().getSvcr().getFinInstnId().getBIC() == null) {
					throw new EJBSepaImportCountryExceptionLP(camtFormat.toString(), "<Stmt><Acct><Svcr>", 
							lkz, EJBSepaImportExceptionLP.SEVERITY_WARNING);
				}
			}
		} catch (NullPointerException e) {
			throw new EJBSepaImportCountryExceptionLP(camtFormat.toString(), "<Stmt><Acct><Svcr>", 
					lkz, EJBSepaImportExceptionLP.SEVERITY_ERROR, e);
		}
	}

	@Override
	public void validateEntryBankTransactionCode() throws EJBSepaImportExceptionLP {
		try {
			for (CAAccountStatement2 stmt : xmlDocument.getBkToCstmrStmt().getStmt()) {
				for (CAReportEntry2 ntry : stmt.getNtry()) {
					if(ntry.getBkTxCd().getDomn().getCd() == null && ntry.getBkTxCd().getPrtry().getCd() == null) {
						throw new EJBSepaImportCountryExceptionLP(camtFormat.toString(), "<Ntry><BkTxCd><Domn> && <Ntry><BkTxCd><Prtry>", 
								lkz, EJBSepaImportExceptionLP.SEVERITY_WARNING);
					}
				}
			}
		} catch (NullPointerException e) {
			throw new EJBSepaImportCountryExceptionLP(camtFormat.toString(), "<Ntry><BkTxCd><Domn>", 
					lkz, EJBSepaImportExceptionLP.SEVERITY_ERROR, e);
		}
	}

	@Override
	public void validateTransactionDtlsBankTransactionCode() throws EJBSepaImportExceptionLP {
		try {
			for (CAAccountStatement2 stmt : xmlDocument.getBkToCstmrStmt().getStmt()) {
				for (CAReportEntry2 ntry : stmt.getNtry()) {
					for (CAEntryDetails1 ntryDtls : ntry.getNtryDtls()) {
						for (CAEntryTransaction2 txDtl : ntryDtls.getTxDtls()) {
							if (txDtl.getBkTxCd().getDomn().getCd() == null && txDtl.getBkTxCd().getPrtry().getCd() == null) {
								throw new EJBSepaImportCountryExceptionLP(camtFormat.toString(), "<TxDtls><BkTxCd><Domn> && <TxDtls><BkTxCd><Prtry>", 
										lkz, EJBSepaImportExceptionLP.SEVERITY_WARNING);
							}
						}
					}
				}
			}
		} catch (NullPointerException e) {
			throw new EJBSepaImportCountryExceptionLP(camtFormat.toString(), "<Ntry><NtryDtls><TxDtls><BkTxCd><Domn>", 
					lkz, EJBSepaImportExceptionLP.SEVERITY_ERROR, e);
		}
	}

}
