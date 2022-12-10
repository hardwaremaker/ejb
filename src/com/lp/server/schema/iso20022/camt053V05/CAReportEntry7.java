//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2015.06.25 um 02:54:36 PM CEST 
//


package com.lp.server.schema.iso20022.camt053V05;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer ReportEntry7 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="ReportEntry7">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="NtryRef" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}Max35Text" minOccurs="0"/>
 *         &lt;element name="Amt" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}ActiveOrHistoricCurrencyAndAmount"/>
 *         &lt;element name="CdtDbtInd" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}CreditDebitCode"/>
 *         &lt;element name="RvslInd" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}TrueFalseIndicator" minOccurs="0"/>
 *         &lt;element name="Sts" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}EntryStatus2Code"/>
 *         &lt;element name="BookgDt" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}DateAndDateTimeChoice" minOccurs="0"/>
 *         &lt;element name="ValDt" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}DateAndDateTimeChoice" minOccurs="0"/>
 *         &lt;element name="AcctSvcrRef" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}Max35Text" minOccurs="0"/>
 *         &lt;element name="Avlbty" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}CashBalanceAvailability2" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="BkTxCd" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}BankTransactionCodeStructure4"/>
 *         &lt;element name="ComssnWvrInd" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}YesNoIndicator" minOccurs="0"/>
 *         &lt;element name="AddtlInfInd" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}MessageIdentification2" minOccurs="0"/>
 *         &lt;element name="AmtDtls" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}AmountAndCurrencyExchange3" minOccurs="0"/>
 *         &lt;element name="Chrgs" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}Charges4" minOccurs="0"/>
 *         &lt;element name="TechInptChanl" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}TechnicalInputChannel1Choice" minOccurs="0"/>
 *         &lt;element name="Intrst" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}TransactionInterest3" minOccurs="0"/>
 *         &lt;element name="CardTx" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}CardEntry2" minOccurs="0"/>
 *         &lt;element name="NtryDtls" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}EntryDetails6" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="AddtlNtryInf" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}Max500Text" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReportEntry7", propOrder = {
    "ntryRef",
    "amt",
    "cdtDbtInd",
    "rvslInd",
    "sts",
    "bookgDt",
    "valDt",
    "acctSvcrRef",
    "avlbty",
    "bkTxCd",
    "comssnWvrInd",
    "addtlInfInd",
    "amtDtls",
    "chrgs",
    "techInptChanl",
    "intrst",
    "cardTx",
    "ntryDtls",
    "addtlNtryInf"
})
public class CAReportEntry7 {

    @XmlElement(name = "NtryRef")
    protected String ntryRef;
    @XmlElement(name = "Amt", required = true)
    protected CAActiveOrHistoricCurrencyAndAmount amt;
    @XmlElement(name = "CdtDbtInd", required = true)
    @XmlSchemaType(name = "string")
    protected CACreditDebitCode cdtDbtInd;
    @XmlElement(name = "RvslInd")
    protected Boolean rvslInd;
    @XmlElement(name = "Sts", required = true)
    @XmlSchemaType(name = "string")
    protected CAEntryStatus2Code sts;
    @XmlElement(name = "BookgDt")
    protected CADateAndDateTimeChoice bookgDt;
    @XmlElement(name = "ValDt")
    protected CADateAndDateTimeChoice valDt;
    @XmlElement(name = "AcctSvcrRef")
    protected String acctSvcrRef;
    @XmlElement(name = "Avlbty")
    protected List<CACashBalanceAvailability2> avlbty;
    @XmlElement(name = "BkTxCd", required = true)
    protected CABankTransactionCodeStructure4 bkTxCd;
    @XmlElement(name = "ComssnWvrInd")
    protected Boolean comssnWvrInd;
    @XmlElement(name = "AddtlInfInd")
    protected CAMessageIdentification2 addtlInfInd;
    @XmlElement(name = "AmtDtls")
    protected CAAmountAndCurrencyExchange3 amtDtls;
    @XmlElement(name = "Chrgs")
    protected CACharges4 chrgs;
    @XmlElement(name = "TechInptChanl")
    protected CATechnicalInputChannel1Choice techInptChanl;
    @XmlElement(name = "Intrst")
    protected CATransactionInterest3 intrst;
    @XmlElement(name = "CardTx")
    protected CACardEntry2 cardTx;
    @XmlElement(name = "NtryDtls")
    protected List<CAEntryDetails6> ntryDtls;
    @XmlElement(name = "AddtlNtryInf")
    protected String addtlNtryInf;

    /**
     * Ruft den Wert der ntryRef-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNtryRef() {
        return ntryRef;
    }

    /**
     * Legt den Wert der ntryRef-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNtryRef(String value) {
        this.ntryRef = value;
    }

    /**
     * Ruft den Wert der amt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CAActiveOrHistoricCurrencyAndAmount }
     *     
     */
    public CAActiveOrHistoricCurrencyAndAmount getAmt() {
        return amt;
    }

    /**
     * Legt den Wert der amt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CAActiveOrHistoricCurrencyAndAmount }
     *     
     */
    public void setAmt(CAActiveOrHistoricCurrencyAndAmount value) {
        this.amt = value;
    }

    /**
     * Ruft den Wert der cdtDbtInd-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CACreditDebitCode }
     *     
     */
    public CACreditDebitCode getCdtDbtInd() {
        return cdtDbtInd;
    }

    /**
     * Legt den Wert der cdtDbtInd-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CACreditDebitCode }
     *     
     */
    public void setCdtDbtInd(CACreditDebitCode value) {
        this.cdtDbtInd = value;
    }

