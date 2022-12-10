//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2016.04.11 um 10:19:37 AM CEST 
//


package com.lp.server.schema.vendidata.customers;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * This elements defines Additional Fieleds. For instance, Additional Fields can be attached to Customers, Vendingmachins or VirtualVendingmachines.
 * 
 * Additional fields are comprised by a maximum of 6 different values (Code 1 til Code 6).
 * 
 * <p>Java-Klasse fuer AdditionalFields complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="AdditionalFields">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Code1" type="{http://www.vendidata.com/XML/Schema/Customers}AdditionalFieldStringValue" minOccurs="0"/>
 *         &lt;element name="Code2" type="{http://www.vendidata.com/XML/Schema/Customers}AdditionalFieldStringValue" minOccurs="0"/>
 *         &lt;element name="Code3" type="{http://www.vendidata.com/XML/Schema/Customers}AdditionalFieldChoiceValue" minOccurs="0"/>
 *         &lt;element name="Code4" type="{http://www.vendidata.com/XML/Schema/Customers}AdditionalFieldChoiceValue" minOccurs="0"/>
 *         &lt;element name="Code5" type="{http://www.vendidata.com/XML/Schema/Customers}AdditionalFieldDoubleValue" minOccurs="0"/>
 *         &lt;element name="Code6" type="{http://www.vendidata.com/XML/Schema/Customers}AdditionalFieldDoubleValue" minOccurs="0"/>
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

    @XmlElement(name = "Code1")
    protected XMLAdditionalFieldStringValue code1;
    @XmlElement(name = "Code2")
    protected XMLAdditionalFieldStringValue code2;
    @XmlElement(name = "Code3")
    protected XMLAdditionalFieldChoiceValue code3;
    @XmlElement(name = "Code4")
    protected XMLAdditionalFieldChoiceValue code4;
    @XmlElement(name = "Code5")
    protected XMLAdditionalFieldDoubleValue code5;
    @XmlElement(name = "Code6")
    protected XMLAdditionalFieldDoubleValue code6;

    /**
     * Ruft den Wert der code1-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLAdditionalFieldStringValue }
     *     
     */
    public XMLAdditionalFieldStringValue getCode1() {
        return code1;
    }

    /**
     * Legt den Wert der code1-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLAdditionalFieldStringValue }
     *     
     */
    public void setCode1(XMLAdditionalFieldStringValue value) {
        this.code1 = value;
    }

    /**
     * Ruft den Wert der code2-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLAdditionalFieldStringValue }
     *     
     */
    public XMLAdditionalFieldStringValue getCode2() {
        return code2;
    }

    /**
     * Legt den Wert der code2-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLAdditionalFieldStringValue }
     *     
     */
    public void setCode2(XMLAdditionalFieldStringValue value) {
        this.code2 = value;
    }

    /**
     * Ruft den Wert der code3-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLAdditionalFieldChoiceValue }
     *     
     */
    public XMLAdditionalFieldChoiceValue getCode3() {
        return code3;
    }

    /**
     * Legt den Wert der code3-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLAdditionalFieldChoiceValue }
     *     
     */
    public void setCode3(XMLAdditionalFieldChoiceValue value) {
        this.code3 = value;
    }

    /**
     * Ruft den Wert der code4-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLAdditionalFieldChoiceValue }
     *     
     */
    public XMLAdditionalFieldChoiceValue getCode4() {
        return code4;
    }

    /**
     * Legt den Wert der code4-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLAdditionalFieldChoiceValue }
     *     
     */
    public void setCode4(XMLAdditionalFieldChoiceValue value) {
        this.code4 = value;
    }

    /**
     * Ruft den Wert der code5-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLAdditionalFieldDoubleValue }
     *     
     */
    public XMLAdditionalFieldDoubleValue getCode5() {
        return code5;
    }

    /**
     * Legt den Wert der code5-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLAdditionalFieldDoubleValue }
     *     
     */
    public void setCode5(XMLAdditionalFieldDoubleValue value) {
        this.code5 = value;
    }

    /**
     * Ruft den Wert der code6-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLAdditionalFieldDoubleValue }
     *     
     */
    public XMLAdditionalFieldDoubleValue getCode6() {
        return code6;
    }

    /**
     * Legt den Wert der code6-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLAdditionalFieldDoubleValue }
     *     
     */
    public void setCode6(XMLAdditionalFieldDoubleValue value) {
        this.code6 = value;
    }

}
