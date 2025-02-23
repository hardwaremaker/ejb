//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.10.19 at 11:43:37 AM CEST 
//


package com.lp.server.schema.iso20022.camt052V02;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for EntryTransaction2 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EntryTransaction2">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Refs" type="{urn:iso:std:iso:20022:tech:xsd:camt.052.001.02}TransactionReferences2" minOccurs="0"/>
 *         &lt;element name="AmtDtls" type="{urn:iso:std:iso:20022:tech:xsd:camt.052.001.02}AmountAndCurrencyExchange3" minOccurs="0"/>
 *         &lt;element name="Avlbty" type="{urn:iso:std:iso:20022:tech:xsd:camt.052.001.02}CashBalanceAvailability2" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="BkTxCd" type="{urn:iso:std:iso:20022:tech:xsd:camt.052.001.02}BankTransactionCodeStructure4" minOccurs="0"/>
 *         &lt;element name="Chrgs" type="{urn:iso:std:iso:20022:tech:xsd:camt.052.001.02}ChargesInformation6" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Intrst" type="{urn:iso:std:iso:20022:tech:xsd:camt.052.001.02}TransactionInterest2" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="RltdPties" type="{urn:iso:std:iso:20022:tech:xsd:camt.052.001.02}TransactionParty2" minOccurs="0"/>
 *         &lt;element name="RltdAgts" type="{urn:iso:std:iso:20022:tech:xsd:camt.052.001.02}TransactionAgents2" minOccurs="0"/>
 *         &lt;element name="Purp" type="{urn:iso:std:iso:20022:tech:xsd:camt.052.001.02}Purpose2Choice" minOccurs="0"/>
 *         &lt;element name="RltdRmtInf" type="{urn:iso:std:iso:20022:tech:xsd:camt.052.001.02}RemittanceLocation2" maxOccurs="10" minOccurs="0"/>
 *         &lt;element name="RmtInf" type="{urn:iso:std:iso:20022:tech:xsd:camt.052.001.02}RemittanceInformation5" minOccurs="0"/>
 *         &lt;element name="RltdDts" type="{urn:iso:std:iso:20022:tech:xsd:camt.052.001.02}TransactionDates2" minOccurs="0"/>
 *         &lt;element name="RltdPric" type="{urn:iso:std:iso:20022:tech:xsd:camt.052.001.02}TransactionPrice2Choice" minOccurs="0"/>
 *         &lt;element name="RltdQties" type="{urn:iso:std:iso:20022:tech:xsd:camt.052.001.02}TransactionQuantities1Choice" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="FinInstrmId" type="{urn:iso:std:iso:20022:tech:xsd:camt.052.001.02}SecurityIdentification4Choice" minOccurs="0"/>
 *         &lt;element name="Tax" type="{urn:iso:std:iso:20022:tech:xsd:camt.052.001.02}TaxInformation3" minOccurs="0"/>
 *         &lt;element name="RtrInf" type="{urn:iso:std:iso:20022:tech:xsd:camt.052.001.02}ReturnReasonInformation10" minOccurs="0"/>
 *         &lt;element name="CorpActn" type="{urn:iso:std:iso:20022:tech:xsd:camt.052.001.02}CorporateAction1" minOccurs="0"/>
 *         &lt;element name="SfkpgAcct" type="{urn:iso:std:iso:20022:tech:xsd:camt.052.001.02}CashAccount16" minOccurs="0"/>
 *         &lt;element name="AddtlTxInf" type="{urn:iso:std:iso:20022:tech:xsd:camt.052.001.02}Max500Text" minOccurs="0"/>
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
public class CA052EntryTransaction2 {

    @XmlElement(name = "Refs")
    protected CA052TransactionReferences2 refs;
    @XmlElement(name = "AmtDtls")
    protected CA052AmountAndCurrencyExchange3 amtDtls;
    @XmlElement(name = "Avlbty")
    protected List<CA052CashBalanceAvailability2> avlbty;
    @XmlElement(name = "BkTxCd")
    protected CA052BankTransactionCodeStructure4 bkTxCd;
    @XmlElement(name = "Chrgs")
    protected List<CA052ChargesInformation6> chrgs;
    @XmlElement(name = "Intrst")
    protected List<CA052TransactionInterest2> intrst;
    @XmlElement(name = "RltdPties")
    protected CA052TransactionParty2 rltdPties;
    @XmlElement(name = "RltdAgts")
    protected CA052TransactionAgents2 rltdAgts;
    @XmlElement(name = "Purp")
    protected CA052Purpose2Choice purp;
    @XmlElement(name = "RltdRmtInf")
    protected List<CA052RemittanceLocation2> rltdRmtInf;
    @XmlElement(name = "RmtInf")
    protected CA052RemittanceInformation5 rmtInf;
    @XmlElement(name = "RltdDts")
    protected CA052TransactionDates2 rltdDts;
    @XmlElement(name = "RltdPric")
    protected CA052TransactionPrice2Choice rltdPric;
    @XmlElement(name = "RltdQties")
    protected List<CA052TransactionQuantities1Choice> rltdQties;
    @XmlElement(name = "FinInstrmId")
    protected CA052SecurityIdentification4Choice finInstrmId;
    @XmlElement(name = "Tax")
    protected CA052TaxInformation3 tax;
    @XmlElement(name = "RtrInf")
    protected CA052ReturnReasonInformation10 rtrInf;
    @XmlElement(name = "CorpActn")
    protected CA052CorporateAction1 corpActn;
    @XmlElement(name = "SfkpgAcct")
    protected CA052CashAccount16 sfkpgAcct;
    @XmlElement(name = "AddtlTxInf")
    protected String addtlTxInf;

