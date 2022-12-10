//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2017.02.21 um 11:13:49 AM CET 
//


package com.lp.server.schema.iso20022.pain008V02;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer DirectDebitTransactionInformation9 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="DirectDebitTransactionInformation9">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PmtId" type="{urn:iso:std:iso:20022:tech:xsd:pain.008.001.02}PaymentIdentification1"/>
 *         &lt;element name="PmtTpInf" type="{urn:iso:std:iso:20022:tech:xsd:pain.008.001.02}PaymentTypeInformation20" minOccurs="0"/>
 *         &lt;element name="InstdAmt" type="{urn:iso:std:iso:20022:tech:xsd:pain.008.001.02}ActiveOrHistoricCurrencyAndAmount"/>
 *         &lt;element name="ChrgBr" type="{urn:iso:std:iso:20022:tech:xsd:pain.008.001.02}ChargeBearerType1Code" minOccurs="0"/>
 *         &lt;element name="DrctDbtTx" type="{urn:iso:std:iso:20022:tech:xsd:pain.008.001.02}DirectDebitTransaction6" minOccurs="0"/>
 *         &lt;element name="UltmtCdtr" type="{urn:iso:std:iso:20022:tech:xsd:pain.008.001.02}PartyIdentification32" minOccurs="0"/>
 *         &lt;element name="DbtrAgt" type="{urn:iso:std:iso:20022:tech:xsd:pain.008.001.02}BranchAndFinancialInstitutionIdentification4"/>
 *         &lt;element name="DbtrAgtAcct" type="{urn:iso:std:iso:20022:tech:xsd:pain.008.001.02}CashAccount16" minOccurs="0"/>
 *         &lt;element name="Dbtr" type="{urn:iso:std:iso:20022:tech:xsd:pain.008.001.02}PartyIdentification32"/>
 *         &lt;element name="DbtrAcct" type="{urn:iso:std:iso:20022:tech:xsd:pain.008.001.02}CashAccount16"/>
 *         &lt;element name="UltmtDbtr" type="{urn:iso:std:iso:20022:tech:xsd:pain.008.001.02}PartyIdentification32" minOccurs="0"/>
 *         &lt;element name="InstrForCdtrAgt" type="{urn:iso:std:iso:20022:tech:xsd:pain.008.001.02}Max140Text" minOccurs="0"/>
 *         &lt;element name="Purp" type="{urn:iso:std:iso:20022:tech:xsd:pain.008.001.02}Purpose2Choice" minOccurs="0"/>
 *         &lt;element name="RgltryRptg" type="{urn:iso:std:iso:20022:tech:xsd:pain.008.001.02}RegulatoryReporting3" maxOccurs="10" minOccurs="0"/>
 *         &lt;element name="Tax" type="{urn:iso:std:iso:20022:tech:xsd:pain.008.001.02}TaxInformation3" minOccurs="0"/>
 *         &lt;element name="RltdRmtInf" type="{urn:iso:std:iso:20022:tech:xsd:pain.008.001.02}RemittanceLocation2" maxOccurs="10" minOccurs="0"/>
 *         &lt;element name="RmtInf" type="{urn:iso:std:iso:20022:tech:xsd:pain.008.001.02}RemittanceInformation5" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DirectDebitTransactionInformation9", propOrder = {
    "pmtId",
    "pmtTpInf",
    "instdAmt",
    "chrgBr",
    "drctDbtTx",
    "ultmtCdtr",
    "dbtrAgt",
    "dbtrAgtAcct",
    "dbtr",
    "dbtrAcct",
    "ultmtDbtr",
    "instrForCdtrAgt",
    "purp",
    "rgltryRptg",
    "tax",
    "rltdRmtInf",
    "rmtInf"
})
public class PADirectDebitTransactionInformation9 {

