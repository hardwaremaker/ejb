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
import com.lp.server.eingangsrechnung.service.ZahlungsvorschlaglaufDto;
import com.lp.server.finanz.service.BankverbindungDto;
import com.lp.server.partner.service.BankDto;
import com.lp.server.partner.service.PartnerDto;
import com.lp.server.partner.service.PartnerbankDto;
import com.lp.server.schema.iso20022.ch.pain001V03.ObjectFactory;
import com.lp.server.schema.iso20022.ch.pain001V03.PACHAccountIdentification4ChoiceCH;
import com.lp.server.schema.iso20022.ch.pain001V03.PACHActiveOrHistoricCurrencyAndAmount;
import com.lp.server.schema.iso20022.ch.pain001V03.PACHAmountType3Choice;
import com.lp.server.schema.iso20022.ch.pain001V03.PACHBranchAndFinancialInstitutionIdentification4CH;
import com.lp.server.schema.iso20022.ch.pain001V03.PACHBranchAndFinancialInstitutionIdentification4CHBicOrClrId;
import com.lp.server.schema.iso20022.ch.pain001V03.PACHCashAccount16CHId;
import com.lp.server.schema.iso20022.ch.pain001V03.PACHCashAccount16CHIdTpCcy;
import com.lp.server.schema.iso20022.ch.pain001V03.PACHChargeBearerType1Code;
import com.lp.server.schema.iso20022.ch.pain001V03.PACHCreditTransferTransactionInformation10CH;
import com.lp.server.schema.iso20022.ch.pain001V03.PACHCreditorReferenceInformation2;
import com.lp.server.schema.iso20022.ch.pain001V03.PACHCreditorReferenceType1Choice;
import com.lp.server.schema.iso20022.ch.pain001V03.PACHCreditorReferenceType2;
import com.lp.server.schema.iso20022.ch.pain001V03.PACHCustomerCreditTransferInitiationV03CH;
import com.lp.server.schema.iso20022.ch.pain001V03.PACHDocument;
import com.lp.server.schema.iso20022.ch.pain001V03.PACHDocumentType3Code;
import com.lp.server.schema.iso20022.ch.pain001V03.PACHFinancialInstitutionIdentification7CH;
import com.lp.server.schema.iso20022.ch.pain001V03.PACHFinancialInstitutionIdentification7CHBicOrClrId;
import com.lp.server.schema.iso20022.ch.pain001V03.PACHGroupHeader32CH;
import com.lp.server.schema.iso20022.ch.pain001V03.PACHPartyIdentification32CH;
import com.lp.server.schema.iso20022.ch.pain001V03.PACHPartyIdentification32CHName;
import com.lp.server.schema.iso20022.ch.pain001V03.PACHPartyIdentification32CHNameAndId;
import com.lp.server.schema.iso20022.ch.pain001V03.PACHPaymentIdentification1;
import com.lp.server.schema.iso20022.ch.pain001V03.PACHPaymentInstructionInformation3CH;
import com.lp.server.schema.iso20022.ch.pain001V03.PACHPaymentMethod3Code;
import com.lp.server.schema.iso20022.ch.pain001V03.PACHPaymentTypeInformation19CH;
import com.lp.server.schema.iso20022.ch.pain001V03.PACHPostalAddress6CH;
import com.lp.server.schema.iso20022.ch.pain001V03.PACHRemittanceInformation5CH;
import com.lp.server.schema.iso20022.ch.pain001V03.PACHServiceLevel8Choice;
import com.lp.server.schema.iso20022.ch.pain001V03.PACHStructuredRemittanceInformation7;
import com.lp.server.system.service.LandDto;
import com.lp.server.system.service.MandantDto;
import com.lp.server.system.service.TheClientDto;
import com.lp.server.util.collection.CollectionTools;
import com.lp.server.util.collection.IInject;
import com.lp.util.EJBExceptionLP;
import com.lp.util.Helper;

@Local
@Stateless
public class SepaExportTransformerPain001V03CH extends SepaExportTransformer implements SepaExportTransformerFac {

	private ObjectFactory objectFactory = new ObjectFactory();
	
