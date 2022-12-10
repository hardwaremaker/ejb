//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2020.05.17 um 06:36:00 PM CEST 
//


package com.lp.server.schema.iso20022.camt053V04;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für Price2 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="Price2">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Tp" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}YieldedOrValueType1Choice"/>
 *         &lt;element name="Val" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}PriceRateOrAmountChoice"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Price2", propOrder = {
    "tp",
    "val"
})
public class CA04Price2 {

    @XmlElement(name = "Tp", required = true)
    protected CA04YieldedOrValueType1Choice tp;
    @XmlElement(name = "Val", required = true)
    protected CA04PriceRateOrAmountChoice val;

    /**
     * Ruft den Wert der tp-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04YieldedOrValueType1Choice }
     *     
     */
    public CA04YieldedOrValueType1Choice getTp() {
        return tp;
    }

    /**
     * Legt den Wert der tp-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04YieldedOrValueType1Choice }
     *     
     */
    public void setTp(CA04YieldedOrValueType1Choice value) {
        this.tp = value;
    }

    /**
     * Ruft den Wert der val-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04PriceRateOrAmountChoice }
     *     
     */
    public CA04PriceRateOrAmountChoice getVal() {
        return val;
    }

    /**
     * Legt den Wert der val-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04PriceRateOrAmountChoice }
     *     
     */
    public void setVal(CA04PriceRateOrAmountChoice value) {
        this.val = value;
    }

}
