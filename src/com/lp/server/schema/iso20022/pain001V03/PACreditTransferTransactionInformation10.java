//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2015.07.30 um 10:14:02 AM CEST 
//


package com.lp.server.schema.iso20022.pain001V03;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer CreditTransferTransactionInformation10 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="CreditTransferTransactionInformation10">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PmtId" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.03}PaymentIdentification1"/>
 *         &lt;element name="PmtTpInf" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.03}PaymentTypeInformation19" minOccurs="0"/>
 *         &lt;element name="Amt" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.03}AmountType3Choice"/>
 *         &lt;element name="XchgRateInf" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.03}ExchangeRateInformation1" minOccurs="0"/>
 *         &lt;element name="ChrgBr" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.03}ChargeBearerType1Code" minOccurs="0"/>
 *         &lt;element name="ChqInstr" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.03}Cheque6" minOccurs="0"/>
 *         &lt;element name="UltmtDbtr" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.03}PartyIdentification32" minOccurs="0"/>
 *         &lt;element name="IntrmyAgt1" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.03}BranchAndFinancialInstitutionIdentification4" minOccurs="0"/>
 *         &lt;element name="IntrmyAgt1Acct" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.03}CashAccount16" minOccurs="0"/>
 *         &lt;element name="IntrmyAgt2" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.03}BranchAndFinancialInstitutionIdentification4" minOccurs="0"/>
 *         &lt;element name="IntrmyAgt2Acct" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.03}CashAccount16" minOccurs="0"/>
 *         &lt;element name="IntrmyAgt3" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.03}BranchAndFinancialInstitutionIdentification4" minOccurs="0"/>
 *         &lt;element name="IntrmyAgt3Acct" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.03}CashAccount16" minOccurs="0"/>
 *         &lt;element name="CdtrAgt" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.03}BranchAndFinancialInstitutionIdentification4" minOccurs="0"/>
 *         &lt;element name="CdtrAgtAcct" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.03}CashAccount16" minOccurs="0"/>
 *         &lt;element name="Cdtr" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.03}PartyIdentification32" minOccurs="0"/>
 *         &lt;element name="CdtrAcct" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.03}CashAccount16" minOccurs="0"/>
 *         &lt;element name="UltmtCdtr" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.03}PartyIdentification32" minOccurs="0"/>
 *         &lt;element name="InstrForCdtrAgt" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.03}InstructionForCreditorAgent1" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="InstrForDbtrAgt" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.03}Max140Text" minOccurs="0"/>
 *         &lt;element name="Purp" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.03}Purpose2Choice" minOccurs="0"/>
 *         &lt;element name="RgltryRptg" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.03}RegulatoryReporting3" maxOccurs="10" minOccurs="0"/>
 *         &lt;element name="Tax" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.03}TaxInformation3" minOccurs="0"/>
 *         &lt;element name="RltdRmtInf" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.03}RemittanceLocation2" maxOccurs="10" minOccurs="0"/>
 *         &lt;element name="RmtInf" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.03}RemittanceInformation5" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CreditTransferTransactionInformation10", propOrder = {
    "pmtId",
    "pmtTpInf",
    "amt",
    "xchgRateInf",
    "chrgBr",
    "chqInstr",
    "ultmtDbtr",
    "intrmyAgt1",
    "intrmyAgt1Acct",
    "intrmyAgt2",
    "intrmyAgt2Acct",
    "intrmyAgt3",
    "intrmyAgt3Acct",
    "cdtrAgt",
    "cdtrAgtAcct",
    "cdtr",
    "cdtrAcct",
    "ultmtCdtr",
    "instrForCdtrAgt",
    "instrForDbtrAgt",
    "purp",
    "rgltryRptg",
    "tax",
    "rltdRmtInf",
    "rmtInf"
})
public class PACreditTransferTransactionInformation10 {

