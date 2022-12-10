package com.lp.server.eingangsrechnung.bl;

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import com.lp.server.schema.iso20022.ch.pain001V03.PACHDocument;

public class SepaXmlMarshallerPain001V03CH extends SepaXmlMarshaller {
	
	String xsdSchemaV03CHpart1 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\r\n" + 
			"<!--\r\n" + 
			"(C) Copyright 2010, SKSF, www.sksf.ch\r\n" + 
			"CH Version fuer pain.001 Credit Transfer: xmlns=\"http://www.iso-payments.ch\"\r\n" + 
			".ch.:	            Identification for this CH version\r\n" + 
			"Last part (.02):   Version of this scheme\r\n" + 
			"\r\n" + 
			"Based on ISO pain.001.001.03 (urn:iso:std:iso:20022:tech:xsd:pain.001.001.03)\r\n" + 
			"\r\n" + 
			"Anregungen und Fragen zu diesem Dokument können an das jeweilige Finanzinstitut gerichtet werden.\r\n" + 
			"Allgemeine Anregungen können auch bei der SIX Interbank Clearing AG unter folgender Adresse angebracht werden:\r\n" + 
			"pm@six-group.com\r\n" + 
			"\r\n" + 
			"History\r\n" + 
			"15.02.2010  V01  initial version, targetNamespace=\"http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.01.xsd\"  File:pain.001.001.03.ch.01.xsd\r\n" + 
			"30.04.2010  V02  added: element Initiating Party/Contact Details contains Software name and Version of producing application\r\n" + 
			"						 changed: name in \"PartyIdentification32-CH_Name\" mandatory\r\n" + 
			"-->\r\n" + 
			"<!-- V01: changed:\r\n" + 
			"-->\r\n" + 
			"<xs:schema xmlns=\"http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" targetNamespace=\"http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd\" elementFormDefault=\"qualified\">\r\n" + 
			"	<xs:element name=\"Document\" type=\"Document\"/>\r\n" + 
			"	<!-- V01: changed: CH version changes applied -->\r\n" + 
			"	<xs:complexType name=\"AccountIdentification4Choice-CH\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:choice>\r\n" + 
			"				<xs:element name=\"IBAN\" type=\"IBAN2007Identifier\"/>\r\n" + 
			"				<xs:element name=\"Othr\" type=\"GenericAccountIdentification1-CH\"/>\r\n" + 
			"			</xs:choice>\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<!-- V01: unused\r\n" + 
			"	<xs:complexType name=\"AccountSchemeName1Choice\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:choice>\r\n" + 
			"				<xs:element name=\"Cd\" type=\"ExternalAccountIdentification1Code\"/>\r\n" + 
			"				<xs:element name=\"Prtry\" type=\"Max35Text\"/>\r\n" + 
			"			</xs:choice>\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"    -->\r\n" + 
			"	<xs:simpleType name=\"ActiveOrHistoricCurrencyAndAmount_SimpleType\">\r\n" + 
			"		<xs:restriction base=\"xs:decimal\">\r\n" + 
			"			<xs:minInclusive value=\"0\"/>\r\n" + 
			"			<xs:fractionDigits value=\"5\"/>\r\n" + 
			"			<xs:totalDigits value=\"18\"/>\r\n" + 
			"		</xs:restriction>\r\n" + 
			"	</xs:simpleType>\r\n" + 
			"	<xs:complexType name=\"ActiveOrHistoricCurrencyAndAmount\">\r\n" + 
			"		<xs:simpleContent>\r\n" + 
			"			<xs:extension base=\"ActiveOrHistoricCurrencyAndAmount_SimpleType\">\r\n" + 
			"				<xs:attribute name=\"Ccy\" type=\"ActiveOrHistoricCurrencyCode\" use=\"required\"/>\r\n" + 
			"			</xs:extension>\r\n" + 
			"		</xs:simpleContent>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<xs:simpleType name=\"ActiveOrHistoricCurrencyCode\">\r\n" + 
			"		<xs:restriction base=\"xs:string\">\r\n" + 
			"			<xs:pattern value=\"[A-Z]{3,3}\"/>\r\n" + 
			"		</xs:restriction>\r\n" + 
			"	</xs:simpleType>\r\n" + 
			"	<xs:simpleType name=\"AddressType2Code\">\r\n" + 
			"		<xs:restriction base=\"xs:string\">\r\n" + 
			"			<xs:enumeration value=\"ADDR\"/>\r\n" + 
			"			<xs:enumeration value=\"PBOX\"/>\r\n" + 
			"			<xs:enumeration value=\"HOME\"/>\r\n" + 
			"			<xs:enumeration value=\"BIZZ\"/>\r\n" + 
			"			<xs:enumeration value=\"MLTO\"/>\r\n" + 
			"			<xs:enumeration value=\"DLVY\"/>\r\n" + 
			"		</xs:restriction>\r\n" + 
			"	</xs:simpleType>\r\n" + 
			"	<xs:complexType name=\"AmountType3Choice\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:choice>\r\n" + 
			"				<xs:element name=\"InstdAmt\" type=\"ActiveOrHistoricCurrencyAndAmount\"/>\r\n" + 
			"				<xs:element name=\"EqvtAmt\" type=\"EquivalentAmount2\"/>\r\n" + 
			"			</xs:choice>\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<xs:simpleType name=\"AnyBICIdentifier\">\r\n" + 
			"		<xs:restriction base=\"xs:string\">\r\n" + 
			"			<xs:pattern value=\"[A-Z]{6,6}[A-Z2-9][A-NP-Z0-9]([A-Z0-9]{3,3}){0,1}\"/>\r\n" + 
			"		</xs:restriction>\r\n" + 
			"	</xs:simpleType>\r\n" + 
			"	<!-- V01: unused: type Authorisation1Choice is not allowed or used in CH Version\r\n" + 
			"	<xs:complexType name=\"Authorisation1Choice\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:choice>\r\n" + 
			"				<xs:element name=\"Cd\" type=\"Authorisation1Code\"/>\r\n" + 
			"				<xs:element name=\"Prtry\" type=\"Max128Text\"/>\r\n" + 
			"			</xs:choice>\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<xs:simpleType name=\"Authorisation1Code\">\r\n" + 
			"		<xs:restriction base=\"xs:string\">\r\n" + 
			"			<xs:enumeration value=\"AUTH\"/>\r\n" + 
			"			<xs:enumeration value=\"FDET\"/>\r\n" + 
			"			<xs:enumeration value=\"FSUM\"/>\r\n" + 
			"			<xs:enumeration value=\"ILEV\"/>\r\n" + 
			"		</xs:restriction>\r\n" + 
			"	</xs:simpleType>\r\n" + 
			"	-->\r\n" + 
			"	<!-- V01: added: CH version supports only this character set. All text fields use this type -->\r\n" + 
			"	<xs:simpleType name=\"BasicText-CH\">\r\n" + 
			"		<xs:restriction base=\"xs:string\">\r\n" + 
			"			<xs:pattern value=\"([a-zA-Z0-9\\.,;:'\\+\\-/\\(\\)?\\*\\[\\]\\{\\}\\\\`´~ ]|[!&quot;#%&amp;&lt;&gt;÷=@_$£]|[àáâäçèéêëìíîïñòóôöùúûüýßÀÁÂÄÇÈÉÊËÌÍÎÏÒÓÔÖÙÚÛÜÑ])*\"/>\r\n" + 
			"		</xs:restriction>\r\n" + 
			"	</xs:simpleType>\r\n" + 
			"	<!-- V01: added: This is the SWIFT character set -->\r\n" + 
			"	<xs:simpleType name=\"BasicText-Swift\">\r\n" + 
			"		<xs:restriction base=\"xs:string\">\r\n" + 
			"			<xs:pattern value=\"([A-Za-z0-9]|[+|\\?|/|\\-|:|\\(|\\)|\\.|,|'|\\p{Zs}])*\"/>\r\n" + 
			"		</xs:restriction>\r\n" + 
			"	</xs:simpleType>\r\n" + 
			"	<xs:simpleType name=\"BICIdentifier\">\r\n" + 
			"		<xs:restriction base=\"xs:string\">\r\n" + 
			"			<xs:pattern value=\"[A-Z]{6,6}[A-Z2-9][A-NP-Z0-9]([A-Z0-9]{3,3}){0,1}\"/>\r\n" + 
			"		</xs:restriction>\r\n" + 
			"	</xs:simpleType>\r\n" + 
			"	<xs:simpleType name=\"BaseOneRate\">\r\n" + 
			"		<xs:restriction base=\"xs:decimal\">\r\n" + 
			"			<xs:fractionDigits value=\"10\"/>\r\n" + 
			"			<xs:totalDigits value=\"11\"/>\r\n" + 
			"		</xs:restriction>\r\n" + 
			"	</xs:simpleType>\r\n" + 
			"	<xs:simpleType name=\"BatchBookingIndicator\">\r\n" + 
			"		<xs:restriction base=\"xs:boolean\"/>\r\n" + 
			"	</xs:simpleType>\r\n" + 
			"	<xs:complexType name=\"BranchAndFinancialInstitutionIdentification4\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"FinInstnId\" type=\"FinancialInstitutionIdentification7\"/>\r\n" + 
			"			<xs:element name=\"BrnchId\" type=\"BranchData2\" minOccurs=\"0\"/>\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<!-- VO1: added: definition of FI where only BIC or Clearing Id is allowed, but no branch data -->\r\n" + 
			"	<xs:complexType name=\"BranchAndFinancialInstitutionIdentification4-CH_BicOrClrId\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"FinInstnId\" type=\"FinancialInstitutionIdentification7-CH_BicOrClrId\"/>\r\n" + 
			"			<!-- V01: unused\r\n" + 
			"			<xs:element name=\"BrnchId\" type=\"BranchData2\" minOccurs=\"0\"/>\r\n" + 
			"			-->\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<!-- VO1: added: definition of FI where all elements are allowed, but no branch data -->\r\n" + 
			"	<xs:complexType name=\"BranchAndFinancialInstitutionIdentification4-CH\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"FinInstnId\" type=\"FinancialInstitutionIdentification7-CH\"/>\r\n" + 
			"			<!-- V01: unused\r\n" + 
			"			<xs:element name=\"BrnchId\" type=\"BranchData2\" minOccurs=\"0\"/>\r\n" + 
			"			-->\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<xs:complexType name=\"BranchData2\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"Id\" type=\"Max35Text\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"Nm\" type=\"Max140Text\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"PstlAdr\" type=\"PostalAddress6\" minOccurs=\"0\"/>\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<!-- V01: changed: CH version changes applied  -->\r\n" + 
			"	<xs:complexType name=\"CashAccount16-CH_IdAndCurrency\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"Id\" type=\"AccountIdentification4Choice-CH\"/>\r\n" + 
			"			<xs:element name=\"Ccy\" type=\"ActiveOrHistoricCurrencyCode\" minOccurs=\"0\"/>\r\n" + 
			"			<!-- V01: unused\r\n" + 
			"			<xs:element name=\"Tp\" type=\"CashAccountType2\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"Nm\" type=\"Max70Text\" minOccurs=\"0\"/>\r\n" + 
			"			-->\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<!-- V01: added -->\r\n" + 
			"	<xs:complexType name=\"CashAccount16-CH_IdTpCcy\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"Id\" type=\"AccountIdentification4Choice-CH\"/>\r\n" + 
			"			<xs:element name=\"Tp\" type=\"CashAccountType2\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"Ccy\" type=\"ActiveOrHistoricCurrencyCode\" minOccurs=\"0\"/>\r\n" + 
			"			<!-- V01: unused\r\n" + 
			"			<xs:element name=\"Nm\" type=\"Max70Text\" minOccurs=\"0\"/>\r\n" + 
			"			-->\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<!-- V01: added -->\r\n" + 
			"	<xs:complexType name=\"CashAccount16-CH_Id\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"Id\" type=\"AccountIdentification4Choice-CH\"/>\r\n" + 
			"			<!-- V01: unused\r\n" + 
			"			<xs:element name=\"Tp\" type=\"CashAccountType2\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"Ccy\" type=\"ActiveOrHistoricCurrencyCode\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"Nm\" type=\"Max70Text\" minOccurs=\"0\"/>\r\n" + 
			"			-->\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<xs:complexType name=\"CashAccountType2\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:choice>\r\n" + 
			"				<xs:element name=\"Cd\" type=\"CashAccountType4Code\"/>\r\n" + 
			"				<xs:element name=\"Prtry\" type=\"Max35Text\"/>\r\n" + 
			"			</xs:choice>\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<xs:simpleType name=\"CashAccountType4Code\">\r\n" + 
			"		<xs:restriction base=\"xs:string\">\r\n" + 
			"			<xs:enumeration value=\"CASH\"/>\r\n" + 
			"			<xs:enumeration value=\"CHAR\"/>\r\n" + 
			"			<xs:enumeration value=\"COMM\"/>\r\n" + 
			"			<xs:enumeration value=\"TAXE\"/>\r\n" + 
			"			<xs:enumeration value=\"CISH\"/>\r\n" + 
			"			<xs:enumeration value=\"TRAS\"/>\r\n" + 
			"			<xs:enumeration value=\"SACC\"/>\r\n" + 
			"			<xs:enumeration value=\"CACC\"/>\r\n" + 
			"			<xs:enumeration value=\"SVGS\"/>\r\n" + 
			"			<xs:enumeration value=\"ONDP\"/>\r\n" + 
			"			<xs:enumeration value=\"MGLD\"/>\r\n" + 
			"			<xs:enumeration value=\"NREX\"/>\r\n" + 
			"			<xs:enumeration value=\"MOMA\"/>\r\n" + 
			"			<xs:enumeration value=\"LOAN\"/>\r\n" + 
			"			<xs:enumeration value=\"SLRY\"/>\r\n" + 
			"			<xs:enumeration value=\"ODFT\"/>\r\n" + 
			"		</xs:restriction>\r\n" + 
			"	</xs:simpleType>\r\n" + 
			"	<!-- V01: changed: Only element Code allowed in Ch version -->\r\n" + 
			"	<xs:complexType name=\"CategoryPurpose1-CH_Code\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"Cd\" type=\"ExternalCategoryPurpose1Code\"/>\r\n" + 
			"			<!-- V01: unused\r\n" + 
			"				<xs:element name=\"Prtry\" type=\"Max35Text\"/>\r\n" + 
			"				-->\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<!-- -->\r\n" + 
			"	<xs:simpleType name=\"ChargeBearerType1Code\">\r\n" + 
			"		<xs:restriction base=\"xs:string\">\r\n" + 
			"			<xs:enumeration value=\"DEBT\"/>\r\n" + 
			"			<xs:enumeration value=\"CRED\"/>\r\n" + 
			"			<xs:enumeration value=\"SHAR\"/>\r\n" + 
			"			<xs:enumeration value=\"SLEV\"/>\r\n" + 
			"		</xs:restriction>\r\n" + 
			"	</xs:simpleType>\r\n" + 
			"	<!-- V01: unused\r\n" + 
			"	<xs:complexType name=\"Cheque6\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"ChqTp\" type=\"ChequeType2Code\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"ChqNb\" type=\"Max35Text\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"ChqFr\" type=\"NameAndAddress10\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"DlvryMtd\" type=\"ChequeDeliveryMethod1Choice\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"DlvrTo\" type=\"NameAndAddress10\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"InstrPrty\" type=\"Priority2Code\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"ChqMtrtyDt\" type=\"ISODate\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"FrmsCd\" type=\"Max35Text\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"MemoFld\" type=\"Max35Text\" minOccurs=\"0\" maxOccurs=\"2\"/>\r\n" + 
			"			<xs:element name=\"RgnlClrZone\" type=\"Max35Text\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"PrtLctn\" type=\"Max35Text\" minOccurs=\"0\"/>\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"    -->\r\n" + 
			"	<!-- V01: added -->\r\n" + 
			"	<xs:complexType name=\"Cheque6-CH\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"ChqTp\" type=\"ChequeType2Code\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"DlvryMtd\" type=\"ChequeDeliveryMethod1Choice\" minOccurs=\"0\"/>\r\n" + 
			"			<!-- V01: unused\r\n" + 
			"			<xs:element name=\"ChqNb\" type=\"Max35Text\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"ChqFr\" type=\"NameAndAddress10\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"DlvrTo\" type=\"NameAndAddress10\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"InstrPrty\" type=\"Priority2Code\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"ChqMtrtyDt\" type=\"ISODate\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"FrmsCd\" type=\"Max35Text\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"MemoFld\" type=\"Max35Text\" minOccurs=\"0\" maxOccurs=\"2\"/>\r\n" + 
			"			<xs:element name=\"RgnlClrZone\" type=\"Max35Text\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"PrtLctn\" type=\"Max35Text\" minOccurs=\"0\"/>\r\n" + 
			"			-->\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<xs:simpleType name=\"ChequeDelivery1Code\">\r\n" + 
			"		<xs:restriction base=\"xs:string\">\r\n" + 
			"			<xs:enumeration value=\"MLDB\"/>\r\n" + 
			"			<xs:enumeration value=\"MLCD\"/>\r\n" + 
			"			<xs:enumeration value=\"MLFA\"/>\r\n" + 
			"			<xs:enumeration value=\"CRDB\"/>\r\n" + 
			"			<xs:enumeration value=\"CRCD\"/>\r\n" + 
			"			<xs:enumeration value=\"CRFA\"/>\r\n" + 
			"			<xs:enumeration value=\"PUDB\"/>\r\n" + 
			"			<xs:enumeration value=\"PUCD\"/>\r\n" + 
			"			<xs:enumeration value=\"PUFA\"/>\r\n" + 
			"			<xs:enumeration value=\"RGDB\"/>\r\n" + 
			"			<xs:enumeration value=\"RGCD\"/>\r\n" + 
			"			<xs:enumeration value=\"RGFA\"/>\r\n" + 
			"		</xs:restriction>\r\n" + 
			"	</xs:simpleType>\r\n" + 
			"	<xs:complexType name=\"ChequeDeliveryMethod1Choice\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:choice>\r\n" + 
			"				<xs:element name=\"Cd\" type=\"ChequeDelivery1Code\"/>\r\n" + 
			"				<xs:element name=\"Prtry\" type=\"Max35Text\"/>\r\n" + 
			"			</xs:choice>\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<xs:simpleType name=\"ChequeType2Code\">\r\n" + 
			"		<xs:restriction base=\"xs:string\">\r\n" + 
			"			<xs:enumeration value=\"CCHQ\"/>\r\n" + 
			"			<xs:enumeration value=\"CCCH\"/>\r\n" + 
			"			<xs:enumeration value=\"BCHQ\"/>\r\n" + 
			"			<xs:enumeration value=\"DRFT\"/>\r\n" + 
			"			<xs:enumeration value=\"ELDR\"/>\r\n" + 
			"		</xs:restriction>\r\n" + 
			"	</xs:simpleType>\r\n" + 
			"	<xs:complexType name=\"ClearingSystemIdentification2Choice\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:choice>\r\n" + 
			"				<xs:element name=\"Cd\" type=\"ExternalClearingSystemIdentification1Code\"/>\r\n" + 
			"				<xs:element name=\"Prtry\" type=\"Max35Text\"/>\r\n" + 
			"			</xs:choice>\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<xs:complexType name=\"ClearingSystemMemberIdentification2\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"ClrSysId\" type=\"ClearingSystemIdentification2Choice\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"MmbId\" type=\"Max35Text\"/>\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<xs:complexType name=\"ContactDetails2\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"NmPrfx\" type=\"NamePrefix1Code\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"Nm\" type=\"Max140Text\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"PhneNb\" type=\"PhoneNumber\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"MobNb\" type=\"PhoneNumber\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"FaxNb\" type=\"PhoneNumber\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"EmailAdr\" type=\"Max2048Text\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"Othr\" type=\"Max35Text\" minOccurs=\"0\"/>\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<!-- V02: changed: include Contact Details for Software name and version -->\r\n" + 
			"	<xs:complexType name=\"ContactDetails2-CH\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"Nm\" type=\"Max70Text\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"Othr\" type=\"Max35Text\" minOccurs=\"0\"/>\r\n" + 
			"			<!-- V02: unused\r\n" + 
			"			<xs:element name=\"NmPrfx\" type=\"NamePrefix1Code\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"PhneNb\" type=\"PhoneNumber\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"MobNb\" type=\"PhoneNumber\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"FaxNb\" type=\"PhoneNumber\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"EmailAdr\" type=\"Max2048Text\" minOccurs=\"0\"/>\r\n" + 
			"			-->\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<xs:simpleType name=\"CountryCode\">\r\n" + 
			"		<xs:restriction base=\"xs:string\">\r\n" + 
			"			<xs:pattern value=\"[A-Z]{2,2}\"/>\r\n" + 
			"		</xs:restriction>\r\n" + 
			"	</xs:simpleType>\r\n" + 
			"	<xs:simpleType name=\"CreditDebitCode\">\r\n" + 
			"		<xs:restriction base=\"xs:string\">\r\n" + 
			"			<xs:enumeration value=\"CRDT\"/>\r\n" + 
			"			<xs:enumeration value=\"DBIT\"/>\r\n" + 
			"		</xs:restriction>\r\n" + 
			"	</xs:simpleType>\r\n" + 
			"	<xs:complexType name=\"CreditTransferTransactionInformation10-CH\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"PmtId\" type=\"PaymentIdentification1\"/>\r\n" + 
			"			<xs:element name=\"PmtTpInf\" type=\"PaymentTypeInformation19-CH\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"Amt\" type=\"AmountType3Choice\"/>\r\n" + 
			"			<xs:element name=\"XchgRateInf\" type=\"ExchangeRateInformation1\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"ChrgBr\" type=\"ChargeBearerType1Code\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"ChqInstr\" type=\"Cheque6-CH\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"UltmtDbtr\" type=\"PartyIdentification32-CH\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"IntrmyAgt1\" type=\"BranchAndFinancialInstitutionIdentification4-CH\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"CdtrAgt\" type=\"BranchAndFinancialInstitutionIdentification4-CH\" minOccurs=\"0\"/>\r\n" + 
			"			<!-- V02: changed:  element Name mandatory -->\r\n" + 
			"			<xs:element name=\"Cdtr\" type=\"PartyIdentification32-CH_Name\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"CdtrAcct\" type=\"CashAccount16-CH_Id\" minOccurs=\"0\"/>\r\n" + 
			"			<!-- V02: changed:  element Name mandatory -->\r\n" + 
			"			<xs:element name=\"UltmtCdtr\" type=\"PartyIdentification32-CH_Name\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"InstrForCdtrAgt\" type=\"InstructionForCreditorAgent1\" minOccurs=\"0\" maxOccurs=\"unbounded\"/>\r\n" + 
			"			<xs:element name=\"InstrForDbtrAgt\" type=\"Max140Text\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"Purp\" type=\"Purpose2-CH_Code\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"RgltryRptg\" type=\"RegulatoryReporting3\" minOccurs=\"0\" maxOccurs=\"10\"/>\r\n" + 
			"			<xs:element name=\"RmtInf\" type=\"RemittanceInformation5-CH\" minOccurs=\"0\"/>\r\n" + 
			"			<!-- V01: usused\r\n" + 
			"			<xs:element name=\"IntrmyAgt1Acct\" type=\"CashAccount16\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"IntrmyAgt2\" type=\"BranchAndFinancialInstitutionIdentification4\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"IntrmyAgt2Acct\" type=\"CashAccount16\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"IntrmyAgt3\" type=\"BranchAndFinancialInstitutionIdentification4\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"IntrmyAgt3Acct\" type=\"CashAccount16\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"CdtrAgtAcct\" type=\"CashAccount16-CH_Id\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"Tax\" type=\"TaxInformation3\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"RltdRmtInf\" type=\"RemittanceLocation2\" minOccurs=\"0\" maxOccurs=\"10\"/>\r\n" + 
			"			-->\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<xs:complexType name=\"CreditorReferenceInformation2\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"Tp\" type=\"CreditorReferenceType2\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"Ref\" type=\"Max35Text\" minOccurs=\"0\"/>\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<xs:complexType name=\"CreditorReferenceType1Choice\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:choice>\r\n" + 
			"				<xs:element name=\"Cd\" type=\"DocumentType3Code\"/>\r\n" + 
			"				<xs:element name=\"Prtry\" type=\"Max35Text\"/>\r\n" + 
			"			</xs:choice>\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<xs:complexType name=\"CreditorReferenceType2\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"CdOrPrtry\" type=\"CreditorReferenceType1Choice\"/>\r\n" + 
			"			<xs:element name=\"Issr\" type=\"Max35Text\" minOccurs=\"0\"/>\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<xs:complexType name=\"CustomerCreditTransferInitiationV03-CH\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"GrpHdr\" type=\"GroupHeader32-CH\"/>\r\n" + 
			"			<xs:element name=\"PmtInf\" type=\"PaymentInstructionInformation3-CH\" maxOccurs=\"unbounded\"/>\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<xs:complexType name=\"DateAndPlaceOfBirth\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"BirthDt\" type=\"ISODate\"/>\r\n" + 
			"			<xs:element name=\"PrvcOfBirth\" type=\"Max35Text\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"CityOfBirth\" type=\"Max35Text\"/>\r\n" + 
			"			<xs:element name=\"CtryOfBirth\" type=\"CountryCode\"/>\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<!-- V01: unused\r\n" + 
			"	<xs:complexType name=\"DatePeriodDetails\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"FrDt\" type=\"ISODate\"/>\r\n" + 
			"			<xs:element name=\"ToDt\" type=\"ISODate\"/>\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"    -->\r\n" + 
			"	<xs:simpleType name=\"DecimalNumber\">\r\n" + 
			"		<xs:restriction base=\"xs:decimal\">\r\n" + 
			"			<xs:fractionDigits value=\"17\"/>\r\n" + 
			"			<xs:totalDigits value=\"18\"/>\r\n" + 
			"		</xs:restriction>\r\n" + 
			"	</xs:simpleType>\r\n" + 
			"	<xs:complexType name=\"Document\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"CstmrCdtTrfInitn\" type=\"CustomerCreditTransferInitiationV03-CH\"/>\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<xs:complexType name=\"DocumentAdjustment1\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"Amt\" type=\"ActiveOrHistoricCurrencyAndAmount\"/>\r\n" + 
			"			<xs:element name=\"CdtDbtInd\" type=\"CreditDebitCode\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"Rsn\" type=\"Max4Text\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"AddtlInf\" type=\"Max140Text\" minOccurs=\"0\"/>\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<xs:simpleType name=\"DocumentType3Code\">\r\n" + 
			"		<xs:restriction base=\"xs:string\">\r\n" + 
			"			<xs:enumeration value=\"RADM\"/>\r\n" + 
			"			<xs:enumeration value=\"RPIN\"/>\r\n" + 
			"			<xs:enumeration value=\"FXDR\"/>\r\n" + 
			"			<xs:enumeration value=\"DISP\"/>\r\n" + 
			"			<xs:enumeration value=\"PUOR\"/>\r\n" + 
			"			<xs:enumeration value=\"SCOR\"/>\r\n" + 
			"		</xs:restriction>\r\n" + 
			"	</xs:simpleType>\r\n" + 
			"	<xs:simpleType name=\"DocumentType5Code\">\r\n" + 
			"		<xs:restriction base=\"xs:string\">\r\n" + 
			"			<xs:enumeration value=\"MSIN\"/>\r\n" + 
			"			<xs:enumeration value=\"CNFA\"/>\r\n" + 
			"			<xs:enumeration value=\"DNFA\"/>\r\n" + 
			"			<xs:enumeration value=\"CINV\"/>\r\n" + 
			"			<xs:enumeration value=\"CREN\"/>\r\n" + 
			"			<xs:enumeration value=\"DEBN\"/>\r\n" + 
			"			<xs:enumeration value=\"HIRI\"/>\r\n" + 
			"			<xs:enumeration value=\"SBIN\"/>\r\n" + 
			"			<xs:enumeration value=\"CMCN\"/>\r\n" + 
			"			<xs:enumeration value=\"SOAC\"/>\r\n" + 
			"			<xs:enumeration value=\"DISP\"/>\r\n" + 
			"			<xs:enumeration value=\"BOLD\"/>\r\n" + 
			"			<xs:enumeration value=\"VCHR\"/>\r\n" + 
			"			<xs:enumeration value=\"AROI\"/>\r\n" + 
			"			<xs:enumeration value=\"TSUT\"/>\r\n" + 
			"		</xs:restriction>\r\n" + 
			"	</xs:simpleType>\r\n" + 
			"	<xs:complexType name=\"EquivalentAmount2\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"Amt\" type=\"ActiveOrHistoricCurrencyAndAmount\"/>\r\n" + 
			"			<xs:element name=\"CcyOfTrf\" type=\"ActiveOrHistoricCurrencyCode\"/>\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<xs:complexType name=\"ExchangeRateInformation1\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"XchgRate\" type=\"BaseOneRate\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"RateTp\" type=\"ExchangeRateType1Code\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"CtrctId\" type=\"Max35Text\" minOccurs=\"0\"/>\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n"; 
		String xsdSchemaV03CHpart2 =	"	<xs:simpleType name=\"ExchangeRateType1Code\">\r\n" + 
			"		<xs:restriction base=\"xs:string\">\r\n" + 
			"			<xs:enumeration value=\"SPOT\"/>\r\n" + 
			"			<xs:enumeration value=\"SALE\"/>\r\n" + 
			"			<xs:enumeration value=\"AGRD\"/>\r\n" + 
			"		</xs:restriction>\r\n" + 
			"	</xs:simpleType>\r\n" + 
			"	<!-- V01:  unused:\r\n" + 
			"	<xs:simpleType name=\"ExternalAccountIdentification1Code\">\r\n" + 
			"		<xs:restriction base=\"xs:string\">\r\n" + 
			"			<xs:minLength value=\"1\"/>\r\n" + 
			"			<xs:maxLength value=\"4\"/>\r\n" + 
			"		</xs:restriction>\r\n" + 
			"	</xs:simpleType>\r\n" + 
			"    -->\r\n" + 
			"	<xs:simpleType name=\"ExternalCategoryPurpose1Code\">\r\n" + 
			"		<xs:restriction base=\"xs:string\">\r\n" + 
			"			<xs:minLength value=\"1\"/>\r\n" + 
			"			<xs:maxLength value=\"4\"/>\r\n" + 
			"		</xs:restriction>\r\n" + 
			"	</xs:simpleType>\r\n" + 
			"	<xs:simpleType name=\"ExternalClearingSystemIdentification1Code\">\r\n" + 
			"		<xs:restriction base=\"xs:string\">\r\n" + 
			"			<xs:minLength value=\"1\"/>\r\n" + 
			"			<xs:maxLength value=\"5\"/>\r\n" + 
			"		</xs:restriction>\r\n" + 
			"	</xs:simpleType>\r\n" + 
			"	<xs:simpleType name=\"ExternalFinancialInstitutionIdentification1Code\">\r\n" + 
			"		<xs:restriction base=\"xs:string\">\r\n" + 
			"			<xs:minLength value=\"1\"/>\r\n" + 
			"			<xs:maxLength value=\"4\"/>\r\n" + 
			"		</xs:restriction>\r\n" + 
			"	</xs:simpleType>\r\n" + 
			"	<xs:simpleType name=\"ExternalLocalInstrument1Code\">\r\n" + 
			"		<xs:restriction base=\"xs:string\">\r\n" + 
			"			<xs:minLength value=\"1\"/>\r\n" + 
			"			<xs:maxLength value=\"35\"/>\r\n" + 
			"		</xs:restriction>\r\n" + 
			"	</xs:simpleType>\r\n" + 
			"	<xs:simpleType name=\"ExternalOrganisationIdentification1Code\">\r\n" + 
			"		<xs:restriction base=\"xs:string\">\r\n" + 
			"			<xs:minLength value=\"1\"/>\r\n" + 
			"			<xs:maxLength value=\"4\"/>\r\n" + 
			"		</xs:restriction>\r\n" + 
			"	</xs:simpleType>\r\n" + 
			"	<xs:simpleType name=\"ExternalPersonIdentification1Code\">\r\n" + 
			"		<xs:restriction base=\"xs:string\">\r\n" + 
			"			<xs:minLength value=\"1\"/>\r\n" + 
			"			<xs:maxLength value=\"4\"/>\r\n" + 
			"		</xs:restriction>\r\n" + 
			"	</xs:simpleType>\r\n" + 
			"	<xs:simpleType name=\"ExternalPurpose1Code\">\r\n" + 
			"		<xs:restriction base=\"xs:string\">\r\n" + 
			"			<xs:minLength value=\"1\"/>\r\n" + 
			"			<xs:maxLength value=\"4\"/>\r\n" + 
			"		</xs:restriction>\r\n" + 
			"	</xs:simpleType>\r\n" + 
			"	<xs:simpleType name=\"ExternalServiceLevel1Code\">\r\n" + 
			"		<xs:restriction base=\"xs:string\">\r\n" + 
			"			<xs:minLength value=\"1\"/>\r\n" + 
			"			<xs:maxLength value=\"4\"/>\r\n" + 
			"		</xs:restriction>\r\n" + 
			"	</xs:simpleType>\r\n" + 
			"	<xs:complexType name=\"FinancialIdentificationSchemeName1Choice\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:choice>\r\n" + 
			"				<xs:element name=\"Cd\" type=\"ExternalFinancialInstitutionIdentification1Code\"/>\r\n" + 
			"				<xs:element name=\"Prtry\" type=\"Max35Text\"/>\r\n" + 
			"			</xs:choice>\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<xs:complexType name=\"FinancialInstitutionIdentification7\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"BIC\" type=\"BICIdentifier\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"ClrSysMmbId\" type=\"ClearingSystemMemberIdentification2\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"Nm\" type=\"Max140Text\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"PstlAdr\" type=\"PostalAddress6\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"Othr\" type=\"GenericFinancialIdentification1\" minOccurs=\"0\"/>\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<!-- V01: added: definition of FI where only BIC or Clearing Id is allowed -->\r\n" + 
			"	<xs:complexType name=\"FinancialInstitutionIdentification7-CH_BicOrClrId\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"BIC\" type=\"BICIdentifier\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"ClrSysMmbId\" type=\"ClearingSystemMemberIdentification2\" minOccurs=\"0\"/>\r\n" + 
			"			<!-- V01: unused\r\n" + 
			"			<xs:element name=\"Nm\" type=\"Max140Text\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"PstlAdr\" type=\"PostalAddress6\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"Othr\" type=\"GenericFinancialIdentification1\" minOccurs=\"0\"/>\r\n" + 
			"			-->\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<!-- V01: added: definition of FI where all elements are allowed (in a CH version) -->\r\n" + 
			"	<xs:complexType name=\"FinancialInstitutionIdentification7-CH\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"BIC\" type=\"BICIdentifier\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"ClrSysMmbId\" type=\"ClearingSystemMemberIdentification2\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"Nm\" type=\"Max70Text\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"PstlAdr\" type=\"PostalAddress6-CH\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"Othr\" type=\"GenericFinancialIdentification1-CH\" minOccurs=\"0\"/>\r\n" + 
			"			<!-- V01: unused\r\n" + 
			"			-->\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<!-- V01: changed: only element ID allowed in CH version -->\r\n" + 
			"	<xs:complexType name=\"GenericAccountIdentification1-CH\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"Id\" type=\"Max34Text\"/>\r\n" + 
			"			<!-- V01: unused\r\n" + 
			"			<xs:element name=\"SchmeNm\" type=\"AccountSchemeName1Choice\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"Issr\" type=\"Max35Text\" minOccurs=\"0\"/>\r\n" + 
			"			-->\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<xs:complexType name=\"GenericFinancialIdentification1\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"Id\" type=\"Max35Text\"/>\r\n" + 
			"			<xs:element name=\"SchmeNm\" type=\"FinancialIdentificationSchemeName1Choice\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"Issr\" type=\"Max35Text\" minOccurs=\"0\"/>\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<!-- V01: added: only element Id allowed in CH version -->\r\n" + 
			"	<xs:complexType name=\"GenericFinancialIdentification1-CH\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"Id\" type=\"Max35Text\"/>\r\n" + 
			"			<!-- V01: unused\r\n" + 
			"			<xs:element name=\"SchmeNm\" type=\"FinancialIdentificationSchemeName1Choice\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"Issr\" type=\"Max35Text\" minOccurs=\"0\"/>\r\n" + 
			"			-->\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<xs:complexType name=\"GenericOrganisationIdentification1\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"Id\" type=\"Max35Text\"/>\r\n" + 
			"			<xs:element name=\"SchmeNm\" type=\"OrganisationIdentificationSchemeName1Choice\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"Issr\" type=\"Max35Text\" minOccurs=\"0\"/>\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<xs:complexType name=\"GenericPersonIdentification1\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"Id\" type=\"Max35Text\"/>\r\n" + 
			"			<xs:element name=\"SchmeNm\" type=\"PersonIdentificationSchemeName1Choice\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"Issr\" type=\"Max35Text\" minOccurs=\"0\"/>\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<xs:complexType name=\"GroupHeader32-CH\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"MsgId\" type=\"Max35Text-Swift\"/>\r\n" + 
			"			<xs:element name=\"CreDtTm\" type=\"ISODateTime\"/>\r\n" + 
			"			<xs:element name=\"NbOfTxs\" type=\"Max15NumericText\"/>\r\n" + 
			"			<xs:element name=\"CtrlSum\" type=\"DecimalNumber\" minOccurs=\"0\"/>\r\n" + 
			"			<!-- V02: changed: include Contact Details for Software name and version -->\r\n" + 
			"			<xs:element name=\"InitgPty\" type=\"PartyIdentification32-CH_NameAndId\"/>\r\n" + 
			"			<xs:element name=\"FwdgAgt\" type=\"BranchAndFinancialInstitutionIdentification4\" minOccurs=\"0\"/>\r\n" + 
			"			<!-- V01: unused: type Authorisation1Choice is not allowed or used in CH Version\r\n" + 
			"			<xs:element name=\"Authstn\" type=\"Authorisation1Choice\" minOccurs=\"0\" maxOccurs=\"2\"/>\r\n" + 
			"			-->\r\n" + 
			"			<!-- V01: changed: Initiating party only to contain name and id in CH version\r\n" + 
			"			<xs:element name=\"InitgPty\" type=\"PartyIdentification32\"/>\r\n" + 
			"			-->\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<xs:simpleType name=\"IBAN2007Identifier\">\r\n" + 
			"		<xs:restriction base=\"xs:string\">\r\n" + 
			"			<xs:pattern value=\"[A-Z]{2,2}[0-9]{2,2}[a-zA-Z0-9]{1,30}\"/>\r\n" + 
			"		</xs:restriction>\r\n" + 
			"	</xs:simpleType>\r\n" + 
			"	<xs:simpleType name=\"ISODate\">\r\n" + 
			"		<xs:restriction base=\"xs:date\"/>\r\n" + 
			"	</xs:simpleType>\r\n" + 
			"	<xs:simpleType name=\"ISODateTime\">\r\n" + 
			"		<xs:restriction base=\"xs:dateTime\"/>\r\n" + 
			"	</xs:simpleType>\r\n" + 
			"	<xs:simpleType name=\"Instruction3Code\">\r\n" + 
			"		<xs:restriction base=\"xs:string\">\r\n" + 
			"			<xs:enumeration value=\"CHQB\"/>\r\n" + 
			"			<xs:enumeration value=\"HOLD\"/>\r\n" + 
			"			<xs:enumeration value=\"PHOB\"/>\r\n" + 
			"			<xs:enumeration value=\"TELB\"/>\r\n" + 
			"		</xs:restriction>\r\n" + 
			"	</xs:simpleType>\r\n" + 
			"	<xs:complexType name=\"InstructionForCreditorAgent1\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"Cd\" type=\"Instruction3Code\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"InstrInf\" type=\"Max140Text\" minOccurs=\"0\"/>\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<xs:complexType name=\"LocalInstrument2Choice\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:choice>\r\n" + 
			"				<xs:element name=\"Cd\" type=\"ExternalLocalInstrument1Code\"/>\r\n" + 
			"				<xs:element name=\"Prtry\" type=\"Max35Text\"/>\r\n" + 
			"			</xs:choice>\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<xs:simpleType name=\"Max10Text\">\r\n" + 
			"		<xs:restriction base=\"BasicText-CH\">\r\n" + 
			"			<xs:minLength value=\"1\"/>\r\n" + 
			"			<xs:maxLength value=\"10\"/>\r\n" + 
			"		</xs:restriction>\r\n" + 
			"	</xs:simpleType>\r\n" + 
			"	<!-- V01: unused\r\n" + 
			"	<xs:simpleType name=\"Max128Text\">\r\n" + 
			"		<xs:restriction base=\"BasicText-CH\">\r\n" + 
			"			<xs:minLength value=\"1\"/>\r\n" + 
			"			<xs:maxLength value=\"128\"/>\r\n" + 
			"		</xs:restriction>\r\n" + 
			"	</xs:simpleType>\r\n" + 
			"    -->\r\n" + 
			"	<xs:simpleType name=\"Max140Text\">\r\n" + 
			"		<xs:restriction base=\"BasicText-CH\">\r\n" + 
			"			<xs:minLength value=\"1\"/>\r\n" + 
			"			<xs:maxLength value=\"140\"/>\r\n" + 
			"		</xs:restriction>\r\n" + 
			"	</xs:simpleType>\r\n" + 
			"	<xs:simpleType name=\"Max15NumericText\">\r\n" + 
			"		<xs:restriction base=\"xs:string\">\r\n" + 
			"			<xs:pattern value=\"[0-9]{1,15}\"/>\r\n" + 
			"		</xs:restriction>\r\n" + 
			"	</xs:simpleType>\r\n" + 
			"	<xs:simpleType name=\"Max16Text\">\r\n" + 
			"		<xs:restriction base=\"BasicText-CH\">\r\n" + 
			"			<xs:minLength value=\"1\"/>\r\n" + 
			"			<xs:maxLength value=\"16\"/>\r\n" + 
			"		</xs:restriction>\r\n" + 
			"	</xs:simpleType>\r\n" + 
			"	<xs:simpleType name=\"Max2048Text\">\r\n" + 
			"		<xs:restriction base=\"BasicText-CH\">\r\n" + 
			"			<xs:minLength value=\"1\"/>\r\n" + 
			"			<xs:maxLength value=\"2048\"/>\r\n" + 
			"		</xs:restriction>\r\n" + 
			"	</xs:simpleType>\r\n" + 
			"	<xs:simpleType name=\"Max34Text\">\r\n" + 
			"		<xs:restriction base=\"BasicText-CH\">\r\n" + 
			"			<xs:minLength value=\"1\"/>\r\n" + 
			"			<xs:maxLength value=\"34\"/>\r\n" + 
			"		</xs:restriction>\r\n" + 
			"	</xs:simpleType>\r\n" + 
			"	<xs:simpleType name=\"Max35Text\">\r\n" + 
			"		<xs:restriction base=\"BasicText-CH\">\r\n" + 
			"			<xs:minLength value=\"1\"/>\r\n" + 
			"			<xs:maxLength value=\"35\"/>\r\n" + 
			"		</xs:restriction>\r\n" + 
			"	</xs:simpleType>\r\n" + 
			"	<!-- V01: added: replacement type for Max35Text where only the Swift character set is allowed -->\r\n" + 
			"	<xs:simpleType name=\"Max35Text-Swift\">\r\n" + 
			"		<xs:restriction base=\"BasicText-Swift\">\r\n" + 
			"			<xs:minLength value=\"1\"/>\r\n" + 
			"			<xs:maxLength value=\"35\"/>\r\n" + 
			"		</xs:restriction>\r\n" + 
			"	</xs:simpleType>\r\n" + 
			"	<xs:simpleType name=\"Max4Text\">\r\n" + 
			"		<xs:restriction base=\"BasicText-CH\">\r\n" + 
			"			<xs:minLength value=\"1\"/>\r\n" + 
			"			<xs:maxLength value=\"4\"/>\r\n" + 
			"		</xs:restriction>\r\n" + 
			"	</xs:simpleType>\r\n" + 
			"	<xs:simpleType name=\"Max70Text\">\r\n" + 
			"		<xs:restriction base=\"BasicText-CH\">\r\n" + 
			"			<xs:minLength value=\"1\"/>\r\n" + 
			"			<xs:maxLength value=\"70\"/>\r\n" + 
			"		</xs:restriction>\r\n" + 
			"	</xs:simpleType>\r\n" + 
			"	<!-- V01: unused\r\n" + 
			"	<xs:complexType name=\"NameAndAddress10\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"Nm\" type=\"Max140Text\"/>\r\n" + 
			"			<xs:element name=\"Adr\" type=\"PostalAddress6\"/>\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"    -->\r\n" + 
			"	<!-- V01: added: CH-Version: unused (prepared for later usage)\r\n" + 
			"	<xs:complexType name=\"NameAndAddress10-CH\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"Nm\" type=\"Max70Text\"/>\r\n" + 
			"			<xs:element name=\"Adr\" type=\"PostalAddress6-CH\"/>\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"    -->\r\n" + 
			"	<xs:simpleType name=\"NamePrefix1Code\">\r\n" + 
			"		<xs:restriction base=\"xs:string\">\r\n" + 
			"			<xs:enumeration value=\"DOCT\"/>\r\n" + 
			"			<xs:enumeration value=\"MIST\"/>\r\n" + 
			"			<xs:enumeration value=\"MISS\"/>\r\n" + 
			"			<xs:enumeration value=\"MADM\"/>\r\n" + 
			"		</xs:restriction>\r\n" + 
			"	</xs:simpleType>\r\n" + 
			"	<!-- V01: unused\r\n" + 
			"	<xs:simpleType name=\"Number\">\r\n" + 
			"		<xs:restriction base=\"xs:decimal\">\r\n" + 
			"			<xs:fractionDigits value=\"0\"/>\r\n" + 
			"			<xs:totalDigits value=\"18\"/>\r\n" + 
			"		</xs:restriction>\r\n" + 
			"	</xs:simpleType>\r\n" + 
			"    -->\r\n" + 
			"	<xs:complexType name=\"OrganisationIdentification4\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"BICOrBEI\" type=\"AnyBICIdentifier\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"Othr\" type=\"GenericOrganisationIdentification1\" minOccurs=\"0\" maxOccurs=\"unbounded\"/>\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<!-- V01: added:  only one occurance of element other allowed in CH version -->\r\n" + 
			"	<xs:complexType name=\"OrganisationIdentification4-CH\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"BICOrBEI\" type=\"AnyBICIdentifier\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"Othr\" type=\"GenericOrganisationIdentification1\" minOccurs=\"0\"/>\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<xs:complexType name=\"OrganisationIdentificationSchemeName1Choice\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:choice>\r\n" + 
			"				<xs:element name=\"Cd\" type=\"ExternalOrganisationIdentification1Code\"/>\r\n" + 
			"				<xs:element name=\"Prtry\" type=\"Max35Text\"/>\r\n" + 
			"			</xs:choice>\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<xs:complexType name=\"Party6Choice\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:choice>\r\n" + 
			"				<xs:element name=\"OrgId\" type=\"OrganisationIdentification4\"/>\r\n" + 
			"				<xs:element name=\"PrvtId\" type=\"PersonIdentification5\"/>\r\n" + 
			"			</xs:choice>\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<!-- V01: added:  -->\r\n" + 
			"	<xs:complexType name=\"Party6Choice-CH\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:choice>\r\n" + 
			"				<xs:element name=\"OrgId\" type=\"OrganisationIdentification4-CH\"/>\r\n" + 
			"				<xs:element name=\"PrvtId\" type=\"PersonIdentification5-CH\"/>\r\n" + 
			"			</xs:choice>\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<xs:complexType name=\"PartyIdentification32\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"Nm\" type=\"Max140Text\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"PstlAdr\" type=\"PostalAddress6\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"Id\" type=\"Party6Choice\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"CtryOfRes\" type=\"CountryCode\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"CtctDtls\" type=\"ContactDetails2\" minOccurs=\"0\"/>\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<!-- V01: added: replacement type for PartyIdentification8 where only elements Name and Id may be used -->\r\n" + 
			"	<xs:complexType name=\"PartyIdentification32-CH_NameAndId\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"Nm\" type=\"Max70Text\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"Id\" type=\"Party6Choice-CH\" minOccurs=\"0\"/>\r\n" + 
			"			<!-- V02: added: Contact Details for Software name and version -->\r\n" + 
			"			<xs:element name=\"CtctDtls\" type=\"ContactDetails2-CH\" minOccurs=\"0\"/>\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<!-- V01: added -->\r\n" + 
			"	<xs:complexType name=\"PartyIdentification32-CH\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"Nm\" type=\"Max70Text\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"PstlAdr\" type=\"PostalAddress6-CH\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"Id\" type=\"Party6Choice-CH\" minOccurs=\"0\"/>\r\n" + 
			"			<!-- changed -->\r\n" + 
			"			<!-- unused\r\n" + 
			"			<xs:element name=\"CtryOfRes\" type=\"CountryCode\" minOccurs=\"0\"/>\r\n" + 
			"			-->\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<!-- V02: changed: element Name mandatory -->\r\n" + 
			"	<xs:complexType name=\"PartyIdentification32-CH_Name\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"Nm\" type=\"Max70Text\"/>\r\n" + 
			"			<xs:element name=\"PstlAdr\" type=\"PostalAddress6-CH\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"Id\" type=\"Party6Choice-CH\" minOccurs=\"0\"/>\r\n" + 
			"			<!-- changed -->\r\n" + 
			"			<!-- unused\r\n" + 
			"			<xs:element name=\"CtryOfRes\" type=\"CountryCode\" minOccurs=\"0\"/>\r\n" + 
			"			-->\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<!--\r\n" + 
			"	<xs:complexType name=\"PartyIdentification32-CH_Debtor\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"Nm\" type=\"Max70Text\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"PstlAdr\" type=\"PostalAddress6-CH\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"Id\" type=\"Party6Choice-CH\" minOccurs=\"0\"/>\r\n" + 
			"\r\n" + 
			"			<xs:element name=\"CtryOfRes\" type=\"CountryCode\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"CtctDtls\" type=\"ContactDetails2\" minOccurs=\"0\"/>\r\n" + 
			"\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"\r\n" + 
			"	<xs:complexType name=\"PartyIdentification32-CH_Creditor\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"Nm\" type=\"Max70Text\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"PstlAdr\" type=\"PostalAddress6-CH\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"Id\" type=\"Party6Choice-CH\" minOccurs=\"0\"/>\r\n" + 
			"\r\n" + 
			"			<xs:element name=\"CtryOfRes\" type=\"CountryCode\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"CtctDtls\" type=\"ContactDetails2\" minOccurs=\"0\"/>\r\n" + 
			"\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	-->\r\n" + 
			"	<xs:complexType name=\"PaymentIdentification1\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"InstrId\" type=\"Max35Text-Swift\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"EndToEndId\" type=\"Max35Text-Swift\"/>\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<!-- V01: changed: CH-version changes applied -->\r\n" + 
			"	<xs:complexType name=\"PaymentInstructionInformation3-CH\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"PmtInfId\" type=\"Max35Text-Swift\"/>\r\n" + 
			"			<xs:element name=\"PmtMtd\" type=\"PaymentMethod3Code\"/>\r\n" + 
			"			<xs:element name=\"BtchBookg\" type=\"BatchBookingIndicator\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"NbOfTxs\" type=\"Max15NumericText\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"CtrlSum\" type=\"DecimalNumber\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"PmtTpInf\" type=\"PaymentTypeInformation19-CH\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"ReqdExctnDt\" type=\"ISODate\"/>\r\n" + 
			"			<xs:element name=\"Dbtr\" type=\"PartyIdentification32-CH\"/>\r\n" + 
			"			<xs:element name=\"DbtrAcct\" type=\"CashAccount16-CH_IdTpCcy\"/>\r\n" + 
			"			<xs:element name=\"DbtrAgt\" type=\"BranchAndFinancialInstitutionIdentification4-CH_BicOrClrId\"/>\r\n" + 
			"			<xs:element name=\"UltmtDbtr\" type=\"PartyIdentification32-CH\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"ChrgBr\" type=\"ChargeBearerType1Code\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"ChrgsAcct\" type=\"CashAccount16-CH_IdAndCurrency\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"CdtTrfTxInf\" type=\"CreditTransferTransactionInformation10-CH\" maxOccurs=\"unbounded\"/>\r\n" + 
			"			<!-- V01: unused\r\n" + 
			"			<xs:element name=\"PoolgAdjstmntDt\" type=\"ISODate\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"DbtrAgtAcct\" type=\"CashAccount16\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"ChrgsAcctAgt\" type=\"BranchAndFinancialInstitutionIdentification4\" minOccurs=\"0\"/>\r\n" + 
			"			-->\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<xs:simpleType name=\"PaymentMethod3Code\">\r\n" + 
			"		<xs:restriction base=\"xs:string\">\r\n" + 
			"			<xs:enumeration value=\"CHK\"/>\r\n" + 
			"			<xs:enumeration value=\"TRF\"/>\r\n" + 
			"			<xs:enumeration value=\"TRA\"/>\r\n" + 
			"		</xs:restriction>\r\n" + 
			"	</xs:simpleType>\r\n" + 
			"	<!-- V01: changed: CH version changes applied -->\r\n" + 
			"	<xs:complexType name=\"PaymentTypeInformation19-CH\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"InstrPrty\" type=\"Priority2Code\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"SvcLvl\" type=\"ServiceLevel8Choice\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"LclInstrm\" type=\"LocalInstrument2Choice\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"CtgyPurp\" type=\"CategoryPurpose1-CH_Code\" minOccurs=\"0\"/>\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<!-- V01: unused\r\n" + 
			"	<xs:simpleType name=\"PercentageRate\">\r\n" + 
			"		<xs:restriction base=\"xs:decimal\">\r\n" + 
			"			<xs:fractionDigits value=\"10\"/>\r\n" + 
			"			<xs:totalDigits value=\"11\"/>\r\n" + 
			"		</xs:restriction>\r\n" + 
			"	</xs:simpleType>\r\n" + 
			"    -->\r\n" + 
			"	<xs:complexType name=\"PersonIdentification5\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"DtAndPlcOfBirth\" type=\"DateAndPlaceOfBirth\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"Othr\" type=\"GenericPersonIdentification1\" minOccurs=\"0\" maxOccurs=\"unbounded\"/>\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<!-- V01: changed: only one occurance of element Othr allowed in CH version -->\r\n" + 
			"	<xs:complexType name=\"PersonIdentification5-CH\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"DtAndPlcOfBirth\" type=\"DateAndPlaceOfBirth\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"Othr\" type=\"GenericPersonIdentification1\" minOccurs=\"0\"/>\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<xs:complexType name=\"PersonIdentificationSchemeName1Choice\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:choice>\r\n" + 
			"				<xs:element name=\"Cd\" type=\"ExternalPersonIdentification1Code\"/>\r\n" + 
			"				<xs:element name=\"Prtry\" type=\"Max35Text\"/>\r\n" + 
			"			</xs:choice>\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<xs:simpleType name=\"PhoneNumber\">\r\n" + 
			"		<xs:restriction base=\"xs:string\">\r\n" + 
			"			<xs:pattern value=\"\\+[0-9]{1,3}-[0-9()+\\-]{1,30}\"/>\r\n" + 
			"		</xs:restriction>\r\n" + 
			"	</xs:simpleType>\r\n" + 
			"	<xs:complexType name=\"PostalAddress6\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"AdrTp\" type=\"AddressType2Code\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"Dept\" type=\"Max70Text\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"SubDept\" type=\"Max70Text\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"StrtNm\" type=\"Max70Text\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"BldgNb\" type=\"Max16Text\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"PstCd\" type=\"Max16Text\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"TwnNm\" type=\"Max35Text\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"CtrySubDvsn\" type=\"Max35Text\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"Ctry\" type=\"CountryCode\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"AdrLine\" type=\"Max70Text\" minOccurs=\"0\" maxOccurs=\"7\"/>\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<!-- V01: added: only 2 lines of address lines allowed in CH version -->\r\n" + 
			"	<xs:complexType name=\"PostalAddress6-CH\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"AdrTp\" type=\"AddressType2Code\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"Dept\" type=\"Max70Text\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"SubDept\" type=\"Max70Text\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"StrtNm\" type=\"Max70Text\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"BldgNb\" type=\"Max16Text\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"PstCd\" type=\"Max16Text\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"TwnNm\" type=\"Max35Text\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"CtrySubDvsn\" type=\"Max35Text\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"Ctry\" type=\"CountryCode\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"AdrLine\" type=\"Max70Text\" minOccurs=\"0\" maxOccurs=\"2\"/>\r\n" + 
			"			<!-- V01: changed: max. 2 occurence -->\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<xs:simpleType name=\"Priority2Code\">\r\n" + 
			"		<xs:restriction base=\"xs:string\">\r\n" + 
			"			<xs:enumeration value=\"HIGH\"/>\r\n" + 
			"			<xs:enumeration value=\"NORM\"/>\r\n" + 
			"		</xs:restriction>\r\n" + 
			"	</xs:simpleType>\r\n" + 
			"	<!-- V01: changed: CH-version changes applied -->\r\n" + 
			"	<xs:complexType name=\"Purpose2-CH_Code\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"Cd\" type=\"ExternalPurpose1Code\"/>\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<xs:complexType name=\"ReferredDocumentInformation3\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"Tp\" type=\"ReferredDocumentType2\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"Nb\" type=\"Max35Text\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"RltdDt\" type=\"ISODate\" minOccurs=\"0\"/>\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<xs:complexType name=\"ReferredDocumentType1Choice\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:choice>\r\n" + 
			"				<xs:element name=\"Cd\" type=\"DocumentType5Code\"/>\r\n" + 
			"				<xs:element name=\"Prtry\" type=\"Max35Text\"/>\r\n" + 
			"			</xs:choice>\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<xs:complexType name=\"ReferredDocumentType2\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"CdOrPrtry\" type=\"ReferredDocumentType1Choice\"/>\r\n" + 
			"			<xs:element name=\"Issr\" type=\"Max35Text\" minOccurs=\"0\"/>\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<xs:complexType name=\"RegulatoryAuthority2\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"Nm\" type=\"Max140Text\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"Ctry\" type=\"CountryCode\" minOccurs=\"0\"/>\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<xs:complexType name=\"RegulatoryReporting3\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"DbtCdtRptgInd\" type=\"RegulatoryReportingType1Code\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"Authrty\" type=\"RegulatoryAuthority2\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"Dtls\" type=\"StructuredRegulatoryReporting3\" minOccurs=\"0\" maxOccurs=\"unbounded\"/>\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<xs:simpleType name=\"RegulatoryReportingType1Code\">\r\n" + 
			"		<xs:restriction base=\"xs:string\">\r\n" + 
			"			<xs:enumeration value=\"CRED\"/>\r\n" + 
			"			<xs:enumeration value=\"DEBT\"/>\r\n" + 
			"			<xs:enumeration value=\"BOTH\"/>\r\n" + 
			"		</xs:restriction>\r\n" + 
			"	</xs:simpleType>\r\n" + 
			"	<xs:complexType name=\"RemittanceAmount1\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"DuePyblAmt\" type=\"ActiveOrHistoricCurrencyAndAmount\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"DscntApldAmt\" type=\"ActiveOrHistoricCurrencyAndAmount\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"CdtNoteAmt\" type=\"ActiveOrHistoricCurrencyAndAmount\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"TaxAmt\" type=\"ActiveOrHistoricCurrencyAndAmount\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"AdjstmntAmtAndRsn\" type=\"DocumentAdjustment1\" minOccurs=\"0\" maxOccurs=\"unbounded\"/>\r\n" + 
			"			<xs:element name=\"RmtdAmt\" type=\"ActiveOrHistoricCurrencyAndAmount\" minOccurs=\"0\"/>\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<xs:complexType name=\"RemittanceInformation5-CH\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"Ustrd\" type=\"Max140Text\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"Strd\" type=\"StructuredRemittanceInformation7\" minOccurs=\"0\"/>\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<!-- V01: unused\r\n" + 
			"	<xs:complexType name=\"RemittanceLocation2\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"RmtId\" type=\"Max35Text\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"RmtLctnMtd\" type=\"RemittanceLocationMethod2Code\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"RmtLctnElctrncAdr\" type=\"Max2048Text\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"RmtLctnPstlAdr\" type=\"NameAndAddress10\" minOccurs=\"0\"/>\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<xs:simpleType name=\"RemittanceLocationMethod2Code\">\r\n" + 
			"		<xs:restriction base=\"xs:string\">\r\n" + 
			"			<xs:enumeration value=\"FAXI\"/>\r\n" + 
			"			<xs:enumeration value=\"EDIC\"/>\r\n" + 
			"			<xs:enumeration value=\"URID\"/>\r\n" + 
			"			<xs:enumeration value=\"EMAL\"/>\r\n" + 
			"			<xs:enumeration value=\"POST\"/>\r\n" + 
			"			<xs:enumeration value=\"SMSM\"/>\r\n" + 
			"		</xs:restriction>\r\n" + 
			"	</xs:simpleType>\r\n" + 
			"    -->\r\n" + 
			"	<xs:complexType name=\"ServiceLevel8Choice\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:choice>\r\n" + 
			"				<xs:element name=\"Cd\" type=\"ExternalServiceLevel1Code\"/>\r\n" + 
			"				<xs:element name=\"Prtry\" type=\"Max35Text\"/>\r\n" + 
			"			</xs:choice>\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<xs:complexType name=\"StructuredRegulatoryReporting3\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"Tp\" type=\"Max35Text\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"Dt\" type=\"ISODate\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"Ctry\" type=\"CountryCode\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"Cd\" type=\"Max10Text\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"Amt\" type=\"ActiveOrHistoricCurrencyAndAmount\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"Inf\" type=\"Max35Text\" minOccurs=\"0\" maxOccurs=\"unbounded\"/>\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<xs:complexType name=\"StructuredRemittanceInformation7\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"RfrdDocInf\" type=\"ReferredDocumentInformation3\" minOccurs=\"0\" maxOccurs=\"unbounded\"/>\r\n" + 
			"			<xs:element name=\"RfrdDocAmt\" type=\"RemittanceAmount1\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"CdtrRefInf\" type=\"CreditorReferenceInformation2\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"Invcr\" type=\"PartyIdentification32\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"Invcee\" type=\"PartyIdentification32\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"AddtlRmtInf\" type=\"Max140Text\" minOccurs=\"0\" maxOccurs=\"3\"/>\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<!-- V01: unused\r\n" + 
			"	<xs:complexType name=\"TaxAmount1\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"Rate\" type=\"PercentageRate\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"TaxblBaseAmt\" type=\"ActiveOrHistoricCurrencyAndAmount\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"TtlAmt\" type=\"ActiveOrHistoricCurrencyAndAmount\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"Dtls\" type=\"TaxRecordDetails1\" minOccurs=\"0\" maxOccurs=\"unbounded\"/>\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<xs:complexType name=\"TaxAuthorisation1\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"Titl\" type=\"Max35Text\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"Nm\" type=\"Max140Text\" minOccurs=\"0\"/>\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<xs:complexType name=\"TaxInformation3\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"Cdtr\" type=\"TaxParty1\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"Dbtr\" type=\"TaxParty2\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"AdmstnZn\" type=\"Max35Text\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"RefNb\" type=\"Max140Text\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"Mtd\" type=\"Max35Text\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"TtlTaxblBaseAmt\" type=\"ActiveOrHistoricCurrencyAndAmount\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"TtlTaxAmt\" type=\"ActiveOrHistoricCurrencyAndAmount\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"Dt\" type=\"ISODate\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"SeqNb\" type=\"Number\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"Rcrd\" type=\"TaxRecord1\" minOccurs=\"0\" maxOccurs=\"unbounded\"/>\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<xs:complexType name=\"TaxParty1\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"TaxId\" type=\"Max35Text\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"RegnId\" type=\"Max35Text\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"TaxTp\" type=\"Max35Text\" minOccurs=\"0\"/>\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<xs:complexType name=\"TaxParty2\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"TaxId\" type=\"Max35Text\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"RegnId\" type=\"Max35Text\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"TaxTp\" type=\"Max35Text\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"Authstn\" type=\"TaxAuthorisation1\" minOccurs=\"0\"/>\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<xs:complexType name=\"TaxPeriod1\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"Yr\" type=\"ISODate\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"Tp\" type=\"TaxRecordPeriod1Code\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"FrToDt\" type=\"DatePeriodDetails\" minOccurs=\"0\"/>\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<xs:complexType name=\"TaxRecord1\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"Tp\" type=\"Max35Text\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"Ctgy\" type=\"Max35Text\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"CtgyDtls\" type=\"Max35Text\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"DbtrSts\" type=\"Max35Text\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"CertId\" type=\"Max35Text\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"FrmsCd\" type=\"Max35Text\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"Prd\" type=\"TaxPeriod1\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"TaxAmt\" type=\"TaxAmount1\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"AddtlInf\" type=\"Max140Text\" minOccurs=\"0\"/>\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<xs:complexType name=\"TaxRecordDetails1\">\r\n" + 
			"		<xs:sequence>\r\n" + 
			"			<xs:element name=\"Prd\" type=\"TaxPeriod1\" minOccurs=\"0\"/>\r\n" + 
			"			<xs:element name=\"Amt\" type=\"ActiveOrHistoricCurrencyAndAmount\"/>\r\n" + 
			"		</xs:sequence>\r\n" + 
			"	</xs:complexType>\r\n" + 
			"	<xs:simpleType name=\"TaxRecordPeriod1Code\">\r\n" + 
			"		<xs:restriction base=\"xs:string\">\r\n" + 
			"			<xs:enumeration value=\"MM01\"/>\r\n" + 
			"			<xs:enumeration value=\"MM02\"/>\r\n" + 
			"			<xs:enumeration value=\"MM03\"/>\r\n" + 
			"			<xs:enumeration value=\"MM04\"/>\r\n" + 
			"			<xs:enumeration value=\"MM05\"/>\r\n" + 
			"			<xs:enumeration value=\"MM06\"/>\r\n" + 
			"			<xs:enumeration value=\"MM07\"/>\r\n" + 
			"			<xs:enumeration value=\"MM08\"/>\r\n" + 
			"			<xs:enumeration value=\"MM09\"/>\r\n" + 
			"			<xs:enumeration value=\"MM10\"/>\r\n" + 
			"			<xs:enumeration value=\"MM11\"/>\r\n" + 
			"			<xs:enumeration value=\"MM12\"/>\r\n" + 
			"			<xs:enumeration value=\"QTR1\"/>\r\n" + 
			"			<xs:enumeration value=\"QTR2\"/>\r\n" + 
			"			<xs:enumeration value=\"QTR3\"/>\r\n" + 
			"			<xs:enumeration value=\"QTR4\"/>\r\n" + 
			"			<xs:enumeration value=\"HLF1\"/>\r\n" + 
			"			<xs:enumeration value=\"HLF2\"/>\r\n" + 
			"		</xs:restriction>\r\n" + 
			"	</xs:simpleType>\r\n" + 
			"    -->\r\n" + 
			"</xs:schema>\r\n" + 
			"";
		String xsdSchemaV03CH = xsdSchemaV03CHpart1 + xsdSchemaV03CHpart2;
		
	public SepaXmlMarshallerPain001V03CH() {
	}

	@Override
	public String marshal(Object sepaDoc) throws JAXBException, SAXException {
		return marshal(xsdSchemaV03CH, (PACHDocument)sepaDoc);
	}

}
