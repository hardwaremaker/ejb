//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2018.11.06 um 11:35:53 AM CET 
//


package com.lp.server.schema.opentrans_1_0.order;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.lp.server.schema.opentrans_1_0.base.XmlOtCONTROLINFO;
import com.lp.server.schema.opentrans_1_0.base.XmlOtSOURCINGINFO;


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
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}CONTROL_INFO" minOccurs="0"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}SOURCING_INFO" minOccurs="0"/>
 *         &lt;element ref="{http://www.opentrans.org/XMLSchema/1.0}ORDER_INFO"/>
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
    "controlinfo",
    "sourcinginfo",
    "orderinfo"
})
@XmlRootElement(name = "ORDER_HEADER")
public class XmlOtORDERHEADER {

    @XmlElement(name = "CONTROL_INFO")
    protected XmlOtCONTROLINFO controlinfo;
    @XmlElement(name = "SOURCING_INFO")
    protected XmlOtSOURCINGINFO sourcinginfo;
    @XmlElement(name = "ORDER_INFO", required = true)
    protected XmlOtORDERINFO orderinfo;

    /**
     * Ruft den Wert der controlinfo-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XmlOtCONTROLINFO }
     *     
     */
    public XmlOtCONTROLINFO getCONTROLINFO() {
        return controlinfo;
    }

    /**
     * Legt den Wert der controlinfo-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlOtCONTROLINFO }
     *     
     */
    public void setCONTROLINFO(XmlOtCONTROLINFO value) {
        this.controlinfo = value;
    }

    /**
     * Ruft den Wert der sourcinginfo-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XmlOtSOURCINGINFO }
     *     
     */
    public XmlOtSOURCINGINFO getSOURCINGINFO() {
        return sourcinginfo;
    }

    /**
     * Legt den Wert der sourcinginfo-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlOtSOURCINGINFO }
     *     
     */
    public void setSOURCINGINFO(XmlOtSOURCINGINFO value) {
        this.sourcinginfo = value;
    }

    /**
     * Ruft den Wert der orderinfo-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link XmlOtORDERINFO }
     *     
     */
    public XmlOtORDERINFO getORDERINFO() {
        return orderinfo;
    }

    /**
     * Legt den Wert der orderinfo-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlOtORDERINFO }
     *     
     */
    public void setORDERINFO(XmlOtORDERINFO value) {
        this.orderinfo = value;
    }

}