    @XmlElement(name = "PmtId", required = true)
    protected PAPaymentIdentification1 pmtId;
    @XmlElement(name = "PmtTpInf")
    protected PAPaymentTypeInformation19 pmtTpInf;
    @XmlElement(name = "Amt", required = true)
    protected PAAmountType3Choice amt;
    @XmlElement(name = "XchgRateInf")
    protected PAExchangeRateInformation1 xchgRateInf;
    @XmlElement(name = "ChrgBr")
    @XmlSchemaType(name = "string")
    protected PAChargeBearerType1Code chrgBr;
    @XmlElement(name = "ChqInstr")
    protected PACheque6 chqInstr;
    @XmlElement(name = "UltmtDbtr")
    protected PAPartyIdentification32 ultmtDbtr;
    @XmlElement(name = "IntrmyAgt1")
    protected PABranchAndFinancialInstitutionIdentification4 intrmyAgt1;
    @XmlElement(name = "IntrmyAgt1Acct")
    protected PACashAccount16 intrmyAgt1Acct;
    @XmlElement(name = "IntrmyAgt2")
    protected PABranchAndFinancialInstitutionIdentification4 intrmyAgt2;
    @XmlElement(name = "IntrmyAgt2Acct")
    protected PACashAccount16 intrmyAgt2Acct;
    @XmlElement(name = "IntrmyAgt3")
    protected PABranchAndFinancialInstitutionIdentification4 intrmyAgt3;
    @XmlElement(name = "IntrmyAgt3Acct")
    protected PACashAccount16 intrmyAgt3Acct;
    @XmlElement(name = "CdtrAgt")
    protected PABranchAndFinancialInstitutionIdentification4 cdtrAgt;
    @XmlElement(name = "CdtrAgtAcct")
    protected PACashAccount16 cdtrAgtAcct;
    @XmlElement(name = "Cdtr")
    protected PAPartyIdentification32 cdtr;
    @XmlElement(name = "CdtrAcct")
    protected PACashAccount16 cdtrAcct;
    @XmlElement(name = "UltmtCdtr")
    protected PAPartyIdentification32 ultmtCdtr;
    @XmlElement(name = "InstrForCdtrAgt")
    protected List<PAInstructionForCreditorAgent1> instrForCdtrAgt;
    @XmlElement(name = "InstrForDbtrAgt")
    protected String instrForDbtrAgt;
    @XmlElement(name = "Purp")
    protected PAPurpose2Choice purp;
    @XmlElement(name = "RgltryRptg")
    protected List<PARegulatoryReporting3> rgltryRptg;
    @XmlElement(name = "Tax")
    protected PATaxInformation3 tax;
    @XmlElement(name = "RltdRmtInf")
    protected List<PARemittanceLocation2> rltdRmtInf;
    @XmlElement(name = "RmtInf")
    protected PARemittanceInformation5 rmtInf;

    /**
     * Ruft den Wert der pmtId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PAPaymentIdentification1 }
     *     
     */
    public PAPaymentIdentification1 getPmtId() {
        return pmtId;
    }

    /**
     * Legt den Wert der pmtId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PAPaymentIdentification1 }
     *     
     */
    public void setPmtId(PAPaymentIdentification1 value) {
        this.pmtId = value;
    }

    /**
     * Ruft den Wert der pmtTpInf-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PAPaymentTypeInformation19 }
     *     
     */
    public PAPaymentTypeInformation19 getPmtTpInf() {
        return pmtTpInf;
    }

    /**
     * Legt den Wert der pmtTpInf-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PAPaymentTypeInformation19 }
     *     
     */
    public void setPmtTpInf(PAPaymentTypeInformation19 value) {
        this.pmtTpInf = value;
    }

    /**
     * Ruft den Wert der amt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PAAmountType3Choice }
     *     
     */
    public PAAmountType3Choice getAmt() {
        return amt;
    }

    /**
     * Legt den Wert der amt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PAAmountType3Choice }
     *     
     */
    public void setAmt(PAAmountType3Choice value) {
        this.amt = value;
    }

    /**
     * Ruft den Wert der xchgRateInf-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PAExchangeRateInformation1 }
     *     
     */
    public PAExchangeRateInformation1 getXchgRateInf() {
        return xchgRateInf;
    }

    /**
     * Legt den Wert der xchgRateInf-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PAExchangeRateInformation1 }
     *     
     */
    public void setXchgRateInf(PAExchangeRateInformation1 value) {
        this.xchgRateInf = value;
    }

    /**
     * Ruft den Wert der chrgBr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PAChargeBearerType1Code }
     *     
     */
    public PAChargeBearerType1Code getChrgBr() {
        return chrgBr;
    }

    /**
     * Legt den Wert der chrgBr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PAChargeBearerType1Code }
     *     
     */
    public void setChrgBr(PAChargeBearerType1Code value) {
        this.chrgBr = value;
    }

