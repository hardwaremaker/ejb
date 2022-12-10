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
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer Customer complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="Customer">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RowId">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *               &lt;minInclusive value="0"/>
 *               &lt;maxInclusive value="2147483640"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="CustomerId">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *               &lt;minInclusive value="0"/>
 *               &lt;maxInclusive value="2147483640"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="FinancialAccountingId">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *               &lt;minInclusive value="0"/>
 *               &lt;maxInclusive value="2147483640"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
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
@XmlType(name = "Customer", propOrder = {
    "rowId",
    "customerId",
    "financialAccountingId",
    "additionalFields"
})
public class XMLCustomer {

    @XmlElement(name = "RowId")
    protected int rowId;
    @XmlElement(name = "CustomerId")
    protected int customerId;
    @XmlElement(name = "FinancialAccountingId")
    protected int financialAccountingId;
    @XmlElement(name = "AdditionalFields")
    protected XMLAdditionalFields additionalFields;

    /**
     * Ruft den Wert der rowId-Eigenschaft ab.
     * 
     */
    public int getRowId() {
        return rowId;
    }

    /**
     * Legt den Wert der rowId-Eigenschaft fest.
     * 
     */
    public void setRowId(int value) {
        this.rowId = value;
    }

    /**
     * Ruft den Wert der customerId-Eigenschaft ab.
     * 
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * Legt den Wert der customerId-Eigenschaft fest.
     * 
     */
    public void setCustomerId(int value) {
        this.customerId = value;
    }

    /**
     * Ruft den Wert der financialAccountingId-Eigenschaft ab.
     * 
     */
    public int getFinancialAccountingId() {
        return financialAccountingId;
    }

    /**
     * Legt den Wert der financialAccountingId-Eigenschaft fest.
     * 
     */
    public void setFinancialAccountingId(int value) {
        this.financialAccountingId = value;
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
