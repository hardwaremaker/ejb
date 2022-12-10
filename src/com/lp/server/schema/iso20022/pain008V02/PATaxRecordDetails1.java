//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2017.02.21 um 11:13:49 AM CET 
//


package com.lp.server.schema.iso20022.pain008V02;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer TaxRecordDetails1 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="TaxRecordDetails1">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Prd" type="{urn:iso:std:iso:20022:tech:xsd:pain.008.001.02}TaxPeriod1" minOccurs="0"/>
 *         &lt;element name="Amt" type="{urn:iso:std:iso:20022:tech:xsd:pain.008.001.02}ActiveOrHistoricCurrencyAndAmount"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TaxRecordDetails1", propOrder = {
    "prd",
    "amt"
})
public class PATaxRecordDetails1 {

    @XmlElement(name = "Prd")
    protected PATaxPeriod1 prd;
    @XmlElement(name = "Amt", required = true)
    protected PAActiveOrHistoricCurrencyAndAmount amt;

    /**
     * Ruft den Wert der prd-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PATaxPeriod1 }
     *     
     */
    public PATaxPeriod1 getPrd() {
        return prd;
    }

    /**
     * Legt den Wert der prd-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PATaxPeriod1 }
     *     
     */
    public void setPrd(PATaxPeriod1 value) {
        this.prd = value;
    }

    /**
     * Ruft den Wert der amt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PAActiveOrHistoricCurrencyAndAmount }
     *     
     */
    public PAActiveOrHistoricCurrencyAndAmount getAmt() {
        return amt;
    }

    /**
     * Legt den Wert der amt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PAActiveOrHistoricCurrencyAndAmount }
     *     
     */
    public void setAmt(PAActiveOrHistoricCurrencyAndAmount value) {
        this.amt = value;
    }

}
