//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2020.05.17 um 06:36:00 PM CEST 
//


package com.lp.server.schema.iso20022.camt053V04;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für EntryTransaction4 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="EntryTransaction4">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Refs" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}TransactionReferences3" minOccurs="0"/>
 *         &lt;element name="Amt" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}ActiveOrHistoricCurrencyAndAmount"/>
 *         &lt;element name="CdtDbtInd" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}CreditDebitCode"/>
 *         &lt;element name="AmtDtls" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}AmountAndCurrencyExchange3" minOccurs="0"/>
 *         &lt;element name="Avlbty" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}CashBalanceAvailability2" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="BkTxCd" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}BankTransactionCodeStructure4" minOccurs="0"/>
 *         &lt;element name="Chrgs" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}Charges4" minOccurs="0"/>
 *         &lt;element name="Intrst" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}TransactionInterest3" minOccurs="0"/>
 *         &lt;element name="RltdPties" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}TransactionParties3" minOccurs="0"/>
 *         &lt;element name="RltdAgts" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}TransactionAgents3" minOccurs="0"/>
 *         &lt;element name="Purp" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}Purpose2Choice" minOccurs="0"/>
 *         &lt;element name="RltdRmtInf" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}RemittanceLocation2" maxOccurs="10" minOccurs="0"/>
 *         &lt;element name="RmtInf" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}RemittanceInformation7" minOccurs="0"/>
 *         &lt;element name="RltdDts" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}TransactionDates2" minOccurs="0"/>
 *         &lt;element name="RltdPric" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}TransactionPrice3Choice" minOccurs="0"/>
 *         &lt;element name="RltdQties" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}TransactionQuantities2Choice" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="FinInstrmId" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}SecurityIdentification14" minOccurs="0"/>
 *         &lt;element name="Tax" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}TaxInformation3" minOccurs="0"/>
 *         &lt;element name="RtrInf" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}PaymentReturnReason2" minOccurs="0"/>
 *         &lt;element name="CorpActn" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}CorporateAction9" minOccurs="0"/>
 *         &lt;element name="SfkpgAcct" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}SecuritiesAccount13" minOccurs="0"/>
 *         &lt;element name="CshDpst" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}CashDeposit1" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="CardTx" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}CardTransaction1" minOccurs="0"/>
 *         &lt;element name="AddtlTxInf" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}Max500Text" minOccurs="0"/>
 *         &lt;element name="SplmtryData" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}SupplementaryData1" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EntryTransaction4", propOrder = {
    "refs",
    "amt",
    "cdtDbtInd",
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
    "cshDpst",
    "cardTx",
    "addtlTxInf",
    "splmtryData"
})
public class CA04EntryTransaction4 {

    @XmlElement(name = "Refs")
    protected CA04TransactionReferences3 refs;
    @XmlElement(name = "Amt", required = true)
    protected CA04ActiveOrHistoricCurrencyAndAmount amt;
    @XmlElement(name = "CdtDbtInd", required = true)
    @XmlSchemaType(name = "string")
    protected CA04CreditDebitCode cdtDbtInd;
    @XmlElement(name = "AmtDtls")
    protected CA04AmountAndCurrencyExchange3 amtDtls;
    @XmlElement(name = "Avlbty")
    protected List<CA04CashBalanceAvailability2> avlbty;
    @XmlElement(name = "BkTxCd")
    protected CA04BankTransactionCodeStructure4 bkTxCd;
    @XmlElement(name = "Chrgs")
    protected CA04Charges4 chrgs;
    @XmlElement(name = "Intrst")
    protected CA04TransactionInterest3 intrst;
    @XmlElement(name = "RltdPties")
    protected CA04TransactionParties3 rltdPties;
    @XmlElement(name = "RltdAgts")
    protected CA04TransactionAgents3 rltdAgts;
    @XmlElement(name = "Purp")
    protected CA04Purpose2Choice purp;
    @XmlElement(name = "RltdRmtInf")
    protected List<CA04RemittanceLocation2> rltdRmtInf;
    @XmlElement(name = "RmtInf")
    protected CA04RemittanceInformation7 rmtInf;
    @XmlElement(name = "RltdDts")
    protected CA04TransactionDates2 rltdDts;
    @XmlElement(name = "RltdPric")
    protected CA04TransactionPrice3Choice rltdPric;
    @XmlElement(name = "RltdQties")
    protected List<CA04TransactionQuantities2Choice> rltdQties;
    @XmlElement(name = "FinInstrmId")
    protected CA04SecurityIdentification14 finInstrmId;
    @XmlElement(name = "Tax")
    protected CA04TaxInformation3 tax;
    @XmlElement(name = "RtrInf")
    protected CA04PaymentReturnReason2 rtrInf;
    @XmlElement(name = "CorpActn")
    protected CA04CorporateAction9 corpActn;
    @XmlElement(name = "SfkpgAcct")
    protected CA04SecuritiesAccount13 sfkpgAcct;
    @XmlElement(name = "CshDpst")
    protected List<CA04CashDeposit1> cshDpst;
    @XmlElement(name = "CardTx")
    protected CA04CardTransaction1 cardTx;
    @XmlElement(name = "AddtlTxInf")
    protected String addtlTxInf;
    @XmlElement(name = "SplmtryData")
    protected List<CA04SupplementaryData1> splmtryData;

