//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �nderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2020.05.17 um 06:36:00 PM CEST 
//


package com.lp.server.schema.iso20022.camt053V04;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse f�r TotalsPerBankTransactionCode3 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="TotalsPerBankTransactionCode3">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="NbOfNtries" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}Max15NumericText" minOccurs="0"/>
 *         &lt;element name="Sum" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}DecimalNumber" minOccurs="0"/>
 *         &lt;element name="TtlNetNtry" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}AmountAndDirection35" minOccurs="0"/>
 *         &lt;element name="FcstInd" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}TrueFalseIndicator" minOccurs="0"/>
 *         &lt;element name="BkTxCd" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}BankTransactionCodeStructure4"/>
 *         &lt;element name="Avlbty" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}CashBalanceAvailability2" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TotalsPerBankTransactionCode3", propOrder = {
    "nbOfNtries",
    "sum",
    "ttlNetNtry",
    "fcstInd",
    "bkTxCd",
    "avlbty"
})
public class CA04TotalsPerBankTransactionCode3 {

    @XmlElement(name = "NbOfNtries")
    protected String nbOfNtries;
    @XmlElement(name = "Sum")
    protected BigDecimal sum;
    @XmlElement(name = "TtlNetNtry")
    protected CA04AmountAndDirection35 ttlNetNtry;
    @XmlElement(name = "FcstInd")
    protected Boolean fcstInd;
    @XmlElement(name = "BkTxCd", required = true)
    protected CA04BankTransactionCodeStructure4 bkTxCd;
    @XmlElement(name = "Avlbty")
    protected List<CA04CashBalanceAvailability2> avlbty;

    /**
     * Ruft den Wert der nbOfNtries-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNbOfNtries() {
        return nbOfNtries;
    }

    /**
     * Legt den Wert der nbOfNtries-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNbOfNtries(String value) {
        this.nbOfNtries = value;
    }

    /**
     * Ruft den Wert der sum-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getSum() {
        return sum;
    }

    /**
     * Legt den Wert der sum-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setSum(BigDecimal value) {
        this.sum = value;
    }

    /**
     * Ruft den Wert der ttlNetNtry-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04AmountAndDirection35 }
     *     
     */
    public CA04AmountAndDirection35 getTtlNetNtry() {
        return ttlNetNtry;
    }

    /**
     * Legt den Wert der ttlNetNtry-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04AmountAndDirection35 }
     *     
     */
    public void setTtlNetNtry(CA04AmountAndDirection35 value) {
        this.ttlNetNtry = value;
    }

    /**
     * Ruft den Wert der fcstInd-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isFcstInd() {
        return fcstInd;
    }

    /**
     * Legt den Wert der fcstInd-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setFcstInd(Boolean value) {
        this.fcstInd = value;
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

}