    /**
     * Gets the value of the refs property.
     * 
     * @return
     *     possible object is
     *     {@link CA052TransactionReferences2 }
     *     
     */
    public CA052TransactionReferences2 getRefs() {
        return refs;
    }

    /**
     * Sets the value of the refs property.
     * 
     * @param value
     *     allowed object is
     *     {@link CA052TransactionReferences2 }
     *     
     */
    public void setRefs(CA052TransactionReferences2 value) {
        this.refs = value;
    }

    /**
     * Gets the value of the amtDtls property.
     * 
     * @return
     *     possible object is
     *     {@link CA052AmountAndCurrencyExchange3 }
     *     
     */
    public CA052AmountAndCurrencyExchange3 getAmtDtls() {
        return amtDtls;
    }

    /**
     * Sets the value of the amtDtls property.
     * 
     * @param value
     *     allowed object is
     *     {@link CA052AmountAndCurrencyExchange3 }
     *     
     */
    public void setAmtDtls(CA052AmountAndCurrencyExchange3 value) {
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
     * {@link CA052CashBalanceAvailability2 }
     * 
     * 
     */
    public List<CA052CashBalanceAvailability2> getAvlbty() {
        if (avlbty == null) {
            avlbty = new ArrayList<CA052CashBalanceAvailability2>();
        }
        return this.avlbty;
    }

    /**
     * Gets the value of the bkTxCd property.
     * 
     * @return
     *     possible object is
     *     {@link CA052BankTransactionCodeStructure4 }
     *     
     */
    public CA052BankTransactionCodeStructure4 getBkTxCd() {
        return bkTxCd;
    }

    /**
     * Sets the value of the bkTxCd property.
     * 
     * @param value
     *     allowed object is
     *     {@link CA052BankTransactionCodeStructure4 }
     *     
     */
    public void setBkTxCd(CA052BankTransactionCodeStructure4 value) {
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
     * {@link CA052ChargesInformation6 }
     * 
     * 
     */
    public List<CA052ChargesInformation6> getChrgs() {
        if (chrgs == null) {
            chrgs = new ArrayList<CA052ChargesInformation6>();
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
     * {@link CA052TransactionInterest2 }
     * 
     * 
     */
    public List<CA052TransactionInterest2> getIntrst() {
        if (intrst == null) {
            intrst = new ArrayList<CA052TransactionInterest2>();
        }
        return this.intrst;
    }

    /**
     * Gets the value of the rltdPties property.
     * 
     * @return
     *     possible object is
     *     {@link CA052TransactionParty2 }
     *     
     */
    public CA052TransactionParty2 getRltdPties() {
        return rltdPties;
    }

    /**
     * Sets the value of the rltdPties property.
     * 
     * @param value
     *     allowed object is
     *     {@link CA052TransactionParty2 }
     *     
     */
    public void setRltdPties(CA052TransactionParty2 value) {
        this.rltdPties = value;
    }

    /**
     * Gets the value of the rltdAgts property.
     * 
     * @return
     *     possible object is
     *     {@link CA052TransactionAgents2 }
     *     
     */
    public CA052TransactionAgents2 getRltdAgts() {
        return rltdAgts;
    }

    /**
     * Sets the value of the rltdAgts property.
     * 
     * @param value
     *     allowed object is
     *     {@link CA052TransactionAgents2 }
     *     
     */
    public void setRltdAgts(CA052TransactionAgents2 value) {
        this.rltdAgts = value;
    }

    /**
     * Gets the value of the purp property.
     * 
     * @return
     *     possible object is
     *     {@link CA052Purpose2Choice }
     *     
     */
    public CA052Purpose2Choice getPurp() {
        return purp;
    }

    /**
     * Sets the value of the purp property.
     * 
     * @param value
     *     allowed object is
     *     {@link CA052Purpose2Choice }
     *     
     */
    public void setPurp(CA052Purpose2Choice value) {
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
     * {@link CA052RemittanceLocation2 }
     * 
     * 
     */
    public List<CA052RemittanceLocation2> getRltdRmtInf() {
        if (rltdRmtInf == null) {
            rltdRmtInf = new ArrayList<CA052RemittanceLocation2>();
        }
        return this.rltdRmtInf;
    }

    /**
     * Gets the value of the rmtInf property.
     * 
     * @return
     *     possible object is
     *     {@link CA052RemittanceInformation5 }
     *     
     */
    public CA052RemittanceInformation5 getRmtInf() {
        return rmtInf;
    }

    /**
     * Sets the value of the rmtInf property.
     * 
     * @param value
     *     allowed object is
     *     {@link CA052RemittanceInformation5 }
     *     
     */
    public void setRmtInf(CA052RemittanceInformation5 value) {
        this.rmtInf = value;
    }

    /**
     * Gets the value of the rltdDts property.
     * 
     * @return
     *     possible object is
     *     {@link CA052TransactionDates2 }
     *     
     */
    public CA052TransactionDates2 getRltdDts() {
        return rltdDts;
    }

    /**
     * Sets the value of the rltdDts property.
     * 
     * @param value
     *     allowed object is
     *     {@link CA052TransactionDates2 }
     *     
     */
    public void setRltdDts(CA052TransactionDates2 value) {
        this.rltdDts = value;
    }

    /**
     * Gets the value of the rltdPric property.
     * 
     * @return
     *     possible object is
     *     {@link CA052TransactionPrice2Choice }
     *     
     */
    public CA052TransactionPrice2Choice getRltdPric() {
        return rltdPric;
    }

    /**
     * Sets the value of the rltdPric property.
     * 
     * @param value
     *     allowed object is
     *     {@link CA052TransactionPrice2Choice }
     *     
     */
    public void setRltdPric(CA052TransactionPrice2Choice value) {
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
     * {@link CA052TransactionQuantities1Choice }
     * 
     * 
     */
    public List<CA052TransactionQuantities1Choice> getRltdQties() {
        if (rltdQties == null) {
            rltdQties = new ArrayList<CA052TransactionQuantities1Choice>();
        }
        return this.rltdQties;
    }

    /**
     * Gets the value of the finInstrmId property.
     * 
     * @return
     *     possible object is
     *     {@link CA052SecurityIdentification4Choice }
     *     
     */
    public CA052SecurityIdentification4Choice getFinInstrmId() {
        return finInstrmId;
    }

    /**
     * Sets the value of the finInstrmId property.
     * 
     * @param value
     *     allowed object is
     *     {@link CA052SecurityIdentification4Choice }
     *     
     */
    public void setFinInstrmId(CA052SecurityIdentification4Choice value) {
        this.finInstrmId = value;
    }

    /**
     * Gets the value of the tax property.
     * 
     * @return
     *     possible object is
     *     {@link CA052TaxInformation3 }
     *     
     */
    public CA052TaxInformation3 getTax() {
        return tax;
    }

    /**
     * Sets the value of the tax property.
     * 
     * @param value
     *     allowed object is
     *     {@link CA052TaxInformation3 }
     *     
     */
    public void setTax(CA052TaxInformation3 value) {
        this.tax = value;
    }

    /**
     * Gets the value of the rtrInf property.
     * 
     * @return
     *     possible object is
     *     {@link CA052ReturnReasonInformation10 }
     *     
     */
    public CA052ReturnReasonInformation10 getRtrInf() {
        return rtrInf;
    }

    /**
     * Sets the value of the rtrInf property.
     * 
     * @param value
     *     allowed object is
     *     {@link CA052ReturnReasonInformation10 }
     *     
     */
    public void setRtrInf(CA052ReturnReasonInformation10 value) {
        this.rtrInf = value;
    }

    /**
     * Gets the value of the corpActn property.
     * 
     * @return
     *     possible object is
     *     {@link CA052CorporateAction1 }
     *     
     */
    public CA052CorporateAction1 getCorpActn() {
        return corpActn;
    }

    /**
     * Sets the value of the corpActn property.
     * 
     * @param value
     *     allowed object is
     *     {@link CA052CorporateAction1 }
     *     
     */
    public void setCorpActn(CA052CorporateAction1 value) {
        this.corpActn = value;
    }

    /**
     * Gets the value of the sfkpgAcct property.
     * 
     * @return
     *     possible object is
     *     {@link CA052CashAccount16 }
     *     
     */
    public CA052CashAccount16 getSfkpgAcct() {
        return sfkpgAcct;
    }

    /**
     * Sets the value of the sfkpgAcct property.
     * 
     * @param value
     *     allowed object is
     *     {@link CA052CashAccount16 }
     *     
     */
    public void setSfkpgAcct(CA052CashAccount16 value) {
        this.sfkpgAcct = value;
    }

    /**
     * Gets the value of the addtlTxInf property.
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
     * Sets the value of the addtlTxInf property.
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
