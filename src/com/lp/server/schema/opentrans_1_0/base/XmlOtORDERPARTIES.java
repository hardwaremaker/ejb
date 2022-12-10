//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2018.11.06 um 11:35:53 AM CET 
//


package com.lp.server.schema.opentrans_1_0.base;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer anonymous complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}BUYER_PARTY"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}SUPPLIER_PARTY"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}INVOICE_PARTY" minOccurs="0"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}SHIPMENT_PARTIES" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "buyerparty",
    "supplierparty",
    "invoiceparty",
    "shipmentparties"
})
@XmlRootElement(name = "ORDER_PARTIES")
public class XmlOtORDERPARTIES {

    @XmlElement(name = "BUYER_PARTY", required = true)
    protected XmlOtBUYERPARTY buyerparty;
    @XmlElement(name = "SUPPLIER_PARTY", required = true)
    protected XmlOtSUPPLIERPARTY supplierparty;
    @XmlElement(name = "INVOICE_PARTY")
    protected XmlOtINVOICEPARTY invoiceparty;
    @XmlElement(name = "SHIPMENT_PARTIES")
    protected XmlOtSHIPMENTPARTIES shipmentparties;

    /**
     * Ruft den Wert der buyerparty-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XmlOtBUYERPARTY }
     *     
     */
    public XmlOtBUYERPARTY getBUYERPARTY() {
        return buyerparty;
    }

    /**
     * Legt den Wert der buyerparty-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlOtBUYERPARTY }
     *     
     */
    public void setBUYERPARTY(XmlOtBUYERPARTY value) {
        this.buyerparty = value;
    }

    /**
     * Ruft den Wert der supplierparty-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XmlOtSUPPLIERPARTY }
     *     
     */
    public XmlOtSUPPLIERPARTY getSUPPLIERPARTY() {
        return supplierparty;
    }

    /**
     * Legt den Wert der supplierparty-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlOtSUPPLIERPARTY }
     *     
     */
    public void setSUPPLIERPARTY(XmlOtSUPPLIERPARTY value) {
        this.supplierparty = value;
    }

    /**
     * Ruft den Wert der invoiceparty-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XmlOtINVOICEPARTY }
     *     
     */
    public XmlOtINVOICEPARTY getINVOICEPARTY() {
        return invoiceparty;
    }

    /**
     * Legt den Wert der invoiceparty-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlOtINVOICEPARTY }
     *     
     */
    public void setINVOICEPARTY(XmlOtINVOICEPARTY value) {
        this.invoiceparty = value;
    }

    /**
     * Ruft den Wert der shipmentparties-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XmlOtSHIPMENTPARTIES }
     *     
     */
    public XmlOtSHIPMENTPARTIES getSHIPMENTPARTIES() {
        return shipmentparties;
    }

    /**
     * Legt den Wert der shipmentparties-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlOtSHIPMENTPARTIES }
     *     
     */
    public void setSHIPMENTPARTIES(XmlOtSHIPMENTPARTIES value) {
        this.shipmentparties = value;
    }

}
