//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2015.06.29 um 04:43:26 PM CEST 
//


package com.lp.server.schema.iso20022.camt053V02;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer FromToAmountRange complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="FromToAmountRange">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="FrAmt" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.02}AmountRangeBoundary1"/>
 *         &lt;element name="ToAmt" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.02}AmountRangeBoundary1"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FromToAmountRange", propOrder = {
    "frAmt",
    "toAmt"
})
public class CAFromToAmountRange {

    @XmlElement(name = "FrAmt", required = true)
    protected CAAmountRangeBoundary1 frAmt;
    @XmlElement(name = "ToAmt", required = true)
    protected CAAmountRangeBoundary1 toAmt;

    /**
     * Ruft den Wert der frAmt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CAAmountRangeBoundary1 }
     *     
     */
    public CAAmountRangeBoundary1 getFrAmt() {
        return frAmt;
    }

    /**
     * Legt den Wert der frAmt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CAAmountRangeBoundary1 }
     *     
     */
    public void setFrAmt(CAAmountRangeBoundary1 value) {
        this.frAmt = value;
    }

    /**
     * Ruft den Wert der toAmt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CAAmountRangeBoundary1 }
     *     
     */
    public CAAmountRangeBoundary1 getToAmt() {
        return toAmt;
    }

    /**
     * Legt den Wert der toAmt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CAAmountRangeBoundary1 }
     *     
     */
    public void setToAmt(CAAmountRangeBoundary1 value) {
        this.toAmt = value;
    }

}
