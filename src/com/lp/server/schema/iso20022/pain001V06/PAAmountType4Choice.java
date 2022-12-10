//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2015.06.25 um 02:55:19 PM CEST 
//


package com.lp.server.schema.iso20022.pain001V06;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer AmountType4Choice complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="AmountType4Choice">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="InstdAmt" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.06}ActiveOrHistoricCurrencyAndAmount"/>
 *         &lt;element name="EqvtAmt" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.06}EquivalentAmount2"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AmountType4Choice", propOrder = {
    "instdAmt",
    "eqvtAmt"
})
public class PAAmountType4Choice {

    @XmlElement(name = "InstdAmt")
    protected PAActiveOrHistoricCurrencyAndAmount instdAmt;
    @XmlElement(name = "EqvtAmt")
    protected PAEquivalentAmount2 eqvtAmt;

    /**
     * Ruft den Wert der instdAmt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PAActiveOrHistoricCurrencyAndAmount }
     *     
     */
    public PAActiveOrHistoricCurrencyAndAmount getInstdAmt() {
        return instdAmt;
    }

    /**
     * Legt den Wert der instdAmt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PAActiveOrHistoricCurrencyAndAmount }
     *     
     */
    public void setInstdAmt(PAActiveOrHistoricCurrencyAndAmount value) {
        this.instdAmt = value;
    }

    /**
     * Ruft den Wert der eqvtAmt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PAEquivalentAmount2 }
     *     
     */
    public PAEquivalentAmount2 getEqvtAmt() {
        return eqvtAmt;
    }

    /**
     * Legt den Wert der eqvtAmt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PAEquivalentAmount2 }
     *     
     */
    public void setEqvtAmt(PAEquivalentAmount2 value) {
        this.eqvtAmt = value;
    }

}
