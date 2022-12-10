//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2020.05.13 um 09:59:39 AM CEST 
//


package com.lp.server.schema.iso20022.ch.pain001V03;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java-Klasse fuer PaymentInstructionInformation3-CH complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="PaymentInstructionInformation3-CH">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PmtInfId" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}Max35Text-Swift"/>
 *         &lt;element name="PmtMtd" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}PaymentMethod3Code"/>
 *         &lt;element name="BtchBookg" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}BatchBookingIndicator" minOccurs="0"/>
 *         &lt;element name="NbOfTxs" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}Max15NumericText" minOccurs="0"/>
 *         &lt;element name="CtrlSum" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}DecimalNumber" minOccurs="0"/>
 *         &lt;element name="PmtTpInf" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}PaymentTypeInformation19-CH" minOccurs="0"/>
 *         &lt;element name="ReqdExctnDt" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}ISODate"/>
 *         &lt;element name="Dbtr" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}PartyIdentification32-CH"/>
 *         &lt;element name="DbtrAcct" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}CashAccount16-CH_IdTpCcy"/>
 *         &lt;element name="DbtrAgt" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}BranchAndFinancialInstitutionIdentification4-CH_BicOrClrId"/>
 *         &lt;element name="UltmtDbtr" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}PartyIdentification32-CH" minOccurs="0"/>
 *         &lt;element name="ChrgBr" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}ChargeBearerType1Code" minOccurs="0"/>
 *         &lt;element name="ChrgsAcct" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}CashAccount16-CH_IdAndCurrency" minOccurs="0"/>
 *         &lt;element name="CdtTrfTxInf" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}CreditTransferTransactionInformation10-CH" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PaymentInstructionInformation3-CH", propOrder = {
    "pmtInfId",
    "pmtMtd",
    "btchBookg",
    "nbOfTxs",
    "ctrlSum",
    "pmtTpInf",
    "reqdExctnDt",
    "dbtr",
    "dbtrAcct",
    "dbtrAgt",
    "ultmtDbtr",
    "chrgBr",
    "chrgsAcct",
    "cdtTrfTxInf"
})
public class PACHPaymentInstructionInformation3CH {

