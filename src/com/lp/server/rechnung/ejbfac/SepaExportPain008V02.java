package com.lp.server.rechnung.ejbfac;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;

import com.lp.server.eingangsrechnung.service.SepaExportTransformerFac;
import com.lp.server.finanz.service.BankverbindungDto;
import com.lp.server.partner.service.BankDto;
import com.lp.server.rechnung.service.LastschriftvorschlagDto;
import com.lp.server.rechnung.service.RechnungDto;
import com.lp.server.rechnung.service.sepa.errors.SepaException;
import com.lp.server.schema.iso20022.pain008V02.ObjectFactory;
import com.lp.server.schema.iso20022.pain008V02.PAAccountIdentification4Choice;
import com.lp.server.schema.iso20022.pain008V02.PAActiveOrHistoricCurrencyAndAmount;
import com.lp.server.schema.iso20022.pain008V02.PABranchAndFinancialInstitutionIdentification4;
import com.lp.server.schema.iso20022.pain008V02.PACashAccount16;
import com.lp.server.schema.iso20022.pain008V02.PAChargeBearerType1Code;
import com.lp.server.schema.iso20022.pain008V02.PACustomerDirectDebitInitiationV02;
import com.lp.server.schema.iso20022.pain008V02.PADirectDebitTransaction6;
import com.lp.server.schema.iso20022.pain008V02.PADirectDebitTransactionInformation9;
import com.lp.server.schema.iso20022.pain008V02.PADocument;
import com.lp.server.schema.iso20022.pain008V02.PAFinancialInstitutionIdentification7;
import com.lp.server.schema.iso20022.pain008V02.PAGenericPersonIdentification1;
import com.lp.server.schema.iso20022.pain008V02.PAGroupHeader39;
import com.lp.server.schema.iso20022.pain008V02.PALocalInstrument2Choice;
import com.lp.server.schema.iso20022.pain008V02.PAMandateRelatedInformation6;
import com.lp.server.schema.iso20022.pain008V02.PAParty6Choice;
import com.lp.server.schema.iso20022.pain008V02.PAPartyIdentification32;
import com.lp.server.schema.iso20022.pain008V02.PAPaymentIdentification1;
import com.lp.server.schema.iso20022.pain008V02.PAPaymentInstructionInformation4;
import com.lp.server.schema.iso20022.pain008V02.PAPaymentMethod2Code;
import com.lp.server.schema.iso20022.pain008V02.PAPaymentTypeInformation20;
import com.lp.server.schema.iso20022.pain008V02.PAPersonIdentification5;
import com.lp.server.schema.iso20022.pain008V02.PAPersonIdentificationSchemeName1Choice;
import com.lp.server.schema.iso20022.pain008V02.PARemittanceInformation5;
import com.lp.server.schema.iso20022.pain008V02.PASequenceType1Code;
import com.lp.server.schema.iso20022.pain008V02.PAServiceLevel8Choice;

@Local
@Stateless
public class SepaExportPain008V02 extends SepaExportPain008 implements SepaExportPain008Fac {

	private ObjectFactory sepaFactory;
	private SepaDatenFinder sepaDatenFinder;
	private BankverbindungDto myBankverbindung;
	private BankDto myBank;
	
	public SepaExportPain008V02() {
		sepaFactory = new ObjectFactory();
	}
	
	private ObjectFactory getSepaFactory() {
		return sepaFactory;
	}
	
	private void setSepaDatenFinder(SepaDatenFinder sepaDatenFinder) {
		this.sepaDatenFinder = sepaDatenFinder;
	}
	
	private SepaDatenFinder getSepaDatenFinder() {
		return sepaDatenFinder;
	}
	
	private void setMyBank(BankDto myBank) {
		this.myBank = myBank;
	}
	
	private BankDto getMyBank() {
		return myBank;
	}
	
	private void setMyBankverbindung(BankverbindungDto myBankverbindung) {
		this.myBankverbindung = myBankverbindung;
	}
	
	private BankverbindungDto getMyBankverbindung() {
		return myBankverbindung;
	}

	@Override
	protected Object transformImpl() throws SepaException {
		setSepaDatenFinder(new SepaDatenFinder());
		
		setMyBankverbindung(getSepaDatenFinder().getMandantLastschriftBankverbindung());
		setMyBank(getSepaDatenFinder().getBankByPrimaryKey(getMyBankverbindung().getBankIId()));
		
		PADocument sepaDoc = getSepaFactory().createPADocument();
		PACustomerDirectDebitInitiationV02 cstmrDrctDbtInitn = 
				getSepaFactory().createPACustomerDirectDebitInitiationV02();
		cstmrDrctDbtInitn.setGrpHdr(createGroupHeader(
				getSepaDatenFinder().getMandantDto().getPartnerDto().getCName1nachnamefirmazeile1()));
		cstmrDrctDbtInitn.getPmtInf().addAll(createPaymentInformation(
				getSepaDatenFinder().getMandantDto().getPartnerDto().getCName1nachnamefirmazeile1()));
		sepaDoc.setCstmrDrctDbtInitn(cstmrDrctDbtInitn);
		return sepaDoc;
	}
	
