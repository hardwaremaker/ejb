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
package com.lp.server.eingangsrechnung.bl;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;

import com.lp.server.eingangsrechnung.service.EingangsrechnungDto;
import com.lp.server.eingangsrechnung.service.ErZahlungsempfaenger;
import com.lp.server.eingangsrechnung.service.SepaExportTransformerFac;
import com.lp.server.eingangsrechnung.service.ZahlungsvorschlagDto;
import com.lp.server.finanz.service.BankverbindungDto;
import com.lp.server.partner.service.BankDto;
import com.lp.server.schema.iso20022.pain001V03.PAAccountIdentification4Choice;
import com.lp.server.schema.iso20022.pain001V03.PAActiveOrHistoricCurrencyAndAmount;
import com.lp.server.schema.iso20022.pain001V03.PAAmountType3Choice;
import com.lp.server.schema.iso20022.pain001V03.PABranchAndFinancialInstitutionIdentification4;
import com.lp.server.schema.iso20022.pain001V03.PACashAccount16;
import com.lp.server.schema.iso20022.pain001V03.PAChargeBearerType1Code;
import com.lp.server.schema.iso20022.pain001V03.PACreditTransferTransactionInformation10;
import com.lp.server.schema.iso20022.pain001V03.PACreditorReferenceInformation2;
import com.lp.server.schema.iso20022.pain001V03.PACreditorReferenceType1Choice;
import com.lp.server.schema.iso20022.pain001V03.PACreditorReferenceType2;
import com.lp.server.schema.iso20022.pain001V03.PACustomerCreditTransferInitiationV03;
import com.lp.server.schema.iso20022.pain001V03.PADocument;
import com.lp.server.schema.iso20022.pain001V03.PADocumentType3Code;
import com.lp.server.schema.iso20022.pain001V03.PAFinancialInstitutionIdentification7;
import com.lp.server.schema.iso20022.pain001V03.PAGroupHeader32;
import com.lp.server.schema.iso20022.pain001V03.PAPartyIdentification32;
import com.lp.server.schema.iso20022.pain001V03.PAPaymentIdentification1;
import com.lp.server.schema.iso20022.pain001V03.PAPaymentInstructionInformation3;
import com.lp.server.schema.iso20022.pain001V03.PAPaymentMethod3Code;
import com.lp.server.schema.iso20022.pain001V03.PAPaymentTypeInformation19;
import com.lp.server.schema.iso20022.pain001V03.PARemittanceInformation5;
import com.lp.server.schema.iso20022.pain001V03.PAServiceLevel8Choice;
import com.lp.server.schema.iso20022.pain001V03.PAStructuredRemittanceInformation7;
import com.lp.server.system.service.MandantDto;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Local
@Stateless
/**
 * Abgeleiteter SepaExportTransformer fuer Pain-Version 03
 * 
 * @author andi
 *
 */
public class SepaExportTransformerPainV03 extends SepaExportTransformer implements SepaExportTransformerFac{

	public SepaExportTransformerPainV03() {
	}

	@Override
	protected Object transformImpl() 
			throws EJBExceptionLP, RemoteException {
		
		BankverbindungDto bvDto = getFinanzFac()
				.bankverbindungFindByPrimaryKey(getLaufDto().getBankverbindungIId());
		BankDto bankDto = getBankFac()
				.bankFindByPrimaryKey(bvDto.getBankIId(), getTheClientDto());
		MandantDto mandantDto = getMandantFac()
				.mandantFindByPrimaryKey(getTheClientDto().getMandant(), getTheClientDto());
		
		//Validierung der Pflichtfelder gemaess pain-Spez
		getDataValidator().validateHeaderData(
				mandantDto.getPartnerDto().getCName1nachnamefirmazeile1(), 
				bvDto.getCIban(), 
				bankDto.getCBic());
		
		PADocument sepaDoc = new PADocument();
		PACustomerCreditTransferInitiationV03 cstmrCdtTrfInitn = 
				new PACustomerCreditTransferInitiationV03();
		myLogger.info("Erzeugen des GroupHeaders");
		cstmrCdtTrfInitn.setGrpHdr(createGroupHeader(
				mandantDto.getPartnerDto().getCName1nachnamefirmazeile1()));
		myLogger.info("Erzeugen der Zahlungsinformation");
		
		cstmrCdtTrfInitn.getPmtInf().addAll(createPaymentInformation(
				mandantDto.getPartnerDto().getCName1nachnamefirmazeile1(), 
				bvDto.getCIban(), bankDto.getCBic()));
		sepaDoc.setCstmrCdtTrfInitn(cstmrCdtTrfInitn);
		
		return sepaDoc;
	}

