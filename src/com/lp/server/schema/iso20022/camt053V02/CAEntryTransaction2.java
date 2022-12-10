//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2015.06.29 um 04:43:26 PM CEST 
//


package com.lp.server.schema.iso20022.camt053V02;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer EntryTransaction2 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="EntryTransaction2">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Refs" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.02}TransactionReferences2" minOccurs="0"/>
 *         &lt;element name="AmtDtls" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.02}AmountAndCurrencyExchange3" minOccurs="0"/>
 *         &lt;element name="Avlbty" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.02}CashBalanceAvailability2" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="BkTxCd" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.02}BankTransactionCodeStructure4" minOccurs="0"/>
 *         &lt;element name="Chrgs" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.02}ChargesInformation6" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Intrst" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.02}TransactionInterest2" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="RltdPties" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.02}TransactionParty2" minOccurs="0"/>
 *         &lt;element name="RltdAgts" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.02}TransactionAgents2" minOccurs="0"/>
 *         &lt;element name="Purp" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.02}Purpose2Choice" minOccurs="0"/>
 *         &lt;element name="RltdRmtInf" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.02}RemittanceLocation2" maxOccurs="10" minOccurs="0"/>
 *         &lt;element name="RmtInf" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.02}RemittanceInformation5" minOccurs="0"/>
 *         &lt;element name="RltdDts" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.02}TransactionDates2" minOccurs="0"/>
 *         &lt;element name="RltdPric" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.02}TransactionPrice2Choice" minOccurs="0"/>
 *         &lt;element name="RltdQties" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.02}TransactionQuantities1Choice" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="FinInstrmId" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.02}SecurityIdentification4Choice" minOccurs="0"/>
 *         &lt;element name="Tax" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.02}TaxInformation3" minOccurs="0"/>
 *         &lt;element name="RtrInf" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.02}ReturnReasonInformation10" minOccurs="0"/>
 *         &lt;element name="CorpActn" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.02}CorporateAction1" minOccurs="0"/>
 *         &lt;element name="SfkpgAcct" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.02}CashAccount16" minOccurs="0"/>
 *         &lt;element name="AddtlTxInf" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.02}Max500Text" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EntryTransaction2", propOrder = {
    "refs",
    "amtDtls",
    "avlbty",
    "bkTxCd",
    "chrgs",
    "intrst",
    "rltdPties",
    "rltdAgts",
    "purp",
    "rltdRmtInf",
    "rmtInf",
    "rltdDts",
    "rltdPric",
    "rltdQties",
    "finInstrmId",
    "tax",
    "rtrInf",
    "corpActn",
    "sfkpgAcct",
    "addtlTxInf"
})
public class CAEntryTransaction2 {

    @XmlElement(name = "Refs")
    protected CATransactionReferences2 refs;
    @XmlElement(name = "AmtDtls")
    protected CAAmountAndCurrencyExchange3 amtDtls;
    @XmlElement(name = "Avlbty")
    protected List<CACashBalanceAvailability2> avlbty;
    @XmlElement(name = "BkTxCd")
    protected CABankTransactionCodeStructure4 bkTxCd;
    @XmlElement(name = "Chrgs")
    protected List<CAChargesInformation6> chrgs;
    @XmlElement(name = "Intrst")
    protected List<CATransactionInterest2> intrst;
    @XmlElement(name = "RltdPties")
    protected CATransactionParty2 rltdPties;
    @XmlElement(name = "RltdAgts")
    protected CATransactionAgents2 rltdAgts;
    @XmlElement(name = "Purp")
    protected CAPurpose2Choice purp;
    @XmlElement(name = "RltdRmtInf")
    protected List<CARemittanceLocation2> rltdRmtInf;
    @XmlElement(name = "RmtInf")
    protected CARemittanceInformation5 rmtInf;
    @XmlElement(name = "RltdDts")
    protected CATransactionDates2 rltdDts;
    @XmlElement(name = "RltdPric")
    protected CATransactionPrice2Choice rltdPric;
    @XmlElement(name = "RltdQties")
    protected List<CATransactionQuantities1Choice> rltdQties;
    @XmlElement(name = "FinInstrmId")
    protected CASecurityIdentification4Choice finInstrmId;
    @XmlElement(name = "Tax")
    protected CATaxInformation3 tax;
    @XmlElement(name = "RtrInf")
    protected CAReturnReasonInformation10 rtrInf;
    @XmlElement(name = "CorpActn")
    protected CACorporateAction1 corpActn;
    @XmlElement(name = "SfkpgAcct")
    protected CACashAccount16 sfkpgAcct;
    @XmlElement(name = "AddtlTxInf")
    protected String addtlTxInf;

    /**
     * Ruft den Wert der refs-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CATransactionReferences2 }
     *     
     */
    public CATransactionReferences2 getRefs() {
        return refs;
    }

    /**
     * Legt den Wert der refs-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CATransactionReferences2 }
     *     
     */
    public void setRefs(CATransactionReferences2 value) {
        this.refs = value;
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
     * Gets the value of the chrgs property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the chrgs property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getChrgs().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CAChargesInformation6 }
     * 
     * 
     */
    public List<CAChargesInformation6> getChrgs() {
        if (chrgs == null) {
            chrgs = new ArrayList<CAChargesInformation6>();
        }
        return this.chrgs;
    }