	public SepaExportTransformerPain001V03CH() {
	}

	@Override
	protected Object transformImpl() throws EJBExceptionLP, RemoteException {
		IPain001ProducerCh painProducer = new Pain001V03ProducerCH();
		return painProducer.createPain001(getLaufDto(), getTheClientDto());
	}
	
	@Override
	protected boolean acceptEingangsrechnung(EingangsrechnungDto erDto) {
		return erDto != null;
	}

	public interface IPain001ProducerCh {
		PACHDocument createPain001(ZahlungsvorschlaglaufDto zvLaufDto, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException;
	}
	
	private PACHAmountType3Choice createInstructedAmount(BigDecimal amount, String currency) {
		PACHActiveOrHistoricCurrencyAndAmount instructedAmount = objectFactory.createPACHActiveOrHistoricCurrencyAndAmount();
		instructedAmount.setCcy(currency);
		instructedAmount.setValue(amount);
		PACHAmountType3Choice amt = objectFactory.createPACHAmountType3Choice();
		amt.setInstdAmt(instructedAmount);
		
		return amt;
	}
	
	private PACHBranchAndFinancialInstitutionIdentification4CHBicOrClrId createFinancialInstitutionBicOrId(String bic) {
		PACHBranchAndFinancialInstitutionIdentification4CHBicOrClrId id = 
				objectFactory.createPACHBranchAndFinancialInstitutionIdentification4CHBicOrClrId();
		PACHFinancialInstitutionIdentification7CHBicOrClrId finInstnId =
				objectFactory.createPACHFinancialInstitutionIdentification7CHBicOrClrId();
		finInstnId.setBIC(bic);
		id.setFinInstnId(finInstnId);
		return id;
	}

	private PACHCashAccount16CHIdTpCcy createCashAccount(String iban) {
		PACHCashAccount16CHIdTpCcy cashAcc = objectFactory.createPACHCashAccount16CHIdTpCcy();
		PACHAccountIdentification4ChoiceCH accId = objectFactory.createPACHAccountIdentification4ChoiceCH();
		accId.setIBAN(iban != null ? iban.replaceAll("\\s", "") : null);
		cashAcc.setId(accId);
		return cashAcc;
	}

	private PACHCashAccount16CHId createCashAccountId(String iban) {
		PACHCashAccount16CHId cashAcc = objectFactory.createPACHCashAccount16CHId();
		PACHAccountIdentification4ChoiceCH accId = objectFactory.createPACHAccountIdentification4ChoiceCH();
		accId.setIBAN(iban != null ? iban.replaceAll("\\s", "") : null);
		cashAcc.setId(accId);
		return cashAcc;
	}

	private PACHPartyIdentification32CHNameAndId createPartyIdentificationNameAndId(PartnerDto partnerDto) {
		PACHPartyIdentification32CHNameAndId id = objectFactory.createPACHPartyIdentification32CHNameAndId();
		id.setNm(partnerDto.getCName1nachnamefirmazeile1());
		return id;
	}
	
	private PACHPartyIdentification32CH createPartyIdentification(PartnerDto partnerDto) {
		PACHPartyIdentification32CH id = objectFactory.createPACHPartyIdentification32CH();
		id.setNm(partnerDto.getCName1nachnamefirmazeile1());
		return id;
	}
	
	private PACHPartyIdentification32CHName createPartyIdentificationName(PartnerDto partnerDto) {
		PACHPartyIdentification32CHName id = objectFactory.createPACHPartyIdentification32CHName();
		id.setNm(partnerDto.getCName1nachnamefirmazeile1());
		return id;
	}

	private PACHBranchAndFinancialInstitutionIdentification4CH createFinancialInstitutionId(String bic) {
		PACHBranchAndFinancialInstitutionIdentification4CH id = objectFactory.createPACHBranchAndFinancialInstitutionIdentification4CH();
		PACHFinancialInstitutionIdentification7CH finInstnId = objectFactory.createPACHFinancialInstitutionIdentification7CH();
		finInstnId.setBIC(bic);
		id.setFinInstnId(finInstnId);
		return id;
	}