    /**
     * Ruft den Wert der rvslInd-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isRvslInd() {
        return rvslInd;
    }

    /**
     * Legt den Wert der rvslInd-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRvslInd(Boolean value) {
        this.rvslInd = value;
    }

    /**
     * Ruft den Wert der sts-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CAEntryStatus2Code }
     *     
     */
    public CAEntryStatus2Code getSts() {
        return sts;
    }

    /**
     * Legt den Wert der sts-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CAEntryStatus2Code }
     *     
     */
    public void setSts(CAEntryStatus2Code value) {
        this.sts = value;
    }

    /**
     * Ruft den Wert der bookgDt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CADateAndDateTimeChoice }
     *     
     */
    public CADateAndDateTimeChoice getBookgDt() {
        return bookgDt;
    }

    /**
     * Legt den Wert der bookgDt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CADateAndDateTimeChoice }
     *     
     */
    public void setBookgDt(CADateAndDateTimeChoice value) {
        this.bookgDt = value;
    }

    /**
     * Ruft den Wert der valDt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CADateAndDateTimeChoice }
     *     
     */
    public CADateAndDateTimeChoice getValDt() {
        return valDt;
    }

    /**
     * Legt den Wert der valDt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CADateAndDateTimeChoice }
     *     
     */
    public void setValDt(CADateAndDateTimeChoice value) {
        this.valDt = value;
    }

    /**
     * Ruft den Wert der acctSvcrRef-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAcctSvcrRef() {
        return acctSvcrRef;
    }

    /**
     * Legt den Wert der acctSvcrRef-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAcctSvcrRef(String value) {
        this.acctSvcrRef = value;
    }

    /**
     * Gets the value of the avlbty property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the avlbty property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAvlbty().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CACashBalanceAvailability2 }
     * 
     * 
     */
    public List<CACashBalanceAvailability2> getAvlbty() {
        if (avlbty == null) {
            avlbty = new ArrayList<CACashBalanceAvailability2>();
        }
        return this.avlbty;
    }

    /**
     * Ruft den Wert der bkTxCd-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CABankTransactionCodeStructure4 }
     *     
     */
    public CABankTransactionCodeStructure4 getBkTxCd() {
        return bkTxCd;
    }

    /**
     * Legt den Wert der bkTxCd-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CABankTransactionCodeStructure4 }
     *     
     */
    public void setBkTxCd(CABankTransactionCodeStructure4 value) {
        this.bkTxCd = value;
    }

    /**
     * Ruft den Wert der comssnWvrInd-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isComssnWvrInd() {
        return comssnWvrInd;
    }

    /**
     * Legt den Wert der comssnWvrInd-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setComssnWvrInd(Boolean value) {
        this.comssnWvrInd = value;
    }

    /**
     * Ruft den Wert der addtlInfInd-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CAMessageIdentification2 }
     *     
     */
    public CAMessageIdentification2 getAddtlInfInd() {
        return addtlInfInd;
    }

    /**
     * Legt den Wert der addtlInfInd-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CAMessageIdentification2 }
     *     
     */
    public void setAddtlInfInd(CAMessageIdentification2 value) {
        this.addtlInfInd = value;
    }

    /**
     * Ruft den Wert der amtDtls-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CAAmountAndCurrencyExchange3 }
     *     
     */
    public CAAmountAndCurrencyExchange3 getAmtDtls() {
        return amtDtls;
    }

    /**
     * Legt den Wert der amtDtls-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CAAmountAndCurrencyExchange3 }
     *     
     */
    public void setAmtDtls(CAAmountAndCurrencyExchange3 value) {
        this.amtDtls = value;
    }

    /**
     * Ruft den Wert der chrgs-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CACharges4 }
     *     
     */
    public CACharges4 getChrgs() {
        return chrgs;
    }

    /**
     * Legt den Wert der chrgs-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CACharges4 }
     *     
     */
    public void setChrgs(CACharges4 value) {
        this.chrgs = value;
    }

    /**
     * Ruft den Wert der techInptChanl-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CATechnicalInputChannel1Choice }
     *     
     */
    public CATechnicalInputChannel1Choice getTechInptChanl() {
        return techInptChanl;
    }

    /**
     * Legt den Wert der techInptChanl-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CATechnicalInputChannel1Choice }
     *     
     */
    public void setTechInptChanl(CATechnicalInputChannel1Choice value) {
        this.techInptChanl = value;
    }

    /**
     * Ruft den Wert der intrst-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CATransactionInterest3 }
     *     
     */
    public CATransactionInterest3 getIntrst() {
        return intrst;
    }

    /**
     * Legt den Wert der intrst-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CATransactionInterest3 }
     *     
     */
    public void setIntrst(CATransactionInterest3 value) {
        this.intrst = value;
    }

    /**
     * Ruft den Wert der cardTx-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CACardEntry2 }
     *     
     */
    public CACardEntry2 getCardTx() {
        return cardTx;
    }

    /**
     * Legt den Wert der cardTx-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CACardEntry2 }
     *     
     */
    public void setCardTx(CACardEntry2 value) {
        this.cardTx = value;
    }

    /**
     * Gets the value of the ntryDtls property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ntryDtls property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNtryDtls().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CAEntryDetails6 }
     * 
     * 
     */
    public List<CAEntryDetails6> getNtryDtls() {
        if (ntryDtls == null) {
            ntryDtls = new ArrayList<CAEntryDetails6>();
        }
        return this.ntryDtls;
    }

    /**
     * Ruft den Wert der addtlNtryInf-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddtlNtryInf() {
        return addtlNtryInf;
    }

    /**
     * Legt den Wert der addtlNtryInf-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddtlNtryInf(String value) {
        this.addtlNtryInf = value;
    }

}