    @XmlElement(name = "PmtId", required = true)
    protected PAPaymentIdentification1 pmtId;
    @XmlElement(name = "PmtTpInf")
    protected PAPaymentTypeInformation20 pmtTpInf;
    @XmlElement(name = "InstdAmt", required = true)
    protected PAActiveOrHistoricCurrencyAndAmount instdAmt;
    @XmlElement(name = "ChrgBr")
    @XmlSchemaType(name = "string")
    protected PAChargeBearerType1Code chrgBr;
    @XmlElement(name = "DrctDbtTx")
    protected PADirectDebitTransaction6 drctDbtTx;
    @XmlElement(name = "UltmtCdtr")
    protected PAPartyIdentification32 ultmtCdtr;
    @XmlElement(name = "DbtrAgt", required = true)
    protected PABranchAndFinancialInstitutionIdentification4 dbtrAgt;
    @XmlElement(name = "DbtrAgtAcct")
    protected PACashAccount16 dbtrAgtAcct;
    @XmlElement(name = "Dbtr", required = true)
    protected PAPartyIdentification32 dbtr;
    @XmlElement(name = "DbtrAcct", required = true)
    protected PACashAccount16 dbtrAcct;
    @XmlElement(name = "UltmtDbtr")
    protected PAPartyIdentification32 ultmtDbtr;
    @XmlElement(name = "InstrForCdtrAgt")
    protected String instrForCdtrAgt;
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
     *     {@link PAPaymentTypeInformation20 }
     *     
     */
    public PAPaymentTypeInformation20 getPmtTpInf() {
        return pmtTpInf;
    }

    /**
     * Legt den Wert der pmtTpInf-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PAPaymentTypeInformation20 }
     *     
     */
    public void setPmtTpInf(PAPaymentTypeInformation20 value) {
        this.pmtTpInf = value;
    }

    /**
     * Ruft den Wert der instdAmt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PAActiveOrHistoricCurrencyAndAmount }
     *     
     */
    public PAActiveOrHistoricCurrencyAndAmount getInstdAmt() {
        return instdAmt;
    }

    /**
     * Legt den Wert der instdAmt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PAActiveOrHistoricCurrencyAndAmount }
     *     
     */
    public void setInstdAmt(PAActiveOrHistoricCurrencyAndAmount value) {
        this.instdAmt = value;
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
     * Ruft den Wert der drctDbtTx-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PADirectDebitTransaction6 }
     *     
     */
    public PADirectDebitTransaction6 getDrctDbtTx() {
        return drctDbtTx;
    }

    /**
     * Legt den Wert der drctDbtTx-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PADirectDebitTransaction6 }
     *     
     */
    public void setDrctDbtTx(PADirectDebitTransaction6 value) {
        this.drctDbtTx = value;
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
     * Ruft den Wert der dbtrAgt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PABranchAndFinancialInstitutionIdentification4 }
     *     
     */
    public PABranchAndFinancialInstitutionIdentification4 getDbtrAgt() {
        return dbtrAgt;
    }

    /**
     * Legt den Wert der dbtrAgt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PABranchAndFinancialInstitutionIdentification4 }
     *     
     */
    public void setDbtrAgt(PABranchAndFinancialInstitutionIdentification4 value) {
        this.dbtrAgt = value;
    }

    /**
     * Ruft den Wert der dbtrAgtAcct-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACashAccount16 }
     *     
     */
    public PACashAccount16 getDbtrAgtAcct() {
        return dbtrAgtAcct;
    }

    /**
     * Legt den Wert der dbtrAgtAcct-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACashAccount16 }
     *     
     */
    public void setDbtrAgtAcct(PACashAccount16 value) {
        this.dbtrAgtAcct = value;
    }

    /**
     * Ruft den Wert der dbtr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PAPartyIdentification32 }
     *     
     */
    public PAPartyIdentification32 getDbtr() {
        return dbtr;
    }

    /**
     * Legt den Wert der dbtr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PAPartyIdentification32 }
     *     
     */
    public void setDbtr(PAPartyIdentification32 value) {
        this.dbtr = value;
    }

    /**
     * Ruft den Wert der dbtrAcct-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACashAccount16 }
     *     
     */
    public PACashAccount16 getDbtrAcct() {
        return dbtrAcct;
    }

    /**
     * Legt den Wert der dbtrAcct-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACashAccount16 }
     *     
     */
    public void setDbtrAcct(PACashAccount16 value) {
        this.dbtrAcct = value;
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
     * Ruft den Wert der instrForCdtrAgt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInstrForCdtrAgt() {
        return instrForCdtrAgt;
    }

    /**
     * Legt den Wert der instrForCdtrAgt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInstrForCdtrAgt(String value) {
        this.instrForCdtrAgt = value;
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
