//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aendrungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2015.11.02 um 01:30:28 PM CET 
//


package com.lp.server.schema.vendidata.commonobjects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer VirtualVendingmachine complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="VirtualVendingmachine">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="VvmId">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *               &lt;minInclusive value="0"/>
 *               &lt;maxInclusive value="2147483640"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="LegacyId" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="0"/>
 *               &lt;maxLength value="255"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="AllocationType" type="{http://www.vendidata.com/XML/Schema/CommonObjects}AllocationType"/>
 *         &lt;element name="OperationVariant" type="{http://www.vendidata.com/XML/Schema/CommonObjects}OperationVariant"/>
 *         &lt;element name="AdditionalFields" type="{http://www.vendidata.com/XML/Schema/CommonObjects}AdditionalFields" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VirtualVendingmachine", propOrder = {
    "vvmId",
    "legacyId",
    "allocationType",
    "operationVariant",
    "additionalFields"
})
public class XMLVirtualVendingmachine {

    @XmlElement(name = "VvmId")
    protected int vvmId;
    @XmlElement(name = "LegacyId")
    protected String legacyId;
    @XmlElement(name = "AllocationType", required = true)
    @XmlSchemaType(name = "string")
    protected XMLAllocationType allocationType;
    @XmlElement(name = "OperationVariant", required = true)
    @XmlSchemaType(name = "string")
    protected XMLOperationVariant operationVariant;
    @XmlElement(name = "AdditionalFields")
    protected XMLAdditionalFields additionalFields;

    /**
     * Ruft den Wert der vvmId-Eigenschaft ab.
     * 
     */
    public int getVvmId() {
        return vvmId;
    }

    /**
     * Legt den Wert der vvmId-Eigenschaft fest.
     * 
     */
    public void setVvmId(int value) {
        this.vvmId = value;
    }

    /**
     * Ruft den Wert der legacyId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLegacyId() {
        return legacyId;
    }

    /**
     * Legt den Wert der legacyId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLegacyId(String value) {
        this.legacyId = value;
    }

    /**
     * Ruft den Wert der allocationType-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLAllocationType }
     *     
     */
    public XMLAllocationType getAllocationType() {
        return allocationType;
    }

    /**
     * Legt den Wert der allocationType-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLAllocationType }
     *     
     */
    public void setAllocationType(XMLAllocationType value) {
        this.allocationType = value;
    }

    /**
     * Ruft den Wert der operationVariant-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLOperationVariant }
     *     
     */
    public XMLOperationVariant getOperationVariant() {
        return operationVariant;
    }

    /**
     * Legt den Wert der operationVariant-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLOperationVariant }
     *     
     */
    public void setOperationVariant(XMLOperationVariant value) {
        this.operationVariant = value;
    }

    /**
     * Ruft den Wert der additionalFields-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLAdditionalFields }
     *     
     */
    public XMLAdditionalFields getAdditionalFields() {
        return additionalFields;
    }

    /**
     * Legt den Wert der additionalFields-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLAdditionalFields }
     *     
     */
    public void setAdditionalFields(XMLAdditionalFields value) {
        this.additionalFields = value;
    }

}
