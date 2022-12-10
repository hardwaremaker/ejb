//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2016.04.11 um 10:19:37 AM CEST 
//


package com.lp.server.schema.vendidata.customers;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
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
 *         &lt;element name="Rowid" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *               &lt;minInclusive value="1"/>
 *               &lt;maxInclusive value="999999999"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="LegacyId">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="1"/>
 *               &lt;maxLength value="255"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="OperationVariant" type="{http://www.vendidata.com/XML/Schema/Customers}OperationVariant"/>
 *         &lt;element name="AllocationType" type="{http://www.vendidata.com/XML/Schema/Customers}AllocationType"/>
 *         &lt;element name="AdditionalFields" type="{http://www.vendidata.com/XML/Schema/Customers}AdditionalFields" minOccurs="0"/>
 *         &lt;element name="SalesPriceList" type="{http://www.vendidata.com/XML/Schema/Customers}SalesPriceList" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="hasLocker" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VirtualVendingmachine", propOrder = {
    "rowid",
    "legacyId",
    "operationVariant",
    "allocationType",
    "additionalFields",
    "salesPriceList"
})
public class XMLVirtualVendingmachine {

    @XmlElement(name = "Rowid")
    protected Integer rowid;
    @XmlElement(name = "LegacyId", required = true)
    protected String legacyId;
    @XmlElement(name = "OperationVariant", required = true)
    @XmlSchemaType(name = "string")
    protected XMLOperationVariant operationVariant;
    @XmlElement(name = "AllocationType", required = true)
    @XmlSchemaType(name = "string")
    protected XMLAllocationType allocationType;
    @XmlElement(name = "AdditionalFields")
    protected XMLAdditionalFields additionalFields;
    @XmlElement(name = "SalesPriceList")
    protected XMLSalesPriceList salesPriceList;
    @XmlAttribute(name = "hasLocker")
    protected Boolean hasLocker;

    /**
     * Ruft den Wert der rowid-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getRowid() {
        return rowid;
    }

    /**
     * Legt den Wert der rowid-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setRowid(Integer value) {
        this.rowid = value;
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

    /**
     * Ruft den Wert der salesPriceList-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLSalesPriceList }
     *     
     */
    public XMLSalesPriceList getSalesPriceList() {
        return salesPriceList;
    }

    /**
     * Legt den Wert der salesPriceList-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLSalesPriceList }
     *     
     */
    public void setSalesPriceList(XMLSalesPriceList value) {
        this.salesPriceList = value;
    }

    /**
     * Ruft den Wert der hasLocker-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isHasLocker() {
        if (hasLocker == null) {
            return false;
        } else {
            return hasLocker;
        }
    }

    /**
     * Legt den Wert der hasLocker-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setHasLocker(Boolean value) {
        this.hasLocker = value;
    }

}