    @XmlElement(name = "PmtInfId", required = true)
    protected String pmtInfId;
    @XmlElement(name = "PmtMtd", required = true)
    @XmlSchemaType(name = "string")
    protected PACHPaymentMethod3Code pmtMtd;
    @XmlElement(name = "BtchBookg")
    protected Boolean btchBookg;
    @XmlElement(name = "NbOfTxs")
    protected String nbOfTxs;
    @XmlElement(name = "CtrlSum")
    protected BigDecimal ctrlSum;
    @XmlElement(name = "PmtTpInf")
    protected PACHPaymentTypeInformation19CH pmtTpInf;
    @XmlElement(name = "ReqdExctnDt", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar reqdExctnDt;
    @XmlElement(name = "Dbtr", required = true)
    protected PACHPartyIdentification32CH dbtr;
    @XmlElement(name = "DbtrAcct", required = true)
    protected PACHCashAccount16CHIdTpCcy dbtrAcct;
    @XmlElement(name = "DbtrAgt", required = true)
    protected PACHBranchAndFinancialInstitutionIdentification4CHBicOrClrId dbtrAgt;
    @XmlElement(name = "UltmtDbtr")
    protected PACHPartyIdentification32CH ultmtDbtr;
    @XmlElement(name = "ChrgBr")
    @XmlSchemaType(name = "string")
    protected PACHChargeBearerType1Code chrgBr;
    @XmlElement(name = "ChrgsAcct")
    protected PACHCashAccount16CHIdAndCurrency chrgsAcct;
    @XmlElement(name = "CdtTrfTxInf", required = true)
    protected List<PACHCreditTransferTransactionInformation10CH> cdtTrfTxInf;

    /**
     * Ruft den Wert der pmtInfId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPmtInfId() {
        return pmtInfId;
    }

    /**
     * Legt den Wert der pmtInfId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPmtInfId(String value) {
        this.pmtInfId = value;
    }

    /**
     * Ruft den Wert der pmtMtd-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACHPaymentMethod3Code }
     *     
     */
    public PACHPaymentMethod3Code getPmtMtd() {
        return pmtMtd;
    }

    /**
     * Legt den Wert der pmtMtd-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACHPaymentMethod3Code }
     *     
     */
    public void setPmtMtd(PACHPaymentMethod3Code value) {
        this.pmtMtd = value;
    }

    /**
     * Ruft den Wert der btchBookg-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isBtchBookg() {
        return btchBookg;
    }

    /**
     * Legt den Wert der btchBookg-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setBtchBookg(Boolean value) {
        this.btchBookg = value;
    }

    /**
     * Ruft den Wert der nbOfTxs-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNbOfTxs() {
        return nbOfTxs;
    }

    /**
     * Legt den Wert der nbOfTxs-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNbOfTxs(String value) {
        this.nbOfTxs = value;
    }

    /**
     * Ruft den Wert der ctrlSum-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCtrlSum() {
        return ctrlSum;
    }

    /**
     * Legt den Wert der ctrlSum-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCtrlSum(BigDecimal value) {
        this.ctrlSum = value;
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
     * Ruft den Wert der reqdExctnDt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getReqdExctnDt() {
        return reqdExctnDt;
    }

    /**
     * Legt den Wert der reqdExctnDt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setReqdExctnDt(XMLGregorianCalendar value) {
        this.reqdExctnDt = value;
    }

    /**
     * Ruft den Wert der dbtr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACHPartyIdentification32CH }
     *     
     */
    public PACHPartyIdentification32CH getDbtr() {
        return dbtr;
    }

    /**
     * Legt den Wert der dbtr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACHPartyIdentification32CH }
     *     
     */
    public void setDbtr(PACHPartyIdentification32CH value) {
        this.dbtr = value;
    }

    /**
     * Ruft den Wert der dbtrAcct-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACHCashAccount16CHIdTpCcy }
     *     
     */
    public PACHCashAccount16CHIdTpCcy getDbtrAcct() {
        return dbtrAcct;
    }

    /**
     * Legt den Wert der dbtrAcct-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACHCashAccount16CHIdTpCcy }
     *     
     */
    public void setDbtrAcct(PACHCashAccount16CHIdTpCcy value) {
        this.dbtrAcct = value;
    }

    /**
     * Ruft den Wert der dbtrAgt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACHBranchAndFinancialInstitutionIdentification4CHBicOrClrId }
     *     
     */
    public PACHBranchAndFinancialInstitutionIdentification4CHBicOrClrId getDbtrAgt() {
        return dbtrAgt;
    }

    /**
     * Legt den Wert der dbtrAgt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACHBranchAndFinancialInstitutionIdentification4CHBicOrClrId }
     *     
     */
    public void setDbtrAgt(PACHBranchAndFinancialInstitutionIdentification4CHBicOrClrId value) {
        this.dbtrAgt = value;
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
     * Ruft den Wert der chrgsAcct-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACHCashAccount16CHIdAndCurrency }
     *     
     */
    public PACHCashAccount16CHIdAndCurrency getChrgsAcct() {
        return chrgsAcct;
    }

    /**
     * Legt den Wert der chrgsAcct-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACHCashAccount16CHIdAndCurrency }
     *     
     */
    public void setChrgsAcct(PACHCashAccount16CHIdAndCurrency value) {
        this.chrgsAcct = value;
    }

    /**
     * Gets the value of the cdtTrfTxInf property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the cdtTrfTxInf property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCdtTrfTxInf().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PACHCreditTransferTransactionInformation10CH }
     * 
     * 
     */
    public List<PACHCreditTransferTransactionInformation10CH> getCdtTrfTxInf() {
        if (cdtTrfTxInf == null) {
            cdtTrfTxInf = new ArrayList<PACHCreditTransferTransactionInformation10CH>();
        }
        return this.cdtTrfTxInf;
    }

}