	private PACHRemittanceInformation5CH createRemittanceInformation(EingangsrechnungDto erDto,
			ZahlungsvorschlagDto zvDto) {
		PACHRemittanceInformation5CH rmtInf = objectFactory.createPACHRemittanceInformation5CH();
		if (!Helper.isStringEmpty(erDto.getCKundendaten())) {
			PACHCreditorReferenceInformation2 credRefInf = objectFactory.createPACHCreditorReferenceInformation2();
			credRefInf.setRef(erDto.getCKundendaten());
			PACHStructuredRemittanceInformation7 strRemInf = objectFactory.createPACHStructuredRemittanceInformation7();
			strRemInf.setCdtrRefInf(credRefInf);
			rmtInf.setStrd(strRemInf);
		} else {
			rmtInf.setUstrd(getPaymentIdentification(erDto, zvDto));
		}
		
		return rmtInf;
	}

	public class Pain001V03ProducerCH implements IPain001ProducerCh {
		private BankverbindungDto bankverbindungDto;
		private BankDto bankDto;
		private PartnerDto mandantPartnerDto;

		public Pain001V03ProducerCH() {
		}
		
		@Override
		public PACHDocument createPain001(ZahlungsvorschlaglaufDto zvLaufDto, TheClientDto theClientDto) throws EJBExceptionLP, RemoteException {
			bankverbindungDto = getFinanzFac().bankverbindungFindByPrimaryKey(getLaufDto().getBankverbindungIId());
			bankDto = getBankFac().bankFindByPrimaryKey(bankverbindungDto.getBankIId(), theClientDto);
			MandantDto mandantDto = getMandantFac().mandantFindByPrimaryKey(theClientDto.getMandant(), theClientDto);
			mandantPartnerDto = mandantDto.getPartnerDto();
			
			PACHCustomerCreditTransferInitiationV03CH painContent = objectFactory.createPACHCustomerCreditTransferInitiationV03CH();
			PACHGroupHeader32CH groupHeader = createGroupHeader();
			painContent.setGrpHdr(groupHeader);
			List<PACHPaymentInstructionInformation3CH> payments = createPayments();
			painContent.getPmtInf().addAll(payments);
			
			PACHDocument painDoc = objectFactory.createPACHDocument();
			painDoc.setCstmrCdtTrfInitn(painContent);
			
			return painDoc;
		}
		
		private PACHGroupHeader32CH createGroupHeader() {
			PACHGroupHeader32CH groupHeader = objectFactory.createPACHGroupHeader32CH();

			SimpleDateFormat msgIdFormat = new SimpleDateFormat(MSGID_DATE_FORMAT);
			groupHeader.setMsgId(MSGID_PREFIX + msgIdFormat.format(new Timestamp(getCreationTimeMillis())));
			
			groupHeader.setCreDtTm(convertToXmlGregorian(getCreationTimeMillis(), CREATIONDATETIME_FORMAT));
			BigDecimal sum = CollectionTools.inject(BigDecimal.ZERO, getZahlungsvorschlagDtos(), new IInject<ZahlungsvorschlagDto, BigDecimal>() {
				public BigDecimal inject(BigDecimal sum, ZahlungsvorschlagDto each) {
					sum = sum.add(each.getNZahlbetrag());
					return sum;
				};
			});
			groupHeader.setCtrlSum(sum);
			groupHeader.setNbOfTxs(String.valueOf(getZahlungsvorschlagDtos().size()));
			groupHeader.setInitgPty(createPartyIdentificationNameAndId(mandantPartnerDto));
			
			return groupHeader;
		}

