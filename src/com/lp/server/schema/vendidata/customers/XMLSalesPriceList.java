//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2016.04.11 um 10:19:37 AM CEST 
//


package com.lp.server.schema.vendidata.customers;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer SalesPriceList complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="SalesPriceList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SalesPrice" type="{http://www.vendidata.com/XML/Schema/Customers}SalesPrice" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="updateMode" use="required" type="{http://www.vendidata.com/XML/Schema/Customers}UpdateMode" />
 *       &lt;attribute name="priceLineUpdateMode" type="{http://www.vendidata.com/XML/Schema/Customers}PriceLineUpdateMode" default="ADD" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SalesPriceList", propOrder = {
    "salesPrice"
})
public class XMLSalesPriceList {

    @XmlElement(name = "SalesPrice", required = true)
    protected List<XMLSalesPrice> salesPrice;
    @XmlAttribute(name = "updateMode", required = true)
    protected XMLUpdateMode updateMode;
    @XmlAttribute(name = "priceLineUpdateMode")
    protected XMLPriceLineUpdateMode priceLineUpdateMode;

    /**
     * Gets the value of the salesPrice property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the salesPrice property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSalesPrice().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XMLSalesPrice }
     * 
     * 
     */
    public List<XMLSalesPrice> getSalesPrice() {
        if (salesPrice == null) {
            salesPrice = new ArrayList<XMLSalesPrice>();
        }
        return this.salesPrice;
    }

    /**
     * Ruft den Wert der updateMode-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLUpdateMode }
     *     
     */
    public XMLUpdateMode getUpdateMode() {
        return updateMode;
    }

    /**
     * Legt den Wert der updateMode-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLUpdateMode }
     *     
     */
    public void setUpdateMode(XMLUpdateMode value) {
        this.updateMode = value;
    }

    /**
     * Ruft den Wert der priceLineUpdateMode-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLPriceLineUpdateMode }
     *     
     */
    public XMLPriceLineUpdateMode getPriceLineUpdateMode() {
        if (priceLineUpdateMode == null) {
            return XMLPriceLineUpdateMode.ADD;
        } else {
            return priceLineUpdateMode;
        }
    }

    /**
     * Legt den Wert der priceLineUpdateMode-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLPriceLineUpdateMode }
     *     
     */
    public void setPriceLineUpdateMode(XMLPriceLineUpdateMode value) {
        this.priceLineUpdateMode = value;
    }

}
