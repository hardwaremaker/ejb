//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2018.11.06 um 11:35:53 AM CET 
//


package com.lp.server.schema.opentrans_1_0.base;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer anonymous complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}PARTY"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "party"
})
@XmlRootElement(name = "DELIVERY_PARTY")
public class XmlOtDELIVERYPARTY {

    @XmlElement(name = "PARTY", required = true)
    protected XmlOtPARTY party;

    /**
     * Ruft den Wert der party-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XmlOtPARTY }
     *     
     */
    public XmlOtPARTY getPARTY() {
        return party;
    }

    /**
     * Legt den Wert der party-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlOtPARTY }
     *     
     */
    public void setPARTY(XmlOtPARTY value) {
        this.party = value;
    }

}
