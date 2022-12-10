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
 * <p>Java-Klasse fuer SupplierInformation complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="SupplierInformation">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RowId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="SupplierId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="FinancialAccountingId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SupplierInformation", propOrder = {
    "rowId",
    "supplierId",
    "financialAccountingId",
    "name"
})
public class XMLSupplierInformation {

    @XmlElement(name = "RowId")
    protected int rowId;
    @XmlElement(name = "SupplierId")
    protected int supplierId;
    @XmlElement(name = "FinancialAccountingId")
    protected int financialAccountingId;
    @XmlElement(name = "Name", required = true)
    protected String name;

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
     * Ruft den Wert der supplierId-Eigenschaft ab.
     * 
     */
    public int getSupplierId() {
        return supplierId;
    }

    /**
     * Legt den Wert der supplierId-Eigenschaft fest.
     * 
     */
    public void setSupplierId(int value) {
        this.supplierId = value;
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
     * Ruft den Wert der name-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Legt den Wert der name-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

}