    /**
     * Ruft den Wert der refs-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04TransactionReferences3 }
     *     
     */
    public CA04TransactionReferences3 getRefs() {
        return refs;
    }

    /**
     * Legt den Wert der refs-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04TransactionReferences3 }
     *     
     */
    public void setRefs(CA04TransactionReferences3 value) {
        this.refs = value;
    }

    /**
     * Ruft den Wert der amt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04ActiveOrHistoricCurrencyAndAmount }
     *     
     */
    public CA04ActiveOrHistoricCurrencyAndAmount getAmt() {
        return amt;
    }

    /**
     * Legt den Wert der amt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04ActiveOrHistoricCurrencyAndAmount }
     *     
     */
    public void setAmt(CA04ActiveOrHistoricCurrencyAndAmount value) {
        this.amt = value;
    }

    /**
     * Ruft den Wert der cdtDbtInd-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04CreditDebitCode }
     *     
     */
    public CA04CreditDebitCode getCdtDbtInd() {
        return cdtDbtInd;
    }

    /**
     * Legt den Wert der cdtDbtInd-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04CreditDebitCode }
     *     
     */
    public void setCdtDbtInd(CA04CreditDebitCode value) {
        this.cdtDbtInd = value;
    }

    /**
     * Ruft den Wert der amtDtls-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04AmountAndCurrencyExchange3 }
     *     
     */
    public CA04AmountAndCurrencyExchange3 getAmtDtls() {
        return amtDtls;
    }

