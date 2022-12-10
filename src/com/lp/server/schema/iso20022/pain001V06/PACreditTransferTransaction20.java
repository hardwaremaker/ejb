//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2015.06.25 um 02:55:19 PM CEST 
//


package com.lp.server.schema.iso20022.pain001V06;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer CreditTransferTransaction20 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="CreditTransferTransaction20">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PmtId" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.06}PaymentIdentification1"/>
 *         &lt;element name="PmtTpInf" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.06}PaymentTypeInformation19" minOccurs="0"/>
 *         &lt;element name="Amt" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.06}AmountType4Choice"/>
 *         &lt;element name="XchgRateInf" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.06}ExchangeRate1" minOccurs="0"/>
 *         &lt;element name="ChrgBr" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.06}ChargeBearerType1Code" minOccurs="0"/>
 *         &lt;element name="ChqInstr" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.06}Cheque7" minOccurs="0"/>
 *         &lt;element name="UltmtDbtr" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.06}PartyIdentification43" minOccurs="0"/>
 *         &lt;element name="IntrmyAgt1" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.06}BranchAndFinancialInstitutionIdentification5" minOccurs="0"/>
 *         &lt;element name="IntrmyAgt1Acct" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.06}CashAccount24" minOccurs="0"/>
 *         &lt;element name="IntrmyAgt2" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.06}BranchAndFinancialInstitutionIdentification5" minOccurs="0"/>
 *         &lt;element name="IntrmyAgt2Acct" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.06}CashAccount24" minOccurs="0"/>
 *         &lt;element name="IntrmyAgt3" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.06}BranchAndFinancialInstitutionIdentification5" minOccurs="0"/>
 *         &lt;element name="IntrmyAgt3Acct" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.06}CashAccount24" minOccurs="0"/>
 *         &lt;element name="CdtrAgt" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.06}BranchAndFinancialInstitutionIdentification5" minOccurs="0"/>
 *         &lt;element name="CdtrAgtAcct" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.06}CashAccount24" minOccurs="0"/>
 *         &lt;element name="Cdtr" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.06}PartyIdentification43" minOccurs="0"/>
 *         &lt;element name="CdtrAcct" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.06}CashAccount24" minOccurs="0"/>
 *         &lt;element name="UltmtCdtr" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.06}PartyIdentification43" minOccurs="0"/>
 *         &lt;element name="InstrForCdtrAgt" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.06}InstructionForCreditorAgent1" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="InstrForDbtrAgt" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.06}Max140Text" minOccurs="0"/>
 *         &lt;element name="Purp" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.06}Purpose2Choice" minOccurs="0"/>
 *         &lt;element name="RgltryRptg" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.06}RegulatoryReporting3" maxOccurs="10" minOccurs="0"/>
 *         &lt;element name="Tax" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.06}TaxInformation3" minOccurs="0"/>
 *         &lt;element name="RltdRmtInf" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.06}RemittanceLocation4" maxOccurs="10" minOccurs="0"/>
 *         &lt;element name="RmtInf" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.06}RemittanceInformation10" minOccurs="0"/>
 *         &lt;element name="SplmtryData" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.06}SupplementaryData1" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CreditTransferTransaction20", propOrder = {
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
    "rmtInf",
    "splmtryData"
})
public class PACreditTransferTransaction20 {

