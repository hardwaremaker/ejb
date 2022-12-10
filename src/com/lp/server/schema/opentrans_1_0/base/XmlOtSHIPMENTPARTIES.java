//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2018.11.06 um 11:35:53 AM CET 
//


package com.lp.server.schema.opentrans_1_0.base;

import java.util.ArrayList;
import java.util.List;
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
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}DELIVERY_PARTY"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}FINAL_DELIVERY_PARTY" minOccurs="0"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}TRANSPORT_PARTY" maxOccurs="unbounded" minOccurs="0"/>
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
    "deliveryparty",
    "finaldeliveryparty",
    "transportparty"
})
@XmlRootElement(name = "SHIPMENT_PARTIES")
public class XmlOtSHIPMENTPARTIES {

    @XmlElement(name = "DELIVERY_PARTY", required = true)
    protected XmlOtDELIVERYPARTY deliveryparty;
    @XmlElement(name = "FINAL_DELIVERY_PARTY")
    protected XmlOtFINALDELIVERYPARTY finaldeliveryparty;
    @XmlElement(name = "TRANSPORT_PARTY")
    protected List<XmlOtTRANSPORTPARTY> transportparty;

    /**
     * Ruft den Wert der deliveryparty-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XmlOtDELIVERYPARTY }
     *     
     */
    public XmlOtDELIVERYPARTY getDELIVERYPARTY() {
        return deliveryparty;
    }

    /**
     * Legt den Wert der deliveryparty-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlOtDELIVERYPARTY }
     *     
     */
    public void setDELIVERYPARTY(XmlOtDELIVERYPARTY value) {
        this.deliveryparty = value;
    }

    /**
     * Ruft den Wert der finaldeliveryparty-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XmlOtFINALDELIVERYPARTY }
     *     
     */
    public XmlOtFINALDELIVERYPARTY getFINALDELIVERYPARTY() {
        return finaldeliveryparty;
    }

    /**
     * Legt den Wert der finaldeliveryparty-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlOtFINALDELIVERYPARTY }
     *     
     */
    public void setFINALDELIVERYPARTY(XmlOtFINALDELIVERYPARTY value) {
        this.finaldeliveryparty = value;
    }

    /**
     * Gets the value of the transportparty property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the transportparty property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTRANSPORTPARTY().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XmlOtTRANSPORTPARTY }
     * 
     * 
     */
    public List<XmlOtTRANSPORTPARTY> getTRANSPORTPARTY() {
        if (transportparty == null) {
            transportparty = new ArrayList<XmlOtTRANSPORTPARTY>();
        }
        return this.transportparty;
    }

}