    /**
     * Legt den Wert der amtDtls-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04AmountAndCurrencyExchange3 }
     *     
     */
    public void setAmtDtls(CA04AmountAndCurrencyExchange3 value) {
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
     * {@link CA04CashBalanceAvailability2 }
     * 
     * 
     */
    public List<CA04CashBalanceAvailability2> getAvlbty() {
        if (avlbty == null) {
            avlbty = new ArrayList<CA04CashBalanceAvailability2>();
        }
        return this.avlbty;
    }

    /**
     * Ruft den Wert der bkTxCd-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04BankTransactionCodeStructure4 }
     *     
     */
    public CA04BankTransactionCodeStructure4 getBkTxCd() {
        return bkTxCd;
    }

    /**
     * Legt den Wert der bkTxCd-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04BankTransactionCodeStructure4 }
     *     
     */
    public void setBkTxCd(CA04BankTransactionCodeStructure4 value) {
        this.bkTxCd = value;
    }

    /**
     * Ruft den Wert der chrgs-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04Charges4 }
     *     
     */
    public CA04Charges4 getChrgs() {
        return chrgs;
    }

    /**
     * Legt den Wert der chrgs-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04Charges4 }
     *     
     */
    public void setChrgs(CA04Charges4 value) {
        this.chrgs = value;
    }

    /**
     * Ruft den Wert der intrst-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04TransactionInterest3 }
     *     
     */
    public CA04TransactionInterest3 getIntrst() {
        return intrst;
    }

    /**
     * Legt den Wert der intrst-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04TransactionInterest3 }
     *     
     */
    public void setIntrst(CA04TransactionInterest3 value) {
        this.intrst = value;
    }

    /**
     * Ruft den Wert der rltdPties-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04TransactionParties3 }
     *     
     */
    public CA04TransactionParties3 getRltdPties() {
        return rltdPties;
    }

    /**
     * Legt den Wert der rltdPties-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04TransactionParties3 }
     *     
     */
    public void setRltdPties(CA04TransactionParties3 value) {
        this.rltdPties = value;
    }

    /**
     * Ruft den Wert der rltdAgts-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04TransactionAgents3 }
     *     
     */
    public CA04TransactionAgents3 getRltdAgts() {
        return rltdAgts;
    }

    /**
     * Legt den Wert der rltdAgts-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04TransactionAgents3 }
     *     
     */
    public void setRltdAgts(CA04TransactionAgents3 value) {
        this.rltdAgts = value;
    }

    /**
     * Ruft den Wert der purp-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04Purpose2Choice }
     *     
     */
    public CA04Purpose2Choice getPurp() {
        return purp;
    }

    /**
     * Legt den Wert der purp-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04Purpose2Choice }
     *     
     */
    public void setPurp(CA04Purpose2Choice value) {
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
     * {@link CA04RemittanceLocation2 }
     * 
     * 
     */
    public List<CA04RemittanceLocation2> getRltdRmtInf() {
        if (rltdRmtInf == null) {
            rltdRmtInf = new ArrayList<CA04RemittanceLocation2>();
        }
        return this.rltdRmtInf;
    }

    /**
     * Ruft den Wert der rmtInf-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04RemittanceInformation7 }
     *     
     */
    public CA04RemittanceInformation7 getRmtInf() {
        return rmtInf;
    }

    /**
     * Legt den Wert der rmtInf-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04RemittanceInformation7 }
     *     
     */
    public void setRmtInf(CA04RemittanceInformation7 value) {
        this.rmtInf = value;
    }

    /**
     * Ruft den Wert der rltdDts-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04TransactionDates2 }
     *     
     */
    public CA04TransactionDates2 getRltdDts() {
        return rltdDts;
    }

    /**
     * Legt den Wert der rltdDts-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04TransactionDates2 }
     *     
     */
    public void setRltdDts(CA04TransactionDates2 value) {
        this.rltdDts = value;
    }

    /**
     * Ruft den Wert der rltdPric-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04TransactionPrice3Choice }
     *     
     */
    public CA04TransactionPrice3Choice getRltdPric() {
        return rltdPric;
    }

    /**
     * Legt den Wert der rltdPric-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04TransactionPrice3Choice }
     *     
     */
    public void setRltdPric(CA04TransactionPrice3Choice value) {
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
     * {@link CA04TransactionQuantities2Choice }
     * 
     * 
     */
    public List<CA04TransactionQuantities2Choice> getRltdQties() {
        if (rltdQties == null) {
            rltdQties = new ArrayList<CA04TransactionQuantities2Choice>();
        }
        return this.rltdQties;
    }

    /**
     * Ruft den Wert der finInstrmId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04SecurityIdentification14 }
     *     
     */
    public CA04SecurityIdentification14 getFinInstrmId() {
        return finInstrmId;
    }

    /**
     * Legt den Wert der finInstrmId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04SecurityIdentification14 }
     *     
     */
    public void setFinInstrmId(CA04SecurityIdentification14 value) {
        this.finInstrmId = value;
    }

    /**
     * Ruft den Wert der tax-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04TaxInformation3 }
     *     
     */
    public CA04TaxInformation3 getTax() {
        return tax;
    }

    /**
     * Legt den Wert der tax-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04TaxInformation3 }
     *     
     */
    public void setTax(CA04TaxInformation3 value) {
        this.tax = value;
    }

    /**
     * Ruft den Wert der rtrInf-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04PaymentReturnReason2 }
     *     
     */
    public CA04PaymentReturnReason2 getRtrInf() {
        return rtrInf;
    }

    /**
     * Legt den Wert der rtrInf-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04PaymentReturnReason2 }
     *     
     */
    public void setRtrInf(CA04PaymentReturnReason2 value) {
        this.rtrInf = value;
    }

    /**
     * Ruft den Wert der corpActn-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04CorporateAction9 }
     *     
     */
    public CA04CorporateAction9 getCorpActn() {
        return corpActn;
    }

    /**
     * Legt den Wert der corpActn-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04CorporateAction9 }
     *     
     */
    public void setCorpActn(CA04CorporateAction9 value) {
        this.corpActn = value;
    }

    /**
     * Ruft den Wert der sfkpgAcct-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04SecuritiesAccount13 }
     *     
     */
    public CA04SecuritiesAccount13 getSfkpgAcct() {
        return sfkpgAcct;
    }

    /**
     * Legt den Wert der sfkpgAcct-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04SecuritiesAccount13 }
     *     
     */
    public void setSfkpgAcct(CA04SecuritiesAccount13 value) {
        this.sfkpgAcct = value;
    }

    /**
     * Gets the value of the cshDpst property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the cshDpst property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCshDpst().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CA04CashDeposit1 }
     * 
     * 
     */
    public List<CA04CashDeposit1> getCshDpst() {
        if (cshDpst == null) {
            cshDpst = new ArrayList<CA04CashDeposit1>();
        }
        return this.cshDpst;
    }

    /**
     * Ruft den Wert der cardTx-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04CardTransaction1 }
     *     
     */
    public CA04CardTransaction1 getCardTx() {
        return cardTx;
    }

    /**
     * Legt den Wert der cardTx-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04CardTransaction1 }
     *     
     */
    public void setCardTx(CA04CardTransaction1 value) {
        this.cardTx = value;
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
     * {@link CA04SupplementaryData1 }
     * 
     * 
     */
    public List<CA04SupplementaryData1> getSplmtryData() {
        if (splmtryData == null) {
            splmtryData = new ArrayList<CA04SupplementaryData1>();
        }
        return this.splmtryData;
    }

}