    @XmlElement(name = "PmtId", required = true)
    protected PAPaymentIdentification1 pmtId;
    @XmlElement(name = "PmtTpInf")
    protected PAPaymentTypeInformation19 pmtTpInf;
    @XmlElement(name = "Amt", required = true)
    protected PAAmountType4Choice amt;
    @XmlElement(name = "XchgRateInf")
    protected PAExchangeRate1 xchgRateInf;
    @XmlElement(name = "ChrgBr")
    @XmlSchemaType(name = "string")
    protected PAChargeBearerType1Code chrgBr;
    @XmlElement(name = "ChqInstr")
    protected PACheque7 chqInstr;
    @XmlElement(name = "UltmtDbtr")
    protected PAPartyIdentification43 ultmtDbtr;
    @XmlElement(name = "IntrmyAgt1")
    protected PABranchAndFinancialInstitutionIdentification5 intrmyAgt1;
    @XmlElement(name = "IntrmyAgt1Acct")
    protected PACashAccount24 intrmyAgt1Acct;
    @XmlElement(name = "IntrmyAgt2")
    protected PABranchAndFinancialInstitutionIdentification5 intrmyAgt2;
    @XmlElement(name = "IntrmyAgt2Acct")
    protected PACashAccount24 intrmyAgt2Acct;
    @XmlElement(name = "IntrmyAgt3")
    protected PABranchAndFinancialInstitutionIdentification5 intrmyAgt3;
    @XmlElement(name = "IntrmyAgt3Acct")
    protected PACashAccount24 intrmyAgt3Acct;
    @XmlElement(name = "CdtrAgt")
    protected PABranchAndFinancialInstitutionIdentification5 cdtrAgt;
    @XmlElement(name = "CdtrAgtAcct")
    protected PACashAccount24 cdtrAgtAcct;
    @XmlElement(name = "Cdtr")
    protected PAPartyIdentification43 cdtr;
    @XmlElement(name = "CdtrAcct")
    protected PACashAccount24 cdtrAcct;
    @XmlElement(name = "UltmtCdtr")
    protected PAPartyIdentification43 ultmtCdtr;
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
    protected List<PARemittanceLocation4> rltdRmtInf;
    @XmlElement(name = "RmtInf")
    protected PARemittanceInformation10 rmtInf;
    @XmlElement(name = "SplmtryData")
    protected List<PASupplementaryData1> splmtryData;

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
     *     {@link PAAmountType4Choice }
     *     
     */
    public PAAmountType4Choice getAmt() {
        return amt;
    }

    /**
     * Legt den Wert der amt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PAAmountType4Choice }
     *     
     */
    public void setAmt(PAAmountType4Choice value) {
        this.amt = value;
    }

    /**
     * Ruft den Wert der xchgRateInf-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PAExchangeRate1 }
     *     
     */
    public PAExchangeRate1 getXchgRateInf() {
        return xchgRateInf;
    }