		private List<PACHPaymentInstructionInformation3CH> createPayments() {
			List<PACHPaymentInstructionInformation3CH> payments = new ArrayList<PACHPaymentInstructionInformation3CH>();
			SimpleDateFormat pmtIdFormat = new SimpleDateFormat(PMTINFID_DATE_FORMAT);
			int count = 1;
			PaymentInfoProducerFactory factory = new PaymentInfoProducerFactory();

			for (ZahlungsvorschlagDto zvDto : getZahlungsvorschlagDtos()) {
				EingangsrechnungDto erDto = findER(zvDto.getEingangsrechnungIId());
				ErZahlungsempfaenger erBv = getErBankverbindung(erDto);
				if (erBv == null) continue;
				
				BankDto bankLiefDto = findBank(erBv.getPartnerbankDto().getBankPartnerIId());
				//Validierung der Pflichtdaten
				getDataValidator().validatePaymentData(zvDto.getNZahlbetrag(), 
						erBv.getPartnerbankDto().getCIban(), bankLiefDto, erBv.getPartnerDto(), erDto);
				
				PACHPaymentInstructionInformation3CH pmtInf = objectFactory.createPACHPaymentInstructionInformation3CH();
				pmtInf.setPmtInfId(PMTINFID_PREFIX + pmtIdFormat.format(new Timestamp(getCreationTimeMillis())) + "-" + count++);
				pmtInf.setReqdExctnDt(convertToXmlGregorian(getCreationTimeMillis(), REQUESTEDEXECUTIONDATE_FORMAT));
				pmtInf.setPmtMtd(PACHPaymentMethod3Code.TRF);
				pmtInf.setBtchBookg(false);
				pmtInf.setDbtr(createPartyIdentification(mandantPartnerDto));
				pmtInf.setDbtrAcct(createCashAccount(bankverbindungDto.getCIban()));
				pmtInf.setDbtrAgt(createFinancialInstitutionBicOrId(bankDto.getCBic()));
				
				IPaymentInfoChProducer producer = factory.getProducer(erDto, zvDto, erBv.getPartnerDto(), erBv.getPartnerbankDto(), bankLiefDto, getTheClientDto());
				producer.setupPayment(pmtInf);
				
				payments.add(pmtInf);
			}
			
			return payments;
		}
	}
	
	public class PaymentInfoProducerFactory {
		public IPaymentInfoChProducer getProducer(EingangsrechnungDto erDto, ZahlungsvorschlagDto zvDto, PartnerDto partnerDtoEmpfaenger, 
				PartnerbankDto bvLieferantDto, BankDto bankLiefDto, TheClientDto theClientDto) {
			LandDto lieferantLandDto = partnerDtoEmpfaenger.getLandplzortDto().getLandDto();
			IPaymentInfoChProducer producer = getProducerInland(erDto, zvDto, partnerDtoEmpfaenger, bvLieferantDto, bankLiefDto, lieferantLandDto);
			if (producer != null)
				return producer;
			
			producer = getProducerInlandFremdwaehrung(erDto, zvDto, partnerDtoEmpfaenger, bvLieferantDto, bankLiefDto, lieferantLandDto);
			if (producer != null)
				return producer;
			
			producer = getProducerAuslandSepa(erDto, zvDto, partnerDtoEmpfaenger, bvLieferantDto, bankLiefDto, lieferantLandDto);
			if (producer != null)
				return producer;
			
			producer = getProducerAusland(erDto, zvDto, partnerDtoEmpfaenger, bvLieferantDto, bankLiefDto, lieferantLandDto);
			if (producer != null)
				return producer;
			
			return null;
		}

		private boolean isInland(LandDto landDto) {
			return Helper.isOneOf(landDto.getCLkz().trim(), "CH", "LI");
		}
		
		private boolean isSepaLand(LandDto landDto) {
			return Helper.short2boolean(landDto.getBSepa());
		}
		
		private IPaymentInfoChProducer getProducerInland(EingangsrechnungDto erDto, ZahlungsvorschlagDto zvDto, PartnerDto partnerDtoEmpfaenger, 
				PartnerbankDto bvLieferantDto, BankDto bankLiefDto, LandDto landDto) {
			return Helper.isOneOf(erDto.getWaehrungCNr().trim(), "EUR", "CHF")
						&& isInland(landDto)
					? new PaymentInfoChProducerInland(erDto, zvDto, partnerDtoEmpfaenger, bvLieferantDto, bankLiefDto, landDto)
					: null;
		}

