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
 * <p>Java-Klasse fuer Rate3 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="Rate3">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Tp" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.02}RateType4Choice"/>
 *         &lt;element name="VldtyRg" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.02}CurrencyAndAmountRange2" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Rate3", propOrder = {
    "tp",
    "vldtyRg"
})
public class CARate3 {

    @XmlElement(name = "Tp", required = true)
    protected CARateType4Choice tp;
    @XmlElement(name = "VldtyRg")
    protected CACurrencyAndAmountRange2 vldtyRg;

    /**
     * Ruft den Wert der tp-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CARateType4Choice }
     *     
     */
    public CARateType4Choice getTp() {
        return tp;
    }

    /**
     * Legt den Wert der tp-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CARateType4Choice }
     *     
     */
    public void setTp(CARateType4Choice value) {
        this.tp = value;
    }

    /**
     * Ruft den Wert der vldtyRg-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CACurrencyAndAmountRange2 }
     *     
     */
    public CACurrencyAndAmountRange2 getVldtyRg() {
        return vldtyRg;
    }

    /**
     * Legt den Wert der vldtyRg-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CACurrencyAndAmountRange2 }
     *     
     */
    public void setVldtyRg(CACurrencyAndAmountRange2 value) {
        this.vldtyRg = value;
    }

}
