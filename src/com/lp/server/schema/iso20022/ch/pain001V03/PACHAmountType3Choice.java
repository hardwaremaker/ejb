//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2020.05.13 um 09:59:39 AM CEST 
//


package com.lp.server.schema.iso20022.ch.pain001V03;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer AmountType3Choice complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="AmountType3Choice">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;element name="InstdAmt" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}ActiveOrHistoricCurrencyAndAmount"/>
 *           &lt;element name="EqvtAmt" type="{http://www.six-interbank-clearing.com/de/pain.001.001.03.ch.02.xsd}EquivalentAmount2"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AmountType3Choice", propOrder = {
    "instdAmt",
    "eqvtAmt"
})
public class PACHAmountType3Choice {

    @XmlElement(name = "InstdAmt")
    protected PACHActiveOrHistoricCurrencyAndAmount instdAmt;
    @XmlElement(name = "EqvtAmt")
    protected PACHEquivalentAmount2 eqvtAmt;

    /**
     * Ruft den Wert der instdAmt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACHActiveOrHistoricCurrencyAndAmount }
     *     
     */
    public PACHActiveOrHistoricCurrencyAndAmount getInstdAmt() {
        return instdAmt;
    }

    /**
     * Legt den Wert der instdAmt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACHActiveOrHistoricCurrencyAndAmount }
     *     
     */
    public void setInstdAmt(PACHActiveOrHistoricCurrencyAndAmount value) {
        this.instdAmt = value;
    }

    /**
     * Ruft den Wert der eqvtAmt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PACHEquivalentAmount2 }
     *     
     */
    public PACHEquivalentAmount2 getEqvtAmt() {
        return eqvtAmt;
    }

    /**
     * Legt den Wert der eqvtAmt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PACHEquivalentAmount2 }
     *     
     */
    public void setEqvtAmt(PACHEquivalentAmount2 value) {
        this.eqvtAmt = value;
    }

}
