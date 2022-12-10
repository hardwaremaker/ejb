//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2020.05.13 um 09:59:39 AM CEST 
//


package com.lp.server.schema.iso20022.ch.pain001V03;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer CreditTransferTransactionInformation10-CH complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="CreditTransferTransactionInformation10-CH">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PmtId" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}PaymentIdentification1"/>
 *         &lt;element name="PmtTpInf" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}PaymentTypeInformation19-CH" minOccurs="0"/>
 *         &lt;element name="Amt" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}AmountType3Choice"/>
 *         &lt;element name="XchgRateInf" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}ExchangeRateInformation1" minOccurs="0"/>
 *         &lt;element name="ChrgBr" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}ChargeBearerType1Code" minOccurs="0"/>
 *         &lt;element name="ChqInstr" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}Cheque6-CH" minOccurs="0"/>
 *         &lt;element name="UltmtDbtr" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}PartyIdentification32-CH" minOccurs="0"/>
 *         &lt;element name="IntrmyAgt1" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}BranchAndFinancialInstitutionIdentification4-CH" minOccurs="0"/>
 *         &lt;element name="CdtrAgt" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}BranchAndFinancialInstitutionIdentification4-CH" minOccurs="0"/>
 *         &lt;element name="Cdtr" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}PartyIdentification32-CH_Name" minOccurs="0"/>
 *         &lt;element name="CdtrAcct" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}CashAccount16-CH_Id" minOccurs="0"/>
 *         &lt;element name="UltmtCdtr" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}PartyIdentification32-CH_Name" minOccurs="0"/>
 *         &lt;element name="InstrForCdtrAgt" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}InstructionForCreditorAgent1" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="InstrForDbtrAgt" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}Max140Text" minOccurs="0"/>
 *         &lt;element name="Purp" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}Purpose2-CH_Code" minOccurs="0"/>
 *         &lt;element name="RgltryRptg" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}RegulatoryReporting3" maxOccurs="10" minOccurs="0"/>
 *         &lt;element name="RmtInf" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}RemittanceInformation5-CH" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CreditTransferTransactionInformation10-CH", propOrder = {
    "pmtId",
    "pmtTpInf",
    "amt",
    "xchgRateInf",
    "chrgBr",
    "chqInstr",
    "ultmtDbtr",
    "intrmyAgt1",
    "cdtrAgt",
    "cdtr",
    "cdtrAcct",
    "ultmtCdtr",
    "instrForCdtrAgt",
    "instrForDbtrAgt",
    "purp",
    "rgltryRptg",
    "rmtInf"
})
public class PACHCreditTransferTransactionInformation10CH {

    @XmlElement(name = "PmtId", required = true)
    protected PACHPaymentIdentification1 pmtId;
    @XmlElement(name = "PmtTpInf")
    protected PACHPaymentTypeInformation19CH pmtTpInf;
    @XmlElement(name = "Amt", required = true)
    protected PACHAmountType3Choice amt;
    @XmlElement(name = "XchgRateInf")
    protected PACHExchangeRateInformation1 xchgRateInf;
    @XmlElement(name = "ChrgBr")
    @XmlSchemaType(name = "string")
    protected PACHChargeBearerType1Code chrgBr;
    @XmlElement(name = "ChqInstr")
    protected PACHCheque6CH chqInstr;
    @XmlElement(name = "UltmtDbtr")
    protected PACHPartyIdentification32CH ultmtDbtr;
    @XmlElement(name = "IntrmyAgt1")
    protected PACHBranchAndFinancialInstitutionIdentification4CH intrmyAgt1;
    @XmlElement(name = "CdtrAgt")
    protected PACHBranchAndFinancialInstitutionIdentification4CH cdtrAgt;
    @XmlElement(name = "Cdtr")
    protected PACHPartyIdentification32CHName cdtr;
    @XmlElement(name = "CdtrAcct")
    protected PACHCashAccount16CHId cdtrAcct;
    @XmlElement(name = "UltmtCdtr")
    protected PACHPartyIdentification32CHName ultmtCdtr;
    @XmlElement(name = "InstrForCdtrAgt")
    protected List<PACHInstructionForCreditorAgent1> instrForCdtrAgt;
    @XmlElement(name = "InstrForDbtrAgt")
    protected String instrForDbtrAgt;
    @XmlElement(name = "Purp")
    protected PACHPurpose2CHCode purp;
    @XmlElement(name = "RgltryRptg")
    protected List<PACHRegulatoryReporting3> rgltryRptg;
    @XmlElement(name = "RmtInf")
    protected PACHRemittanceInformation5CH rmtInf;

    /**
     * Ruft den Wert der pmtId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACHPaymentIdentification1 }
     *     
     */
    public PACHPaymentIdentification1 getPmtId() {
        return pmtId;
    }

    /**
     * Legt den Wert der pmtId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACHPaymentIdentification1 }
     *     
     */
    public void setPmtId(PACHPaymentIdentification1 value) {
        this.pmtId = value;
    }

    /**
     * Ruft den Wert der pmtTpInf-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACHPaymentTypeInformation19CH }
     *     
     */
    public PACHPaymentTypeInformation19CH getPmtTpInf() {
        return pmtTpInf;
    }

    /**
     * Legt den Wert der pmtTpInf-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACHPaymentTypeInformation19CH }
     *     
     */
    public void setPmtTpInf(PACHPaymentTypeInformation19CH value) {
        this.pmtTpInf = value;
    }

    /**
     * Ruft den Wert der amt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACHAmountType3Choice }
     *     
     */
    public PACHAmountType3Choice getAmt() {
        return amt;
    }

