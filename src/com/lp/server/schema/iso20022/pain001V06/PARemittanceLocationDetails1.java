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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer RemittanceLocationDetails1 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="RemittanceLocationDetails1">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Mtd" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.06}RemittanceLocationMethod2Code"/>
 *         &lt;element name="ElctrncAdr" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.06}Max2048Text" minOccurs="0"/>
 *         &lt;element name="PstlAdr" type="{urn:iso:std:iso:20022:tech:xsd:pain.001.001.06}NameAndAddress10" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RemittanceLocationDetails1", propOrder = {
    "mtd",
    "elctrncAdr",
    "pstlAdr"
})
public class PARemittanceLocationDetails1 {

    @XmlElement(name = "Mtd", required = true)
    @XmlSchemaType(name = "string")
    protected PARemittanceLocationMethod2Code mtd;
    @XmlElement(name = "ElctrncAdr")
    protected String elctrncAdr;
    @XmlElement(name = "PstlAdr")
    protected PANameAndAddress10 pstlAdr;

    /**
     * Ruft den Wert der mtd-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PARemittanceLocationMethod2Code }
     *     
     */
    public PARemittanceLocationMethod2Code getMtd() {
        return mtd;
    }

    /**
     * Legt den Wert der mtd-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PARemittanceLocationMethod2Code }
     *     
     */
    public void setMtd(PARemittanceLocationMethod2Code value) {
        this.mtd = value;
    }

    /**
     * Ruft den Wert der elctrncAdr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getElctrncAdr() {
        return elctrncAdr;
    }

    /**
     * Legt den Wert der elctrncAdr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setElctrncAdr(String value) {
        this.elctrncAdr = value;
    }

    /**
     * Ruft den Wert der pstlAdr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PANameAndAddress10 }
     *     
     */
    public PANameAndAddress10 getPstlAdr() {
        return pstlAdr;
    }

    /**
     * Legt den Wert der pstlAdr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PANameAndAddress10 }
     *     
     */
    public void setPstlAdr(PANameAndAddress10 value) {
        this.pstlAdr = value;
    }

}
