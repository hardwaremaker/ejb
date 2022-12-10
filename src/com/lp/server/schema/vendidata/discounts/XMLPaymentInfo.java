//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aendrungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2015.11.02 um 01:30:28 PM CET 
//


package com.lp.server.schema.vendidata.discounts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse fuer PaymentInfo complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="PaymentInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Total" type="{http://www.vendidata.com/XML/Schema/Discounts}Amount"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PaymentInfo", propOrder = {
    "total"
})
@XmlSeeAlso({
    XMLCombinedPaymentInfo.class,
    XMLFormulaPaymentInfo.class,
    XMLSettledPaymentInfo.class
})
public class XMLPaymentInfo {

    @XmlElement(name = "Total", required = true)
    protected XMLAmount total;

    /**
     * Ruft den Wert der total-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XMLAmount }
     *     
     */
    public XMLAmount getTotal() {
        return total;
    }

    /**
     * Legt den Wert der total-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLAmount }
     *     
     */
    public void setTotal(XMLAmount value) {
        this.total = value;
    }

}