	/**
	 * Erzeugt die Zahlungsinformation (<PmtInf>) des Sepa-XML-Exports
	 * 
	 * @param auftraggeberName Name des Auftraggebers
	 * @param cIban IBAN des Auftraggebers
	 * @param cBic BIC des Auftraggebers
	 * @return XML-Objekt der Zahlungsinformation
	 * @throws RemoteException
	 * @throws EJBExceptionLP
	 */
	private List<PAPaymentInstructionInformation3> createPaymentInformation(String auftraggeberName, 
			String cIban, String cBic) throws RemoteException, EJBExceptionLP {

		List<PAPaymentInstructionInformation3> pmtInfList = new ArrayList<PAPaymentInstructionInformation3>();
		SimpleDateFormat pmtIdFormat = new SimpleDateFormat(PMTINFID_DATE_FORMAT);
		int count = 1;
		
		for (ZahlungsvorschlagDto zvDto : getZahlungsvorschlagDtos()) {
			
			PAPaymentInstructionInformation3 pmtInf = new PAPaymentInstructionInformation3();
			pmtInf.setPmtInfId(PMTINFID_PREFIX + pmtIdFormat.format(new Timestamp(getCreationTimeMillis())) + "-" + count++);
			pmtInf.setPmtMtd(PAPaymentMethod3Code.TRF);
			// als Sammelbuchung behandeln?
			pmtInf.setBtchBookg(false);
			
			PAServiceLevel8Choice svcLvl = new PAServiceLevel8Choice();
			svcLvl.setCd(SERVICE_LEVEL_CODE_SEPA);
			PAPaymentTypeInformation19 pmtTpInf = new PAPaymentTypeInformation19();
			pmtTpInf.setSvcLvl(svcLvl);
			pmtInf.setPmtTpInf(pmtTpInf);
			pmtInf.setReqdExctnDt(convertToXmlGregorian(getCreationTimeMillis(), REQUESTEDEXECUTIONDATE_FORMAT));
			pmtInf.setDbtr(createPartyIdentification(auftraggeberName));
			pmtInf.setDbtrAcct(createCashAccount(cIban));
			pmtInf.setDbtrAgt(getFinancialInstitutionId(cBic));
			pmtInf.setChrgBr(PAChargeBearerType1Code.SLEV);
			
			if(fillCreditTransferTransactionInformation(pmtInf, zvDto)) {
				pmtInfList.add(pmtInf);
			}
		}		
		return pmtInfList;
	}

	/**
	 * Erzeugt die Daten der einzelnen Zahlungen
	 * 
	 * @param pmtInf XML-Objekt der Zahlungsinfo, die befuellt wird
	 * @throws RemoteException
	 * @throws EJBExceptionLP
	 */
	private boolean fillCreditTransferTransactionInformation(
			PAPaymentInstructionInformation3 pmtInf, ZahlungsvorschlagDto zvDto) 
					throws RemoteException, EJBExceptionLP {

		BigDecimal ctrlSum = new BigDecimal(0);
		Integer nbOfTxs = 0;
		
		EingangsrechnungDto erDto = getEingangsrechnungFac()
				.eingangsrechnungFindByPrimaryKey(zvDto.getEingangsrechnungIId());
		ErZahlungsempfaenger erBv = getErBankverbindung(erDto);
		if (erBv == null) return false;
		BankDto bankLiefDto = getBankFac().bankFindByPrimaryKey(erBv.getPartnerbankDto().getBankPartnerIId(), getTheClientDto());
		
		//Validierung der Pflichtdaten
		getDataValidator().validatePaymentData(zvDto.getNZahlbetrag(), 
				erBv.getPartnerbankDto().getCIban(), bankLiefDto, erBv.getPartnerDto(), erDto);
		
		PACreditTransferTransactionInformation10 cdtTrfTxInf = new PACreditTransferTransactionInformation10();
		PAPaymentIdentification1 pmtId = new PAPaymentIdentification1();
		pmtId.setEndToEndId(getEndToEndId(zvDto));
		cdtTrfTxInf.setPmtId(pmtId);
		
		cdtTrfTxInf.setAmt(createInstructedAmount(zvDto.getNZahlbetrag()));
		cdtTrfTxInf.setCdtrAgt(getFinancialInstitutionId(bankLiefDto.getCBic()));
		cdtTrfTxInf.setCdtr(createPartyIdentification(erBv.getPartnerDto().getCName1nachnamefirmazeile1()));
		cdtTrfTxInf.setCdtrAcct(createCashAccount(erBv.getPartnerbankDto().getCIban()));
		cdtTrfTxInf.setRmtInf(createRemittanceInformation(erDto, zvDto));
		
		pmtInf.getCdtTrfTxInf().add(cdtTrfTxInf);
		
		ctrlSum = ctrlSum.add(zvDto.getNZahlbetrag());
		nbOfTxs++;
	
		pmtInf.setCtrlSum(ctrlSum);
		pmtInf.setNbOfTxs(nbOfTxs.toString());
		
		return true;
	}

	/**
	 * Erzeugt den Zahlbetrag als XML-Objekt
	 * 
	 * @param nZahlbetrag Zahlbetrag
	 * @return XML-Objekt eines Zahlbetrags
	 */
	private PAAmountType3Choice createInstructedAmount(BigDecimal nZahlbetrag) {
		
		PAActiveOrHistoricCurrencyAndAmount instdAmt = new PAActiveOrHistoricCurrencyAndAmount();
		instdAmt.setCcy(CURRENCY_SEPA);
		instdAmt.setValue(nZahlbetrag);
		PAAmountType3Choice amt = new PAAmountType3Choice();
		amt.setInstdAmt(instdAmt);
		
		return amt;
	}