		private IPaymentInfoChProducer getProducerInlandFremdwaehrung(EingangsrechnungDto erDto, ZahlungsvorschlagDto zvDto, PartnerDto partnerDtoEmpfaenger, 
				PartnerbankDto bvLieferantDto, BankDto bankLiefDto, LandDto landDto) {
			return !Helper.isOneOf(erDto.getWaehrungCNr().trim(), "EUR", "CHF")
						&& isInland(landDto)
					? new PaymentInfoChProducerInlandFremdwaehrung(erDto, zvDto, partnerDtoEmpfaenger, bvLieferantDto, bankLiefDto, landDto)
					: null;
		}
		
		private IPaymentInfoChProducer getProducerAuslandSepa(EingangsrechnungDto erDto, ZahlungsvorschlagDto zvDto, PartnerDto partnerDtoEmpfaenger, 
				PartnerbankDto bvLieferantDto, BankDto bankLiefDto, LandDto landDto) {
			return erDto.getWaehrungCNr().trim().equals("EUR")
						&& !isInland(landDto)
						&& isSepaLand(landDto)
					? new PaymentInfoChProducerAuslandSepa(erDto, zvDto, partnerDtoEmpfaenger, bvLieferantDto, bankLiefDto, landDto)
					: null;
		}
		
		private IPaymentInfoChProducer getProducerAusland(EingangsrechnungDto erDto, ZahlungsvorschlagDto zvDto, PartnerDto partnerDtoEmpfaenger, 
				PartnerbankDto bvLieferantDto, BankDto bankLiefDto, LandDto landDto) {
			return !isInland(landDto)
						&& isSepaLand(landDto)
					? new PaymentInfoChProducerAusland(erDto, zvDto, partnerDtoEmpfaenger, bvLieferantDto, bankLiefDto, landDto)
					: null;
		}
	}
	
	public interface IPaymentInfoChProducer {
		PACHPaymentInstructionInformation3CH setupPayment(PACHPaymentInstructionInformation3CH pmtInf);
	}
	
	public class PaymentInfoChProducerBase implements IPaymentInfoChProducer {
		private EingangsrechnungDto erDto;
		private ZahlungsvorschlagDto zvDto;
		private PartnerDto partnerDtoEmpfaenger;
		private PartnerbankDto bvLieferantDto;
		private BankDto bankLieferantDto;
		
		public PaymentInfoChProducerBase(EingangsrechnungDto erDto, ZahlungsvorschlagDto zvDto, PartnerDto partnerDtoEmpfaenger, 
				PartnerbankDto bvLieferantDto, BankDto bankLiefDto, LandDto landDto) {
			this.erDto = erDto;
			this.zvDto = zvDto;
			this.partnerDtoEmpfaenger = partnerDtoEmpfaenger;
			this.bvLieferantDto = bvLieferantDto;
			this.bankLieferantDto = bankLiefDto;
		}
		
		protected EingangsrechnungDto erDto() {
			return erDto;
		}
		
		protected ZahlungsvorschlagDto zvDto() {
			return zvDto;
		}
		
		protected PartnerDto partnerDtoEmpfaenger() {
			return partnerDtoEmpfaenger;
		}
		
		protected PartnerbankDto bvLieferantDto() {
			return bvLieferantDto;
		}
		
		protected BankDto bankLieferantDto() {
			return bankLieferantDto;
		}
		
		@Override
		public PACHPaymentInstructionInformation3CH setupPayment(PACHPaymentInstructionInformation3CH pmtInf) {
			PACHCreditTransferTransactionInformation10CH transaction = objectFactory.createPACHCreditTransferTransactionInformation10CH();
			PACHPaymentIdentification1 pmtId = objectFactory.createPACHPaymentIdentification1();
			pmtId.setEndToEndId(getEndToEndId(zvDto));
			pmtId.setInstrId("INSTRID-1");
			transaction.setPmtId(pmtId);
			
			transaction.setAmt(createInstructedAmount(zvDto.getNZahlbetrag(), erDto.getWaehrungCNr()));
			transaction.setCdtrAgt(createFinancialInstitutionId(bankLieferantDto.getCBic()));
			transaction.setCdtr(createPartyIdentificationName(partnerDtoEmpfaenger));
			transaction.setCdtrAcct(createCashAccountId(bvLieferantDto.getCIban()));
			transaction.setRmtInf(createRemittanceInformation(erDto, zvDto));
			setupTransaction(transaction);
			
			pmtInf.getCdtTrfTxInf().add(transaction);
//			pmtInf.setCtrlSum(zvDto.getNZahlbetrag());
			pmtInf.setNbOfTxs("1");
			
			return pmtInf;
		}

