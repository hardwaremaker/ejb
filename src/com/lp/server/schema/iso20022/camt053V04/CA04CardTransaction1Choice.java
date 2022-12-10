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
 * <p>Java-Klasse für CardTransaction1Choice complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="CardTransaction1Choice">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="Aggtd" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}CardAggregated1"/>
 *         &lt;element name="Indv" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}CardIndividualTransaction1"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CardTransaction1Choice", propOrder = {
    "aggtd",
    "indv"
})
public class CA04CardTransaction1Choice {

    @XmlElement(name = "Aggtd")
    protected CA04CardAggregated1 aggtd;
    @XmlElement(name = "Indv")
    protected CA04CardIndividualTransaction1 indv;

    /**
     * Ruft den Wert der aggtd-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04CardAggregated1 }
     *     
     */
    public CA04CardAggregated1 getAggtd() {
        return aggtd;
    }

    /**
     * Legt den Wert der aggtd-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04CardAggregated1 }
     *     
     */
    public void setAggtd(CA04CardAggregated1 value) {
        this.aggtd = value;
    }

    /**
     * Ruft den Wert der indv-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CA04CardIndividualTransaction1 }
     *     
     */
    public CA04CardIndividualTransaction1 getIndv() {
        return indv;
    }

    /**
     * Legt den Wert der indv-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CA04CardIndividualTransaction1 }
     *     
     */
    public void setIndv(CA04CardIndividualTransaction1 value) {
        this.indv = value;
    }

}