    /**
     * Legt den Wert der amt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACHAmountType3Choice }
     *     
     */
    public void setAmt(PACHAmountType3Choice value) {
        this.amt = value;
    }

    /**
     * Ruft den Wert der xchgRateInf-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACHExchangeRateInformation1 }
     *     
     */
    public PACHExchangeRateInformation1 getXchgRateInf() {
        return xchgRateInf;
    }

    /**
     * Legt den Wert der xchgRateInf-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACHExchangeRateInformation1 }
     *     
     */
    public void setXchgRateInf(PACHExchangeRateInformation1 value) {
        this.xchgRateInf = value;
    }

    /**
     * Ruft den Wert der chrgBr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACHChargeBearerType1Code }
     *     
     */
    public PACHChargeBearerType1Code getChrgBr() {
        return chrgBr;
    }

    /**
     * Legt den Wert der chrgBr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACHChargeBearerType1Code }
     *     
     */
    public void setChrgBr(PACHChargeBearerType1Code value) {
        this.chrgBr = value;
    }

    /**
     * Ruft den Wert der chqInstr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACHCheque6CH }
     *     
     */
    public PACHCheque6CH getChqInstr() {
        return chqInstr;
    }

    /**
     * Legt den Wert der chqInstr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACHCheque6CH }
     *     
     */
    public void setChqInstr(PACHCheque6CH value) {
        this.chqInstr = value;
    }

    /**
     * Ruft den Wert der ultmtDbtr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACHPartyIdentification32CH }
     *     
     */
    public PACHPartyIdentification32CH getUltmtDbtr() {
        return ultmtDbtr;
    }

    /**
     * Legt den Wert der ultmtDbtr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACHPartyIdentification32CH }
     *     
     */
    public void setUltmtDbtr(PACHPartyIdentification32CH value) {
        this.ultmtDbtr = value;
    }

    /**
     * Ruft den Wert der intrmyAgt1-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACHBranchAndFinancialInstitutionIdentification4CH }
     *     
     */
    public PACHBranchAndFinancialInstitutionIdentification4CH getIntrmyAgt1() {
        return intrmyAgt1;
    }

    /**
     * Legt den Wert der intrmyAgt1-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACHBranchAndFinancialInstitutionIdentification4CH }
     *     
     */
    public void setIntrmyAgt1(PACHBranchAndFinancialInstitutionIdentification4CH value) {
        this.intrmyAgt1 = value;
    }

    /**
     * Ruft den Wert der cdtrAgt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACHBranchAndFinancialInstitutionIdentification4CH }
     *     
     */
    public PACHBranchAndFinancialInstitutionIdentification4CH getCdtrAgt() {
        return cdtrAgt;
    }

    /**
     * Legt den Wert der cdtrAgt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACHBranchAndFinancialInstitutionIdentification4CH }
     *     
     */
    public void setCdtrAgt(PACHBranchAndFinancialInstitutionIdentification4CH value) {
        this.cdtrAgt = value;
    }

    /**
     * Ruft den Wert der cdtr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACHPartyIdentification32CHName }
     *     
     */
    public PACHPartyIdentification32CHName getCdtr() {
        return cdtr;
    }

    /**
     * Legt den Wert der cdtr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACHPartyIdentification32CHName }
     *     
     */
    public void setCdtr(PACHPartyIdentification32CHName value) {
        this.cdtr = value;
    }

    /**
     * Ruft den Wert der cdtrAcct-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACHCashAccount16CHId }
     *     
     */
    public PACHCashAccount16CHId getCdtrAcct() {
        return cdtrAcct;
    }

    /**
     * Legt den Wert der cdtrAcct-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACHCashAccount16CHId }
     *     
     */
    public void setCdtrAcct(PACHCashAccount16CHId value) {
        this.cdtrAcct = value;
    }

    /**
     * Ruft den Wert der ultmtCdtr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACHPartyIdentification32CHName }
     *     
     */
    public PACHPartyIdentification32CHName getUltmtCdtr() {
        return ultmtCdtr;
    }

    /**
     * Legt den Wert der ultmtCdtr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACHPartyIdentification32CHName }
     *     
     */
    public void setUltmtCdtr(PACHPartyIdentification32CHName value) {
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
     * {@link PACHInstructionForCreditorAgent1 }
     * 
     * 
     */
    public List<PACHInstructionForCreditorAgent1> getInstrForCdtrAgt() {
        if (instrForCdtrAgt == null) {
            instrForCdtrAgt = new ArrayList<PACHInstructionForCreditorAgent1>();
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
     *     {@link PACHPurpose2CHCode }
     *     
     */
    public PACHPurpose2CHCode getPurp() {
        return purp;
    }

    /**
     * Legt den Wert der purp-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACHPurpose2CHCode }
     *     
     */
    public void setPurp(PACHPurpose2CHCode value) {
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
     * {@link PACHRegulatoryReporting3 }
     * 
     * 
     */
    public List<PACHRegulatoryReporting3> getRgltryRptg() {
        if (rgltryRptg == null) {
            rgltryRptg = new ArrayList<PACHRegulatoryReporting3>();
        }
        return this.rgltryRptg;
    }

    /**
     * Ruft den Wert der rmtInf-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACHRemittanceInformation5CH }
     *     
     */
    public PACHRemittanceInformation5CH getRmtInf() {
        return rmtInf;
    }

    /**
     * Legt den Wert der rmtInf-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACHRemittanceInformation5CH }
     *     
     */
    public void setRmtInf(PACHRemittanceInformation5CH value) {
        this.rmtInf = value;
    }

}
