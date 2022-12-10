//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2017.02.21 um 11:13:49 AM CET 
//


package com.lp.server.schema.iso20022.pain008V02;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer RemittanceLocation2 complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="RemittanceLocation2">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RmtId" type="{urn:iso:std:iso:20022:tech:xsd:pain.008.001.02}Max35Text" minOccurs="0"/>
 *         &lt;element name="RmtLctnMtd" type="{urn:iso:std:iso:20022:tech:xsd:pain.008.001.02}RemittanceLocationMethod2Code" minOccurs="0"/>
 *         &lt;element name="RmtLctnElctrncAdr" type="{urn:iso:std:iso:20022:tech:xsd:pain.008.001.02}Max2048Text" minOccurs="0"/>
 *         &lt;element name="RmtLctnPstlAdr" type="{urn:iso:std:iso:20022:tech:xsd:pain.008.001.02}NameAndAddress10" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RemittanceLocation2", propOrder = {
    "rmtId",
    "rmtLctnMtd",
    "rmtLctnElctrncAdr",
    "rmtLctnPstlAdr"
})
public class PARemittanceLocation2 {

    @XmlElement(name = "RmtId")
    protected String rmtId;
    @XmlElement(name = "RmtLctnMtd")
    @XmlSchemaType(name = "string")
    protected PARemittanceLocationMethod2Code rmtLctnMtd;
    @XmlElement(name = "RmtLctnElctrncAdr")
    protected String rmtLctnElctrncAdr;
    @XmlElement(name = "RmtLctnPstlAdr")
    protected PANameAndAddress10 rmtLctnPstlAdr;

    /**
     * Ruft den Wert der rmtId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRmtId() {
        return rmtId;
    }

    /**
     * Legt den Wert der rmtId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRmtId(String value) {
        this.rmtId = value;
    }

    /**
     * Ruft den Wert der rmtLctnMtd-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PARemittanceLocationMethod2Code }
     *     
     */
    public PARemittanceLocationMethod2Code getRmtLctnMtd() {
        return rmtLctnMtd;
    }

    /**
     * Legt den Wert der rmtLctnMtd-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PARemittanceLocationMethod2Code }
     *     
     */
    public void setRmtLctnMtd(PARemittanceLocationMethod2Code value) {
        this.rmtLctnMtd = value;
    }

    /**
     * Ruft den Wert der rmtLctnElctrncAdr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRmtLctnElctrncAdr() {
        return rmtLctnElctrncAdr;
    }

    /**
     * Legt den Wert der rmtLctnElctrncAdr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRmtLctnElctrncAdr(String value) {
        this.rmtLctnElctrncAdr = value;
    }

    /**
     * Ruft den Wert der rmtLctnPstlAdr-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link PANameAndAddress10 }
     *     
     */
    public PANameAndAddress10 getRmtLctnPstlAdr() {
        return rmtLctnPstlAdr;
    }

    /**
     * Legt den Wert der rmtLctnPstlAdr-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link PANameAndAddress10 }
     *     
     */
    public void setRmtLctnPstlAdr(PANameAndAddress10 value) {
        this.rmtLctnPstlAdr = value;
    }

}
