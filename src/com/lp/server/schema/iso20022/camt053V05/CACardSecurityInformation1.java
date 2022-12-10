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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer CardSecurityInformation1 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="CardSecurityInformation1">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CSCMgmt" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}CSCManagement1Code"/>
 *         &lt;element name="CSCVal" type="{urn:iso:std:iso:20022:tech:xsd:camt.053.001.05}Min3Max4NumericText" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CardSecurityInformation1", propOrder = {
    "cscMgmt",
    "cscVal"
})
public class CACardSecurityInformation1 {

    @XmlElement(name = "CSCMgmt", required = true)
    @XmlSchemaType(name = "string")
    protected CACSCManagement1Code cscMgmt;
    @XmlElement(name = "CSCVal")
    protected String cscVal;

    /**
     * Ruft den Wert der cscMgmt-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link CACSCManagement1Code }
     *     
     */
    public CACSCManagement1Code getCSCMgmt() {
        return cscMgmt;
    }

    /**
     * Legt den Wert der cscMgmt-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link CACSCManagement1Code }
     *     
     */
    public void setCSCMgmt(CACSCManagement1Code value) {
        this.cscMgmt = value;
    }

    /**
     * Ruft den Wert der cscVal-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCSCVal() {
        return cscVal;
    }

    /**
     * Legt den Wert der cscVal-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCSCVal(String value) {
        this.cscVal = value;
    }

}
