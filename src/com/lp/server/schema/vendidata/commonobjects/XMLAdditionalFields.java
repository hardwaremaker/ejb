//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aendrungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2015.11.02 um 01:30:28 PM CET 
//


package com.lp.server.schema.vendidata.commonobjects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer AdditionalFields complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="AdditionalFields">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="code1" type="{http://www.vendidata.com/XML/Schema/CommonObjects}AdditionalFieldsStringValue" minOccurs="0"/>
 *         &lt;element name="code2" type="{http://www.vendidata.com/XML/Schema/CommonObjects}AdditionalFieldsStringValue" minOccurs="0"/>
 *         &lt;element name="code3" type="{http://www.vendidata.com/XML/Schema/CommonObjects}AdditionalFieldsStringValue" minOccurs="0"/>
 *         &lt;element name="code4" type="{http://www.vendidata.com/XML/Schema/CommonObjects}AdditionalFieldsStringValue" minOccurs="0"/>
 *         &lt;element name="code5" type="{http://www.vendidata.com/XML/Schema/CommonObjects}AdditionalFieldsDoubleValue" minOccurs="0"/>
 *         &lt;element name="code6" type="{http://www.vendidata.com/XML/Schema/CommonObjects}AdditionalFieldsDoubleValue" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AdditionalFields", propOrder = {
    "code1",
    "code2",
    "code3",
    "code4",
    "code5",
    "code6"
})
public class XMLAdditionalFields {

    protected XMLAdditionalFieldsStringValue code1;
    protected XMLAdditionalFieldsStringValue code2;
    protected XMLAdditionalFieldsStringValue code3;
    protected XMLAdditionalFieldsStringValue code4;
    protected XMLAdditionalFieldsDoubleValue code5;
    protected XMLAdditionalFieldsDoubleValue code6;

    /**
     * Ruft den Wert der code1-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLAdditionalFieldsStringValue }
     *     
     */
    public XMLAdditionalFieldsStringValue getCode1() {
        return code1;
    }

    /**
     * Legt den Wert der code1-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLAdditionalFieldsStringValue }
     *     
     */
    public void setCode1(XMLAdditionalFieldsStringValue value) {
        this.code1 = value;
    }

    /**
     * Ruft den Wert der code2-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLAdditionalFieldsStringValue }
     *     
     */
    public XMLAdditionalFieldsStringValue getCode2() {
        return code2;
    }

    /**
     * Legt den Wert der code2-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLAdditionalFieldsStringValue }
     *     
     */
    public void setCode2(XMLAdditionalFieldsStringValue value) {
        this.code2 = value;
    }

    /**
     * Ruft den Wert der code3-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLAdditionalFieldsStringValue }
     *     
     */
    public XMLAdditionalFieldsStringValue getCode3() {
        return code3;
    }

    /**
     * Legt den Wert der code3-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLAdditionalFieldsStringValue }
     *     
     */
    public void setCode3(XMLAdditionalFieldsStringValue value) {
        this.code3 = value;
    }

    /**
     * Ruft den Wert der code4-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLAdditionalFieldsStringValue }
     *     
     */
    public XMLAdditionalFieldsStringValue getCode4() {
        return code4;
    }

    /**
     * Legt den Wert der code4-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLAdditionalFieldsStringValue }
     *     
     */
    public void setCode4(XMLAdditionalFieldsStringValue value) {
        this.code4 = value;
    }

    /**
     * Ruft den Wert der code5-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLAdditionalFieldsDoubleValue }
     *     
     */
    public XMLAdditionalFieldsDoubleValue getCode5() {
        return code5;
    }

    /**
     * Legt den Wert der code5-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLAdditionalFieldsDoubleValue }
     *     
     */
    public void setCode5(XMLAdditionalFieldsDoubleValue value) {
        this.code5 = value;
    }

    /**
     * Ruft den Wert der code6-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLAdditionalFieldsDoubleValue }
     *     
     */
    public XMLAdditionalFieldsDoubleValue getCode6() {
        return code6;
    }

    /**
     * Legt den Wert der code6-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLAdditionalFieldsDoubleValue }
     *     
     */
    public void setCode6(XMLAdditionalFieldsDoubleValue value) {
        this.code6 = value;
    }

}