		protected PACHCreditTransferTransactionInformation10CH setupTransaction(PACHCreditTransferTransactionInformation10CH transaction) {
			return transaction;
		}
		
		protected void fillPartyWithStructuredAddress(PACHPartyIdentification32CHName party, PartnerDto partnerDto) {
			if (partnerDto.getLandplzortDto() == null 
					|| partnerDto.getLandplzortDto().getLandDto() == null
					|| partnerDto.getLandplzortDto().getOrtDto() == null)
				return;

			PACHPostalAddress6CH address = objectFactory.createPACHPostalAddress6CH();
			address.setStrtNm(partnerDto.getCStrasse());
			address.setPstCd(partnerDto.getLandplzortDto().getCPlz());
			address.setTwnNm(partnerDto.getLandplzortDto().getOrtDto().getCName());
			address.setCtry(partnerDto.getLandplzortDto().getLandDto().getCLkz());
			party.setPstlAdr(address);
		}
		
		protected void fillPartyWithUnstructuredAddress(PACHPartyIdentification32CHName party, PartnerDto partnerDto) {
			if (partnerDto.getLandplzortDto() == null 
					|| partnerDto.getLandplzortDto().getLandDto() == null
					|| partnerDto.getLandplzortDto().getOrtDto() == null)
				return;

			PACHPostalAddress6CH address = objectFactory.createPACHPostalAddress6CH();
			address.getAdrLine().add(partnerDto.getCStrasse());
			address.getAdrLine().add(partnerDto.formatLKZPLZOrt());
			address.setCtry(partnerDto.getLandplzortDto().getLandDto().getCLkz());
			party.setPstlAdr(address);
		}
	}
	
	public class PaymentInfoChProducerInland extends PaymentInfoChProducerBase {
		
		public PaymentInfoChProducerInland(EingangsrechnungDto erDto, ZahlungsvorschlagDto zvDto, PartnerDto partnerDtoEmpfaenger, 
				PartnerbankDto bvLieferantDto, BankDto bankLiefDto, LandDto landDto) {
			super(erDto, zvDto, partnerDtoEmpfaenger, bvLieferantDto, bankLiefDto, landDto);
		}
		
		@Override
		public PACHPaymentInstructionInformation3CH setupPayment(PACHPaymentInstructionInformation3CH pmtInf) {
			return super.setupPayment(pmtInf);
		}
		
		@Override
		protected PACHCreditTransferTransactionInformation10CH setupTransaction(
				PACHCreditTransferTransactionInformation10CH transaction) {
			fillPartyWithStructuredAddress(transaction.getCdtr(), partnerDtoEmpfaenger());
			return super.setupTransaction(transaction);
		}
	}
	
	public class PaymentInfoChProducerInlandFremdwaehrung extends PaymentInfoChProducerBase {

		public PaymentInfoChProducerInlandFremdwaehrung(EingangsrechnungDto erDto, ZahlungsvorschlagDto zvDto, PartnerDto partnerDtoEmpfaenger, 
				PartnerbankDto bvLieferantDto, BankDto bankLiefDto, LandDto landDto) {
			super(erDto, zvDto, partnerDtoEmpfaenger, bvLieferantDto, bankLiefDto, landDto);
		}

		public PACHPaymentInstructionInformation3CH setupPayment(PACHPaymentInstructionInformation3CH pmtInf) {
			return super.setupPayment(pmtInf);
		}
		
		protected PACHCreditTransferTransactionInformation10CH setupTransaction(
				PACHCreditTransferTransactionInformation10CH transaction) {
			fillPartyWithStructuredAddress(transaction.getCdtr(), partnerDtoEmpfaenger());
			return super.setupTransaction(transaction);
		}
	}
	