	private List<PAPaymentInstructionInformation4> createPaymentInformation(String auftraggebername) throws SepaException {
		List<PAPaymentInstructionInformation4> pmtInfList = new ArrayList<PAPaymentInstructionInformation4>();
		int count = 1;
		
		for (LastschriftvorschlagDto lvDto : getLastschriftvorschlagDtos()) {
			PAPaymentInstructionInformation4 pmtInf = getSepaFactory().createPAPaymentInstructionInformation4();
			pmtInf.setPmtInfId(createPaymentInformationId(count++));
			pmtInf.setPmtMtd(PAPaymentMethod2Code.DD);
			pmtInf.setBtchBookg(false);
			PAServiceLevel8Choice svcLvl = getSepaFactory().createPAServiceLevel8Choice();
			svcLvl.setCd(SepaExportTransformerFac.SERVICE_LEVEL_CODE_SEPA);
			PAPaymentTypeInformation20 pmtTpInf = getSepaFactory().createPAPaymentTypeInformation20();
			pmtTpInf.setSvcLvl(svcLvl);
			PALocalInstrument2Choice lclInstrm = getSepaFactory().createPALocalInstrument2Choice();
			lclInstrm.setCd("CORE");
			pmtTpInf.setLclInstrm(lclInstrm);
			pmtTpInf.setSeqTp(PASequenceType1Code.RCUR);
			pmtInf.setPmtTpInf(pmtTpInf);
			pmtInf.setCdtr(createPartyIdentfication(auftraggebername));
			pmtInf.setCdtrAcct(createCashAccount(getMyBankverbindung().getCIban()));
			pmtInf.setCdtrAgt(createFinancialInstitutionId(getMyBank().getCBic()));
			pmtInf.setChrgBr(PAChargeBearerType1Code.SLEV);
			
			pmtInf.setCdtrSchmeId(createCreditorId());
			RechnungDto rechnungDto = getSepaDatenFinder().getRechnung(lvDto.getRechnungIId());
			pmtInf.setReqdColltnDt(convertToXmlGregorian(
					getSepaDatenFinder().getLastschriftFaelligkeit(rechnungDto).getTime(), 
					SepaExportTransformerFac.REQUESTEDEXECUTIONDATE_FORMAT));
			pmtInf.getDrctDbtTxInf().add(createDirectDebitTransactionInformation(lvDto, rechnungDto));
			
			pmtInfList.add(pmtInf);
		}
		
		return pmtInfList;
	}
	
	private PADirectDebitTransactionInformation9 createDirectDebitTransactionInformation(
			LastschriftvorschlagDto lvDto, RechnungDto rechnungDto) throws SepaException {
		PartnerBankDaten partnerBankDaten = getSepaDatenFinder().getPartnerBankDaten(rechnungDto);
		
		PADirectDebitTransactionInformation9 ddTransInf = 
				getSepaFactory().createPADirectDebitTransactionInformation9();
		PADirectDebitTransaction6 ddTrans = getSepaFactory().createPADirectDebitTransaction6();
		PAMandateRelatedInformation6 mandatInf = 
				getSepaFactory().createPAMandateRelatedInformation6();
		
		getSepaDatenFinder().checkSepaMandat(rechnungDto);
		mandatInf.setMndtId(partnerBankDaten.getBankverbindungDto().getCSepamandatsnummer());
		mandatInf.setDtOfSgntr(convertToXmlGregorian(
				partnerBankDaten.getBankverbindungDto().getTSepaerteilt().getTime(), 
				SepaExportTransformerFac.DATEOFSIGNATURE_FORMAT));
		ddTrans.setMndtRltdInf(mandatInf);
		ddTransInf.setDrctDbtTx(ddTrans);
		
		PAPaymentIdentification1 pmtId = getSepaFactory().createPAPaymentIdentification1();
		pmtId.setEndToEndId(getSepaDatenFinder().getAuftraggeberreferenz(lvDto));
		ddTransInf.setPmtId(pmtId);
		ddTransInf.setInstdAmt(createAmount(lvDto.getNZahlbetrag()));
		ddTransInf.setDbtr(createPartyIdentfication(partnerBankDaten.getPartnerDto().getCName1nachnamefirmazeile1()));
		ddTransInf.setDbtrAgt(createFinancialInstitutionId(partnerBankDaten.getBankDto().getCBic()));
		ddTransInf.setDbtrAcct(createCashAccount(partnerBankDaten.getBankverbindungDto().getCIban()));
		ddTransInf.setRmtInf(createRemittanceInformation(lvDto, rechnungDto));
		
		return ddTransInf;
	}

