//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �nderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2020.05.17 um 06:36:00 PM CEST 
//


package com.lp.server.schema.iso20022.camt053V04;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse f�r CashBalanceAvailability2 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="CashBalanceAvailability2">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Dt" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}CashBalanceAvailabilityDate1"/>
 *         &lt;element name="Amt" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}ActiveOrHistoricCurrencyAndAmount"/>
 *         &lt;element name="CdtDbtInd" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}CreditDebitCode"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CashBalanceAvailability2", propOrder = {
    "dt",
    "amt",
    "cdtDbtInd"
})
public class CA04CashBalanceAvailability2 {

    @XmlElement(name = "Dt", required = true)
    protected CA04CashBalanceAvailabilityDate1 dt;
    @XmlElement(name = "Amt", required = true)
    protected CA04ActiveOrHistoricCurrencyAndAmount amt;
    @XmlElement(name = "CdtDbtInd", required = true)
    @XmlSchemaType(name = "string")
    protected CA04CreditDebitCode cdtDbtInd;

    /**
     * Ruft den Wert der dt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04CashBalanceAvailabilityDate1 }
     *     
     */
    public CA04CashBalanceAvailabilityDate1 getDt() {
        return dt;
    }

    /**
     * Legt den Wert der dt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04CashBalanceAvailabilityDate1 }
     *     
     */
    public void setDt(CA04CashBalanceAvailabilityDate1 value) {
        this.dt = value;
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

}
