//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2015.06.25 um 02:54:36 PM CEST 
//


package com.lp.server.schema.iso20022.camt053V05;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer InterestRecord1 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="InterestRecord1">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Amt" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}ActiveOrHistoricCurrencyAndAmount"/>
 *         &lt;element name="CdtDbtInd" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}CreditDebitCode"/>
 *         &lt;element name="Tp" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}InterestType1Choice" minOccurs="0"/>
 *         &lt;element name="Rate" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}Rate3" minOccurs="0"/>
 *         &lt;element name="FrToDt" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}DateTimePeriodDetails" minOccurs="0"/>
 *         &lt;element name="Rsn" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}Max35Text" minOccurs="0"/>
 *         &lt;element name="Tax" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}TaxCharges2" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InterestRecord1", propOrder = {
    "amt",
    "cdtDbtInd",
    "tp",
    "rate",
    "frToDt",
    "rsn",
    "tax"
})
public class CAInterestRecord1 {

    @XmlElement(name = "Amt", required = true)
    protected CAActiveOrHistoricCurrencyAndAmount amt;
    @XmlElement(name = "CdtDbtInd", required = true)
    @XmlSchemaType(name = "string")
    protected CACreditDebitCode cdtDbtInd;
    @XmlElement(name = "Tp")
    protected CAInterestType1Choice tp;
    @XmlElement(name = "Rate")
    protected CARate3 rate;
    @XmlElement(name = "FrToDt")
    protected CADateTimePeriodDetails frToDt;
    @XmlElement(name = "Rsn")
    protected String rsn;
    @XmlElement(name = "Tax")
    protected CATaxCharges2 tax;

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
     * Ruft den Wert der tp-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CAInterestType1Choice }
     *     
     */
    public CAInterestType1Choice getTp() {
        return tp;
    }

    /**
     * Legt den Wert der tp-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CAInterestType1Choice }
     *     
     */
    public void setTp(CAInterestType1Choice value) {
        this.tp = value;
    }

    /**
     * Ruft den Wert der rate-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CARate3 }
     *     
     */
    public CARate3 getRate() {
        return rate;
    }

    /**
     * Legt den Wert der rate-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CARate3 }
     *     
     */
    public void setRate(CARate3 value) {
        this.rate = value;
    }

    /**
     * Ruft den Wert der frToDt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CADateTimePeriodDetails }
     *     
     */
    public CADateTimePeriodDetails getFrToDt() {
        return frToDt;
    }

    /**
     * Legt den Wert der frToDt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CADateTimePeriodDetails }
     *     
     */
    public void setFrToDt(CADateTimePeriodDetails value) {
        this.frToDt = value;
    }

    /**
     * Ruft den Wert der rsn-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRsn() {
        return rsn;
    }

    /**
     * Legt den Wert der rsn-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRsn(String value) {
        this.rsn = value;
    }

    /**
     * Ruft den Wert der tax-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CATaxCharges2 }
     *     
     */
    public CATaxCharges2 getTax() {
        return tax;
    }

    /**
     * Legt den Wert der tax-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CATaxCharges2 }
     *     
     */
    public void setTax(CATaxCharges2 value) {
        this.tax = value;
    }

}
