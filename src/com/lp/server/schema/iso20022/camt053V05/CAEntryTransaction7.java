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
 * <p>Java-Klasse fuer EntryTransaction7 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="EntryTransaction7">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Refs" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}TransactionReferences3" minOccurs="0"/>
 *         &lt;element name="Amt" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}ActiveOrHistoricCurrencyAndAmount"/>
 *         &lt;element name="CdtDbtInd" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}CreditDebitCode"/>
 *         &lt;element name="AmtDtls" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}AmountAndCurrencyExchange3" minOccurs="0"/>
 *         &lt;element name="Avlbty" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}CashBalanceAvailability2" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="BkTxCd" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}BankTransactionCodeStructure4" minOccurs="0"/>
 *         &lt;element name="Chrgs" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}Charges4" minOccurs="0"/>
 *         &lt;element name="Intrst" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}TransactionInterest3" minOccurs="0"/>
 *         &lt;element name="RltdPties" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}TransactionParties3" minOccurs="0"/>
 *         &lt;element name="RltdAgts" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}TransactionAgents3" minOccurs="0"/>
 *         &lt;element name="Purp" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}Purpose2Choice" minOccurs="0"/>
 *         &lt;element name="RltdRmtInf" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}RemittanceLocation4" maxOccurs="10" minOccurs="0"/>
 *         &lt;element name="RmtInf" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}RemittanceInformation10" minOccurs="0"/>
 *         &lt;element name="RltdDts" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}TransactionDates2" minOccurs="0"/>
 *         &lt;element name="RltdPric" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}TransactionPrice3Choice" minOccurs="0"/>
 *         &lt;element name="RltdQties" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}TransactionQuantities2Choice" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="FinInstrmId" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}SecurityIdentification14" minOccurs="0"/>
 *         &lt;element name="Tax" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}TaxInformation3" minOccurs="0"/>
 *         &lt;element name="RtrInf" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}PaymentReturnReason2" minOccurs="0"/>
 *         &lt;element name="CorpActn" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}CorporateAction9" minOccurs="0"/>
 *         &lt;element name="SfkpgAcct" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}SecuritiesAccount13" minOccurs="0"/>
 *         &lt;element name="CshDpst" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}CashDeposit1" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="CardTx" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}CardTransaction2" minOccurs="0"/>
 *         &lt;element name="AddtlTxInf" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}Max500Text" minOccurs="0"/>
 *         &lt;element name="SplmtryData" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}SupplementaryData1" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EntryTransaction7", propOrder = {
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
public class CAEntryTransaction7 {

    @XmlElement(name = "Refs")
    protected CATransactionReferences3 refs;
    @XmlElement(name = "Amt", required = true)
    protected CAActiveOrHistoricCurrencyAndAmount amt;
    @XmlElement(name = "CdtDbtInd", required = true)
    @XmlSchemaType(name = "string")
    protected CACreditDebitCode cdtDbtInd;
    @XmlElement(name = "AmtDtls")
    protected CAAmountAndCurrencyExchange3 amtDtls;
    @XmlElement(name = "Avlbty")
    protected List<CACashBalanceAvailability2> avlbty;
    @XmlElement(name = "BkTxCd")
    protected CABankTransactionCodeStructure4 bkTxCd;
    @XmlElement(name = "Chrgs")
    protected CACharges4 chrgs;
    @XmlElement(name = "Intrst")
    protected CATransactionInterest3 intrst;
    @XmlElement(name = "RltdPties")
    protected CATransactionParties3 rltdPties;
    @XmlElement(name = "RltdAgts")
    protected CATransactionAgents3 rltdAgts;
    @XmlElement(name = "Purp")
    protected CAPurpose2Choice purp;
    @XmlElement(name = "RltdRmtInf")
    protected List<CARemittanceLocation4> rltdRmtInf;
    @XmlElement(name = "RmtInf")
    protected CARemittanceInformation10 rmtInf;
    @XmlElement(name = "RltdDts")
    protected CATransactionDates2 rltdDts;
    @XmlElement(name = "RltdPric")
    protected CATransactionPrice3Choice rltdPric;
    @XmlElement(name = "RltdQties")
    protected List<CATransactionQuantities2Choice> rltdQties;
    @XmlElement(name = "FinInstrmId")
    protected CASecurityIdentification14 finInstrmId;
    @XmlElement(name = "Tax")
    protected CATaxInformation3 tax;
    @XmlElement(name = "RtrInf")
    protected CAPaymentReturnReason2 rtrInf;
    @XmlElement(name = "CorpActn")
    protected CACorporateAction9 corpActn;
    @XmlElement(name = "SfkpgAcct")
    protected CASecuritiesAccount13 sfkpgAcct;
    @XmlElement(name = "CshDpst")
    protected List<CACashDeposit1> cshDpst;
    @XmlElement(name = "CardTx")
    protected CACardTransaction2 cardTx;
    @XmlElement(name = "AddtlTxInf")
    protected String addtlTxInf;
    @XmlElement(name = "SplmtryData")
    protected List<CASupplementaryData1> splmtryData;

    /**
     * Ruft den Wert der refs-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CATransactionReferences3 }
     *     
     */
    public CATransactionReferences3 getRefs() {
        return refs;
    }

    /**
     * Legt den Wert der refs-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CATransactionReferences3 }
     *     
     */
    public void setRefs(CATransactionReferences3 value) {
        this.refs = value;
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
     * Ruft den Wert der rltdPties-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CATransactionParties3 }
     *     
     */
    public CATransactionParties3 getRltdPties() {
        return rltdPties;
    }

    /**
     * Legt den Wert der rltdPties-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CATransactionParties3 }
     *     
     */
    public void setRltdPties(CATransactionParties3 value) {
        this.rltdPties = value;
    }

    /**
     * Ruft den Wert der rltdAgts-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CATransactionAgents3 }
     *     
     */
    public CATransactionAgents3 getRltdAgts() {
        return rltdAgts;
    }

    /**
     * Legt den Wert der rltdAgts-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CATransactionAgents3 }
     *     
     */
    public void setRltdAgts(CATransactionAgents3 value) {
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
     * {@link CARemittanceLocation4 }
     * 
     * 
     */
    public List<CARemittanceLocation4> getRltdRmtInf() {
        if (rltdRmtInf == null) {
            rltdRmtInf = new ArrayList<CARemittanceLocation4>();
        }
        return this.rltdRmtInf;
    }

    /**
     * Ruft den Wert der rmtInf-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CARemittanceInformation10 }
     *     
     */
    public CARemittanceInformation10 getRmtInf() {
        return rmtInf;
    }

    /**
     * Legt den Wert der rmtInf-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CARemittanceInformation10 }
     *     
     */
    public void setRmtInf(CARemittanceInformation10 value) {
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
     *     {@link CATransactionPrice3Choice }
     *     
     */
    public CATransactionPrice3Choice getRltdPric() {
        return rltdPric;
    }

    /**
     * Legt den Wert der rltdPric-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CATransactionPrice3Choice }
     *     
     */
    public void setRltdPric(CATransactionPrice3Choice value) {
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
     * {@link CATransactionQuantities2Choice }
     * 
     * 
     */
    public List<CATransactionQuantities2Choice> getRltdQties() {
        if (rltdQties == null) {
            rltdQties = new ArrayList<CATransactionQuantities2Choice>();
        }
        return this.rltdQties;
    }

    /**
     * Ruft den Wert der finInstrmId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CASecurityIdentification14 }
     *     
     */
    public CASecurityIdentification14 getFinInstrmId() {
        return finInstrmId;
    }

    /**
     * Legt den Wert der finInstrmId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CASecurityIdentification14 }
     *     
     */
    public void setFinInstrmId(CASecurityIdentification14 value) {
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
     *     {@link CAPaymentReturnReason2 }
     *     
     */
    public CAPaymentReturnReason2 getRtrInf() {
        return rtrInf;
    }

    /**
     * Legt den Wert der rtrInf-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CAPaymentReturnReason2 }
     *     
     */
    public void setRtrInf(CAPaymentReturnReason2 value) {
        this.rtrInf = value;
    }

    /**
     * Ruft den Wert der corpActn-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CACorporateAction9 }
     *     
     */
    public CACorporateAction9 getCorpActn() {
        return corpActn;
    }

    /**
     * Legt den Wert der corpActn-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CACorporateAction9 }
     *     
     */
    public void setCorpActn(CACorporateAction9 value) {
        this.corpActn = value;
    }

    /**
     * Ruft den Wert der sfkpgAcct-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CASecuritiesAccount13 }
     *     
     */
    public CASecuritiesAccount13 getSfkpgAcct() {
        return sfkpgAcct;
    }

    /**
     * Legt den Wert der sfkpgAcct-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CASecuritiesAccount13 }
     *     
     */
    public void setSfkpgAcct(CASecuritiesAccount13 value) {
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
     * {@link CACashDeposit1 }
     * 
     * 
     */
    public List<CACashDeposit1> getCshDpst() {
        if (cshDpst == null) {
            cshDpst = new ArrayList<CACashDeposit1>();
        }
        return this.cshDpst;
    }

    /**
     * Ruft den Wert der cardTx-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CACardTransaction2 }
     *     
     */
    public CACardTransaction2 getCardTx() {
        return cardTx;
    }

    /**
     * Legt den Wert der cardTx-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CACardTransaction2 }
     *     
     */
    public void setCardTx(CACardTransaction2 value) {
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
     * {@link CASupplementaryData1 }
     * 
     * 
     */
    public List<CASupplementaryData1> getSplmtryData() {
        if (splmtryData == null) {
            splmtryData = new ArrayList<CASupplementaryData1>();
        }
        return this.splmtryData;
    }

}
