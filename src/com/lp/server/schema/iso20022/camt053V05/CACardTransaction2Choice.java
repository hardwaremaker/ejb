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
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer CardTransaction2Choice complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="CardTransaction2Choice">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="Aggtd" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}CardAggregated1"/>
 *         &lt;element name="Indv" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}CardIndividualTransaction2"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CardTransaction2Choice", propOrder = {
    "aggtd",
    "indv"
})
public class CACardTransaction2Choice {

    @XmlElement(name = "Aggtd")
    protected CACardAggregated1 aggtd;
    @XmlElement(name = "Indv")
    protected CACardIndividualTransaction2 indv;

    /**
     * Ruft den Wert der aggtd-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CACardAggregated1 }
     *     
     */
    public CACardAggregated1 getAggtd() {
        return aggtd;
    }

    /**
     * Legt den Wert der aggtd-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CACardAggregated1 }
     *     
     */
    public void setAggtd(CACardAggregated1 value) {
        this.aggtd = value;
    }

    /**
     * Ruft den Wert der indv-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CACardIndividualTransaction2 }
     *     
     */
    public CACardIndividualTransaction2 getIndv() {
        return indv;
    }

    /**
     * Legt den Wert der indv-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CACardIndividualTransaction2 }
     *     
     */
    public void setIndv(CACardIndividualTransaction2 value) {
        this.indv = value;
    }

}
