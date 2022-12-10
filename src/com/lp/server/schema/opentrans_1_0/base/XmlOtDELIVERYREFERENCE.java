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
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}DELIVERYNOTE_ID"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}LINE_ITEM_ID"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}DELIVERY_DATE" minOccurs="0"/>
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
    "deliverynoteid",
    "lineitemid",
    "deliverydate"
})
@XmlRootElement(name = "DELIVERY_REFERENCE")
public class XmlOtDELIVERYREFERENCE {

    @XmlElement(name = "DELIVERYNOTE_ID", required = true)
    protected String deliverynoteid;
    @XmlElement(name = "LINE_ITEM_ID", required = true)
    protected String lineitemid;
    @XmlElement(name = "DELIVERY_DATE")
    protected XmlOtDELIVERYDATE deliverydate;

    /**
     * Ruft den Wert der deliverynoteid-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDELIVERYNOTEID() {
        return deliverynoteid;
    }

    /**
     * Legt den Wert der deliverynoteid-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDELIVERYNOTEID(String value) {
        this.deliverynoteid = value;
    }

    /**
     * Ruft den Wert der lineitemid-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLINEITEMID() {
        return lineitemid;
    }

    /**
     * Legt den Wert der lineitemid-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLINEITEMID(String value) {
        this.lineitemid = value;
    }

    /**
     * Ruft den Wert der deliverydate-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XmlOtDELIVERYDATE }
     *     
     */
    public XmlOtDELIVERYDATE getDELIVERYDATE() {
        return deliverydate;
    }

    /**
     * Legt den Wert der deliverydate-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlOtDELIVERYDATE }
     *     
     */
    public void setDELIVERYDATE(XmlOtDELIVERYDATE value) {
        this.deliverydate = value;
    }

}
