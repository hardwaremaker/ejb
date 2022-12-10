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
 * SalesPrice defines a sales price for exactly one article, the article has to be denominated either by it's ERPCode, Base- or SalesArticleNumber. Furthermore the value of the sales price can be specified, if not the price is taken from the default sales price from the article list. Additionaly an arbitrary count of PriceLines can be specified.
 * 
 * <p>Java-Klasse fuer SalesPrice complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="SalesPrice">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;element name="ERPCode">
 *             &lt;simpleType>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                 &lt;minLength value="1"/>
 *                 &lt;maxLength value="50"/>
 *               &lt;/restriction>
 *             &lt;/simpleType>
 *           &lt;/element>
 *           &lt;element name="SalesProductID">
 *             &lt;simpleType>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *                 &lt;minInclusive value="1"/>
 *                 &lt;maxInclusive value="2147483646"/>
 *               &lt;/restriction>
 *             &lt;/simpleType>
 *           &lt;/element>
 *           &lt;element name="BaseProductID">
 *             &lt;simpleType>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *                 &lt;minInclusive value="0"/>
 *                 &lt;maxInclusive value="2147483646"/>
 *               &lt;/restriction>
 *             &lt;/simpleType>
 *           &lt;/element>
 *         &lt;/choice>
 *         &lt;element name="SalesPriceValue" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}double">
 *               &lt;minInclusive value="0"/>
 *               &lt;maxInclusive value="1999999"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="PriceLines" type="{http://www.vendidata.com/XML/Schema/Customers}PriceLines" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SalesPrice", propOrder = {
    "erpCode",
    "salesProductID",
    "baseProductID",
    "salesPriceValue",
    "priceLines"
})
public class XMLSalesPrice {

    @XmlElement(name = "ERPCode")
    protected String erpCode;
    @XmlElement(name = "SalesProductID")
    protected Integer salesProductID;
    @XmlElement(name = "BaseProductID")
    protected Integer baseProductID;
    @XmlElement(name = "SalesPriceValue")
    protected Double salesPriceValue;
    @XmlElement(name = "PriceLines")
    protected XMLPriceLines priceLines;

    /**
     * Ruft den Wert der erpCode-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getERPCode() {
        return erpCode;
    }

    /**
     * Legt den Wert der erpCode-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setERPCode(String value) {
        this.erpCode = value;
    }

    /**
     * Ruft den Wert der salesProductID-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSalesProductID() {
        return salesProductID;
    }

    /**
     * Legt den Wert der salesProductID-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSalesProductID(Integer value) {
        this.salesProductID = value;
    }

    /**
     * Ruft den Wert der baseProductID-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getBaseProductID() {
        return baseProductID;
    }

    /**
     * Legt den Wert der baseProductID-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setBaseProductID(Integer value) {
        this.baseProductID = value;
    }

    /**
     * Ruft den Wert der salesPriceValue-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getSalesPriceValue() {
        return salesPriceValue;
    }

    /**
     * Legt den Wert der salesPriceValue-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setSalesPriceValue(Double value) {
        this.salesPriceValue = value;
    }

    /**
     * Ruft den Wert der priceLines-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLPriceLines }
     *     
     */
    public XMLPriceLines getPriceLines() {
        return priceLines;
    }

    /**
     * Legt den Wert der priceLines-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLPriceLines }
     *     
     */
    public void setPriceLines(XMLPriceLines value) {
        this.priceLines = value;
    }

}