    /**
     * Legt den Wert der xchgRateInf-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PAExchangeRate1 }
     *     
     */
    public void setXchgRateInf(PAExchangeRate1 value) {
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
     *     {@link PACheque7 }
     *     
     */
    public PACheque7 getChqInstr() {
        return chqInstr;
    }

    /**
     * Legt den Wert der chqInstr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACheque7 }
     *     
     */
    public void setChqInstr(PACheque7 value) {
        this.chqInstr = value;
    }

    /**
     * Ruft den Wert der ultmtDbtr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PAPartyIdentification43 }
     *     
     */
    public PAPartyIdentification43 getUltmtDbtr() {
        return ultmtDbtr;
    }

    /**
     * Legt den Wert der ultmtDbtr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PAPartyIdentification43 }
     *     
     */
    public void setUltmtDbtr(PAPartyIdentification43 value) {
        this.ultmtDbtr = value;
    }

    /**
     * Ruft den Wert der intrmyAgt1-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PABranchAndFinancialInstitutionIdentification5 }
     *     
     */
    public PABranchAndFinancialInstitutionIdentification5 getIntrmyAgt1() {
        return intrmyAgt1;
    }

    /**
     * Legt den Wert der intrmyAgt1-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PABranchAndFinancialInstitutionIdentification5 }
     *     
     */
    public void setIntrmyAgt1(PABranchAndFinancialInstitutionIdentification5 value) {
        this.intrmyAgt1 = value;
    }

    /**
     * Ruft den Wert der intrmyAgt1Acct-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACashAccount24 }
     *     
     */
    public PACashAccount24 getIntrmyAgt1Acct() {
        return intrmyAgt1Acct;
    }

    /**
     * Legt den Wert der intrmyAgt1Acct-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACashAccount24 }
     *     
     */
    public void setIntrmyAgt1Acct(PACashAccount24 value) {
        this.intrmyAgt1Acct = value;
    }

    /**
     * Ruft den Wert der intrmyAgt2-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PABranchAndFinancialInstitutionIdentification5 }
     *     
     */
    public PABranchAndFinancialInstitutionIdentification5 getIntrmyAgt2() {
        return intrmyAgt2;
    }

    /**
     * Legt den Wert der intrmyAgt2-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PABranchAndFinancialInstitutionIdentification5 }
     *     
     */
    public void setIntrmyAgt2(PABranchAndFinancialInstitutionIdentification5 value) {
        this.intrmyAgt2 = value;
    }

    /**
     * Ruft den Wert der intrmyAgt2Acct-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACashAccount24 }
     *     
     */
    public PACashAccount24 getIntrmyAgt2Acct() {
        return intrmyAgt2Acct;
    }

    /**
     * Legt den Wert der intrmyAgt2Acct-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACashAccount24 }
     *     
     */
    public void setIntrmyAgt2Acct(PACashAccount24 value) {
        this.intrmyAgt2Acct = value;
    }

    /**
     * Ruft den Wert der intrmyAgt3-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PABranchAndFinancialInstitutionIdentification5 }
     *     
     */
    public PABranchAndFinancialInstitutionIdentification5 getIntrmyAgt3() {
        return intrmyAgt3;
    }

    /**
     * Legt den Wert der intrmyAgt3-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PABranchAndFinancialInstitutionIdentification5 }
     *     
     */
    public void setIntrmyAgt3(PABranchAndFinancialInstitutionIdentification5 value) {
        this.intrmyAgt3 = value;
    }

    /**
     * Ruft den Wert der intrmyAgt3Acct-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACashAccount24 }
     *     
     */
    public PACashAccount24 getIntrmyAgt3Acct() {
        return intrmyAgt3Acct;
    }

    /**
     * Legt den Wert der intrmyAgt3Acct-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACashAccount24 }
     *     
     */
    public void setIntrmyAgt3Acct(PACashAccount24 value) {
        this.intrmyAgt3Acct = value;
    }

    /**
     * Ruft den Wert der cdtrAgt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PABranchAndFinancialInstitutionIdentification5 }
     *     
     */
    public PABranchAndFinancialInstitutionIdentification5 getCdtrAgt() {
        return cdtrAgt;
    }

    /**
     * Legt den Wert der cdtrAgt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PABranchAndFinancialInstitutionIdentification5 }
     *     
     */
    public void setCdtrAgt(PABranchAndFinancialInstitutionIdentification5 value) {
        this.cdtrAgt = value;
    }

    /**
     * Ruft den Wert der cdtrAgtAcct-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACashAccount24 }
     *     
     */
    public PACashAccount24 getCdtrAgtAcct() {
        return cdtrAgtAcct;
    }

    /**
     * Legt den Wert der cdtrAgtAcct-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACashAccount24 }
     *     
     */
    public void setCdtrAgtAcct(PACashAccount24 value) {
        this.cdtrAgtAcct = value;
    }

    /**
     * Ruft den Wert der cdtr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PAPartyIdentification43 }
     *     
     */
    public PAPartyIdentification43 getCdtr() {
        return cdtr;
    }

    /**
     * Legt den Wert der cdtr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PAPartyIdentification43 }
     *     
     */
    public void setCdtr(PAPartyIdentification43 value) {
        this.cdtr = value;
    }

    /**
     * Ruft den Wert der cdtrAcct-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACashAccount24 }
     *     
     */
    public PACashAccount24 getCdtrAcct() {
        return cdtrAcct;
    }

    /**
     * Legt den Wert der cdtrAcct-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACashAccount24 }
     *     
     */
    public void setCdtrAcct(PACashAccount24 value) {
        this.cdtrAcct = value;
    }

    /**
     * Ruft den Wert der ultmtCdtr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PAPartyIdentification43 }
     *     
     */
    public PAPartyIdentification43 getUltmtCdtr() {
        return ultmtCdtr;
    }

    /**
     * Legt den Wert der ultmtCdtr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PAPartyIdentification43 }
     *     
     */
    public void setUltmtCdtr(PAPartyIdentification43 value) {
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
     * {@link PARemittanceLocation4 }
     * 
     * 
     */
    public List<PARemittanceLocation4> getRltdRmtInf() {
        if (rltdRmtInf == null) {
            rltdRmtInf = new ArrayList<PARemittanceLocation4>();
        }
        return this.rltdRmtInf;
    }

    /**
     * Ruft den Wert der rmtInf-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PARemittanceInformation10 }
     *     
     */
    public PARemittanceInformation10 getRmtInf() {
        return rmtInf;
    }

    /**
     * Legt den Wert der rmtInf-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PARemittanceInformation10 }
     *     
     */
    public void setRmtInf(PARemittanceInformation10 value) {
        this.rmtInf = value;
    }

    /**
     * Gets the value of the splmtryData property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the splmtryData property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSplmtryData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PASupplementaryData1 }
     * 
     * 
     */
    public List<PASupplementaryData1> getSplmtryData() {
        if (splmtryData == null) {
            splmtryData = new ArrayList<PASupplementaryData1>();
        }
        return this.splmtryData;
    }

}