	/**
	 * Erzeugt die Information zur Ueberweisung in strukturierter (Zahlungsreferenz, 
	 * wenn vorhanden) oder unstruktierter (Verwendungszweck) Form
	 * 
	 * @param erDto EingangsrechnungsDto
	 * @return das XML-Objekt der Zahlungsinformation
	 */
	private PARemittanceInformation5 createRemittanceInformation(
			EingangsrechnungDto erDto, ZahlungsvorschlagDto zvDto) {
		
		PARemittanceInformation5 rmtInf = new PARemittanceInformation5();
		if(!Helper.isStringEmpty(erDto.getCKundendaten())) {
			PACreditorReferenceType1Choice cdOrPrtry = new PACreditorReferenceType1Choice();
			cdOrPrtry.setCd(PADocumentType3Code.SCOR);
			PACreditorReferenceType2 tp = new PACreditorReferenceType2();
			tp.setCdOrPrtry(cdOrPrtry);
			PACreditorReferenceInformation2 cdtrRefInf = new PACreditorReferenceInformation2();
			cdtrRefInf.setTp(tp);
			cdtrRefInf.setRef(erDto.getCKundendaten());
			PAStructuredRemittanceInformation7 strd = new PAStructuredRemittanceInformation7();
			strd.setCdtrRefInf(cdtrRefInf);
			rmtInf.getStrd().add(strd);
		} else {
			rmtInf.getUstrd().add(getPaymentIdentification(erDto, zvDto));
		}
		
		return rmtInf;
	}

	/**
	 * Erzeugt die Info ueber die BIC einer Bank
	 * 
	 * @param cBic BIC einer Bank
	 * @return XML-Objekt ueber die BIC
	 */
	private PABranchAndFinancialInstitutionIdentification4 getFinancialInstitutionId(
			String cBic) {
		
		PAFinancialInstitutionIdentification7 finInstnId = new PAFinancialInstitutionIdentification7();
		finInstnId.setBIC(cBic);
		PABranchAndFinancialInstitutionIdentification4 id = new PABranchAndFinancialInstitutionIdentification4();
		id.setFinInstnId(finInstnId);
		
		return id;
	}

	/**
	 * Erstellt den GroupHeader mit Kopfinformation ueber die Buchungen
	 * 
	 * @param auftraggeberName Name des Auftraggebers
	 * @return XML-Objekt des GroupHeaders
	 */
	private PAGroupHeader32 createGroupHeader(String auftraggeberName) {
		PAGroupHeader32 grpHdr = new PAGroupHeader32();

		SimpleDateFormat msgIdFormat = new SimpleDateFormat(MSGID_DATE_FORMAT);
		grpHdr.setMsgId(MSGID_PREFIX + msgIdFormat.format(new Timestamp(getCreationTimeMillis())));
		grpHdr.setCreDtTm(convertToXmlGregorian(getCreationTimeMillis(), CREATIONDATETIME_FORMAT));
		
		BigDecimal ctrlSum = new BigDecimal(0);
		Integer nbOfTxs = 0;
		for (ZahlungsvorschlagDto zvDto : getZahlungsvorschlagDtos()) {
			ctrlSum = ctrlSum.add(zvDto.getNZahlbetrag());
			nbOfTxs++;
		}
		grpHdr.setNbOfTxs(nbOfTxs.toString());
		grpHdr.setCtrlSum(ctrlSum);
		grpHdr.setInitgPty(createPartyIdentification(auftraggeberName));
		
		return grpHdr;
	}

	/**
	 * 
	 * @param auftraggeberName
	 * @return Auftraggebername als PartyIdentification
	 */
	private PAPartyIdentification32 createPartyIdentification(String auftraggeberName) {
		PAPartyIdentification32 ptyId = new PAPartyIdentification32();
		ptyId.setNm(auftraggeberName);
		return ptyId;
	}
	
	/**
	 * Erstellt ein XML-Objekt ueber die IBAN
	 * 
	 * @param cIban IBAN
	 * @return IBAN als CashAccount Objekt
	 */
	private PACashAccount16 createCashAccount(String cIban) {
		PAAccountIdentification4Choice accId = new PAAccountIdentification4Choice();
		if (cIban != null) {
			accId.setIBAN(cIban.replaceAll("\\s", ""));
		}
		PACashAccount16 account = new PACashAccount16();
		account.setId(accId);
		
		return account;
	}

	@Override
	protected boolean acceptEingangsrechnung(EingangsrechnungDto erDto) {
		return erDto != null && erDto.getWaehrungCNr().startsWith(SepaExportTransformerFac.CURRENCY_SEPA);
	}
	
	@Override
	protected boolean isSepa() {
		return true;
	}
	
	@Override
	protected boolean isSwiss() {
		return false;
	}
}