    /**
     * Ruft den Wert der chqInstr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACheque6 }
     *     
     */
    public PACheque6 getChqInstr() {
        return chqInstr;
    }

    /**
     * Legt den Wert der chqInstr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACheque6 }
     *     
     */
    public void setChqInstr(PACheque6 value) {
        this.chqInstr = value;
    }

    /**
     * Ruft den Wert der ultmtDbtr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PAPartyIdentification32 }
     *     
     */
    public PAPartyIdentification32 getUltmtDbtr() {
        return ultmtDbtr;
    }

    /**
     * Legt den Wert der ultmtDbtr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PAPartyIdentification32 }
     *     
     */
    public void setUltmtDbtr(PAPartyIdentification32 value) {
        this.ultmtDbtr = value;
    }

    /**
     * Ruft den Wert der intrmyAgt1-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PABranchAndFinancialInstitutionIdentification4 }
     *     
     */
    public PABranchAndFinancialInstitutionIdentification4 getIntrmyAgt1() {
        return intrmyAgt1;
    }

    /**
     * Legt den Wert der intrmyAgt1-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PABranchAndFinancialInstitutionIdentification4 }
     *     
     */
    public void setIntrmyAgt1(PABranchAndFinancialInstitutionIdentification4 value) {
        this.intrmyAgt1 = value;
    }

    /**
     * Ruft den Wert der intrmyAgt1Acct-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACashAccount16 }
     *     
     */
    public PACashAccount16 getIntrmyAgt1Acct() {
        return intrmyAgt1Acct;
    }

    /**
     * Legt den Wert der intrmyAgt1Acct-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACashAccount16 }
     *     
     */
    public void setIntrmyAgt1Acct(PACashAccount16 value) {
        this.intrmyAgt1Acct = value;
    }

    /**
     * Ruft den Wert der intrmyAgt2-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PABranchAndFinancialInstitutionIdentification4 }
     *     
     */
    public PABranchAndFinancialInstitutionIdentification4 getIntrmyAgt2() {
        return intrmyAgt2;
    }

    /**
     * Legt den Wert der intrmyAgt2-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PABranchAndFinancialInstitutionIdentification4 }
     *     
     */
    public void setIntrmyAgt2(PABranchAndFinancialInstitutionIdentification4 value) {
        this.intrmyAgt2 = value;
    }

    /**
     * Ruft den Wert der intrmyAgt2Acct-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACashAccount16 }
     *     
     */
    public PACashAccount16 getIntrmyAgt2Acct() {
        return intrmyAgt2Acct;
    }

    /**
     * Legt den Wert der intrmyAgt2Acct-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACashAccount16 }
     *     
     */
    public void setIntrmyAgt2Acct(PACashAccount16 value) {
        this.intrmyAgt2Acct = value;
    }

    /**
     * Ruft den Wert der intrmyAgt3-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PABranchAndFinancialInstitutionIdentification4 }
     *     
     */
    public PABranchAndFinancialInstitutionIdentification4 getIntrmyAgt3() {
        return intrmyAgt3;
    }

    /**
     * Legt den Wert der intrmyAgt3-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PABranchAndFinancialInstitutionIdentification4 }
     *     
     */
    public void setIntrmyAgt3(PABranchAndFinancialInstitutionIdentification4 value) {
        this.intrmyAgt3 = value;
    }

    /**
     * Ruft den Wert der intrmyAgt3Acct-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACashAccount16 }
     *     
     */
    public PACashAccount16 getIntrmyAgt3Acct() {
        return intrmyAgt3Acct;
    }

    /**
     * Legt den Wert der intrmyAgt3Acct-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACashAccount16 }
     *     
     */
    public void setIntrmyAgt3Acct(PACashAccount16 value) {
        this.intrmyAgt3Acct = value;
    }

    /**
     * Ruft den Wert der cdtrAgt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PABranchAndFinancialInstitutionIdentification4 }
     *     
     */
    public PABranchAndFinancialInstitutionIdentification4 getCdtrAgt() {
        return cdtrAgt;
    }

    /**
     * Legt den Wert der cdtrAgt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PABranchAndFinancialInstitutionIdentification4 }
     *     
     */
    public void setCdtrAgt(PABranchAndFinancialInstitutionIdentification4 value) {
        this.cdtrAgt = value;
    }

