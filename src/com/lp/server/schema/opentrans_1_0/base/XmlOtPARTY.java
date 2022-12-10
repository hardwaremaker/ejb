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
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}PARTY_ID" minOccurs="0"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}ADDRESS" minOccurs="0"/>
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
    "partyid",
    "address"
})
@XmlRootElement(name = "PARTY")
public class XmlOtPARTY {

    @XmlElement(name = "PARTY_ID")
    protected XmlOtPARTYID partyid;
    @XmlElement(name = "ADDRESS")
    protected XmlOtADDRESS address;

    /**
     * Ruft den Wert der partyid-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XmlOtPARTYID }
     *     
     */
    public XmlOtPARTYID getPARTYID() {
        return partyid;
    }

    /**
     * Legt den Wert der partyid-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlOtPARTYID }
     *     
     */
    public void setPARTYID(XmlOtPARTYID value) {
        this.partyid = value;
    }

    /**
     * Ruft den Wert der address-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XmlOtADDRESS }
     *     
     */
    public XmlOtADDRESS getADDRESS() {
        return address;
    }

    /**
     * Legt den Wert der address-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlOtADDRESS }
     *     
     */
    public void setADDRESS(XmlOtADDRESS value) {
        this.address = value;
    }

}