    /**
     * Gets the value of the intrst property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the intrst property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIntrst().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CATransactionInterest2 }
     * 
     * 
     */
    public List<CATransactionInterest2> getIntrst() {
        if (intrst == null) {
            intrst = new ArrayList<CATransactionInterest2>();
        }
        return this.intrst;
    }

    /**
     * Ruft den Wert der rltdPties-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CATransactionParty2 }
     *     
     */
    public CATransactionParty2 getRltdPties() {
        return rltdPties;
    }

    /**
     * Legt den Wert der rltdPties-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CATransactionParty2 }
     *     
     */
    public void setRltdPties(CATransactionParty2 value) {
        this.rltdPties = value;
    }

    /**
     * Ruft den Wert der rltdAgts-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CATransactionAgents2 }
     *     
     */
    public CATransactionAgents2 getRltdAgts() {
        return rltdAgts;
    }

    /**
     * Legt den Wert der rltdAgts-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CATransactionAgents2 }
     *     
     */
    public void setRltdAgts(CATransactionAgents2 value) {
        this.rltdAgts = value;
    }

    /**
     * Ruft den Wert der purp-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CAPurpose2Choice }
     *     
     */
    public CAPurpose2Choice getPurp() {
        return purp;
    }

    /**
     * Legt den Wert der purp-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CAPurpose2Choice }
     *     
     */
    public void setPurp(CAPurpose2Choice value) {
        this.purp = value;
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
     * {@link CARemittanceLocation2 }
     * 
     * 
     */
    public List<CARemittanceLocation2> getRltdRmtInf() {
        if (rltdRmtInf == null) {
            rltdRmtInf = new ArrayList<CARemittanceLocation2>();
        }
        return this.rltdRmtInf;
    }

    /**
     * Ruft den Wert der rmtInf-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CARemittanceInformation5 }
     *     
     */
    public CARemittanceInformation5 getRmtInf() {
        return rmtInf;
    }

    /**
     * Legt den Wert der rmtInf-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CARemittanceInformation5 }
     *     
     */
    public void setRmtInf(CARemittanceInformation5 value) {
        this.rmtInf = value;
    }

    /**
     * Ruft den Wert der rltdDts-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CATransactionDates2 }
     *     
     */
    public CATransactionDates2 getRltdDts() {
        return rltdDts;
    }

    /**
     * Legt den Wert der rltdDts-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CATransactionDates2 }
     *     
     */
    public void setRltdDts(CATransactionDates2 value) {
        this.rltdDts = value;
    }

    /**
     * Ruft den Wert der rltdPric-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CATransactionPrice2Choice }
     *     
     */
    public CATransactionPrice2Choice getRltdPric() {
        return rltdPric;
    }

    /**
     * Legt den Wert der rltdPric-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CATransactionPrice2Choice }
     *     
     */
    public void setRltdPric(CATransactionPrice2Choice value) {
        this.rltdPric = value;
    }

    /**
     * Gets the value of the rltdQties property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the rltdQties property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRltdQties().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CATransactionQuantities1Choice }
     * 
     * 
     */
    public List<CATransactionQuantities1Choice> getRltdQties() {
        if (rltdQties == null) {
            rltdQties = new ArrayList<CATransactionQuantities1Choice>();
        }
        return this.rltdQties;
    }

    /**
     * Ruft den Wert der finInstrmId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CASecurityIdentification4Choice }
     *     
     */
    public CASecurityIdentification4Choice getFinInstrmId() {
        return finInstrmId;
    }

    /**
     * Legt den Wert der finInstrmId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CASecurityIdentification4Choice }
     *     
     */
    public void setFinInstrmId(CASecurityIdentification4Choice value) {
        this.finInstrmId = value;
    }

    /**
     * Ruft den Wert der tax-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CATaxInformation3 }
     *     
     */
    public CATaxInformation3 getTax() {
        return tax;
    }

    /**
     * Legt den Wert der tax-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CATaxInformation3 }
     *     
     */
    public void setTax(CATaxInformation3 value) {
        this.tax = value;
    }

    /**
     * Ruft den Wert der rtrInf-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CAReturnReasonInformation10 }
     *     
     */
    public CAReturnReasonInformation10 getRtrInf() {
        return rtrInf;
    }

    /**
     * Legt den Wert der rtrInf-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CAReturnReasonInformation10 }
     *     
     */
    public void setRtrInf(CAReturnReasonInformation10 value) {
        this.rtrInf = value;
    }

    /**
     * Ruft den Wert der corpActn-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CACorporateAction1 }
     *     
     */
    public CACorporateAction1 getCorpActn() {
        return corpActn;
    }

    /**
     * Legt den Wert der corpActn-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CACorporateAction1 }
     *     
     */
    public void setCorpActn(CACorporateAction1 value) {
        this.corpActn = value;
    }

    /**
     * Ruft den Wert der sfkpgAcct-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CACashAccount16 }
     *     
     */
    public CACashAccount16 getSfkpgAcct() {
        return sfkpgAcct;
    }

    /**
     * Legt den Wert der sfkpgAcct-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CACashAccount16 }
     *     
     */
    public void setSfkpgAcct(CACashAccount16 value) {
        this.sfkpgAcct = value;
    }

    /**
     * Ruft den Wert der addtlTxInf-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddtlTxInf() {
        return addtlTxInf;
    }

    /**
     * Legt den Wert der addtlTxInf-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddtlTxInf(String value) {
        this.addtlTxInf = value;
    }

}