	public class PaymentInfoChProducerAuslandSepa extends PaymentInfoChProducerBase {
		
		public PaymentInfoChProducerAuslandSepa(EingangsrechnungDto erDto, ZahlungsvorschlagDto zvDto, PartnerDto partnerDtoEmpfaenger, 
				PartnerbankDto bvLieferantDto, BankDto bankLiefDto, LandDto landDto) {
			super(erDto, zvDto, partnerDtoEmpfaenger, bvLieferantDto, bankLiefDto, landDto);
		}

		public PACHPaymentInstructionInformation3CH setupPayment(PACHPaymentInstructionInformation3CH pmtInf) {
			super.setupPayment(pmtInf);
			
			PACHServiceLevel8Choice svcLvl = objectFactory.createPACHServiceLevel8Choice();
			svcLvl.setCd(SERVICE_LEVEL_CODE_SEPA);
			PACHPaymentTypeInformation19CH pmtTpInf = objectFactory.createPACHPaymentTypeInformation19CH();
			pmtTpInf.setSvcLvl(svcLvl);
			
			pmtInf.setPmtTpInf(pmtTpInf);
			pmtInf.setChrgBr(PACHChargeBearerType1Code.SLEV);
			
			return pmtInf;
		}
		
		protected PACHCreditTransferTransactionInformation10CH setupTransaction(
				PACHCreditTransferTransactionInformation10CH transaction) {
			fillPartyWithUnstructuredAddress(transaction.getCdtr(), partnerDtoEmpfaenger());
			
			PACHCreditorReferenceInformation2 cdtrRefInf = transaction.getRmtInf().getStrd() != null
					? transaction.getRmtInf().getStrd().getCdtrRefInf()
					: null;
			if (cdtrRefInf != null) {
				PACHCreditorReferenceType1Choice typeChoice = objectFactory.createPACHCreditorReferenceType1Choice();
				typeChoice.setCd(PACHDocumentType3Code.SCOR);
				PACHCreditorReferenceType2 type = objectFactory.createPACHCreditorReferenceType2();
				type.setCdOrPrtry(typeChoice);
				cdtrRefInf.setTp(type);
			}
			
			return super.setupTransaction(transaction);
		}
	}
	
	public class PaymentInfoChProducerAusland extends PaymentInfoChProducerBase {
		public PaymentInfoChProducerAusland(EingangsrechnungDto erDto, ZahlungsvorschlagDto zvDto, PartnerDto partnerDtoEmpfaenger, 
				PartnerbankDto bvLieferantDto, BankDto bankLiefDto, LandDto landDto) {
			super(erDto, zvDto, partnerDtoEmpfaenger, bvLieferantDto, bankLiefDto, landDto);
		}
		
		protected PACHCreditTransferTransactionInformation10CH setupTransaction(
				PACHCreditTransferTransactionInformation10CH transaction) {
			fillPartyWithUnstructuredAddress(transaction.getCdtr(), partnerDtoEmpfaenger());
			PACHBranchAndFinancialInstitutionIdentification4CH cdtrAgt = transaction.getCdtrAgt();

			if (bankLieferantDto().getPartnerDto().getLandplzortDto() == null 
					|| bankLieferantDto().getPartnerDto().getLandplzortDto().getLandDto() == null
					|| bankLieferantDto().getPartnerDto().getLandplzortDto().getOrtDto() == null)
				return super.setupTransaction(transaction);

			PACHPostalAddress6CH address = objectFactory.createPACHPostalAddress6CH();
			address.getAdrLine().add(bankLieferantDto().getPartnerDto().getCStrasse());
			address.getAdrLine().add(bankLieferantDto().getPartnerDto().formatLKZPLZOrt());
			address.setCtry(bankLieferantDto().getPartnerDto().getLandplzortDto().getLandDto().getCLkz());
			cdtrAgt.getFinInstnId().setPstlAdr(address);
			
			return super.setupTransaction(transaction);
		}
	}
	
	@Override
	protected boolean isSepa() {
		return false;
	}
	
	@Override
	protected boolean isSwiss() {
		return true;
	}
}
