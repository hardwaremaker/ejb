//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �nderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2020.05.17 um 06:36:00 PM CEST 
//


package com.lp.server.schema.iso20022.camt053V04;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse f�r CorporateAction9 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="CorporateAction9">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="EvtTp" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}Max35Text"/>
 *         &lt;element name="EvtId" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.04}Max35Text"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CorporateAction9", propOrder = {
    "evtTp",
    "evtId"
})
public class CA04CorporateAction9 {

    @XmlElement(name = "EvtTp", required = true)
    protected String evtTp;
    @XmlElement(name = "EvtId", required = true)
    protected String evtId;

    /**
     * Ruft den Wert der evtTp-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEvtTp() {
        return evtTp;
    }

    /**
     * Legt den Wert der evtTp-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEvtTp(String value) {
        this.evtTp = value;
    }

    /**
     * Ruft den Wert der evtId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEvtId() {
        return evtId;
    }

    /**
     * Legt den Wert der evtId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEvtId(String value) {
        this.evtId = value;
    }

}