    /**
     * Ruft den Wert der cdtrAgtAcct-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACashAccount16 }
     *     
     */
    public PACashAccount16 getCdtrAgtAcct() {
        return cdtrAgtAcct;
    }

    /**
     * Legt den Wert der cdtrAgtAcct-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACashAccount16 }
     *     
     */
    public void setCdtrAgtAcct(PACashAccount16 value) {
        this.cdtrAgtAcct = value;
    }

    /**
     * Ruft den Wert der cdtr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PAPartyIdentification32 }
     *     
     */
    public PAPartyIdentification32 getCdtr() {
        return cdtr;
    }

    /**
     * Legt den Wert der cdtr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PAPartyIdentification32 }
     *     
     */
    public void setCdtr(PAPartyIdentification32 value) {
        this.cdtr = value;
    }

    /**
     * Ruft den Wert der cdtrAcct-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACashAccount16 }
     *     
     */
    public PACashAccount16 getCdtrAcct() {
        return cdtrAcct;
    }

    /**
     * Legt den Wert der cdtrAcct-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACashAccount16 }
     *     
     */
    public void setCdtrAcct(PACashAccount16 value) {
        this.cdtrAcct = value;
    }

    /**
     * Ruft den Wert der ultmtCdtr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PAPartyIdentification32 }
     *     
     */
    public PAPartyIdentification32 getUltmtCdtr() {
        return ultmtCdtr;
    }

    /**
     * Legt den Wert der ultmtCdtr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PAPartyIdentification32 }
     *     
     */
    public void setUltmtCdtr(PAPartyIdentification32 value) {
        this.ultmtCdtr = value;
    }

    /**
     * Gets the value of the instrForCdtrAgt property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the instrForCdtrAgt property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInstrForCdtrAgt().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PAInstructionForCreditorAgent1 }
     * 
     * 
     */
    public List<PAInstructionForCreditorAgent1> getInstrForCdtrAgt() {
        if (instrForCdtrAgt == null) {
            instrForCdtrAgt = new ArrayList<PAInstructionForCreditorAgent1>();
        }
        return this.instrForCdtrAgt;
    }

    /**
     * Ruft den Wert der instrForDbtrAgt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInstrForDbtrAgt() {
        return instrForDbtrAgt;
    }

    /**
     * Legt den Wert der instrForDbtrAgt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInstrForDbtrAgt(String value) {
        this.instrForDbtrAgt = value;
    }

    /**
     * Ruft den Wert der purp-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PAPurpose2Choice }
     *     
     */
    public PAPurpose2Choice getPurp() {
        return purp;
    }

    /**
     * Legt den Wert der purp-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PAPurpose2Choice }
     *     
     */
    public void setPurp(PAPurpose2Choice value) {
        this.purp = value;
    }

    /**
     * Gets the value of the rgltryRptg property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the rgltryRptg property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRgltryRptg().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PARegulatoryReporting3 }
     * 
     * 
     */
    public List<PARegulatoryReporting3> getRgltryRptg() {
        if (rgltryRptg == null) {
            rgltryRptg = new ArrayList<PARegulatoryReporting3>();
        }
        return this.rgltryRptg;
    }

    /**
     * Ruft den Wert der tax-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PATaxInformation3 }
     *     
     */
    public PATaxInformation3 getTax() {
        return tax;
    }

    /**
     * Legt den Wert der tax-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PATaxInformation3 }
     *     
     */
    public void setTax(PATaxInformation3 value) {
        this.tax = value;
    }

    /**
     * Gets the value of the rltdRmtInf property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the rltdRmtInf property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRltdRmtInf().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PARemittanceLocation2 }
     * 
     * 
     */
    public List<PARemittanceLocation2> getRltdRmtInf() {
        if (rltdRmtInf == null) {
            rltdRmtInf = new ArrayList<PARemittanceLocation2>();
        }
        return this.rltdRmtInf;
    }

    /**
     * Ruft den Wert der rmtInf-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PARemittanceInformation5 }
     *     
     */
    public PARemittanceInformation5 getRmtInf() {
        return rmtInf;
    }

    /**
     * Legt den Wert der rmtInf-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PARemittanceInformation5 }
     *     
     */
    public void setRmtInf(PARemittanceInformation5 value) {
        this.rmtInf = value;
    }

}
