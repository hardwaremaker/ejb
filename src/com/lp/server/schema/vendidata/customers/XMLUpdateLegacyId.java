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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * This element acts as a command for updating VVM LegacyId values prior to all other import actions.
 * 
 * <p>Java-Klasse fuer UpdateLegacyId complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="UpdateLegacyId">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="OldValue">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="255"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;choice>
 *           &lt;element name="newValue">
 *             &lt;simpleType>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                 &lt;minLength value="1"/>
 *                 &lt;maxLength value="255"/>
 *               &lt;/restriction>
 *             &lt;/simpleType>
 *           &lt;/element>
 *           &lt;element name="newValueNull" type="{http://www.vendidata.com/XML/Schema/Customers}UpdateLegacyIdNull"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UpdateLegacyId", propOrder = {
    "oldValue",
    "newValue",
    "newValueNull"
})
public class XMLUpdateLegacyId {

    @XmlElement(name = "OldValue", required = true)
    protected String oldValue;
    protected String newValue;
    @XmlSchemaType(name = "string")
    protected XMLUpdateLegacyIdNull newValueNull;

    /**
     * Ruft den Wert der oldValue-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOldValue() {
        return oldValue;
    }

    /**
     * Legt den Wert der oldValue-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOldValue(String value) {
        this.oldValue = value;
    }

    /**
     * Ruft den Wert der newValue-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNewValue() {
        return newValue;
    }

    /**
     * Legt den Wert der newValue-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNewValue(String value) {
        this.newValue = value;
    }

    /**
     * Ruft den Wert der newValueNull-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLUpdateLegacyIdNull }
     *     
     */
    public XMLUpdateLegacyIdNull getNewValueNull() {
        return newValueNull;
    }

    /**
     * Legt den Wert der newValueNull-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLUpdateLegacyIdNull }
     *     
     */
    public void setNewValueNull(XMLUpdateLegacyIdNull value) {
        this.newValueNull = value;
    }

}