	private PARemittanceInformation5 createRemittanceInformation(
			LastschriftvorschlagDto lvDto, RechnungDto rechnungDto) {
		PARemittanceInformation5 remInfo = getSepaFactory().createPARemittanceInformation5();
		remInfo.getUstrd().add(getSepaDatenFinder().getVerwendungszweck(lvDto, rechnungDto));
		return remInfo;
	}

	private PAActiveOrHistoricCurrencyAndAmount createAmount(
			BigDecimal nZahlbetrag) {
		PAActiveOrHistoricCurrencyAndAmount amount = 
				getSepaFactory().createPAActiveOrHistoricCurrencyAndAmount();
		amount.setCcy(SepaExportTransformerFac.CURRENCY_SEPA);
		amount.setValue(nZahlbetrag);
		return amount;
	}

	private PAPartyIdentification32 createCreditorId() throws SepaException {
		PAPartyIdentification32 creditorId = getSepaFactory().createPAPartyIdentification32();
		PAParty6Choice id = getSepaFactory().createPAParty6Choice();
		PAPersonIdentification5 personId = getSepaFactory().createPAPersonIdentification5();
		PAGenericPersonIdentification1 genPersonId = getSepaFactory().createPAGenericPersonIdentification1();
		genPersonId.setId(getSepaDatenFinder().getMandantDto().getCGlauebiger());
		PAPersonIdentificationSchemeName1Choice schemeName = getSepaFactory().createPAPersonIdentificationSchemeName1Choice();
		schemeName.setPrtry(SepaExportTransformerFac.SERVICE_LEVEL_CODE_SEPA);
		genPersonId.setSchmeNm(schemeName);
		personId.getOthr().add(genPersonId);
		id.setPrvtId(personId);
		creditorId.setId(id);
		
		return creditorId;
	}

	private PABranchAndFinancialInstitutionIdentification4 createFinancialInstitutionId(
			String bic) {
		PAFinancialInstitutionIdentification7 finInstnId = 
				getSepaFactory().createPAFinancialInstitutionIdentification7();
		finInstnId.setBIC(bic);
		PABranchAndFinancialInstitutionIdentification4 id = 
				getSepaFactory().createPABranchAndFinancialInstitutionIdentification4();
		id.setFinInstnId(finInstnId);
		return id;
	}

	private PACashAccount16 createCashAccount(String cIban) {
		PAAccountIdentification4Choice accId = getSepaFactory().createPAAccountIdentification4Choice();
		if (cIban != null) {
			accId.setIBAN(cIban.replaceAll("\\s", ""));
		}
		PACashAccount16 account = getSepaFactory().createPACashAccount16();
		account.setId(accId);
		return account;
	}

	protected String createPaymentInformationId(Integer count) {
		StringBuilder builder = new StringBuilder();
		builder.append(SepaExportTransformerFac.PMTINFID_PREFIX)
			.append(new SimpleDateFormat(SepaExportTransformerFac.PMTINFID_DATE_FORMAT)
				.format(new Timestamp(getCreationTimeMillis())))
			.append("-")
			.append(count);
		return builder.toString();
	}

	private PAGroupHeader39 createGroupHeader(String auftraggebername) {
		PAGroupHeader39 groupHeader = getSepaFactory().createPAGroupHeader39();
		SimpleDateFormat msgIdFormat = new SimpleDateFormat(SepaExportTransformerFac.MSGID_DATE_FORMAT);
		groupHeader.setMsgId(SepaExportTransformerFac.MSGID_PREFIX 
				+ msgIdFormat.format(new Timestamp(getCreationTimeMillis())));
		groupHeader.setCreDtTm(convertToXmlGregorian(getCreationTimeMillis(), 
				SepaExportTransformerFac.CREATIONDATETIME_FORMAT));
		
		BigDecimal ctrlSum = BigDecimal.ZERO;
		Integer nbOfTransactions = 0;
		for (LastschriftvorschlagDto lvDto : getLastschriftvorschlagDtos()) {
			ctrlSum = ctrlSum.add(lvDto.getNZahlbetrag());
			nbOfTransactions++;
		}
		
		groupHeader.setNbOfTxs(nbOfTransactions.toString());
		groupHeader.setCtrlSum(ctrlSum);
		groupHeader.setInitgPty(createPartyIdentfication(auftraggebername));
		
		return groupHeader;
	}

	private PAPartyIdentification32 createPartyIdentfication(String auftraggebername) {
		// auf 70 Zeichen begrenzen
		auftraggebername = auftraggebername != null && auftraggebername.length() > 70 ? 
				auftraggebername.substring(0, 70) : auftraggebername;
		PAPartyIdentification32 partyId = getSepaFactory().createPAPartyIdentification32();
		partyId.setNm(auftraggebername);
		
		return partyId;
	}

}
